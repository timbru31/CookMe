package de.dustplanet.cookme;

import java.sql.Timestamp;
import java.util.HashMap;

import org.bukkit.entity.Player;

/**
 * CookeMe for CraftBukkit/Bukkit
 * Handles the cooldown
 * 
 * Refer to the forum thread:
 * http://bit.ly/cookmebukkit
 * Refer to the dev.bukkit.org page:
 * http://bit.ly/cookmebukkitdev
 *
 * @author xGhOsTkiLLeRx
 * @thanks nisovin for his awesome code snippet!
 * 
 */

public class CooldownManager {

	public CookMe plugin;
	public CooldownManager(CookMe instance) {
		plugin = instance;
	}
	private static int cooldown;
	private static HashMap<String, Timestamp> cooldownList = new HashMap<String, Timestamp>();
	
	// Get the cooldown value
	public static void getCooldown() {
		cooldown = CookMe.config.getInt("configuration.cooldown");
	}
	
	// Add the player with the now time to the hashmap
	public static void addPlayer(Player player) {
		Timestamp time = new Timestamp(System.currentTimeMillis());
		if (!cooldownList.containsKey(player.getName())) cooldownList.put(player.getName(), time);
	}
	
	// Remove player
	public static void removePlayer(Player player) {
		if (cooldownList.containsKey(player.getName())) cooldownList.remove(player.getName());
	}
	
	// Check for the cooldown
	public static boolean hasCooldown(Player player, Timestamp now) {
		getCooldown();
		// If the player is on the list
		if (cooldownList.containsKey(player.getName())) {
			Timestamp time = cooldownList.get(player.getName());
			long difference = (now.getTime()- time.getTime()) / 1000;
			// If the difference is bigger than the cooldown time -> no cooldown anymore
			if (difference > cooldown) {
				removePlayer(player);
				return false;
			}
			else {
				return true;
			}
		}
		return false;
	}
}
