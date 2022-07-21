package de.dustplanet.cookme;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

/**
 * Handles the cooldown.
 *
 * @author timrbu31
 */

public class CooldownManager {
    /**
     * Cooldown in seconds.
     */
    private int cooldown;
    /**
     * List of player (UUIDs) and current timestamps.
     */
    @SuppressWarnings("PMD.UseConcurrentHashMap")
    private final Map<UUID, Timestamp> cooldownList = new LinkedHashMap<>();

    /**
     * Creates a new CooldownManagr with the given cooldown.
     *
     * @param cooldownValue cooldown in seconds
     */
    public CooldownManager(final int cooldownValue) {
        cooldown = cooldownValue;
    }

    /**
     * Returns the current cooldown.
     *
     * @return cooldown in seconds
     */
    public int getCooldown() {
        return cooldown;
    }

    /**
     * Sets the cooldown value in seconds.
     *
     * @param cooldownValue cooldown in seconds.
     */
    public void setCooldown(final int cooldownValue) {
        cooldown = cooldownValue;
    }

    /**
     * Adds a player to the cooldown list.
     *
     * @param player player to add
     * @return true if added, false if not added (already existing)
     */
    public boolean addPlayer(final Player player) {
        final Timestamp time = new Timestamp(System.currentTimeMillis());
        if (!cooldownList.containsKey(player.getUniqueId())) {
            cooldownList.put(player.getUniqueId(), time);
            return true;
        }
        return false;
    }

    /**
     * Removes a player from the cooldown list.
     *
     * @param player a player to remove
     * @return if the player was removed or not
     */
    public boolean removePlayer(final Player player) {
        if (cooldownList.containsKey(player.getUniqueId())) {
            cooldownList.remove(player.getUniqueId());
            return true;
        }
        return false;
    }

    /**
     * Checks if a player has a cooldown.
     *
     * @param player the player to check
     * @param now a Timestamp
     * @return the boolean value is a player has cooldown or not
     */
    public boolean hasCooldown(final Player player, final Timestamp now) {
        final Timestamp time = cooldownList.get(player.getUniqueId());
        if (time != null) {
            final long difference = (now.getTime() - time.getTime()) / 1000;
            if (difference > cooldown) {
                removePlayer(player);
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * Clears the cooldown list.
     */
    public void clearCooldownList() {
        cooldownList.clear();
    }

    /**
     * Returns the time a player needs to cooldown.
     *
     * @param player the player to check
     * @param now a Timestamp
     * @return the remaining cooldown (or 0)
     */
    public long getRemainingCooldownTime(final Player player, final Timestamp now) {
        final Timestamp time = cooldownList.get(player.getUniqueId());
        if (time != null) {
            final long difference = (now.getTime() - time.getTime()) / 1000;
            if (difference > cooldown) {
                return 0;
            }
            return difference;
        }
        return 0;
    }
}
