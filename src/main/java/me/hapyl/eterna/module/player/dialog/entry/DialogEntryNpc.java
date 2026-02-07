package me.hapyl.eterna.module.player.dialog.entry;

import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.player.dialog.DialogInstance;
import me.hapyl.eterna.module.registry.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link DialogEntry} that sends an {@link Npc} message to a {@link Player}.
 */
public class DialogEntryNpc extends DialogEntryText {
    
    private final Npc npc;
    
    /**
     * Creates a new {@link DialogEntryNpc}.
     *
     * @param key  - The key of the entry.
     * @param npc  - The npc who is talking.
     * @param text - The text to display.
     */
    public DialogEntryNpc(@NotNull Key key, @NotNull Npc npc, @NotNull Component text) {
        super(key, text);
        
        this.npc = npc;
    }
    
    /**
     * Creates a new {@link DialogEntryNpc}.
     *
     * @param npc  - The npc who is talking.
     * @param text - The text to display.
     */
    public DialogEntryNpc(@NotNull Npc npc, @NotNull Component text) {
        this(Key.empty(), npc, text);
    }
    
    /**
     * Displays the text for a {@link Player}.
     *
     * <p>
     * The displayed text is following the {@link Npc#getNpcMessageFormat()}.
     * </p>
     *
     * @param dialogInstance - The dialog instant to display to.
     */
    @Override
    public void display(@NotNull DialogInstance dialogInstance) {
        final Player player = dialogInstance.getPlayer();
        
        npc.sendMessage(player, text);
    }
}
