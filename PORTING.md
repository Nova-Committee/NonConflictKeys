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

- `Fabric/26.3` — this branch. Loom 1.18.2, Mojang mappings (26.x is
  unobfuscated, so no mappings line), Loader 0.20.1, Gradle 9.7.1, Java 25.
  Yarn ends at 1.21.11, so Java was rewritten to Mojang names:
  KeyBinding→KeyMapping, InputUtil→InputConstants, boundKey→key,
  timesPressed→clickCount, KEYS_BY_ID→ALL, onKeyPressed→click,
  setKeyPressed→set/setDown, updateKeysByCode→resetMapping, constructor
  category String→KeyMapping.Category (verified against 26.2 KeyMapping.java).
  NOTE: vanilla 26.x already keeps a MAP multimap and fires all same-key
  bindings; this port faithfully replicates the original logic explicitly.
- `Fabric/1.20.1` — Loom 1.4.1, Yarn 1.20.1+build.10, Loader 0.14.25,
  Gradle 8.3, Java 17. No Java source changes were needed: the 1.19.x
  `KeyBinding` constructor / `onKeyPressed` / `setKeyPressed` /
  `updateKeysByCode` signatures are unchanged in 1.20.1 (confirmed against
  Nova-Committee's own ModernKeyBinding Fabric/1.20.1 branch, which ships
  the same feature).
- `Forge/1.20.1` — ForgeGradle, Forge 47.4.20, official mappings 1.20.1.
- `NeoForge/26.3` — see that branch.

## Builds

**Never build locally.** All compilation happens in GitHub Actions:
`.github/workflows/build.yml` runs `./gradlew build` on push / PR /
workflow_dispatch and uploads the jar as an artifact.
