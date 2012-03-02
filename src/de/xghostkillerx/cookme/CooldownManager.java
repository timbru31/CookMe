package de.xghostkillerx.cookme;

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
	private static HashMap<Player, Timestamp> cooldownList = new HashMap<Player, Timestamp>();
	
	// Get the cooldown value
	public static void getCooldown() {
		cooldown = CookMe.config.getInt("configuration.cooldown");
	}
	
	// Add the player with the now time to the hashmap
	public static void addPlayer(Player name) {
		Timestamp time = new Timestamp(System.currentTimeMillis());
		cooldownList.put(name, time);
	}
	
	// Remove player
	public static void removePlayer(Player name) {
		cooldownList.remove(name);
	}
	
	// Check for the cooldown
	public static boolean hasCooldown(Player name, Timestamp now) {
		getCooldown();
		// If the player is on the list
		if (cooldownList.containsKey(name)) {
			Timestamp time = cooldownList.get(name);
			long difference = (now.getTime()- time.getTime()) / 1000;
			// If the difference is bigger than the cooldown time -> no cooldown anymore
			if (difference > cooldown) {
				removePlayer(name);
				return false;
			}
			else {
				return true;
			}
		}
		return false;
	}
}
