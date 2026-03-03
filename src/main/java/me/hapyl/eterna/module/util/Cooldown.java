package me.hapyl.eterna.module.util;

/**
 * Represents an object that may have a cooldown.
 */
public interface Cooldown {
    
    /**
     * Gets the cooldown, in ticks.
     *
     * @return the cooldown, in ticks.
     */
    int getCooldown();
    
    /**
     * Gets the cooldown, in seconds.
     *
     * @return the cooldown, in seconds.
     */
    default float getCooldownSeconds() {
        return this.getCooldown() / 20f;
    }
    
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
     * @param cooldownSeconds - The cooldown to set.
     */
    default void setCooldownSeconds(float cooldownSeconds) {
        this.setCooldown((int) (cooldownSeconds * 20));
    }
    
}
