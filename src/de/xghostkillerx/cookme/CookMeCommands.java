package de.xghostkillerx.cookme;

import java.util.Arrays;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

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
 * @thanks nisovin
 * 
 */

public class CookMeCommands {

	CookMe plugin;
	public CookMeCommands(CookMe instance) {
		plugin = instance;
	}
	public String message, effect;
	String[] effects = {"damage", "death", "venom", "hungervenom", "hungerdecrease", "weakness", "slowness", "slowness_blocks", "confusion", "blindness", "hungervenom", "hungerdecrease"};
	public int i;

	//Commands; always check for permissions!
	public boolean CookMeCommand (CommandSender sender, Command command, String commandLabel, String[] args) {
		if (command.getName().equalsIgnoreCase("cookme")) {

			// debug TODO REMOVE
			if (args.length > 0 && args[0].equalsIgnoreCase("debug")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					player.setFoodLevel(5);
				}
			}


			// reload
			if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
				if (plugin.config.getBoolean("configuration.permissions") == true) {
					if (sender.hasPermission("cookme.reload")) {
						CookMeReload(sender, args);
						return true;
					}
					else {
						message = plugin.localization.getString("permission_denied");
						message(sender, message);
						return true;
					}
				}
				if (plugin.config.getBoolean("configuration.permissions") == false) {
					CookMeReload(sender, args);
					return true;
				}
			}
			// help
			if (args.length > 0 && args[0].equalsIgnoreCase("help")) {
				if (plugin.config.getBoolean("configuration.permissions") == true) {
					if (sender.hasPermission("cookme.help")) {
						CookMeHelp(sender, args);						
						return true;
					}
					else {
						 message = plugin.localization.getString("permission_denied");
						message(sender, message);
						return true;
					}
				}
				if (plugin.config.getBoolean("configuration.permissions") == false) {
					CookMeHelp(sender, args);
					return true;
				}
			}
			// enable
			if (args.length > 0 && args[0].equalsIgnoreCase("enable")) {
				// permissions
				if (args.length > 1 && args[1].equalsIgnoreCase("permissions")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.enable.permissions")) {
							CookMeEnablePermissions(sender, args);
							return true;
						}
						else {
							message = plugin.localization.getString("permission_denied");
							message(sender, message);
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeEnablePermissions(sender, args);
						return true;
					}
				}
				// messages
				if (args.length > 1 && args[1].equalsIgnoreCase("messages")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.enable.messages")) {
							CookMeEnableMessages(sender, args);
							return true;
						}
						else {
							message = plugin.localization.getString("permission_denied");
							message(sender, message);
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeEnableMessages(sender, args);
						return true;
					}
				}
				// Enable an effect
				if (args.length > 1 && Arrays.asList(effects).contains(args[1])) {
					effect = args[1];
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.enable." + args[1])) {
							CookMeEnableEffect(sender, args, effect);
							return true;
						}
						else {
							message = plugin.localization.getString("permission_denied");
							message(sender, message);
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeEnableEffect(sender, args, effect);
						return true;
					}
				}				
				// all
				if (args.length > 1 && args[1].equalsIgnoreCase("all")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.enable.all")) {
							CookMeEnableAll(sender, args);
							return true;
						}
						else {
							message = plugin.localization.getString("permission_denied");
							message(sender, message);
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						return true;
					}
				}
			}
			// disable
			if (args.length > 0 && args[0].equalsIgnoreCase("disable")) {
				// permissions
				if (args.length > 1 && args[1].equalsIgnoreCase("permissions")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.disable.permissions")) {
							CookMeDisablePermissions(sender, args);
							return true;
						}
						else {
							message = plugin.localization.getString("permission_denied");
							message(sender, message);
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeDisablePermissions(sender, args);
						return true;
					}
				}
				// messages
				if (args.length > 1 && args[1].equalsIgnoreCase("messages")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.disable.messages")) {
							CookMeDisableMessages(sender, args);
							return true;
						}
						else {
							message = plugin.localization.getString("permission_denied");
							message(sender, message);
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeDisableMessages(sender, args);
						return true;
					}
				}
				// Disbale an effect
				if (args.length > 1 && Arrays.asList(effects).contains(args[1])) {
					effect = args[1];
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.disable." + args[1])) {
							CookMeDisableEffect(sender, args, effect);
							return true;
						}
						else {
							message = plugin.localization.getString("permission_denied");
							message(sender, message);
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeDisableEffect(sender, args, effect);
						return true;
					}
				}
				// all
				if (args.length > 1 && args[1].equalsIgnoreCase("all")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.disable.all")) {
							CookMeDisableAll(sender, args);
							return true;
						}
						else {
							message = plugin.localization.getString("permission_denied");
							message(sender, message);
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeDisableAll(sender, args);
						return true;
					}
				}
			}
		}
		return false;
	}

	// Message sender
	private void message(CommandSender sender, String message) {
		PluginDescriptionFile pdfFile = plugin.getDescription();
		sender.sendMessage(message
				.replaceAll("&([0-9a-f])", "\u00A7$1")
				.replaceAll("%version", pdfFile.getVersion()));
		
	}

	// Message sender
	private void message(CommandSender sender, String message, String effect) {
		PluginDescriptionFile pdfFile = plugin.getDescription();
		sender.sendMessage(message
				.replaceAll("&([0-9a-f])", "\u00A7$1")
				.replaceAll("%effect", effect)
				.replaceAll("%version", pdfFile.getVersion()));
		
	}

	// See the help with /cookme help
	private boolean CookMeHelp(CommandSender sender, String[] args) {
		for (i=1; i <= 8; i++) {
			message = plugin.localization.getString("help_" + Integer.toString(i));
			message(sender, message);
		}
		return true;
	}

	// Reloads the config with /cookme reload
	private boolean CookMeReload(CommandSender sender, String[] args) {
		plugin.loadConfigsAgain();		
		message = plugin.localization.getString("reload");
		message(sender, message);
		return true;
	}

	// Enables permissions with /cookme enable permissions
	private boolean CookMeEnablePermissions(CommandSender sender, String[] args) {
		plugin.config.set("configuration.permissions", true);
		plugin.saveConfig();
		for (i=1; i <= 2; i++) {
			message = plugin.localization.getString("enable_permissions_" + Integer.toString(i));
			message(sender, message);
		}
		return true;
	}

	// Disables permissions with /cookme disable permissions
	private boolean CookMeDisablePermissions(CommandSender sender, String[] args) {
		plugin.config.set("configuration.permissions", false);
		plugin.saveConfig();
		for (i=1; i <= 2; i++) {
			message = plugin.localization.getString("disable_permissions_" + Integer.toString(i));
			message(sender, message);
		}
		return true;
	}

	// Enables messages with /cookme enable messages
	private boolean CookMeEnableMessages(CommandSender sender, String[] args) {
		plugin.config.set("configuration.messages", true);
		plugin.saveConfig();
		message = plugin.localization.getString("enable_messages");
		message(sender, message);
		return true;
	}

	// Disables messages with /cookme disable messages
	private boolean CookMeDisableMessages(CommandSender sender, String[] args) {
		plugin.config.set("configuration.messages", false);
		plugin.saveConfig();
		message = plugin.localization.getString("disable_messages");
		message(sender, message);
		return true;
	}
	
	// CookMe enable effect
	private boolean CookMeEnableEffect(CommandSender sender, String[] args, String effect) {
		plugin.config.set("effects." + effect, true);
		plugin.saveConfig();
		message = plugin.localization.getString("enable_effect");
		message(sender, message, effect);
		return true;
	}
	
	
	// CookMe disable effect
	private boolean CookMeDisableEffect(CommandSender sender, String[] args, String effect) {
		plugin.config.set("effects." + effect, false);
		plugin.saveConfig();
		message = plugin.localization.getString("disable_effect");
		message(sender, message, effect);
		return true;
	}

	// Enables all effects with /cookme enable all
	private boolean CookMeEnableAll(CommandSender sender, String[] args) {
		plugin.config.set("effects.damage", true);
		plugin.config.set("effects.death", true);
		plugin.config.set("effects.venom", true);
		plugin.config.set("effects.hungervenom", true);
		plugin.config.set("effects.hungerdecrease", true);
		plugin.config.set("effects.confusion", true);
		plugin.config.set("effects.blindness", true);
		plugin.config.set("effects.weakness", true);
		plugin.config.set("effects.venom", true);
		plugin.config.set("effects.slowness", true);
		plugin.config.set("effects.slowness_blocks", true);
		plugin.config.set("effects.instant_damage", true);
		plugin.saveConfig();
		message = plugin.localization.getString("enable_all");
		message(sender, message);
		return true;
	}
	// Disables all effects with /cookme disable all
	private boolean CookMeDisableAll(CommandSender sender, String[] args) {
		plugin.config.set("effects.damage", false);
		plugin.config.set("effects.death", false);
		plugin.config.set("effects.venom", false);
		plugin.config.set("effects.hungervenom", false);
		plugin.config.set("effects.hungerdecrease", false);
		plugin.config.set("effects.confusion", false);
		plugin.config.set("effects.blindness", false);
		plugin.config.set("effects.weakness", false);
		plugin.config.set("effects.venom", false);
		plugin.config.set("effects.slowness", false);
		plugin.config.set("effects.slowness_blocks", false);
		plugin.config.set("effects.instant_damage", false);
		plugin.saveConfig();
		message = plugin.localization.getString("disable_all");
		message(sender, message);
		return true;
	}
}
