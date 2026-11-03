# Bundled build tools

- `loom-maven/`: [Retrospectum Loom 0.1.0-alpha.1](https://github.com/DesideriumMC/RetrospectumLoom/releases/tag/v0.1.0-alpha.1), packaged as a local Maven repository.
- `retrospectum-mixin-0.1.0-alpha.1.jar`: [Retrospectum Mixin 0.1.0-alpha.1](https://github.com/DesideriumMC/RetrospectumMixin/releases/tag/v0.1.0-alpha.1).

These released artifacts are included so repositories created from this template
can use them immediately. Configure `loom_version` and `mixin_bootstrap` in
`gradle.properties` when updating them.

Loom's license and upstream attribution are in `loom-maven/`. Mixin's component
licenses and notices are included in its JAR. `checksums.sha256` records the
SHA-256 of every bundled file in this directory, relative to `libs/`.
