package me.hapyl.eterna.module.player.dialog;

import me.hapyl.eterna.module.reflect.npc.HumanNPC;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents a base {@link DialogEntry}.
 */
public interface DialogEntry {

    /**
     * Executed whenever it's the current {@link DialogEntry} in the {@link DialogInstance}.
     *
     * @param dialog - {@link DialogInstance}.
     */
    void run(@Nonnull DialogInstance dialog);

    /**
     * Gets the delay before sending the next {@link DialogEntry}.
     *
     * @return the delay before sending the next {@link DialogEntry}.
     */
    default int getDelay() {
        return 20;
    }

    /**
     * Gets the delay before sending the next {@link DialogEntry}.
     * <br>
     * This method allows per-player delays, defaults to {@link #getDelay()}
     *
     * @param player - Player to get the delay for.
     * @return the delay before sending the next {@link DialogEntry}.
     */
    default int getDelay(@Nonnull Player player) {
        return getDelay();
    }

    /**
     * Constructs a {@link DialogString} from the given {@link String}.
     *
     * @param strings - Strings.
     * @return an array of {@link DialogString}.
     * @see DialogString
     */
    @Nonnull
    static DialogEntry[] of(@Nonnull String... strings) {
        return makeArray(strings, DialogString::new);
    }

    /**
     * Constructs a {@link DialogNpcEntry} from the given {@link String}.
     *
     * @param npc     - {@link HumanNPC} who will be sending the messages.
     * @param strings - Strings.
     * @return an array of {@link DialogNpcEntry}.
     * @see DialogNpcEntry
     */
    @Nonnull
    static DialogEntry[] of(@Nonnull HumanNPC npc, @Nonnull String... strings) {
        return makeArray(strings, string -> new DialogNpcEntry(npc, string));
    }

    /**
     * Constructs a {@link DialogEntry}.
     *
     * @param action - Action to perform.
     */
    @Nonnull
    static DialogEntry of(@Nonnull Consumer<DialogInstance> action) {
        return action::accept;
    }

    private static <T> DialogEntry[] makeArray(T[] array, Function<T, DialogEntry> fn) {
        final DialogEntry[] entries = new DialogEntry[array.length];

        for (int i = 0; i < array.length; i++) {
            entries[i] = fn.apply(array[i]);
        }

        return entries;
    }

}
