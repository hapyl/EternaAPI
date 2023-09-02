package test;

import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.reflect.npc.FlippedHumanNPC;
import me.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@RuntimeStaticTest
final class NPCTest {

    static HumanNPC npc;

    private NPCTest() {
    }

    static void test(Player player, String name) {
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

        if (name.equalsIgnoreCase("sit")) {
            if (npc.isSitting()) {
                npc.setSitting(false);
                Chat.sendMessage(player, "&cNot sitting.");
            }
            else {
                npc.setSitting(true);
                Chat.sendMessage(player, "&aSitting.");
            }
        }

        // empty
        if (npc != null) {
            return;
        }

        npc = new HumanNPC(player.getLocation(), player.getName(), player.getName());
        npc.show(player);

        player.sendMessage(ChatColor.GREEN + "Created npc.");
    }

}
