package me.hapyl.eterna.module.component;

import me.hapyl.eterna.module.chat.Chat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;

import javax.annotation.Nonnull;

public class LegacyStringComponent implements ComponentLike {
    
    private final Component component;
    
    LegacyStringComponent(@Nonnull String component) {
        this.component = Component.text(Chat.format(component));
    }
    
    @Override
    @Nonnull
    public Component asComponent() {
        return component;
    }
}
