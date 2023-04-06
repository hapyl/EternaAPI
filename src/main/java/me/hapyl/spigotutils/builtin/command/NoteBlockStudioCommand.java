package me.hapyl.spigotutils.builtin.command;

import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.annotate.BuiltIn;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.chat.LazyClickEvent;
import me.hapyl.spigotutils.module.chat.LazyHoverEvent;
import me.hapyl.spigotutils.module.command.SimpleAdminCommand;
import me.hapyl.spigotutils.module.nbs.Parser;
import me.hapyl.spigotutils.module.player.song.Song;
import me.hapyl.spigotutils.module.player.song.SongPlayer;
import me.hapyl.spigotutils.module.player.song.SongQueue;
import me.hapyl.spigotutils.module.player.song.SongStorage;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.io.File;
import java.util.*;

/**
 * Built in command for playing nbs files.
 */
@BuiltIn
public final class NoteBlockStudioCommand extends SimpleAdminCommand {

    private final SongPlayer radio;

    public NoteBlockStudioCommand(String str) {
        super(str);
        radio = EternaPlugin.getPlugin().getSongPlayer();
        setAliases("radio");
        setDescription(
                "Allows admins to play \".nbs\" format song! Only one instance of radio may exists per server. Once song played once it will be cached into memory."
        );
        setUsage("nbs play/stop/pause/list/info/add/skip [SongPath/CachedSongName]");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        final Player player = (Player) sender;
        if (args.length >= 1) {
            final String argument0 = args[0].toLowerCase(Locale.ROOT);
            switch (argument0) {
                case "info" -> {
                    if (this.validateHasSong(player)) {
                        return;
                    }

                    final Song song = radio.getCurrentSong();
                    radio.sendMessage(
                            player,
                            "Currently playing \"%s\" by %s (%s). (%s frames long)",
                            song.getName(),
                            song.getOriginalAuthor(),
                            song.getAuthor(),
                            song.getLength()
                    );

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

                // queue related commands
                case "queue" -> {
                    final SongQueue queue = radio.getQueue();
                    final Queue<Song> list = queue.getQueue();
                    if (list.isEmpty()) {
                        radio.sendMessage(player, "No songs in the queue.");
                    }
                    else {
                        final StringBuilder builder = new StringBuilder();
                        int i = 0;
                        for (final Song song : list) {
                            builder.append("%s".formatted(song.getName()));
                            ++i;
                            if (i < list.size()) {
                                builder.append(", ");
                            }
                        }
                        radio.sendMessage(player, "&aCurrent Queue:");
                        radio.sendMessage(player, builder.toString());
                    }
                }

                case "add" -> {
                    // radio add (...)
                    final StringBuilder builder = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        builder.append(args[i]).append(" ");
                    }
                    final String name = builder.toString().trim();
                    final Song song = findSong(name);

                    if (song == null) {
                        radio.sendMessage(player, "&cCouldn't find song named \"%s\"!", name);
                        return;
                    }

                    radio.getQueue().addSong(song);
                    radio.sendMessage(player, "&aAdded \"%s\" to the queue!", song.getName());
                }

                case "skip" -> {
                    if (validateHasSong(player)) {
                        return;
                    }

                    radio.getQueue().skip();
                }
            }

            if (args.length >= 2) {
                if (argument0.equalsIgnoreCase("play")) {
                    final StringBuilder builder = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        builder.append(args[i]).append(" ");
                    }

                    final Song song = findSong(builder.toString());
                    if (song == null) {
                        radio.sendMessage(player, "&cCouldn't find a nbs file named \"%s\"!", builder.toString());
                        return;
                    }

                    this.startPlayingSong(song);

                }
            }
            return;
        }

        this.sendInvalidUsageMessage(player);
    }

    @Nullable
    private Song findSong(String name) {
        // Add extension if not added
        name = name.trim();
        name = name.endsWith(".nbs") ? name : name + ".nbs";
        final String nameWithoutNbs = name.replace(".nbs", "");

        // find song in cache
        if (SongStorage.alreadyParsed(nameWithoutNbs)) {
            return SongStorage.getSong(nameWithoutNbs);
        }
        // else try and find the file
        else {
            File file = new File(EternaPlugin.getPlugin().getDataFolder() + "/songs", name);

            // if exact name does not find the file then try and find similar one
            if (!file.exists()) {
                final File parent = file.getParentFile();
                if (parent == null || parent.listFiles() == null) {
                    return null;
                }

                final File[] files = parent.listFiles();
                if (files == null) {
                    return null;
                }

                for (final File otherFile : files) {
                    if (otherFile.getName().toLowerCase(Locale.ROOT).contains(nameWithoutNbs)) {
                        file = otherFile;
                        break;
                    }
                }
            }

            if (!file.exists()) {
                return null;
            }

            final Parser parses = new Parser(file);
            parses.parse();
            final Song song = parses.getSong();

            if (song == null) {
                return null;
            }

            SongStorage.addSong(nameWithoutNbs, song);
            return song;

        }
    }

    private void displayListOfCachedSongs(Player player) {
        if (SongStorage.getNames().isEmpty()) {
            radio.sendMessage(
                    player,
                    "&cSong Storage does not have any stored songs yet! Play it once to save it into memory."
            );
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
                // for what reason it's called run_command, but it does not call command but instead sends a chat message
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
            return completerSort(Arrays.asList("play", "stop", "list", "pause", "info", "queue", "add", "skip"), args);
        }

        else if (args.length >= 2 && args[0].equalsIgnoreCase("play")) {
            return completerSort(new ArrayList<>(SongStorage.getNames()), args);
        }

        return null;
    }

}