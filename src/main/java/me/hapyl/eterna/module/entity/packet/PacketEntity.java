package me.hapyl.eterna.module.entity.packet;

import com.google.common.collect.Sets;
import me.hapyl.eterna.module.reflect.DataWatcherType;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.util.BukkitWrapper;
import me.hapyl.eterna.module.util.TeamHelper;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Objects;
import java.util.Set;

public class PacketEntity<T extends Entity> implements IPacketEntity, BukkitWrapper<org.bukkit.entity.Entity> {

    public final int entityId;
    public final T entity;

    private final Set<Player> players;

    public PacketEntity(@Nonnull T entity, @Nonnull Location location) {
        this.entity = entity;
        this.players = Sets.newHashSet();
        this.entityId = Reflect.getEntityId(entity);

        Reflect.setEntityLocation(entity, location);
    }

    /**
     * Gets the bukkit entity of the packet entity.
     *
     * @return the bukkit entity.
     * @implNote Implementation may override this method to statically cast the entity.
     * <pre>{@code
     * @Nonnull
     * @Override
     * public final BlockDisplay bukkit() {
     *      return (BlockDisplay) super.bukkit();
     * }
     * }</pre>
     */
    @Nonnull
    @Override
    @OverridingMethodsMustInvokeSuper
    public org.bukkit.entity.Entity bukkit() {
        return entity.getBukkitEntity();
    }

    @Override
    @Nonnull
    public T getEntity() {
        return entity;
    }

    @Override
    public boolean isShowingTo(@Nonnull Player player) {
        return players.contains(player);
    }

    @Override
    public void show(@Nonnull Player player) {
        players.add(player);
        Reflect.createEntity(entity, player);
    }

    @Override
    public void hide(@Nonnull Player player) {
        players.remove(player);
        hide0(player);
    }

    @Override
    public void destroy() {
        players.forEach(this::hide0);
        players.clear();
    }

    @Override
    public void setVisible(boolean visibility) {
        final byte bitMask = getDataWatcherValue(DataWatcherType.BYTE, 0, (byte) 0);
        setDataWatcherValue(DataWatcherType.BYTE, 0, (byte) (visibility ? (bitMask & ~0x20) : (bitMask | 0x20)));
    }

    @Override
    public void setCollision(boolean collision) {
        players.forEach(player -> {
            final TeamHelper fakeEntity = TeamHelper.FAKE_ENTITY;
            final Team team = fakeEntity.getTeam(player.getScoreboard());
            final String uuid = Reflect.getScoreboardEntityName(entity);

            if (collision) {
                team.removeEntry(uuid);
            }
            else {
                team.addEntry(uuid);
            }
        });
    }

    @Override
    public void setSilent(boolean silent) {
        setDataWatcherValue(DataWatcherType.BOOL, 4, silent);
    }

    @Override
    public void setGravity(boolean gravity) {
        setDataWatcherValue(DataWatcherType.BOOL, 5, !gravity);
    }

    @Override
    public void setMarker() {
        setSilent(true);
        setGravity(false);
        setVisible(false);
        setCollision(false);
    }

    @Override
    public void teleport(@Nonnull Location location) {
        Reflect.setEntityLocation(entity, location);
        players.forEach(player -> Reflect.updateEntityLocation(entity, player));
    }

    @Override
    public int getId() {
        return entityId;
    }

    protected void updateMetadata() {
        players.forEach(player -> Reflect.updateMetadata(entity, player));
    }

    protected <D> void setDataWatcherValue(@Nonnull DataWatcherType<D> type, int key, D value) {
        final SynchedEntityData dataWatcher = getDataWatcher();

        Reflect.setDataWatcherValue0(dataWatcher, type.get().createAccessor(key), value);
        updateMetadata();
    }

    @Nullable
    protected <D> D getDataWatcherValue(@Nonnull DataWatcherType<D> type, int key) {
        return getDataWatcherValue(type, key, null);
    }

    @Nonnull
    protected <D> D getDataWatcherValue(@Nonnull DataWatcherType<D> type, int key, D def) {
        final D value = Reflect.getDataWatcherValue(entity, type, key);

        return value == null ? def : value;
    }

    @Nonnull
    protected SynchedEntityData getDataWatcher() {
        return Reflect.getDataWatcher(entity);
    }

    private void hide0(Player player) {
        Reflect.destroyEntity(entity, player);
    }

    @Nonnull
    protected static ServerLevel getWorld(@Nonnull Location location) {
        return Reflect.getMinecraftWorld(Objects.requireNonNull(location.getWorld(), "World must be loaded."));
    }

}
