package test;

import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import me.hapyl.spigotutils.module.reflect.npc.NPCPose;
import org.bukkit.entity.Player;

public class NPCPoseTest {

    private static HumanNPC npc;

    public static void work(Player player, String[] args) {
        if (args.length == 0) {
            if (npc == null) {
                npc = new HumanNPC(player.getLocation(), "Test", player.getName());
                npc.show(player);
                Chat.sendMessage(player, "&aCreated NPC!");
            }
            else {
                npc.remove();
                npc = null;
                Chat.sendMessage(player, "&cRemoved NPC!");
            }
            return;
        }

        final NPCPose newPose = NPCPose.valueOf(args[0]);
        npc.setPose(newPose);
        Chat.sendMessage(player, "&aSet pose to %s!", newPose.name());
    }

}
