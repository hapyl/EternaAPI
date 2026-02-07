package me.hapyl.eterna.module.inventory.menu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

/**
 * Indicates that the annotated {@link PlayerMenu} is disabled, and will not open, showing the {@link #disableReason()} instead.
 */
public interface DisabledPlayerMenu {
    
    /**
     * Gets the reason for why this {@link PlayerMenu} is disabled.
     *
     * @return the reason for why this menu is disabled.
     */
    @NotNull
    default Component disableReason() {
        return Component.text("This menu is currently disabled!", NamedTextColor.RED);
    }
    
}
