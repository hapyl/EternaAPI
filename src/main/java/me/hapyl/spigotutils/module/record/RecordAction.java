package me.hapyl.spigotutils.module.record;

import me.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import me.hapyl.spigotutils.module.reflect.npc.NPCAnimation;
import me.hapyl.spigotutils.module.reflect.npc.NPCPose;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * All the available actions that the {@link Record} stored.
 * <br>
 * Plugins can add custom actions and implement them using {@link Record#fetchRecordAction(Player, RecordAction)}
 */
public interface RecordAction {

    RecordAction SNEAK = (RecordPoseAction) npc -> npc.setPose(NPCPose.CROUCHING);
    RecordAction SLEEP = (RecordPoseAction) npc -> npc.setPose(NPCPose.SLEEPING);
    RecordAction SWIM = (RecordPoseAction) npc -> npc.setPose(NPCPose.SWIMMING);

    RecordAction TAKE_DAMAGE = npc -> npc.playAnimation(NPCAnimation.TAKE_DAMAGE);

    RecordAction ATTACK = npc -> npc.playAnimation(NPCAnimation.SWING_MAIN_HAND);
    RecordAction ATTACK_OFFHAND = npc -> npc.playAnimation(NPCAnimation.SWING_OFF_HAND);

    void use(@Nonnull HumanNPC npc);

    /**
     * Annotates that this action is a pose change action.
     * <br>
     * If there are no pose actions at the current frame, the pose will be reset to {@link NPCPose#STANDING}.
     */
    interface RecordPoseAction extends RecordAction {
    }

}
