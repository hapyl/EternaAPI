package me.hapyl.eterna.module.reflect.team;

import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.builtin.Debuggable;
import me.hapyl.eterna.module.annotate.EventLike;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.util.SupportsColorFormatting;
import me.hapyl.eterna.module.util.TrackedValue;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Represents a scoreboard {@link Team}, implemented using packets, rather than the API.
 * <p>This means the changes are <b>only</b> visual and does not reflect any actual team.
 * <p>Note that because this game is garbage, some stuff like glowing color is client-side and based on teams, so changing the packet team will break glowing color and etc.
 */
@SuppressWarnings("deprecation" /* honestly paper can just suck my balls I ain't switching to adventure until you make it work for nms */)
public class PacketTeam implements Debuggable {
    
    private static final Scoreboard dummyScoreboard;
    private static final Method cachedFromStringOrNull;
    
    static {
        dummyScoreboard = new Scoreboard();
        
        // Cache the method bukkit uses to parse string -> nms
        try {
            // FIXME @May 25, 2025 (xanyjl) -> When paper changes this fucking 2001 java 1 stupid shit just call the method directly
            final Class<?> clazz = Class.forName("org.bukkit.craftbukkit.util.CraftChatMessage");
            
            cachedFromStringOrNull = clazz.getMethod("fromStringOrNull", String.class);
        }
        catch (ClassNotFoundException | NoSuchMethodException e) {
            throw EternaLogger.exception(e);
        }
    }
    
    protected final String name;
    protected final PlayerTeam team;
    
    @Nullable private final TeamOptionTracker teamOptionTracker;
    
    /**
     * Creates a new packet team with the given name.
     *
     * @param name - The name of the team.
     */
    public PacketTeam(@Nonnull String name) {
        this(name, null);
    }
    
    /**
     * Creates a new packet team with the given name.
     *
     * @param name         - The name of the team.
     * @param existingTeam - The existing team; if passed, all options are copied from the existing team.
     */
    public PacketTeam(@Nonnull String name, @Nullable Team existingTeam) {
        this.name = name;
        this.team = new PlayerTeam(dummyScoreboard, name);
        this.teamOptionTracker = existingTeam != null ? new TeamOptionTracker(existingTeam) : null;
        
        // Copy options if existing team exists
        copyOptionsFromExistingTeam(PacketTeamSyncer.DEFAULT, true);
        
        // Call onCreate
        onCreate();
    }
    
    /**
     * Gets the dummy scoreboard this team belongs to.
     *
     * @return the dummy scoreboard this team belongs to.
     */
    @Nonnull
    public Scoreboard scoreboard() {
        return dummyScoreboard;
    }
    
    /**
     * Gets the name of this packet team.
     *
     * @return the name of this packet team.
     */
    @Nonnull
    public String name() {
        return name;
    }
    
    /**
     * Creates the team for the given player.
     *
     * @param player - The player to create the team for.
     */
    public void create(@Nonnull Player player) {
        Reflect.sendPacket(player, ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, true));
    }
    
    /**
     * Destroys the team for the given player.
     *
     * @param player - The player to destroy the team for.
     */
    public void destroy(@Nonnull Player player) {
        Reflect.sendPacket(player, ClientboundSetPlayerTeamPacket.createRemovePacket(team));
    }
    
    /**
     * Adds the given entry to the team.
     *
     * @param player - The player to add the entry for.
     * @param entry  - The entry to add.
     *               <br>
     *               For {@link Player}s, it's their {@link Player#getName()}.
     *               For other {@link Entity}s it's their {@link UUID} in {@link String} form.
     */
    public void entry(@Nonnull Player player, @Nonnull String entry) {
        Reflect.sendPacket(player, ClientboundSetPlayerTeamPacket.createPlayerPacket(team, entry, ClientboundSetPlayerTeamPacket.Action.ADD));
    }
    
    /**
     * Sets the prefix for the given player.
     *
     * @param player - The player to set the prefix for.
     * @param prefix - The prefix to set.
     */
    public void prefix(@Nonnull Player player, @SupportsColorFormatting @Nonnull String prefix) {
        update(player, team -> team.setPlayerPrefix(componentFromString(prefix)));
    }
    
    /**
     * Sets the suffix for the given player.
     *
     * @param player - The player to set the suffix for.
     * @param suffix - The suffix to set.
     */
    public void suffix(@Nonnull Player player, @SupportsColorFormatting @Nonnull String suffix) {
        update(player, team -> team.setPlayerSuffix(componentFromString(suffix)));
    }
    
    /**
     * Sets the color for the given player.
     *
     * @param player - The player to set the color for.
     * @param color  - The color to set.
     */
    public void color(@Nonnull Player player, @Nonnull ChatColor color) {
        update(player, team -> team.setColor(ChatFormatting.getByCode(color.getChar())));
    }
    
    /**
     * Sets the option for the team.
     *
     * @param player - The player to set the option for.
     * @param option - The option to set.
     * @param status - The new option status to set.
     */
    public void option(@Nonnull Player player, @Nonnull Team.Option option, @Nonnull Team.OptionStatus status) {
        update(
                player, team -> {
                    final PacketTeamOption packetOption = PacketTeamOption.of(status);
                    
                    switch (option) {
                        case NAME_TAG_VISIBILITY -> team.setNameTagVisibility(packetOption.visibility());
                        case DEATH_MESSAGE_VISIBILITY -> team.setDeathMessageVisibility(packetOption.visibility());
                        case COLLISION_RULE -> team.setCollisionRule(packetOption.collision());
                    }
                }
        );
    }
    
    /**
     * Synchronizes options from the existing team (if exists) according to the given {@link PacketTeamSyncer}.
     *
     * @param player - The player to synchronize for.
     * @param syncer - The syncer.
     */
    public void synchronize(@Nonnull Player player, @Nonnull PacketTeamSyncer syncer) {
        if (teamOptionTracker == null) {
            return;
        }
        
        // Don't send stray packets
        if (copyOptionsFromExistingTeam(syncer, false)) {
            Reflect.sendPacket(player, ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, false));
        }
    }
    
    @Nonnull
    @Override
    public String toDebugString() {
        return "PacketTeam{%s}".formatted(team.pack());
    }
    
    /**
     * An event-like method that is called after the instance of {@link PacketTeam} is created.
     */
    @EventLike
    protected void onCreate() {
    }
    
    private boolean copyOptionsFromExistingTeam(PacketTeamSyncer syncer, boolean force) {
        if (teamOptionTracker == null) {
            return false;
        }
        
        boolean changed = false;
        
        changed |= applyIfChanged(syncer.friendlyFire(), teamOptionTracker.friendlyFire, team::setAllowFriendlyFire, force);
        changed |= applyIfChanged(syncer.friendlyInvisibles(), teamOptionTracker.friendlyInvisibles, team::setSeeFriendlyInvisibles, force);
        changed |= applyIfChanged(syncer.collisionRule(), teamOptionTracker.collisionRule, team::setCollisionRule, force);
        changed |= applyIfChanged(syncer.deathMessageVisibility(), teamOptionTracker.deathMessageVisibility, team::setDeathMessageVisibility, force);
        changed |= applyIfChanged(syncer.nameTagVisibility(), teamOptionTracker.nameTagVisibility, team::setNameTagVisibility, force);
        changed |= applyIfChanged(syncer.prefix(), teamOptionTracker.prefix, team::setPlayerPrefix, force);
        changed |= applyIfChanged(syncer.suffix(), teamOptionTracker.suffix, team::setPlayerSuffix, force);
        changed |= applyIfChanged(syncer.color(), teamOptionTracker.color, team::setColor, force);
        
        return changed;
    }
    
    private void update(Player player, Consumer<PlayerTeam> consumer) {
        consumer.accept(team);
        Reflect.sendPacket(player, ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, false));
    }
    
    private <C> boolean applyIfChanged(boolean condition, TrackedValue<?, C> tracker, Consumer<C> setter, boolean force) {
        if (condition && (force || tracker.hasChanged())) {
            setter.accept(tracker.get());
            return true;
        }
        
        return false;
    }
    
    private static Component componentFromString(String string) {
        try {
            return (Component) cachedFromStringOrNull.invoke(null, Chat.color(string));
        }
        catch (Exception e) {
            throw EternaLogger.exception(e);
        }
    }
    
    private static class TeamOptionTracker {
        
        private final Team team;
        
        private final TrackedValue<Boolean, Boolean> friendlyFire;
        private final TrackedValue<Boolean, Boolean> friendlyInvisibles;
        private final TrackedValue<Team.OptionStatus, net.minecraft.world.scores.Team.CollisionRule> collisionRule;
        private final TrackedValue<Team.OptionStatus, net.minecraft.world.scores.Team.Visibility> deathMessageVisibility;
        private final TrackedValue<Team.OptionStatus, net.minecraft.world.scores.Team.Visibility> nameTagVisibility;
        private final TrackedValue<String, Component> prefix;
        private final TrackedValue<String, Component> suffix;
        private final TrackedValue<ChatColor, ChatFormatting> color;
        
        TeamOptionTracker(@Nonnull Team team) {
            this.team = team;
            
            this.friendlyFire = TrackedValue.of(team::allowFriendlyFire);
            this.friendlyInvisibles = TrackedValue.of(team::canSeeFriendlyInvisibles);
            this.collisionRule = TrackedValue.of(() -> team.getOption(Team.Option.COLLISION_RULE), collision -> PacketTeamOption.of(collision).collision());
            this.deathMessageVisibility = TrackedValue.of(() -> team.getOption(Team.Option.DEATH_MESSAGE_VISIBILITY), collision -> PacketTeamOption.of(collision).visibility());
            this.nameTagVisibility = TrackedValue.of(() -> team.getOption(Team.Option.NAME_TAG_VISIBILITY), collision -> PacketTeamOption.of(collision).visibility());
            this.prefix = TrackedValue.of(team::getPrefix, PacketTeam::componentFromString);
            this.suffix = TrackedValue.of(team::getSuffix, PacketTeam::componentFromString);
            this.color = TrackedValue.of(team::getColor, color -> ChatFormatting.getByCode(color.getChar()));
        }
    }
}
