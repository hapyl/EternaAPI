package me.hapyl.spigotutils.builtin.command;

import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.annotate.BuiltIn;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.chat.LazyClickEvent;
import me.hapyl.spigotutils.module.chat.LazyHoverEvent;
import me.hapyl.spigotutils.module.command.SimpleAdminCommand;
import me.hapyl.spigotutils.module.player.song.Song;
import me.hapyl.spigotutils.module.player.song.SongPlayer;
import me.hapyl.spigotutils.module.player.song.SongQueue;
import me.hapyl.spigotutils.module.player.song.SongRegistry;
import me.hapyl.spigotutils.module.util.Validate;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;
import java.util.Queue;

/**
 * Built in command for playing nbs files.
 */
@BuiltIn
public final class NoteBlockStudioCommand extends SimpleAdminCommand {

    private final SongPlayer radio;

    public NoteBlockStudioCommand(String str) {
        super(str);
        radio = EternaPlugin.getPlugin().getSongPlayer();
        radio.everyoneIsListener();

        setAliases("radio");
        setDescription(
                "Allows admins to play \".nbs\" format song! Only one instance of radio may exists per server using this command.");

        addCompleterValues(1, "play", "stop", "list", "pause", "info", "repeat", "queue", "add", "skip", "reload");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        final Player player = (Player) sender;
        final SongRegistry registry = getRegistry();

        if (registry.isLock()) {
            radio.sendMessage(sender, "&cCannot use while parsing songs, please wait!");
            return;
        }

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
                    this.displayListOfCachedSongs(player, args.length == 1 ? 1 : Validate.getInt(args[1]));
                }

                case "repeat" -> {
                    radio.setOnRepeat(!radio.isOnRepeat());

                    radio.sendMessage("The player %s on repeat.", radio.isOnRepeat() ? "now" : "no longer");
                }

                case "reload" -> {
                    radio.stopPlaying();
                    radio.sendMessage(player, "Reloading, might take a while...");

                    registry.reload(i -> {
                        radio.sendMessage(player, "Successfully loaded %s songs!", i);
                    });
                }

                // queue related commands
                case "queue" -> {
                    final SongQueue queue = radio.getQueue();
                    final Queue<Song> list = queue.getQueue();
                    if (list.isEmpty()) {
                        radio.sendMessage(player, "No songs in the queue.");
                    }
                    else {
                        radio.sendMessage(player, "&aCurrent Queue:");

                        int index = 0;
                        for (Song song : list) {
                            radio.sendMessage(
                                    player,
                                    " #%s: %s - %s",
                                    index + 1,
                                    song.getOriginalAuthor(),
                                    song.getName()
                            );

                            if (++index >= 10) {
                                radio.sendMessage(player, "And %s more!", list.size() - index);
                                break;
                            }
                        }
                    }
                }

                case "add" -> {
                    // radio add (...)
                    final String query = buildSongName(args);
                    final Song song = registry.byName(query);

                    if (song == null) {
                        radio.sendMessage(player, "&cCouldn't find song named \"%s\"!", query);
                        return;
                    }

                    radio.getQueue().addSong(song);
                    radio.sendMessage(player, "&aAdded \"%s\" to the queue!", song.getName());

                    // Start playing if nothing is already playing
                    if (!radio.isPlaying()) {
                        radio.everyoneIsListener();
                        radio.getQueue().playNext();
                    }
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
                    final String query = buildSongName(args);

                    if (registry.isEmpty()) {
                        radio.sendMessage("&cThere are no songs!");
                        return;
                    }

                    final Song song = registry.byName(query);

                    if (song == null) {
                        radio.sendMessage(player, "&cCould not find song named \"%s\"!", query);
                        return;
                    }

                    this.startPlayingSong(song);
                }
            }
            return;
        }

        this.sendInvalidUsageMessage(player);
    }

    private String buildSongName(String[] args) {
        final StringBuilder builder = new StringBuilder();

        for (int i = 1; i < args.length; i++) {
            if (i != 1) {
                builder.append(" ");
            }
            builder.append(args[i]);
        }

        return builder.toString();
    }

    private void displayListOfCachedSongs(Player player, int page) {
        final SongRegistry registry = getRegistry();

        if (!registry.anySongs()) {
            radio.sendMessage(
                    player,
                    "&cThere are no songs loaded, add them into 'plugins/%s/songs' folder and run 'nbs reload'!".formatted(
                            EternaPlugin.getPlugin().getName())
            );
            return;
        }

        radio.sendMessage(player, "&aListing songs: (Page %s/%s) &e&l&nClickable!", page, registry.maxPage());

        final List<String> names = registry.listNames(page);
        final ComponentBuilder builder = new ComponentBuilder();
        int index = 0;

        for (final String name : names) {
            if (index++ != 0) {
                builder.append(ChatColor.GRAY + ", ");
            }

            builder.append(createClickable(name));
        }

        radio.sendMessage(player, builder.create());
    }

    private BaseComponent[] createClickable(String name) {
        final String coolName = Chat.capitalize(name);
        return new ComponentBuilder(ChatColor.GOLD + coolName).event(LazyHoverEvent.SHOW_TEXT.of(
                        "&eClick to play \"" + name + "\"!"))
                // for what reason it's called run_command, but it does not call command but instead sends a chat message
                .event(LazyClickEvent.RUN_COMMAND.of("/nbs play " + name)).create();
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
        if (args.length >= 2) {
            final String arg0 = args[0];
            if (arg0.equals("play") || arg0.equals("add")) {
                return completerSort(getRegistry().listNames(), args);
            }
        }

        return null;
    }

    private SongRegistry getRegistry() {
        return EternaPlugin.getPlugin().getRegistry().songRegistry;
    }

}