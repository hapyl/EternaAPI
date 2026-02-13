package me.hapyl.eterna.module.block.display;

import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;

public class DisplayObjectText extends DisplayObject<TextDisplay> {

    private final Component component;

    public DisplayObjectText(@NotNull JsonObject json, @NotNull Component component) {
        super(TextDisplay.class, json);

        this.component = component;
    }

    @Override
    protected void onCreate(@NotNull TextDisplay display) {
        display.text(component);
    }
}
