package me.hapyl.eterna.module.reflect.glowing;

import com.google.common.base.Preconditions;
import me.hapyl.eterna.module.reflect.DataWatcherType;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.util.Removable;
import me.hapyl.eterna.module.util.Ticking;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

/**
 * Represents a {@link GlowingInstance} of a specific {@link Entity} and {@link Player}.
 */
public class GlowingInstance implements Ticking, Removable {
    
    private final Player player;
    private final Entity entity;
    
    @Nonnull private final Scoreboard playerScoreboard;
    @Nullable private final Team existingTeam;
    
    private final net.minecraft.world.scores.PlayerTeam nmsTeam;
    
    @Nonnull private GlowingColor color;
    private int duration;
    
    GlowingInstance(@Nonnull Glowing glowing, @Nonnull Player player, @Nonnull Entity entity, @Nonnull GlowingColor color, int duration) {
        this.player = player;
        this.entity = entity;
        this.color = color;
        this.duration = duration;
        
        // Prepare nms
        this.nmsTeam = glowing.scoreboard.addPlayerTeam(UUID.randomUUID().toString());
        this.nmsTeam.setColor(color.nmsColor);
        
        this.playerScoreboard = player.getScoreboard();
        this.existingTeam = playerScoreboard.getEntryTeam(scoreboardName());
        
        // If the entity was in the actual team, copy everything from it except color
        if (existingTeam != null) {
            this.nmsTeam.setAllowFriendlyFire(existingTeam.allowFriendlyFire());
            this.nmsTeam.setSeeFriendlyInvisibles(existingTeam.canSeeFriendlyInvisibles());
            
            this.nmsTeam.setCollisionRule(NmsTeamOption.of(existingTeam.getOption(Team.Option.COLLISION_RULE)).collision());
            this.nmsTeam.setDeathMessageVisibility(NmsTeamOption.of(existingTeam.getOption(Team.Option.DEATH_MESSAGE_VISIBILITY)).visibility());
            this.nmsTeam.setNameTagVisibility(NmsTeamOption.of(existingTeam.getOption(Team.Option.NAME_TAG_VISIBILITY)).visibility());
        }
        
        final ClientboundSetPlayerTeamPacket packetAddTeam = ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(nmsTeam, true);
        final ClientboundSetPlayerTeamPacket packetAddEntity = ClientboundSetPlayerTeamPacket.createPlayerPacket(nmsTeam, scoreboardName(), ClientboundSetPlayerTeamPacket.Action.ADD);
        
        Reflect.sendPacket(player, packetAddTeam);
        Reflect.sendPacket(player, packetAddEntity);
        
        sendGlowingPacket(true);
    }
    
    /**
     * Gets the player of this instance.
     *
     * @return the player of this instance.
     */
    @Nonnull
    public Player player() {
        return player;
    }
    
    /**
     * Gets the entity of this instance.
     *
     * @return the entity of this instance.
     */
    @Nonnull
    public Entity entity() {
        return entity;
    }
    
    /**
     * Sets the new outline color.
     *
     * @param color - The new color.
     */
    public void setColor(@Nonnull GlowingColor color) {
        if (this.color == color) {
            return;
        }
        
        this.color = color;
        updateColor();
    }
    
    /**
     * Sets the new duration.
     *
     * @param duration - The new duration.
     * @throws IllegalArgumentException for negative duration unless {@link Glowing#INFINITE_DURATION}.
     */
    public void setDuration(int duration) {
        Preconditions.checkArgument(duration >= Glowing.INFINITE_DURATION, "The duration must be positive or %s".formatted(Glowing.INFINITE_DURATION));
        this.duration = duration;
    }
    
    @Override
    public void tick() {
        if (duration == Glowing.INFINITE_DURATION) {
            return;
        }
        
        duration--;
    }
    
    /**
     * Stops the glowing, restoring the real teams.
     */
    @Override
    public void remove() {
        sendGlowingPacket(false);
        
        // Remove fake team
        final ClientboundSetPlayerTeamPacket packetRemoveTeam = ClientboundSetPlayerTeamPacket.createRemovePacket(nmsTeam);
        Reflect.sendPacket(player, packetRemoveTeam);
        
        // Update the actual team
        final org.bukkit.scoreboard.Scoreboard scoreboard = player.getScoreboard();
        final Team team = scoreboard.getEntryTeam(scoreboardName());
        
        if (team != null) {
            Reflect.sendPacket(player, ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(Reflect.getNetTeam(team), true));
        }
    }
    
    /**
     * Gets whether this instance should be stopped.
     * <p>
     *     The default condition for stop is:
     *     <ul>
     *         <li>Player's scoreboard or entity's team has changed.
     *         <li>Entity has died.
     *         <li>Duration isn't {@link Glowing#INFINITE_DURATION} and has reached 0.
     *     </ul>
     * </p>
     *
     * @return {@code true} if the instance should be stopped, {@code false} otherwise.
     */
    @Override
    public boolean shouldRemove() {
        final Scoreboard currentScoreboard = player.getScoreboard();
        final Team currentTeam = currentScoreboard.getEntryTeam(scoreboardName());
        
        // Changing scoreboard or team throws internal error and is not supported
        if (playerScoreboard != currentScoreboard || (existingTeam != null && currentTeam != null && !existingTeam.equals(currentTeam))) {
            return true;
        }
        
        return entity.isDead() || duration != Glowing.INFINITE_DURATION && duration <= 0;
    }
    
    protected void sendGlowingPacket(boolean flag) {
        final SynchedEntityData dataWatcher = Reflect.getDataWatcher(Reflect.getMinecraftEntity(entity));
        final Byte bitMask = dataWatcher.get(DataWatcherType.BYTE.get().createAccessor(0));
        
        final ClientboundSetEntityDataPacket packet = new ClientboundSetEntityDataPacket(
                entity.getEntityId(),
                List.of(new SynchedEntityData.DataValue<>(
                        0,
                        DataWatcherType.BYTE.get(),
                        !flag ? (byte) (bitMask & ~Glowing.BITMASK) : (byte) (bitMask | Glowing.BITMASK)
                ))
        );
        
        Reflect.sendPacket(player, packet);
    }
    
    private String scoreboardName() {
        return entity instanceof Player ? entity.getName() : entity.getUniqueId().toString();
    }
    
    private void updateColor() {
        nmsTeam.setColor(color.nmsColor);
        
        Reflect.sendPacket(player, ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(nmsTeam, false));
    }
    
}
