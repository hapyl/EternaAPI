package me.hapyl.eterna.module.player.dialog;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.builtin.manager.DialogManager;
import me.hapyl.eterna.module.annotate.EventLike;
import me.hapyl.eterna.module.annotate.SelfReturn;
import me.hapyl.eterna.module.reflect.npc.HumanNPC;
import me.hapyl.eterna.module.util.BukkitUtils;
import me.hapyl.eterna.module.util.Named;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Represents a {@link Dialog}, usually between an {@link HumanNPC} and a {@link Player}.
 */
public class Dialog implements Named {
    
    private static final NamespacedKey attributeKey = BukkitUtils.createKey("eterna_dialog");
    
    protected final Queue<DialogEntry> entries;
    
    protected String name;
    protected String summary;
    
    public Dialog(@Nonnull String name) {
        this.entries = new LinkedList<>();
        this.name = name;
        this.summary = null;
    }
    
    /**
     * @deprecated Prefer naming dialogs.
     */
    @Deprecated
    public Dialog() {
        this("Unnamed Dialog");
    }
    
    /**
     * Gets the summary of this dialog, in case a player {@link DialogInstance#skip()} the dialog to optionally display the summary.
     *
     * @return the summary of this dialog.
     */
    @Nullable
    public String summary() {
        return summary;
    }
    
    /**
     * Sets the summary of this dialog, in case a player {@link DialogInstance#skip()} the dialog to optionally display the summary.
     *
     * @param summary - The new dialog summary. {@code null} to remove summary.
     */
    @SelfReturn
    public Dialog summary(@Nullable String summary) {
        this.summary = summary;
        return this;
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
     * Add all the given {@link DialogEntry} to the {@link Dialog}.
     *
     * @param entries - Entries to add.
     */
    public Dialog addEntry(@Nonnull List<DialogEntry> entries) {
        this.entries.addAll(entries);
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
        final AttributeInstance speedAttribute = Objects.requireNonNull(player.getAttribute(Attribute.MOVEMENT_SPEED), "Players must have MOVEMENT_SPEED attribute!");
        final AttributeInstance jumpAttribute = Objects.requireNonNull(player.getAttribute(Attribute.JUMP_STRENGTH), "Players must have JUMP_STRENGTH attribute!");
        
        speedAttribute.addTransientModifier(new AttributeModifier(attributeKey, -0.4, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
        jumpAttribute.addTransientModifier(new AttributeModifier(attributeKey, -1, AttributeModifier.Operation.ADD_NUMBER));
    }
    
    /**
     * Called whenever a {@link Player} finishes this {@link Dialog}.
     *
     * @param player - {@link Player} who has finished the {@link Dialog}.
     */
    @EventLike
    public void onDialogFinish(@Nonnull Player player) {
        final AttributeInstance speedAttribute = Objects.requireNonNull(player.getAttribute(Attribute.MOVEMENT_SPEED), "Players must have MOVEMENT_SPEED attribute!");
        final AttributeInstance jumpAttribute = Objects.requireNonNull(player.getAttribute(Attribute.JUMP_STRENGTH), "Players must have JUMP_STRENGTH attribute!");
        
        speedAttribute.removeModifier(attributeKey);
        jumpAttribute.removeModifier(attributeKey);
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
     * Called every tick a {@link Player} is listening to this {@link Dialog}.
     *
     * @param player - The player who is listening to this dialog.
     */
    @EventLike
    public void onDialogTick(@Nonnull Player player) {
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
    
    @Nonnull
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void setName(@Nonnull String name) {
        this.name = name;
    }
    
    /**
     * Gets a {@link DialogSkip} handler for this {@link Dialog}.
     * <p>Default to {@link DialogSkipGUI}</p>
     *
     * @param player - The player to create the handler for.
     */
    @Nonnull
    public DialogSkip skipHandler(@Nonnull Player player) {
        return new DialogSkipGUI(player, this);
    }
    
    /**
     * Returns true if the given {@link Player} is in <b>any</b> {@link Dialog} right now, false otherwise.
     *
     * @param player - Player
     * @return {@code true} if the given player is in any dialog right now, {@code false} otherwise.
     */
    public static boolean isInAnyDialog(@Nonnull Player player) {
        return Eterna.getManagers().dialog.get(player) != null;
    }
}
