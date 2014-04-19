# CookMe [![Build Status](http://ci.dustplanet.de/job/CookMe/badge/icon)](http://ci.dustplanet.de/job/CookMe/)

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
* Please note that there is currently no support for the new fishes, see [#2](https://github.com/xGhOsTkiLLeRx/CookMe/issues/2)

*Third party features, all of them can be disabled*
* Metrics for usage statistics

## License
This plugin is released under the  
*Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)* license.  
Please see [LICENSE.md](LICENSE.md) for more information.

## Standard config
````yaml
# For help please refer to http://dev.bukkit.org/bukkit-plugins/cookme/
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
  damage: 8.0
  death: 4.0
  venom: 8.0
  hungervenom: 8.0
  hungerdecrease: 8.0
  confusion: 8.0
  blindness: 8.0
  weakness: 8.0
  slowness: 8.0
  slowness_blocks: 8.0
  instant_damage: 8.0
  refusing: 8.0
  wither: 8.0
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
# Add your own stuff here, IDs are supported, too!
food:
- RAW_BEEF
- RAW_CHICKEN
- RAW_FISH
- PORK
- ROTTEN_FLESH
````

## Commands & Permissions
(Fallback to OPs, if no permissions system is found)

### General commands
| Command | Permission node | Description |
|:----------:|:----------:|:----------:|
| - | cookme.safe | Opt out permission (no effects appear) |
| /cookme reload | cookme.reload | Reloads the configuration files |
| /cookme help | cookme.help | Displays a help menu |
| /cookme debug | only active when debug is true | Reduces your food level |

### Enabling or disabling parts
In the following table the X needs to be replaced by one of these values
* permissions
* messages

| Command | Permission node | Description |
|:----------:|:----------:|:----------:|
| /cookme enable X | cookme.enable.X | Enables permissions/messages |
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

| Command | Permission node | Description |
|:----------:|:----------:|:----------:|
| /cookme set cooldown | cookme.cooldown | Sets the cooldown time in seconds |
| /cookme set duration max | cookme.duration | Sets the maximum duration in seconds |
| /cookme set duration min | cookme.duration | Sets the minimum duration in seconds |
| /cookme set X | cookme.set.X | Sets the percentage of effect X |

## Credits
* nisovin for the code snippet prior to the Potion API
* MatthewEnderle for the nice logo

## Support
For support visit the dev.bukkit.org page: http://dev.bukkit.org/bukkit-plugins/cookme

## Pull Requests
Feel free to submit any PRs here. :)  
Please follow the Sun Coding Guidelines, thanks!

## Usage statistics
[![MCStats](http://mcstats.org/signature/CookMe.png)](http://mcstats.org/plugin/CookMe)

## Data usage collection of Metrics

#### Disabling Metrics
The file ../plugins/Plugin Metrics/config.yml contains an option to *opt-out*

#### The following data is **read** from the server in some way or another
* File Contents of plugins/Plugin Metrics/config.yml (created if not existent)
* Players currently online (not max player count)
* Server version string (the same version string you see in /version)
* Plugin version of the metrics-supported plugin
* Mineshafter status - it does not properly propagate Metrics requests however it is a very simple check and does not read the filesystem

#### The following data is **sent** to http://mcstats.org and can be seen under http://mcstats.org/plugin/CookMe
* Metrics revision of the implementing class
* Server's GUID
* Players currently online (not max player count)
* Server version string (the same version string you see in /version)
* Plugin version of the metrics-supported plugin

## Donation
[![PayPal](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif "Donation via PayPal")](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=T9TEV7Q88B9M2)

![BitCoin](https://dl.dropboxusercontent.com/u/26476995/bitcoin_logo.png "Donation via BitCoins")  
Address: 1NnrRgdy7CfiYN63vKHiypSi3MSctCP55C
