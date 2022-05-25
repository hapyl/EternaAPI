package kz.hapyl.spigotutils.module.record;

import com.google.common.collect.Sets;
import kz.hapyl.spigotutils.module.reflect.npc.Human;
import kz.hapyl.spigotutils.module.util.Nulls;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;

import java.util.Set;

/**
 * Represents data of certain tick of the replay.
 */
public class Data {

    private final Player player;
    private final Location location;
    private final Set<ReplayAction> actions;
    private final EntityEquipment equipment;

    public Data(Player player) {
        this.player = player;
        this.location = player.getLocation();
        this.equipment = player.getEquipment();
        this.actions = Sets.newHashSet();

        addActionIf(player.isSneaking(), ReplayAction.SNEAKING);
        addActionIf(player.isSleeping(), ReplayAction.SLEEPING);
        addActionIf(player.isSwimming(), ReplayAction.SWIMMING);
        addActionIf(player.getFireTicks() > 0, ReplayAction.ON_FIRE);
    }

    public Location getLocation() {
        return location;
    }

    public void addActionIf(boolean condition, ReplayAction action) {
        if (condition) {
            addAction(action);
        }
    }

    public void addAction(ReplayAction action) {
        actions.add(action);
    }

    public final void applyToNpc(Human human) {
        // teleport
        human.setLocation(location);

        // apply equipment
        human.setEquipment(equipment);

        // apply actions
        for (ReplayAction action : ReplayAction.values()) {
            if (actions.contains(action)) {
                action.getAction().accept(human);
            }
            // this resets pose after sneaking, sleeping or swimming
            else {
                Nulls.runIfNotNull(action.getActionOff(), a -> a.accept(human));
            }
        }
    }

    public Player getPlayer() {
        return player;
    }
}
