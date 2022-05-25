package kz.hapyl.spigotutils.module.record;

import kz.hapyl.spigotutils.module.reflect.npc.Human;
import kz.hapyl.spigotutils.module.reflect.npc.NPCAnimation;
import kz.hapyl.spigotutils.module.reflect.npc.NPCPose;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public enum ReplayAction {
    SNEAKING(human -> human.setPose(NPCPose.CROUCHING), human -> human.setPose(NPCPose.STANDING)),
    SLEEPING(human -> human.setPose(NPCPose.SLEEPING), human -> human.setPose(NPCPose.STANDING)),
    SWIMMING(human -> human.setPose(NPCPose.SWIMMING), human -> human.setPose(NPCPose.STANDING)),
    ON_FIRE(human -> human.bukkitEntity().setFireTicks(2)),
    TAKE_DAMAGE(human -> human.playAnimation(NPCAnimation.TAKE_DAMAGE)),
    ATTACK_MAIN_HAND(human -> human.playAnimation(NPCAnimation.SWING_MAIN_HAND)),
    ATTACK_OFF_HAND(human -> human.playAnimation(NPCAnimation.SWING_OFF_HAND));

    private final Consumer<Human> action;
    private final Consumer<Human> actionOff;

    ReplayAction(Consumer<Human> action) {
        this(action, null);
    }

    ReplayAction(Consumer<Human> action, @Nullable Consumer<Human> actionOff) {
        this.action = action;
        this.actionOff = actionOff;
    }

    public Consumer<Human> getActionOff() {
        return actionOff;
    }

    public Consumer<Human> getAction() {
        return action;
    }
}
