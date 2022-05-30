package me.hapyl.spigotutils.module.reflect.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedEnumEntityUseAction;
import me.hapyl.spigotutils.module.reflect.npc.ClickType;
import me.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import me.hapyl.spigotutils.module.util.Runnables;
import org.bukkit.entity.Player;

public class HumanNPCListener extends ProtocolListener {

    public HumanNPCListener() {
        super(PacketType.Play.Client.USE_ENTITY);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        final Player player = event.getPlayer();
        final PacketContainer packet = event.getPacket();

        final int entityId = packet.getIntegers().read(0);
        final HumanNPC npc = HumanNPC.getById(entityId);

        if (npc == null) {
            return;
        }

        final StructureModifier<WrappedEnumEntityUseAction> useAction = packet.getEnumEntityUseActions();
        final WrappedEnumEntityUseAction action = useAction.read(0);

        final EnumWrappers.EntityUseAction clickAction = action.getAction();
        final EnumWrappers.Hand clickHand = clickAction == EnumWrappers.EntityUseAction.ATTACK ? EnumWrappers.Hand.MAIN_HAND : action.getHand();

        Runnables.runSync(() -> {
            if (clickHand == EnumWrappers.Hand.OFF_HAND || clickAction == EnumWrappers.EntityUseAction.INTERACT_AT) {
                return;
            }

            npc.onClick(player, npc, ClickType.fromProtocol(clickAction));
            npc.onClickAuto(player);
        });

    }

    @Override
    public void onPacketSending(PacketEvent event) {
    }

}
