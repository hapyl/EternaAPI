package me.hapyl.eterna.builtin.updater;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

public enum UpdateResult implements ComponentLike {
    
    ERROR(Component.text("An errors has occurred whilst trying to check for updates.", NamedTextColor.RED)),
    UP_TO_DATE(Component.text("You're using the latest version!", NamedTextColor.GREEN)),
    OUTDATED(Component.text("You're using an outdated version, please update!", NamedTextColor.GOLD)),
    DEVELOPMENT(Component.text("You're using a development version.", NamedTextColor.AQUA));
    
    private final Component component;
    
    UpdateResult(@NotNull Component component) {
        this.component = component;
    }
    
    @Override
    @NotNull
    public Component asComponent() {
        return component;
    }
}
