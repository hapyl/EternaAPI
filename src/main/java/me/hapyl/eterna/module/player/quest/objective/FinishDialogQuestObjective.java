package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.player.dialog.Dialog;
import me.hapyl.eterna.module.player.quest.QuestData;
import me.hapyl.eterna.module.player.quest.QuestObjectArray;
import me.hapyl.eterna.module.player.quest.QuestObjective;

import javax.annotation.Nonnull;

/**
 * A {@link QuestObjective} for the completion of which the player must complete a {@link Dialog}.
 * <p>This objective requires manual dialog start by the plugin.</p>
 *
 * @see TalkToNpcQuestObjective
 */
public class FinishDialogQuestObjective extends QuestObjective {

    public final Dialog dialog;

    /**
     * Creates a new objective for the completion of which the player must complete a {@link Dialog}.
     *
     * @param dialog - The dialog to complete.
     */
    public FinishDialogQuestObjective(@Nonnull Dialog dialog) {
        super("Talker", "Complete a dialog.", 1);

        this.dialog = dialog;
    }

    @Nonnull
    @Override
    public Response test(@Nonnull QuestData data, @Nonnull QuestObjectArray object) {
        return object.compareAs(Dialog.class, dialog -> {
            return this.dialog == dialog;
        });
    }
}
