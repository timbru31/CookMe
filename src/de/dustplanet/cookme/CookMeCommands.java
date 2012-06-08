package de.dustplanet.cookme;

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
	private String message, effect, cooldown, duration, percentage;
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
					plugin.message(sender, null, message, null, null);
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
					plugin.message(sender, null, message, null, null);
					return true;
				}
			}
			if (CookMe.config.getBoolean("configuration.permissions") == false) {
				CookMeHelp(sender);
				return true;
			}
		}
		// Set cooldown, duration or percentage of an effect
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
							plugin.message(sender, null, message, cooldown, null);
							return true;
						}
					}
					else {
						message = plugin.localization.getString("permission_denied");
						plugin.message(sender, null, message, null, null);
						return true;
					}
				}
				// Duration
				if (args[1].equalsIgnoreCase("duration")) {
					if (args.length > 2) {
						if (sender.hasPermission("cookme.duration")) {
							// Max or Min
							if (args[2].equalsIgnoreCase("max") || args[2].equalsIgnoreCase("min")) {
								if (args.length > 3) {
									duration = args[3];
									durationInt = Integer.valueOf(args[3]);
									CookMe.config.set("configuration.duration." + args[2].toLowerCase(), durationInt);
									plugin.saveConfig();
									message = plugin.localization.getString("changed_duration_" + args[2].toLowerCase());
									plugin.message(sender, null, message, duration, null);
									return true;
								}
							}
						}
						else {
							message = plugin.localization.getString("permission_denied");
							plugin.message(sender, null, message, null, null);
							return true;
						}
					}
				}
				// Effect
				if (Arrays.asList(plugin.effects).contains(args[1])) {
					effect = args[1];
					if (args.length > 2) {
						percentage = args[2];
						if (sender.hasPermission("cookme.set." + effect)) {
							CookMe.config.set("effects." + effect.toLowerCase(), Double.valueOf(percentage));
							plugin.saveConfig();
							message = plugin.localization.getString("changed_effect");
							plugin.message(sender, null, message, effect, percentage);
							return true;
						}
					}
					else {
						message = plugin.localization.getString("permission_denied");
						plugin.message(sender, null, message, null, null);
						return true;
					}
				}
			}
			if (CookMe.config.getBoolean("configuration.permissions") == false) {
				// Cooldown
				if (args[1].equalsIgnoreCase("cooldown")) {
					if (args.length > 2) {
						cooldown = args[2];
						cooldownInt = Integer.valueOf(args[2]);
						CookMe.config.set("configuration.cooldown", cooldownInt);
						plugin.saveConfig();
						message = plugin.localization.getString("changed_cooldown");
						plugin.message(sender, null, message, cooldown, null);
						return true;
					}
				}
				// Duration (min or max)
				if (args[1].equalsIgnoreCase("duration")) {
					if (args[2].length() > 2) {
						if (args[2].equalsIgnoreCase("max") || args[2].equalsIgnoreCase("min")) {
							if (args.length > 3) {
								duration = args[3];
								durationInt = Integer.valueOf(args[3]);
								CookMe.config.set("configuration.duration." + args[2].toLowerCase(), durationInt);
								plugin.saveConfig();
								message = plugin.localization.getString("changed_duration_" + args[2].toLowerCase());
								plugin.message(sender, null, message, duration, null);
								return true;
							}
						}
					}
				}
				// Effect
				if (Arrays.asList(plugin.effects).contains(args[1])) {
					effect = args[1];
					if (args.length > 2) {
						percentage = args[2];
						if (sender.hasPermission("cookme.set." + effect)) {
							CookMe.config.set("effects." + effect.toLowerCase(), Double.valueOf(percentage));
							plugin.saveConfig();
							message = plugin.localization.getString("changed_effect");
							plugin.message(sender, null, message, effect, percentage);
							return true;
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
						plugin.message(sender, null, message, null, null);
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
						plugin.message(sender, null, message, null, null);
						return true;
					}
				}
				if (CookMe.config.getBoolean("configuration.permissions") == false) {
					CookMeEnableMessages(sender);
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
						plugin.message(sender, null, message, null, null);
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
						plugin.message(sender, null, message, null, null);
						return true;
					}
				}
				if (CookMe.config.getBoolean("configuration.permissions") == false) {
					CookMeDisableMessages(sender);
					return true;
				}
			}
		}
		return false;
	}

	// See the help with /cookme help
	private void CookMeHelp(CommandSender sender) {
		for (i=1; i <= 10; i++) {
			message = plugin.localization.getString("help_" + Integer.toString(i));
			plugin.message(sender, null, message, null, null);
		}
	}

	// Reloads the config with /cookme reload
	private void CookMeReload(CommandSender sender) {
		plugin.loadConfigsAgain();		
		message = plugin.localization.getString("reload");
		plugin.message(sender, null, message, null, null);
	}

	// Enables permissions with /cookme enable permissions
	private void CookMeEnablePermissions(CommandSender sender) {
		CookMe.config.set("configuration.permissions", true);
		plugin.saveConfig();
		for (i=1; i <= 2; i++) {
			message = plugin.localization.getString("enable_permissions_" + Integer.toString(i));
			plugin.message(sender, null, message, null, null);
		}
	}

	// Disables permissions with /cookme disable permissions
	private void CookMeDisablePermissions(CommandSender sender) {
		CookMe.config.set("configuration.permissions", false);
		plugin.saveConfig();
		for (i=1; i <= 2; i++) {
			message = plugin.localization.getString("disable_permissions_" + Integer.toString(i));
			plugin.message(sender, null, message, null, null);
		}
	}

	// Enables messages with /cookme enable messages
	private void CookMeEnableMessages(CommandSender sender) {
		CookMe.config.set("configuration.messages", true);
		plugin.saveConfig();
		message = plugin.localization.getString("enable_messages");
		plugin.message(sender, null, message, null, null);
	}

	// Disables messages with /cookme disable messages
	private void CookMeDisableMessages(CommandSender sender) {
		CookMe.config.set("configuration.messages", false);
		plugin.saveConfig();
		message = plugin.localization.getString("disable_messages");
		plugin.message(sender, null, message, null, null);
	}
}
