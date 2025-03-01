package me.hapyl.eterna.module.record;

import com.google.common.collect.Sets;
import me.hapyl.eterna.module.inventory.Equipment;
import me.hapyl.eterna.module.reflect.npc.HumanNPC;
import me.hapyl.eterna.module.reflect.npc.NPCPose;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Represents a {@link ReplayData} at the given frame.
 */
public class ReplayData {

    private final Record record;
    private final Player player;
    private final Location location;
    private final Set<RecordAction> actions;
    private final Equipment equipment;

    public ReplayData(@Nonnull Record record) {
        this.record = record;
        this.player = record.getPlayer();
        this.location = player.getLocation();
        this.equipment = Equipment.of(player);
        this.actions = Sets.newHashSet();

        addActionIf(player.isSneaking(), RecordAction.SNEAK);
        addActionIf(player.isSleeping(), RecordAction.SLEEP);
        addActionIf(player.isSwimming(), RecordAction.SWIM);
    }

    @Nonnull
    public Player getPlayer() {
        return player;
    }

    @Nonnull
    public Location getLocation() {
        return location;
    }

    public void addAction(@Nonnull RecordAction action) {
        if (record.isIgnoredAction(action)) {
            return;
        }

        actions.add(action);
    }

    public final void use(@Nonnull HumanNPC human) {
        // Location
        human.setLocation(location);

        // Equipment
        human.setEquipment(equipment);

        boolean hasAnyPose = false;

        for (RecordAction action : actions) {
            action.use(human);

            if (action instanceof RecordAction.RecordPoseAction) {
                hasAnyPose = true;
            }
        }

        if (!hasAnyPose) {
            human.setPose(NPCPose.STANDING);
        }
    }

    private void addActionIf(boolean condition, RecordAction action) {
        if (condition) {
            addAction(action);
        }
    }

}
