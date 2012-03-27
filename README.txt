This is the README of CookMe!
For support visit the old forum thread: http://bit.ly/cookmebukkit
or the new dev.bukkit.org page: http://bit.ly/cookmebukkitdev
Thanks for using!

This plugin sends usage statistics! If you wish to disable the usage stats, look at plugins/PluginMetrics/config.yml!
This plugin is released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported (CC BY-NC-SA 3.0) license.


Standard config:

# For help please refer to http://bit.ly/cookmebukkitdev or http://bit.ly/cookmebukkit
configuration:
  permissions: true
  messages: true
  duration:
    min: 15
    max: 30
  noBlocks: true
  cooldown: 30
effects:
  damage: 8.75
  death: 4.25
  venom: 8.75
  hungervenom: 8.75
  hungerdecrease: 8.75
  confusion: 8.75
  blindness: 8.75
  weakness: 8.75
  slowness: 8.75
  slowness_blocks: 8.75
  instant_damage: 8.75
  refusing: 8.75
food:
- RAW_BEEF
- RAW_CHICKEN
- RAW_FISH
- PORK
- ROTTEN_FLESH

Commands & Permissions (if no permissions system is detected, only OPs are able to use the commands!)
Only bukkit's permissions system is supported!

Node: cookme.safe
Description: No effects will appear, if a player has got this permission

/cookme reload
Node: cookme.reload
Description: Reloads the config

/cookme help
Node: cookme.help
Description: Displays the help

/cookme set <effect> <percentage>
Node: cookme.set.<effect>
Description: Sets the percentage for the specified effect

Description: Allows you to enable the refusing of raw food

/cookme enable permissions
Node: cookme.enable.permissions
Description: Enables the permissions! (Only OPs or player with the permission can use a specific command)

/cookme enable messages
Node: cookme.enable.messages
Description: Enables the messages!

/cookme disable permissions
Node: cookme.disable.permissions
Description: Disables the permissions! ALL players can use the commands!

/cookme disable messages
Node: cookme.disable.messages
Description: Disables the messages!

/cookme set cooldown <value>
Node: cookme.cooldown
Description: Sets the cooldown value in seconds

/cookme set duration min <value>
Node: cookme.duration
Description: Sets the minimum duration value in seconds

/cookme set duration max <value>
Node: cookme.duration
Description: Sets the maximum duration value in seconds