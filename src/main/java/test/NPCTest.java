package test;

import me.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class NPCTest {

    public static HumanNPC npc;

    public static void create(Player player, String name) {
        if (npc != null) {
            npc.remove();
            npc = null;
            player.sendMessage(ChatColor.GREEN + "Removed npc.");
            return;
        }

        npc = new HumanNPC(player.getLocation(), "NPC", name);
        npc.show(player);

        player.sendMessage(ChatColor.GREEN + "Created npc.");
    }

}
