package me.hapyl.spigotutils.module.ai.goal;

import com.comphenix.protocol.wrappers.BlockPosition;
import me.hapyl.spigotutils.module.ai.AI;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class MoveToGoal extends Goal {
    public MoveToGoal(AI ai, Location location, double speedModifier, int range, int heightRange) {
        super(null);

        goal = new MoveToBlockGoal(ai.getMob(PathfinderMob.class), speedModifier, range, heightRange) {
            @Override
            protected boolean isValidTarget(LevelReader levelReader, BlockPos blockPos) {
                if (levelReader instanceof ServerLevel worldServer) {
                    final String levelName = worldServer.J.getLevelName();
                    final World world = Bukkit.getWorld(levelName);

                    if (world == null) {
                        return false;
                    }

                    return MoveToGoal.this.isValidTarget(world, world.getBlockAt(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
                }

                return false;
            }
        };

        // will throw error

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
