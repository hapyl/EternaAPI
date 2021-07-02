package kz.hapyl.spigotutils.module.player;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import kz.hapyl.spigotutils.SpigotUtilsPlugin;
import kz.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

public class SoundQueue {

	private final LinkedList<BukkitSound> sounds;
	private final Set<Player> listeners;
	private CachedSoundQueue cachedSounds;
	private boolean playing;

	public SoundQueue() {
		this.sounds = Lists.newLinkedList();
		this.listeners = Sets.newHashSet();
	}

	public SoundQueue append(BukkitSound sound) {
		this.sounds.addLast(sound);
		return this;
	}

	/**
	 * Appends a sound to the queue.
	 *
	 * @param sound - Sound to append.
	 * @param pitch - Pitch of the sound.
	 * @param delay - Delay before playing. Keep in mind that delay is addictive and global.
	 */
	public SoundQueue append(Sound sound, float pitch, int delay) {
		return this.append(new BukkitSound(sound, pitch, delay));
	}

	public SoundQueue appendSameSound(Sound sound, float pitch, int... delays) {
		return this.appendSame(sound, pitch, delays);
	}

	public SoundQueue appendSame(Sound sound, float pitch, int... delays) {
		Validate.isTrue(delays.length != 0, "no delays provided");
		for (final int delay : delays) {
			this.append(new BukkitSound(sound, pitch, delay));
		}
		return this;
	}

	public SoundQueue addListener(Player player) {
		this.listeners.remove(player);
		return this;
	}

	public SoundQueue removeListener(Player player) {
		this.listeners.remove(player);
		return this;
	}

	public boolean isListener(Player player) {
		return this.listeners.contains(player);
	}

	public SoundQueue clearListeners() {
		this.listeners.clear();
		return this;
	}

	public SoundQueue everyoneIsListener() {
		return this.clearListeners();
	}

	public boolean isPlaying() {
		return playing;
	}

	public void play() {
		if (isPlaying()) {
			return;
		}
		this.playing = true;
		this.cacheIfNotCached();
		final Collection<? extends Player> listeners = this.listeners.isEmpty() ? Bukkit.getOnlinePlayers() : this.listeners;
		new BukkitRunnable() {
			private int frame = 0;

			@Override
			public void run() {
				if (!playing || frame++ >= cachedSounds.getMaxFrame() || cachedSounds == null) {
					playing = false;
					this.cancel();
					return;
				}

				for (final BukkitSound sound : cachedSounds.getIfExists(frame)) {
					listeners.forEach(player -> PlayerLib.playSound(player, sound.getSound(), sound.getPitch()));
				}
			}
		}.runTaskTimer(SpigotUtilsPlugin.getPlugin(), 0, 1);
	}

	public void stop() {
		if (!isPlaying()) {
			return;
		}
		this.playing = false;
	}

	private void cacheIfNotCached() {
		if (this.cachedSounds != null) {
			return;
		}
		this.cachedSounds = new CachedSoundQueue(this.sounds);
	}


}
