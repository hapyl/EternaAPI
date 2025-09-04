package me.hapyl.eterna.module.component;

import me.hapyl.eterna.module.annotate.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A helper class for {@link Component}.
 */
@UtilityClass
public final class Components {
    
    private Components() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Converts the legacy text into a {@link Component}.
     * <p>Any legacy colors using the color codes are kept.</p>
     *
     * @param legacy - The legacy text.
     * @return a {@link Component} based on legacy text.
     */
    @Nonnull
    public static Component ofLegacy(@Nonnull Object legacy) {
        return new LegacyStringComponent(String.valueOf(legacy)).asComponent();
    }
    
    /**
     * Converse the legacy text into a {@link Component}, or {@code null} if legacy is {@code null}.
     *
     * @param legacy - The legacy text.
     * @return a {@link Component} based on legacy text.
     */
    @Nullable
    public static Component ofLegacyOrNull(@Nullable Object legacy) {
        return legacy != null ? ofLegacy(legacy) : null;
    }
    
    /**
     * Gets whether the given {@link Component} is empty.
     *
     * @param component - The component to check.
     * @return {@code true} if the {@link Component} is empty, {@code false} otherwise.
     */
    public static boolean isEmpty(@Nonnull Component component) {
        return component instanceof TextComponent textComponent && textComponent.content().isEmpty();
    }
    
    /**
     * Gets whether the given {@link Component} is a newline character ({@code \n}).
     *
     * @param component - The component to check.
     * @return {@code true} if the {@link Component} is a newline character ({@code \n}), {@code false} otherwise.
     */
    public static boolean isNewLine(@Nonnull Component component) {
        return component instanceof TextComponent textComponent && textComponent.content().equals("\n");
    }
    
    /**
     * Gets whether the given {@link Component} is empty or a newline character ({@code \n}).
     *
     * @param component - The component to check.
     * @return {@code true} if the {@link Component} is empty or a newline character ({@code \n}), {@code false} otherwise.
     */
    public static boolean isEmptyOrNewLine(@Nonnull Component component) {
        return isEmpty(component) || isNewLine(component);
    }
    
    /**
     * Gets the {@link String} representation of the given {@link Component}.
     * <p>For {@link TextComponent}, it's their {@link TextComponent#content()}, a default {@link Object#toString()} is called otherwise.</p>
     *
     * @param component - The component to get the string representation for.
     * @return the {@link String} representation of the given {@link Component}.
     */
    @Nonnull
    public static String toString(@Nonnull Component component) {
        if (component instanceof TextComponent textComponent) {
            return textComponent.content();
        }
        
        return component.toString();
    }
    
    /**
     * Creates a single {@link Component} containing the given {@link Component}.
     *
     * @param components - The components.
     * @return a single {@link Component}.
     */
    @Nonnull
    public static Component join(@Nonnull Component... components) {
        if (components.length == 0) {
            return Component.empty();
        }
        
        Component root = components[0];
        
        for (int i = 1; i < components.length; i++) {
            root = root.append(components[i]);
        }
        
        return root;
    }
    
}
