package test;

import com.google.common.collect.Maps;
import me.hapyl.spigotutils.module.util.Compute;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

@RuntimeStaticTest
final class ComputeTest {

    static void test(Player player) {
        Map<String, Integer> intMap = Maps.newHashMap();

        intMap.compute("hapyl", Compute.intAdd(69));
        intMap.compute("didenpro", Compute.intAdd(420));

        player.sendMessage("intMap:");
        intMap.forEach((str, i) -> {
            player.sendMessage("%s=%s".formatted(str, i));
        });

        Map<String, List<Material>> listMap = Maps.newHashMap();

        listMap.compute("stone", Compute.listAdd(Material.STONE, Material.COBBLESTONE, Material.DEEPSLATE));
        listMap.compute("dirt", Compute.listAdd(Material.GRASS_BLOCK, Material.DIRT, Material.DIRT_PATH, Material.PODZOL));
        listMap.compute("dirt", Compute.listRemove(Material.DIRT_PATH));

        player.sendMessage("listMap:");
        listMap.forEach((str, list) -> {
            player.sendMessage("%s=%s".formatted(str, list));
        });
    }
}
