package kz.hapyl.spigotutils.builtin.command;

import kz.hapyl.spigotutils.SpigotUtilsPlugin;
import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.chat.LazyClickEvent;
import kz.hapyl.spigotutils.module.chat.LazyHoverEvent;
import kz.hapyl.spigotutils.module.command.SimpleAdminCommand;
import kz.hapyl.spigotutils.module.nbs.Parser;
import kz.hapyl.spigotutils.module.player.song.Song;
import kz.hapyl.spigotutils.module.player.song.SongPlayer;
import kz.hapyl.spigotutils.module.player.song.SongStorage;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class NoteBlockStudioCommand extends SimpleAdminCommand {

	private final SongPlayer radio;

	public NoteBlockStudioCommand(String str) {
		super(str);
		this.radio = SpigotUtilsPlugin.getPlugin().getSongPlayer();
		this.setAliases("radio");
		this.setDescription("Allows admins to play \".nbs\" format song! Only one instance of radio may exists per server. Once song played once it " +
				"will be cached into memory.");
		this.setUsage("nbs play/stop/pause/list/info (SongPath/CachedSongName)");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		final Player player = (Player)sender;
		if (args.length >= 1) {
			final String argument0 = args[0].toLowerCase(Locale.ROOT);
			switch (argument0) {
				case "info" -> {
					if (this.validateHasSong(player)) {
						return;
					}

					final Song song = radio.getCurrentSong();
					radio.sendMessage(player, "Currently playing \"%s\" by %s (%s). (%s frames long)", song.getName(), song.getOriginalAuthor(),
							song.getAuthor(), song.getLength());

				}
				case "stop" -> {
					if (this.validateHasSong(player)) {
						return;
					}

					if (radio.isPlaying()) {
						radio.stopPlaying();
					}

				}
				case "pause" -> {
					if (this.validateHasSong(player)) {
						return;
					}

					radio.pausePlaying();
				}
				case "list" -> {
					this.displayListOfCachedSongs(player);
				}
			}

			if (args.length >= 2) {
				if (argument0.equalsIgnoreCase("play")) {

					final StringBuilder builder = new StringBuilder();
					for (int i = 1; i < args.length; i++) {
						builder.append(args[i]).append(" ");
					}

					// Add extension if not added
					String name = builder.toString().trim();
					name = name.endsWith(".nbs") ? name : name + ".nbs";
					final String nameWithoutNbs = name.replace(".nbs", "");

					radio.sendMessage(player, "&eLooking for \"%s\" in cache...", name);

					if (SongStorage.alreadyParsed(nameWithoutNbs)) {
						this.startPlayingSong(SongStorage.getSong(nameWithoutNbs));
					}
					else {
						radio.sendMessage(player, "&eCouldn't find \"%s\" in cache, trying to parse...", name);
						final File file = new File(SpigotUtilsPlugin.getPlugin().getDataFolder() + "/songs", name);

						if (!file.exists()) {
							radio.sendMessage(player, "&cCouldn't find a nbs file named \"%s\"!", name);
							return;
						}

						final Parser parses = new Parser(file);
						parses.parse();
						final Song song = parses.getSong();

						if (song == null) {
							radio.sendMessage(player, "&cAn error occurred whilst trying to parse %s!", name);
							return;
						}

						radio.sendMessage(player, "&7Caching \"%s\", expect some lag...", nameWithoutNbs);
						SongStorage.addSong(nameWithoutNbs, song);
						this.startPlayingSong(song);

					}

				}
			}
			return;
		}

		this.sendInvalidUsageMessage(player);

	}

	private void displayListOfCachedSongs(Player player) {

		if (SongStorage.getNames().isEmpty()) {
			radio.sendMessage(player, "&cSong Storage does not have any stored songs yet! Play it once to save it into memory.");
			return;
		}

		radio.sendMessage(player, "&aThese songs are currently stored! &e(Clickable!)");

		final ComponentBuilder builder = new ComponentBuilder();
		int index = 0;
		for (final String name : SongStorage.getNames()) {
			builder.append(createClickable(name));
			if (index++ != (SongStorage.getNames().size() - 1)) {
				builder.append(ChatColor.GRAY + ", ");
			}
		}

		radio.sendMessage(player, builder.create());

	}

	private BaseComponent[] createClickable(String name) {
		final String coolName = Chat.capitalize(name);
		return new ComponentBuilder(ChatColor.GOLD + coolName).event(LazyHoverEvent.SHOW_TEXT.of("&eClick to play \"" + name + "\"!"))
				// for what reason it's called run_command but it does not call command but instead sends a chat message
				.event(LazyClickEvent.RUN_COMMAND.of("/nbs play " + name))
				.create();
	}

	private boolean validateHasSong(Player player) {
		if (!radio.hasSong()) {
			radio.sendMessage(player, "&cSong Player does not have anything playing right now!");
			return true;
		}
		return false;
	}

	private void startPlayingSong(Song song) {
		radio.setCurrentSong(song);
		radio.everyoneIsListener();
		radio.startPlaying();
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {

		if (args.length == 1) {
			return completerSort(Arrays.asList("play", "stop", "list", "pause", "info"), args);
		}

		else if (args.length >= 2 && args[0].equalsIgnoreCase("play")) {
			return completerSort(new ArrayList<>(SongStorage.getNames()), args);
		}

		return null;

	}

}