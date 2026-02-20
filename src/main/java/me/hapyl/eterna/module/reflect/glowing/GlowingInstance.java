package me.hapyl.eterna.module.reflect.glowing;

import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.reflect.team.PacketTeam;
import me.hapyl.eterna.module.reflect.team.PacketTeamColor;
import me.hapyl.eterna.module.reflect.team.PacketTeamSyncer;
import me.hapyl.eterna.module.util.Removable;
import me.hapyl.eterna.module.util.Ticking;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Represents a {@link GlowingInstance} responsible for handling player-entity data.
 */
public final class GlowingInstance implements Ticking, Removable {
    
    static final byte BITMASK = 0x40;
    
    private final Player player;
    private final Entity entity;
    private final PacketTeam team;
    
    @NotNull private PacketTeamColor color;
    private int duration;
    
    GlowingInstance(@NotNull Player player, @NotNull Entity entity, @NotNull PacketTeamColor color, int duration) {
        this.player = player;
        this.entity = entity;
        this.color = color;
        this.duration = duration;
        
        // Prepare nms
        this.team = new PacketTeam(UUID.randomUUID().toString(), player.getScoreboard().getEntryTeam(scoreboardName()));
        this.team.create(player);
        this.team.color(player, color);
        this.team.entry(player, scoreboardName());
        
        this.sendGlowingPacket(true);
    }
    
    /**
     * Gets the {@link Player} this {@link GlowingInstance} belongs to.
     *
     * @return the player this glowing instance belongs to.
     */
    @NotNull
    public Player player() {
        return player;
    }
    
    /**
     * Gets the {@link Entity} that is glowing.
     *
     * @return the entity that is glowing.
     */
    @NotNull
    public Entity entity() {
        return entity;
    }
    
    /**
     * Sets the color of this {@link GlowingInstance}.
     *
     * @param color - The color to set.
     */
    public void setColor(@NotNull PacketTeamColor color) {
        if (this.color == color) {
            return;
        }
        
        this.color = color;
        updateColor();
    }
    
    /**
     * Sets the duration of this {@link GlowingInstance}.
     *
     * @param duration - The duration to set.
     */
    public void setDuration(final int duration) {
        if (duration < Glowing.INFINITE_DURATION) {
            throw new IllegalArgumentException("Duration must be positive or %s!".formatted(Glowing.INFINITE_DURATION));
        }
        
        this.duration = duration;
    }
    
    /**
     * Ticks this {@link GlowingInstance}.
     */
    @Override
    public void tick() {
        // Synchronize from the existing team, so it updates everything except the color.
        // Needed mostly for prefix/suffix updates if the player is glowing, because we're overriding the values otherwise
        team.synchronize(player, PacketTeamSyncer.NO_COLOR);
        
        if (duration == Glowing.INFINITE_DURATION) {
            return;
        }
        
        duration--;
    }
    
    /**
     * Removes this {@link GlowingInstance} and stops the {@link Entity} glowing.
     */
    @ApiStatus.Internal
    @Override
    public void remove() {
        this.sendGlowingPacket(false);
        
        // Remove fake team
        team.destroy(player);
        
        // Update the actual team
        final org.bukkit.scoreboard.Scoreboard scoreboard = player.getScoreboard();
        final Team team = scoreboard.getEntryTeam(scoreboardName());
        
        if (team != null) {
            Reflect.sendPacket(player, ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(Reflect.getHandle(team), true));
        }
    }
    
    /**
     * Gets whether this {@link GlowingInstance} should be removed, either because the entity has died or the duration ended.
     *
     * @return {@code true} if this glowing instance should be removed; {@code false} otherwise.
     */
    @Override
    public boolean shouldRemove() {
        return entity.isDead() || duration != Glowing.INFINITE_DURATION && duration <= 0;
    }
    
    @ApiStatus.Internal
    void sendGlowingPacket(boolean flag) {
        final SynchedEntityData dataWatcher = Reflect.getHandle(entity).getEntityData();
        final byte existingMask = dataWatcher.get(EntityDataSerializers.BYTE.createAccessor(0));
        
        final ClientboundSetEntityDataPacket packet = new ClientboundSetEntityDataPacket(
                entity.getEntityId(),
                List.of(new SynchedEntityData.DataValue<>(
                        0,
                        EntityDataSerializers.BYTE,
                        flag ? (byte) (existingMask | BITMASK) : (byte) (existingMask & ~BITMASK)
                ))
        );
        
        Reflect.sendPacket(player, packet);
    }
    
    @NotNull
    private String scoreboardName() {
        return entity instanceof Player ? entity.getName() : entity.getUniqueId().toString();
    }
    
    private void updateColor() {
        team.color(player, color);
    }
    
}
