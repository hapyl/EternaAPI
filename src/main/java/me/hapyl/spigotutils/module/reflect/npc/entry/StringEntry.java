package me.hapyl.spigotutils.module.reflect.npc.entry;

import me.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class StringEntry extends NPCEntry {

	private final String stringEntry;

	public StringEntry(String stringEntry, int delay) {
		super(delay);
		this.stringEntry = stringEntry;
	}

	@Override
	public void invokeEntry(HumanNPC npc, Player player) {
		npc.sendNpcMessage(player, this.stringEntry);
		player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, SoundCategory.RECORDS, 2, 1.0f);
	}

}
