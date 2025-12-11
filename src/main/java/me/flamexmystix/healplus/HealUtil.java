package me.flamexmystix.healplus;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public final class HealUtil {

    private HealUtil() {
        // Utility class
    }

    /**
     * Restores a player's core survival stats.
     */
    public static void healPlayer(Player player) {
        double maxHealth = player.getMaxHealth();
        if (player.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null) {
            maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        }
        player.setHealth(maxHealth);
        player.setFoodLevel(20);
        player.setSaturation(20f);
        player.setFireTicks(0);
    }
}

