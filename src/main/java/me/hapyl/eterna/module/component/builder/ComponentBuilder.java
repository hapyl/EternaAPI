package me.hapyl.eterna.module.component.builder;

import com.google.common.collect.Lists;
import me.hapyl.eterna.module.annotate.SelfReturn;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Represents a {@link Component} builder with a support of placeholders.
 */
public class ComponentBuilder {
    private final List<ComponentSupplier> suppliers;
    
    /**
     * Creates a new {@link ComponentBuilder}.
     */
    public ComponentBuilder() {
        this.suppliers = Lists.newLinkedList();
    }
    
    /**
     * Appends the given {@link ComponentSupplier}.
     *
     * @param supplier - The supplier to append.
     * @see ComponentSupplier#ofLiteral(Component)
     * @see ComponentSupplier#ofPlaceholder(String)
     */
    @SelfReturn
    public ComponentBuilder append(@Nonnull ComponentSupplier supplier) {
        this.suppliers.add(supplier);
        return this;
    }
    
    /**
     * Builds the {@link Component} with the given {@link ComponentResolver}.
     *
     * @param resolver - The resolver to apply.
     * @return the built {@link Component}.
     * @see ComponentResolver#literal()
     * @see ComponentResolver#builder()
     */
    @Nonnull
    public Component build(@Nonnull ComponentResolver resolver) {
        final TextComponent.Builder builder = Component.text();
        
        for (ComponentSupplier supplier : suppliers) {
            builder.append(resolver.resolve(supplier));
        }
        
        return builder.build();
    }
    
}
