package me.hapyl.eterna.module.component.builder;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

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
    @NotNull
    Component supply(@NotNull Map<String, Component> components);
    
    /**
     * Creates a supplier that always returns the given literal {@link Component}.
     *
     * @param component - The literal component to supply.
     */
    @NotNull
    static ComponentSupplier ofLiteral(@NotNull Component component) {
        return new ComponentSupplierLiteral(component);
    }
    
    /**
     * Creates a supplier representing a placeholder {@link Component}.
     *
     * @param placeholder - The placeholder key.
     */
    @NotNull
    static ComponentSupplier ofPlaceholder(@NotNull String placeholder) {
        return new ComponentSupplierPlaceholder(placeholder);
    }
    
}