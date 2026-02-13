package me.hapyl.eterna.module.component.builder;

import com.google.common.collect.Maps;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Resolves {@link Component}s produced by a {@link ComponentSupplier}.
 * <p>
 * A resolver defines how components are rendered, most notably how placeholders are handled.
 * </p>
 *
 * <ul>
 *   <li>{@link #literal()} – Renders components as-is; placeholders are left unresolved and use {@code {key}} syntax.</li>
 *   <li>{@link #builder()} – Requires every placeholder to be explicitly resolved via the returned builder.</li>
 * </ul>
 */
public interface ComponentResolver {
    
    /**
     * Resolves the component supplied by the given {@link ComponentSupplier}.
     *
     * @param supplier - The component supplier to resolve.
     * @return The resolved {@link Component}.
     */
    @NotNull
    Component resolve(@NotNull ComponentSupplier supplier);
    
    /**
     * Returns a {@link ComponentResolver} that renders components literally.
     * <p>Placeholders are not substituted and are rendered using {@code {key}} syntax.</p>
     *
     * @return a literal resolver.
     */
    @NotNull
    static ComponentResolver literal() {
        return supplier -> supplier.supply(Map.of());
    }
    
    /**
     * Gets a resolver {@link Builder} where each placeholder must be explicitly resolved.
     *
     * @return a resolver builder.
     */
    @NotNull
    static Builder builder() {
        return new Builder();
    }
    
    /**
     * A {@link ComponentResolver} that resolves placeholders using a predefined mapping.
     * <p>
     * If a placeholder is encountered without a resolved component, an {@link IllegalStateException} is thrown.
     * </p>
     */
    final class Builder implements ComponentResolver {
        private final Map<String, Component> resolved;
        
        Builder() {
            this.resolved = Maps.newHashMap();
        }
        
        /**
         * Resolves a placeholder with the given component.
         *
         * @param placeholder - The placeholder key.
         * @param component   - The {@link Component} to substitute.
         */
        @NotNull
        public Builder resolve(@NotNull String placeholder, @NotNull Component component) {
            this.resolved.put(placeholder, component);
            return this;
        }
        
        /**
         * Resolves the supplied component, substituting placeholders using the builder mappings.
         *
         * @param supplier - The component supplier to resolve.
         * @throws IllegalStateException if a placeholder is encountered without a resolved component
         */
        @NotNull
        @Override
        public Component resolve(@NotNull ComponentSupplier supplier) throws IllegalStateException {
            if (supplier instanceof ComponentSupplierPlaceholder placeholderSupplier) {
                final String placeholder = placeholderSupplier.getKey();
                final Component component = resolved.get(placeholder);
                
                if (component == null) {
                    throw new IllegalStateException("Unresolved placeholder: %s".formatted(placeholder));
                }
                
                return component;
            }
            
            return supplier.supply(Map.of());
        }
    }
}
