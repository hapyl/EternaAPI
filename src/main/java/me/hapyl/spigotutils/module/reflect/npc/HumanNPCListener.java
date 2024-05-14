package me.hapyl.spigotutils.module.reflect.npc;

import me.hapyl.spigotutils.EternaLogger;
import me.hapyl.spigotutils.module.event.protocol.PacketReceiveEvent;
import me.hapyl.spigotutils.module.reflect.Reflect;
import me.hapyl.spigotutils.module.util.Runnables;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import net.minecraft.world.EnumHand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.Method;

public class HumanNPCListener implements Listener {

    public HumanNPCListener() {
    }

    @EventHandler()
    public void handlePacketReceiveEvent(PacketReceiveEvent ev) {
        final Player player = ev.getPlayer();
        final PacketPlayInUseEntity packet = ev.getPacket(PacketPlayInUseEntity.class);

        if (packet == null) {
            return;
        }

        final Integer entityId = Reflect.getDeclaredFieldValue(packet, "b", Integer.class);

        if (entityId == null) {
            return;
        }

        final HumanNPC npc = HumanNPC.getById(entityId);

        if (npc == null) {
            return;
        }

        final Object useAction = Reflect.getDeclaredFieldValue(packet, "c", Object.class);

        if (useAction == null) {
            return;
        }

        try {
            final Method method = useAction.getClass().getDeclaredMethod("a");
            method.setAccessible(true);

            final Object actionType = method.invoke(useAction);
            final String actionTypeString = actionType.toString();

            // Don't care about INTERACT_AT
            if (actionTypeString.equals("INTERACT_AT")) {
                return;
            }

            // Attack doesn't have EnumHand field, have to handle it separately
            if (actionTypeString.equals("ATTACK")) {
                workClick(player, npc, ClickType.ATTACK);
            }
            else {
                final EnumHand enumHand = Reflect.getDeclaredFieldValue(useAction, "a", EnumHand.class);

                if (enumHand != EnumHand.b) {
                    workClick(player, npc, ClickType.INTERACT);
                }
            }
        } catch (Exception e) {
            EternaLogger.exception(e);
        }
    }

    private void workClick(Player player, HumanNPC npc, ClickType clickType) {
        Runnables.runSync(() -> npc.onClickAuto(player, clickType));
    }

}
