package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.player.dialog.Dialog;
import me.hapyl.eterna.module.player.quest.QuestData;
import me.hapyl.eterna.module.player.quest.QuestObjective;
import me.hapyl.eterna.module.reflect.npc.HumanNPC;

import javax.annotation.Nonnull;

/**
 * A {@link QuestObjective} for the completion of which the player must talk to a npc.
 * <p>The given dialog will be started automatically if this is the current objective of the quest and the player clicks at the given npc.</p>
 *
 * @see TalkToMultipleNpcQuestObjective
 */
public class TalkToNpcQuestObjective extends AbstractDialogQuestObjective {

    public final HumanNPC npc;
    public final Dialog dialog;

    /**
     * Creates a new objective for the completion of which the player must craft an item.
     *
     * @param npc    - The npc to talk to.
     * @param dialog - The dialog to display.
     */
    public TalkToNpcQuestObjective(@Nonnull HumanNPC npc, @Nonnull Dialog dialog) {
        super("Talker", "Talk to %s.".formatted(npc.getName()));

        this.npc = npc;
        this.dialog = dialog;
    }

    @Override
    public boolean test(@Nonnull QuestData data, @Nonnull Dialog dialog) {
        return this.dialog == dialog;
    }
}
