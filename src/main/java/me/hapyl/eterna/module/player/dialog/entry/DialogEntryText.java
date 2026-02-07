package me.hapyl.eterna.module.player.dialog.entry;

import me.hapyl.eterna.EternaColors;
import me.hapyl.eterna.module.component.Components;
import me.hapyl.eterna.module.player.dialog.DialogInstance;
import me.hapyl.eterna.module.registry.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link DialogEntry} that sends a message to a {@link Player}.
 *
 * <p>
 * The default implementation sends a {@link EternaColors#LIGHT_GRAY} italicized message prefixed by a space.
 * </p>
 */
public class DialogEntryText extends DialogEntryImpl {
    
    public static final Style TEXT_STYLE = Style.style(EternaColors.LIGHT_GRAY, TextDecoration.ITALIC);
    
    protected final Component text;
    protected final int length;
    
    /**
     * Creates a new {@link DialogEntryText}.
     *
     * @param key  - The key of the entry.
     * @param text - The text to send.
     */
    public DialogEntryText(@NotNull Key key, @NotNull Component text) {
        super(key);
        
        this.text = text;
        this.length = Components.lengthOf(text);
    }
    
    /**
     * Creates a new {@link DialogEntryText}.
     *
     * @param text - The text to send.
     */
    public DialogEntryText(@NotNull Component text) {
        this(Key.empty(), text);
    }
    
    /**
     * Displays the text for a {@link Player}.
     *
     * @param dialogInstance - The dialog instant to display to.
     */
    @Override
    public void display(@NotNull DialogInstance dialogInstance) {
        dialogInstance.getPlayer().sendMessage(Component.text(" ").append(text.style(TEXT_STYLE)));
    }
    
    /**
     * Gets the delay, in ticks, before the next {@link DialogEntry} is displayed.
     *
     * <p>
     * The default implementation delay is based on the length of the {@link Component} text.
     * </p>
     *
     * @return the delay in ticks, before the next entry is displayed.
     * @see Components#lengthOf(Component)
     */
    @Override
    public int getDelay() {
        return Math.clamp(length, 20, 200);
    }
}
