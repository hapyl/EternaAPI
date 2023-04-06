package test;

import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class NPCTest {

    public static HumanNPC npc;

    public static void create(Player player, String name) {
        if (name.equalsIgnoreCase("push")) {
            final Vector vector = player.getLocation().getDirection().normalize().multiply(2.0d);
            npc.push(vector);

            Chat.sendMessage(player, "Pushed");
            return;
        }

        if (name.equalsIgnoreCase("remove")) {
            npc.remove();
            npc = null;
            Chat.sendMessage(player, "Removed");
            return;
        }

        // empty
        if (npc != null) {
            npc.jump(2);

            Chat.sendMessage(player, "jump");
            return;
        }

        npc = HumanNPC.create(player.getLocation(), name, player.getName()).handle();
        npc.show(player);

        player.sendMessage(ChatColor.GREEN + "Created npc.");
    }

}
