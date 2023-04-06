package me.hapyl.spigotutils.module.reflect.npc.entry;

import me.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class StringSoundEntry extends StringEntry {

	private final Sound sound;
	private final float pitch;

	public StringSoundEntry(String stringEntry, Sound sound, float pitch, int delay) {
		super(stringEntry, delay);
		this.sound = sound;
		this.pitch = pitch;
	}

	@Override
	public void invokeEntry(HumanNPC npc, Player player) {
		super.invokeEntry(npc, player);
		player.playSound(npc.getLocation(), this.sound, SoundCategory.RECORDS, 10, this.pitch);
	}
}
