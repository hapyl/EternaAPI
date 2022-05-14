package kz.hapyl.spigotutils.module.reflect.glow;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import kz.hapyl.spigotutils.EternaPlugin;
import kz.hapyl.spigotutils.module.reflect.Ticking;
import kz.hapyl.spigotutils.module.util.Helper;
import kz.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Map;
import java.util.Set;

public class Glowing implements Ticking, GlowingListener {

    private final ProtocolManager manager = ProtocolLibrary.getProtocolManager();

    private ChatColor color;
    private int duration;
    private boolean everyoneIsListener;

    private boolean status;

    private final Set<Player> viewers;
    private final Map<Player, ChatColor> oldColor = Maps.newHashMap();
    private final Entity entity;

    /**
     * Creates a glowing object.
     *
     * @param entity   - Entity to glow.
     * @param duration - Glow duration.
     */
    public Glowing(Entity entity, int duration) {
        this(entity, ChatColor.WHITE, duration);
    }

    /**
     * Creates a glowing object.
     *
     * @param entity   - Entity to glow.
     * @param color    - Glowing color.
     * @param duration - Glow duration.
     */
    public Glowing(Entity entity, ChatColor color, int duration) {
        this.entity = entity;
        this.duration = duration;
        this.everyoneIsListener = false;
        this.status = false;
        this.viewers = Sets.newHashSet();
        this.setColor(color);

        EternaPlugin.getPlugin().getGlowingManager().addGlowing(entity, this);
    }

    @Override
    public void onGlowingStart() {

    }

    @Override
    public void onGlowingStop() {

    }

    @Override
    public void onGlowingTick() {

    }

    /**
     * Changes glowing color.
     *
     * @param color - New Color.
     */
    public final void setColor(ChatColor color) {
        Validate.isTrue(color.isColor(), "color must be color, not formatter!");
        this.color = color;
        if (isGlowing()) {
            updateTeamColor();
        }
    }

    private void createPacket(boolean flag) {
        final PacketContainer packet = manager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, entity.getEntityId());

        final WrappedDataWatcher data = new WrappedDataWatcher();
        final WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);

        final WrappedDataWatcher realData = WrappedDataWatcher.getEntityWatcher(entity);

        final byte initByte = realData.getByte(0);
        final byte bitMask = (byte) 0x40;

        data.setEntity(entity);
        data.setObject(0, serializer, (byte) (flag ? (initByte | bitMask) : (initByte & ~bitMask)));
        packet.getWatchableCollectionModifier().write(0, data.getWatchableObjects());

        // send packet
        try {
            if (isEveryoneIsListener()) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    manager.sendServerPacket(player, packet);
                }
            }
            else {
                for (Player player : viewers) {
                    manager.sendServerPacket(player, packet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final Entity getEntity() {
        return entity;
    }

    public final ChatColor getColor() {
        return color;
    }

    public final Set<Player> getViewers() {
        return viewers;
    }

    @Override
    public final void tick() {
        if (!status) {
            return;
        }

        boolean lastTick = duration-- <= 0;

        if (lastTick) {
            forceStop();
        }
        this.onGlowingTick();
    }

    public final boolean isEveryoneIsListener() {
        return everyoneIsListener;
    }

    /**
     * Makes every online player a viewer.
     *
     * @param flag - true to make everyone a viewer.
     */
    public final void setEveryoneIsListener(boolean flag) {
        this.everyoneIsListener = flag;
    }

    /**
     * Adds a viewer for this glowing.
     * <b>Glowing viewers must be assigned BEFORE {@link Glowing#glow()} is called!</b>
     *
     * @param player - Player.
     */
    public final void addViewer(Player player) {
        this.viewers.add(player);
    }

    /**
     * Removes a viewer for this glowing.
     *
     * @param player - Player.
     */
    public final void removeViewer(Player player) {
        this.viewers.remove(player);
    }

    /**
     * Returns true is player can see the glowing.
     *
     * @param player - Player.
     * @return true is player can see the glowing.
     */
    public final boolean isViewer(Player player) {
        return this.viewers.contains(player);
    }

    /**
     * Returns duration of glowing.
     *
     * @return duration of glowing.
     */
    public final int getDuration() {
        return duration;
    }

    /**
     * Stars the glowing.
     */
    public final void glow() {
        this.start();
    }

    /**
     * Stars the glowing.
     */
    public final void start() {
        this.status = true;
        createPacket(true);
        createTeam(true);

        this.onGlowingStart();
    }

    public final boolean isGlowing() {
        return status && duration > 0;
    }

    public final void stop() {
        this.duration = 0;
    }

    /**
     * This calls all onStop methods instead of setting duration to 0.
     */
    public final void forceStop() {
        createPacket(false);
        createTeam(false);
        EternaPlugin.getPlugin().getGlowingManager().removeGlowing(entity, this);
        this.onGlowingStop();
    }

    private void updateTeamColor() {
        viewers.forEach(player -> getTeamOrCreate(player).setColor(this.color));
    }

    private void createTeam(boolean flag) {
        if (viewers.isEmpty()) {
            return;
        }

        // Create team
        viewers.forEach(player -> {
            if (flag) {
                final Team team = getTeamOrCreate(player);
                team.addEntry(getEntityName());
            }
            else {
                deleteTeam(player);
            }
        });
    }

    /**
     * This doesn't actually delete anything, just removes target from a team.
     */
    private void deleteTeam(Player player) {
        final Team entryTeam = player.getScoreboard().getEntryTeam(getEntityName());
        if (entryTeam == null) {
            return;
        }

        // if glowing entity is player then restore their team is there is one
        if (entity instanceof Player target) {
            entryTeam.setColor(oldColor.getOrDefault(target, ChatColor.WHITE));
            oldColor.remove(target);
        }

        // if fake team, remove target from that team
        if (Helper.Teams.GLOWING.compareName(entryTeam.getName())) {
            entryTeam.removeEntry(getEntityName());
        }
    }

    /**
     * This creates (or gets) a glowing for specific scoreboard and applies color to it.
     * If target is a player, then store their old team to restore after glowing is over.
     */
    private Team getTeamOrCreate(Player player) {
        final Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getEntryTeam(getEntityName());

        if (team == null) {
            team = Helper.getGlowingTeam(scoreboard);
        }

        if (entity instanceof Player target) {
            oldColor.putIfAbsent(target, team.getColor());
        }
        team.setColor(this.color);
        return team;
    }

    // returns either player's name or entities UUID to use as entry in a team
    private String getEntityName() {
        return this.entity instanceof Player ? this.entity.getName() : this.entity.getUniqueId().toString();
    }

}
