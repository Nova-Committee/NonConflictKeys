# Non Conflict Keys

A Minecraft Forge 1.6.4 mod that lets several key bindings share the same key.
Pressing that key triggers every binding assigned to it: bind *Forward* and
*Strafe Left* to `W` and holding `W` moves diagonally.

## Features

- Every binding on a pressed key activates together.
- Uses the key currently assigned in Controls or `options.txt`.

## Installation

1. Install Minecraft Forge 9.11.1.1345 for Minecraft 1.6.4.
2. Install [Retrospectum Mixin](https://github.com/DesideriumMC/RetrospectumMixin).
3. Place `nckeys-retrospectum-1.6.4-<version>.jar` in `mods/`.

## Building

JDK 17 runs Gradle, JDK 8 compiles and JDK 7 runs the game. The setup scripts
locate or download the required JDKs and record the selection in
`.jdks/homes.properties`.

```powershell
# Windows
./setup.ps1
./build.ps1 build
./build.ps1 runClient
```

```sh
# Linux x86_64
./setup.sh
./build.sh build
./build.sh runClient
```

Artifacts are written to `build/libs/`:

- `nckeys-retrospectum-1.6.4-<version>-dev.jar` for `runClient`;
- `nckeys-retrospectum-1.6.4-<version>.jar`, the SRG release jar.

## Development

The project uses [Retrospectum Loom](https://github.com/DesideriumMC/RetrospectumLoom)
and [Retrospectum Mixin](https://github.com/DesideriumMC/RetrospectumMixin) from
the [Retrospectum Mod Template](https://github.com/DesideriumMC/RetrospectumModTemplate).

Mod metadata lives in `gradle.properties`. `MixinKeyBinding` handles input by
updating every binding whose current `keyCode` matches the pressed key, in both
`setKeyBindState` and `onTick`.

## License

LGPL-3.0-only. See [LICENSE](LICENSE).
