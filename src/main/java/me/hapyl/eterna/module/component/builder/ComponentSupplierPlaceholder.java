package me.hapyl.eterna.module.component.builder;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Represents a placeholder component supplier.
 */
public final class ComponentSupplierPlaceholder implements ComponentSupplier {
    private final String key;
    
    ComponentSupplierPlaceholder(@NotNull String key) {
        this.key = key;
    }
    
    /**
     * Gets the placeholder key.
     *
     * @return the placeholder key.
     */
    @NotNull
    public String getKey() {
        return key;
    }
    
    @NotNull
    @Override
    public Component supply(@NotNull Map<String, Component> components) {
        return components.getOrDefault(key, Component.text("{%s}".formatted(key)));
    }
}
