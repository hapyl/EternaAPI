package me.hapyl.eterna.module.component.builder;

import net.kyori.adventure.text.Component;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Supplies a {@link Component} to be resolved by a {@link ComponentResolver}.
 * <p>
 * A supplier may represent a literal component or a placeholder that is substituted
 * during resolution.
 * </p>
 */
public interface ComponentSupplier {
    
    /**
     * Supplies a {@link Component} using the given resolved components.
     *
     * @param components - The resolved placeholder components.
     */
    @Nonnull
    Component supply(@Nonnull Map<String, Component> components);
    
    /**
     * Creates a supplier that always returns the given literal {@link Component}.
     *
     * @param component - The literal component to supply.
     */
    @Nonnull
    static ComponentSupplier ofLiteral(@Nonnull Component component) {
        return new ComponentSupplierLiteral(component);
    }
    
    /**
     * Creates a supplier representing a placeholder {@link Component}.
     *
     * @param placeholder - The placeholder key.
     */
    @Nonnull
    static ComponentSupplier ofPlaceholder(@Nonnull String placeholder) {
        return new ComponentSupplierPlaceholder(placeholder);
    }
    
}