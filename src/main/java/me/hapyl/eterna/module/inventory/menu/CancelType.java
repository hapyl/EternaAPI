package me.hapyl.eterna.module.inventory.menu;

import me.hapyl.eterna.module.annotate.DefaultEnumValue;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

/**
 * Defines the {@link ClickType} cancel type for {@link PlayerMenu}.
 *
 * <p><b>Note that this only refers to cancelling the click event, actions will be executed regardless.</b></p>
 */
public enum CancelType {
    
    /**
     * Cancels all clicks inside the {@link PlayerMenu}.
     */
    INSIDE_ONLY,
    
    /**
     * Cancels all clicks outside the {@link PlayerMenu}, which include {@link Player} inventory.
     */
    OUTSIDE_ONLY,
    
    /**
     * Cancels both clicks inside and outside the {@link PlayerMenu}.
     */
    @DefaultEnumValue
    EITHER,
    
    /**
     * Cancels no clicks whatsoever.
     */
    NEITHER
}
