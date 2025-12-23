package me.hapyl.eterna.module.component.builder;

import net.kyori.adventure.text.Component;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Represents a placeholder component supplier.
 */
public final class ComponentSupplierPlaceholder implements ComponentSupplier {
    private final String key;
    
    ComponentSupplierPlaceholder(@Nonnull String key) {
        this.key = key;
    }
    
    /**
     * Gets the placeholder key.
     *
     * @return the placeholder key.
     */
    @Nonnull
    public String getKey() {
        return key;
    }
    
    @Nonnull
    @Override
    public Component supply(@Nonnull Map<String, Component> components) {
        return components.getOrDefault(key, Component.text("{%s}".formatted(key)));
    }
}
