package kz.hapyl.spigotutils.module.parkour;

import kz.hapyl.spigotutils.module.util.Holder;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Collection;

public class PlayerInfo extends Holder<Player> {

    private final GameMode gamemode;
    private final Collection<PotionEffect> effects;
    private final ItemStack[] inventory;

    public PlayerInfo(Player player) {
        super(player);
        this.gamemode = player.getGameMode();
        this.effects = new ArrayList<>(player.getActivePotionEffects());
        this.inventory = player.getInventory().getContents();
    }

    public void restore() {
        final Player player = get();
        player.setGameMode(gamemode);

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        for (PotionEffect effect : effects) {
            player.addPotionEffect(effect);
        }

        player.getInventory().setContents(inventory);
    }

}
