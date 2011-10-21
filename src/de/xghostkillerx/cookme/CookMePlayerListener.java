package de.xghostkillerx.cookme;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;

/**
 * UnlimitedLavaPlayerListener
 * Handles the players activities!
 *
 * Refer to the forum thread:
 * http://bit.ly/n1Wex2
 * Refer to the dev.bukkit.org page:
 * http://bit.ly/pCj7v3
 *
 * @author xGhOsTkiLLeRx
 * @thanks to loganwm for the help!!
 * @thanks to Edward Hand for the idea and original InfiniteLava plugin!
 *
 */

public class CookMePlayerListener extends PlayerListener {

	public static CookMe plugin;
	public CookMePlayerListener(CookMe instance) {
		plugin = instance;
	}

	@SuppressWarnings("deprecation")
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		// Check if player is affected
		if (!player.hasPermission("cookme.safe")) {
			// Check for raw food & right clicking
			if (((event.getMaterial() == Material.RAW_BEEF) || (event.getMaterial() == Material.RAW_CHICKEN) || (event.getMaterial() == Material.RAW_FISH) || (event.getMaterial() == Material.ROTTEN_FLESH) || (event.getMaterial() == Material.PORK)) && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
				// Check for food level
				if (player.getFoodLevel() != 20) {
					int randomNumber = (int)(Math.random()*15);
					// Player gets random damage, stack minus 1
					if ((randomNumber == 1) || (randomNumber == 8)) {
						int randomDamage = (int) (Math.random()*11)+1;
						player.damage(randomDamage);
						if (plugin.config.getBoolean("configuration.messages", true)) {
							player.sendMessage(ChatColor.DARK_RED + "You got some random damage! Eat some cooked food!");
						}
						ItemStack afterEating = player.getItemInHand();
						afterEating.setAmount(afterEating.getAmount() -1);
						player.setItemInHand(afterEating);
						player.updateInventory();
						event.setCancelled(true);
					}
					if ((randomNumber == 2) || (randomNumber == 9)) {
						// will fill that
					}
					// Nothing happens, everything is normal
					if ((randomNumber == 3) || (randomNumber == 7) || (randomNumber == 13) || (randomNumber == 0) || (randomNumber == 14)) {
						return;
					}
					// Player dies, stack minus 1
					if ((randomNumber == 4) || (randomNumber == 10)) {
						ItemStack afterEating = player.getItemInHand();
						afterEating.setAmount(afterEating.getAmount() -1);
						player.setItemInHand(afterEating);
						player.updateInventory();
						if (plugin.config.getBoolean("configuration.messages", true)) {
							player.sendMessage(ChatColor.DARK_RED + "The raw food killed you :(");
						}
						player.setHealth(0);
						event.setCancelled(true);
					}
					/*if ((randomNumber == 5) || (randomNumber == 11)) {
							//grüne Leiste
					}*/
					// Sets the food level down. Stack minus 1
					if ((randomNumber == 6) || (randomNumber == 12)) {
						int currentFoodLevel = player.getFoodLevel();
						int randomFoodLevel = (int)(Math.random()*currentFoodLevel);
						player.setFoodLevel(randomFoodLevel);
						if (plugin.config.getBoolean("configuration.messages", true)) {
							player.sendMessage(ChatColor.DARK_RED + "Your food level went down! Eat some cooked food!");
						}
						ItemStack afterEating = player.getItemInHand();
						afterEating.setAmount(afterEating.getAmount() -1);
						player.setItemInHand(afterEating);
						player.updateInventory();
						event.setCancelled(true);
					}
				}
			}
		}
	}
}