package me.hapyl.eterna.module.player.dialog;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.builtin.manager.DialogManager;
import me.hapyl.eterna.module.annotate.EventLike;
import me.hapyl.eterna.module.reflect.npc.HumanNPC;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Represents a {@link Dialog}, usually between an {@link HumanNPC} and a {@link Player}.
 */
public class Dialog {

    protected final Queue<DialogEntry> entries;

    public Dialog(@Nonnull Dialog dialog) {
        this.entries = new LinkedList<>(dialog.entries);
    }

    public Dialog() {
        this.entries = new LinkedList<>();
    }

    /**
     * Creates a new {@link DialogInstance}.
     *
     * @param player - Profile.
     * @param tags   - Optional dialog tags.
     * @return a new {@link DialogInstance}.
     */
    @Nonnull
    public DialogInstance newInstance(@Nonnull Player player, @Nonnull DialogTags tags) {
        return new DialogInstance(player, this);
    }

    /**
     * Forcefully starts the {@link Dialog} for the given {@link Player},
     * cancelling the previous {@link DialogInstance} if it exists.
     *
     * @param player - Player to start the dialog for.
     * @param tags   - Optional dialog tags.
     */
    public void startForcefully(@Nonnull Player player, @Nonnull DialogTags tags) {
        final DialogManager dialogManager = Eterna.getManagers().dialog;
        final DialogInstance previousDialog = dialogManager.get(player);

        // Cancel previous dialog
        if (previousDialog != null) {
            previousDialog.cancel();
        }

        // Start new dialog
        final DialogInstance dialog = newInstance(player, tags);
        dialogManager.register(player, dialog);

        dialog.doStartInstance();
    }

    /**
     * Forcefully starts the {@link Dialog} for the given {@link Player},
     * cancelling the previous {@link DialogInstance} if it exists.
     *
     * @param player - Player to start the dialog for.
     */
    public final void startForcefully(@Nonnull Player player) {
        startForcefully(player, DialogTags.empty());
    }

    /**
     * Attempts to start the {@link Dialog} for the given {@link Player}.
     * <br>
     * The dialog only starts if the given player is not currently in a dialog.
     *
     * @param player - Player to attempt to start the dialog for.
     * @param tags   - Optional tags.
     * @return {@code true} if the dialog was started, {@code false} otherwise.
     */
    public boolean start(@Nonnull Player player, @Nonnull DialogTags tags) {
        final DialogManager dialogManager = Eterna.getManagers().dialog;
        final DialogInstance dialog = dialogManager.get(player);

        if (dialog != null) {
            return false;
        }

        startForcefully(player, tags);
        return true;
    }

    /**
     * Attempts to start the {@link Dialog} for the given {@link Player}.
     * <br>
     * The dialog only starts if the given player is not currently in a dialog.
     *
     * @param player - Player to attempt to start the dialog for.
     * @return {@code true} if the dialog was started, {@code false} otherwise.
     */
    public final boolean start(@Nonnull Player player) {
        return start(player, DialogTags.empty());
    }

    /**
     * Returns true if the given {@link Player} is in <b>any</b> {@link Dialog} right now, false otherwise.
     *
     * @param player - Player
     * @return {@code true} if the given player is in any dialog right now, {@code false} otherwise.
     */
    public boolean isInAnyDialog(@Nonnull Player player) {
        return Eterna.getManagers().dialog.get(player) != null;
    }

    /**
     * Creates a copy of the {@link DialogEntry}.
     *
     * @return a copy of the entries.
     */
    @Nonnull
    public ArrayDeque<DialogEntry> entriesCopy() {
        return new ArrayDeque<>(entries);
    }

    /**
     * Adds a new {@link DialogEntry} to the {@link Dialog}.
     *
     * @param entry - Entry to add.
     */
    public Dialog addEntry(@Nonnull DialogEntry entry) {
        entries.offer(entry);
        return this;
    }

    /**
     * Add all the given {@link DialogEntry} to the {@link Dialog}.
     *
     * @param entries - Entries to add.
     */
    public Dialog addEntry(@Nonnull DialogEntry[] entries) {
        this.entries.addAll(List.of(entries));
        return this;
    }

    /**
     * Adds a {@link DialogNpcEntry} to the {@link Dialog} with the given {@link String}s.
     *
     * @param npc     - {@link HumanNPC} who will be sending the messages.
     * @param entries - Array of messages, each considered to be a different message.
     */
    public Dialog addEntry(@Nonnull HumanNPC npc, @Nonnull String... entries) {
        return addEntry(DialogEntry.of(npc, entries));
    }

    /**
     * Called whenever a {@link Player} starts this {@link Dialog}.
     *
     * @param player - {@link Player} who has started the {@link Dialog}.
     */
    @EventLike
    public void onDialogStart(@Nonnull Player player) {
        final AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_JUMP_STRENGTH);

        if (attribute != null) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, -1, 2, false, false, false));
            attribute.setBaseValue(0.0f);
        }
    }

    /**
     * Called whenever a {@link Player} finishes this {@link Dialog}.
     *
     * @param player - {@link Player} who has finished the {@link Dialog}.
     */
    @EventLike
    public void onDialogFinish(@Nonnull Player player) {
        final AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_JUMP_STRENGTH);

        if (attribute != null) {
            player.removePotionEffect(PotionEffectType.SLOWNESS);
            attribute.setBaseValue(0.45f);
        }
    }

    /**
     * Called whenever a {@link Player} <b>successfully</b> finishes this {@link Dialog}.
     *
     * @param player - {@link Player} who has <b>successfully</b> finished the {@link Dialog}.
     */
    @EventLike
    public void onDialogComplete(@Nonnull Player player) {
    }

    /**
     * Gets the delay before the next {@link DialogEntry} is displayed.
     * <p>Override to allow per-player delays.</p>
     *
     * @param entry  - The entry to get the delay for.
     * @param player - The player to get the delay for,
     * @return the delay before the next entry is displayed.
     */
    public int getEntryDelay(@Nonnull DialogEntry entry, @Nonnull Player player) {
        return entry.getDelay();
    }
}
