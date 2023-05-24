**English** | [中文](README_zh.md)

# Introduction
NonConflictKeys is an independent implementation of the same name feature in [Modern KeyBinding](https://github.com/Nova-Committee/ModernKeyBinding) ([Modrinth](https://modrinth.com/mod/modernkeybinding) | [CurseForge](https://www.curseforge.com/minecraft/mc-mods/modern-keybinding)).

# Feature
The keybindings with the same key will all be activated if the key is pressed.

# Import
```groovy
repositories {
  //...
  maven {
    url "https://maven.nova-committee.cn/releases"
  }
}
```
## Forge
```groovy
dependencies {
  implementation fg.deobf("committee.nova.nckey.forge:nckey-${mc_version}:${mod_version}") {
    transitive = false
  }
}
```
## Fabric
```groovy
dependencies {
  modImplementation("committee.nova.nckey.forge:nckey-${mc_version}:${mod_version}") {
    transitive = false
  }
}
```
# LICENSE
NonConflictKeys is under [GNU-AGPL-3.0-only](https://www.gnu.org/licenses/agpl-3.0.html).

# Extra Notes
- We don't want the mod to be "merged into" | "replaced by" | "Any foo of the same meaning" any modpack-behavior mod (like Universal Tweaks or Debugify). See [this](https://gist.github.com/fxmorin/9770473614e3e5e0703e44273dab33f7).
- We don't want any modpack using our mod to charge players in any way.

# ATTENTION
You **DEFINITELY CAN** add the mod to your modpack.
