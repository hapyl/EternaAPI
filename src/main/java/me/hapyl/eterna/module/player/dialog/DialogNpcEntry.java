package me.hapyl.eterna.module.player.dialog;

import me.hapyl.eterna.module.component.Components;
import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.npc.NpcPlaceholder;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Represents an entry that sends the given string as if a {@link Npc} sent it.
 * <br>
 * The entry uses {@link Npc#sendMessage(Player, Component)}, meaning {@link NpcPlaceholder} is supported.
 */
public class DialogNpcEntry extends DialogString {

    protected final Npc npc;

    public DialogNpcEntry(@Nonnull Npc npc, @Nonnull String string) {
        super(string);

        this.npc = npc;
    }

    @Override
    public void run(@Nonnull DialogInstance dialog) {
        final Player player = dialog.getPlayer();

        npc.sendMessage(player, Components.ofLegacy(string));
    }
}
