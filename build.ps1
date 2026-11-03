param([Parameter(ValueFromRemainingArguments=$true)][string[]]$GradleArguments)
$ErrorActionPreference = 'Stop'
$root = $PSScriptRoot
$homesFile = Join-Path $root '.jdks/homes.properties'
$needsSetup = !(Test-Path -LiteralPath $homesFile)
if (!$needsSetup) {
    $entries = @(Get-Content -LiteralPath $homesFile -Encoding UTF8 | Where-Object { $_ -match '^jdk(7|8|17)=' })
    if ($entries.Count -ne 3) { $needsSetup = $true }
    foreach ($entry in $entries) {
        $selectedPath = $entry.Split('=',2)[1]
        if (!(Test-Path -LiteralPath (Join-Path $selectedPath 'bin/java.exe')) -or
            !(Test-Path -LiteralPath (Join-Path $selectedPath 'bin/javac.exe'))) { $needsSetup = $true }
    }
}
if ($needsSetup) { & (Join-Path $root 'setup.ps1') }
$jdk17 = Get-Content -LiteralPath $homesFile -Encoding UTF8 | Where-Object { $_.StartsWith('jdk17=') } | ForEach-Object { $_.Substring(6) }
if (!$jdk17 -or !(Test-Path -LiteralPath (Join-Path $jdk17 'bin/java.exe'))) { throw 'Run ./setup.ps1 to configure JDK 17.' }
$previousJava = $env:JAVA_HOME
$previousPath = $env:PATH
Push-Location $root
try {
    $env:JAVA_HOME = $jdk17
    $env:PATH = (Join-Path $jdk17 'bin') + [IO.Path]::PathSeparator + $env:PATH
    & (Join-Path $root 'gradlew.bat') @GradleArguments
    $result = $LASTEXITCODE
} finally {
    Pop-Location
    $env:JAVA_HOME = $previousJava
    $env:PATH = $previousPath
}
exit $result
