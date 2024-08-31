package me.hapyl.eterna.module.player.dialog;

import com.google.common.collect.Maps;
import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.util.Compute;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Set;

/**
 * Represents a {@link DialogInstance} for the given {@link Player}.
 */
public class DialogInstance extends BukkitRunnable {

    public static final int OPTION_RESTING_SLOT = 8;

    protected final ArrayDeque<DialogEntry> entries;

    private final Player player;
    private final Dialog dialog;
    private final Map<DialogOptionEntry, Set<Integer>> selectedOptions;

    protected boolean awaitInput;
    protected DialogEntry currentEntry;

    private int tick;

    /**
     * Constructs a new {@link DialogInstance} for the given {@link Player} with the given {@link Dialog}.
     *
     * @param player - Player who to construct the instance for.
     * @param dialog - Dialog of which the instance will be based on.
     */
    public DialogInstance(@Nonnull Player player, @Nonnull Dialog dialog) {
        this.player = player;
        this.dialog = dialog;
        this.entries = dialog.entriesCopy();
        this.selectedOptions = Maps.newIdentityHashMap();
    }

    /**
     * Gets the {@link Player} of this {@link DialogInstance}.
     *
     * @return the {@link Player} of this {@link DialogInstance}.
     */
    @Nonnull
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Gets the {@link Dialog} on which this {@link DialogInstance} is based on.
     *
     * @return the {@link Dialog} on which this {@link DialogInstance} is based on.
     */
    @Nonnull
    public Dialog getDialog() {
        return dialog;
    }

    /**
     * Hijacks the given {@link DialogEntry} into this {@link DialogInstance}.
     * <br>
     * Hijacking the {@link DialogEntry} meaning putting it as the first element in the {@link DialogInstance} entries,
     * making it the next {@link DialogEntry}.
     *
     * @param entry - Entry to hijack.
     */
    public void hijackEntry(@Nonnull DialogEntry entry) {
        this.entries.addFirst(entry);
    }

    /**
     * Hijacks the given {@link DialogEntry}s into this {@link DialogInstance}.
     * <br>
     * Hijacking the {@link DialogEntry} meaning putting it as the first element in the {@link DialogInstance} entries,
     * making it the next {@link DialogEntry}.
     * <br>
     * The hijacking is done in reverse order, making {@link DialogEntry} maintain their order.
     *
     * @param entries - Entries to hijack.
     */
    public void hijackEntries(@Nonnull DialogEntry[] entries) {
        for (int i = entries.length - 1; i >= 0; i--) {
            hijackEntry(entries[i]);
        }
    }

    @Override
    public final void run() {
        // Player left
        if (!player.isOnline()) {
            cancel();
            return;
        }

        // For for player dialog
        if (awaitInput) {
            return;
        }

        if (tick-- <= 0) {
            nextEntry();
        }
    }

    /**
     * Forces the next {@link DialogEntry}.
     * <br>
     * If the next {@link DialogEntry} is {@code null}, the {@link Dialog} is considered as finished.
     */
    public void nextEntry() {
        currentEntry = entries.pollFirst();

        // Dialog finished
        if (currentEntry == null) {
            dialog.onDialogComplete(player);
            selectedOptions.clear();
            cancel();
            return;
        }

        currentEntry.run(this);

        int delay = currentEntry.getDelay(getPlayer());

        // Add a little more delay if the next is an option because it uses
        // all ten chat lines and short strings are very easy to miss
        if (entries.peekFirst() instanceof DialogOptionEntry) {
            delay += 6;
        }

        tick = delay;
    }

    /**
     * Forcefully cancels this {@link DialogInstance}.
     *
     * @throws IllegalStateException If the dialog was not started.
     */
    @Override
    public final synchronized void cancel() throws IllegalStateException {
        super.cancel();
        Eterna.getManagers().dialog.unregister(player);

        dialog.onDialogFinish(player);
    }

    /**
     * Stars this {@link DialogInstance}.
     */
    public final DialogInstance doStartInstance() {
        runTaskTimer(EternaPlugin.getPlugin(), 1, 1);

        dialog.onDialogStart(player);
        return this;
    }

    /**
     * Attempts to select an option if the current {@link DialogEntry} is a {@link DialogOptionEntry}, does nothing otherwise.
     *
     * @param option - Option to select.
     */
    public void trySelectOption(int option) {
        if (!(currentEntry instanceof DialogOptionEntry optionEntry)) {
            return;
        }

        final DialogOptionEntry.DialogOption dialogOption = optionEntry.getOption(option);
        final Player player = getPlayer();

        // Do snap cursor even if the option is invalid
        player.getInventory().setHeldItemSlot(OPTION_RESTING_SLOT);

        if (dialogOption != null) {
            dialogOption.select(optionEntry, this);
        }
    }

    /**
     * Returns {@code true} if the {@link Player} has selected all the options for the
     * given {@link DialogOptionEntry}, {@code false} otherwise.
     *
     * @param entry  - Entry to check for.
     * @param option - Option to check.
     * @return {@code true} if the {@link Player} has selected all the options for
     * the given {@link DialogOptionEntry}, {@code false} otherwise.
     */
    public boolean hasSelectedOption(@Nonnull DialogOptionEntry entry, int option) {
        final Set<Integer> selectedOptions = this.selectedOptions.get(entry);

        return selectedOptions != null && selectedOptions.contains(option);
    }

    /**
     * Marks the given option as selected for the given {@link DialogOptionEntry}.
     *
     * @param entry  - Entry to mark for.
     * @param option - Option to mark.
     */
    public void setHasSelectedOption(@Nonnull DialogOptionEntry entry, int option) {
        this.selectedOptions.compute(entry, Compute.setAdd(option));
    }

    /**
     * Returns true if the {@link Player} has selected all the options in the given {@link DialogOptionEntry},
     * minus the options that would forward the {@link Dialog}.
     *
     * @param entry - Entry to check for.
     * @return true if the {@link Player} has selected all the options in the given {@link DialogOptionEntry}, minus the options that would forward the {@link Dialog}.
     */
    public boolean hasSelectedAllOptions(@Nonnull DialogOptionEntry entry) {
        final int optionCount = entry.optionSizeMinusAdvancingDialog();
        final Set<Integer> selectedOptions = this.selectedOptions.get(entry);

        return selectedOptions != null && selectedOptions.size() == optionCount;
    }

}
