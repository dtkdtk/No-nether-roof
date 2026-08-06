# No nether roof

[![Ore Downloads](https://img.shields.io/ore/dt/no_nether_roof
)](https://ore.spongepowered.org/dtkdtk/No-nether-roof)
[![License: Apache-2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.txt)

[Ore project page & downloads](https://ore.spongepowered.org/dtkdtk/No-nether-roof)

A Sponge plugin that prevents players from being above the roof of the Nether. It just teleports them 2 blocks down.

**This version (1.0-sponge7) requires Sponge API v7 (minecraft 1.12.2)**. Other versions can be found in the [Branches](https://github.com/dtkdtk/No-nether-roof/branches) tab.


## How does it work

The plugin just teleports the player 2 blocks down if they are above the `roofY` coordinate in the Nether. Checks are based on the player movement (`MoveEntityEvent`).


## Features

1. Configurable `roofY` coordinate
2. Configurable deny message
3. 2 ways to show deny message: actionbar (default) and chat
4. Checks can be disabled by adding the `no_nether_roof.bypass` permission


## [Source code](https://github.com/dtkdtk/No-nether-roof)
## [Issues & bugs](https://github.com/dtkdtk/No-nether-roof/issues)
