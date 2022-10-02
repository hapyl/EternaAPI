package test;

import me.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class NPCTest {

    public static HumanNPC npc;
    public static HumanNPC npcNoCol;

    public static void create(Player player, String name) {
        if (npc != null) {
            npc.remove();
            npcNoCol.remove();
            npc = null;
            npcNoCol = null;
            player.sendMessage(ChatColor.GREEN + "Removed npc.");
            return;
        }

        npc = new HumanNPC(player.getLocation(), "Collision", name);
        npc.show(player);

        npcNoCol = new HumanNPC(player.getLocation().add(3.0d, 0.0d, 0.0d), "&aNo Collision", name);
        npcNoCol.setCollision(false);
        npcNoCol.show(player);

        player.sendMessage(ChatColor.GREEN + "Created npc.");

        final Team npcTeam = npc.getTeamOrCreate(player.getScoreboard());
        final Team npc2Team = npcNoCol.getTeamOrCreate(player.getScoreboard());
        player.sendMessage("NPC1-" + npcTeam.getOption(Team.Option.COLLISION_RULE));
        player.sendMessage("NPC2-" + npc2Team.getOption(Team.Option.COLLISION_RULE));
    }

}
