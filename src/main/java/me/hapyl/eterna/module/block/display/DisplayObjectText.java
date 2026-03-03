package me.hapyl.eterna.module.block.display;

import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link DisplayObject} of an {@link TextDisplay}.
 */
public final class DisplayObjectText extends DisplayObject<TextDisplay> {
    
    private final Component text;
    private final TextDisplay.TextAlignment textAlignment;
    
    private final int lineWidth;
    private final int background;
    private final boolean defaultBackground;
    private final boolean seeThrough;
    private final boolean shadow;
    private final byte textOpacity;
    
    DisplayObjectText(
            @NotNull JsonObject json,
            @NotNull Component text,
            @NotNull TextDisplay.TextAlignment textAlignment,
            int lineWidth,
            int background,
            boolean defaultBackground,
            boolean seeThrough,
            boolean shadow,
            byte textOpacity
    ) {
        super(TextDisplay.class, json);
        
        this.text = text;
        this.textAlignment = textAlignment;
        this.lineWidth = lineWidth;
        this.background = background;
        this.defaultBackground = defaultBackground;
        this.seeThrough = seeThrough;
        this.shadow = shadow;
        this.textOpacity = textOpacity;
    }
    
    @Override
    public void onCreate(@NotNull TextDisplay display) {
        display.text(this.text);
        display.setAlignment(this.textAlignment);
        display.setLineWidth(this.lineWidth);
        display.setBackgroundColor(Color.fromARGB(this.background));
        display.setDefaultBackground(this.defaultBackground);
        display.setSeeThrough(this.seeThrough);
        display.setShadowed(this.shadow);
        display.setTextOpacity(this.textOpacity);
    }
}
