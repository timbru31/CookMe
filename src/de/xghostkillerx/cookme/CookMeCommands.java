package de.xghostkillerx.cookme;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;

/**
 * CookMe for CraftBukkit/Bukkit
 * Handles the commands!
 * 
 * Refer to the forum thread:
 * 
 * Refer to the dev.bukkit.org page:
 * 
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

	//Commands; always check for permissions!
	public boolean CookMeCommand (CommandSender sender, Command command, String commandLabel, String[] args) {
		if (command.getName().equalsIgnoreCase("cookme")) {
			// reload
			if (args.length > 0 && args[0].equals("reload")) {
				if (plugin.config.getBoolean("configuration.permissions") == true) {
					if (sender.hasPermission("cookme.reload")) {
						CookMeReload(sender, args);
						return true;
					}
					else {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
					}
				}
				if (plugin.config.getBoolean("configuration.permissions") == false) {
					CookMeReload(sender, args);
					return true;
				}
			}
			// help
			if (args.length > 0 && args[0].equals("help")) {
				if (plugin.config.getBoolean("configuration.permissions") == true) {
					if (sender.hasPermission("cookme.help")) {
						CookMeHelp(sender, args);
						return true;
					}
					else {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
						return true;
					}
				}
				if (plugin.config.getBoolean("configuration.permissions") == false) {
					CookMeHelp(sender, args);
					return true;
				}
			}
			// enable
			if (args.length > 0 && args[0].equals("enable")) {
				// permissions
				if (args.length > 1 && args[1].equals("permissions")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.enable.permissions")) {
							CookMeEnablePermissions(sender, args);
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeEnablePermissions(sender, args);
						return true;
					}
				}
				// messages
				if (args.length > 1 && args[1].equals("messages")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.enable.messages")) {
							CookMeEnableMessages(sender, args);
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeEnableMessages(sender, args);
						return true;
					}
				}
				// damage
				if (args.length > 1 && args[1].equals("damage")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.enable.damage")) {
							CookMeEnableDamage(sender, args);
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeEnableDamage(sender, args);
						return true;
					}
				}
				// death
				if (args.length > 1 && args[1].equals("death")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.enable.death")) {
							CookMeEnableDeath(sender, args);
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeEnableDeath(sender, args);
						return true;
					}
				}
				// venom
				if (args.length > 1 && args[1].equals("venom")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.enable.venom")) {
							CookMeEnableVenom(sender, args);
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeEnableVenom(sender, args);
						return true;
					}
				}
				// hungervenom
				if (args.length > 1 && args[1].equals("hungervenom")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.enable.hungervenom")) {
							CookMeEnableHungerVenom(sender, args);
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeEnableHungerVenom(sender, args);
						return true;
					}
				}
				// hungerdecrease
				if (args.length > 1 && args[1].equals("hungerdecrease")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.enable.hungerdecrease")) {
							CookMeEnableHungerDecrease(sender, args);
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeEnableHungerDecrease(sender, args);
						return true;
					}
				}
				// all
				if (args.length > 1 && args[1].equals("all")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.enable.all")) {
							CookMeEnableAll(sender, args);
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						return true;
					}
				}
			}
			// disable
			if (args.length > 0 && args[0].equals("disable")) {
				// permissions
				if (args.length > 1 && args[1].equals("permissions")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.disable.permissions")) {
							CookMeDisablePermissions(sender, args);
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeDisablePermissions(sender, args);
						return true;
					}
				}
				// messages
				if (args.length > 1 && args[1].equals("messages")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.disable.messages")) {
							CookMeDisableMessages(sender, args);
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeDisableMessages(sender, args);
						return true;
					}
				}
				// damage
				if (args.length > 1 && args[1].equals("damage")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.disable.damage")) {
							CookMeDisableDamage(sender, args);
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeDisableDamage(sender, args);
						return true;
					}
				}
				// death
				if (args.length > 1 && args[1].equals("death")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.disable.death")) {
							CookMeDisableDeath(sender, args);
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeDisableDeath(sender, args);
						return true;
					}
				}
				// venom
				if (args.length > 1 && args[1].equals("venom")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.disable.venom")) {
							CookMeDisableVenom(sender, args);
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeDisableVenom(sender, args);
						return true;
					}
				}
				// hungervenom
				if (args.length > 1 && args[1].equals("hungervenom")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.disable.hungervenom")) {
							CookMeDisableHungerVenom(sender, args);
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeDisableHungerVenom(sender, args);
						return true;
					}
				}
				// hungerdecrease
				if (args.length > 1 && args[1].equals("hungerdecrease")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.disable.hungerdecrease")) {
							CookMeDisableHungerDecrease(sender, args);
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeDisableHungerDecrease(sender, args);
						return true;
					}
				}
				// all
				if (args.length > 1 && args[1].equals("all")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.disable.all")) {
							CookMeDisableAll(sender, args);
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
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

	// See the help with /cookme help
	private boolean CookMeHelp(CommandSender sender, String[] args) {
		PluginDescriptionFile pdfFile = plugin.getDescription();
		sender.sendMessage(ChatColor.DARK_GREEN	+ "Welcome to the CookMe version " + ChatColor.DARK_RED + pdfFile.getVersion() + ChatColor.DARK_GREEN + " help!");
		sender.sendMessage("To see the help type " + ChatColor.DARK_RED	+ "/cookme help");
		sender.sendMessage("To reload use " + ChatColor.DARK_RED + "/cookme reload");
		sender.sendMessage("To enable something use " + ChatColor.DARK_RED + "/cookme enable " + ChatColor.YELLOW + "<value>");
		sender.sendMessage("To disable something use " + ChatColor.DARK_RED	+ "/cookme disable " + ChatColor.YELLOW + "<value>");
		sender.sendMessage(ChatColor.YELLOW + "Values: " + ChatColor.WHITE + "permissions, messages, damage, death, venom,");
		sender.sendMessage("hungervenom, hungerdecrease");
		return true;
	}

	// Reload the config with /cookme reload
	private boolean CookMeReload(CommandSender sender, String[] args) {
		PluginDescriptionFile pdfFile = plugin.getDescription();
		plugin.loadConfigAgain();		
		sender.sendMessage(ChatColor.DARK_GREEN + "CookMe version " + ChatColor.DARK_RED + pdfFile.getVersion() + ChatColor.DARK_GREEN + " reloaded!");
		return true;
	}

	// Enable permissions with /cookme enable permissions
	private boolean CookMeEnablePermissions(CommandSender sender, String[] args) {
		plugin.config.set("configuration.permissions", true);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "CookMe " + ChatColor.DARK_RED	+ "permissions " + ChatColor.DARK_GREEN	+ "enabled! Only OPs");
		sender.sendMessage(ChatColor.DARK_GREEN + "or players with the permission can use the plugin!");
		return true;
	}

	// Disable permissions with /cookme disable permissions
	private boolean CookMeDisablePermissions(CommandSender sender, String[] args) {
		plugin.config.set("configuration.permissions", false);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "CookMe " + ChatColor.DARK_RED + "permissions " + ChatColor.DARK_GREEN	+ "disabled!"); 
		sender.sendMessage(ChatColor.DARK_GREEN + "All players can use the plugin!");
		return true;
	}

	// Enable messages with /cookme enable messages
	private boolean CookMeEnableMessages(CommandSender sender, String[] args) {
		plugin.config.set("configuration.messages", true);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "CookMe " + ChatColor.DARK_RED	+ "messages " + ChatColor.DARK_GREEN + "enabled!");
		return true;
	}

	// Disable messages with /cookme disable messages
	private boolean CookMeDisableMessages(CommandSender sender, String[] args) {
		plugin.config.set("configuration.messages", false);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "CookMe " + ChatColor.DARK_RED + "messages " + ChatColor.DARK_GREEN + "disabled!");
		return true;
	}

	// Enables effect damage with /cookme enable damage
	private boolean CookMeEnableDamage(CommandSender sender, String[] args) {
		plugin.config.set("effects.damage", true);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "Effect " + ChatColor.DARK_RED	+ "damage " + ChatColor.DARK_GREEN + "enabled");
		return true;
	}
	// Disables effect damage with /cookme disable damage
	private boolean CookMeDisableDamage(CommandSender sender, String[] args) {
		plugin.config.set("effects.damage", false);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "Effect " + ChatColor.DARK_RED	+ "damage " + ChatColor.DARK_GREEN + "disabled");
		return true;
	}
	// Enables effect death with /cookme enable death
	private boolean CookMeEnableDeath(CommandSender sender, String[] args) {
		plugin.config.set("effects.death", true);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "Effect " + ChatColor.DARK_RED	+ "death " + ChatColor.DARK_GREEN + "enabled");
		return true;
	}
	// Disables effect death with /cookme disable death
	private boolean CookMeDisableDeath(CommandSender sender, String[] args) {
		plugin.config.set("effects.death", false);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "Effect " + ChatColor.DARK_RED	+ "death " + ChatColor.DARK_GREEN + "disabled");
		return true;
	}
	// Enables effect venom with /cookme enable venom
	private boolean CookMeEnableVenom(CommandSender sender, String[] args) {
		plugin.config.set("effects.venom", true);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "Effect " + ChatColor.DARK_RED	+ "venom " + ChatColor.DARK_GREEN + "enabled");
		return true;
	}
	// Disables effect venom with /cookme disable venom
	private boolean CookMeDisableVenom(CommandSender sender, String[] args) {
		plugin.config.set("effects.venom", false);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "Effect " + ChatColor.DARK_RED	+ "venom " + ChatColor.DARK_GREEN + "disabled");
		return true;
	}
	// Enables effect hungervenom with /cookme enable hungervenom
	private boolean CookMeEnableHungerVenom(CommandSender sender, String[] args) {
		plugin.config.set("effects.hungervenom", true);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "Effect " + ChatColor.DARK_RED	+ "hungervenom " + ChatColor.DARK_GREEN + "enabled");
		return true;
	}
	// Disables effect hungervenom with /cookme disable hungervenom
	private boolean CookMeDisableHungerVenom(CommandSender sender, String[] args) {
		plugin.config.set("effects.hungervenom", false);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "Effect " + ChatColor.DARK_RED	+ "hungervenom " + ChatColor.DARK_GREEN + "disabled");
		return true;
	}
	// Enables effect hungerdecrease with /cookme enable hungerdecrease
	private boolean CookMeEnableHungerDecrease(CommandSender sender, String[] args) {
		plugin.config.set("effects.hungerdecrease", true);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "Effect " + ChatColor.DARK_RED	+ "hungerdecrease " + ChatColor.DARK_GREEN + "enabled");
		return true;
	}
	// Disables effect hungerdecrease with /cookme disable hungerdecrease
	private boolean CookMeDisableHungerDecrease(CommandSender sender, String[] args) {
		plugin.config.set("effects.hungerecrease", false);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "Effect " + ChatColor.DARK_RED	+ "hungerdecrease " + ChatColor.DARK_GREEN + "disabled");
		return true;
	}
	// Enables all effects with /cookme enable all
	private boolean CookMeEnableAll(CommandSender sender, String[] args) {
		plugin.config.set("effects.damage", true);
		plugin.config.set("effects.death", true);
		plugin.config.set("effects.venom", true);
		plugin.config.set("effects.hungervenom", true);
		plugin.config.set("effects.hungerdecrease", true);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_RED	+ "All " + ChatColor.DARK_GREEN + "effects " + ChatColor.DARK_GREEN + "enabled");
		return true;
	}
	// Disables all effects with /cookme disable all
	private boolean CookMeDisableAll(CommandSender sender, String[] args) {
		plugin.config.set("effects.damage", false);
		plugin.config.set("effects.death", false);
		plugin.config.set("effects.venom", false);
		plugin.config.set("effects.hungervenom", false);
		plugin.config.set("effects.hungerdecrease", false);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_RED	+ "All " + ChatColor.DARK_GREEN + "effects " + ChatColor.DARK_GREEN + "disabled");
		return true;
	}
}
