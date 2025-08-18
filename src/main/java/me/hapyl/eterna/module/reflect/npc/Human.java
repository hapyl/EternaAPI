package me.hapyl.eterna.module.reflect.npc;

import me.hapyl.eterna.module.entity.Showable;
import me.hapyl.eterna.module.hologram.Hologram;
import me.hapyl.eterna.module.hologram.LineSupplier;
import me.hapyl.eterna.module.locaiton.Located;
import me.hapyl.eterna.module.player.PlayerSkin;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Range;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents an interface for a {@link HumanNPC}.
 */
public interface Human extends Showable, Located {
    
    /**
     * Gets the current {@link NPCFormat}.
     *
     * @return the current {@link NPCFormat}
     * @see NPCFormat
     */
    @Nonnull
    NPCFormat getFormat();
    
    /**
     * Sets the {@link NPCFormat}.
     *
     * @param format - The new {@link NPCFormat}.
     */
    void setFormat(@Nonnull NPCFormat format);
    
    /**
     * Gets the interaction delay, in ticks.
     *
     * @return the interaction delay, in ticks.
     */
    int getInteractionDelay();
    
    /**
     * Sets the interaction delay, in ticks.
     *
     * @param interactionDelay - The new interaction delay.
     */
    void setInteractionDelay(@Range(from = 0, to = Integer.MAX_VALUE) int interactionDelay);
    
    /**
     * sets the interaction delay, in seconds.
     *
     * @param interactionDelaySec - The new interaction delay.
     */
    default void setInteractionDelaySec(@Range(from = 0, to = Integer.MAX_VALUE) float interactionDelaySec) {
        setInteractionDelay((int) (interactionDelaySec * 20));
    }
    
    /**
     * Gets whether this {@link Human} is shaking because of cold.
     *
     * @return whether this {@link Human} is shaking because of cold.
     */
    boolean isShaking();
    
    /**
     * Sets whether this {@link Human} should be shaking because of cold.
     *
     * @param shaking - {@code true} to start shaking; {@code false} to stop shaking.
     */
    void setShaking(boolean shaking);
    
    /**
     * Gets a copy of this {@link Human} location.
     *
     * @return a copy of this {@link Human} location.
     */
    @Nonnull
    @Override
    Location getLocation();
    
    /**
     * Gets whether this {@link Human} is sitting.
     *
     * @return {@code true} if sitting, {@code false} otherwise.
     */
    boolean isSitting();
    
    /**
     * Sets whether this {@link Human} should be sitting.
     *
     * @param sitting - {@code true} to start sitting; {@code false} to stop sitting.
     */
    void setSitting(boolean sitting);
    
    /**
     * Sets the rest position of this {@link Human}.
     * <p>Rest position defines the {@code yaw} and {@code pitch} of the {@link Human} head, while it isn't looking at the closest player.</p>
     *
     * @param yaw   - The rest yaw.
     * @param pitch - The rest pitch.
     */
    void setRestPosition(float yaw, float pitch);
    
    /**
     * Gets the rest position of this {@link Human}.
     *
     * @return the rest position of this {@link Human}.
     */
    @Nullable
    RestPosition getRestPosition();
    
    /**
     * Resets the rest position of this {@link Human}.
     */
    void resetRestPotion();
    
    /**
     * Gets whether this {@link Human} exists.
     * <p>By existing, it means that the {@link Human} has not been {@link #remove()}.</p>
     *
     * @return {@code true} if this {@link Human} exists; {@code false} otherwise.
     */
    boolean exists();
    
    /**
     * Gets the <i>default</i> name of this {@link Human}.
     *
     * @return the <i>default</i> name of this {@link Human}
     * @see #getName(Player)
     * @deprecated The names are per-player, and this is only used as backwards compatibility.
     */
    @Nonnull
    @Deprecated
    String getName();
    
    /**
     * Sets the <i>default</i> name of this {@link Human}.
     *
     * @param newName - The new default name.
     * @see #getName(Player)
     * @deprecated The names are per-player, and this is only used as backwards compatibility.
     */
    @Deprecated
    void setName(@Nullable String newName);
    
    /**
     * Gets whether this {@link Human} has a <i>default</i> name.
     *
     * @return {@code true} if this {@link Human} has a <i>default</i> name; {@code false} otherwise.
     */
    default boolean hasName() {
        return !getName().isEmpty();
    }
    
    /**
     * Gets the name of this {@link Human} for the given {@link Player}.
     *
     * @param player - The player to get the name for.
     * @return the name of this {@link Human} for the given {@link Player}.
     * @implNote It is expected for the implementation to override this method in order to archive per-player names.
     */
    @Nonnull
    default String getName(@Nonnull Player player) {
        return getName();
    }
    
    /**
     * Returns the distance, in blocks, that a {@link Player} must be within for this {@link Human} to start looking at them.
     *
     * @return the required distance in blocks
     */
    double getLookAtCloseDist();
    
    /**
     * Sets the distance, in blocks, that a {@link Player} must be within for this {@link Human} to start looking at them.
     *
     * @param lookAtCloseDist - The new distance.
     */
    void setLookAtCloseDist(double lookAtCloseDist);
    
    /**
     * Sets the pose of this {@link Human}.
     *
     * @param pose - The new pose.
     */
    void setPose(@Nonnull NPCPose pose);
    
    /**
     * Gets the current pose of this {@link Human}.
     *
     * @return the current pose of this {@link Human}.
     */
    @Nonnull
    NPCPose getPose();
    
    /**
     * Makes this {@link Human} look towards the given {@link Location}.
     *
     * @param location - The location to look at.
     */
    void lookAt(@Nonnull Location location);
    
    /**
     * Makes this {@link Human} look towards the given {@link Entity}.
     *
     * @param entity - The entity to look at.
     */
    default void lookAt(@Nonnull Entity entity) {
        lookAt(entity.getLocation());
    }
    
    /**
     * Teleports this {@link Human} to the desired {@link Location}.
     * <p>The provided location will be defensively copied.</p>
     *
     * @param location - The location to teleport to.
     */
    void teleport(@Nonnull Location location);
    
    /**
     * Teleports this {@link Human} to the desired coordinates.
     *
     * @param x     - The desired {@code X} coordinate.
     * @param y     - The desired {@code Y} coordinate.
     * @param z     - The desired {@code Z} coordinate.
     * @param yaw   - The desired {@code yaw}.
     * @param pitch - The desired {@code pitch}.
     */
    default void teleport(double x, double y, double z, float yaw, float pitch) {
        teleport(new Location(getWorld(), x, y, z, yaw, pitch));
    }
    
    /**
     * Gets whether this {@link Human} is standing on the ground.
     *
     * @return {@code true} if this {@link Human} is standing on the ground; {@code false} otherwise.
     */
    boolean isOnGround();
    
    /**
     * Sets the head rotation of this {@link Human}.
     *
     * @param yaw   - The desired {@code yaw}.
     * @param pitch - The desired {@code pitch}.
     */
    void setHeadRotation(float yaw, float pitch);
    
    /**
     * Sets the head rotation of this {@link Human}.
     *
     * @param yaw - The desired {@code yaw}.
     */
    void setHeadRotation(float yaw);
    
    /**
     * Makes this {@link Human} swing their hand.
     */
    void swingMainHand();
    
    /**
     * Makes this {@link Human} swing their offhand.
     */
    void swingOffHand();
    
    /**
     * Sets the skin of this {@link Human}.
     *
     * @param skin - The new skin.
     */
    void setSkin(@Nonnull PlayerSkin skin);
    
    /**
     * Sets the skin of this {@link Human}.
     *
     * @param texture   - The texture of the skin.
     * @param signature - The signature of the skin.
     */
    default void setSkin(@Nonnull String texture, @Nonnull String signature) {
        setSkin(PlayerSkin.of(texture, signature));
    }
    
    /**
     * Updates the skin with all the default parts.
     */
    void updateSkin();
    
    /**
     * Updates the skin with the given {@link SkinPart}.
     *
     * @param parts - The parts of skin to update.
     */
    void updateSkin(@Nonnull SkinPart... parts);
    
    /**
     * Sends a <i>ghost</i> item change to the given {@link Player}.
     * <p>This will ignore the current equipment of this {@link Human}, but will not affect it in any way.</p>
     *
     * @param player - The player to will see the change.
     * @param slot   - The slot to change the item at.
     * @param item   - The item to change; or {@code null} to remove item.
     */
    void sendItemChange(@Nonnull Player player, @Nonnull ItemSlot slot, @Nullable ItemStack item);
    
    /**
     * Changes the item on a given {@link ItemSlot}.
     *
     * @param slot - The slot to change the item at.
     * @param item - The item to change to.
     */
    void setItem(@Nonnull ItemSlot slot, @Nullable ItemStack item);
    
    /**
     * Changes this {@link Human} equipment.
     *
     * @param equipment - The new equipment.
     */
    void setEquipment(@Nonnull EntityEquipment equipment);
    
    /**
     * Updates this {@link Human} equipment.
     * <p>This is automatically called on {@link #setEquipment(EntityEquipment)}, but may be used to <i>synchronize</i>
     * equipment across players, especially after using {@link #sendItemChange(Player, ItemSlot, ItemStack)}.</p>
     */
    void updateEquipment();
    
    /**
     * Shows this {@link Human} for the given player.
     *
     * @param player - The player for whom this entity should be shown.
     */
    @Override
    void show(@Nonnull Player player);
    
    /**
     * Hides this {@link Human} for this given player.
     *
     * @param player - The player for whom this entity should be hidden.
     */
    @Override
    void hide(@Nonnull Player player);
    
    /**
     * Gets whether this {@link Human} is shown for the given player.
     *
     * @param player - The player to check.
     * @return {@code true} of ths {@link Human} is shown for the given player.
     */
    @Override
    boolean isShowingTo(@Nonnull Player player);
    
    /**
     * Reloads the {@link Human}, by hidding and showing it again for all players who can see it.
     */
    void reloadNpcData();
    
    /**
     * Sets the {@link SynchedEntityData} byte value.
     * <p>This is an advanced operation, see <a href="https://minecraft.wiki/w/Java_Edition_protocol/Entity_metadata">Entity Metadata</a> on wiki for help.</p>
     *
     * @param index - The index of the value.
     * @param value - The value.
     */
    void setDataWatcherByteValue(int index, byte value);
    
    /**
     * Gets the {@link SynchedEntityData} byte value.
     *
     * @param key - The index of the value.
     * @return the {@code byte} value at the given index.
     */
    byte getDataWatcherByteValue(int key);
    
    /**
     * Gets the {@link SynchedEntityData} of this {@link Human}.
     *
     * @return the {@link SynchedEntityData} of this {@link Human}.
     */
    @Nonnull
    SynchedEntityData getDataWatcher();
    
    /**
     * Updates this {@link Human} metadata to all players who it's shown for.
     */
    void updateDataWatcher();
    
    /**
     * Plays a given animation.
     *
     * @param animation - The animation to play.
     */
    void playAnimation(@Nonnull NPCAnimation animation);
    
    /**
     * Completely removes this {@link Human} and unregisters it.
     * <p>Is it prohibited to continue using this {@link Human} instance after removing it, since it can lead to exceptions and unexpected behaviours.</p>
     */
    void remove();
    
    /**
     * Gets the visibility of this {@link Human} for the given player; or {@code null} if the {@link Human} isn't shown for the player.
     *
     * @param player - The player to check.
     * @return the visibility.
     * @see Visibility
     */
    @Nullable
    Visibility visibility(@Nonnull Player player);
    
    /**
     * Hides this {@link Human}, but does <b>not</b> unregister it, for all players who it's shown for.
     */
    void hideAll();
    
    /**
     * Sets the collision of this {@link Human}.
     *
     * @param flag - {@code true} to enable collision; {@code false} to disable collision.
     */
    void setCollision(boolean flag);
    
    /**
     * Gets the minecraft protocol id for this {@link Human}.
     *
     * @return the minecraft protocol id for this {@link Human}.
     */
    int getId();
    
    /**
     * Gets the {@code nms} instance of this {@link Human}.
     *
     * @return the {@code nms} instance of this {@link Human}.
     */
    @Nonnull
    ServerPlayer getHuman();
    
    /**
     * Gets the bukkit wrapped {@link Player} of this {@link Human}.
     * <p>Note that most bukkit-related methods may not work.</p>
     *
     * @return the bukkit wrapped {@link Player} of this {@link Human}.
     */
    @Nonnull
    Player bukkitEntity();
    
    /**
     * Gets the hologram of this {@link Human}.
     * <p>The hologram is used to display text about this {@link Human}.</p>
     *
     * @return the hologram of this {@link Human}.
     */
    @Nonnull
    Hologram getHologram();
    
    /**
     * Sets the above text of this {@link Human} name.
     *
     * @param aboveHead - The text to set above name.
     */
    void setAboveHead(@Nullable LineSupplier aboveHead);
    
    /**
     * Sets the below text of this {@link Human} name tag.
     *
     * @param belowHead - The text to set below name tag.
     */
    void setBelowHead(@Nullable LineSupplier belowHead);
    
    /**
     * Forcefully updates the hologram text.
     */
    void updateHologram();
    
    /**
     * Gets whether this {@link Human} has a <i>dynamic</i> name tag.
     * <p>Dynamic tag will change the {@code Y} offset based on {@link Human} position.</p>
     *
     * @return {@code true} if this {@link Human} has <i>dynamic</i> name tag; {@code false} otherwise.
     */
    boolean isDynamicNameTag();
    
}
