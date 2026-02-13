package me.hapyl.eterna.module.command.completer;

import com.google.common.collect.Lists;
import me.hapyl.eterna.module.annotate.SelfReturn;
import me.hapyl.eterna.module.util.Buildable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * Represents a tab-completer handler.
 */
public interface CompleterHandler {
    
    /**
     * Gets the argument index of this handler.
     *
     * @return the argument index of this handler.
     */
    int index();
    
    /**
     * Gets a {@link List} of completers for this argument.
     *
     * @return a list of completers for this argument.
     */
    @NotNull
    List<Completer> handle();
    
    /**
     * Gets a {@link CompleterTooltip} for this argument.
     *
     * @return a tooltip for this argument.
     */
    @Nullable
    CompleterTooltip tooltip();
    
    /**
     * Creates a new {@link Builder}.
     *
     * @param index - The target argument index.
     * @return a new builder.
     */
    @NotNull
    static Builder builder(int index) {
        return new Builder(index);
    }
    
    class Builder implements Buildable<CompleterHandler> {
        private final int index;
        private final List<Completer> completers;
        private CompleterTooltip tooltip;
        
        Builder(int index) {
            this.index = index;
            this.completers = Lists.newArrayList();
        }
        
        /**
         * Adds a literal argument.
         *
         * @param literal - The literal argument.
         */
        @SelfReturn
        public Builder literal(@NotNull String literal) {
            this.completers.add((sender, args, index) -> List.of(literal));
            return this;
        }
        
        /**
         * Adds the given values to arguments.
         *
         * @param values - The values to add.
         */
        @SelfReturn
        public Builder values(@NotNull Collection<String> values) {
            this.completers.add((sender, args, index) -> List.copyOf(values));
            return this;
        }
        
        /**
         * Sets the tooltip of this handler.
         *
         * @param tooltip - The tooltip to set.
         */
        @NotNull
        public Builder tooltip(@NotNull CompleterTooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }
        
        /**
         * Builds the {@link CompleterHandler}.
         *
         * @return the built handler.
         */
        @NotNull
        @Override
        public CompleterHandler build() {
            return new CompleterHandler() {
                @Override
                public int index() {
                    return index;
                }
                
                @NotNull
                @Override
                public List<Completer> handle() {
                    return List.copyOf(completers);
                }
                
                @Nullable
                @Override
                public CompleterTooltip tooltip() {
                    return tooltip;
                }
            };
        }
    }
    
}
