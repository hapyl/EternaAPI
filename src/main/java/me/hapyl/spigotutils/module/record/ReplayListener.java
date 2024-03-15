package me.hapyl.spigotutils.module.record;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class ReplayListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void handlePlayerInteractEvent(PlayerInteractEvent ev) {
        final Player player = ev.getPlayer();
        final Action action = ev.getAction();
        final EquipmentSlot hand = ev.getHand();

        if (action != Action.LEFT_CLICK_AIR && action != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        Record.fetchRecordAction(player, hand == EquipmentSlot.HAND ? RecordAction.ATTACK : RecordAction.ATTACK_OFFHAND);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleEntityDamageEvent(EntityDamageEvent ev) {
        if (!(ev.getEntity() instanceof Player player)) {
            return;
        }

        Record.fetchRecordAction(player, RecordAction.TAKE_DAMAGE);
    }

}
