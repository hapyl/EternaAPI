package me.hapyl.eterna.module.npc;

import me.hapyl.eterna.EternaHandler;
import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.event.protocol.PacketReceiveEvent;
import me.hapyl.eterna.module.reflect.packet.wrapped.PacketWrappers;
import me.hapyl.eterna.module.reflect.packet.wrapped.WrappedServerboundInteractPacket;
import me.hapyl.eterna.module.util.Disposable;
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
        
        ev.getWrappedPacket(PacketWrappers.SERVERBOUND_INTERACT).ifPresent(packet -> {
            get(packet.getEntityId()).ifPresent(npc -> {
                final WrappedServerboundInteractPacket.WrappedAction action = packet.getAction();
                final WrappedServerboundInteractPacket.WrappedActionType type = action.getType();
                final WrappedServerboundInteractPacket.WrappedHand hand = action.getHand();
                
                // Don't care about INTERACT_AT or OFF_HAND clicks
                if (type == WrappedServerboundInteractPacket.WrappedActionType.INTERACT_AT || hand == WrappedServerboundInteractPacket.WrappedHand.OFF_HAND) {
                    return;
                }
                
                ev.synchronize(() -> npc.onClick0(player, type == WrappedServerboundInteractPacket.WrappedActionType.ATTACK ? ClickType.ATTACK : ClickType.INTERACT));
            });
        });
    }
    
    @Override
    public void dispose() {
        this.forEach(Npc::dispose);
    }
}
