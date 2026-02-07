package me.hapyl.eterna.module.player.quest;

import me.hapyl.eterna.module.location.Position;
import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.player.dialog.Dialog;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a {@link QuestStartBehaviour} that defines how a {@link Quest} is started.
 */
@ApiStatus.NonExtendable
public interface QuestStartBehaviour {
    
    /**
     * Creates a new {@link QuestStartBehaviour} that will start a {@link Quest} upon {@link Player} joining.
     *
     * @return a new start behaviour that will start the quest upon player joining.
     */
    @NotNull
    static QuestStartBehaviour onJoin() {
        return onJoin(false);
    }
    
    /**
     * Creates a new {@link QuestStartBehaviour} that will start a {@link Quest} upon {@link Player} joining.
     *
     * @param silent - {@code true} to not send formatter start and errors messages; {@code false} otherwise.
     * @return a new start behaviour that will start the quest upon player joining.
     */
    @NotNull
    static QuestStartBehaviour onJoin(boolean silent) {
        return new OnJoin(silent);
    }
    
    /**
     * Creates a new {@link QuestStartBehaviour} that will start a {@link Quest} upon {@link Player} completing the given {@link Dialog},
     * that will be started upon player clicking at the given {@link Npc}.
     *
     * @param npc    - The npc to interact with.
     * @param dialog - The dialog to complete.
     * @return a new start behaviour that will start the quest upon player talking to npc.
     */
    @NotNull
    static QuestStartBehaviour talkToNpc(@NotNull Npc npc, @NotNull Dialog dialog) {
        return new TalkToNpc(npc, dialog);
    }
    
    /**
     * Creates a new {@link QuestStartBehaviour} that will start a {@link Quest} upon {@link Player} completing the given {@link Dialog}.
     *
     * <p>
     * Note that the given {@link Dialog} must be started <b>manually</b>.
     * </p>
     *
     * @param dialog - The dialog to complete.
     * @return a new start behaviour that will start the quest upon player completing the dialog.
     */
    @NotNull
    static QuestStartBehaviour completeDialog(@NotNull Dialog dialog) {
        return new CompleteDialog(dialog);
    }
    
    /**
     * Creates a new {@link QuestStartBehaviour} that will start a {@link Quest} upon {@link Player} reaching the given {@link Position}.
     *
     * @param position - The position to go to.
     * @return a new start behaviour that will start the quest upon player reaching the given position.
     */
    @NotNull
    static QuestStartBehaviour goTo(@NotNull Position position) {
        return goTo(position, null);
    }
    
    /**
     * Creates a new {@link QuestStartBehaviour} that will start a {@link Quest} upon {@link Player} completing the {@link Dialog},
     * that will be started upon reaching the given {@link Position}.
     *
     * @param position - The position to go to.
     * @param dialog   - The dialog to display upon reaching the position.
     * @return a new start behaviour that will start the quest upon player reaching the given position.
     */
    @NotNull
    static QuestStartBehaviour goTo(@NotNull Position position, @Nullable Dialog dialog) {
        return new GoTo(position, dialog);
    }
    
    /**
     * Represents an abstract {@link QuestStartBehaviour} that requires a {@link Dialog}.
     */
    interface DialogStartBehaviour extends QuestStartBehaviour {
        @Nullable
        Dialog dialog();
    }
    
    /**
     * Represents a {@link QuestStartBehaviour} that will start a {@link Quest} upon {@link Player} joinig.
     *
     * @param silent - {@code true} to not send formatter start and errors messages; {@code false} otherwise.
     */
    record OnJoin(boolean silent) implements QuestStartBehaviour {
    }
    
    /**
     * Represents a {@link QuestStartBehaviour} that will start a {@link Quest} upon reaching the given {@link Position}.
     *
     * @param position - The position to reach.
     * @param dialog   - The dialog to display, or {@code null} to not display.
     */
    record GoTo(@NotNull Position position, @Nullable Dialog dialog) implements DialogStartBehaviour {
        @Nullable
        @Override
        public Dialog dialog() {
            return this.dialog;
        }
    }
    
    /**
     * Represents a {@link QuestStartBehaviour} that will start a {@link Quest} upon talking to the {@link Npc}.
     *
     * @param npc    - The npc to talk to.
     * @param dialog - The dialog to display.
     */
    record TalkToNpc(@NotNull Npc npc, @NotNull Dialog dialog) implements DialogStartBehaviour {
        @NotNull
        @Override
        public Dialog dialog() {
            return this.dialog;
        }
    }
    
    /**
     * Represents a {@link QuestStartBehaviour} that will start a {@link Quest} upon completing the {@link Dialog}.
     *
     * @param dialog - The dialog to complete.
     */
    record CompleteDialog(@NotNull Dialog dialog) implements DialogStartBehaviour {
        @NotNull
        @Override
        public Dialog dialog() {
            return this.dialog;
        }
    }
    
}
