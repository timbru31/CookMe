# CookMe
[![Build Status](https://ci.dustplanet.de/job/CookMe/badge/icon)](https://ci.dustplanet.de/job/CookMe/)
[![Build Status](https://travis-ci.org/timbru31/CookMe.svg?branch=master)](https://travis-ci.org/timbru31/CookMe)
[![Circle CI](https://img.shields.io/circleci/project/timbru31/CookMe.svg)](https://circleci.com/gh/timbru31/CookMe)
[![Build status](https://ci.appveyor.com/api/projects/status/nb3s68pfwruj1xq5?svg=true)](https://ci.appveyor.com/project/timbru31/cookme)

[![BukkitDev](https://img.shields.io/badge/BukkitDev-v1.8.4-orange.svg)](https://dev.bukkit.org/projects/cookme/)

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## Info
This CraftBukkit plugin adds a more realistic and detailed behavior when you consume raw food
* Control each effect by giving a percentage, disabling means a percentage of 0.0
* Minimum and maximum duration
* Control the strength of an effect yourself or let the dice decide
* Prevent vanilla effects
* Affects any controlled food (default are raw foods)
* Flexible on the fly configuration via commands
* Opt-out permission and command permissions
* Cooldown until the next effect should occur
* Please note that there is currently no support for the new fishes, see [#2](https://github.com/timbru31/CookMe/issues/2)

*Third party features, all of them can be disabled*
* bStats for usage statistics

## Standard config
```yaml
# For help please refer to https://dev.bukkit.org/projects/cookme/
configuration:
  # Should permissions be used?
  permissions: true
  # Display a message when eating?
  messages: true
  # The minimum and maximum duration of the effect in seconds
  duration:
    min: 15
    max: 30
  # Cooldown in seconds, how long the player is safe before the next effect happens.
  cooldown: 30
  # Should /cookme debug be enabled (modifies the food level)
  debug: false
  # Should the vanilla poison from raw chicken and rotten flesh be prevented if the player has the permission cookme.safe
  preventVanillaPoison: false
  # Should the effect strength be random or controlled from below (also called amplifier)
  randomEffectStrength: true
# The percentage for each effect. Make sure all together are exactly 100! 0 means disabled!
effects:
  damage: 6.9
  death: 3.4
  venom: 6.9
  hungervenom: 6.9
  hungerdecrease: 6.9
  confusion: 6.9
  blindness: 6.9
  weakness: 6.9
  slowness: 6.9
  slowness_blocks: 6.9
  instant_damage: 6.9
  refusing: 6.9
  wither: 6.9
  levitation: 6.9
  unluck: 6.9
# Put a number here for the strength of an effect, only used when randomEffectStrength is false
effectStrength:
  venom: 8
  hungervenom: 8
  confusion: 8
  blindness: 8
  weakness: 8
  slowness: 8
  slowness_blocks: 8
  instant_damage: 8
  wither: 8
  levitation: 8
  unluck: 8
# Add your own stuff here, IDs are supported, too!
food:
- RAW_BEEF
- RAW_CHICKEN
- RAW_FISH
- PORK
- ROTTEN_FLESH
- MUTTON
- RABBIT
```

## Commands & Permissions
(Fallback to OPs, if no permissions system is found)

### General commands
| Command        | Permission node                | Description                            |
|:--------------:|:------------------------------:|:--------------------------------------:|
|              - | cookme.safe                    | Opt out permission (no effects appear) |
| /cookme reload | cookme.reload                  | Reloads the configuration files        |
| /cookme help   | cookme.help                    | Displays a help menu                   |
| /cookme debug  | only active when debug is true | Reduces your food level                |

### Enabling or disabling parts
In the following table the X needs to be replaced by one of these values
* permissions
* messages

| Command           | Permission node  | Description                   |
|:-----------------:|:----------------:|:-----------------------------:|
| /cookme enable X  | cookme.enable.X  | Enables permissions/messages  |
| /cookme disable X | cookme.disable.X | Disables permissions/messages |

### Adjusting duration, cooldown and percentages
In the following table the X needs to be replaced by one of these effects
* damage
* death
* venom
* hungervenom
* hungerdecrease
* confusion
* blindness
* weakness
* slowness
* slowness_blocks
* instant_damage
* refusing
* wither
* levitation
* unluck

| Command                  | Permission node | Description                          |
|:------------------------:|:---------------:|:------------------------------------:|
| /cookme set cooldown     | cookme.cooldown | Sets the cooldown time in seconds    |
| /cookme set duration max | cookme.duration | Sets the maximum duration in seconds |
| /cookme set duration min | cookme.duration | Sets the minimum duration in seconds |
| /cookme set X            | cookme.set.X    | Sets the percentage of effect X      |

## Credits
* nisovin for the code snippet prior to the Potion API
* MatthewEnderle for the nice logo

## Support
For support visit the dev.bukkit.org page: https://dev.bukkit.org/projects/cookme

## Pull Requests
Feel free to submit any PRs here. :)  
Please follow the Sun Coding Guidelines, thanks!

## Usage statistics

_stats images are returning soon!_

## Data usage collection of bStats

#### Disabling bStats
The file `./plugins/bStats/config.yml` contains an option to *opt-out*.

#### The following data is **read and sent** to https://bstats.org and can be seen under https://bstats.org/plugin/bukkit/CookMe
* Your server's randomly generated UUID
* The amount of players on your server
* The online mode of your server
* The bukkit version of your server
* The java version of your system (e.g. Java 8)
* The name of your OS (e.g. Windows)
* The version of your OS
* The architecture of your OS (e.g. amd64)
* The system cores of your OS (e.g. 8)
* bStats-supported plugins
* Plugin version of bStats-supported plugins
* List of enabled foods

## Donation
[![PayPal](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif "Donation via PayPal")](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=T9TEV7Q88B9M2)

![BitCoin](https://dustplanet.de/wp-content/uploads/2015/01/bitcoin-logo-plain.png "Donation via BitCoins")  
1NnrRgdy7CfiYN63vKHiypSi3MSctCP55C

---
Built by (c) Tim Brust and contributors. Released under the MIT license.
