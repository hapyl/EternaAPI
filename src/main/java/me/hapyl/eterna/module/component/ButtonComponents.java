package me.hapyl.eterna.module.component;

import me.hapyl.eterna.EternaColors;
import me.hapyl.eterna.module.annotate.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
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
    static @NotNull Component click(@NotNull String clickTo) {
        return bullet(Component.text("Click to " + clickTo), Style.style(EternaColors.GOLD));
    }
    
    /**
     * Creates a {@code "Left-click to"} component.
     *
     * @param clickTo - The left-click action to append.
     * @return a new styled component.
     */
    static @NotNull Component left(@NotNull String clickTo) {
        return bullet(Component.text("Left-click to " + clickTo), Style.style(EternaColors.GOLD));
    }
    
    /**
     * Creates a {@code "Right-click to"} component.
     *
     * @param clickTo - The right-click action to append.
     * @return a new styled component.
     */
    static @NotNull Component right(@NotNull String clickTo) {
        return bullet(Component.text("Right-click to " + clickTo), Style.style(EternaColors.DARK_GOLD));
    }
    
    /**
     * Creates a {@code "Shift left-click to"} component.
     *
     * @param clickTo - The shift left-click action to append.
     * @return a new styled component.
     */
    static @NotNull Component shiftLeft(@NotNull String clickTo) {
        return bullet(Component.text("Shift left-click to " + clickTo), Style.style(EternaColors.AQUA));
    }
    
    /**
     * Creates a {@code "Shift right-click to"} component.
     *
     * @param clickTo - The shift right-click action to append.
     * @return a new styled component.
     */
    static @NotNull Component shiftRight(@NotNull String clickTo) {
        return bullet(Component.text("Shift right-click to " + clickTo), Style.style(EternaColors.DARK_AQUA));
    }
    
    /**
     * Creates a {@code "Middle-click to"} component.
     *
     * @param clickTo - The middle-click action to append.
     * @return a new styled component.
     */
    static @NotNull Component middle(@NotNull String clickTo) {
        return bullet(Component.text("Middle-click to " + clickTo), Style.style(EternaColors.PINK));
    }
    
    /**
     * Creates a {@code "SWAP_OFFHANDS to"} component, with the player's keybind for swapping offhand (defaults to F).
     *
     * @param clickTo - The swap off hands action to append.
     * @return a new styled component.
     */
    static @NotNull Component swapOffhand(@NotNull String clickTo) {
        return bullet(
                Component.empty()
                         .append(Keybind.SWAP_OFFHAND)
                         .append(Component.text(" to " + clickTo)),
                Style.style(EternaColors.PINK)
        );
    }
    
    /**
     * Creates the bullet component for the given {@link Component}.
     *
     * @param component - The component to prefix.
     * @param style     - The style to apply to both the component and prefix.
     * @return a new styled component.
     */
    static @NotNull Component bullet(@NotNull Component component, @NotNull Style style) {
        return Component.empty()
                        .append(Handler.BULLET.style(style))
                        .appendSpace()
                        .append(component.style(style));
    }
    
    class Handler {
        private static final Component BULLET = Component.text("\uD83D\uDFC4");
        
        private Handler() {
        }
    }
    
}
