# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [v1.2.0] - 2024-01-27
### :sparkles: New Features
- [`26ec728`](https://github.com/WinDanesz/WizardryUtils/commit/26ec7287e6ddb352b18d7b1af139673fd4347834) - Implemented potency, cost, duration, blast, range and cooldown attribute modifiers for each element. These can be applied to any handheld/armor/bauble items. They are named as attribute.name.wizardryutils.<element name><modifier> (e.g.: attribute.name.wizardryutils.SorcerySpellRange) *(commit by [@WinDanesz](https://github.com/WinDanesz))*

### :bug: Bug Fixes
- [`d41bba6`](https://github.com/WinDanesz/WizardryUtils/commit/d41bba6731f89616ea2ef60cfe9f58428475e914) - Fixed attribute lang keys *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`e4604f2`](https://github.com/WinDanesz/WizardryUtils/commit/e4604f2adc12ae7cd74dfeda9516327314a8cc21) - WizardryUtils from now will only attach its dynamic/summoned item capability to items that are actually used by dynamic conjuration spells. This should fix many incompatibility/broken recipe issues. *(commit by [@WinDanesz](https://github.com/WinDanesz))*


## [v1.1.1] - 2022-10-30
### :sparkles: New Features
- [`96943fb`](https://github.com/WinDanesz/WizardryUtils/commit/96943fb62242e30e7c9d0c3aaa0bdd1fa3cf53bc) - Some more helper methods *(commit by [@WinDanesz](https://github.com/WinDanesz))*


## [v1.1.0] - 2022-10-04
### :sparkles: New Features
- [`43afbff`](https://github.com/WinDanesz/WizardryUtils/commit/43afbffb89bd80711553106ec22d634ac36c1a4b) - Implemented spell modifiers as attribute modifiers. This way any item can have spell modifiers the same way vanilla attribute modifiers work. See the wiki for more info at https://github.com/WinDanesz/WizardryUtils/wiki/Attribute-Modifiers *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`d40beb1`](https://github.com/WinDanesz/WizardryUtils/commit/d40beb1f62a68849678818faa18dd477e4c6e9d1) - Crafttweaker support implementation for spellcast events *(commit by [@WinDanesz](https://github.com/WinDanesz))*

### :bug: Bug Fixes
- [`a259f2f`](https://github.com/WinDanesz/WizardryUtils/commit/a259f2fb486d98d814e79db0cf8bded81d4525be) - Dynamic minions are no longer targeted by regular minions and allies *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`e8ef900`](https://github.com/WinDanesz/WizardryUtils/commit/e8ef9009f06922f416ae4adf9699e4db27df1336) - Dynamic minions shouldn't revenge target their owner anymore (unless enabled in the config) *(commit by [@WinDanesz](https://github.com/WinDanesz))*


## [v1.0.1] - 2022-06-20
### :bug: Bug Fixes
- [`391fd7d`](https://github.com/WinDanesz/WizardryUtils/commit/391fd7da9dfbbb815999ad89b67c6d8bec4e84da) - Fixed server-side crash *(commit by [@WinDanesz](https://github.com/WinDanesz))*

### :wrench: Chores
- [`de5443d`](https://github.com/WinDanesz/WizardryUtils/commit/de5443d12864d7a882a79f880e2909dd23ad91ad) - v1.0.1, updated fancygradle *(commit by [@WinDanesz](https://github.com/WinDanesz))*


## [v1.0.0] - 2022-06-09
### :sparkles: New Features
- [`f351ee5`](https://github.com/WinDanesz/WizardryUtils/commit/f351ee562e93b031ad7201657726443ea7ee9254) - Dynamic minions now can be set to follow their owner *(commit by [@WinDanesz](https://github.com/WinDanesz))*

### :wrench: Chores
- [`83d7d2a`](https://github.com/WinDanesz/WizardryUtils/commit/83d7d2a39de83f88ed610d6b55ce8721dac138c4) - v1.0.0 *(commit by [@WinDanesz](https://github.com/WinDanesz))*


## [v0.1.2] - 2022-04-29
### :sparkles: New Features
- [`94b0a4a`](https://github.com/WinDanesz/WizardryUtils/commit/94b0a4ae09bec60cd5b89bfccda1e3a38f70b4d3) - Added support for BODY Baubles slot artefacts *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`3489e40`](https://github.com/WinDanesz/WizardryUtils/commit/3489e4022757bc242869a0503131cb6a83aaf011) - BaublesIntegration refactor to make it more similar to Wizardry's own integration, added some more helper methods *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`d713a68`](https://github.com/WinDanesz/WizardryUtils/commit/d713a68882c45be740b2c303b482862452d2c316) - Added automated artefact loot injector with config to disable if necessary *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`27d7f9b`](https://github.com/WinDanesz/WizardryUtils/commit/27d7f9b3d5652bb7cc3165c9c8e92d10a82bca3a) - Added test artefacts only appearing in dev env *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`3c254b3`](https://github.com/WinDanesz/WizardryUtils/commit/3c254b357bf8cb053d7298de0886b0e339f3f38b) - Added automated config generator to easily disable/enable for HEAD, BODY and BELT slot artefacts, ITickableArtefacts only tick if they are enabled *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`d5fca4c`](https://github.com/WinDanesz/WizardryUtils/commit/d5fca4c4ef26d3f41ac62ec10b449478a59b2cf4) - Added some helper methods *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`45df79e`](https://github.com/WinDanesz/WizardryUtils/commit/45df79e35a00116ce3cd75f8de5ad0b15cb58114) - Dynamic summons cannot be bred anymore *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`34f233d`](https://github.com/WinDanesz/WizardryUtils/commit/34f233dba96f7d22a9dcebc3c4df4f8f187665c8) - added SummonedItemCapability, SummonedThing class and base class for summoned item spells *(commit by [@WinDanesz](https://github.com/WinDanesz))*

### :bug: Bug Fixes
- [`e7a49ea`](https://github.com/WinDanesz/WizardryUtils/commit/e7a49ea754870eadfaf47027cc2e3bbf775cdbdb) - Fixed ITickableArtefacts not ticking *(commit by [@WinDanesz](https://github.com/WinDanesz))*

### :recycle: Refactors
- [`330c753`](https://github.com/WinDanesz/WizardryUtils/commit/330c753ac901407ef663d46aa669e58385b767e9) - Code cleanups in registry methods, removed unused classes *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`b90e61d`](https://github.com/WinDanesz/WizardryUtils/commit/b90e61dcbb59b9b8aa8fb02ecf22682134e944a6) - Refactored SummonedCreatureData *(commit by [@WinDanesz](https://github.com/WinDanesz))*

### :wrench: Chores
- [`9b51d60`](https://github.com/WinDanesz/WizardryUtils/commit/9b51d6042b8548619bffd87d1dc6521c6ba34481) - added FUNDING.yml *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`2a21d32`](https://github.com/WinDanesz/WizardryUtils/commit/2a21d329903f78848cc5031970352b90c800f9aa) - update bug_report.yml *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`1d195b0`](https://github.com/WinDanesz/WizardryUtils/commit/1d195b06b8154b2f0801f9d40067456eb2b5457d) - updated README.md *(commit by [@WinDanesz](https://github.com/WinDanesz))*


## [v0.1.1] - 2022-04-15
### :wrench: Chores
- [`de4490c`](https://github.com/WinDanesz/WizardryUtils/commit/de4490ce2034cb9a8889f2d446dea2e0540c4124) - added bug report ymls *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`44d62b6`](https://github.com/WinDanesz/WizardryUtils/commit/44d62b647d3eb9b8dd2d594dc557e872b7d5424b) - updated .gitignore *(commit by [@WinDanesz](https://github.com/WinDanesz))*


[v0.1.1]: https://github.com/WinDanesz/WizardryUtils/compare/v0.1.0...v0.1.1
[v0.1.2]: https://github.com/WinDanesz/WizardryUtils/compare/v0.1.1...v0.1.2
[v1.0.0]: https://github.com/WinDanesz/WizardryUtils/compare/v0.1.2...v1.0.0
[v1.0.1]: https://github.com/WinDanesz/WizardryUtils/compare/v1.0.0...v1.0.1
[v1.1.0]: https://github.com/WinDanesz/WizardryUtils/compare/v1.0.1...v1.1.0
[v1.1.1]: https://github.com/WinDanesz/WizardryUtils/compare/v1.1.0...v1.1.1
[v1.2.0]: https://github.com/WinDanesz/WizardryUtils/compare/v1.1.4...v1.2.0