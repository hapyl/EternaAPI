package test;

import kz.hapyl.spigotutils.module.quest.Quest;
import kz.hapyl.spigotutils.module.quest.objectives.BreakBlocks;
import kz.hapyl.spigotutils.module.quest.objectives.FinishDialogue;
import kz.hapyl.spigotutils.module.quest.objectives.TalkToNpc;
import kz.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import kz.hapyl.spigotutils.module.util.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class QuestAndEntries {

	private final Quest quest;
	private final HumanNPC npc;

	public QuestAndEntries() throws IllegalAccessException {
		throw new IllegalAccessException();
		//this.npc = new HumanNPC(BukkitUtils.getSpawnLocation(), "Who even is this?", "Minikloon");
		//this.quest = new Quest("Test Quest");
		//this.quest.addStage(new BreakBlocks(Material.SPONGE, 13));
		//this.quest.addStage(new TalkToNpc(npc, 2));
		//this.quest.addStage(new FinishDialogue(npc));
		//npc.showAll();
		//npc.addDialogLine("Hello {PLAYER} hello hello who are you my name is {NAME}");
		//npc.addDialogLine("You look cool me looking cool too!");
		//npc.addDialogLine("well that's it bye.");
		//for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
		//	this.quest.startQuest(onlinePlayer);
		//}
	}

}
