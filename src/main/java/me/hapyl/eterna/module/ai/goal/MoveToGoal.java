package me.hapyl.eterna.module.ai.goal;

import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.ai.AI;
import me.hapyl.eterna.module.reflect.Reflect;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import org.apache.commons.lang.reflect.FieldUtils;
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

        goal = new MoveToBlockGoal(ai.getMob(PathfinderMob.class), speedModifier, range, heightRange) {

            @Override
            protected boolean isValidTarget(LevelReader levelReader, BlockPos blockPos) {
                if (levelReader instanceof ServerLevel worldServer) {
                    final World world = Reflect.getBukkitWorld(worldServer);

                    if (world == null) {
                        return false;
                    }

                    return MoveToGoal.this.isValidTarget(world, world.getBlockAt(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
                }

                return false;
            }
        };

        try {
            FieldUtils.writeField(
                    goal,
                    "blockPos",
                    new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ()),
                    true
            );
        } catch (IllegalAccessException e) {
            EternaLogger.exception(e);
        }
    }

    public boolean isValidTarget(World world, Block block) {
        return true;
    }
}
