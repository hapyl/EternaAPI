package me.hapyl.eterna.builtin.command;

import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.builtin.menu.SongPlayerMenu;
import me.hapyl.eterna.module.command.ArgumentList;
import me.hapyl.eterna.module.command.SimpleAdminCommand;
import me.hapyl.eterna.module.player.song.SongHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Built in command for playing nbs files.
 */
@ApiStatus.Internal
public final class NoteBlockStudioCommand extends SimpleAdminCommand {
    
    public NoteBlockStudioCommand(@NotNull EternaKey key, @NotNull String name) {
        super(name);
        
        key.validateKey();
        
        setDescription("Opens the song player menu.");
        setCompleterValues(0, "reload");
    }
    
    @Override
    protected void execute(@NotNull CommandSender sender, @NotNull ArgumentList args) {
        if (SongHandler.isLock()) {
            EternaLogger.nbs(sender, Component.text("Currently loading songs, please wait...", NamedTextColor.RED));
            return;
        }
        
        final String argument = args.get(0).toString();
        
        if (argument.equalsIgnoreCase("reload")) {
            EternaLogger.nbs(sender, Component.text("Reloading songs...", NamedTextColor.YELLOW));
            
            SongHandler.reload().thenAccept(count -> EternaLogger.nbs(
                    sender,
                    Component.empty()
                             .append(Component.text("Successfully reloaded ", NamedTextColor.GREEN))
                             .append(Component.text(count, NamedTextColor.AQUA))
                             .append(Component.text(" songs!", NamedTextColor.GREEN))
            ));
        }
        else {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Component.text("You must be a player to use this!", NamedTextColor.RED));
                return;
            }
            
            new SongPlayerMenu(player, SongPlayerMenu.SongSupplier.allSongs());
        }
    }
}