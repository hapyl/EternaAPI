package test;

import kz.hapyl.spigotutils.EternaPlugin;
import kz.hapyl.spigotutils.module.parkour.Parkour;
import kz.hapyl.spigotutils.module.parkour.Position;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class Test {

    public void test() {
        Commands.createCommands();

        final World world = Bukkit.getWorlds().get(0);
        final Parkour parkour = new Parkour(
                "Lobby Parkour",
                new Position(world, 9, 65, -5, -45.0f, 0.0f),
                new Position(world, -2, 65, 8)
        );

        parkour.addCheckpoint(world, 9, 67, 5, 35.0f, 0.0f);
        parkour.addCheckpoint(world, 1, 67, 11, 55.0f, 0.0f);
        parkour.addCheckpoint(world, -4, 68, 8, -90.0f, 65.0f);

        EternaPlugin.getPlugin().getParkourManager().registerParkour(parkour);
    }

}
