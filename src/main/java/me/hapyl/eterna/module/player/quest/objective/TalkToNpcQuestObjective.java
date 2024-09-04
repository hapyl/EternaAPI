package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.player.dialog.NPCDialog;
import me.hapyl.eterna.module.player.quest.QuestObjective;
import me.hapyl.eterna.module.reflect.npc.HumanNPC;

import javax.annotation.Nonnull;

/**
 * A {@link QuestObjective} for the completion of which the player must finish a {@link NPCDialog}.
 * <p>This objective will automatically start the given dialog whenever the player clicks at the npc.</p>
 */
public class TalkToNpcQuestObjective extends FinishDialogQuestObjective {

    /**
     * Creates a new objective for the completion of which the player must finish a {@link NPCDialog}.
     *
     * @param dialog - The dialog to finish.
     */
    public TalkToNpcQuestObjective(@Nonnull NPCDialog dialog) {
        super(dialog);

        final HumanNPC npc = dialog.getNpc();

        setDescription("Talk to %s.".formatted(npc.getName()));
        setDialog(dialog);
    }

}
