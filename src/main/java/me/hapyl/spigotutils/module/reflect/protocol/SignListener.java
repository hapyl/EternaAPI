package me.hapyl.spigotutils.module.reflect.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.hapyl.spigotutils.module.inventory.Response;
import me.hapyl.spigotutils.module.inventory.SignGUI;
import org.bukkit.entity.Player;

public class SignListener extends ProtocolListener {

    public SignListener() {
        super(PacketType.Play.Client.UPDATE_SIGN);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        final PacketContainer packet = event.getPacket();
        final Player player = event.getPlayer();
        final SignGUI signGUI = SignGUI.savedCopy().get(player);

        if (signGUI == null) {
            return;
        }

        final String[] lines = packet.getStringArrays().read(0);
        signGUI.onResponse(new Response(player, lines));
        signGUI.clearSign();
        SignGUI.remove(player);
    }

    @Override
    public void onPacketSending(PacketEvent adapter) {
    }
}
