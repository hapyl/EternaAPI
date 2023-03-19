package me.hapyl.spigotutils.module.hologram;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.annotate.Super;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.reflect.Reflect;
import me.hapyl.spigotutils.module.reflect.ReflectPacket;
import me.hapyl.spigotutils.registry.EternaRegistry;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * This class allows to create packet-holograms
 */
public class Hologram implements Holo {

    /**
     * Vertical offset of the holograms.
     */
    public static final double HOLOGRAM_OFFSET = 0.3d;
    public static final double HOLOGRAM_HEIGHT = 1.75d;

    private final List<String> lines;
    private final List<HologramArmorStand> packets;
    private final Map<Player, Boolean> showingTo;

    private int removeWhenFarAway = 25;
    private boolean persistent;

    private BukkitTask task;
    private Location location;

    /**
     * Creates hologram with provided size.
     *
     * @param size - Lines size.
     */
    public Hologram(int size) {
        this.lines = new ArrayList<>(size);
        this.packets = Lists.newArrayList();
        this.showingTo = Maps.newConcurrentMap();
        EternaRegistry.getHologramRegistry().register(this);
    }

    public Hologram() {
        this(1);
    }

    @Override
    public Hologram addLine(String line) {
        this.lines.add(line);
        return this;
    }

    @Override
    public Hologram addLines(String... lines) {
        this.lines.addAll(Arrays.asList(lines));
        return this;
    }

    @Override
    public Hologram setLines(String... lines) {
        this.lines.clear();
        this.lines.addAll(Arrays.asList(lines));
        this.updateLines(false);
        return this;
    }

    @Override
    public Hologram removeLine(int index) throws IndexOutOfBoundsException {
        if (this.lines.size() - 1 < index) {
            throw new IndexOutOfBoundsException(String.format("There is only %s lines, not %s.", this.lines.size(), index));
        }
        else {
            this.lines.remove(index);
        }
        return this;
    }

    @Override
    public Hologram setLine(int index, String line) {
        if (this.lines.size() - 1 < index) {
            for (int i = 0; i < index; i++) {
                addLine("");
            }
        }
        else {
            this.lines.set(index, line);
        }
        return this;
    }

    @Override
    public Hologram clear() {
        this.lines.clear();
        return this;
    }

    @Override
    public Hologram setPersistent(boolean persistent) {
        this.persistent = persistent;
        return this;
    }

    @Override
    public boolean isPersistent() {
        return persistent;
    }

    @Override
    public Hologram setRemoveWhenFarAway(int hideWhenFurtherThan) {
        this.removeWhenFarAway = hideWhenFurtherThan;
        return this;
    }

    @Override
    public int getRemoveWhenFarAway() {
        return removeWhenFarAway;
    }

    /**
     * Returns the map of players and their status.
     *
     * @return the map of players and their status.
     */
    public Map<Player, Boolean> getShowingTo() {
        return showingTo;
    }

    @Override
    public boolean isShowingTo(Player player) {
        return this.showingTo.containsKey(player) && this.showingTo.get(player);
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public boolean destroy() {
        this.removeStands();

        if (this.task != null) {
            this.task.cancel();
        }

        EternaRegistry.getHologramRegistry().unregister(this);
        return true;
    }

    protected void removeStands() {
        this.showingTo.forEach((player, status) -> hide(player));
        this.packets.clear();
    }

    @Override
    public Hologram showAll() {
        return this.show(Bukkit.getOnlinePlayers().toArray(new Player[] {}));
    }

    @Super
    @Override
    public Hologram show(Player... players) {
        if (this.location == null) {
            for (final Player player : players) {
                Chat.sendMessage(player, "&4Could not spawn a hologram for you since is wasn't created yet!");
            }
            return null;
        }

        for (Player player : players) {
            showingTo.put(player, true);
            packets.forEach(hologram -> {
                hologram.show(player);
                hologram.updateLocation(player);
            });
        }

        return this;
    }

    @Super
    @Override
    public Hologram hide(boolean flag, Player... players) {
        for (Player player : players) {
            if (flag) {
                showingTo.put(player, false);
            }
            else {
                showingTo.remove(player);
            }

            packets.forEach(hologram -> hologram.hide(player));
        }

        return this;
    }

    @Override
    public Hologram hide(Player... players) {
        return hide(false, players);
    }

    public Hologram onTick(HologramAction<?> action, int tick) {
        return onTick(action, tick, Bukkit.getOnlinePlayers().toArray(new Player[] {}));
    }

    public Hologram onTick(HologramAction<?> action, int tick, Player... receivers) {
        if (this.task != null) {
            this.task.cancel();
        }

        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                action.consume(receivers);
            }
        }.runTaskTimer(EternaPlugin.getPlugin(), 0, tick);
        return this;
    }

    @Override
    public Hologram teleport(Location location) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            move(location, onlinePlayer);
        }
        return this;
    }

    public double getHologramHeight() {
        return HOLOGRAM_HEIGHT;
    }

    @Override
    public Hologram updateLines(boolean keepListSorted) {
        Location location = this.location.clone();
        location.add(0.0d, getHologramHeight(), 0.0d); // fix marker
        removeStands();

        if (keepListSorted) {
            for (final String line : lines) {
                location.add(0.0d, HOLOGRAM_OFFSET, 0.0d);
                createStand(location, ChatColor.translateAlternateColorCodes('&', line));
            }
        }
        else {
            location.add(0.0d, HOLOGRAM_OFFSET * (double) lines.size(), 0.0d);
            for (final String line : lines) {
                createStand(location, ChatColor.translateAlternateColorCodes('&', line));
                location.subtract(0.0d, HOLOGRAM_OFFSET, 0.0d);
            }
        }

        return this;
    }

    @Override
    public Hologram updateLines() {
        return this.updateLines(false);
    }

    @Override
    public Hologram create(Location location) {
        if (this.location != null) {
            throw new IllegalArgumentException("This hologram was already created!");
        }

        this.location = location.clone();
        this.location.add(0.0d, getHologramHeight(), 0.0); // Fix marker location

        // Move current location up so holograms stop at the start position
        this.location.add(0.0d, HOLOGRAM_OFFSET * (double) lines.size(), 0.0d);

        for (String string : lines) {
            createStand(this.location, Chat.format(string));
            this.location.subtract(0.0d, HOLOGRAM_OFFSET, 0.0d);
        }
        return this;
    }

    @Override
    public Hologram move(Location location, Player... players) {
        location.add(0.0d, HOLOGRAM_OFFSET * (double) lines.size(), 0.0d);
        packets.forEach(hologram -> {
            hologram.setLocation(location);
            hologram.updateLocation(players);
            location.subtract(0.0d, HOLOGRAM_OFFSET, 0.0d);
        });
        return this;
    }

    private void createStand(Location location, String name) {
        // skip line if empty instead of | thing
        if (name.isEmpty()) {
            return;
        }

        packets.add(new HologramArmorStand(location, name));
    }

    public void updateMetadata(EntityArmorStand stand, Player player) {
        final ArmorStand bukkit = (ArmorStand) stand.getBukkitEntity();
        final PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(bukkit.getEntityId(), Reflect.getDataWatcher(stand).c());

        ReflectPacket.send(packet, player);
    }

    @Override
    public String toString() {
        return this.lines.toString();
    }

    public boolean isCreated() {
        return this.location != null;
    }
}
