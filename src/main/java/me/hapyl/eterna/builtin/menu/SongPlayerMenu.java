package me.hapyl.eterna.builtin.menu;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.EternaColors;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.component.ButtonComponents;
import me.hapyl.eterna.module.component.Components;
import me.hapyl.eterna.module.component.ProgressBar;
import me.hapyl.eterna.module.inventory.ItemStacks;
import me.hapyl.eterna.module.inventory.builder.ItemBuilder;
import me.hapyl.eterna.module.inventory.menu.ChestSize;
import me.hapyl.eterna.module.inventory.menu.PlayerPageMenu;
import me.hapyl.eterna.module.inventory.menu.SlotBoundItemStack;
import me.hapyl.eterna.module.inventory.menu.action.PlayerMenuAction;
import me.hapyl.eterna.module.inventory.menu.pattern.SlotPattern;
import me.hapyl.eterna.module.inventory.sign.SignInput;
import me.hapyl.eterna.module.inventory.sign.SignResponse;
import me.hapyl.eterna.module.inventory.sign.SignType;
import me.hapyl.eterna.module.player.PlayerLib;
import me.hapyl.eterna.module.player.song.*;
import me.hapyl.eterna.Runnables;
import me.hapyl.eterna.module.text.TimeFormat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Represents an internal menu for song player.
 */
@ApiStatus.Internal
public final class SongPlayerMenu extends PlayerPageMenu<Song> {
    
    private static final ItemStack ITEM_EMPTY_CONTENTS_NO_SONGS_ON_SEVER;
    private static final ProgressBar PROGRESS_BAR;
    private static final ProgressBar PROGRESS_BAR_PAUSED;
    
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("mm:ss");
    
    static {
        ITEM_EMPTY_CONTENTS_NO_SONGS_ON_SEVER = new ItemBuilder(Material.MUSIC_DISC_11)
                .setName(Component.text("No Songs!", NamedTextColor.RED))
                .addLore()
                .addWrappedLore(Component.text("There isn't anything to show here, because there aren't any songs on the server!"))
                .addLore()
                .addWrappedLore(
                        Component.empty()
                                 .append(Component.text("Make sure to put `.nbs` files into ", NamedTextColor.GRAY))
                                 .append(Component.text("/songs ", NamedTextColor.YELLOW))
                                 .append(Component.text("folder and do ", NamedTextColor.GRAY))
                                 .append(Component.text("/nbs reload", NamedTextColor.YELLOW))
                                 .append(Component.text("!", NamedTextColor.GRAY))
                )
                .addLore()
                .asIcon();
        
        PROGRESS_BAR = new ProgressBar("|", 50, Style.style(NamedTextColor.GREEN));
        PROGRESS_BAR_PAUSED = PROGRESS_BAR.copyOfStyle(Style.style(NamedTextColor.YELLOW));
    }
    
    private final SongPlayer serverSongPlayer;
    private final SongSupplier supplier;
    
    @ApiStatus.Internal
    public SongPlayerMenu(@NotNull Player player, @NotNull SongSupplier supplier) {
        super(player, supplier.createTitle(), ChestSize.SIZE_6);
        
        this.serverSongPlayer = SongPlayer.SERVER_PLAYER;
        this.supplier = supplier;
        
        this.setContents(supplier.listSongs());
        this.setPattern(SlotPattern.INNER_LEFT_TO_RIGHT);
        this.setFrom(ChestSize.SIZE_2);
        this.setTo(ChestSize.SIZE_5);
        
        this.setItemArrowPrevious(SlotBoundItemStack.of(2, ITEM_ARROW_PREVIOUS));
        this.setItemArrowNext(SlotBoundItemStack.of(6, ITEM_ARROW_NEXT));
        
        this.setItemEmptyContents(SlotBoundItemStack.of(22, createEmptyContentsNoSongsToQueryItem(supplier)));
        
        this.openMenu();
    }
    
    @Override
    public boolean canOpen(@NotNull Player player) {
        return player.isOp();
    }
    
    @Override
    @NotNull
    public ItemStack asItem(@NotNull Player player, @NotNull Song song, int index, int page) {
        final SongPlayer.Status status = serverSongPlayer.getStatus();
        
        final SongInstance currentSong = serverSongPlayer.getCurrentSong();
        final boolean isCurrentSong = currentSong != null && currentSong.getSong() == song;
        
        final SongQueue queue = serverSongPlayer.getQueue();
        final int queueIndex = queue.hasSong(song) ? queue.indexOf(song) + 1 : 0;
        
        final ItemBuilder builder = new ItemBuilder(
                isCurrentSong
                ? status == SongPlayer.Status.PAUSED
                  ? Material.MUSIC_DISC_13
                  : Material.MUSIC_DISC_CAT
                : queueIndex > 0
                  ? Material.MUSIC_DISC_LAVA_CHICKEN
                  : Material.MUSIC_DISC_STAL
        );
        
        builder.setName(song.getName());
        builder.setAmount(Math.max(1, queueIndex));
        
        builder.addLore(Component.text(song.getFileName(), NamedTextColor.DARK_GRAY));
        builder.addLore();
        
        final Component description = song.getDescription();
        
        if (!Components.isEmpty(description)) {
            builder.addWrappedLore(
                    description,
                    _component -> Component.text(" ").append(_component)
                                           .color(NamedTextColor.GRAY)
                                           .decoration(TextDecoration.ITALIC, false)
            );
            
            builder.addLore();
        }
        
        builder.addLore(Component.text("Author ", NamedTextColor.WHITE).append(song.getAuthor().color(NamedTextColor.GOLD)));
        builder.addLore(Component.text("Original Author ", NamedTextColor.WHITE).append(song.getOriginalAuthor().color(NamedTextColor.GOLD)));
        builder.addLore();
        
        builder.addLore(Component.text("Length ", NamedTextColor.WHITE).append(Component.text(TimeFormat.format((long) (song.getDurationIsSeconds() * 1000L)), NamedTextColor.GREEN)));
        builder.addLore(Component.text("TPM ", NamedTextColor.WHITE).append(Component.text(song.getTempo(), NamedTextColor.GREEN)));
        builder.addLore();
        
        // If not vanilla compatible, show warning
        if (!song.isOkOctave()) {
            builder.addWrappedLore(
                    Component.empty()
                             .append(Component.text("Some notes in this song aren't between", NamedTextColor.YELLOW))
                             .append(Component.text("F#0", NamedTextColor.GOLD))
                             .append(Component.text("-", NamedTextColor.YELLOW))
                             .append(Component.text("F#2", NamedTextColor.GOLD))
                             .append(Component.text(", therefore, it might sound off!", NamedTextColor.YELLOW))
            );
            builder.addLore();
        }
        
        if (isCurrentSong) {
            if (status == SongPlayer.Status.PAUSED) {
                builder.addLore(Component.text("⏸ Currently paused!", EternaColors.AQUA));
                builder.addLore();
                
                builder.addLore(ButtonComponents.left("unpause"));
            }
            else {
                builder.addLore(Component.text("▶ Currently playing!", EternaColors.AQUA));
                builder.addLore();
                
                builder.addLore(ButtonComponents.left("pause"));
            }
        }
        else {
            builder.addLore(ButtonComponents.left(queue.hasSong(song) ? "remove from queue" : "add to queue"));
            builder.addLore(ButtonComponents.right("play"));
        }
        
        return builder.asIcon();
    }
    
    @Override
    public void onClick(@NotNull Player player, @NotNull Song song, int index, int page, @NotNull ClickType clickType) {
        final SongInstance currentSong = serverSongPlayer.getCurrentSong();
        final boolean isCurrentSong = currentSong != null && currentSong.getSong() == song;
        
        if (isCurrentSong) {
            final SongPlayer.Status status = serverSongPlayer.getStatus();
            
            if (status == SongPlayer.Status.PAUSED || status == SongPlayer.Status.PLAYING) {
                serverSongPlayer.pause();
            }
        }
        else {
            if (clickType.isLeftClick()) {
                final SongQueue queue = serverSongPlayer.getQueue();
                
                if (queue.hasSong(song)) {
                    serverSongPlayer.removeFromQueue(player, song);
                }
                else {
                    serverSongPlayer.addToQueue(player, song);
                }
            }
            else if (clickType.isRightClick()) {
                serverSongPlayer.startPlaying(song);
            }
        }
        
        this.openMenu();
    }
    
    @Override
    public void onOpen() {
        // Set header & footer
        this.fillRow(0, ItemStacks.blackBar());
        this.fillRow(5, ItemStacks.blackBar());
        
        // Set buttons
        this.setCloseButton();
        
        this.setItem(46,
                     createSearchItem(),
                     PlayerMenuAction.builder()
                                     .left(player -> {
                                         new SignInput(player, SignType.JUNGLE, "Enter Song Name") {
                                             @Override
                                             public void onResponse(@NotNull SignResponse response) {
                                                 response.synchronize(Eterna.getPlugin(), () -> new SongPlayerMenu(player, SongSupplier.query(response.toString())));
                                             }
                                         };
                                     })
                                     .right(player -> {
                                         if (supplier.isQuerying()) {
                                             new SongPlayerMenu(player, SongSupplier.allSongs());
                                             
                                             PlayerLib.playSound(player, Sound.ENTITY_CAT_AMBIENT, 1.0f);
                                         }
                                     })
        );
        
        this.setItem(
                47,
                createQueueItem(),
                PlayerMenuAction.builder()
                                .left(player -> {
                                    final Song nextSong = serverSongPlayer.getQueue().pollNext();
                                    
                                    if (nextSong != null) {
                                        serverSongPlayer.startPlaying(nextSong);
                                        openMenu();
                                    }
                                })
                                .right(player -> {
                                    final Song nextSong = serverSongPlayer.getQueue().peekNext();
                                    
                                    if (nextSong != null) {
                                        serverSongPlayer.removeFromQueue(player, nextSong);
                                        openMenu();
                                    }
                                })
                                .middle(player -> {
                                    serverSongPlayer.getQueue().clear();
                                    openMenu();
                                    
                                    PlayerLib.playSound(player, Sound.ITEM_BUCKET_FILL, 1.0f);
                                })
        );
        
        this.setItem(
                51,
                createCurrentlyPlayingItem(),
                PlayerMenuAction.builder()
                                .left(player -> {
                                    serverSongPlayer.pause();
                                    openMenu();
                                })
                                .right(player -> {
                                    serverSongPlayer.stopPlaying(true);
                                    openMenu();
                                })
                                .shiftLeft(player -> doAdvancePlayer(player, -10))
                                .shiftRight(player -> doAdvancePlayer(player, 10))
                                .middle(player -> {
                                    serverSongPlayer.stopPlaying(false);
                                    openMenu();
                                })
        );
        
        this.setItem(52, createVolumeItem(), PlayerMenuAction.of(
                player -> new SignInput(player, SignType.OAK, "", SignInput.DASHED_LINE, "Enter Volume", "(0-100)") {
                    @Override
                    public void onResponse(@NotNull SignResponse response) {
                        final int value = response.get(0).toInt();
                        
                        Runnables.sync(() -> {
                            serverSongPlayer.setVolume(value / 100f);
                            EternaLogger.nbs(
                                    player,
                                    Component.empty()
                                             .append(Component.text("Set volume to ", NamedTextColor.GREEN))
                                             .append(Component.text("%.0f%%".formatted(serverSongPlayer.getVolume() * 100), NamedTextColor.AQUA))
                                             .append(Component.text("!", NamedTextColor.GREEN))
                            );
                            
                            PlayerLib.plingNote(player, 1.0f);
                            openMenu();
                        });
                    }
                }
        ));
        
        // Call super after we set out buttons
        super.onOpen();
    }
    
    private void doAdvancePlayer(@NotNull Player player, final int advance) {
        serverSongPlayer.advanceCurrentSong(advance);
        openMenu();
        
        PlayerLib.playSound(player, Sound.BLOCK_NOTE_BLOCK_HAT, advance > 0 ? 1.25f : 0.75f);
    }
    
    @NotNull
    private ItemStack createSearchItem() {
        final ItemBuilder builder = new ItemBuilder(Material.JUNGLE_SIGN);
        builder.setName(Component.text("Search"));
        builder.addLore();
        
        final String query = supplier.query();
        final boolean querying = supplier.isQuerying();
        
        builder.predicate(querying, ItemBuilder::glow);
        
        builder.addLore(Component.text("Current Query:", NamedTextColor.GREEN));
        builder.addLore(querying ? Component.text(query, NamedTextColor.GRAY) : Component.text(" None!", NamedTextColor.DARK_GRAY));
        builder.addLore();
        
        if (querying) {
            builder.addLore(ButtonComponents.left("edit"));
            builder.addLore(ButtonComponents.right("clear"));
        }
        else {
            builder.addLore(ButtonComponents.left("search"));
        }
        
        return builder.asIcon();
    }
    
    @NotNull
    private ItemStack createQueueItem() {
        final ItemBuilder builder = new ItemBuilder(Material.WRITABLE_BOOK);
        builder.setName(Component.text("Queue"));
        builder.addLore();
        
        final SongQueue queue = serverSongPlayer.getQueue();
        
        if (queue.isEmpty()) {
            builder.addLore(Component.text("Nothing in the queue!"));
        }
        else {
            final List<? extends Component> songNames = queue.stream()
                                                             .limit(10)
                                                             .map(song -> Component.empty()
                                                                                   .append(song.getOriginalAuthor())
                                                                                   .append(Component.text(" - "))
                                                                                   .append(song.getName())
                                                                                   .color(NamedTextColor.WHITE))
                                                             .toList();
            
            final int extra = Math.max(0, queue.size() - 10);
            
            for (int i = 0; i < songNames.size(); i++) {
                final Component songName = songNames.get(i);
                
                builder.addLore(
                        Component.empty()
                                 .append(Component.text("%s. ".formatted(i + 1)))
                                 .append(songName)
                );
            }
            
            if (extra > 0) {
                builder.addLore(Component.text("...and %s more!".formatted(extra), NamedTextColor.DARK_GRAY));
            }
            
            builder.addLore();
            builder.addLore(ButtonComponents.left("play next"));
            builder.addLore(ButtonComponents.right("remove next"));
            builder.addLore(ButtonComponents.middle("clear"));
        }
        
        return builder.asIcon();
    }
    
    @NotNull
    private ItemStack createVolumeItem() {
        final ItemBuilder builder = new ItemBuilder(Material.GOAT_HORN);
        builder.setName(Component.text("Volume"));
        builder.addLore();
        
        builder.addLore(Component.text("Current Volume", NamedTextColor.GREEN));
        builder.addLore(Component.text(" %.0f%%".formatted(serverSongPlayer.getVolume() * 100)));
        builder.addLore();
        
        builder.addLore(ButtonComponents.left("set volume"));
        return builder.asIcon();
    }
    
    @NotNull
    private ItemStack createCurrentlyPlayingItem() {
        final SongInstance currentSong = serverSongPlayer.getCurrentSong();
        final SongPlayer.Status status = serverSongPlayer.getStatus();
        
        final ItemBuilder builder = new ItemBuilder(switch (status) {
            case NOT_PLAYING -> Material.GRAY_DYE;
            case PLAYING -> Material.GREEN_DYE;
            case PAUSED -> Material.YELLOW_DYE;
        });
        
        builder.setName(Component.text("Currently Playing"));
        builder.addLore();
        
        if (currentSong != null) {
            final Song song = currentSong.getSong();
            
            builder.addLore(song.getName().color(NamedTextColor.GREEN));
            builder.addLore(Component.text(" ").append(song.getOriginalAuthor()).color(NamedTextColor.GRAY));
            builder.addLore();
            
            final long progress = (long) ((float) currentSong.currentProgress() / song.getTempo() * 1000L);
            final long duration = (long) (song.getDurationIsSeconds() * 1000L);
            
            builder.addLore(
                    Component.empty()
                             .append(Component.text(TIME_FORMAT.format(progress), NamedTextColor.DARK_GRAY))
                             .append(Component.text("  "))
                             .append(status == SongPlayer.Status.PAUSED ? PROGRESS_BAR_PAUSED.build(currentSong) : PROGRESS_BAR.build(currentSong))
                             .append(Component.text("  "))
                             .append(Component.text(TIME_FORMAT.format(duration), NamedTextColor.DARK_GRAY))
            );
            builder.addLore();
            
            builder.addLore(ButtonComponents.left(status == SongPlayer.Status.PAUSED ? "resume" : "pause"));
            builder.addLore(ButtonComponents.right("skip"));
            
            builder.addLore(ButtonComponents.shiftLeft("advance by -10s"));
            builder.addLore(ButtonComponents.shiftRight("advance by +10s"));
            
            builder.addLore(ButtonComponents.middle("stop playing"));
        }
        else {
            builder.addLore(Component.text("Nothing is playing right now!"));
        }
        
        return builder.asIcon();
    }
    
    @NotNull
    private ItemStack createEmptyContentsNoSongsToQueryItem(@NotNull SongSupplier supplier) {
        if (supplier.isQuerying()) {
            return new ItemBuilder(Material.MUSIC_DISC_11)
                    .setName(Component.text("No Songs!", NamedTextColor.RED))
                    .addLore()
                    .addWrappedLore(
                            Component.empty()
                                     .append(Component.text("There aren't any songs that match the query", NamedTextColor.GRAY))
                                     .append(Component.text("\"%s\"".formatted(supplier.query().trim()), NamedTextColor.YELLOW))
                                     .append(Component.text("!", NamedTextColor.GRAY))
                                     .append(Component.newline())
                                     .append(Component.text("Right-click the sign below to clear the search query!", EternaColors.AMBER))
                    )
                    .asIcon();
        }
        
        return ITEM_EMPTY_CONTENTS_NO_SONGS_ON_SEVER;
    }
    
    /**
     * Represents a supplier for a {@link List} of {@link Song}.
     */
    public interface SongSupplier {
        
        /**
         * Lists the {@link Song} supplied by this {@link SongSupplier}.
         *
         * @return the song list supplied by this supplier.
         */
        @NotNull
        List<Song> listSongs();
        
        /**
         * Gets the query of this {@link SongSupplier}, or empty {@link String} is no query.
         *
         * @return the query of this supplier, or empty string is no query.
         */
        @NotNull
        String query();
        
        /**
         * Gets whether this {@link SongSupplier} is querying something.
         *
         * @return {@code true} if this supplier is querying something, {@code false} otherwise.
         */
        default boolean isQuerying() {
            return !query().isEmpty();
        }
        
        /**
         * Creates a {@link Component} to be used as a title for a menu.
         *
         * @return a component to be used as a title for a menu.
         */
        @NotNull
        default Component createTitle() {
            final String query = query();
            final Component component = Component.text("Song Player");
            
            return query.isEmpty()
                   ? component
                   : component.append(Component.text(" \"%s\"".formatted(query)));
        }
        
        /**
         * A static factory method for creating a {@link SongSupplier} consisting of all {@link Song}.
         *
         * @return a new {@link SongSupplier}.
         */
        @NotNull
        static SongSupplier allSongs() {
            return new SongSupplier() {
                @NotNull
                @Override
                public List<Song> listSongs() {
                    return SongHandler.listSongs();
                }
                
                @NotNull
                @Override
                public String query() {
                    return "";
                }
            };
        }
        
        /**
         * A static factory method for creating a {@link SongSupplier} consisting of all {@link Song} that match the given {@code query}.
         *
         * @param query - The query for song filter.
         * @return a new {@link SongSupplier}.
         */
        @NotNull
        static SongSupplier query(@NotNull String query) {
            final String queryLowerCase = query.toLowerCase();
            
            return new SongSupplier() {
                private final List<Song> songList = SongHandler.listSongs()
                                                               .stream()
                                                               .filter(song -> {
                                                                   final String fileName = song.getFileName().toLowerCase();
                                                                   final String songName = Components.toString(song.getName()).toLowerCase();
                                                                   
                                                                   return fileName.contains(queryLowerCase) || songName.contains(queryLowerCase);
                                                               })
                                                               .toList();
                
                @NotNull
                @Override
                public List<Song> listSongs() {
                    return songList;
                }
                
                @NotNull
                @Override
                public String query() {
                    return queryLowerCase;
                }
            };
        }
    }
}
