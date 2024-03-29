
# WizardryUtils &nbsp; [![Build Mod](https://github.com/WinDanesz/WizardryUtils/actions/workflows/gradle.yml/badge.svg)](https://github.com/WinDanesz/WizardryUtils/actions/workflows/gradle.yml) [![Curseforge](http://cf.way2muchnoise.eu/full_wizardryutils_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/wizardryutils) [![Curseforge](http://cf.way2muchnoise.eu/versions/608287.svg)](http://www.curseforge.com/minecraft/mc-mods/wizardryutils/files) [![Discord](https://img.shields.io/discord/544897694448091146?color=7289DA&label=Discord)](https://discord.gg/wuSsgKwAKv)
A library mod for addon devs for the Electroblob's Wizardry Minecraft mod.

**This mod requires the [Electroblob's Wizardry mod](https://www.curseforge.com/minecraft/mc-mods/electroblobs-wizardry) .**

WizardryUtils contains shared functionality and code for my addons for Electroblob's Wizardry.

**Features**
- Framework for new artefact types (Head slot Bauble, belt slot Bauble)
- Capability-based handler for registering "dynamic" minion spells. This means minion spells no longer have to register a duplicate variant of the entity which
  implements various interfaces and methods to e.g., remove loot, drops.
- Shared code for automated artefact injection into Shrine loot tables
- Automated config generator for registered artefacts
- Automated item model registry
- SpellModifiers implemented as [entity attribute modifiers](https://github.com/WinDanesz/WizardryUtils/wiki/Attribute-Modifiers) (see the [wiki](https://github.com/WinDanesz/WizardryUtils/wiki))
- Crafttweaker support for spellcast events
- Shared code for conjured item spells - this will allow easily creating new spells that summons temporary variants of modded (and vanilla) items

**Planned features**
- Automated spell summary table wiki page generator
- Automated spell wiki page generator
- Automated Artefact item wiki page generator

**Other Plans**
- Create a test spell pack project to allow people easily get started with spell pack (and mod) development
