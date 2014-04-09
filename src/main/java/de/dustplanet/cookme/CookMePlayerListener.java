package de.dustplanet.cookme;

import java.sql.Timestamp;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * CookMePlayerListener
 * Handles the players activities!

 * Refer to the dev.bukkit.org page:
 * http://dev.bukkit.org/bukkit-plugins/cookme/
 * 
 * @author xGhOsTkiLLeRx
 * thanks nisovin for his awesome code snippet!
 * 
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
	// Just in case to prevent NPE
	String effect = "damage";
	Player player = event.getPlayer();
	Timestamp now = new Timestamp(System.currentTimeMillis());
	// Check if player is affected
	if (!player.hasPermission("cookme.safe")) {
	    // Check for item & right clicking
	    if (sameItem(player.getItemInHand().getType()) && !plugin.cooldownManager.hasCooldown(player, now)) {
		// Make a temp double and a value between 0 and 99
		double temp = 0;
		int effectNumber = 0;
		// Get the number for the effect
		for (int i = 0; i < plugin.percentages.length; i++) {
		    temp += plugin.percentages[i];
		    if (random.nextInt(100) <= temp) {
			effectNumber = i;
			break;
		    }
		}
		// EffectStrength, Duration etc.
		int effectStrength = 0;
		if (plugin.randomEffectStrength) {
		    effectStrength = random.nextInt(16);
		} else {
		    effectStrength = plugin.effectStrengths[effectNumber];
		}
		int randomEffectTime = (random.nextInt((plugin.maxDuration - plugin.minDuration) + 1) + plugin.minDuration);

		// TODO Find a beter way then this if number is this, number is that case
		switch(effectNumber) {
		default:
		case 0:
		    // Player gets random damage, stack minus 1
		    int randomDamage = random.nextInt(9) + 1;
		    effect = plugin.localization.getString("damage");
		    player.damage(randomDamage);
		    break;
		case 1:
		    // Player dies, stack minus 1
		    effect = plugin.localization.getString("death");
		    decreaseItem(player);
		    player.setHealth(0);
		    break;
		case 2:
		    // Random venom damage (including green hearts :) )
		    effect = plugin.localization.getString("venom");
		    player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, randomEffectTime, effectStrength));
		    break;
		case 3:
		    // Food bar turns green (poison)
		    effect = plugin.localization.getString("hungervenom");
		    player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, randomEffectTime, effectStrength));
		    break;
		case 4:
		    // Sets the food level down. Stack minus 1
		    int currentFoodLevel = player.getFoodLevel();
		    int randomFoodLevel = 0;
		    if (currentFoodLevel != 0) {
			randomFoodLevel = random.nextInt(currentFoodLevel);
		    }
		    effect = plugin.localization.getString("hungerdecrease");
		    player.setFoodLevel(randomFoodLevel);
		    break;
		case 5:
		    // Confusion
		    effect = plugin.localization.getString("confusion");
		    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, randomEffectTime, effectStrength));
		    break;
		case 6:
		    // Blindness
		    effect = plugin.localization.getString("blindness");
		    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, randomEffectTime, effectStrength));
		    break;
		case 7:
		    // Weakness
		    effect = plugin.localization.getString("weakness");
		    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, randomEffectTime, effectStrength));
		    break;
		case 8:
		    // Slowness
		    effect = plugin.localization.getString("slowness");
		    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, randomEffectTime, effectStrength));
		    break;
		case 9:
		    // Slowness for blocks
		    effect = plugin.localization.getString("slowness_blocks");
		    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, randomEffectTime, effectStrength));
		    break;
		case 10:
		    // Instant Damage
		    effect = plugin.localization.getString("instant_damage");
		    player.addPotionEffect(new PotionEffect(PotionEffectType.HARM, randomEffectTime, effectStrength));
		    break;
		case 11:
		    // Refusing
		    effect = plugin.localization.getString("refusing");
		    event.setCancelled(true);
		    break;
		case 12:
		    // Wither effect
		    effect = plugin.localization.getString("wither");
		    player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, randomEffectTime, effectStrength));
		    break;
		}
		// Message player
		message(player, effect);
		// Add player to cooldown list
		if (plugin.cooldown != 0) {
		    plugin.cooldownManager.addPlayer(player);
		}
		// Cancel event and reduce food
		event.setCancelled(true);
		// Death needs to be called before, otherwise food is not reduced, because it's dropped
		if (effectNumber != 11 && effectNumber != 1) {
		    decreaseItem(player);
		}
	    }
	} else if (plugin.preventVanillaPoison) {
	    ItemStack item = event.getItem();
	    // Prevent the vanilla poison, too?
	    if (item.getType() == Material.RAW_CHICKEN || item.getType() == Material.ROTTEN_FLESH) {
		int foodLevel = player.getFoodLevel();
		// Case chicken
		if (item.getType() == Material.RAW_CHICKEN) {
		    // Quote: 1 unit of hunger (2 hunger)
		    foodLevel += 2;
		} else if (item.getType() == Material.ROTTEN_FLESH) {
		    // Case rotten flesh
		    // Quote: 2 units of hunger (4 hunger)
		    foodLevel += 4;
		}
		// Not higher than 20
		if (foodLevel > 20) {
		    foodLevel = 20;
		}
		player.setFoodLevel(foodLevel);
		decreaseItem(player);
		event.setCancelled(true);
	    }
	}
    }

    private void message(Player player, String message) {
	if (plugin.messages) {
	    plugin.message(null, player, message, null, null);
	}
    }

    // Is the item in the list? Yes or no
    private boolean sameItem(Material item) {
	for (String itemName : plugin.itemList) {
	    // Get the Material
	    Material mat = Material.matchMaterial(itemName);
	    // Not valid
	    if (mat == null) {
		// Prevent spamming
		if (message) {
		    plugin.getLogger().warning("Couldn't load the foods! Please check your config!");
		    plugin.getLogger().warning("The following item ID/name is invalid: " + itemName);
		    message = false;
		}
		// Go on
		continue;
	    }
	    // Get ID & compare
	    if (mat == item) {
		return true;
	    }
	}
	return false;
    }

    // Sets the raw food -1
    private void decreaseItem (Player player) {
	ItemStack afterEating = player.getItemInHand();
	if (afterEating.getAmount() == 1) {
	    player.setItemInHand(null);
	} else {
	    afterEating.setAmount(afterEating.getAmount() - 1);
	    player.setItemInHand(afterEating);
	}
    }
}