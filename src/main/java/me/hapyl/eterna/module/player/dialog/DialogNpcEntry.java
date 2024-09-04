package me.hapyl.eterna.module.player.dialog;

import me.hapyl.eterna.module.reflect.npc.HumanNPC;
import me.hapyl.eterna.module.util.Placeholder;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Represents an entry that sends the given string as if a {@link HumanNPC} sent it.
 * <br>
 * The entry uses {@link HumanNPC#sendNpcMessage(Player, String)}, meaning {@link Placeholder} is supported.
 */
public class DialogNpcEntry extends DialogString {

    protected final HumanNPC npc;

    public DialogNpcEntry(@Nonnull HumanNPC npc, @Nonnull String string) {
        super(string);

        this.npc = npc;
    }

    @Override
    public void run(@Nonnull DialogInstance dialog) {
        final Player player = dialog.getPlayer();

        npc.sendNpcMessage(player, string);
    }
}
