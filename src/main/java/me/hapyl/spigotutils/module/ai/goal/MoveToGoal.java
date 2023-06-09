package me.hapyl.spigotutils.module.ai.goal;

import me.hapyl.spigotutils.module.ai.AI;
import me.hapyl.spigotutils.module.reflect.Reflect;
import net.minecraft.core.BlockPosition;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.ai.goal.PathfinderGoalGotoTarget;
import net.minecraft.world.level.IWorldReader;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * Adds a goal to move to a location.
 */
public class MoveToGoal extends Goal {

    /**
     * Adds a goal to move to a location.
     *
     * @param ai            - AI reference.
     * @param location      - Location to move to.
     * @param speedModifier - Speed modifier.
     * @param range         - Range.
     * @param heightRange   - Height range.
     */
    public MoveToGoal(AI ai, Location location, double speedModifier, int range, int heightRange) {
        super(null);

        goal = new PathfinderGoalGotoTarget(ai.getMob(EntityCreature.class), speedModifier, range, heightRange) {

            @Override
            protected boolean a(IWorldReader iWorldReader, BlockPosition blockPosition) {
                if (iWorldReader instanceof WorldServer worldServer) {
                    final World world = Reflect.getBukkitWorld(worldServer);

                    if (world == null) {
                        return false;
                    }

                    return isValidTarget(world, world.getBlockAt(blockPosition.u(), blockPosition.v(), blockPosition.w()));
                }

                return false;
            }
        };

        try {
            FieldUtils.writeField(
                    goal,
                    "e",
                    new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()),
                    true
            );
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public boolean isValidTarget(World world, Block block) {
        return true;
    }
}
