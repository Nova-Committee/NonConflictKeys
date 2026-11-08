#!/usr/bin/env bash
set -euo pipefail
root=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)
[[ $(uname -s) == Linux && $(uname -m) == x86_64 ]] || { echo 'Supported platform: Linux x86_64.' >&2; exit 1; }
download=false
case ${1:-} in '') ;; --download) download=true ;; *) echo 'Usage: ./setup.sh [--download]' >&2; exit 1 ;; esac
getconf GNU_LIBC_VERSION >/dev/null 2>&1 || { echo 'The pinned Linux JDKs require glibc.' >&2; exit 1; }
for command in curl tar sha256sum find awk readlink; do command -v "$command" >/dev/null || { echo "Missing command: $command" >&2; exit 1; }; done
mkdir -p "$root/.jdks/.downloads"

valid_jdk() {
    local jdk_home=$1 major=$2 version
    [[ -f $jdk_home/release && -x $jdk_home/bin/java && -x $jdk_home/bin/javac ]] || return 1
    grep -Eq '^OS_ARCH="(amd64|x86_64)"' "$jdk_home/release" || return 1
    version=$(sed -n 's/^JAVA_VERSION="\([^"]*\)".*/\1/p' "$jdk_home/release")
    case $major in 7|8) [[ $version == 1.$major.* ]] || return 1 ;; *) [[ $version == "$major".* ]] || return 1 ;; esac
    [[ $major != 7 || -f $jdk_home/jre/lib/rt.jar ]] || return 1
    return 0
}

find_jdk() {
    local major=$1 candidate name executable
    local candidates=("$root/.jdks/$major")
    for name in "JAVA${major}_HOME" "JDK${major}" JAVA_HOME; do
        candidate=${!name:-}; [[ -z $candidate ]] || candidates+=("$candidate")
    done
    executable=$(command -v java || true)
    [[ -z $executable ]] || candidates+=("$(dirname "$(dirname "$(readlink -f "$executable")")")")
    for candidate in /usr/lib/jvm/* /opt/java/* /opt/jdks/* "$HOME"/.jdks/* "$HOME"/.sdkman/candidates/java/* "$HOME"/.gradle/jdks/* "$HOME"/.gradle/jdks/*/*; do
        [[ ! -d $candidate ]] || candidates+=("$candidate")
    done
    for candidate in "${candidates[@]}"; do
        if valid_jdk "$candidate" "$major"; then (cd -- "$candidate" && pwd -P); return; fi
    done
    return 1
}

install_jdk() {
    local major=$1 row platform number version url sha archive stage jdk_home
    if valid_jdk "$root/.jdks/$major" "$major"; then echo "$root/.jdks/$major"; return; fi
    [[ ! -e $root/.jdks/$major ]] || { echo "Invalid JDK directory: $root/.jdks/$major" >&2; return 1; }
    row=$(awk -F '\t' -v major="$major" '$1=="linux-x64" && $2==major' "$root/gradle/jdks.lock.tsv")
    [[ -n $row ]] || { echo "No pinned Linux JDK $major package" >&2; return 1; }
    IFS=$'\t' read -r platform number version url sha <<< "$row"
    archive="$root/.jdks/.downloads/${url##*/}"
    if [[ ! -f $archive ]]; then
        echo "Downloading JDK $major ($version)..." >&2
        curl --fail --location --retry 3 --output "$archive.part" "$url" || return 1
        printf '%s  %s\n' "$sha" "$archive.part" | sha256sum --check --status || { echo "Checksum mismatch: $archive.part" >&2; return 1; }
        mv -- "$archive.part" "$archive" || return 1
    fi
    printf '%s  %s\n' "$sha" "$archive" | sha256sum --check --status || { echo "Checksum mismatch: $archive" >&2; return 1; }
    stage=$(mktemp -d "$root/.jdks/.stage-$major-XXXXXX")
    tar -xzf "$archive" -C "$stage" || return 1
    jdk_home=$(find "$stage" -mindepth 2 -maxdepth 2 -name release -type f -printf '%h\n')
    [[ -n $jdk_home && $jdk_home != *$'\n'* ]] && valid_jdk "$jdk_home" "$major" || { echo 'Invalid downloaded JDK layout' >&2; return 1; }
    mv -- "$jdk_home" "$root/.jdks/$major" || return 1
    rmdir -- "$stage" || return 1
    echo "$root/.jdks/$major"
}

declare -A homes
for major in 7 8 17; do
    if ! $download && jdk_home=$(find_jdk "$major"); then :; else jdk_home=$(install_jdk "$major"); fi
    homes[$major]=$jdk_home
    echo "JDK $major: $jdk_home"
done
printf 'jdk7=%s\njdk8=%s\njdk17=%s\n' "${homes[7]}" "${homes[8]}" "${homes[17]}" > "$root/.jdks/homes.properties"
printf 'java7Home=%s\norg.gradle.java.installations.paths=%s,%s,%s\n' "${homes[7]}" "${homes[7]}" "${homes[8]}" "${homes[17]}" > "$root/gradle.local.properties"

echo 'Ready. Run ./build.sh build genIdeaRuns, or open the project in IntelliJ IDEA.'
