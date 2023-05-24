**中文** | [English](README.md)

# 介绍
全键无冲是[现代化按键绑定](https://github.com/Nova-Committee/ModernKeyBinding) ([Modrinth](https://modrinth.com/mod/modernkeybinding) | [CurseForge](https://www.curseforge.com/minecraft/mc-mods/modern-keybinding))中同名功能的单独实现。

# 功能
按下按键时，所有指定键位为该按键的按键绑定均能被激活。

# 导入
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
# 许可证
全键无冲遵循[GNU-AGPL-3.0-only](https://www.gnu.org/licenses/agpl-3.0.html).

# 附加说明
- 我们不希望本模组被"合并到" | "由……替换" | "任何类似的说法"任何“整合包行为”的模组（如 Universal Tweaks 或 Debugify）。请见[本帖](https://gist.github.com/fxmorin/9770473614e3e5e0703e44273dab33f7)。
- 我们不希望使用本模组的整合包以任何形式向玩家收费。

# 注意事项
你**可以**将本模组加入整合包。
