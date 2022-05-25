package kz.hapyl.spigotutils.module.record;

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
        final Record record = Record.getReplay(player);
        final Action action = ev.getAction();

        if (record == null || (action != Action.LEFT_CLICK_AIR && action != Action.LEFT_CLICK_BLOCK)) {
            return;
        }

        record
                .getDataAtCurrentFrame()
                .addAction(ev.getHand() == EquipmentSlot.HAND ? ReplayAction.ATTACK_MAIN_HAND : ReplayAction.ATTACK_OFF_HAND);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleEntityDamageEvent(EntityDamageEvent ev) {
        if (!(ev.getEntity() instanceof Player player)) {
            return;
        }

        final Record record = Record.getReplay(player);
        if (record == null) {
            return;
        }

        record.getDataAtCurrentFrame().addAction(ReplayAction.TAKE_DAMAGE);
    }

}
