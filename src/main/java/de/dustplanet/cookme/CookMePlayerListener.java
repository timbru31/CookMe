package de.dustplanet.cookme;

import java.sql.Timestamp;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * CookMePlayerListener. Handles the players activities! Refer to the dev.bukkit.org page: https://dev.bukkit.org/projects/cookme/
 *
 * @author xGhOsTkiLLeRx thanks nisovin for his awesome code snippet!
 */

public class CookMePlayerListener implements Listener {
    private CookMe plugin;
    private boolean message = true;
    private Random random = new Random();

    public CookMePlayerListener(CookMe instance) {
        plugin = instance;
    }

    @EventHandler
    public void onPlayerConsumeItem(PlayerItemConsumeEvent event) {
        String effect = "damage";
        Player player = event.getPlayer();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        boolean mainHand = event.getItem().getType() == player.getInventory().getItemInMainHand().getType();
        if (!player.hasPermission("cookme.safe")) {
            if (sameItem(event.getItem().getType()) && !plugin.getCooldownManager().hasCooldown(player, now)) {
                double temp = 0;
                int effectNumber = 0;
                for (int i = 0; i < plugin.getPercentages().length; i++) {
                    temp += plugin.getPercentages()[i];
                    if (random.nextInt(100) <= temp) {
                        effectNumber = i;
                        break;
                    }
                }
                int effectStrength = 0;
                if (plugin.isRandomEffectStrength()) {
                    effectStrength = random.nextInt(16);
                } else {
                    effectStrength = plugin.getEffectStrengths()[effectNumber];
                }
                int randomEffectTime = random.nextInt(plugin.getMaxDuration() - plugin.getMinDuration() + 1) + plugin.getMinDuration();

                switch (effectNumber) {
                    default:
                    case 0:
                        int randomDamage = random.nextInt(9) + 1;
                        effect = plugin.getLocalization().getString("damage");
                        player.damage(randomDamage);
                        break;
                    case 1:
                        effect = plugin.getLocalization().getString("death");
                        decreaseItem(player, mainHand);
                        player.setHealth(0);
                        break;
                    case 2:
                        effect = plugin.getLocalization().getString("venom");
                        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, randomEffectTime, effectStrength));
                        break;
                    case 3:
                        effect = plugin.getLocalization().getString("hungervenom");
                        player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, randomEffectTime, effectStrength));
                        break;
                    case 4:
                        int currentFoodLevel = player.getFoodLevel();
                        int randomFoodLevel = 0;
                        if (currentFoodLevel != 0) {
                            randomFoodLevel = random.nextInt(currentFoodLevel);
                        }
                        effect = plugin.getLocalization().getString("hungerdecrease");
                        player.setFoodLevel(randomFoodLevel);
                        break;
                    case 5:
                        effect = plugin.getLocalization().getString("confusion");
                        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, randomEffectTime, effectStrength));
                        break;
                    case 6:
                        effect = plugin.getLocalization().getString("blindness");
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, randomEffectTime, effectStrength));
                        break;
                    case 7:
                        effect = plugin.getLocalization().getString("weakness");
                        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, randomEffectTime, effectStrength));
                        break;
                    case 8:
                        effect = plugin.getLocalization().getString("slowness");
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, randomEffectTime, effectStrength));
                        break;
                    case 9:
                        effect = plugin.getLocalization().getString("slowness_blocks");
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, randomEffectTime, effectStrength));
                        break;
                    case 10:
                        effect = plugin.getLocalization().getString("instant_damage");
                        player.addPotionEffect(new PotionEffect(PotionEffectType.HARM, randomEffectTime, effectStrength));
                        break;
                    case 11:
                        effect = plugin.getLocalization().getString("refusing");
                        event.setCancelled(true);
                        break;
                    case 12:
                        effect = plugin.getLocalization().getString("wither");
                        player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, randomEffectTime, effectStrength));
                        break;
                    case 13:
                        effect = plugin.getLocalization().getString("levitation");
                        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, randomEffectTime, effectStrength));
                        break;
                    case 14:
                        effect = plugin.getLocalization().getString("unluck");
                        player.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, randomEffectTime, effectStrength));
                        break;
                }
                message(player, effect);
                if (plugin.getCooldown() != 0) {
                    plugin.getCooldownManager().addPlayer(player);
                }
                event.setCancelled(true);
                if (effectNumber != 11 && effectNumber != 1) {
                    decreaseItem(player, mainHand);
                }
            }
        } else if (plugin.isPreventVanillaPoison()) {
            ItemStack item = event.getItem();
            if (item.getType() == Material.CHICKEN || item.getType() == Material.ROTTEN_FLESH) {
                int foodLevel = player.getFoodLevel();
                if (item.getType() == Material.CHICKEN) {
                    foodLevel += 2;
                } else if (item.getType() == Material.ROTTEN_FLESH) {
                    foodLevel += 4;
                }
                if (foodLevel > 20) {
                    foodLevel = 20;
                }
                player.setFoodLevel(foodLevel);
                decreaseItem(player, mainHand);
                event.setCancelled(true);
            }
        }
    }

    private void message(Player player, String messageToSend) {
        if (plugin.isMessages()) {
            plugin.message(null, player, messageToSend, null, null);
        }
    }

    private boolean sameItem(Material item) {
        for (String itemName : plugin.getItemList()) {
            Material mat = Material.matchMaterial(itemName);
            if (mat == null) {
                if (message) {
                    plugin.getLogger().warning("Couldn't load the foods! Please check your config!");
                    plugin.getLogger().warning("The following item ID/name is invalid: " + itemName);
                    message = false;
                }
                continue;
            }
            if (mat == item) {
                return true;
            }
        }
        return false;
    }

    private void decreaseItem(Player player, boolean mainHand) {
        ItemStack afterEating = null;
        if (mainHand) {
            afterEating = player.getInventory().getItemInMainHand();
        } else {
            afterEating = player.getInventory().getItemInOffHand();
        }
        if (afterEating.getAmount() == 1) {
            if (mainHand) {
                player.getInventory().setItemInMainHand(null);
            } else {
                player.getInventory().setItemInOffHand(null);
            }
        } else {
            afterEating.setAmount(afterEating.getAmount() - 1);
            if (mainHand) {
                player.getInventory().setItemInMainHand(afterEating);
            } else {
                player.getInventory().setItemInOffHand(afterEating);
            }
        }
    }
}
