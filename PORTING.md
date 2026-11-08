# Porting notes (Fork: Sekai0NI0itamio/NonConflictKeys)

Original mod: **NonConflictKeys by Tapio / Nova-Committee**
Upstream: https://github.com/Nova-Committee/NonConflictKeys
License: **GNU AGPL-3.0-only** (see LICENSE / COPYING.md — unchanged in this fork)

This fork does not claim original authorship. It only updates
Minecraft / loader / build versions so the mod runs on newer releases.
All original code, authorship and license headers are preserved.
Upstream `gradle.properties` / `fabric.mod.json` listed GPL/LGPL;
corrected to the actual `AGPL-3.0-only` to match the LICENSE file.

## Branches

- `NeoForge/26.3` — this branch. Based on upstream `neoforge-dev` (1.20.2).
  NeoForge 26.3.0.101, ModDevGradle 2.0.148, Java 25, Mojang official mappings.
  pack_format min/max [92,0]/[113,1] is extrapolated from LightSpeedRe's
  26.1.2 [84,0]/[101,1] and 26.2 [88,0]/[107,1] — it does not affect compilation.
  No Java changes planned: KeyMapping.click / KeyMappingLookup API was
  unchanged 1.19.2 Forge → 1.20.2 NeoForge upstream.
- `Fabric/1.20.1` — Loom 1.4.1, Yarn 1.20.1+build.10, Loader 0.14.25.
- `Forge/1.20.1` — Forge 47.4.20, official mappings 1.20.1.
- `Fabric/26.3` — Fabric on 26.x uses Mojang mappings (Yarn ends at 1.21.11),
  so that branch needs import/field rewrites; see its PORTING.md.

## Builds

**Never build locally.** All compilation happens in GitHub Actions:
`.github/workflows/build.yml` runs `./gradlew build` on push / PR /
workflow_dispatch and uploads the jar as an artifact.
