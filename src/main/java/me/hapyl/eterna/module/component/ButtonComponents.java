package me.hapyl.eterna.module.component;

import me.hapyl.eterna.EternaColors;
import me.hapyl.eterna.module.annotate.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a utility class that creates buttons {@link Component} (eg: "Left-click to", "Right-click to")
 * with the default Eterna format.
 */
@ApiStatus.NonExtendable
@UtilityClass
public interface ButtonComponents {
    
    /**
     * Creates a {@code "Click to"} component.
     *
     * @param clickTo - The click action to append.
     * @return a new styled component.
     */
    @NotNull
    static Component click(@NotNull String clickTo) {
        return bullet(Component.text("Click to " + clickTo), EternaColors.GOLD);
    }
    
    /**
     * Creates a {@code "Left-click to"} component.
     *
     * @param clickTo - The left-click action to append.
     * @return a new styled component.
     */
    @NotNull
    static Component left(@NotNull String clickTo) {
        return bullet(Component.text("Left-click to " + clickTo), EternaColors.GOLD);
    }
    
    /**
     * Creates a {@code "Right-click to"} component.
     *
     * @param clickTo - The right-click action to append.
     * @return a new styled component.
     */
    @NotNull
    static Component right(@NotNull String clickTo) {
        return bullet(Component.text("Right-click to " + clickTo), EternaColors.DARK_GOLD);
    }
    
    /**
     * Creates a {@code "Shift left-click to"} component.
     *
     * @param clickTo - The shift left-click action to append.
     * @return a new styled component.
     */
    @NotNull
    static Component shiftLeft(@NotNull String clickTo) {
        return bullet(Component.text("Shift left-click to " + clickTo), EternaColors.AQUA);
    }
    
    /**
     * Creates a {@code "Shift right-click to"} component.
     *
     * @param clickTo - The shift right-click action to append.
     * @return a new styled component.
     */
    @NotNull
    static Component shiftRight(@NotNull String clickTo) {
        return bullet(Component.text("Shift right-click to " + clickTo), EternaColors.DARK_AQUA);
    }
    
    /**
     * Creates a {@code "Middle-click to"} component.
     *
     * @param clickTo - The middle-click action to append.
     * @return a new styled component.
     */
    @NotNull
    static Component middle(@NotNull String clickTo) {
        return bullet(Component.text("Middle-click to " + clickTo), EternaColors.PINK);
    }
    
    @NotNull
    private static Component bullet(@NotNull Component component, @NotNull TextColor textColor) {
        return Component.text("\uD83D\uDFC4 ").append(component).color(textColor);
    }
    
}
