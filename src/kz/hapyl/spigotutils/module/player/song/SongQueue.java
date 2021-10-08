package kz.hapyl.spigotutils.module.player.song;

import java.util.Queue;

public interface SongQueue {

	Song current();

	Song getNext();

	boolean hasNext();

	void playNext();

	void skip();

	void addSong(Song song);

	void removeSong();

	Queue<Song> getQueue();


}
