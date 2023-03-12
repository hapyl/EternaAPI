package test;

import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import me.hapyl.spigotutils.module.reflect.npc.SkinPart;
import me.hapyl.spigotutils.module.util.Runnables;
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

        npc = HumanNPC.create(player.getLocation(), name, player.getName()).handle();
        npc.show(player);

        int k = 0;
        for (SkinPart value : SkinPart.values()) {
            Runnables.runLater(() -> {
                npc.updateSkin(value);
                Chat.sendMessage(player, "Set skin part to %s", value.name());
            }, 30L * k);
            k++;
        }

        player.sendMessage(ChatColor.GREEN + "Created npc.");
    }

}
