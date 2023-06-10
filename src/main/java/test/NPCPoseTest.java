package test;

import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import me.hapyl.spigotutils.module.reflect.npc.NPCPose;
import org.bukkit.entity.Player;

@RuntimeStaticTest
final class NPCPoseTest {

    private NPCPoseTest() {
    }

    static HumanNPC npc;

    static void test(Player player, String[] args) {
        if (args.length == 0) {
            if (npc == null) {
                npc = new HumanNPC(player.getLocation(), "test.Test", player.getName());
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
