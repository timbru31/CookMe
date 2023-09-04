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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Handles the commands.
 *
 * @author timbru31
 */
@SuppressFBWarnings({ "IMC_IMMATURE_CLASS_NO_TOSTRING" })
public class CookMeCommands implements CommandExecutor {
    private final CookMe plugin;

    public CookMeCommands(final CookMe instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
        if (args.length > 0) {
            final String subCommand = args[0].toLowerCase(Locale.ENGLISH);
            switch (subCommand) {
                case "reload":
                case "rl":
                    handleReloadCommand(sender);
                    break;
                case "help":
                    handleHelpCommand(sender);
                    break;
                case "debug":
                    handleDebugCommand(sender);
                    break;
                case "set":
                    handleSetCommand(sender, Arrays.copyOfRange(args, 1, args.length));
                    break;
                case "enable":
                case "disable":
                    handleEnableDisableCommand(sender, subCommand, Arrays.copyOfRange(args, 1, args.length));
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    private void handleReloadCommand(final CommandSender sender) {
        if (sender.hasPermission("cookme.reload") || !plugin.isPermissions()) {
            plugin.loadConfigsAgain();
            plugin.message(sender, "reload", null, null);
        } else {
            plugin.message(sender, "permission_denied", null, null);
        }
    }

    private void handleHelpCommand(final CommandSender sender) {
        if (sender.hasPermission("cookme.help") || !plugin.isPermissions()) {
            plugin.message(sender, "help", null, null);
        } else {
            plugin.message(sender, "permission_denied", null, null);
        }
    }

    private void handleDebugCommand(final CommandSender sender) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            player.setFoodLevel(10);
            player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            for (final PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }
            player.sendMessage(ChatColor.GREEN + "Food level reduced!");
        } else {
            sender.sendMessage(ChatColor.RED + "Please use debug mode in-game!");
        }
    }

    private void handleSetCommand(final CommandSender sender, final String[] args) {
        if (args.length < 2) {
            // Handle insufficient arguments
            sender.sendMessage(ChatColor.RED + "Usage: /cookme set <property> <value>");
            return;
        }

        final String property = args[0].toLowerCase(Locale.ENGLISH);

        if ("cooldown".equals(property)) {
            handleCooldownSetCommand(sender, args);
        } else if ("duration".equals(property)) {
            handleDurationSetCommand(sender, args);
        } else if (Arrays.asList(plugin.getEffectNames()).contains(property)) {
            handleEffectSetCommand(sender, args, property);
        } else {
            sender.sendMessage(ChatColor.RED + "Unknown property: " + property);
        }
    }

    private void handleCooldownSetCommand(final CommandSender sender, final String[] args) {
        if (sender.hasPermission("cookme.cooldown") || !plugin.isPermissions()) {
            if (args.length > 1) {
                int cooldown = 0;
                try {
                    cooldown = Integer.parseInt(args[1]);
                } catch (final NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid cooldown value. Please provide a valid number.");
                    return;
                }
                plugin.getConfig().set("configuration.cooldown", cooldown);
                plugin.saveConfig();
                plugin.message(sender, "changed_cooldown", Integer.toString(cooldown), null);
                plugin.getCooldownManager().setCooldown(cooldown);
                plugin.setCooldown(cooldown);
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /cookme set cooldown <value>");
            }
        } else {
            plugin.message(sender, "permission_denied", null, null);
        }
    }

    private void handleDurationSetCommand(final CommandSender sender, final String[] args) {
        if (sender.hasPermission("cookme.duration") || !plugin.isPermissions()) {
            if (args.length > 1) {
                final String durationType = args[1].toLowerCase(Locale.ENGLISH);
                if ("max".equals(durationType) || "min".equals(durationType)) {
                    if (args.length > 2) {
                        int duration = 0;
                        try {
                            duration = Integer.parseInt(args[2]);
                        } catch (final NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid duration value. Please provide a valid number.");
                            return;
                        }
                        plugin.getConfig().set("configuration.duration." + durationType, duration);
                        plugin.saveConfig();
                        plugin.message(sender, "changed_duration_" + durationType, Integer.toString(duration), null);
                        if ("max".equals(durationType)) {
                            plugin.setMaxDuration(duration);
                        } else {
                            plugin.setMinDuration(duration);
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Usage: /cookme set duration <max|min> <value>");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Invalid duration type. Please use 'max' or 'min'.");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /cookme set duration <max|min> <value>");
            }
        } else {
            plugin.message(sender, "permission_denied", null, null);
        }
    }

    private void handleEffectSetCommand(final CommandSender sender, final String[] args, final String effect) {
        if (sender.hasPermission("cookme.set." + effect) || !plugin.isPermissions()) {
            if (args.length > 1) {
                double percentage = 0.0;
                try {
                    percentage = Double.parseDouble(args[1]);
                } catch (final NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid percentage value. Please provide a valid number.");
                    return;
                }
                plugin.getConfig().set("effects." + effect, percentage);
                plugin.saveConfig();
                plugin.message(sender, "changed_effect", effect, Double.toString(percentage));
                final int effectIndex = Arrays.asList(plugin.getEffectNames()).indexOf(effect);
                plugin.getPercentages()[effectIndex] = percentage;
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /cookme set " + effect + " <value>");
            }
        } else {
            plugin.message(sender, "permission_denied", null, null);
        }
    }

    private void handleEnableDisableCommand(final CommandSender sender, final String action, final String[] args) {
        if (args.length < 1) {
            // Handle insufficient arguments
            sender.sendMessage(ChatColor.RED + "Usage: /cookme " + action + " <property>");
            return;
        }

        final String property = args[0].toLowerCase(Locale.ENGLISH);

        if ("enable".equals(action)) {
            handleEnableProperty(sender, property);
        } else if ("disable".equals(action)) {
            handleDisableProperty(sender, property);
        } else {
            sender.sendMessage(ChatColor.RED + "Invalid action: " + action);
        }
    }

    private void handleEnableProperty(final CommandSender sender, final String property) {
        if ("permissions".equals(property)) {
            if (sender.hasPermission("cookme.enable.permissions") || !plugin.isPermissions()) {
                plugin.getConfig().set("configuration.permissions", Boolean.TRUE);
                plugin.saveConfig();
                plugin.message(sender, "enable_permissions", null, null);
                plugin.setPermissions(true);
            } else {
                plugin.message(sender, "permission_denied", null, null);
            }
        } else if ("messages".equals(property)) {
            if (sender.hasPermission("cookme.enable.messages") || !plugin.isPermissions()) {
                plugin.getConfig().set("configuration.messages", Boolean.TRUE);
                plugin.saveConfig();
                plugin.message(sender, "enable_messages", null, null);
                plugin.setMessages(true);
            } else {
                plugin.message(sender, "permission_denied", null, null);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Invalid property: " + property);
        }
    }

    private void handleDisableProperty(final CommandSender sender, final String property) {
        if ("permissions".equals(property)) {
            if (sender.hasPermission("cookme.disable.permissions") || !plugin.isPermissions()) {
                plugin.getConfig().set("configuration.permissions", Boolean.FALSE);
                plugin.saveConfig();
                plugin.message(sender, "disable_permissions", null, null);
                plugin.setPermissions(false);
            } else {
                plugin.message(sender, "permission_denied", null, null);
            }
        } else if ("messages".equals(property)) {
            if (sender.hasPermission("cookme.disable.messages") || !plugin.isPermissions()) {
                plugin.getConfig().set("configuration.messages", Boolean.FALSE);
                plugin.saveConfig();
                plugin.message(sender, "disable_messages", null, null);
                plugin.setMessages(false);
            } else {
                plugin.message(sender, "permission_denied", null, null);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Invalid property: " + property);
        }
    }

}
