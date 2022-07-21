package de.dustplanet.cookme;

import java.util.Arrays;
import java.util.Locale;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

/**
 * Handles the commands.
 *
 * @author timbru31
 */

public class CookMeCommands implements CommandExecutor {
    private final CookMe plugin;

    public CookMeCommands(final CookMe instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
        if (args.length > 0 && "reload".equalsIgnoreCase(args[0])) {
            if (sender.hasPermission("cookme.reload") || !plugin.isPermissions()) {
                cookMeReload(sender);
            } else {
                final String message = plugin.getLocalization().getString("permission_denied");
                plugin.message(sender, null, message, null, null);
            }
            return true;
        } else if (args.length > 0 && "help".equalsIgnoreCase(args[0])) {
            if (sender.hasPermission("cookme.help") || !plugin.isPermissions()) {
                cookMeHelp(sender);
            } else {
                final String message = plugin.getLocalization().getString("permission_denied");
                plugin.message(sender, null, message, null, null);
            }
            return true;
        } else if (args.length > 0 && "debug".equalsIgnoreCase(args[0]) && plugin.isDebug()) {
            if (sender instanceof Player) {
                final Player player = (Player) sender;
                player.setFoodLevel(10);
                player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                for (final PotionEffect effect : player.getActivePotionEffects()) {
                    player.removePotionEffect(effect.getType());
                }
                player.sendMessage(ChatColor.GREEN + "Food level reduced!");
            } else {
                sender.sendMessage(ChatColor.RED + "Please use debug mode ingame!");
            }
            return true;
        } else if (args.length > 1 && "set".equalsIgnoreCase(args[0])) {
            if ("cooldown".equalsIgnoreCase(args[1])) {
                if (sender.hasPermission("cookme.cooldown") || !plugin.isPermissions()) {
                    if (args.length > 2) {
                        int cooldown = 0;
                        try {
                            cooldown = Integer.parseInt(args[2]);
                        } catch (@SuppressWarnings("unused") final NumberFormatException e) {
                            final String message = plugin.getLocalization().getString("no_number");
                            plugin.message(sender, null, message, null, null);
                            return true;
                        }
                        plugin.getConfig().set("configuration.cooldown", cooldown);
                        plugin.saveConfig();
                        final String message = plugin.getLocalization().getString("changed_cooldown");
                        plugin.message(sender, null, message, Integer.toString(cooldown), null);
                        plugin.getCooldownManager().setCooldown(cooldown);
                        plugin.setCooldown(cooldown);
                    }
                } else {
                    final String message = plugin.getLocalization().getString("permission_denied");
                    plugin.message(sender, null, message, null, null);
                }
                return true;
            } else if ("duration".equalsIgnoreCase(args[1]) && args.length > 2) {
                if (sender.hasPermission("cookme.duration") || !plugin.isPermissions()) {
                    if ("max".equalsIgnoreCase(args[2]) || "min".equalsIgnoreCase(args[2])) {
                        if (args.length > 3) {
                            int duration = 0;
                            try {
                                duration = Integer.parseInt(args[3]);
                            } catch (@SuppressWarnings("unused") final NumberFormatException e) {
                                final String message = plugin.getLocalization().getString("no_number");
                                plugin.message(sender, null, message, null, null);
                                return true;
                            }
                            plugin.getConfig().set("configuration.duration." + args[2].toLowerCase(Locale.ENGLISH), duration);
                            plugin.saveConfig();
                            final String message = plugin.getLocalization()
                                    .getString("changed_duration_" + args[2].toLowerCase(Locale.ENGLISH));
                            plugin.message(sender, null, message, Integer.toString(duration), null);
                            if ("max".equalsIgnoreCase(args[2])) {
                                plugin.setMaxDuration(duration);
                            } else {
                                plugin.setMinDuration(duration);
                            }
                        }
                    } else {
                        return false;
                    }
                } else {
                    final String message = plugin.getLocalization().getString("permission_denied");
                    plugin.message(sender, null, message, null, null);
                }
                return true;
            } else if (Arrays.asList(plugin.getEffects()).contains(args[1].toLowerCase(Locale.ENGLISH))) {
                final String effect = args[1].toLowerCase(Locale.ENGLISH);
                if (args.length > 2) {
                    if (sender.hasPermission("cookme.set." + effect) || !plugin.isPermissions()) {
                        double percentage = 0.0;
                        try {
                            percentage = Double.valueOf(args[2]);
                        } catch (@SuppressWarnings("unused") final NumberFormatException e) {
                            final String message = plugin.getLocalization().getString("no_number");
                            plugin.message(sender, null, message, null, null);
                            return true;
                        }
                        plugin.getConfig().set("effects." + effect, percentage);
                        plugin.saveConfig();
                        final String message = plugin.getLocalization().getString("changed_effect");
                        plugin.message(sender, null, message, effect, Double.toString(percentage));
                        plugin.getPercentages()[Arrays.asList(plugin.getEffects()).indexOf(effect)] = percentage;
                    } else {
                        final String message = plugin.getLocalization().getString("permission_denied");
                        plugin.message(sender, null, message, null, null);
                    }
                    return true;
                }
                return false;
            }
        } else if (args.length > 0 && "enable".equalsIgnoreCase(args[0])) {
            if (args.length > 1 && "permissions".equalsIgnoreCase(args[1])) {
                if (sender.hasPermission("cookme.enable.permissions") || !plugin.isPermissions()) {
                    cookMeEnablePermissions(sender);
                } else {
                    final String message = plugin.getLocalization().getString("permission_denied");
                    plugin.message(sender, null, message, null, null);
                }
                return true;
            } else if (args.length > 1 && "messages".equalsIgnoreCase(args[1])) {
                if (sender.hasPermission("cookme.enable.messages") || !plugin.isPermissions()) {
                    cookMeEnableMessages(sender);
                } else {
                    final String message = plugin.getLocalization().getString("permission_denied");
                    plugin.message(sender, null, message, null, null);
                }
                return true;
            }
        } else if (args.length > 0 && "disable".equalsIgnoreCase(args[0])) {
            if (args.length > 1 && "permissions".equalsIgnoreCase(args[1])) {
                if (sender.hasPermission("cookme.disable.permissions") || !plugin.isPermissions()) {
                    cookMeDisablePermissions(sender);
                } else {
                    final String message = plugin.getLocalization().getString("permission_denied");
                    plugin.message(sender, null, message, null, null);
                }
                return true;
            } else if (args.length > 1 && "messages".equalsIgnoreCase(args[1])) {
                if (sender.hasPermission("cookme.disable.messages") || !plugin.isPermissions()) {
                    cookMeDisableMessages(sender);
                } else {
                    final String message = plugin.getLocalization().getString("permission_denied");
                    plugin.message(sender, null, message, null, null);
                }
                return true;
            }
        }
        return false;
    }

    private void cookMeHelp(final CommandSender sender) {
        final String message = plugin.getLocalization().getString("help");
        plugin.message(sender, null, message, null, null);

    }

    private void cookMeReload(final CommandSender sender) {
        plugin.loadConfigsAgain();
        final String message = plugin.getLocalization().getString("reload");
        plugin.message(sender, null, message, null, null);
    }

    private void cookMeEnablePermissions(final CommandSender sender) {
        plugin.getConfig().set("configuration.permissions", Boolean.TRUE);
        plugin.saveConfig();
        final String message = plugin.getLocalization().getString("enable_permissions");
        plugin.message(sender, null, message, null, null);
        plugin.setPermissions(true);
    }

    private void cookMeDisablePermissions(final CommandSender sender) {
        plugin.getConfig().set("configuration.permissions", Boolean.FALSE);
        plugin.saveConfig();
        final String message = plugin.getLocalization().getString("disable_permissions");
        plugin.message(sender, null, message, null, null);
        plugin.setPermissions(false);
    }

    private void cookMeEnableMessages(final CommandSender sender) {
        plugin.getConfig().set("configuration.messages", Boolean.TRUE);
        plugin.saveConfig();
        final String message = plugin.getLocalization().getString("enable_messages");
        plugin.message(sender, null, message, null, null);
        plugin.setMessages(true);
    }

    private void cookMeDisableMessages(final CommandSender sender) {
        plugin.getConfig().set("configuration.messages", Boolean.FALSE);
        plugin.saveConfig();
        final String message = plugin.getLocalization().getString("disable_messages");
        plugin.message(sender, null, message, null, null);
        plugin.setMessages(false);
    }
}
