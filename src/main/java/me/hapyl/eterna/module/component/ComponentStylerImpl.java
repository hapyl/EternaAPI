package me.hapyl.eterna.module.component;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public final class ComponentStylerImpl implements ComponentStyler {
    
    private final Style style;
    private final Component prefix;
    
    ComponentStylerImpl(@NotNull Style style, @NotNull Component prefix) {
        this.style = style;
        this.prefix = prefix;
    }
    
    @Override
    public @NotNull Style style() {
        return style;
    }
    
    @Override
    public @NotNull Component prefix() {
        return prefix;
    }
    
}