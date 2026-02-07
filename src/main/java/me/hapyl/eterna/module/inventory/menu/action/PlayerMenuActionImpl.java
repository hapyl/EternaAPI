package me.hapyl.eterna.module.inventory.menu.action;

import com.google.common.collect.Maps;
import me.hapyl.eterna.module.inventory.menu.PlayerMenu;
import me.hapyl.eterna.module.player.PlayerAction;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

/**
 * An internal implementation for {@link PlayerMenuAction}.
 *
 * @see PlayerMenuAction#builder()
 */
@ApiStatus.Internal
public final class PlayerMenuActionImpl implements PlayerMenuAction {
    
    final EnumMap<ClickType, PlayerAction> mapped;
    
    PlayerMenuActionImpl() {
        this.mapped = Maps.newEnumMap(ClickType.class);
    }
    
    @Override
    public void use(@NotNull PlayerMenu menu, @NotNull Player player, @NotNull ClickType clickType, int slot, int hotbarNumber) {
        final PlayerAction action = mapped.get(clickType);
        
        if (action != null) {
            action.use(player);
        }
    }
}
