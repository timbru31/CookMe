package de.dustplanet.cookme;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

/**
 * CookeMe for CraftBukkit/Spigot.
 * Handles the cooldown.
 *
 * Refer to the dev.bukkit.org page:
 * http://dev.bukkit.org/bukkit-plugins/cookme/
 *
 * @author xGhOsTkiLLeRx
 * thanks nisovin for his awesome code snippet!
 *
 */

public class CooldownManager {
    /**
     * Cooldown in seconds.
     */
    private int cooldown;
    /**
     * List of player (UUIDs) and current timestamps.
     */
    private LinkedHashMap<UUID, Timestamp> cooldownList = new LinkedHashMap<>();

    // Sets the cooldown!
    /**
     * Creates a new CooldownManagr with the given cooldown.
     * @param cooldownValue cooldown in seconds
     */
    public CooldownManager(int cooldownValue) {
        cooldown = cooldownValue;
    }

    // Get the cooldown value
    /**
     * Returns the current cooldown.
     * @return cooldown in seconds
     */
    public int getCooldown() {
        return cooldown;
    }

    // Set the cooldown value
    /**
     * Sets the cooldown value in seconds.
     * @param cooldownValue cooldown in seconds.
     */
    public void setCooldown(int cooldownValue) {
        cooldown = cooldownValue;
    }

    // Add the player with the now time to the hashmap
    /**
     * Adds a player to the cooldown list.
     * @param player player to add
     * @return true if added, false if not added (already existing)
     */
    public boolean addPlayer(Player player) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        if (!cooldownList.containsKey(player.getUniqueId())) {
            cooldownList.put(player.getUniqueId(), time);
            return true;
        }
        return false;
    }

    // Remove player
    /**
     * Removes a player from the cooldown list.
     * @param player a player to remove
     * @return if the player was removed or not
     */
    public boolean removePlayer(Player player) {
        if (cooldownList.containsKey(player.getUniqueId())) {
            cooldownList.remove(player.getUniqueId());
            return true;
        }
        return false;
    }

    // Check for the cooldown
    /**
     * Checks if a player has a cooldown.
     * @param player the player to check
     * @param now a Timestamp
     * @return the boolean value is a player has cooldown or not
     */
    public boolean hasCooldown(Player player, Timestamp now) {
        // If the player is on the list
        if (cooldownList.containsKey(player.getUniqueId())) {
            Timestamp time = cooldownList.get(player.getUniqueId());
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
    /**
     * Clears the cooldown list.
     */
    public void clearCooldownList() {
        cooldownList.clear();
    }

    // Returns the rest of the cooldown time
    /**
     * Returns the time a player needs to cooldown.
     * @param player the player to check
     * @param now a Timestamp
     * @return the remaining cooldown (or 0)
     */
    public long getRemainingCooldownTime(Player player, Timestamp now) {
        if (cooldownList.containsKey(player.getUniqueId())) {
            Timestamp time = cooldownList.get(player.getUniqueId());
            long difference = (now.getTime() - time.getTime()) / 1000;
            // If the difference is bigger than the cooldown time -> no cooldown
            // anymore
            if (difference > cooldown) {
                return 0;
            }
            return difference;
        }
        return 0;
    }
}
