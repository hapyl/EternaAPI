package me.hapyl.eterna.module.parkour;

import me.hapyl.eterna.module.util.StrictReference;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Stores player-related info before the starting the {@link Parkour} so we can restore it.
 */
@ApiStatus.Internal
public class PlayerInfo {
    
    private final Player player;
    
    private final StrictReference<GameMode> gameMode;
    private final StrictReference<Collection<PotionEffect>> potionEffects;
    private final StrictReference<ItemStack[]> inventoryContents;
    private final StrictReference<Boolean> invulnerable;
    
    PlayerInfo(@NotNull Player player) {
        this.player = player;
        
        // Store the data
        this.gameMode = StrictReference.refer(player::getGameMode);
        this.potionEffects = StrictReference.refer(player::getActivePotionEffects);
        this.inventoryContents = StrictReference.refer(() -> player.getInventory().getContents());
        this.invulnerable = StrictReference.refer(player::isInvulnerable);
        
        // Clear the data
        player.setGameMode(GameMode.ADVENTURE);
        player.setInvulnerable(true);
        
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, PotionEffect.INFINITE_DURATION, 1, false, false, false));
        player.getInventory().clear();
    }
    
    @ApiStatus.Internal
    public void restore() {
        final GameMode gameMode = this.gameMode.refer();
        final Collection<PotionEffect> potionEffects = this.potionEffects.refer();
        final ItemStack[] inventoryContents = this.inventoryContents.refer();
        final boolean invulnerable = this.invulnerable.refer();
        
        this.player.setGameMode(gameMode);
        this.player.setInvulnerable(invulnerable);
        
        // Make sure to remove our invisibility BEFORE restoring the effects
        this.player.removePotionEffect(PotionEffectType.INVISIBILITY);
        
        // Return player's effects
        for (PotionEffect effect : potionEffects) {
            this.player.addPotionEffect(effect);
        }
        
        // Return player's inventory
        this.player.getInventory().setContents(inventoryContents);
    }
    
}
