package de.dustplanet.cookme;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * CookMe for CraftBukkit/Spigot
 * Handles the commands!
 *
 * Refer to the dev.bukkit.org page:
 * https://dev.bukkit.org/projects/cookme/
 *
 * @author xGhOsTkiLLeRx
 * thanks nisovin for his awesome code snippet!
 *
 */

public class CookMeCommands implements CommandExecutor {
    private CookMe plugin;

    public CookMeCommands(CookMe instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("cookme.reload") || !plugin.isPermissions()) {
                cookMeReload(sender);
            } else {
                String message = plugin.getLocalization().getString("permission_denied");
                plugin.message(sender, null, message, null, null);
            }
            return true;
        } else if (args.length > 0 && args[0].equalsIgnoreCase("help")) {
            if (sender.hasPermission("cookme.help") || !plugin.isPermissions()) {
                cookMeHelp(sender);
            } else {
                String message = plugin.getLocalization().getString("permission_denied");
                plugin.message(sender, null, message, null, null);
            }
            return true;
        } else if (args.length > 0 && args[0].equalsIgnoreCase("debug") && plugin.isDebug()) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.setFoodLevel(10);
                player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                player.sendMessage(ChatColor.GREEN + "Food level reduced!");
            } else {
                sender.sendMessage(ChatColor.RED + "Please use debug mode ingame!");
            }
            return true;
        } else if (args.length > 1 && args[0].equalsIgnoreCase("set")) {
            if (args[1].equalsIgnoreCase("cooldown")) {
                if (sender.hasPermission("cookme.cooldown") || !plugin.isPermissions()) {
                    if (args.length > 2) {
                        int cooldown = 0;
                        try {
                            cooldown = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            String message = plugin.getLocalization().getString("no_number");
                            plugin.message(sender, null, message, null, null);
                            return true;
                        }
                        plugin.getConfig().set("configuration.cooldown", cooldown);
                        plugin.saveConfig();
                        String message = plugin.getLocalization().getString("changed_cooldown");
                        plugin.message(sender, null, message, Integer.toString(cooldown), null);
                        plugin.getCooldownManager().setCooldown(cooldown);
                        plugin.setCooldown(cooldown);
                    }
                } else {
                    String message = plugin.getLocalization().getString("permission_denied");
                    plugin.message(sender, null, message, null, null);
                }
                return true;
            } else if (args[1].equalsIgnoreCase("duration") && args.length > 2) {
                if (sender.hasPermission("cookme.duration") || !plugin.isPermissions()) {
                    if (args[2].equalsIgnoreCase("max") || args[2].equalsIgnoreCase("min")) {
                        if (args.length > 3) {
                            int duration = 0;
                            try {
                                duration = Integer.parseInt(args[3]);
                            } catch (NumberFormatException e) {
                                String message = plugin.getLocalization().getString("no_number");
                                plugin.message(sender, null, message, null, null);
                                return true;
                            }
                            plugin.getConfig().set("configuration.duration." + args[2].toLowerCase(), duration);
                            plugin.saveConfig();
                            String message = plugin.getLocalization().getString("changed_duration_" + args[2].toLowerCase());
                            plugin.message(sender, null, message, Integer.toString(duration), null);
                            if (args[2].equalsIgnoreCase("max")) {
                                plugin.setMaxDuration(duration);
                            } else {
                                plugin.setMinDuration(duration);
                            }
                        }
                    } else {
                        return false;
                    }
                } else {
                    String message = plugin.getLocalization().getString("permission_denied");
                    plugin.message(sender, null, message, null, null);
                }
                return true;
            } else if (Arrays.asList(plugin.getEffects()).contains(args[1].toLowerCase())) {
                String effect = args[1].toLowerCase();
                if (args.length > 2) {
                    if (sender.hasPermission("cookme.set." + effect) || !plugin.isPermissions()) {
                        double percentage = 0.0;
                        try {
                            percentage = Double.valueOf(args[2]);
                        } catch (NumberFormatException e) {
                            String message = plugin.getLocalization().getString("no_number");
                            plugin.message(sender, null, message, null, null);
                            return true;
                        }
                        plugin.getConfig().set("effects." + effect, percentage);
                        plugin.saveConfig();
                        String message = plugin.getLocalization().getString("changed_effect");
                        plugin.message(sender, null, message, effect, Double.toString(percentage));
                        plugin.getPercentages()[Arrays.asList(plugin.getEffects()).indexOf(effect)] = percentage;
                    } else {
                        String message = plugin.getLocalization().getString("permission_denied");
                        plugin.message(sender, null, message, null, null);
                    }
                    return true;
                }
                return false;
            }
        } else if (args.length > 0 && args[0].equalsIgnoreCase("enable")) {
            if (args.length > 1 && args[1].equalsIgnoreCase("permissions")) {
                if (sender.hasPermission("cookme.enable.permissions") || !plugin.isPermissions()) {
                    cookMeEnablePermissions(sender);
                } else {
                    String message = plugin.getLocalization().getString("permission_denied");
                    plugin.message(sender, null, message, null, null);
                }
                return true;
            } else if (args.length > 1 && args[1].equalsIgnoreCase("messages")) {
                if (sender.hasPermission("cookme.enable.messages") || !plugin.isPermissions()) {
                    cookMeEnableMessages(sender);
                } else {
                    String message = plugin.getLocalization().getString("permission_denied");
                    plugin.message(sender, null, message, null, null);
                }
                return true;
            }
        } else if (args.length > 0 && args[0].equalsIgnoreCase("disable")) {
            if (args.length > 1 && args[1].equalsIgnoreCase("permissions")) {
                if (sender.hasPermission("cookme.disable.permissions") || !plugin.isPermissions()) {
                    cookMeDisablePermissions(sender);
                } else {
                    String message = plugin.getLocalization().getString("permission_denied");
                    plugin.message(sender, null, message, null, null);
                }
                return true;
            } else if (args.length > 1 && args[1].equalsIgnoreCase("messages")) {
                if (sender.hasPermission("cookme.disable.messages") || !plugin.isPermissions()) {
                    cookMeDisableMessages(sender);
                } else {
                    String message = plugin.getLocalization().getString("permission_denied");
                    plugin.message(sender, null, message, null, null);
                }
                return true;
            }
        }
        return false;
    }

    private void cookMeHelp(CommandSender sender) {
        String message = plugin.getLocalization().getString("help");
        plugin.message(sender, null, message, null, null);

    }

    private void cookMeReload(CommandSender sender) {
        plugin.loadConfigsAgain();
        String message = plugin.getLocalization().getString("reload");
        plugin.message(sender, null, message, null, null);
    }

    private void cookMeEnablePermissions(CommandSender sender) {
        plugin.getConfig().set("configuration.permissions", true);
        plugin.saveConfig();
        String message = plugin.getLocalization().getString("enable_permissions");
        plugin.message(sender, null, message, null, null);
        plugin.setPermissions(true);
    }

    private void cookMeDisablePermissions(CommandSender sender) {
        plugin.getConfig().set("configuration.permissions", false);
        plugin.saveConfig();
        String message = plugin.getLocalization().getString("disable_permissions");
        plugin.message(sender, null, message, null, null);
        plugin.setPermissions(false);
    }

    private void cookMeEnableMessages(CommandSender sender) {
        plugin.getConfig().set("configuration.messages", true);
        plugin.saveConfig();
        String message = plugin.getLocalization().getString("enable_messages");
        plugin.message(sender, null, message, null, null);
        plugin.setMessages(true);
    }

    private void cookMeDisableMessages(CommandSender sender) {
        plugin.getConfig().set("configuration.messages", false);
        plugin.saveConfig();
        String message = plugin.getLocalization().getString("disable_messages");
        plugin.message(sender, null, message, null, null);
        plugin.setMessages(false);
    }
}
