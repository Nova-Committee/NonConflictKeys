# Troubleshooting

- **Java 7 API or Java 8 compiler not found:** rerun setup, then check
  `gradle.local.properties`. Explicit `-Pjava7Home=...` and
  `-Porg.gradle.java.installations.paths=...` override local selections.
- **IDEA has old launch configurations:** existing XML files are preserved.
  Back up customized configurations, delete only the generated configuration
  you want to recreate, and run `genIdeaRuns` again.
- **Eclipse / VS Code launch fails after a clean checkout:** run `build` and the
  IDE generation task first and select Java 7 or 8 in the IDE launcher. Direct Java launches do not execute Gradle's build
  dependencies; regenerate mappings after changing dependencies or mappings.
- **Mixin applies in development but fails in a normal installation:** distribute
  the SRG release JAR, include its generated refmap and config, and install one
  shared bootstrap. Do not bundle a second Mixin runtime inside the mod.
- **Remapper initialization fails:** retain the complete root-cause exception.
  Check Forge 9.11.1.1345, duplicate runtimes and classloader conflicts; the
  bootstrap now stops instead of proceeding without its required remapper.
- **Dependency download fails:** check the named repository and proxy settings.
  `--offline` uses existing caches and cannot provision missing artifacts.
