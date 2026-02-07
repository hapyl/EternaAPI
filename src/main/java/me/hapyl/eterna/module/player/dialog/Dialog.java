package me.hapyl.eterna.module.player.dialog;

import me.hapyl.eterna.module.annotate.EventLike;
import me.hapyl.eterna.module.annotate.SelfReturn;
import me.hapyl.eterna.module.player.dialog.entry.DialogEntry;
import me.hapyl.eterna.module.player.dialog.entry.DialogEntryNpc;
import me.hapyl.eterna.module.player.dialog.entry.DialogEntryOptions;
import me.hapyl.eterna.module.player.dialog.entry.DialogEntryText;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.registry.Keyed;
import me.hapyl.eterna.module.util.BukkitUtils;
import me.hapyl.eterna.module.component.Named;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.*;

/**
 * Represents a {@link Dialog} conversation, that can be displayed to a {@link Player}.
 *
 * @see DialogEntry
 * @see DialogEntryText
 * @see DialogEntryNpc
 * @see DialogEntryOptions
 */
public class Dialog implements Keyed, Named {
    
    private static final NamespacedKey ATTRIBUTE_KEY = BukkitUtils.createKey("eterna_dialog");
    
    protected final Queue<DialogEntry> entries;
    
    private final Key key;
    private final Component name;
    
    private Component summary;
    
    /**
     * Creates a new {@link Dialog}.
     *
     * @param key  - The dialog key.
     * @param name - The dialog name.
     */
    public Dialog(@NotNull Key key, @NotNull Component name) {
        this.key = key;
        this.entries = new LinkedList<>();
        this.name = name;
        this.summary = null;
    }
    
    /**
     * Gets the {@link Key} of this {@link Dialog}.
     *
     * <p>
     * Note that even though dialogs are keyed, they are not registered nor stored by the Eterna.
     * The keys are used solely for comparing and consistency.
     * </p>
     *
     * @return the key of this dialog.
     */
    @NotNull
    @Override
    public final Key getKey() {
        return this.key;
    }
    
    /**
     * Gets the hash code of this {@link Dialog}.
     *
     * @return the hash code of this dialog.
     */
    @Override
    public final int hashCode() {
        return Objects.hashCode(this.key);
    }
    
    /**
     * Compares the given {@link Object} to this {@link Dialog}.
     *
     * @param object - The object to compare to.
     * @return {@code true} if the given object is a dialog and keys match; {@code false} otherwise.
     */
    @Override
    public final boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        
        final Dialog that = (Dialog) object;
        return Objects.equals(this.key, that.key);
    }
    
    /**
     * Gets the name of this {@link Dialog}.
     *
     * <p>
     * Names are mostly used for the default {@link DialogSkip} implementation.
     * </p>
     *
     * @return the name of this dialog.
     */
    @Override
    @NotNull
    public Component getName() {
        return name;
    }
    
    /**
     * Adds the given {@link DialogEntry} to the end of the entry list.
     *
     * @param entry - The entry to add.
     */
    @SelfReturn
    public Dialog addEntry(@NotNull DialogEntry entry) {
        this.entries.add(entry);
        return this;
    }
    
    /**
     * Adds the given array of {@link DialogEntry} to the end of the entry list.
     *
     * <p>
     * This is a convenience method for {@link DialogEntry} static factory methods since it always returns an array of entries.
     * </p>
     *
     * @param entries - The entries to add.
     */
    @SelfReturn
    public Dialog addEntry(@NotNull DialogEntry[] entries) {
        this.entries.addAll(List.of(entries));
        return this;
    }
    
    /**
     * Gets the summary of this {@link Dialog}, which is used in the default {@link DialogSkip}.
     *
     * @return the summary of this dialog.
     */
    @Nullable
    public Component getSummary() {
        return this.summary;
    }
    
    /**
     * Sets the summary of this {@link Dialog}, which is used in the default {@link DialogSkip}.
     *
     * @param summary - The summary to set.
     */
    @SelfReturn
    public Dialog setSummary(@Nullable Component summary) {
        this.summary = summary;
        return this;
    }
    
    /**
     * Creates new {@link DialogInstance} for this {@link Dialog}.
     *
     * <p>
     * This method is called for when any of the {@code start} methods succeeds, and is meant to be overridden in order
     * to process the optional {@link DialogTags} when needed, like adding additional {@link DialogEntry} for when a player
     * started the dialog with a specific condition.
     * </p>
     *
     * @param player - The player for whom to create the instance.
     * @param tags   - The optional dialog tags.
     * @return a new dialog instance.
     */
    @NotNull
    public DialogInstance newInstance(@NotNull Player player, @NotNull DialogTags tags) {
        return new DialogInstance(player, this);
    }
    
    /**
     * Forcefully starts this {@link Dialog} for the given {@link Player}, cancelling the previous {@link Dialog} if needed.
     *
     * @param player - The player for whom to start the dialog.
     * @param tags   - The optional dialog tags.
     */
    public final void startForcefully(@NotNull Player player, @NotNull DialogTags tags) {
        // Cancel previous instant if exists
        final DialogHandler handler = DialogHandler.handler;
        
        handler.get(player).ifPresent(DialogInstance::cancel);
        
        // Start a new instance
        final DialogInstance instance = newInstance(player, tags);
        handler.register(player, instance);
        
        instance.startInstance0();
    }
    
    /**
     * Forcefully starts this {@link Dialog} for the given {@link Player}, cancelling the previous {@link Dialog} if needed.
     *
     * @param player - The player for whom to start the dialog.
     */
    public final void startForcefully(@NotNull Player player) {
        this.startForcefully(player, DialogTags.empty());
    }
    
    /**
     * Attempts to start this {@link Dialog} for the given {@link Player}.
     *
     * @param player - The player for whom to start the dialog.
     * @param tag    - The optional dialogs tags.
     * @return {@code true} if the dialog has started for the player; {@code false} otherwise.
     */
    public final boolean start(@NotNull Player player, @NotNull DialogTags tag) {
        if (DialogHandler.handler.keyPresent(player)) {
            return false;
        }
        
        startForcefully(player, tag);
        return true;
    }
    
    /**
     * Attempts to start this {@link Dialog} for the given {@link Player}.
     *
     * @param player - The player for whom to start the dialog.
     * @return {@code true} if the dialog has started for the player; {@code false} otherwise.
     */
    public final boolean start(@NotNull Player player) {
        return this.start(player, DialogTags.empty());
    }
    
    /**
     * Gets a copy of the {@code entries} as a {@link Deque}.
     *
     * @return a copy of the {@code entries} as a deque.
     */
    @NotNull
    public Deque<DialogEntry> entriesCopy() {
        return new ArrayDeque<>(entries);
    }
    
    /**
     * An event-like method which is called when this {@link Dialog} is started for a {@link Player}.
     *
     * <p>
     * The default implementation adds slowness and removes the ability to jump for a "zoom-in" effect. If the effect
     * is unwanted, implementations are free to override this method.
     * </p>
     *
     * @param player - The player for whom the dialog was started.
     */
    @EventLike
    public void onDialogStart(@NotNull Player player) {
        final AttributeInstance speedAttribute = Objects.requireNonNull(player.getAttribute(Attribute.MOVEMENT_SPEED), "Players must have MOVEMENT_SPEED attribute!");
        final AttributeInstance jumpAttribute = Objects.requireNonNull(player.getAttribute(Attribute.JUMP_STRENGTH), "Players must have JUMP_STRENGTH attribute!");
        
        speedAttribute.addTransientModifier(new AttributeModifier(ATTRIBUTE_KEY, -0.4, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
        jumpAttribute.addTransientModifier(new AttributeModifier(ATTRIBUTE_KEY, -1, AttributeModifier.Operation.ADD_NUMBER));
    }
    
    /**
     * An event-like method which is called when this {@link Dialog} ends for a {@link Player}.
     *
     * @param player        - The player for whom the dialog ended.
     * @param dialogEndType - The dialog end type.
     */
    @EventLike
    public void onDialogEnd(@NotNull Player player, @NotNull DialogEndType dialogEndType) {
        final AttributeInstance speedAttribute = Objects.requireNonNull(player.getAttribute(Attribute.MOVEMENT_SPEED), "Players must have MOVEMENT_SPEED attribute!");
        final AttributeInstance jumpAttribute = Objects.requireNonNull(player.getAttribute(Attribute.JUMP_STRENGTH), "Players must have JUMP_STRENGTH attribute!");
        
        speedAttribute.removeModifier(ATTRIBUTE_KEY);
        jumpAttribute.removeModifier(ATTRIBUTE_KEY);
    }
    
    /**
     * An event-like method which is called every tick this {@link Dialog} is being shown for a {@link Player}.
     *
     * @param player - The player for whom this dialog is ticking.
     */
    @EventLike
    public void onDialogTick(@NotNull Player player) {
    }
    
    /**
     * Gets the delay for the given {@link DialogEntry}.
     *
     * <p>
     * This method is meant to overridden for when a conditional adjustment for an {@link DialogEntry} delay is needed for the given {@link Player}.
     * </p>
     *
     * @param player - The player for whom to get the delay.
     * @param entry  - The entry for which to get the delay.
     * @return the delay for the entry.
     * @implNote Even though the {@link Range} annotation indicates that the return type must be positive, it is only bound to a minimum of {@code 1} but not the maximum.
     */
    @Range(from = 1, to = Integer.MAX_VALUE)
    public int getEntryDelay(@NotNull Player player, @NotNull DialogEntry entry) {
        return entry.getDelay();
    }
    
    /**
     * Gets the {@link DialogSkip} for this {@link Dialog}.
     *
     * <p>
     * The default implementation uses {@link DialogSkipDefaultImpl} as a confirmation and dialog summary display.
     * </p>
     *
     * @param player - The player for whom the dialog is being skipped.
     * @return the dialog skip.
     * @see DialogSkip
     */
    @NotNull
    public DialogSkip getDialogSkip(@NotNull Player player) {
        return new DialogSkipDefaultImpl(this);
    }
    
    /**
     * Gets the index of the {@link DialogEntry} keyed with the given {@link Key}, or {@code -1} if key is empty or no keyed entry by that key.
     *
     * @param key - The key to search.
     * @return the index of the entry, or {@code -1} otherwise.
     */
    public int indexOfKeyed(@NotNull Key key) {
        if (key.isEmpty()) {
            return -1;
        }
        
        int index = 0;
        
        for (DialogEntry entry : entries) {
            if (entry.getKey().equals(key)) {
                return index;
            }
            
            index++;
        }
        
        return -1;
    }
    
    /**
     * A static method for retrieving the current {@link DialogInstance} for the given {@link Player}.
     *
     * @param player - The player for whom to retrieve the instance.
     * @return the current dialog instance wrapped in an optional.
     */
    @NotNull
    public static Optional<DialogInstance> getCurrentDialog(@NotNull Player player) {
        return DialogHandler.handler.get(player);
    }
    
}
