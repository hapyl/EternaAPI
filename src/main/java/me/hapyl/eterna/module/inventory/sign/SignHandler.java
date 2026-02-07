package me.hapyl.eterna.module.inventory.sign;

import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaKeyed;
import me.hapyl.eterna.module.event.protocol.PacketReceiveEvent;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class SignHandler extends EternaKeyed implements Listener {
    
    public SignHandler(@Nullable EternaKey key) {
        super(key);
    }
    
    @EventHandler()
    public void handlePacketReceiveEvent(PacketReceiveEvent ev) {
        final Player player = ev.getPlayer();
        
        ev.getPacket(ServerboundSignUpdatePacket.class).ifPresent(packet -> {
            final SignInput awaitingSign = SignInput.awaitingResponse.remove(player);
            
            // Not awaiting, don't care
            if (awaitingSign == null) {
                return;
            }
            
            final String[] lines = packet.getLines();
            
            awaitingSign.onResponse(new SignResponse(player, lines));
            awaitingSign.clearSign();
        });
    }
    
}
