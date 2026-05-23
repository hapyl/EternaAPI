package me.hapyl.eterna.module.npc;

import me.hapyl.eterna.EternaHandler;
import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.event.protocol.PacketReceiveEvent;
import me.hapyl.eterna.module.util.Disposable;
import net.minecraft.network.protocol.game.ServerboundAttackPacket;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.world.InteractionHand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public final class NpcHandler extends EternaHandler<Integer, Npc> implements Disposable, Listener {
    
    static NpcHandler handler;
    
    public NpcHandler(@NotNull EternaKey key, @NotNull EternaPlugin eterna) {
        super(key, eterna);
        
        handler = this;
    }
    
    @EventHandler()
    public void handlePacketReceiveEvent(PacketReceiveEvent ev) {
        final Player player = ev.getPlayer();
        
        // Handle interact packet
        ev.getPacket(ServerboundInteractPacket.class).ifPresent(packet -> {
            get(packet.entityId()).ifPresent(npc -> {
                // Don't care about OFF_HAND clicks
                if (packet.hand() == InteractionHand.OFF_HAND) {
                    return;
                }
                
                // Synchronize the onClick method call
                ev.synchronize(() -> npc.onClick0(player, ClickType.RIGHT_CLICK));
            });
        });
        
        // Handler attack packet
        ev.getPacket(ServerboundAttackPacket.class).ifPresent(packet -> {
            get(packet.entityId()).ifPresent(npc -> {
                ev.synchronize(() -> npc.onClick0(player, ClickType.LEFT_CLICK));
            });
        });
    }
    
    @Override
    public void dispose() {
        this.forEach(Npc::dispose);
    }
}
