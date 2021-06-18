package kz.hapyl.spigotutils.module.player.song;

public interface SongQueue {

	Song current();

	Song getNext();

	boolean hasNext();

	void playNext();

	void skip();

	void addSong(Song song);

	void removeSong();


}
