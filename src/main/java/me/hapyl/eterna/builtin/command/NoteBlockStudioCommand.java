package me.hapyl.eterna.builtin.command;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.builtin.manager.SongManager;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.command.SimpleAdminCommand;
import me.hapyl.eterna.module.math.Numbers;
import me.hapyl.eterna.module.player.song.Song;
import me.hapyl.eterna.module.player.song.SongPlayer;
import me.hapyl.eterna.module.player.song.SongQueue;
import me.hapyl.eterna.module.util.CollectionUtils;
import me.hapyl.eterna.module.util.NanoBenchmark;
import me.hapyl.eterna.module.util.Wrap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

/**
 * Built in command for playing nbs files.
 */
public final class NoteBlockStudioCommand extends SimpleAdminCommand {
    
    private final SongPlayer radio = SongPlayer.DEFAULT_PLAYER;
    
    public NoteBlockStudioCommand(String str) {
        super(str);
        
        final String[] completeValues = new String[] { "play", "stop", "list", "pause", "info", "queue", "add", "skip", "reload", "clear", "volume" };
        setUsage("/nbs %s [name...]".formatted(CollectionUtils.wrapToString(completeValues, Wrap.of("(", ", ", ")"))));
        
        setAliases("radio");
        setDescription(
                "Allows playing \".nbs\" files to a default song player."
        );
        
        addCompleterValues(1, completeValues);
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        final Player player = (Player) sender;
        final SongManager registry = Eterna.getManagers().song;
        
        if (registry.isLock()) {
            radio.message(sender, "&cCurrently parsing songs, please wait...");
            return;
        }
        
        if (args.length < 1) {
            sendInvalidUsageMessage(player);
            return;
        }
        
        final String argument0 = args[0].toLowerCase(Locale.ROOT);
        
        switch (argument0) {
            case "info" -> {
                final Song song = radio.currentSong();
                
                if (song == null) {
                    sendNoSongMessage(player);
                    return;
                }
                
                radio.message(
                        player,
                        "&aCurrently playing &6%s&a by &6%s. &8(%sf @ %st)".formatted(
                                song.getName(),
                                song.getEitherOriginalAuthorOrAuthorWithAsteriskOrUnknownIfAuthorNotSpecified(),
                                song.getLength(),
                                song.tempo()
                        )
                );
                
            }
            case "stop" -> {
                final Song currentSong = radio.currentSong();
                
                if (currentSong == null) {
                    sendNoSongMessage(player);
                    return;
                }
                
                radio.stopPlaying();
                radio.message(player, "&aStopped &6%s&a.".formatted(currentSong.getName()));
            }
            
            case "pause" -> {
                if (!radio.hasSong()) {
                    sendNoSongMessage(player);
                    return;
                }
                
                radio.pausePlaying();
            }
            
            case "list" -> {
                displayListOfCachedSongs(player, args.length == 1 ? 1 : Numbers.getInt(args[1]));
            }
            
            case "reload" -> {
                radio.stopPlaying();
                radio.message(player, "&eReloading, might take a while...");
                
                final NanoBenchmark benchmark = NanoBenchmark.ofNow();
                
                registry.reload().thenAccept(count -> {
                    benchmark.step("load");
                    
                    radio.message(player, "&aSuccessfully loaded &6%s&a songs in &b%sms&a!".formatted(count, benchmark.getFirstResult().asMillis()));
                });
            }
            
            // Queue related commands
            case "queue" -> {
                final SongQueue songQueue = radio.getQueue();
                final Queue<Song> queue = songQueue.getQueue();
                
                if (queue.isEmpty()) {
                    radio.message(player, "&cNothing is queued!");
                }
                else {
                    radio.message(player, "&aCurrent queue:");
                    
                    int index = 1;
                    for (Song song : queue) {
                        radio.message(
                                player,
                                " &f#&l%s&f %s".formatted(
                                        index,
                                        song.getNameFormatted()
                                )
                        );
                        
                        if (++index >= 10) {
                            radio.message(player, " &7....and %s more!".formatted(queue.size() - index));
                            break;
                        }
                    }
                }
            }
            
            case "add" -> {
                final String query = buildSongName(args);
                final Song song = registry.byName(query);
                
                if (song == null) {
                    radio.message(player, "&cCouldn't find song named &6%s&c!".formatted(query));
                    return;
                }
                
                final SongQueue queue = radio.getQueue();
                
                queue.addSong(song);
                
                radio.message(player, "&aAdded &6%s&a to the queue! &8(%s)".formatted(song.getName(), queue.size()));
                
                // Start playing if nothing is already playing
                if (!radio.isPlaying()) {
                    radio.getQueue().playNext();
                }
            }
            
            case "skip" -> {
                if (!radio.hasSong()) {
                    sendNoSongMessage(player);
                    return;
                }
                
                final Song skippedSong = radio.currentSong();
                
                if (skippedSong != null) {
                    radio.message(player, "&aSkipped &6%s&a!".formatted(skippedSong.getName()));
                    
                    radio.stopPlaying();
                    radio.getQueue().playNext();
                }
                else {
                    radio.message(player, "&cNothing to skip!");
                }
            }
            
            case "play" -> {
                // If just typed play then play the next queue song unless already playing
                if (args.length == 1) {
                    if (radio.isPlaying()) {
                        radio.message(player, "&cAlready playing!");
                    }
                    else {
                        final SongQueue queue = radio.getQueue();
                        
                        if (!queue.hasNext()) {
                            radio.message(player, "&cNothing to play! Either add something to the queue or give provide a song's name!");
                        }
                        else {
                            queue.playNext();
                        }
                    }
                }
                // Else play the given song
                else {
                    final String query = buildSongName(args);
                    final Song song = registry.byName(query);
                    
                    if (song == null) {
                        radio.message(player, "&cCould not find song named &6%s&a!".formatted(query));
                        return;
                    }
                    
                    startPlayingSong(song);
                }
            }
            
            case "clear" -> {
                final SongQueue queue = radio.getQueue();
                
                if (!queue.hasNext()) {
                    radio.message(player, "&cThe queue is currently empty!");
                }
                else {
                    queue.clear();
                    radio.message(player, "&aSuccessfully cleared the queue!");
                }
            }
            
            case "volume" -> {
                if (args.length == 1) {
                    final float volume = radio.volume();
                    
                    radio.message(player, "&aCurrent volume is &b%.0f%%&a".formatted(volume * 100));
                }
                else {
                    final float volume = Numbers.getFloat(args[1]);
                    
                    if (volume < 0 || volume > 100) {
                        radio.message(player, "&cVolume must be between 0-100!");
                        return;
                    }
                    
                    radio.volume(volume / 100);
                    radio.message("&6%s&a changed volume to &b%.0f%%&a.".formatted(player.getName(), volume));
                }
            }
        }
        
    }
    
    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length >= 2 && (args[0].equals("play") || args[0].equals("add"))) {
            return completerSort(Eterna.getManagers().song.listNames(), args);
        }
        
        return null;
    }
    
    private String buildSongName(@Nonnull String[] args) {
        return Chat.arrayToString(args, 1);
    }
    
    private void displayListOfCachedSongs(@Nonnull Player player, int page) {
        final SongManager registry = Eterna.getManagers().song;
        
        if (!registry.anySongs()) {
            radio.message(player, "&cThere aren't any songs loaded!");
            radio.message(
                    player,
                    "&cMake sure to put your songs into &eplugins/EternaAPI/songs&c folder and run &e/nbs reload&c!"
            );
            return;
        }
        
        // Floor page
        final int maxPage = registry.maxPage();
        page = Math.min(page, maxPage);
        
        final TextComponent.Builder builder
                = Component.text()
                           .append(Component.text("Listing loaded songs: ", NamedTextColor.GREEN))
                           .append(Component.text(" (%s/%s)".formatted(page, maxPage), NamedTextColor.GRAY));
        
        // Make page clicks
        if (page > 1) {
            builder.append(Component.text(" ⮜", NamedTextColor.DARK_AQUA)
                                    .hoverEvent(
                                            HoverEvent.showText(Component.text("Click to show previous page!", NamedTextColor.AQUA))
                                    )
                                    .clickEvent(
                                            ClickEvent.runCommand("/nbs list %s".formatted(page - 1))
                                    ));
        }
        
        if (page < maxPage) {
            builder.append(Component.text(" ⮞", NamedTextColor.DARK_AQUA)
                                    .hoverEvent(
                                            HoverEvent.showText(Component.text("Click to show next page!", NamedTextColor.AQUA))
                                    )
                                    .clickEvent(
                                            ClickEvent.runCommand("/nbs list %s".formatted(page + 1))
                                    ));
        }
        
        radio.message(player, builder);
        
        for (final String name : registry.listNames(page)) {
            radio.message(player, makeSongComponent(name));
        }
    }
    
    private TextComponent.Builder makeSongComponent(String name) {
        final String prettyName = Chat.capitalize(name);
        final String prettyNameShortened = shorten(prettyName, 35);
        final TextComponent.Builder builder = Component.text();
        
        builder.append(Component.text(prettyNameShortened, NamedTextColor.GOLD)).append(Component.text("  "));
        
        // Make buttons
        builder.append(
                Component.text("ǫᴜᴇᴜᴇ", NamedTextColor.YELLOW, TextDecoration.BOLD, TextDecoration.UNDERLINED)
                         .hoverEvent(HoverEvent.showText(Component.text("Click to add %s to the queue!".formatted(prettyName), NamedTextColor.GOLD)))
                         .clickEvent(ClickEvent.runCommand("/nbs add " + name))
        );
        
        builder.append(Component.text("  "));
        
        builder.append(
                Component.text("ᴘʟᴀʏ", NamedTextColor.GOLD, TextDecoration.BOLD, TextDecoration.UNDERLINED)
                         .hoverEvent(HoverEvent.showText(Component.text("Click to play %s!".formatted(prettyName), NamedTextColor.YELLOW)))
                         .clickEvent(ClickEvent.runCommand("/nbs play " + name))
        );
        
        return builder;
    }
    
    private String shorten(String name, int length) {
        return name.length() <= length ? name : name.substring(0, length) + "...";
    }
    
    private void sendNoSongMessage(@Nonnull Player player) {
        radio.message(player, "&cThere isn't anything playing right now!");
    }
    
    private void startPlayingSong(Song song) {
        radio.startPlaying(song);
    }
    
}