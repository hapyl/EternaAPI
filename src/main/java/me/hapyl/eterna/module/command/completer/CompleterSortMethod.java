package me.hapyl.eterna.module.command.completer;

import me.hapyl.eterna.module.annotate.DefaultEnumValue;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

/**
 * Represents a tab-completer sort method.
 */
public enum CompleterSortMethod implements BiFunction<String, String, Boolean> {
    
    /**
     * The method is to check whether the completion starts with the argument.
     */
    @DefaultEnumValue
    STARTS_WITH {
        @Override
        public Boolean apply(@NotNull String completion, @NotNull String argument) {
            return completion.startsWith(argument);
        }
    },
    
    /**
     * The method is to check whether the completion contains a value.
     */
    CONTAINS {
        @Override
        public Boolean apply(@NotNull String completion, @NotNull String argument) {
            return completion.contains(argument);
        }
    }
    
}
