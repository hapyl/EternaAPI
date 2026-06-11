package me.hapyl.eterna.module.component;

import me.hapyl.eterna.module.annotate.SelfReturn;
import me.hapyl.eterna.module.inventory.builder.ItemBuilder;
import me.hapyl.eterna.module.util.Buildable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NonNull;

/**
 * A functional interface that styles a {@link Component} with support of a prefix.
 */
@FunctionalInterface
public interface ComponentStyler {
    
    /**
     * Gets the {@link Style} to apply to components.
     *
     * @return the style to apply to component.
     */
    @NotNull
    Style style();
    
    /**
     * Gets the prefix to prepend to the components.
     *
     * @return the prefix to prepend to the components.
     */
    default @NotNull Component prefix() {
        return Component.empty();
    }
    
    /**
     * A static factory method for creating a {@link ComponentStyler}.
     *
     * @param style  - The style.
     * @param prefix - The prefix.
     * @return a new component styler.
     */
    static @NotNull ComponentStyler create(@NotNull Style style, @NotNull Component prefix) {
        return new ComponentStylerImpl(style, prefix);
    }
    
    /**
     * A static factory method for creating a {@link ComponentStyler}.
     *
     * @param style - The style.
     * @return a new component styler.
     */
    static @NotNull ComponentStyler create(@NotNull Style style) {
        return create(style, Component.empty());
    }
    
    /**
     * Creates a {@link Builder} for the given {@link Style}.
     *
     * @param style - The style.
     * @return a new builder.
     */
    static @NotNull Builder builder(@NotNull Style style) {
        return new Builder(style);
    }
    
    /**
     * Represents a builder for {@link ComponentStyler}.
     */
    class Builder implements Buildable<ComponentStyler> {
        
        private final @NotNull Style style;
        private @NotNull Component prefix;
        
        private Builder(@NotNull Style style) {
            this.style = style;
            this.prefix = Component.empty();
        }
        
        /**
         * Sets the prefix component of this builder.
         *
         * @param prefix - The prefix to set.
         */
        @SelfReturn
        public Builder withPrefix(@NotNull Component prefix) {
            this.prefix = prefix;
            return this;
        }
        
        /**
         * Sets the prefix component to have whitespace padding of the given length.
         *
         * @param padding - The padding length.
         */
        @SelfReturn
        public Builder withPadding(@Range(from = 1, to = Integer.MAX_VALUE) int padding) {
            this.prefix = Component.text(" ".repeat(padding));
            return this;
        }
        
        /**
         * Builds a new {@link ComponentStyler}.
         *
         * @return a new component styler.
         */
        @Override
        public @NonNull ComponentStyler build() {
            return new ComponentStylerImpl(style, prefix);
        }
    }
    
}