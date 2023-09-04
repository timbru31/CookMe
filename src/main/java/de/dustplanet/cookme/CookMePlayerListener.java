package de.dustplanet.cookme;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Random;

import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Handles the player's activities.
 *
 * @author timbru31
 */
@SuppressFBWarnings({ "IMC_IMMATURE_CLASS_NO_TOSTRING", "FCCD_FIND_CLASS_CIRCULAR_DEPENDENCY", "CD_CIRCULAR_DEPENDENCY" })
public class CookMePlayerListener implements Listener {
    private final CookMe plugin;
    private boolean message = true;
    private final Random random = new SecureRandom();

    public CookMePlayerListener(final CookMe instance) {
        plugin = instance;
    }

    @EventHandler
    public void onPlayerConsumeItem(final PlayerItemConsumeEvent event) {
        final Player player = event.getPlayer();
        final ItemStack consumedItem = event.getItem();
        final Timestamp now = new Timestamp(System.currentTimeMillis());
        final boolean mainHand = consumedItem.getType() == player.getInventory().getItemInMainHand().getType();

        if (!player.hasPermission("cookme.safe")) {
            if (isConsumableItem(consumedItem) && !plugin.getCooldownManager().hasCooldown(player, now)) {
                applyRandomEffect(player, mainHand);
                event.setCancelled(true);
            }
        } else if (plugin.isPreventVanillaPoison() && isVanillaPoisonItem(consumedItem)) {
            handleVanillaPoisonConsumption(player, consumedItem, mainHand);
        }
    }

    private void applyRandomEffect(final Player player, final boolean mainHand) {
        final int effectNumber = chooseRandomEffect();
        final String effectName = getEffectName(effectNumber);

        switch (effectNumber) {
            case 0:
                handleDamageEffect(player);
                break;
            case 1:
                handleDeathEffect(player, mainHand);
                break;
            case 4:
                handleHungerEffect(player);
                break;
            case 11:
                break;
            default:
                handlePotionEffect(player, effectNumber);
                break;
        }

        messagePlayer(player, effectName);

        if (plugin.getCooldown() != 0) {
            plugin.getCooldownManager().addPlayer(player);
        }
        if (effectNumber != 11 && effectNumber != 1) {
            decreaseItem(player, mainHand);
        }
    }

    private int chooseRandomEffect() {
        double cumulativePercentage = 0;
        for (int percentageIndex = 0; percentageIndex < plugin.getPercentages().length; percentageIndex++) {
            cumulativePercentage += plugin.getPercentages()[percentageIndex];
            if (random.nextInt(100) < cumulativePercentage) {
                return percentageIndex;
            }
        }
        return -1; // No effect chosen
    }

    private String getEffectName(final int effectNumber) {
        if (effectNumber >= 0 && effectNumber < plugin.getEffectNames().length) {
            return plugin.getEffectNames()[effectNumber];
        }
        return "Unknown Effect";
    }

    private void handleDamageEffect(final Player player) {
        final int randomDamage = random.nextInt(9) + 1;
        player.damage(randomDamage);
    }

    private void handleDeathEffect(final Player player, final boolean mainHand) {
        decreaseItem(player, mainHand);
        player.setHealth(0);
    }

    private void handleHungerEffect(final Player player) {
        final int currentFoodLevel = player.getFoodLevel();
        int randomFoodLevel = 0;
        if (currentFoodLevel != 0) {
            randomFoodLevel = random.nextInt(currentFoodLevel);
        }
        player.setFoodLevel(randomFoodLevel);
    }

    private void handlePotionEffect(final Player player, final int effectNumber) {
        final int effectStrength = getEffectStrength(effectNumber);
        final int randomEffectTime = getRandomEffectTime();
        final String effect = getEffectName(effectNumber);

        final PotionEffectType potionEffectType = getPotionEffectType(effect);
        if (potionEffectType != null) {
            player.addPotionEffect(new PotionEffect(potionEffectType, randomEffectTime, effectStrength));
        }
    }

    @Nullable
    private PotionEffectType getPotionEffectType(final String effect) {
        switch (effect) {
            case "venom":
                return PotionEffectType.POISON;
            case "hungervenom":
                return PotionEffectType.HUNGER;
            case "confusion":
                return PotionEffectType.CONFUSION;
            case "blindness":
                return PotionEffectType.BLINDNESS;
            case "weakness":
                return PotionEffectType.WEAKNESS;
            case "slowness":
                return PotionEffectType.SLOW;
            case "slowness_blocks":
                return PotionEffectType.SLOW_DIGGING;
            case "instant_damage":
                return PotionEffectType.HARM;
            case "wither":
                return PotionEffectType.WITHER;
            case "levitation":
                return PotionEffectType.LEVITATION;
            case "unluck":
                return PotionEffectType.UNLUCK;
            case "bad_omen":
                return PotionEffectType.BAD_OMEN;
            default:
                return null;
        }
    }

    private int getEffectStrength(final int effectNumber) {
        if (plugin.isRandomEffectStrength()) {
            return random.nextInt(16);
        }
        return plugin.getEffectStrengths()[effectNumber];
    }

    private int getRandomEffectTime() {
        return random.nextInt(plugin.getMaxDuration() - plugin.getMinDuration() + 1) + plugin.getMinDuration();
    }

    private void handleVanillaPoisonConsumption(final Player player, final ItemStack consumedItem, final boolean mainHand) {
        int foodLevel = player.getFoodLevel();

        if (consumedItem.getType() == Material.CHICKEN) {
            foodLevel += 2;
        } else if (consumedItem.getType() == Material.ROTTEN_FLESH) {
            foodLevel += 4;
        }

        foodLevel = Math.min(foodLevel, 20);
        player.setFoodLevel(foodLevel);
        decreaseItem(player, mainHand);
    }

    private void messagePlayer(final Player player, final String messageToSend) {
        if (plugin.isMessages() && messageToSend != null) {
            plugin.message(player, messageToSend, null, null);
        }
    }

    private boolean isConsumableItem(final ItemStack item) {
        final Material itemType = item.getType();
        for (final String itemName : plugin.getItemList()) {
            final Material mat = Material.matchMaterial(itemName);
            if (mat == null) {
                if (message) {
                    plugin.getLogger().warning("Couldn't load the foods! Please check your config!");
                    plugin.getLogger().warning("The following item ID/name is invalid: " + itemName);
                    message = false;
                }
                continue;
            }
            if (mat == itemType) {
                return true;
            }
        }
        return false;
    }

    private boolean isVanillaPoisonItem(final ItemStack item) {
        final Material itemType = item.getType();
        return itemType == Material.CHICKEN || itemType == Material.ROTTEN_FLESH;
    }

    private void decreaseItem(final Player player, final boolean mainHand) {
        final ItemStack consumedItem = mainHand ? player.getInventory().getItemInMainHand() : player.getInventory().getItemInOffHand();

        if (consumedItem.getAmount() == 1) {
            if (mainHand) {
                player.getInventory().setItemInMainHand(null);
            } else {
                player.getInventory().setItemInOffHand(null);
            }
        } else {
            consumedItem.setAmount(consumedItem.getAmount() - 1);
            if (mainHand) {
                player.getInventory().setItemInMainHand(consumedItem);
            } else {
                player.getInventory().setItemInOffHand(consumedItem);
            }
        }
    }
}
