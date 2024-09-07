package me.hapyl.eterna.module.player.quest.objective;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import me.hapyl.eterna.module.player.dialog.Dialog;
import me.hapyl.eterna.module.player.quest.QuestData;
import me.hapyl.eterna.module.player.quest.QuestObjective;
import me.hapyl.eterna.module.reflect.npc.HumanNPC;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Range;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * A {@link QuestObjective} for the completion of which the player must talk to multiple npc.
 * <p>The given dialog will be started automatically if this is the current objective of the quest and the player clicks at the given npc.</p>
 * <p>The objective is considered as 'complete' only after the player talks to all npc.</p>
 * <p>The talk progress is kept on player's UUID, meaning rejoining the server is supported.</p>
 * <p>This objective supports a 'backing' dialog, which is displayed if player talks to an ncp again.</p>
 *
 * @see TalkToNpcQuestObjective
 */
public class TalkToMultipleNpcQuestObjective extends AbstractDialogQuestObjective {

    private static final int MIN_DIALOGS = 2;

    private final Map<HumanNPC, Entry> dialogs;

    /**
     * A {@link QuestObjective} for the completion of which the player must talk to multiple npc.
     * <p>The given dialog will be started automatically if this is the current objective of the quest and the player clicks at the given npc.</p>
     * <p>The objective is considered as 'complete' only after the player talks to all npc.</p>
     * <p>The talk progress is kept on player's UUID, meaning rejoining the server is supported.</p>
     * <p>This objective supports a 'backing' dialog, which is displayed if player talks to an ncp again.</p>
     *
     * @param entries - Entries.
     * @throws IllegalArgumentException If you attempt to create the objective with less than {@link #MIN_DIALOGS} entries.
     *                                  For single npc, use {@link TalkToNpcQuestObjective}.
     * @see TalkToMultipleNpcQuestObjective#entry(HumanNPC, Dialog, Dialog)
     */
    public TalkToMultipleNpcQuestObjective(@Range(from = 2, to = Integer.MAX_VALUE) @Nonnull List<Entry> entries) {
        super("Talk to NPCs.");

        if (entries.size() < MIN_DIALOGS) {
            throw new IllegalArgumentException(
                    "%s requires at least two entries, got %s! For single dialog use %s.".formatted(
                            getClass().getSimpleName(),
                            entries.size(),
                            TalkToNpcQuestObjective.class.getSimpleName()
                    ));
        }

        this.dialogs = Maps.newHashMap();

        // Add entries
        entries.forEach(entry -> this.dialogs.put(entry.npc, entry));
    }

    /**
     * Returns either one of the dialogs that will be displayed to the player,
     * or {@code null} if the given {@link HumanNPC} does not have a dialog in this objective.
     *
     * @param npc    - The npc to get the dialog for.
     * @param player - The player to get the dialog for.
     * @return either one of the dialogs that will be displayed to the player,
     * or {@code null} if the given {@link HumanNPC} does not have a dialog in this objective.
     */
    @Nullable
    public Dialog getDialog(@Nonnull HumanNPC npc, @Nonnull Player player) {
        for (Entry entry : dialogs.values()) {
            if (!entry.npc.equals(npc)) {
                continue;
            }

            return entry.hasTalked.contains(player.getUniqueId()) ? entry.dialogTalked : entry.dialog;
        }

        return null;
    }

    /**
     * Gets the progress of the given player.
     *
     * @param player - The player to get the progress for.
     * @return the progress of the given player, where {@code 0.0} is haven't talked to anyone and {@code 1.0} is talked to everyone.
     */
    public @Range(from = 0, to = 1) double getProgress(@Nonnull Player player) {
        final UUID uuid = player.getUniqueId();
        int talked = 0;

        for (Entry entry : dialogs.values()) {
            if (entry.hasTalked.contains(uuid)) {
                talked++;
            }
        }

        return (double) talked / dialogs.size();
    }

    @Override
    public boolean test(@Nonnull QuestData data, @Nonnull Dialog dialog) {
        final Player player = data.getPlayer();
        final UUID uuid = player.getUniqueId();

        dialogs.values().forEach(entry -> {
            if (entry.dialog == dialog || entry.dialogTalked == dialog) {
                entry.hasTalked.add(uuid);
            }
        });

        return hasTalkedToEveryone(player);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void onFail(@Nonnull Player player) {
        clearTalked(player);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void onComplete(@Nonnull Player player) {
        clearTalked(player);
    }

    private boolean hasTalkedToEveryone(Player player) {
        final UUID uuid = player.getUniqueId();

        for (Entry entry : dialogs.values()) {
            if (!entry.hasTalked.contains(uuid)) {
                return false;
            }
        }

        return true;
    }

    private void clearTalked(Player player) {
        final UUID uuid = player.getUniqueId();
        dialogs.values().forEach(entry -> entry.hasTalked.remove(uuid));
    }

    /**
     * Creates an {@link Entry}.
     *
     * @param npc           - The npc of the dialog.
     * @param dialog        - The dialog to be displayed.
     * @param backingDialog - The backing dialog which is displayed after the player has already talked to the npc.
     */
    @Nonnull
    public static Entry entry(@Nonnull HumanNPC npc, @Nonnull Dialog dialog, @Nullable Dialog backingDialog) {
        return new Entry(npc, dialog, backingDialog);
    }

    public static class Entry {
        private final HumanNPC npc;
        private final Dialog dialog;
        private final Dialog dialogTalked;
        private final Set<UUID> hasTalked;

        private Entry(HumanNPC npc, Dialog dialog, Dialog dialogTalked) {
            this.npc = npc;
            this.dialog = dialog;
            this.dialogTalked = dialogTalked;
            this.hasTalked = Sets.newHashSet();
        }

        @Override
        public String toString() {
            return hasTalked.toString();
        }
    }
}
