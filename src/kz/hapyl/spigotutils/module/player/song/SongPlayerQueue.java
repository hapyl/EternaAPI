package kz.hapyl.spigotutils.module.player.song;

import kz.hapyl.spigotutils.SpigotUtilsPlugin;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;

import java.util.LinkedList;
import java.util.Queue;

public class SongPlayerQueue implements SongQueue {

	private final Queue<Song> queue;
	private Song current;

	public SongPlayerQueue() {
		this.queue = new LinkedList<>();
	}

	@Override
	@Nullable
	public Song current() {
		return current;
	}

	@Override
	public boolean hasNext() {
		return this.queue.peek() != null;
	}

	@Override
	public Song getNext() {
		return this.queue.peek();
	}

	@Override
	public void playNext() {
		if (this.hasNext()) {
			this.current = this.getNext();
			SpigotUtilsPlugin.getPlugin().getSongPlayer().setCurrentSong(this.current);
		}
	}

	@Override
	public void skip() {
		this.playNext();
	}

	@Override
	public void addSong(Song song) {
		this.queue.add(song);
	}

	@Override
	public void removeSong() {
		this.queue.remove();
	}
}
