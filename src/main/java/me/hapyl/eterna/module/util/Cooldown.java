package me.hapyl.eterna.module.util;

/**
 * Represents something that may have a cooldown.
 */
public interface Cooldown {
    
    /**
     * Gets the cooldown, in ticks.
     *
     * @return the cooldown, in ticks.
     */
    int getCooldown();
    
    /**
     * Sets the cooldown, in ticks.
     *
     * @param cooldown - The cooldown to set.
     */
    void setCooldown(int cooldown);
    
    /**
     * Gets whether the cooldown is set.
     *
     * @return the cooldown or {@code 0} if unset.
     */
    default boolean hasCooldown() {
        return getCooldown() > 0;
    }
    
    /**
     * Sets the cooldown, in seconds.
     *
     * @param cooldownSec - The cooldown to set.
     */
    default void setCooldownSec(float cooldownSec) {
        this.setCooldown((int) (cooldownSec * 20));
    }
    
}
