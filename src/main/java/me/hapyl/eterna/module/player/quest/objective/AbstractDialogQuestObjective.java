package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.player.dialog.Dialog;
import me.hapyl.eterna.module.player.quest.QuestData;
import me.hapyl.eterna.module.player.quest.QuestObjectArray;
import me.hapyl.eterna.module.player.quest.QuestObjective;

import javax.annotation.Nonnull;

public abstract class AbstractDialogQuestObjective extends QuestObjective {
    AbstractDialogQuestObjective(@Nonnull String name, @Nonnull String description) {
        super(name, description, 1);
    }

    public abstract boolean test(@Nonnull QuestData data, @Nonnull Dialog dialog);

    @Nonnull
    @Override
    public final Response test(@Nonnull QuestData data, @Nonnull QuestObjectArray object) {
        final Dialog dialog = object.getAs(0, Dialog.class);

        if (dialog == null) {
            return Response.testFailed();
        }

        final boolean success = test(data, dialog);
        return success ? Response.testSucceeded() : Response.testFailed();
    }
}
