package me.hapyl.spigotutils.module.ai.goal;

import me.hapyl.spigotutils.module.ai.AI;
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

public class MoveToGoal extends Goal {
    public MoveToGoal(AI ai, Location location, double speedModifier, int range, int heightRange) {
        super(null);

        goal = new PathfinderGoalGotoTarget(ai.getMob(EntityCreature.class), speedModifier, range, heightRange) {
            @Override
            protected boolean a(IWorldReader iWorldReader, BlockPosition blockPosition) {
                if (iWorldReader instanceof WorldServer worldServer) {
                    final World world = Bukkit.getWorld(worldServer.J.g());
                    if (world == null) {
                        System.out.println(worldServer.J.g());
                        return false;
                    }

                    return isValidTarget(world, world.getBlockAt(blockPosition.u(), blockPosition.v(), blockPosition.w()));
                }

                System.out.println("not worldServer");
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
