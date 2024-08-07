package me.hapyl.eterna.module.reflect.npc.entry;

import me.hapyl.eterna.module.reflect.npc.HumanNPC;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class SoundEntry extends NPCEntry {

	private final Sound sound;
	private final float pitch;
	private final SoundLocation location;

	public SoundEntry(Sound sound, float pitch, int delay) {
		this(sound, pitch, SoundLocation.NPC, delay);
	}

	public SoundEntry(Sound sound, float pitch, SoundLocation location, int delay) {
		super(delay);
		this.sound = sound;
		this.pitch = pitch;
		this.location = location;
	}


	public Sound getSound() {
		return sound;
	}

	public float getPitch() {
		return pitch;
	}

	public SoundLocation getLocation() {
		return location;
	}

	@Override
	public void invokeEntry(HumanNPC npc, Player player) {
		player.playSound(this.getLocation() == SoundLocation.NPC ? npc.getLocation() : player.getLocation(),
				this.getSound(),
				SoundCategory.MASTER,
				10, //
				this.getPitch());
	}

	public enum SoundLocation {
		SELF,
		NPC
	}

}
