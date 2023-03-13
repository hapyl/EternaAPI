package me.hapyl.spigotutils.module.hologram;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerHologram extends Hologram {

    private static final Map<Player, List<PlayerHologram>> perPlayerHolograms = new HashMap<>();
    private final Player player;

    private PlayerHologram(Player player) {
        this.player = player;
        this.addPlayerHologram();
    }

    @Override
    public Hologram show(Player... players) {
        return this.show();
    }

    private void addPlayerHologram() {
        final List<PlayerHologram> list = getPlayerHolograms();
        list.add(this);
        perPlayerHolograms.put(this.player, list);
    }

    public List<PlayerHologram> getPlayerHolograms() {
        return perPlayerHolograms.getOrDefault(this.player, new ArrayList<>());
    }

    public PlayerHologram remove() {
        final List<PlayerHologram> list = getPlayerHolograms();
        list.remove(this);
        perPlayerHolograms.put(this.player, list);
        return this;
    }

    @Override
    public final Hologram hide(Player... players) {
        return this.hide();
    }

    @Override
    public final Hologram hide(boolean flag, Player... players) {
        return this.hide();
    }

    public PlayerHologram show() {
        super.show(this.player);
        return this;
    }

    public PlayerHologram hide() {
        super.hide(this.player);
        return this;
    }

}
