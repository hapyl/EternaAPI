package me.hapyl.eterna.module.component;

import me.hapyl.eterna.module.annotate.UtilityClass;
import me.hapyl.eterna.module.text.SmallCaps;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.map.MapFont;
import org.bukkit.map.MinecraftFont;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a utility class that allows to perform pixel-perfect centering a {@link Component}.
 */
@UtilityClass
public final class ComponentCenter {
    
    /**
     * Defines a {@link MinecraftFont} used for center calculations, which extends the vanilla font with additional support for small caps.
     */
    public static final MinecraftFont FONT = new Font();
    
    /**
     * Defines the default vanilla chat width.
     */
    public static final int DEFAULT_CHAT_WIDTH = 320;
    
    /**
     * Defines the space character width.
     */
    public static final int SPACE_WIDTH = 3;
    
    /**
     * Defines the guesstimate width used for when an unknown character is centered.
     */
    public static final int GUESSTIMATE_WIDTH = 6;
    
    private ComponentCenter() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Centers the given {@link Component} along the given {@code length}.
     *
     * @param component - The component to center.
     * @param length    - The desired length, in pixels.
     * @return a centered component.
     */
    public static @NotNull Component center(@NotNull Component component, int length) {
        final int width = calculateWidth(component, false);
        final int padding = length - width;
        
        if (padding <= 0) {
            return component;
        }
        
        final int spaces = padding / (SPACE_WIDTH + 1);
        final int left = spaces / 2;
        final int right = spaces - left;
        
        return Component.empty()
                        .append(Component.text(" ".repeat(left)))
                        .append(component)
                        .append(Component.text(" ".repeat(right)));
    }
    
    /**
     * Centers the given {@link Component} using the default chat width.
     *
     * @param component - The component to center.
     * @return a centered component.
     */
    public static @NotNull Component center(@NotNull Component component) {
        return center(component, DEFAULT_CHAT_WIDTH);
    }
    
    private static int calculateWidth(@NotNull Component component, boolean inheritedBold) {
        final boolean bold = switch (component.style().decoration(TextDecoration.BOLD)) {
            case TRUE -> true;
            case FALSE -> false;
            case NOT_SET -> inheritedBold;
        };
        
        int width = 0;
        
        if (component instanceof TextComponent text) {
            width += calculateTextWidth(text.content(), bold);
        }
        
        for (final Component child : component.children()) {
            width += calculateWidth(child, bold);
        }
        
        return width;
    }
    
    private static int calculateTextWidth(@NotNull String text, boolean bold) {
        int width = 0;
        
        for (final char c : text.toCharArray()) {
            width += calculateCharWidth(c, bold);
        }
        
        return width;
    }
    
    private static int calculateCharWidth(char ch, boolean bold) {
        final MapFont.CharacterSprite sprite = FONT.getChar(ch);
        int width;
        
        // If the character is in the font, use the width
        if (sprite != null) {
            width = sprite.getWidth();
        }
        // Otherwise guesstimate, because that's the best we can do
        else {
            width = GUESSTIMATE_WIDTH;
        }
        
        // Compensate for spacing
        width++;
        
        // Compensate for bold
        if (bold) {
            width++;
        }
        
        return width;
    }
    
    /**
     * Defines the font used for center calculations.
     */
    public static class Font extends MinecraftFont {
        
        private static final int FONT_HEIGHT = 5;
        
        Font() {
            super();
            
            // Instantiate small caps characters
            SmallCaps.CHARS.forEach((_, ch) -> {
                this.setChar(ch.ch(), new CharacterSprite(ch.length(), FONT_HEIGHT, new boolean[ch.length() * FONT_HEIGHT]));
            });
            
            // Force unmalleability
            super.malleable = false;
        }
        
    }
    
}