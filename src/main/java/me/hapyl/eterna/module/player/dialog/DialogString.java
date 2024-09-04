package me.hapyl.eterna.module.player.dialog;

import org.bukkit.ChatColor;

import javax.annotation.Nonnull;

/**
 * Represents an entry that sends a message to the player.
 * <br>
 * The default implementation simply sends an {@link ChatColor#ITALIC} message to the player.
 */
public class DialogString implements DialogEntry {

    protected final String string;

    public DialogString(String string) {
        this.string = string;
    }

    @Override
    public void run(@Nonnull DialogInstance dialog) {
        dialog.getPlayer().sendMessage(ChatColor.ITALIC + string);
    }

    /**
     * The delay for the next {@link DialogEntry} is calculated based on the length of the {@link String}.
     *
     * @return the delay for the next {@link DialogEntry}.
     */
    @Override
    public int getDelay() {
        return Math.clamp(string.length(), 20, 200);
    }
}
