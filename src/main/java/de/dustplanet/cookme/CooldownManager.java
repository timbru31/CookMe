package de.dustplanet.cookme;

import java.sql.Timestamp;
import java.util.LinkedHashMap;

import org.bukkit.entity.Player;

/**
 * CookeMe for CraftBukkit/Bukkit
 * Handles the cooldown
 * 
 * Refer to the dev.bukkit.org page:
 * http://dev.bukkit.org/bukkit-plugins/cookme/
 * 
 * @author xGhOsTkiLLeRx
 * thanks nisovin for his awesome code snippet!
 * 
 */

public class CooldownManager {
    private int cooldown = 0;
    private LinkedHashMap<String, Timestamp> cooldownList = new LinkedHashMap<String, Timestamp>();

    // Sets the cooldown!
    public CooldownManager(int cooldownValue) {
	cooldown = cooldownValue;
    }

    // Remember to set the cooldown!
    public CooldownManager() {
    }

    // Get the cooldown value
    public int getCooldown() {
	return cooldown;
    }

    // Set the cooldown value
    public void setCooldown(int cooldownValue) {
	cooldown = cooldownValue;
    }

    // Add the player with the now time to the hashmap
    public boolean addPlayer(Player player) {
	Timestamp time = new Timestamp(System.currentTimeMillis());
	if (!cooldownList.containsKey(player.getName())) {
	    cooldownList.put(player.getName(), time);
	    return true;
	}
	return false;
    }

    // Remove player
    public boolean removePlayer(Player player) {
	if (cooldownList.containsKey(player.getName())) {
	    cooldownList.remove(player.getName());
	    return true;
	}
	return false;
    }

    // Check for the cooldown
    public boolean hasCooldown(Player player, Timestamp now) {
	// If the player is on the list
	if (cooldownList.containsKey(player.getName())) {
	    Timestamp time = cooldownList.get(player.getName());
	    long difference = (now.getTime() - time.getTime()) / 1000;
	    // If the difference is bigger than the cooldown time -> no cooldown
	    // anymore
	    if (difference > cooldown) {
		removePlayer(player);
	    } else {
		return true;
	    }
	}
	return false;
    }

    // Makes the list empty
    public void clearCooldownList() {
	cooldownList.clear();
    }

    // Returns the rest of the cooldown time
    public long getRemainingCooldownTime(Player player, Timestamp now) {
	if (cooldownList.containsKey(player.getName())) {
	    Timestamp time = cooldownList.get(player.getName());
	    long difference = (now.getTime() - time.getTime()) / 1000;
	    // If the difference is bigger than the cooldown time -> no cooldown
	    // anymore
	    if (difference > cooldown) {
		return 0;
	    } else {
		return difference;
	    }
	}
	return 0;
    }
}