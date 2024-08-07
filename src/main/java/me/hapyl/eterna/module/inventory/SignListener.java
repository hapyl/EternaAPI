package me.hapyl.eterna.module.inventory;

import me.hapyl.eterna.module.event.protocol.PacketReceiveEvent;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SignListener implements Listener {

    @EventHandler()
    public void handlePacketReceiveEvent(PacketReceiveEvent ev) {
        final Player player = ev.getPlayer();
        final ServerboundSignUpdatePacket packet = ev.getPacket(ServerboundSignUpdatePacket.class);

        if (packet == null) {
            return;
        }

        final SignGUI signGUI = SignGUI.getMap().get(player);

        if (signGUI == null) {
            return;
        }

        final String[] lines = packet.getLines();

        signGUI.onResponse(new Response(player, lines));
        signGUI.clearSign();

        SignGUI.remove(player);
    }

}
