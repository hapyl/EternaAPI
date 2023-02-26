package me.hapyl.spigotutils.module.reflect.glow;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.google.common.collect.Sets;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.reflect.Ticking;
import me.hapyl.spigotutils.module.util.TeamHelper;
import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Note: If you're glowing for other players, you will currently glow for yourself when other are near you.
 * Currently, IS a bug, but I've wasted so many hours trying to fix this so whatever.
 */
public class Glowing implements Ticking, GlowingListener {

    private final ProtocolManager manager = ProtocolLibrary.getProtocolManager();

    private ChatColor color;
    private int duration;
    private boolean everyoneIsListener;

    private boolean status;
    private ChatColor oldTeamColor;

    private final Set<Player> players;
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
        this.players = Sets.newHashSet();
        this.setColor(color);

        EternaPlugin.getPlugin().getGlowingManager().addGlowing(entity, this);
    }

    // static shortcuts
    public static void glow(Entity entity, ChatColor color, int duration, @Nullable Player... viewers) {
        final Glowing glowing = new Glowing(entity, color, duration);
        glowing.addPlayerOrGlobal(viewers);
        glowing.glow();
    }

    public static void glowInfinitly(Entity entity, ChatColor color, @Nullable Player... viewers) {
        glow(entity, color, Integer.MAX_VALUE, viewers);
    }

    public static void stopGlowing(Entity entity) {
        final GlowingRegistry glowingManager = EternaPlugin.getPlugin().getGlowingManager();
        glowingManager.stopGlowing(entity);
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

    protected void createPacket(boolean flag) {
        final PacketContainer packet = manager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, entity.getEntityId());

        final WrappedDataWatcher dataWatcher = WrappedDataWatcher.getEntityWatcher(entity);
        final WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
        final StructureModifier<List<WrappedDataValue>> modifier = packet.getDataValueCollectionModifier();

        final byte bitMask = dataWatcher.getByte(0);

        modifier.write(0, List.of(new WrappedDataValue(0, serializer, !flag ? bitMask : (byte) (bitMask | 0x40))));

        try {
            if (isEveryoneIsListener()) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    manager.sendServerPacket(player, packet);
                }
            }
            else {
                for (Player player : players) {
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

    public final Set<Player> getPlayers() {
        return players;
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
    public final void addPlayer(Player player) {
        this.players.add(player);
    }

    public final void addPlayer(Player... players) {
        this.players.addAll(Arrays.asList(players));
    }

    public final void addPlayer(Collection<Player> players) {
        this.players.addAll(players);
    }

    public final void addPlayerOrGlobal(Player... players) {
        if (players == null || players.length == 0) {
            setEveryoneIsListener(true);
        }
        else {
            addPlayer(players);
        }
    }

    /**
     * Removes a viewer for this glowing.
     *
     * @param player - Player.
     */
    public final void removePlayer(Player player) {
        this.players.remove(player);
    }

    /**
     * Returns true is player can see the glowing.
     *
     * @param player - Player.
     * @return true is player can see the glowing.
     */
    public final boolean isPlayer(Player player) {
        return this.players.contains(player);
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
        players.forEach(player -> getTeamOrCreate(player).setColor(this.color));
    }

    private void createTeam(boolean flag) {
        if (players.isEmpty()) {
            return;
        }

        // Create team
        players.forEach(player -> {
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

        // restore color if team is not fake
        if (oldTeamColor != null) {
            entryTeam.setColor(oldTeamColor);
            oldTeamColor = null;
        }

        // if fake team, remove target from that team
        if (TeamHelper.GLOWING.compareName(entryTeam.getName())) {
            entryTeam.removeEntry(getEntityName());
        }
    }

    /**
     * This creates (or gets) a glowing for specific scoreboard and applies color to it.
     * If target is a player, then store their old team to restore after glowing is over.
     */
    private Team getTeamOrCreate(Player player) {
        final Scoreboard scoreboard = player.getScoreboard();

        // test for existing team
        Team team = scoreboard.getEntryTeam(getEntityName());

        if (team == null) {
            team = TeamHelper.GLOWING.getTeam(scoreboard);
        }
        // store team color if using existing
        else {
            oldTeamColor = team.getColor();
        }

        team.setColor(this.color);
        return team;
    }

    // returns either player's name or entities UUID to use as entry in a team
    private String getEntityName() {
        return this.entity instanceof Player ? this.entity.getName() : this.entity.getUniqueId().toString();
    }
}
