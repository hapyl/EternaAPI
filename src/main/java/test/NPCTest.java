package test;

import kz.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class NPCTest {

    public static HumanNPC npc;

    public static void create(Player player, String name) {

        if (npc != null) {
            npc.remove();
            player.sendMessage(ChatColor.GREEN + "Removed npc.");
            npc = null;
            return;
        }

        npc = new HumanNPC(player.getLocation(), "", name);
        npc.show(player);
        npc.setCollision(false);

        player.sendMessage(ChatColor.GREEN + "Created npc.");
    }

}
