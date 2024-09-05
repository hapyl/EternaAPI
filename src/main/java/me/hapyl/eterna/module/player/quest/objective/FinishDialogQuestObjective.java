package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.player.dialog.Dialog;
import me.hapyl.eterna.module.player.quest.QuestData;
import me.hapyl.eterna.module.player.quest.QuestObjective;

import javax.annotation.Nonnull;

/**
 * A {@link QuestObjective} for the completion of which the player must complete a {@link Dialog}.
 * <p>The given dialog must be started manually!</p>
 *
 * @see TalkToNpcQuestObjective
 * @see TalkToMultipleNpcQuestObjective
 */
public class FinishDialogQuestObjective extends AbstractDialogQuestObjective {

    public final Dialog dialog;

    /**
     * Creates a new objective for the completion of which the player must complete a {@link Dialog}.
     *
     * @param dialog - The dialog to complete.
     */
    public FinishDialogQuestObjective(@Nonnull Dialog dialog) {
        super("Talker", "Complete a dialog.");

        this.dialog = dialog;
    }

    @Override
    public boolean test(@Nonnull QuestData data, @Nonnull Dialog dialog) {
        return this.dialog == dialog;
    }

}
