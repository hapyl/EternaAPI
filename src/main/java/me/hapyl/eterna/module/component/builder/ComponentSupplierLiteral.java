package me.hapyl.eterna.module.component.builder;

import net.kyori.adventure.text.Component;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Represents a literal component supplier.
 */
public final class ComponentSupplierLiteral implements ComponentSupplier {
    private final Component component;
    
    ComponentSupplierLiteral(@Nonnull Component component) {
        this.component = component;
    }
    
    @Nonnull
    @Override
    public Component supply(@Nonnull Map<String, Component> components) {
        return component;
    }
}
