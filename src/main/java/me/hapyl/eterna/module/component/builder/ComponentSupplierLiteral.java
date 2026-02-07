package me.hapyl.eterna.module.component.builder;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Represents a literal component supplier.
 */
public final class ComponentSupplierLiteral implements ComponentSupplier {
    private final Component component;
    
    ComponentSupplierLiteral(@NotNull Component component) {
        this.component = component;
    }
    
    @NotNull
    @Override
    public Component supply(@NotNull Map<String, Component> components) {
        return component;
    }
}
