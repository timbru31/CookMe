package de.xghostkillerx.cookme;

import java.util.Arrays;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * CookMe for CraftBukkit/Bukkit
 * Handles the commands!
 * 
 * Refer to the forum thread:
 * http://bit.ly/cookmebukkit
 * Refer to the dev.bukkit.org page:
 * http://bit.ly/cookmebukkitdev
 *
 * @author xGhOsTkiLLeRx
 * @thanks nisovin for his awesome code snippet!
 * 
 */

public class CookMeCommands implements CommandExecutor {

	CookMe plugin;
	public CookMeCommands(CookMe instance) {
		plugin = instance;
	}
	private String message, effect, cooldown, duration;
	private String[] effects = {"damage", "death", "venom", "hungervenom", "hungerdecrease", "weakness", "slowness", "slowness_blocks", "confusion", "blindness", "hungervenom", "hungerdecrease", "instant_damage"};
	private int i, durationInt, cooldownInt;

	// Commands; always check for permissions!
	public boolean onCommand (CommandSender sender, Command command, String commandLabel, String[] args) {
		// reload
		if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
			if (CookMe.config.getBoolean("configuration.permissions") == true) {
				if (sender.hasPermission("cookme.reload")) {
					CookMeReload(sender);
					return true;
				}
				else {
					message = plugin.localization.getString("permission_denied");
					plugin.message(sender, null, message, null);
					return true;
				}
			}
			if (CookMe.config.getBoolean("configuration.permissions") == false) {
				CookMeReload(sender);
				return true;
			}
		}
		// help
		if (args.length > 0 && args[0].equalsIgnoreCase("help")) {
			if (CookMe.config.getBoolean("configuration.permissions") == true) {
				if (sender.hasPermission("cookme.help")) {
					CookMeHelp(sender);						
					return true;
				}
				else {
					message = plugin.localization.getString("permission_denied");
					plugin.message(sender, null, message, null);
					return true;
				}
			}
			if (CookMe.config.getBoolean("configuration.permissions") == false) {
				CookMeHelp(sender);
				return true;
			}
		}
		// Set cooldown or duration
		if (args.length > 1 && args[0].equalsIgnoreCase("set")) {
			if (CookMe.config.getBoolean("configuration.permissions") == true) {
				if (args[1].equalsIgnoreCase("cooldown")) {
					if (sender.hasPermission("cookme.cooldown")) {
						if (args.length > 2) {
							cooldown = args[2];
							cooldownInt = Integer.valueOf(args[2]);
							CookMe.config.set("configuration.cooldown", cooldownInt);
							plugin.saveConfig();
							message = plugin.localization.getString("changed_cooldown");
							plugin.message(sender, null, message, cooldown);
							return true;
						}
					}
					else {
						message = plugin.localization.getString("permission_denied");
						plugin.message(sender, null, message, null);
						return true;
					}
				}
				if (args[1].equalsIgnoreCase("duration")) {
					if (args.length > 2) {
						if (sender.hasPermission("cookme.duration")) {
							if (args[2].equalsIgnoreCase("max") || args[2].equalsIgnoreCase("min")) {
								if (args.length > 3) {
									duration = args[3];
									durationInt = Integer.valueOf(args[3]);
									CookMe.config.set("configuration.duration." + args[2].toLowerCase(), durationInt);
									plugin.saveConfig();
									message = plugin.localization.getString("changed_duration_" + args[2].toLowerCase());
									plugin.message(sender, null, message, duration);
									return true;
								}
							}
						}
						else {
							message = plugin.localization.getString("permission_denied");
							plugin.message(sender, null, message, null);
							return true;
						}
					}
				}
			}
			if (CookMe.config.getBoolean("configuration.permissions") == false) {
				if (args[1].equalsIgnoreCase("cooldown")) {
					if (args.length > 2) {
						cooldown = args[2];
						cooldownInt = Integer.valueOf(args[2]);
						CookMe.config.set("configuration.cooldown", cooldownInt);
						plugin.saveConfig();
						message = plugin.localization.getString("changed_cooldown");
						plugin.message(sender, null, message, null);
						return true;
					}
				}
				if (args[1].equalsIgnoreCase("duration")) {
					if (args[2].length() > 2) {
						if (args[2].equalsIgnoreCase("max") || args[2].equalsIgnoreCase("min")) {
							if (args.length > 3) {
								duration = args[3];
								durationInt = Integer.valueOf(args[3]);
								CookMe.config.set("configuration.duration." + args[2].toLowerCase(), durationInt);
								plugin.saveConfig();
								message = plugin.localization.getString("changed_duration_" + args[2].toLowerCase());
								plugin.message(sender, null, message, duration);
								return true;
							}
						}
					}
				}
			}
		}
		// enable
		if (args.length > 0 && args[0].equalsIgnoreCase("enable")) {
			// permissions
			if (args.length > 1 && args[1].equalsIgnoreCase("permissions")) {
				if (CookMe.config.getBoolean("configuration.permissions") == true) {
					if (sender.hasPermission("cookme.enable.permissions")) {
						CookMeEnablePermissions(sender);
						return true;
					}
					else {
						message = plugin.localization.getString("permission_denied");
						plugin.message(sender, null, message, null);
						return true;
					}
				}
				if (CookMe.config.getBoolean("configuration.permissions") == false) {
					CookMeEnablePermissions(sender);
					return true;
				}
			}
			// messages
			if (args.length > 1 && args[1].equalsIgnoreCase("messages")) {
				if (CookMe.config.getBoolean("configuration.permissions") == true) {
					if (sender.hasPermission("cookme.enable.messages")) {
						CookMeEnableMessages(sender);
						return true;
					}
					else {
						message = plugin.localization.getString("permission_denied");
						plugin.message(sender, null, message, null);
						return true;
					}
				}
				if (CookMe.config.getBoolean("configuration.permissions") == false) {
					CookMeEnableMessages(sender);
					return true;
				}
			}
			// Enable an effect
			if (args.length > 1 && Arrays.asList(effects).contains(args[1])) {
				effect = args[1];
				if (CookMe.config.getBoolean("configuration.permissions") == true) {
					if (sender.hasPermission("cookme.enable." + args[1])) {
						CookMeEnableEffect(sender, effect);
						return true;
					}
					else {
						message = plugin.localization.getString("permission_denied");
						plugin.message(sender, null, message, null);
						return true;
					}
				}
				if (CookMe.config.getBoolean("configuration.permissions") == false) {
					CookMeEnableEffect(sender, effect);
					return true;
				}
			}				
			// all
			if (args.length > 1 && args[1].equalsIgnoreCase("all")) {
				if (CookMe.config.getBoolean("configuration.permissions") == true) {
					if (sender.hasPermission("cookme.enable.all")) {
						CookMeEnableAll(sender);
						return true;
					}
					else {
						message = plugin.localization.getString("permission_denied");
						plugin.message(sender, null, message, null);
						return true;
					}
				}
				if (CookMe.config.getBoolean("configuration.permissions") == false) {
					return true;
				}
			}
		}
		// disable
		if (args.length > 0 && args[0].equalsIgnoreCase("disable")) {
			// permissions
			if (args.length > 1 && args[1].equalsIgnoreCase("permissions")) {
				if (CookMe.config.getBoolean("configuration.permissions") == true) {
					if (sender.hasPermission("cookme.disable.permissions")) {
						CookMeDisablePermissions(sender);
						return true;
					}
					else {
						message = plugin.localization.getString("permission_denied");
						plugin.message(sender, null, message, null);
						return true;
					}
				}
				if (CookMe.config.getBoolean("configuration.permissions") == false) {
					CookMeDisablePermissions(sender);
					return true;
				}
			}
			// messages
			if (args.length > 1 && args[1].equalsIgnoreCase("messages")) {
				if (CookMe.config.getBoolean("configuration.permissions") == true) {
					if (sender.hasPermission("cookme.disable.messages")) {
						CookMeDisableMessages(sender);
						return true;
					}
					else {
						message = plugin.localization.getString("permission_denied");
						plugin.message(sender, null, message, null);
						return true;
					}
				}
				if (CookMe.config.getBoolean("configuration.permissions") == false) {
					CookMeDisableMessages(sender);
					return true;
				}
			}
			// Disable an effect
			if (args.length > 1 && Arrays.asList(effects).contains(args[1])) {
				effect = args[1];
				if (CookMe.config.getBoolean("configuration.permissions") == true) {
					if (sender.hasPermission("cookme.disable." + args[1])) {
						CookMeDisableEffect(sender, effect);
						return true;
					}
					else {
						message = plugin.localization.getString("permission_denied");
						plugin.message(sender, null, message, null);
						return true;
					}
				}
				if (CookMe.config.getBoolean("configuration.permissions") == false) {
					CookMeDisableEffect(sender, effect);
					return true;
				}
			}
			// all
			if (args.length > 1 && args[1].equalsIgnoreCase("all")) {
				if (CookMe.config.getBoolean("configuration.permissions") == true) {
					if (sender.hasPermission("cookme.disable.all")) {
						CookMeDisableAll(sender);
						return true;
					}
					else {
						message = plugin.localization.getString("permission_denied");
						plugin.message(sender, null, message, null);
						return true;
					}
				}
				if (CookMe.config.getBoolean("configuration.permissions") == false) {
					CookMeDisableAll(sender);
					return true;
				}
			}
		}
		return false;
	}

	// See the help with /cookme help
	private boolean CookMeHelp(CommandSender sender) {
		for (i=1; i <= 10; i++) {
			message = plugin.localization.getString("help_" + Integer.toString(i));
			plugin.message(sender, null, message, null);
		}
		return true;
	}

	// Reloads the config with /cookme reload
	private boolean CookMeReload(CommandSender sender) {
		plugin.loadConfigsAgain();		
		message = plugin.localization.getString("reload");
		plugin.message(sender, null, message, null);
		return true;
	}

	// Enables permissions with /cookme enable permissions
	private boolean CookMeEnablePermissions(CommandSender sender) {
		CookMe.config.set("configuration.permissions", true);
		plugin.saveConfig();
		for (i=1; i <= 2; i++) {
			message = plugin.localization.getString("enable_permissions_" + Integer.toString(i));
			plugin.message(sender, null, message, null);
		}
		return true;
	}

	// Disables permissions with /cookme disable permissions
	private boolean CookMeDisablePermissions(CommandSender sender) {
		CookMe.config.set("configuration.permissions", false);
		plugin.saveConfig();
		for (i=1; i <= 2; i++) {
			message = plugin.localization.getString("disable_permissions_" + Integer.toString(i));
			plugin.message(sender, null, message, null);
		}
		return true;
	}

	// Enables messages with /cookme enable messages
	private boolean CookMeEnableMessages(CommandSender sender) {
		CookMe.config.set("configuration.messages", true);
		plugin.saveConfig();
		message = plugin.localization.getString("enable_messages");
		plugin.message(sender, null, message, null);
		return true;
	}

	// Disables messages with /cookme disable messages
	private boolean CookMeDisableMessages(CommandSender sender) {
		CookMe.config.set("configuration.messages", false);
		plugin.saveConfig();
		message = plugin.localization.getString("disable_messages");
		plugin.message(sender, null, message, null);
		return true;
	}

	// CookMe enable effect
	private boolean CookMeEnableEffect(CommandSender sender, String effect) {
		CookMe.config.set("effects." + effect, true);
		plugin.saveConfig();
		message = plugin.localization.getString("enable_effect");
		plugin.message(sender, null, message, effect);
		return true;
	}


	// CookMe disable effect
	private boolean CookMeDisableEffect(CommandSender sender, String effect) {
		CookMe.config.set("effects." + effect, false);
		plugin.saveConfig();
		message = plugin.localization.getString("disable_effect");
		plugin.message(sender, null, message, effect);
		return true;
	}

	// Enables all effects with /cookme enable all
	private boolean CookMeEnableAll(CommandSender sender) {
		CookMe.config.set("effects.damage", true);
		CookMe.config.set("effects.death", true);
		CookMe.config.set("effects.venom", true);
		CookMe.config.set("effects.hungervenom", true);
		CookMe.config.set("effects.hungerdecrease", true);
		CookMe.config.set("effects.confusion", true);
		CookMe.config.set("effects.blindness", true);
		CookMe.config.set("effects.weakness", true);
		CookMe.config.set("effects.venom", true);
		CookMe.config.set("effects.slowness", true);
		CookMe.config.set("effects.slowness_blocks", true);
		CookMe.config.set("effects.instant_damage", true);
		plugin.saveConfig();
		message = plugin.localization.getString("enable_all");
		plugin.message(sender, null, message, null);
		return true;
	}
	// Disables all effects with /cookme disable all
	private boolean CookMeDisableAll(CommandSender sender) {
		CookMe.config.set("effects.damage", false);
		CookMe.config.set("effects.death", false);
		CookMe.config.set("effects.venom", false);
		CookMe.config.set("effects.hungervenom", false);
		CookMe.config.set("effects.hungerdecrease", false);
		CookMe.config.set("effects.confusion", false);
		CookMe.config.set("effects.blindness", false);
		CookMe.config.set("effects.weakness", false);
		CookMe.config.set("effects.venom", false);
		CookMe.config.set("effects.slowness", false);
		CookMe.config.set("effects.slowness_blocks", false);
		CookMe.config.set("effects.instant_damage", false);
		plugin.saveConfig();
		message = plugin.localization.getString("disable_all");
		plugin.message(sender, null, message, null);
		return true;
	}
}
