package kz.hapyl.spigotutils;

import kz.hapyl.spigotutils.module.api.EternaAPI;
import kz.hapyl.spigotutils.module.player.song.SongPlayer;

public class Eterna implements EternaAPI {

	@Override
	public SongPlayer getSongPlayer() {
		return SpigotUtilsPlugin.getPlugin().getSongPlayer();
	}


}
