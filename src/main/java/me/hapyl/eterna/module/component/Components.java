package me.hapyl.eterna.module.component;

import net.kyori.adventure.text.Component;

import javax.annotation.Nonnull;

public final class Components {
    
    private Components() {
    }
    
    @Nonnull
    public static Component ofLegacy(@Nonnull String legacy) {
        return new LegacyStringComponent(legacy).asComponent();
    }
    
}
