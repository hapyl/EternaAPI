package me.hapyl.spigotutils.module.inventory;

import me.hapyl.spigotutils.module.event.protocol.PacketReceiveEvent;
import net.minecraft.network.protocol.game.PacketPlayInUpdateSign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SignListener implements Listener {

    @EventHandler()
    public void handlePacketReceiveEvent(PacketReceiveEvent ev) {
        final Player player = ev.getPlayer();
        final PacketPlayInUpdateSign packet = ev.getPacket(PacketPlayInUpdateSign.class);

        if (packet == null) {
            return;
        }

        final SignGUI signGUI = SignGUI.getMap().get(player);

        if (signGUI == null) {
            return;
        }

        final String[] lines = packet.f();

        signGUI.onResponse(new Response(player, lines));
        signGUI.clearSign();

        SignGUI.remove(player);
    }

}
