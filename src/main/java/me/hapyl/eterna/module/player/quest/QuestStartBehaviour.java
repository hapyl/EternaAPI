package me.hapyl.eterna.module.player.quest;

import me.hapyl.eterna.module.locaiton.Position;
import me.hapyl.eterna.module.player.dialog.Dialog;
import me.hapyl.eterna.module.reflect.npc.HumanNPC;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface QuestStartBehaviour {

    @Nonnull
    static QuestStartBehaviour onJoin() {
        return onJoin(true);
    }

    @Nonnull
    static QuestStartBehaviour onJoin(boolean shouldNotify) {
        return new OnJoin(shouldNotify);
    }

    @Nonnull
    static QuestStartBehaviour talkToNpc(@Nonnull HumanNPC npc, @Nonnull Dialog dialog) {
        return new TalkToNpc(npc, dialog);
    }

    @Nonnull
    static QuestStartBehaviour completeDialog(@Nonnull Dialog dialog) {
        return new CompleteDialog(dialog);
    }

    @Nonnull
    static QuestStartBehaviour goTo(@Nonnull Position position) {
        return goTo(position, null);
    }

    @Nonnull
    static QuestStartBehaviour goTo(@Nonnull Position position, @Nullable Dialog dialog) {
        return new GoTo(position, dialog);
    }

    interface DialogStartBehaviour extends QuestStartBehaviour {
        @Nullable
        Dialog dialog();
    }

    record OnJoin(boolean sendNotification) implements QuestStartBehaviour {
    }

    record GoTo(@Nonnull Position position, @Nullable Dialog dialog) implements DialogStartBehaviour {
        @Nullable
        @Override
        public Dialog dialog() {
            return this.dialog;
        }
    }

    record TalkToNpc(@Nonnull HumanNPC npc, @Nonnull Dialog dialog) implements DialogStartBehaviour {
        @Nonnull
        @Override
        public Dialog dialog() {
            return this.dialog;
        }
    }

    record CompleteDialog(@Nonnull Dialog dialog) implements DialogStartBehaviour {
        @Nonnull
        @Override
        public Dialog dialog() {
            return this.dialog;
        }
    }

}
