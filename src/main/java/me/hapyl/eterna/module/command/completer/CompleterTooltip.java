package me.hapyl.eterna.module.command.completer;

import me.hapyl.eterna.module.util.TypeConverter;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a tab-completer tooltip.
 * <p>A tooltip is appended at the end of static arguments for the given {@link CompleterHandler} for a user-friends feedback of a given argument.</p>
 */
public interface CompleterTooltip {
    
    /**
     * Gets the tooltip for the current argument converter.
     *
     * @param converter - The current argument converter.
     * @return the tooltip to show.
     */
    @NotNull
    String apply(@NotNull TypeConverter converter);
    
}
