package me.hapyl.eterna.module.player.dialog;

import com.google.common.collect.Maps;
import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.module.player.dialog.entry.DialogEntry;
import me.hapyl.eterna.module.player.dialog.entry.DialogEntryOptions;
import me.hapyl.eterna.module.player.dialog.entry.OptionIndex;
import me.hapyl.eterna.module.player.quest.QuestHandler;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.util.Compute;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Represents a {@link DialogInstance} that handles the {@link Dialog} execution.
 */
public final class DialogInstance extends BukkitRunnable {
    
    /**
     * Defines the option resting slot where to snap player cursor for when a {@link DialogEntryOptions} is shown.
     */
    public static final int OPTION_RESTING_SLOT = 8;
    
    private final Player player;
    private final Dialog dialog;
    private final Map<DialogEntryOptions, Set<OptionIndex>> selectedOptions;
    
    private Deque<DialogEntry> entries;
    private DialogEntry currentEntry;
    
    private boolean awaitInput;
    
    private int tick;
    
    /**
     * Creates a new {@link DialogInstance}.
     *
     * @param player - The player for whom to create the instance.
     * @param dialog - The dialog to handle.
     */
    public DialogInstance(@NotNull Player player, @NotNull Dialog dialog) {
        this.player = player;
        this.dialog = dialog;
        this.entries = dialog.entriesCopy();
        this.selectedOptions = Maps.newIdentityHashMap();
    }
    
    /**
     * Gets the {@link Player} for whom this {@link DialogInstance} belongs to.
     *
     * @return the player for whom this dialog instance belongs to.
     */
    @NotNull
    public Player getPlayer() {
        return this.player;
    }
    
    /**
     * Gets the {@link Dialog} this {@link DialogInstance} handles.
     *
     * @return the dialog this dialog instance handles.
     */
    @NotNull
    public Dialog getDialog() {
        return dialog;
    }
    
    /**
     * Performs a dialog skip based on the {@link DialogSkip} of the handling {@link Dialog}
     *
     * @see Dialog#getDialogSkip(Player)
     */
    public void skip() {
        final DialogSkip handler = dialog.getDialogSkip(player);
        final CompletableFuture<Boolean> response = handler.prompt(player);
        
        // Save the previous input in case we're already awaiting input
        final boolean previousAwait = awaitInput;
        
        // We have to forcefully await the input, so it pauses the dialog
        awaitInput = true;
        
        response.whenComplete((success, _t) -> {
            if (success) {
                handler.onConfirm(player);
                cancel(DialogEndType.SKIPPED);
            }
            else {
                handler.onCancel(player);
                
                tick = 15;
                awaitInput = previousAwait;
            }
        });
    }
    
    /**
     * Forcefully skips this {@link Dialog} without any prompts.
     */
    public void skipNoPrompt() {
        cancel(DialogEndType.SKIPPED);
    }
    
    /**
     * Hijacks the given {@link DialogEntry} to the start of the entry list, which makes it the next entry.
     *
     * @param entry - The entry to hijack.
     */
    public void hijackEntry(@NotNull DialogEntry entry) {
        this.entries.addFirst(entry);
    }
    
    /**
     * Hijacks the given {@link DialogEntry} to the start of the entry list in the exact order passed, which makes them the next entries.
     *
     * @param entries - The entries to hijack.
     */
    public void hijackEntries(@NotNull DialogEntry[] entries) {
        for (int i = entries.length - 1; i >= 0; i--) {
            this.entries.addFirst(entries[i]);
        }
    }
    
    /**
     * Runs this {@link DialogInstance}.
     */
    @Override
    public void run() {
        // Check whether the player is still online, if not, stop the dialog
        if (!player.isOnline()) {
            this.cancel();
            return;
        }
        
        // Call `onDialogTick()`
        dialog.onDialogTick(player);
        
        // Check whether we should await for an input
        if (awaitInput) {
            return;
        }
        
        // Tick the dialog, if reached 0, go to the next entry
        if (tick-- <= 0) {
            nextEntry();
        }
    }
    
    /**
     * Forcefully advances the {@link Dialog} to the next {@link DialogEntry}.
     *
     * <p>
     * If there are no more entries, the dialog is considered as completed.
     * </p>
     *
     * @see #skip()
     * @see #skipNoPrompt()
     */
    public void nextEntry() {
        this.currentEntry = entries.pollFirst();
        
        // The dialog is finished
        if (this.currentEntry == null) {
            this.cancel(DialogEndType.COMPLETED);
            return;
        }
        
        this.currentEntry.display(this);
        
        int delay = Math.max(1, dialog.getEntryDelay(player, currentEntry));
        
        // If the next entry is `DialogEntryOptions`, add a little delay because the default
        // implementation uses 10 chat lines, which basically clears the chat unless it's open,
        // so the player will likely miss whatever current entry says
        if (this.entries.peekFirst() instanceof DialogEntryOptions) {
            delay += 6;
        }
        
        this.tick = delay;
    }
    
    /**
     * Finishes this {@link DialogInstance} with {@link DialogEndType#COMPLETED}.
     *
     * @throws IllegalStateException if not scheduled yet.
     * @deprecated Prefer providing the end type via {@link #cancel(DialogEndType)}.
     */
    @Deprecated(forRemoval = false /* explicit false */)
    @Override
    public synchronized void cancel() throws IllegalStateException {
        cancel(DialogEndType.COMPLETED);
    }
    
    /**
     * Finishes this {@link DialogInstance} with the given {@link DialogEndType}.
     *
     * @throws IllegalStateException if not scheduled yet.
     */
    public synchronized void cancel(@NotNull DialogEndType dialogEndType) throws IllegalStateException {
        super.cancel();
        
        dialog.onDialogEnd(player, dialogEndType);
        selectedOptions.clear();
        
        // Notify QuestHandler that the dialog finished
        QuestHandler.dialogFinished(this, dialogEndType);
        
        // Unregister the instance from here
        DialogHandler.handler.unregister(player);
    }
    
    /**
     * Gets whether the given {@link OptionIndex} was selected for the given {@link DialogEntryOptions}.
     *
     * @param entry - The entry to check.
     * @param index - The index to check.
     * @return {@code true} if the option was selected for the entry; {@code false} otherwise.
     */
    public boolean hasSelectedOption(@NotNull DialogEntryOptions entry, @NotNull OptionIndex index) {
        final Set<OptionIndex> selectedOptions = this.selectedOptions.get(entry);
        
        return selectedOptions != null && selectedOptions.contains(index);
    }
    
    /**
     * Sets that the given {@link OptionIndex} was selected for the given {@link DialogEntryOptions}.
     *
     * @param entry  - The entry to set.
     * @param option - The option to set.
     */
    public void setHasSelectedOption(@NotNull DialogEntryOptions entry, @NotNull OptionIndex option) {
        this.selectedOptions.compute(entry, Compute.setAdd(option));
    }
    
    /**
     * Gets whether all available options (excluding those that advance the dialog) has been selected for the given {@link DialogEntryOptions}.
     *
     * @param entry - The entry to check.
     * @return {@code true} if all available options has been selected for the entry; {@code false} otherwise.
     */
    public boolean hasSelectedAllOptions(@NotNull DialogEntryOptions entry) {
        final int optionCount = entry.getOptionSizeWithoutOptionsThatAdvanceDialog();
        final Set<OptionIndex> selectedOptions = this.selectedOptions.get(entry);
        
        return selectedOptions != null && selectedOptions.size() == optionCount;
    }
    
    /**
     * Sets whether this {@link DialogInstance} should await {@link Player} input.
     *
     * @param awaitInput - {@code true} to await input; {@code false} otherwise.
     */
    @ApiStatus.Internal
    public void awaitInput(final boolean awaitInput) {
        this.awaitInput = awaitInput;
    }
    
    /**
     * Attempts to select an option for the current {@link DialogEntry}.
     *
     * <p>
     * If the current entry is not {@link DialogEntryOptions}, the selection is ignored.
     * </p>
     *
     * @param index - The index of the option.
     * @return {@code true} if the option was selected; {@code false} otherwise.
     */
    public boolean trySelectOption(@NotNull OptionIndex index) {
        if (!(this.currentEntry instanceof DialogEntryOptions optionEntry)) {
            return false;
        }
        
        final DialogEntryOptions.Option entry = optionEntry.getOption(index);
        final Player player = getPlayer();
        
        // Do snap cursor even if the option is invalid
        player.getInventory().setHeldItemSlot(OPTION_RESTING_SLOT);
        
        if (entry != null) {
            entry.select(optionEntry, this);
        }
        
        return true;
    }
    
    /**
     * Jumps to the {@link DialogEntry} keyed by the given {@link Key}.
     *
     * <p>
     * If the key is empty or there is no entry by the given key, this method is silently ignored. Otherwise, all
     * the current {@code entries} will be cleared and the next entry will be the keyed one.
     * </p>
     *
     * @param key - The key of the entry.
     * @return {@code true} if successfully jumped to the entry; {@code false} otherwise.
     */
    public boolean jumpToEntry(@NotNull Key key) {
        final int index = dialog.indexOfKeyed(key);
        
        // Fail silently
        if (index == -1) {
            return false;
        }
        
        this.entries.clear();
        this.entries = dialog.entriesCopy()
                             .stream()
                             .skip(index)
                             .collect(Collectors.toCollection(ArrayDeque::new));
        
        this.nextEntry();
        
        return true;
    }
    
    @ApiStatus.Internal
    void startInstance0() {
        runTaskTimer(Eterna.getPlugin(), 1, 1);
        
        dialog.onDialogStart(player);
    }
    
}
