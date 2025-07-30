package me.hapyl.eterna.module.player.dialog;

import com.google.common.collect.Maps;
import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.builtin.manager.QuestManager;
import me.hapyl.eterna.module.player.quest.QuestStartBehaviour;
import me.hapyl.eterna.module.player.quest.objective.AbstractDialogQuestObjective;
import me.hapyl.eterna.module.util.Compute;
import me.hapyl.eterna.module.util.Runnables;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

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
     * Forcefully finished this dialog, and, if exists, displays the {@link Dialog#summary()}.
     * <p>This method is equivalent to player finishing the dialog and it advanced quests.</p>
     */
    public void skip() {
        // Force dialog to pause, this won't actually activate the input
        awaitInput = true;
        
        final DialogSkip handler = dialog.skipHandler(player);
        
        // Else try to display and either confirm/cancel
        final BukkitRunnable task = Runnables.makeTask(
                () -> {
                    tick = 25;
                    awaitInput = false;
                    
                    handler.onTimeout(player);
                }, _task -> _task.runTaskLater(Eterna.getPlugin(), handler.awaitTime())
        );
        
        final CompletableFuture<Boolean> response = handler.prompt(player);
        
        response.whenComplete((success, _t) -> {
            if (success == null) {
                throw new RuntimeException(_t);
            }
            
            if (success) {
                dialogFinished();
            }
            else {
                tick = 15;
                awaitInput = false;
            }
            
            task.cancel();
        });
    }
    
    /**
     * Skips the current dialog without any prompts.
     * <p>This method is equivalent to player finishing the dialog and it advanced quests.</p>
     */
    public void skipNoPrompt() {
        dialogFinished();
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
    public void hijackEntries(@Nonnull List<DialogEntry> entries) {
        for (int i = entries.size() - 1; i >= 0; i--) {
            hijackEntry(entries.get(i));
        }
    }
    
    @Override
    public final void run() {
        // Player left
        if (!player.isOnline()) {
            cancel();
            return;
        }
        
        dialog.onDialogTick(player);
        
        // Wait for player dialog
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
    public final void nextEntry() {
        currentEntry = entries.pollFirst();
        
        // Dialog finished
        if (currentEntry == null) {
            dialogFinished();
            return;
        }
        
        currentEntry.run(this);
        
        int delay = dialog.getEntryDelay(currentEntry, player);
        
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
        cancel0();
        Eterna.getManagers().dialog.unregister(player);
    }
    
    /**
     * Forcefully cancels this {@link DialogInstance} without unregistering it.
     *
     * @throws IllegalStateException If the dialog was not started.
     */
    public final synchronized void cancel0() throws IllegalStateException {
        super.cancel();
        dialog.onDialogFinish(player);
    }
    
    /**
     * Stars this {@link DialogInstance}.
     */
    public final void doStartInstance() {
        runTaskTimer(EternaPlugin.getPlugin(), 1, 1);
        
        dialog.onDialogStart(player);
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
    
    private void dialogFinished() {
        dialog.onDialogComplete(player); // onDialogComplete() differs from onDialogFinished()!
        selectedOptions.clear();
        
        final QuestManager questManager = Eterna.getManagers().quest;
        
        // Try to start the quest
        questManager.tryStartQuest(
                player, quest -> {
                    for (QuestStartBehaviour startBehaviour : quest.getStartBehaviours()) {
                        if (!(startBehaviour instanceof QuestStartBehaviour.DialogStartBehaviour dialogStartBehaviour)) {
                            continue;
                        }
                        
                        if (this.dialog != dialogStartBehaviour.dialog()) {
                            continue;
                        }
                        
                        return true;
                    }
                    
                    return false;
                }
        );
        
        // Increment objective
        questManager.tryIncrementObjective(player, AbstractDialogQuestObjective.class, dialog);
        
        this.cancel();
    }
    
}
