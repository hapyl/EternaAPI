package me.hapyl.spigotutils.module.reflect.glow;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import me.hapyl.spigotutils.EternaLogger;
import me.hapyl.spigotutils.module.annotate.Super;
import me.hapyl.spigotutils.module.reflect.Reflect;
import me.hapyl.spigotutils.module.reflect.Ticking;
import me.hapyl.spigotutils.module.util.Runnables;
import me.hapyl.spigotutils.registry.EternaRegistry;
import net.minecraft.EnumChatFormat;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam;
import net.minecraft.world.scores.ScoreboardTeam;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Allows to create per-player glowing.
 */
public class Glowing implements Ticking, GlowingListener {

    public static final byte GLOWING_BIT_MASK = 0x40;

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
     * Creates a new {@link Glowing} instance.
     *
     * @param player   - Player, who will see the glowing.
     * @param entity   - The glowing entity.
     * @param duration - Glowing duration.
     */
    public Glowing(@Nonnull Player player, @Nonnull Entity entity, int duration) {
        this(player, entity, ChatColor.WHITE, duration);
    }

    /**
     * Creates a new {@link Glowing} instance.
     *
     * @param player   - Player, who will see the glowing.
     * @param entity   - The glowing entity.
     * @param color    - Glowing color.
     * @param duration - Glowing duration.
     * @throws IllegalArgumentException if the color is <code>not {@link ChatColor#isColor()}</code>.
     */
    @Super
    public Glowing(@Nonnull Player player, @Nonnull Entity entity, @Nonnull ChatColor color, int duration) {
        this.player = player;
        this.entity = entity;
        this.duration = duration;
        this.status = false;
        this.setColor(color);

        getManager().addGlowing(this);
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
    @Nonnull
    public final Entity getEntity() {
        return entity;
    }

    /**
     * Returns current color of glowing.
     *
     * @return current color of glowing.
     */
    @Nonnull
    public final ChatColor getColor() {
        return color;
    }

    /**
     * Changes glowing color.
     *
     * @param color - New Color.
     * @throws IllegalArgumentException if the color is <code>not {@link ChatColor#isColor()}</code>.
     */
    public final void setColor(@Nonnull ChatColor color) {
        if (!color.isColor()) {
            throw new IllegalArgumentException("Glowing color must be a color, not formatter!");
        }

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
    @Nonnull
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

        // This will keep data from an actual player's team,
        // so if anything charged in the real team, the changes
        // are applied to the glowing team.
        // In truth, this system is wack, but I'm going to blame it
        // on having literal 0 official dev tools.
        updateTeam();
        onGlowingTick();
    }

    /**
     * Returns true if player can see the glowing.
     *
     * @param player - Player.
     * @return true is player can see the glowing.
     */
    public final boolean isPlayer(@Nonnull Player player) {
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
     * Starts the glowing.
     */
    public final void glow() {
        this.start();
    }

    /**
     * Starts the glowing.
     */
    public final void start() {
        if (status) {
            return;
        }

        status = true;
        realTeam = player.getScoreboard().getEntryTeam(getEntityName()); // save real team for later

        sendPacket(true);
        createTeam(true);

        onGlowingStart();
    }

    /**
     * Returns true if the entity is glowing.
     *
     * @return true if the entity is glowing.
     */
    public final boolean isGlowing() {
        return status && duration > 0;
    }

    public final boolean shouldGlow() {
        return !entity.isDead() && player.isOnline();
    }

    /**
     * Stops the glowing.
     */
    public final void stop() {
        this.duration = 0;
    }

    /**
     * This calls all onStop methods instead of setting duration to 0.
     */
    public final void forceStop() {
        status = false; // no check for status because it's force

        sendPacket(false);
        createTeam(false);

        getManager().removeGlowing(this);

        this.onGlowingStop();
    }

    /**
     * Calls a force glowing packet update.
     */
    public final void forceUpdate() {
        if (!isGlowing()) {
            return;
        }

        Runnables.runLater(() -> sendPacket(true), 1);
    }

    @Nonnull
    protected PacketContainer createPacket(boolean flag) {
        final PacketContainer packet = manager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, entity.getEntityId());

        final WrappedDataWatcher dataWatcher = WrappedDataWatcher.getEntityWatcher(entity);
        final WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
        final StructureModifier<List<WrappedDataValue>> modifier = packet.getDataValueCollectionModifier();

        final byte bitMask = dataWatcher.getByte(0);

        modifier.write(
                0,
                List.of(new WrappedDataValue(
                        0,
                        serializer,
                        !flag ? (byte) (bitMask & ~GLOWING_BIT_MASK) : (byte) (bitMask | GLOWING_BIT_MASK)
                ))
        );

        return packet;
    }

    protected void sendPacket(boolean flag) {
        try {
            manager.sendServerPacket(player, createPacket(flag));
        } catch (Exception e) {
            EternaLogger.exception(e);
        }
    }

    protected void updateTeamColor() {
        if (!player.isOnline()) {
            forceStop();
            return;
        }

        team.setColor(color);
        Reflect.sendPacket(player, PacketPlayOutScoreboardTeam.a(Reflect.getNetTeam(team), false));
    }

    private void updateTeam() {
        if (realTeam == null) {
            return;
        }

        for (Team.Option value : Team.Option.values()) {
            team.setOption(value, realTeam.getOption(value));
        }

        team.setPrefix(realTeam.getPrefix());
        team.setSuffix(realTeam.getSuffix());
        team.setCanSeeFriendlyInvisibles(realTeam.canSeeFriendlyInvisibles());
        team.setAllowFriendlyFire(realTeam.allowFriendlyFire());
        team.setDisplayName(realTeam.getDisplayName());
    }

    private void createTeam(boolean flag) {
        final Scoreboard scoreboard = player.getScoreboard();

        // Create team
        if (flag) {
            team = scoreboard.registerNewTeam("glowing_" + UUID.randomUUID());
            updateTeam();

            addToTeam(team);
            updateTeamColor();
        }
        // Delete team
        else {
            // Update entity's real team
            final Team currentTeam = scoreboard.getEntryTeam(getEntityName());

            // Entity has an ACTUAL team, add them to it
            if (currentTeam != null) {
                addToTeam(currentTeam);
            }

            if (team != null && scoreboard.getTeam(team.getName()) != null) {
                team.unregister();
            }
        }

    }

    private void addToTeam(Team team) {
        final ScoreboardTeam nmsTeam = Reflect.getNetTeam(team);

        if (nmsTeam == null) {
            return;
        }

        Reflect.sendPacket(
                player,
                PacketPlayOutScoreboardTeam.a(nmsTeam, getEntityName(), PacketPlayOutScoreboardTeam.a.a)
        );
    }

    private String getEntityName() {
        return this.entity instanceof Player ? this.entity.getName() : this.entity.getUniqueId().toString();
    }

    /**
     * Starts the glowing for the entity.
     *
     * @param player   - Player, who will see the glowing.
     * @param entity   - Entity, who will be glowing.
     * @param color    - Glowing color.
     * @param duration - Duration of the glowing.
     * @throws IllegalArgumentException if the color is <code>not {@link ChatColor#isColor()}</code>.
     */
    public static void glow(@Nonnull Player player, @Nonnull Entity entity, @Nonnull ChatColor color, int duration) {
        new Glowing(player, entity, color, duration).glow();
    }

    /**
     * Starts the glowing for the entity.
     *
     * @param entity   - Entity, who will be glowing.
     * @param color    - Glowing color.
     * @param duration - Duration of the glowing.
     * @param viewers  - Players, who will see the glowing.
     * @throws IllegalArgumentException if the color is <code>not {@link ChatColor#isColor()}</code>.
     */
    public static void glow(@Nonnull Entity entity, @Nonnull ChatColor color, int duration, @Nonnull Player... viewers) {
        for (Player viewer : viewers) {
            glow(viewer, entity, color, duration);
        }
    }

    /**
     * Starts the glowing for the entity.
     *
     * @param entity   - Entity, who will be glowing.
     * @param color    - Glowing color.
     * @param duration - Duration of the glowing.
     * @param viewers  - Players, who will see the glowing.
     * @throws IllegalArgumentException if the color is <code>not {@link ChatColor#isColor()}</code>.
     */
    public static void glow(@Nonnull Entity entity, @Nonnull ChatColor color, int duration, @Nonnull Collection<Player> viewers) {
        for (Player viewer : viewers) {
            glow(viewer, entity, color, duration);
        }
    }

    /**
     * Starts the glowing for the entity indefinitely.
     *
     * @param entity  - Entity, who will be glowing.
     * @param color   - Glowing color.
     * @param viewers - Players, who will see the glowing.
     * @throws IllegalArgumentException if the color is <code>not {@link ChatColor#isColor()}</code>.
     */
    public static void glowInfinitely(@Nonnull Entity entity, @Nonnull ChatColor color, @Nonnull Player... viewers) {
        for (Player viewer : viewers) {
            glow(viewer, entity, color, Integer.MAX_VALUE);
        }
    }

    /**
     * Starts the glowing for the entity indefinitely.
     *
     * @param entity  - Entity, who will be glowing.
     * @param color   - Glowing color.
     * @param viewers - Players, who will see the glowing.
     * @throws IllegalArgumentException if the color is <code>not {@link ChatColor#isColor()}</code>.
     */
    public static void glowInfinitely(@Nonnull Entity entity, @Nonnull ChatColor color, @Nonnull Collection<Player> viewers) {
        for (Player viewer : viewers) {
            glow(viewer, entity, color, Integer.MAX_VALUE);
        }
    }

    /**
     * Stops the glowing for the given player of the given entity.
     *
     * @param player - Player.
     * @param entity - Entity.
     */
    public static void stopGlowing(@Nonnull Player player, @Nonnull Entity entity) {
        getManager().stopGlowing(player, entity);
    }

    /**
     * Stops all the glowing for the given entity.
     *
     * @param entity - Entity.
     */
    public static void stopGlowing(@Nonnull Entity entity) {
        getManager().stopGlowing(entity);
    }

    /**
     * Gets the {@link GlowingRegistry}.
     *
     * @return the glowing manager.
     */
    @Nonnull
    public static GlowingRegistry getManager() {
        return EternaRegistry.getGlowingManager();
    }

    @Nonnull
    public static EnumChatFormat chatColorToEnumChatFormat(@Nonnull ChatColor color) {
        return switch (color) {
            case BOLD -> EnumChatFormat.r;
            case GREEN -> EnumChatFormat.k;
            case WHITE -> EnumChatFormat.p;
            case RED -> EnumChatFormat.m;
            case GRAY -> EnumChatFormat.h;
            case DARK_RED -> EnumChatFormat.e;
            case GOLD -> EnumChatFormat.g;
            case YELLOW -> EnumChatFormat.o;
            case AQUA -> EnumChatFormat.l;
            case BLUE -> EnumChatFormat.j;
            case DARK_AQUA -> EnumChatFormat.d;
            case MAGIC -> EnumChatFormat.q;
            case ITALIC -> EnumChatFormat.u;
            case DARK_BLUE -> EnumChatFormat.b;
            case DARK_GRAY -> EnumChatFormat.i;
            case UNDERLINE -> EnumChatFormat.t;
            case DARK_GREEN -> EnumChatFormat.c;
            case DARK_PURPLE -> EnumChatFormat.f;
            case LIGHT_PURPLE -> EnumChatFormat.n;
            case STRIKETHROUGH -> EnumChatFormat.s;
            case RESET -> EnumChatFormat.v;
            default -> EnumChatFormat.a;
        };
    }

}
