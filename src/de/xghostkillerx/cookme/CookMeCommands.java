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

	//Commands; always check for permissions!
	public boolean CookMeCommand (CommandSender sender, Command command, String commandLabel, String[] args) {
		if (command.getName().equalsIgnoreCase("cookme")) {
			// reload
			if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
				if (plugin.config.getBoolean("configuration.permissions") == true) {
					if (sender.hasPermission("cookme.reload")) {
						CookMeReload(sender, args);
						return true;
					}
					else {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
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
			if (args.length > 0 && args[0].equalsIgnoreCase("enable")) {
				// permissions
				if (args.length > 1 && args[1].equalsIgnoreCase("permissions")) {
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
				if (args.length > 1 && args[1].equalsIgnoreCase("messages")) {
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
				if (args.length > 1 && args[1].equalsIgnoreCase("damage")) {
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
				if (args.length > 1 && args[1].equalsIgnoreCase("death")) {
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
				if (args.length > 1 && args[1].equalsIgnoreCase("venom")) {
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
				if (args.length > 1 && args[1].equalsIgnoreCase("hungervenom")) {
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
				if (args.length > 1 && args[1].equalsIgnoreCase("hungerdecrease")) {
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
				// confusion
				if (args.length > 1 && args[1].equalsIgnoreCase("confusion")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.enable.confusion")) {
							CookMeEnableConfusion(sender, args);
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeEnableConfusion(sender, args);
						return true;
					}
				}
				// blindness
				if (args.length > 1 && args[1].equalsIgnoreCase("blindness")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.enable.blindness")) {
							CookMeEnableBlindness(sender, args);
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeEnableBlindness(sender, args);
						return true;
					}
				}
				// weakness
				if (args.length > 1 && args[1].equalsIgnoreCase("weakness")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.enable.weakness")) {
							CookMeEnableWeakness(sender, args);
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeEnableWeakness(sender, args);
						return true;
					}
				}
				// slowness
				if (args.length > 1 && args[1].equalsIgnoreCase("slowness")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.enable.slowness")) {
							CookMeEnableSlowness(sender, args);
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeEnableSlowness(sender, args);
						return true;
					}
				}
				// slowness_blocks
				if (args.length > 1 && args[1].equalsIgnoreCase("slowness_blocks")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.enable.slowness_blocks")) {
							CookMeEnableSlownessBlocks(sender, args);
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeEnableSlownessBlocks(sender, args);
						return true;
					}
				}
				// all
				if (args.length > 1 && args[1].equalsIgnoreCase("all")) {
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
			if (args.length > 0 && args[0].equalsIgnoreCase("disable")) {
				// permissions
				if (args.length > 1 && args[1].equalsIgnoreCase("permissions")) {
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
				if (args.length > 1 && args[1].equalsIgnoreCase("messages")) {
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
				if (args.length > 1 && args[1].equalsIgnoreCase("damage")) {
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
				if (args.length > 1 && args[1].equalsIgnoreCase("death")) {
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
				if (args.length > 1 && args[1].equalsIgnoreCase("venom")) {
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
				if (args.length > 1 && args[1].equalsIgnoreCase("hungervenom")) {
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
				if (args.length > 1 && args[1].equalsIgnoreCase("hungerdecrease")) {
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
				// confusion
				if (args.length > 1 && args[1].equalsIgnoreCase("confusion")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.disable.confusion")) {
							CookMeDisableConfusion(sender, args);
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeDisableConfusion(sender, args);
						return true;
					}
				}
				// blindness
				if (args.length > 1 && args[1].equalsIgnoreCase("blindness")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.disable.blindness")) {
							CookMeDisableBlindness(sender, args);
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeDisableBlindness(sender, args);
						return true;
					}
				}
				// weakness
				if (args.length > 1 && args[1].equalsIgnoreCase("weakness")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.disable.weakness")) {
							CookMeDisableWeakness(sender, args);
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeDisableWeakness(sender, args);
						return true;
					}
				}
				// slowness
				if (args.length > 1 && args[1].equalsIgnoreCase("slowness")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.disable.slowness")) {
							CookMeDisableSlowness(sender, args);
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeDisableSlowness(sender, args);
						return true;
					}
				}
				// slowness_blocks
				if (args.length > 1 && args[1].equalsIgnoreCase("slowness_blocks")) {
					if (plugin.config.getBoolean("configuration.permissions") == true) {
						if (sender.hasPermission("cookme.disable.slowness_blocks")) {
							CookMeDisableSlownessBlocks(sender, args);
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");
							return true;
						}
					}
					if (plugin.config.getBoolean("configuration.permissions") == false) {
						CookMeDisableSlownessBlocks(sender, args);
						return true;
					}
				}
				// all
				if (args.length > 1 && args[1].equalsIgnoreCase("all")) {
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
		sender.sendMessage("hungervenom, hungerdecrease, confusion, blindness, weakness");
		sender.sendMessage("slowness, slowness_blocks");
		return true;
	}

	// Reloads the config with /cookme reload
	private boolean CookMeReload(CommandSender sender, String[] args) {
		PluginDescriptionFile pdfFile = plugin.getDescription();
		plugin.loadConfigAgain();		
		sender.sendMessage(ChatColor.DARK_GREEN + "CookMe version " + ChatColor.DARK_RED + pdfFile.getVersion() + ChatColor.DARK_GREEN + " reloaded!");
		return true;
	}

	// Enables permissions with /cookme enable permissions
	private boolean CookMeEnablePermissions(CommandSender sender, String[] args) {
		plugin.config.set("configuration.permissions", true);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "CookMe " + ChatColor.DARK_RED	+ "permissions " + ChatColor.DARK_GREEN	+ "enabled! Only OPs");
		sender.sendMessage(ChatColor.DARK_GREEN + "or players with the permission can use the plugin!");
		return true;
	}

	// Disables permissions with /cookme disable permissions
	private boolean CookMeDisablePermissions(CommandSender sender, String[] args) {
		plugin.config.set("configuration.permissions", false);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "CookMe " + ChatColor.DARK_RED + "permissions " + ChatColor.DARK_GREEN	+ "disabled!"); 
		sender.sendMessage(ChatColor.DARK_GREEN + "All players can use the plugin!");
		return true;
	}

	// Enables messages with /cookme enable messages
	private boolean CookMeEnableMessages(CommandSender sender, String[] args) {
		plugin.config.set("configuration.messages", true);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "CookMe " + ChatColor.DARK_RED	+ "messages " + ChatColor.DARK_GREEN + "enabled!");
		return true;
	}

	// Disables messages with /cookme disable messages
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
	// Enables effect confusion with /cookme enable confusion
	private boolean CookMeEnableConfusion(CommandSender sender, String[] args) {
		plugin.config.set("effects.confusion", true);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "Effect " + ChatColor.DARK_RED	+ "confusion " + ChatColor.DARK_GREEN + "enabled");
		return true;
	}
	// Disables effect confusion with /cookme disable hungerdecrease
	private boolean CookMeDisableConfusion(CommandSender sender, String[] args) {
		plugin.config.set("effects.confusion", false);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "Effect " + ChatColor.DARK_RED	+ "confusion " + ChatColor.DARK_GREEN + "disabled");
		return true;
	}
	// Enables effect blindness with /cookme enable blindness
	private boolean CookMeEnableBlindness(CommandSender sender, String[] args) {
		plugin.config.set("effects.blindness", true);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "Effect " + ChatColor.DARK_RED	+ "blindness " + ChatColor.DARK_GREEN + "enabled");
		return true;
	}
	// Disables effect blindness with /cookme disable blindness
	private boolean CookMeDisableBlindness(CommandSender sender, String[] args) {
		plugin.config.set("effects.blindness", false);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "Effect " + ChatColor.DARK_RED	+ "blindness " + ChatColor.DARK_GREEN + "disabled");
		return true;
	}
	// Enables effect weakness with /cookme enable weakness
	private boolean CookMeEnableWeakness(CommandSender sender, String[] args) {
		plugin.config.set("effects.weakness", true);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "Effect " + ChatColor.DARK_RED	+ "weakness " + ChatColor.DARK_GREEN + "enabled");
		return true;
	}
	// Disables effect weakness with /cookme disable weakness
	private boolean CookMeDisableWeakness(CommandSender sender, String[] args) {
		plugin.config.set("effects.weakness", false);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "Effect " + ChatColor.DARK_RED	+ "weakness " + ChatColor.DARK_GREEN + "disabled");
		return true;
	}
	// Enables effect slowness with /cookme enable slowness
	private boolean CookMeEnableSlowness(CommandSender sender, String[] args) {
		plugin.config.set("effects.slowness", true);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "Effect " + ChatColor.DARK_RED	+ "slowness " + ChatColor.DARK_GREEN + "enabled");
		return true;
	}
	// Disables effect slowness with /cookme disable slowness
	private boolean CookMeDisableSlowness(CommandSender sender, String[] args) {
		plugin.config.set("effects.slowness", false);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "Effect " + ChatColor.DARK_RED	+ "slowness " + ChatColor.DARK_GREEN + "disabled");
		return true;
	}
	// Enables effect slowness_blocks with /cookme enable slowness_blocks
	private boolean CookMeEnableSlownessBlocks(CommandSender sender, String[] args) {
		plugin.config.set("effects.slowness_blocks", true);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "Effect " + ChatColor.DARK_RED	+ "slowness for blocks " + ChatColor.DARK_GREEN + "enabled");
		return true;
	}
	// Disables effect slowness_blocks with /cookme disable slowness_blocks
	private boolean CookMeDisableSlownessBlocks(CommandSender sender, String[] args) {
		plugin.config.set("effects.slowness_blocks", false);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "Effect " + ChatColor.DARK_RED	+ "slowness for blocks " + ChatColor.DARK_GREEN + "disabled");
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
		plugin.config.set("effects.confusion", false);
		plugin.config.set("effects.blindness", false);
		plugin.config.set("effects.weakness", false);
		plugin.config.set("effects.venom", false);
		plugin.config.set("effects.slowness", false);
		plugin.config.set("effects.slowness_blocks", false);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.DARK_RED	+ "All " + ChatColor.DARK_GREEN + "effects " + ChatColor.DARK_GREEN + "disabled");
		return true;
	}
}
