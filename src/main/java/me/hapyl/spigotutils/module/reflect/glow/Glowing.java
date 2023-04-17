package me.hapyl.spigotutils.module.reflect.glow;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.annotate.Super;
import me.hapyl.spigotutils.module.reflect.Reflect;
import me.hapyl.spigotutils.module.reflect.Ticking;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam;
import net.minecraft.world.scores.ScoreboardTeam;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

/**
 * Allows to create per-player glowing.
 */
public class Glowing implements Ticking, GlowingListener {

    private final ProtocolManager manager = ProtocolLibrary.getProtocolManager();
    private final Player player; // only one player per glowing is now allowed
    private final Entity entity;
    private ChatColor color;
    private int duration;
    private boolean status;
    private Team team;
    @Nullable
    private Team realTeam;

    /**
     * Creates a glowing object.
     *
     * @param entity   - Entity to glow.
     * @param duration - Glow duration.
     */
    public Glowing(Player player, Entity entity, int duration) {
        this(player, entity, ChatColor.WHITE, duration);
    }

    /**
     * Creates a glowing object.
     *
     * @param entity   - Entity to glow.
     * @param color    - Glowing color.
     * @param duration - Glow duration.
     */
    @Super
    public Glowing(Player player, Entity entity, ChatColor color, int duration) {
        this.player = player;
        this.entity = entity;
        this.duration = duration;
        this.status = false;
        this.setColor(color);

        EternaPlugin.getPlugin().getGlowingManager().addGlowing(this);
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
     * Returns entity that is glowing.
     *
     * @return entity that is glowing.
     */
    public final Entity getEntity() {
        return entity;
    }

    /**
     * Returns current color of glowing.
     *
     * @return current color of glowing.
     */
    public final ChatColor getColor() {
        return color;
    }

    /**
     * Changes glowing color.
     *
     * @param color - New Color.
     */
    public final void setColor(ChatColor color) {
        this.color = color;

        if (isGlowing()) {
            updateTeamColor();
        }
    }

    /**
     * Returns player who entity is glowing for.
     *
     * @return player who entity is glowing for.
     */
    public final Player getPlayer() {
        return player;
    }

    @Override
    public final void tick() {
        if (!status) {
            return;
        }

        boolean lastTick = --duration <= 0;

        if (lastTick) {
            forceStop();
            return;
        }

        this.onGlowingTick();
    }

    /**
     * Returns true is player can see the glowing.
     *
     * @param player - Player.
     * @return true is player can see the glowing.
     */
    public final boolean isPlayer(Player player) {
        return this.player == player;
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
        if (status) {
            return;
        }

        status = true;
        realTeam = player.getScoreboard().getEntryTeam(getEntityName()); // save real team for later
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
        status = false; // no check for status because it's force

        createPacket(false);
        createTeam(false);
        this.onGlowingStop();
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
            manager.sendServerPacket(player, packet);
        } catch (Exception e) {
            EternaPlugin.getPlugin().getLogger().severe("Could not parse glowing!");
            e.printStackTrace();
        }
    }

    protected void updateTeamColor() {
        team.setColor(color);
        Reflect.sendPacket(player, PacketPlayOutScoreboardTeam.a(getTeamAsNms(), false));
    }

    private ScoreboardTeam getTeamAsNms() {
        return Reflect.getNetTeam(player.getScoreboard(), team.getName());
    }

    private void createTeam(boolean flag) {
        final Scoreboard scoreboard = player.getScoreboard();

        // Create team
        if (flag) {
            team = scoreboard.registerNewTeam("glowing_" + UUID.randomUUID());

            if (realTeam != null) {
                for (Team.Option value : Team.Option.values()) {
                    team.setOption(value, realTeam.getOption(value));
                }

                team.setPrefix(realTeam.getPrefix());
                team.setSuffix(realTeam.getSuffix());
                team.setCanSeeFriendlyInvisibles(realTeam.canSeeFriendlyInvisibles());
            }

            Reflect.sendPacket(PacketPlayOutScoreboardTeam.a(getTeamAsNms(), true)); // create team
            Reflect.sendPacket(PacketPlayOutScoreboardTeam.a(getTeamAsNms())); // setup team
            Reflect.sendPacket(
                    player,
                    PacketPlayOutScoreboardTeam.a(getTeamAsNms(), getEntityName(), PacketPlayOutScoreboardTeam.a.a)
            ); // add to the team

            updateTeamColor();
        }
        else {
            // Update entity's real team
            final Team realTeam = scoreboard.getEntryTeam(getEntityName());

            if (realTeam != null) {
                realTeam.addEntry(getEntityName());
            }

            // First add to real team then unregister?
            if (team != null && scoreboard.getTeam(team.getName()) != null) {
                team.unregister();
            }
        }

    }

    private String getEntityName() {
        return this.entity instanceof Player ? this.entity.getName() : this.entity.getUniqueId().toString();
    }

    // static members
    public static void glow(Entity entity, ChatColor color, int duration, @Nonnull Player... viewers) {
        for (Player viewer : viewers) {
            glow(viewer, entity, color, duration);
        }
    }

    public static void glow(Player player, Entity entity, ChatColor color, int duration) {
        new Glowing(player, entity, color, duration).glow();
    }

    public static void glowInfinitly(Entity entity, ChatColor color, @Nonnull Player... viewers) {
        for (Player viewer : viewers) {
            new Glowing(viewer, entity, color, Integer.MAX_VALUE).glow();
        }
    }

    public static void stopGlowing(Player player, Entity entity) {
        EternaPlugin.getPlugin().getGlowingManager().stopGlowing(player, entity);
    }

    public static void stopGlowing(Entity entity) {
        EternaPlugin.getPlugin().getGlowingManager().stopGlowing(entity);
    }
}
