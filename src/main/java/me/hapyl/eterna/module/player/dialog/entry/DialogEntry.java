package me.hapyl.eterna.module.player.dialog.entry;

import me.hapyl.eterna.module.annotate.RequiresVarargs;
import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.player.dialog.DialogInstance;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.registry.Keyed;
import me.hapyl.eterna.module.util.Validate;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Function;

/**
 * Represents a {@link DialogEntry}.
 */
public interface DialogEntry extends Keyed {
    
    /**
     * Displays this {@link DialogEntry} to the given {@link DialogInstance}.
     *
     * @param dialogInstance - The dialog instant to display to.
     */
    void display(@NotNull DialogInstance dialogInstance);
    
    /**
     * Gets the {@link Key} of this {@link DialogEntry}, or an empty key if this entry is anonymous.
     *
     * <p>
     * Keyed entries can be used to jump to {@link DialogInstance#jumpToEntry(Key)}, allowing dynamically changing them.
     * </p>
     *
     * @return the key of this entry.
     */
    @Override
    @NotNull
    default Key getKey() {
        return Key.empty();
    }
    
    /**
     * Gets the delay, in ticks, before the next {@link DialogEntry} is displayed.
     *
     * @return the delay for this dialog entry.
     */
    default int getDelay() {
        return 20;
    }
    
    /**
     * A static factory method for creating {@link DialogEntryText}.
     *
     * @param component - The components to display.
     * @return a dialog entry array containing the text.
     * @see DialogEntryText
     */
    @NotNull
    static DialogEntry ofText(@NotNull Component component) {
        return new DialogEntryText(component);
    }
    
    /**
     * A static factory method for creating {@link DialogEntryText}.
     *
     * <p>
     * This is a convenience method that creates a new entry for <b>each</b> passed {@link Component}, allowing to nest
     * components for same-type entries.
     * </p>
     *
     * @param components - The components to display.
     * @return a dialog entry array containing the text.
     * @throws IllegalArgumentException if no components are provided
     * @see DialogEntryText
     */
    @NotNull
    static DialogEntry[] ofText(@NotNull @RequiresVarargs Component... components) {
        return makeEntryArray(components, DialogEntry::ofText);
    }
    
    /**
     * A static factory method for creating {@link DialogEntryNpc}.
     *
     * @param npc       - The npc who is talking.
     * @param component - The component to display.
     * @return a dialog entry array containing npc dialog.
     * @see DialogEntryNpc
     */
    @NotNull
    static DialogEntry ofNpc(@NotNull Npc npc, @NotNull Component component) {
        return new DialogEntryNpc(npc, component);
    }
    
    /**
     * A static factory method for creating {@link DialogEntryNpc}.
     *
     * <p>
     * This is a convenience method that creates a new entry for <b>each</b> passed {@link Component}, allowing to nest
     * components for same-type entries.
     * </p>
     *
     * @param npc        - The npc who is talking.
     * @param components - The components to display.
     * @return a dialog entry array containing npc dialog.
     * @throws IllegalArgumentException if no components are provided
     * @see DialogEntryNpc
     */
    @NotNull
    static DialogEntry[] ofNpc(@NotNull Npc npc, @NotNull @RequiresVarargs Component... components) {
        return makeEntryArray(components, _component -> ofNpc(npc, _component));
    }
    
    /**
     * A static factory method for creating {@link DialogEntryOptions}.
     *
     * @param entry - The entry to display.
     * @return a new options entry.
     * @see DialogEntryOptions
     */
    @NotNull
    static DialogEntryOptions ofSelectableOptions(@NotNull DialogEntry entry) {
        return new DialogEntryOptions(entry);
    }
    
    /**
     * A static factory method for creating {@link DialogEntry}.
     *
     * <p>
     * This is a convenience method that allows treating an existing {@link DialogEntry}
     * uniformly with other factory methods, improving readability and symmetry.
     * </p>
     *
     * @param entry - The entry to accept.
     * @return the entry action.
     */
    @NotNull
    static DialogEntry ofEntry(@NotNull DialogEntry entry) {
        return entry;
    }
    
    @ApiStatus.Internal
    @NotNull
    private static DialogEntry[] makeEntryArray(@NotNull Component[] components, @NotNull Function<Component, ? extends DialogEntry> mapper) {
        return Arrays.stream(Validate.varargs(components))
                     .map(mapper)
                     .toArray(DialogEntry[]::new);
    }
    
}
