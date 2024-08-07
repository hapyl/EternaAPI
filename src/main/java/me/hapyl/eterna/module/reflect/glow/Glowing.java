package me.hapyl.eterna.module.reflect.glow;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.annotate.Super;
import me.hapyl.eterna.module.reflect.DataWatcherType;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.reflect.Ticking;
import me.hapyl.eterna.module.util.Runnables;
import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.scores.PlayerTeam;
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
        forceStop(true);
    }

    private void forceStop(boolean callOnStop) {
        status = false; // no check for status because it's force

        getManager().removeGlowing(this);

        sendPacket(false);
        createTeam(false);

        if (callOnStop) {
            this.onGlowingStop();
        }
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

    /**
     * Stops the current glowing and creates a new instance with the remaining duration.
     *
     * @return New glowing instance.
     */
    @Nonnull
    public final Glowing restart() {
        final Player player = this.player;
        final Entity entity = this.entity;
        final ChatColor color = this.color;
        final int duration = this.duration;

        forceStop(false);

        final Glowing glowing = new Glowing(player, entity, color, duration + 1) {
            @Override
            public void onGlowingTick() {
                Glowing.this.onGlowingTick();
            }

            @Override
            public void onGlowingStop() {
                Glowing.this.onGlowingStop();
            }
        };

        Runnables.runLater(glowing::glow, 1);
        return glowing;
    }

    @Nonnull
    protected Packet<?> createPacket(boolean flag) {
        final SynchedEntityData dataWatcher = Reflect.getDataWatcher(Reflect.getMinecraftEntity(entity));
        final Byte bitMask = dataWatcher.get(DataWatcherType.BYTE.get().createAccessor(0));

        return new ClientboundSetEntityDataPacket(
                entity.getEntityId(),
                List.of(new SynchedEntityData.DataValue<>(
                        0,
                        DataWatcherType.BYTE.get(),
                        !flag ? (byte) (bitMask & ~GLOWING_BIT_MASK) : (byte) (bitMask | GLOWING_BIT_MASK)
                ))
        );
    }

    protected void sendPacket(boolean flag) {
        try {
            Reflect.sendPacket(player, createPacket(flag));
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
        Reflect.sendPacket(player, ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(Reflect.getNetTeam(team), false));
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
        final PlayerTeam nmsTeam = Reflect.getNetTeam(team);

        Reflect.sendPacket(
                player,
                ClientboundSetPlayerTeamPacket.createPlayerPacket(nmsTeam, getEntityName(), ClientboundSetPlayerTeamPacket.Action.ADD)
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
     * @param player   - Player, who will see the glowing.
     * @throws IllegalArgumentException if the color is <code>not {@link ChatColor#isColor()}</code>.
     */
    public static void glow(@Nonnull Entity entity, @Nonnull ChatColor color, int duration, @Nonnull Player player) {
        glow(player, entity, color, duration);
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
     * @param entity - Entity, who will be glowing.
     * @param color  - Glowing color.
     * @param player - Player, who will see the glowing.
     * @throws IllegalArgumentException if the color is <code>not {@link ChatColor#isColor()}</code>.
     */
    public static void glowInfinitely(@Nonnull Entity entity, @Nonnull ChatColor color, @Nonnull Player player) {
        glow(player, entity, color, Integer.MAX_VALUE);
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
        return Eterna.getRegistry().glowingRegistry;
    }

    @Nonnull
    public static ChatFormatting chatColorToEnumChatFormat(@Nonnull ChatColor color) {
        return switch (color) {
            case BOLD -> ChatFormatting.BOLD;
            case GREEN -> ChatFormatting.GREEN;
            case WHITE -> ChatFormatting.WHITE;
            case RED -> ChatFormatting.RED;
            case GRAY -> ChatFormatting.GRAY;
            case DARK_RED -> ChatFormatting.DARK_RED;
            case GOLD -> ChatFormatting.GOLD;
            case YELLOW -> ChatFormatting.YELLOW;
            case AQUA -> ChatFormatting.AQUA;
            case BLUE -> ChatFormatting.BLUE;
            case DARK_AQUA -> ChatFormatting.DARK_AQUA;
            case MAGIC -> ChatFormatting.OBFUSCATED;
            case ITALIC -> ChatFormatting.ITALIC;
            case DARK_BLUE -> ChatFormatting.DARK_BLUE;
            case DARK_GRAY -> ChatFormatting.DARK_GRAY;
            case UNDERLINE -> ChatFormatting.UNDERLINE;
            case DARK_GREEN -> ChatFormatting.DARK_GREEN;
            case DARK_PURPLE -> ChatFormatting.DARK_PURPLE;
            case LIGHT_PURPLE -> ChatFormatting.LIGHT_PURPLE;
            case STRIKETHROUGH -> ChatFormatting.STRIKETHROUGH;
            case RESET -> ChatFormatting.RESET;
            default -> ChatFormatting.BLACK;
        };
    }

}
