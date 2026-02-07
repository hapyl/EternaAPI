package me.hapyl.eterna.module.command;

import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaKeyed;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.command.completer.CompleterTooltip;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a command listener that prevents a kick for sending invalid characters in chat {@code §}.
 * <p>It's packaged here to prevent kick for tabbing the {@link CompleterTooltip}.</p>
 */
@ApiStatus.Internal
public final class CommandHandler extends EternaKeyed implements Listener {
    private static final String translateKey = "multiplayer.disconnect.illegal_characters";
    
    public CommandHandler(@Nullable EternaKey key) {
        super(key);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleAsyncPlayerChatEvent(PlayerKickEvent ev) {
        if (ev.reason() instanceof TranslatableComponent translatableComponent && translatableComponent.key().equals(translateKey)) {
            EternaLogger.message(ev.getPlayer(), Component.text("A kick was prevented because you sent illegal characters!", NamedTextColor.RED));
            ev.setCancelled(true);
        }
    }
}
