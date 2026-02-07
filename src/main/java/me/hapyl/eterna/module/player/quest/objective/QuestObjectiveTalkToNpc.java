package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.player.dialog.Dialog;
import me.hapyl.eterna.module.player.dialog.DialogEndType;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link QuestObjective} for the completion of which the {@link Player} must <b><u>complete</u></b> a {@link Dialog}.
 *
 * <p>
 * This objective is identical to {@link QuestObjectiveCompleteDialog} with the only difference being that the {@link Dialog} is automatically started
 * whenever a {@link Player} clicks at the {@link Npc}.
 * </p>
 */
public class QuestObjectiveTalkToNpc extends QuestObjectiveCompleteDialog {
    
    private final Npc npc;
    
    /**
     * Creates a new {@link QuestObjectiveTalkToNpc}.
     *
     * @param npc             - The npc to talk to.
     * @param dialog          - The dialog to complete.
     * @param allowedEndTypes - The allowed end types that advance this objective, or {@code null} to allow all.
     */
    public QuestObjectiveTalkToNpc(@NotNull Npc npc, @NotNull Dialog dialog, @Nullable DialogEndType... allowedEndTypes) {
        super(Component.text("Talk to %s.".formatted(npc.getDefaultName())), dialog, allowedEndTypes);
        
        this.npc = npc;
    }
    
    /**
     * Gets the {@link Npc} to talk to.
     *
     * @return the npc to talk to.
     */
    @NotNull
    public Npc getNpc() {
        return npc;
    }
}
