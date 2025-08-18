package me.hapyl.eterna.module.parkour;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * Stores all player info <b>before</b> starting the {@link Parkour} to restore later.
 */
public class PlayerInfo {
    
    private final Player player;
    private final GameMode gamemode;
    private final Collection<PotionEffect> effects;
    private final ItemStack[] inventory;
    
    public PlayerInfo(@Nonnull Player player) {
        this.player = player;
        
        // Store player data
        this.gamemode = player.getGameMode();
        this.effects = List.copyOf(player.getActivePotionEffects());
        this.inventory = player.getInventory().getContents();
        
        // Prepare player
        player.setGameMode(GameMode.ADVENTURE);
        
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, PotionEffect.INFINITE_DURATION, 1, false, false, false));
        player.getInventory().clear();
    }
    
    public void restore() {
        player.setGameMode(gamemode);
        
        // Make sure to remove our invisibility BEFORE restoring the effects
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        
        for (PotionEffect effect : effects) {
            player.addPotionEffect(effect);
        }
        
        player.getInventory().setContents(inventory);
    }
    
}
