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
 * http://bit.ly/cookmebukkit
 * Refer to the dev.bukkit.org page:
 * http://bit.ly/cookmebukkitdev
 *
 * @author xGhOsTkiLLeRx
 * @thanks nisovin
 *
 */

public class CookMePlayerListener extends PlayerListener {

	public static CookMe plugin;
	public CookMePlayerListener(CookMe instance) {
		plugin = instance;
	}
	public int message = 0;

	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		// Check if player is affected
		if (!player.hasPermission("cookme.safe")) {
			// Check for item & right clicking
			if (sameItem(player.getItemInHand().getTypeId()) == true && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
				// Check for food level
				if (player.getFoodLevel() != 20) {
					int randomNumber = (int)(Math.random()*25) +1, randomEffectStrength = (int)(Math.random()*16);
					int minimum = 20*plugin.config.getInt("configuration.duration.min"), maximum = 20*plugin.config.getInt("configuration.duration.max");
					int randomEffectTime = (int)(Math.random() * ((maximum - minimum)  + 1)  + minimum);
					//int randomEffectTime = (int)(Math.)
					// Player gets random damage, stack minus 1
					if (plugin.config.getBoolean("effects.damage") == true) {
						if ((randomNumber == 1) || (randomNumber == 12)) {
							int randomDamage = (int) (Math.random()*9) +1;
							decreaseItem(player, event);
							player.damage(randomDamage);
							if (plugin.config.getBoolean("configuration.messages") == true) {
								player.sendMessage(ChatColor.DARK_RED + "You got some random damage! Eat some cooked food!");
							}
						}
					}
					// Food bar turns green (poison)
					if (plugin.config.getBoolean("effects.hungervenom") == true) {
						if ((randomNumber == 2 ) || (randomNumber == 13)) {
							decreaseItem(player, event);
							setMobEffect(player, 17, randomEffectTime, randomEffectStrength);
							if (plugin.config.getBoolean("configuration.messages") == true) {
								player.sendMessage(ChatColor.DARK_RED + "Your foodbar is a random time venomed! Eat some cooked food!");
							}
						}
					}
					// Player dies, stack minus 1
					if (plugin.config.getBoolean("effects.death") == true) {
						if (randomNumber == 4 ) {
							decreaseItem(player, event);
							player.setHealth(0);
							if (plugin.config.getBoolean("configuration.messages") == true) {
								player.sendMessage(ChatColor.DARK_RED + "The raw food killed you :(");
							}
						}
					}
					// Random venom damage (including green hearts :) )
					if (plugin.config.getBoolean("effects.venom") == true) {
						if ((randomNumber == 5) || (randomNumber == 14)) {
							decreaseItem(player, event);
							setMobEffect(player, 19, randomEffectTime, randomEffectStrength);
							if (plugin.config.getBoolean("configuration.messages") == true) {
								player.sendMessage(ChatColor.DARK_RED + "You are for a random time venomed! Eat some cooked food!");
							}
						}
					}
					// Sets the food level down. Stack minus 1
					if (plugin.config.getBoolean("effects.hungerdecrease") == true) {
						if ((randomNumber == 6) || (randomNumber == 15)) {
							int currentFoodLevel = player.getFoodLevel(), randomFoodLevel = (int)(Math.random()*currentFoodLevel);
							decreaseItem(player, event);
							player.setFoodLevel(randomFoodLevel);
							if (plugin.config.getBoolean("configuration.messages") == true) {
								player.sendMessage(ChatColor.DARK_RED + "Your food level went down! Eat some cooked food!");
							}
						}
					}
					// Confusion
					if (plugin.config.getBoolean("effects.confusion") == true) {
						if ((randomNumber == 7) || (randomNumber == 16)) {
							decreaseItem(player, event);
							setMobEffect(player, 9, randomEffectTime, randomEffectStrength);
							if (plugin.config.getBoolean("configuration.messages") == true) {
								player.sendMessage(ChatColor.DARK_RED + "You are for a random time confused! Eat some cooked food!");
							}
						}
					}
					// Blindness
					if (plugin.config.getBoolean("effects.blindness") == true) {
						if ((randomNumber == 8) || (randomNumber == 17)) {
							decreaseItem(player, event);
							setMobEffect(player, 15, randomEffectTime, randomEffectStrength);
							if (plugin.config.getBoolean("configuration.messages") == true) {
								player.sendMessage(ChatColor.DARK_RED + "You are for a random time blind! Eat some cooked food!");
							}
						}
					}
					// Weakness
					if (plugin.config.getBoolean("effects.weakness") == true) {
						if ((randomNumber == 9) || (randomNumber == 18)) {
							decreaseItem(player, event);
							setMobEffect(player, 18, randomEffectTime, randomEffectStrength);
							if (plugin.config.getBoolean("configuration.messages") == true) {
								player.sendMessage(ChatColor.DARK_RED + "You are for a random time weaked! Eat some cooked food!");
							}
						}
					}
					// Slowness
					if (plugin.config.getBoolean("effects.slowness") == true) {
						if ((randomNumber == 10) || (randomNumber == 19)) {
							decreaseItem(player, event);
							setMobEffect(player, 2, randomEffectTime, randomEffectStrength);
							if (plugin.config.getBoolean("configuration.messages") == true) {
								player.sendMessage(ChatColor.DARK_RED + "You are for a random time slower! Eat some cooked food!");
							}
						}
					}
					// Slowness for blocks
					if (plugin.config.getBoolean("effects.slowness_blocks") == true) {
						if ((randomNumber == 11) || (randomNumber == 20)) {
							decreaseItem(player, event);
							setMobEffect(player, 4, randomEffectTime, randomEffectStrength);
							if (plugin.config.getBoolean("configuration.messages") == true) {
								player.sendMessage(ChatColor.DARK_RED + "You mine for a random time slower! Eat some cooked food!");
							}
						}
					}
				}
			}
		}
	}

	// Is the item in the list? Yes or no
	private boolean sameItem(int item) {
		for (int i = 0; i < plugin.itemList.size(); i++) {
			String itemName = plugin.itemList.get(i);
			try {
				Material material = Material.valueOf(itemName);
				if (material.getId() == item) {
					return true;
				}
			}
			catch (Exception e) {
				// Prevent spamming
				if (message == 0) {
					CookMe.log.warning("CookMe couldn't load the foods! Please check your config!");
					message = 1;
				}
			}
		}
		return false;
	}

	/* Sets the specific mob effect! BIG THANKS @nisovin for his awesome code!
	 * http://www.wiki.vg/Protocol#Effects
	 * 
	 * int type = ID value
	 * int duration = in ticks (20 ticks = 1 second)
	 * int amplifier = how fast the effect is applied (0 to 15)
	 * 
	 */
	public void setMobEffect(LivingEntity entity, int type, int duration, int amplifier) {
		((CraftLivingEntity)entity).getHandle().addEffect(new MobEffect(type, duration, amplifier));
	}

	// Sets the raw food -1
	@SuppressWarnings("deprecation")
	public void decreaseItem (Player player, PlayerInteractEvent event) {
		ItemStack afterEating = player.getItemInHand();
		if (afterEating.getAmount() == 1) {
			player.setItemInHand(null);
			player.updateInventory();
			event.setCancelled(true);
		}
		else {
			afterEating.setAmount(afterEating.getAmount() -1);
			player.setItemInHand(afterEating);
			player.updateInventory();
			event.setCancelled(true);
		}
	}
}
