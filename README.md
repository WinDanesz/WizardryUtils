
# WizardryUtils &nbsp; ![Mod Build](https://github.com/WinDanesz/WizardryUtils/workflows/Mod%20Build/badge.svg) [![Curseforge](http://cf.way2muchnoise.eu/358124.svg)](https://www.curseforge.com/minecraft/mc-mods/wizardryutils) [![Curseforge](http://cf.way2muchnoise.eu/versions/358124.svg)](http://www.curseforge.com/minecraft/mc-mods/wizardryutils/files)
A library mod for addon devs for the Electroblob's Wizardry Minecraft mod.

**This mod requires the [Electroblob's Wizardry mod](https://www.curseforge.com/minecraft/mc-mods/electroblobs-wizardry) .**

**Features**
WizardryUtils contains shared functionality and code for my addons, including:
- Framework for new artefact types (Head slot Bauble, belt slot Bauble)
- Capability-based handler for registering "dynamic" minion spells. This means minion spells no longer have to register a duplicate variant of the entity which
  implements various interfaces and methods to e.g., remove loot, drops.
- Shared code for automated artefact injection into Shrine loot tables
- Automated item model registry

**Planned features**
- Automated spell wiki page generator
- Shared code for conjured item spells - this will allow easily creating new spells that summons temporary variants of modded (and vanilla) items