# Porting notes (Fork: Sekai0NI0itamio/NonConflictKeys)

Original mod: **NonConflictKeys by Tapio / Nova-Committee**
Upstream: https://github.com/Nova-Committee/NonConflictKeys
License: **GNU AGPL-3.0-only** (see LICENSE / COPYING.md — unchanged in this fork)

This fork does not claim original authorship. It only updates
Minecraft / loader / build versions so the mod runs on newer releases.
All original code, authorship and license headers are preserved.
`fabric.mod.json` previously listed `LGPL-3.0`; corrected to the
actual `AGPL-3.0-only` to match the LICENSE file.

## Branches

- `Forge/1.20.1` — this branch. Forge 47.4.20, official mappings 1.20.1, Java 17. No Java changes: KeyMapping/KeyMappingLookup API unchanged (same code ships on upstream neoforge-dev for 1.20.2).
- `Fabric/1.20.1` — Fabric Loom 1.4.1, Yarn 1.20.1+build.10, Loader 0.14.25, Gradle 8.3, Java 17. Fabric Loom 1.4.1, Yarn 1.20.1+build.10,
  Loader 0.14.25, Gradle 8.3, Java 17. No Java source changes were needed:
  the 1.19.x `KeyBinding` constructor / `onKeyPressed` / `setKeyPressed` /
  `updateKeysByCode` signatures are unchanged in 1.20.1 (confirmed against
  Nova-Committee's own ModernKeyBinding Fabric/1.20.1 branch, which ships
  the same feature).
- `Forge/1.20.1` — ForgeGradle, Forge 47.4.20, official mappings 1.20.1.
- `Fabric/26.3`, `NeoForge/26.3` — see those branches.

## Builds

**Never build locally.** All compilation happens in GitHub Actions:
`.github/workflows/build.yml` runs `./gradlew build` on push / PR /
workflow_dispatch and uploads the jar as an artifact.
