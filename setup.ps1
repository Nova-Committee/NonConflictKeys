param([switch]$Download)
$ErrorActionPreference = 'Stop'
$root = $PSScriptRoot
if ($env:OS -ne 'Windows_NT' -or ![Environment]::Is64BitOperatingSystem -or
    (($env:PROCESSOR_ARCHITECTURE -ne 'AMD64') -and ($env:PROCESSOR_ARCHITEW6432 -ne 'AMD64'))) {
    throw 'Supported platform: Windows x64.'
}
$jdkRoot = Join-Path $root '.jdks'
New-Item -ItemType Directory -Force -Path (Join-Path $jdkRoot '.downloads') | Out-Null
$utf8 = New-Object System.Text.UTF8Encoding($false)

function Test-Jdk([string]$HomePath, [int]$Major) {
    if (!$HomePath -or !(Test-Path -LiteralPath (Join-Path $HomePath 'release')) -or
        !(Test-Path -LiteralPath (Join-Path $HomePath 'bin/java.exe')) -or
        !(Test-Path -LiteralPath (Join-Path $HomePath 'bin/javac.exe'))) { return $false }
    $release = [IO.File]::ReadAllText((Join-Path $HomePath 'release'))
    if ($release -notmatch '(?m)^OS_ARCH="(amd64|x86_64)"') { return $false }
    $prefix = if ($Major -lt 9) { "1.$Major." } else { "$Major." }
    if ($release -notmatch ('(?m)^JAVA_VERSION="' + [regex]::Escape($prefix))) { return $false }
    if ($Major -eq 7 -and !(Test-Path -LiteralPath (Join-Path $HomePath 'jre/lib/rt.jar'))) { return $false }
    return $true
}

function Find-Jdk([int]$Major) {
    $candidates = New-Object 'System.Collections.Generic.List[string]'
    $candidates.Add((Join-Path $jdkRoot "$Major"))
    foreach ($name in @("JAVA${Major}_HOME", "JDK$Major", 'JAVA_HOME')) {
        $value = [Environment]::GetEnvironmentVariable($name)
        if ($value) { $candidates.Add($value) }
    }
    $roots = @((Join-Path $env:USERPROFILE '.jdks'), (Join-Path $env:USERPROFILE '.gradle/jdks'))
    foreach ($parent in @($env:ProgramFiles, ${env:ProgramFiles(x86)})) {
        if ($parent) {
            foreach ($vendor in @('Java','Eclipse Adoptium','Zulu','Microsoft','OpenJDK')) { $roots += Join-Path $parent $vendor }
        }
    }
    foreach ($directory in $roots) {
        if (Test-Path -LiteralPath $directory) {
            Get-ChildItem -LiteralPath $directory -Directory -Depth 1 -ErrorAction SilentlyContinue |
                ForEach-Object { $candidates.Add($_.FullName) }
        }
    }
    foreach ($candidate in $candidates) {
        if (Test-Jdk $candidate $Major) { return [IO.Path]::GetFullPath($candidate) }
    }
    return $null
}

function Assert-ProjectPath([string]$Path) {
    $resolved = [IO.Path]::GetFullPath($Path)
    $prefix = [IO.Path]::GetFullPath($jdkRoot).TrimEnd('\','/') + [IO.Path]::DirectorySeparatorChar
    if (!$resolved.StartsWith($prefix, [StringComparison]::OrdinalIgnoreCase)) { throw "JDK path escapes project: $resolved" }
}

function Install-Jdk([int]$Major) {
    $destination = Join-Path $jdkRoot "$Major"
    if (Test-Jdk $destination $Major) { return $destination }
    if (Test-Path -LiteralPath $destination) { throw "Invalid JDK directory: $destination" }
    $package = Import-Csv -LiteralPath (Join-Path $root 'gradle/jdks.lock.tsv') -Delimiter "`t" |
        Where-Object { $_.platform -eq 'windows-x64' -and [int]$_.major -eq $Major }
    if (@($package).Count -ne 1) { throw "No pinned Windows JDK $Major package" }
    $archive = Join-Path $jdkRoot ('.downloads/' + [IO.Path]::GetFileName(([Uri]$package.url).AbsolutePath))
    if (!(Test-Path -LiteralPath $archive)) {
        Write-Host "Downloading JDK $Major ($($package.version))..."
        $previousProtocol = [Net.ServicePointManager]::SecurityProtocol
        [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
        $previousProgress = $ProgressPreference
        try { $ProgressPreference = 'SilentlyContinue'; Invoke-WebRequest -UseBasicParsing -Uri $package.url -OutFile "$archive.part" }
        finally { $ProgressPreference = $previousProgress; [Net.ServicePointManager]::SecurityProtocol = $previousProtocol }
        if ((Get-FileHash -LiteralPath "$archive.part" -Algorithm SHA256).Hash -ne $package.sha256) { throw "Checksum mismatch: $archive.part" }
        Assert-ProjectPath "$archive.part"; Assert-ProjectPath $archive
        Move-Item -LiteralPath "$archive.part" -Destination $archive
    }
    if ((Get-FileHash -LiteralPath $archive -Algorithm SHA256).Hash -ne $package.sha256) { throw "Checksum mismatch: $archive" }
    $stage = Join-Path $jdkRoot ('.stage-' + $Major + '-' + [Guid]::NewGuid().ToString('N'))
    Assert-ProjectPath $stage
    try {
        Expand-Archive -LiteralPath $archive -DestinationPath $stage
        $homes = @(Get-ChildItem -LiteralPath $stage -Directory | Where-Object { Test-Jdk $_.FullName $Major })
        if ($homes.Count -ne 1) { throw 'Invalid downloaded JDK layout' }
        Assert-ProjectPath $homes[0].FullName; Assert-ProjectPath $destination
        Move-Item -LiteralPath $homes[0].FullName -Destination $destination
    } finally {
        if (Test-Path -LiteralPath $stage) { Assert-ProjectPath $stage; Remove-Item -LiteralPath $stage -Recurse -Force }
    }
    return $destination
}

$homes = @{}
foreach ($major in @(7,8,17)) {
    $selected = if (!$Download) { Find-Jdk $major } else { $null }
    if (!$selected) { $selected = Install-Jdk $major }
    $homes[$major] = $selected.Replace('\','/')
    Write-Host "JDK ${major}: $selected"
}
[IO.File]::WriteAllText((Join-Path $jdkRoot 'homes.properties'), "jdk7=$($homes[7])`njdk8=$($homes[8])`njdk17=$($homes[17])`n", $utf8)
[IO.File]::WriteAllText((Join-Path $root 'gradle.local.properties'), "java7Home=$($homes[7])`norg.gradle.java.installations.paths=$($homes[7]),$($homes[8]),$($homes[17])`n", $utf8)
Write-Host 'Ready. Run ./build.ps1 build genIdeaRuns, or open the project in IntelliJ IDEA.'
