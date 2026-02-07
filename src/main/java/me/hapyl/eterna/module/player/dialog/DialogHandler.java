package me.hapyl.eterna.module.player.dialog;

import me.hapyl.eterna.EternaHandler;
import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.player.dialog.entry.OptionIndex;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public final class DialogHandler extends EternaHandler<Player, DialogInstance> implements Listener {
    
    static DialogHandler handler;
    
    public DialogHandler(@NotNull EternaKey key, @NotNull EternaPlugin eterna) {
        super(key, eterna);
        
        handler = this;
    }
    
    @EventHandler
    public void handlePlayerItemHeldEvent(PlayerItemHeldEvent ev) {
        final Player player = ev.getPlayer();
        final int newSlot = ev.getNewSlot();
        
        final OptionIndex optionIndex = OptionIndex.fromInt(newSlot);
        
        if (optionIndex == null) {
            return;
        }
        
        get(player).ifPresent(dialogInstance -> dialogInstance.trySelectOption(optionIndex));
    }
    
    @EventHandler
    public void handlePlayerQuitEvent(PlayerQuitEvent ev) {
        final Player player = ev.getPlayer();
        final DialogInstance instance = getOrNull(player);
        
        // cancel0() calls unregister() on the handler
        if (instance != null) {
            instance.cancel(DialogEndType.PLAYER_LEFT);
        }
    }
    
}
