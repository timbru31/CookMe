package de.xghostkillerx.cookme;

import net.minecraft.server.MobEffect;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;

/**
 * CookMePlayerListener
 * Handles the players activities!
 *
 * Refer to the forum thread:
 * 
 * Refer to the dev.bukkit.org page:
 * 
 *
 * @author xGhOsTkiLLeRx
 *
 */

public class CookMePlayerListener extends PlayerListener {

	public static CookMe plugin;
	public CookMePlayerListener(CookMe instance) {
		plugin = instance;
	}

	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		// Check if player is affected
		if (!player.hasPermission("cookme.safe")) {
			// Check for raw food & right clicking
			if (((event.getMaterial() == Material.RAW_BEEF) || (event.getMaterial() == Material.RAW_CHICKEN) || (event.getMaterial() == Material.RAW_FISH) || (event.getMaterial() == Material.ROTTEN_FLESH) || (event.getMaterial() == Material.PORK)) && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
				// Check for food level
				if (player.getFoodLevel() != 20) {
					int randomNumber = (int)(Math.random()*17);
					int randomVenom = (int)(Math.random()*75) +75;
					int randomVenomStrength = (int)(Math.random()*5);
					// Player gets random damage, stack minus 1
					if ((randomNumber == 1) || (randomNumber == 8)) {
						int randomDamage = (int) (Math.random()*11) +1;
						player.damage(randomDamage);
						decreaseItem(player, event);
						if (plugin.config.getBoolean("configuration.messages", true)) {
							player.sendMessage(ChatColor.DARK_RED + "You got some random damage! Eat some cooked food!");
						}
					}
					// Food bar turns green (poison)
					if ((randomNumber >0 ) || (randomNumber == 9)) {
						setMobEffect(player, 17, randomVenom, randomVenomStrength);
						decreaseItem(player, event);
						if (plugin.config.getBoolean("configuration.messages", true)) {
							player.sendMessage(ChatColor.DARK_RED + "Your foodbar is a random time venomed! Eat some cooked food!");
						}
					}
					// Player dies, stack minus 1
					if (randomNumber == 4) {
						player.setHealth(0);
						event.setCancelled(true);
						decreaseItem(player, event);
						if (plugin.config.getBoolean("configuration.messages", true)) {
							player.sendMessage(ChatColor.DARK_RED + "The raw food killed you :(");
						}
					}
					// Random venom damage (including green hearts :) )
					if ((randomNumber == 5) || (randomNumber == 11)) {
						setMobEffect(player, 19, randomVenom, randomVenomStrength);
						decreaseItem(player, event);
						if (plugin.config.getBoolean("configuration.messages", true)) {
							player.sendMessage(ChatColor.DARK_RED + "You are for a random time venomed! Eat some cooked food!");
						}
					}
					// Sets the food level down. Stack minus 1
					if ((randomNumber == 6) || (randomNumber == 12)) {
						int currentFoodLevel = player.getFoodLevel();
						int randomFoodLevel = (int)(Math.random()*currentFoodLevel);
						player.setFoodLevel(randomFoodLevel);
						decreaseItem(player, event);
						if (plugin.config.getBoolean("configuration.messages", true)) {
							player.sendMessage(ChatColor.DARK_RED + "Your food level went down! Eat some cooked food!");
						}
					}
				}
			}
		}
	}

	public void setMobEffect(LivingEntity entity, int type, int duration, int amplifier) {
		((CraftLivingEntity)entity).getHandle().addEffect(new MobEffect(type, duration, amplifier));
	}
	
	@SuppressWarnings("deprecation")
	public void decreaseItem (Player player, PlayerInteractEvent event) {
		ItemStack afterEating = player.getItemInHand();
		afterEating.setAmount(afterEating.getAmount() -1);
		player.setItemInHand(afterEating);
		player.updateInventory();
		event.setCancelled(true);
	}

}
