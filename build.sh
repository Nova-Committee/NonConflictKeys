#!/usr/bin/env bash
set -euo pipefail
root=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)
needs_setup=false
if [[ ! -f $root/.jdks/homes.properties ]]; then
    needs_setup=true
else
    count=0
    while IFS='=' read -r key value; do
        case $key in jdk7|jdk8|jdk17)
            count=$((count + 1))
            [[ -x $value/bin/java && -x $value/bin/javac ]] || needs_setup=true
        ;; esac
    done < "$root/.jdks/homes.properties"
    [[ $count == 3 ]] || needs_setup=true
fi
if $needs_setup; then "$root/setup.sh"; fi
while IFS='=' read -r key value; do
    if [[ $key == jdk17 ]]; then export JAVA_HOME=$value; fi
done < "$root/.jdks/homes.properties"
[[ -x ${JAVA_HOME:-}/bin/java ]] || { echo 'Run ./setup.sh to configure JDK 17.' >&2; exit 1; }
export PATH="$JAVA_HOME/bin:$PATH"
cd -- "$root"
exec "$root/gradlew" "$@"
