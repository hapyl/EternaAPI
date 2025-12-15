package me.hapyl.eterna.module.player.quest;

import me.hapyl.eterna.module.locaiton.Position;
import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.player.dialog.Dialog;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a start behaviour of a {@link Quest}.
 */
public interface QuestStartBehaviour {
    
    /**
     * The quest will be started automatically whenever a player joins the server.
     */
    @Nonnull
    static QuestStartBehaviour onJoin() {
        return onJoin(true);
    }
    
    /**
     * The quest will be started automatically whenever a player joins the server.
     *
     * @param shouldNotify - Whether to send the {@link QuestFormatter#sendQuestStartedFormat(Player, Quest)} message to the player.
     */
    @Nonnull
    static QuestStartBehaviour onJoin(boolean shouldNotify) {
        return new OnJoin(shouldNotify);
    }
    
    /**
     * The quest will be started whenever a player finished the given {@link Dialog}.
     * <p>The dialog will be automatically started whenever a player clicks at the given {@link Npc}.</p>
     *
     * @param npc    - The npc the player must interact with.
     * @param dialog - The dialog to displays before starting the quest.
     */
    @Nonnull
    static QuestStartBehaviour talkToNpc(@Nonnull Npc npc, @Nonnull Dialog dialog) {
        return new TalkToNpc(npc, dialog);
    }
    
    /**
     * The quest will be started whenever a player finished the given {@link Dialog}.
     *
     * @param dialog - The dialog to displays before starting the quest.
     */
    @Nonnull
    static QuestStartBehaviour completeDialog(@Nonnull Dialog dialog) {
        return new CompleteDialog(dialog);
    }
    
    /**
     * The quest will be started whenever a player is within the given {@link Position}.
     * <p>The quest will immediately start upon player reaching the given position.</p>
     *
     * @param position - The position.
     */
    @Nonnull
    static QuestStartBehaviour goTo(@Nonnull Position position) {
        return goTo(position, null);
    }
    
    /**
     * The quest will be started whenever a player is within the given {@link Position}.
     * <p>Whenever a player reaches the given position, the given dialog will be played and the quest will start after the dialog is finished.</p>
     *
     * @param position - The position.
     */
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
    
    record TalkToNpc(@Nonnull Npc npc, @Nonnull Dialog dialog) implements DialogStartBehaviour {
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
