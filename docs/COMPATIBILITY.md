# Compatibility

| Component | Supported baseline |
| --- | --- |
| Retrospectum Loom | 0.2.1 |
| Retrospectum Mixin | 0.2.0, Mixin 0.8.7 |
| Minecraft / Forge | 1.6.4 / 9.11.1.1345 |
| Gradle / build JVM | 8.14.6 / JDK 17 |
| Compiler and annotation processor | JDK 8 |
| Mod bytecode / target API | Java 6 / Java 7 |
| Game runtime | Java 7 or 8 |

The mod version (`1.0.0`) is independent of the toolchain version.
Configuration cache and remote caching of remapped artifacts are not supported.

## Migrating from 0.1.0

- Rebuild mods with the standard `MixinConfigs` manifest attribute, or update
  their manifests. The former `RetroMixinConfigs` attribute has no alias.
- Enable `maven-publish` explicitly and publish `components.java` to expose the
  SRG release archive. Development Minecraft/Forge dependencies are not published.
- Cache locks now live under the Gradle user cache's `retrospectum-loom/locks`
  directory. Builds using the old lock protocol must not share caches concurrently.

The development property remains `retromixin.mixin.configs`.
`IMixinConfigPlugin` implementations use
`dev.desiderium.retro.mixin.internal.asm.tree.ClassNode` in their method signatures.
