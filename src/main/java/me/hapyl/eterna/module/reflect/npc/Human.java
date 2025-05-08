package me.hapyl.eterna.module.reflect.npc;

import me.hapyl.eterna.module.hologram.HologramFunction;
import me.hapyl.eterna.module.hologram.PlayerHologram;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Range;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public interface Human {

    /**
     * Gets the {@link NPCFormat} for this NPC.
     *
     * @return the npc format for this NPC.
     * @see NPCFormat
     */
    @Nonnull
    NPCFormat getFormat();

    /**
     * Sets the {@link NPCFormat} for this NPC.
     *
     * @param format - New format.
     */
    void setFormat(@Nonnull NPCFormat format);

    /**
     * Gets the interaction delay before a {@link Player} can click on the {@link HumanNPC} again.
     *
     * @return the interaction delay before a {@link Player} can click on the {@link HumanNPC} again.
     */
    int getInteractionDelay();

    /**
     * Sets the interaction delay before a {@link Player} can click on the {@link HumanNPC} again.
     *
     * @param interactionDelay - New delay, in ticks.
     */
    HumanNPC setInteractionDelay(@Range(from = 0, to = Integer.MAX_VALUE) int interactionDelay);

    /**
     * Sets the interaction delay before a {@link Player} can click on the {@link HumanNPC} again.
     * <p>The default implementation is as follows:</p>
     * <pre>{@code
     *     default void setInteractionDelaySec(float interactionDelaySec) {
     *         setInteractionDelay((int) (interactionDelaySec * 20));
     *     }
     * }</pre>
     *
     * @param interactionDelaySec - New delay, in seconds.
     */
    default HumanNPC setInteractionDelaySec(@Range(from = 0, to = Integer.MAX_VALUE) float interactionDelaySec) {
        return setInteractionDelay((int) (interactionDelaySec * 20));
    }

    /**
     * Returns true if NPC is shaking of cold.
     *
     * @return true if NPC is shaking of cold.
     */
    boolean isShaking();

    /**
     * Set if NPC is shaking of cold.
     *
     * @param shaking - Enable or disable shaking.
     */
    void setShaking(boolean shaking);

    /**
     * Changes NPC's should entity.
     *
     * @param shoulder - Shoulder.
     * @param status   - Enable or disable entity.
     */
    void setShoulderEntity(Shoulder shoulder, boolean status);

    /**
     * Returns true if NPC is created, false otherwise.
     *
     * @return true if NPC is created, false otherwise.
     */
    boolean isAlive();

    /**
     * Returns set of players that can see this NPC.
     *
     * @return set of players that can see this NPC.
     */
    Set<Player> getViewers();

    /**
     * Returns copy of NPC's location.
     *
     * @return copy of NPC's location.
     */
    Location getLocation();

    /**
     * Changes NPC's actual location.
     *
     * @param location - New location.
     */
    void setLocation(Location location);

    /**
     * Returns true if NPC is currently sitting.
     *
     * @return true if NPC is currently sitting, false otherwise.
     */
    boolean isSitting();

    /**
     * Sets if NPC is sitting.
     *
     * @param sitting - Is NPC sitting.
     */
    void setSitting(boolean sitting);

    /**
     * Sets the rest head position for this NPC.
     * NPC will rotate its head whenever it's not looking at a player.
     *
     * @param yaw   - Yaw.
     * @param pitch - Pitch.
     */
    void setRestPosition(float yaw, float pitch);

    /**
     * Gets the current rest head position if present; null otherwise.
     *
     * @return the current rest head position if present; null otherwise.
     */
    @Nullable
    RestPosition getRestPosition();

    /**
     * Resets the rest head position for this NPC.
     */
    void resetRestPotion();

    /**
     * Returns true if NPC can be seen by the player.
     *
     * @param player - The player.
     * @return true if NPC can be seen by the player.
     */
    boolean isShowingTo(Player player);

    /**
     * Returns true if NPC's is registered, false otherwise.
     *
     * @return true if NPC's is registered, false otherwise.
     */
    boolean exists();

    /**
     * Returns NPC's name.
     *
     * @return NPC's name.
     * @deprecated NPC names are now per-player, this is considered to be a 'default' name.
     */
    @Nonnull
    @Deprecated
    String getName();

    /**
     * Sets the NPC name.
     *
     * @param newName - New name to set.
     * @deprecated NPC names are not per-player, this is considered to be a 'default' name.
     */
    @Deprecated
    void setName(@Nullable String newName);

    /**
     * Gets the NPC name for the given {@link Player}.
     * <p>The default implementation is as follows:</p>
     * <pre>{@code
     *     @Nonnull
     *     default String getName(@Nonnull Player player) {
     *         return getName();
     *     }
     * }</pre>
     *
     * @param player - The {@link Player} to get the name for.
     * @return the NPC name for the given {@link Player}.
     */
    @Nonnull
    default String getName(@Nonnull Player player) {
        return getName();
    }

    /**
     * Returns {@code true} if this {@link Human} has a name, {@code false} otherwise.
     *
     * @return {@code true} if this {@link Human} has a name, {@code false} otherwise.
     */
    default boolean hasName() {
        return !getName().isEmpty();
    }

    /**
     * Returns maximum distance to the nearest player that NPC will look at.
     *
     * @return maximum distance to the nearest player that NPC will look at.
     */
    int getLookAtCloseDist();

    /**
     * Set the distance at which the NPC will look at the player
     *
     * @param lookAtCloseDist the distance.
     */
    HumanNPC setLookAtCloseDist(int lookAtCloseDist);

    /**
     * Changes NPC pose.
     *
     * @param pose - New pose.
     */
    HumanNPC setPose(NPCPose pose);

    /**
     * Returns NPC's pose.
     *
     * @return NPC's pose.
     */
    NPCPose getPose();

    /**
     * Gets if this NPC is showing to anyone.
     *
     * @return true if this NPC is showing to anyone; false otherwise.
     */
    boolean isShowing();

    /**
     * Changes NPC's head position to look at entity.
     *
     * @param entity - Entity to look at.
     */
    void lookAt(Entity entity);

    /**
     * Changes NPC's head position to look at location.
     *
     * @param location - Location to look at.
     */
    void lookAt(Location location);

    /**
     * Teleports the NPC to the given location.
     *
     * @param x     - X coordinate.
     * @param y     - Y coordinate.
     * @param z     - Z coordinate.
     * @param yaw   - Yaw.
     * @param pitch - Pitch.
     */
    void teleport(double x, double y, double z, float yaw, float pitch);

    /**
     * Teleports the NPC to the given location.
     *
     * @param location - Location.
     */
    void teleport(Location location);

    /**
     * Simulates movement of the  NPC to the given location.
     *
     * @param location - Location.
     * @param speed    - Speed in blocks/s.
     */
    void move(Location location, float speed);

    /**
     * Simulates movement of the NPC to the given location.
     *
     * @param x     - X coordinate.
     * @param y     - Y coordinate.
     * @param z     - Z coordinate.
     * @param speed - Speed in blocks/s.
     */
    void move(double x, double y, double z, float speed);

    void jump(double height) throws NotImplementedException;

    /**
     * Returns true if entity is presumably on ground.
     *
     * @return true if entity is presumably on ground.
     */
    boolean isOnGround();

    /**
     * Stops moving NPC.
     *
     * @return true if stopped, false if it wasn't moving.
     */
    boolean stopMoving();

    /**
     * Changes NPC's head rotation.
     * This method modifies NPCs actual location.
     *
     * @param yaw   - Yaw.
     * @param pitch - Pitch.
     */
    void setHeadRotation(float yaw, float pitch);

    /**
     * Changes NPC's head rotation.
     *
     * @param yaw - Yaw.
     */
    void setHeadRotation(float yaw);

    /**
     * Forces NPC to play swing animation with its main hand.
     */
    void swingMainHand();

    /**
     * Forces NPC to play swing animation with its offhand.
     */
    void swingOffHand();

    /**
     * Sets NPC's skin to the given texture and signature.
     * Get the texture and signature from <a href="https://mineskin.org">MineSkin</a>
     *
     * @param texture   - Texture.
     * @param signature - Signature.
     */
    HumanNPC setSkin(String texture, String signature);
    
    /**
     * Ignores NPC's equipment and sends a "ghost" item to provided players.
     * {@link Human#updateEquipment()} will reset NPC's "ghost" equipment.
     *
     * @param slot   - Slot to put items on.
     * @param item   - Item.
     * @param player - Player, who will see the change.
     */
    void setGhostItem(ItemSlot slot, ItemStack item, @Nullable Player player);

    /**
     * Changed actual equipment of the NPC, which is the same for every player.
     *
     * @param slot - Slot to put items on.
     * @param item - Item.
     */
    void setItem(ItemSlot slot, ItemStack item);

    /**
     * Updates NPC's equipment and shows it to players.
     */
    void updateEquipment();

    /**
     * Shows NPC to all online players.
     */
    void showAll();

    /**
     * Shows this NPC to players.
     *
     * @param player - Player, who NPC will be shown to.
     */
    void show(@Nonnull Player player);

    /**
     * Reloads (respawn) NPC to update its textures.
     */
    void reloadNpcData();

    /**
     * Changes NPC's actual equipment.
     *
     * @param equipment - Equipment to change to.
     */
    void setEquipment(EntityEquipment equipment);

    /**
     * Changes byte data watcher of this NPC.
     *
     * @param key   - Index.
     * @param value - Value.
     */
    void setDataWatcherByteValue(int key, byte value);

    /**
     * Returns the byte value of the data watcher.
     *
     * @param key - the key of the data watcher.
     * @return The byte value of the data watcher.
     */
    byte getDataWatcherByteValue(int key);

    /**
     * Returns the data watcher of the NPC.
     *
     * @return The data watcher of the NPC.
     */
    SynchedEntityData getDataWatcher();

    /**
     * Updates skins layers of this NPC.
     */
    void updateSkin();

    /**
     * Updates the skin of the NPC with provided Skin Parts.
     *
     * @param parts - Enabled parts.
     */
    void updateSkin(SkinPart... parts);

    /**
     * Plays provided animation to players.
     *
     * @param animation - Animation to play.
     */
    void playAnimation(NPCAnimation animation);

    /**
     * Updates NPC's DataWatcher. Used after calling {@link HumanNPC#setDataWatcherByteValue(int, byte)}
     */
    void updateDataWatcher();

    /**
     * Completely removes NPC and unregisters it.
     */
    void remove();

    /**
     * Hides NPC for players.
     */
    void hide();

    /**
     * Hides the NPC from the specified player.
     *
     * @param player - The player to hide the NPC from.
     */
    void hide(@Nonnull Player player);

    /**
     * Sets the collision of the NPC.
     *
     * @param flag - true if the NPC should collide with players.
     */
    @InvokePolicy(Policy.ANYTIME)
    HumanNPC setCollision(boolean flag);

    /**
     * Gets the NPC's minecraft ID.
     *
     * @return NPC's minecraft ID.
     */
    int getId();

    /**
     * Returns the human entity.
     *
     * @return the human entity
     */
    @Nonnull
    ServerPlayer getHuman();

    /**
     * Gets the {@link PlayerHologram} associated with this {@link Human}.
     * <p>
     * The aboveHead, NPC name, and belowHead is handled using this {@link PlayerHologram}.
     * </p>
     * <p>
     * It is recommended to use build-in {@link #setAboveHead(HologramFunction)}, and {@link #setBelowHead(HologramFunction)}
     * methods, rather than manually updating the hologram.
     * </p>
     *
     * @return the {@link PlayerHologram} associated with this NPC.
     */
    @Nonnull
    PlayerHologram getHologram();

    /**
     * Sets the text above this {@link Human}'s head.
     *
     * @param function - How to set the text.
     * @see HologramFunction
     * @see #updateHologram()
     */
    void setAboveHead(@Nullable HologramFunction function);

    /**
     * Sets the text below this {@link Human}'s head.
     *
     * @param function - How to set the text.
     * @see HologramFunction
     * @see #updateHologram()
     */
    void setBelowHead(@Nullable HologramFunction function);

    /**
     * Updates the lines of the {@link PlayerHologram}.
     * <p>Ths update is done is the specific order</p>
     * <ul>
     *     <li>The hologram lines are cleared.
     *     <li>If aboveHead is not null, it's applied.
     *     <li>If the NPC has a name, it's applied.
     *     <li>If belowHead is not null, it's applied.
     * </ul>
     *
     * @see PlayerHologram
     */
    void updateHologram();

    /**
     * Returns Bukkit entity of this NPC.
     *
     * @return Bukkit entity of this NPC.
     */
    Player bukkitEntity();

    default HumanNPC handle() {
        return (HumanNPC) this;
    }

    /**
     * Gets if this NPC name tag is dynamic.
     * <br>
     * Dynamic name tag will change its height based on NPCs state.
     *
     * @return true if this NPCs name tag is dynamic.
     */
    boolean isDynamicNameTag();

    /**
     * Returns players that can see this NPC.
     *
     * @return players that can see this NPC.
     */
    @Nonnull
    Player[] getPlayers();

    /**
     * Static access to creating NPC.
     *
     * @param location - Location to create at.
     */
    @Nonnull
    static Human create(@Nonnull Location location) {
        return HumanNPC.create(location);
    }

    /**
     * Static access to creating NPC.
     *
     * @param location - Location to create at.
     * @param name     - NPC name.
     */
    @Nonnull
    static Human create(@Nonnull Location location, @Nonnull String name) {
        return HumanNPC.create(location, name);
    }

    /**
     * Static access to creating NPC.
     *
     * @param location - Location to create at.
     * @param name     - NPC name.
     * @param skin     - Skin owner username.
     */
    @Nonnull
    static Human create(@Nonnull Location location, @Nonnull String name, @Nonnull String skin) {
        return HumanNPC.create(location, name, skin);
    }
}
