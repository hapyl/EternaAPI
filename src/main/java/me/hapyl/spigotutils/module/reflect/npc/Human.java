package me.hapyl.spigotutils.module.reflect.npc;

import me.hapyl.spigotutils.module.reflect.npc.entry.NPCEntry;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public interface Human {

    /**
     * Static access to creating NPC.
     *
     * @param location - Location to create at.
     */
    static Human create(Location location) {
        return HumanNPC.create(location);
    }

    /**
     * Static access to creating NPC.
     *
     * @param location - Location to create at.
     * @param name     - NPC name. Leave blank for empty name, or null for CLICK name.
     */
    static Human create(Location location, String name) {
        return HumanNPC.create(location, name);
    }

    /**
     * Static access to creating NPC.
     *
     * @param location - Location to create at.
     * @param name     - NPC name. Leave blank for empty name, or null for CLICK name.
     * @param skin     - Skin owner username.
     */
    static Human create(Location location, String name, String skin) {
        return HumanNPC.create(location, name, skin);
    }

    /**
     * Set if NPC is shaking of cold.
     *
     * @param shaking - Enable or disable shaking.
     */
    void setShaking(boolean shaking);

    /**
     * Returns true if NPC is shaking of cold.
     *
     * @return true if NPC is shaking of cold.
     */
    boolean isShaking();

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
     * Returns true if NPC can be seen by the player.
     *
     * @param player - The player.
     * @return true if NPC can be seen by the player.
     */
    boolean isShowingTo(Player player);

    /**
     * Returns true if NPC is persistent, false otherwise.
     * Persistent NPC's will not be removed when far away.
     *
     * @return true if NPC is persistent, false otherwise.
     */
    boolean isPersistent();

    /**
     * Sets if NPC should be persistent.
     * Persistent NPC's will not be removed when far away.
     *
     * @param persistent - Enable or disable persistence.
     */
    void setPersistent(boolean persistent);

    /**
     * Returns maximum distance from NPC's location where it's rendered.
     * <i>Note that is {@link Human#isPersistent()} this will be ignored.</i>
     *
     * @return maximum distance from NPC's location where it's rendered.
     */
    int getFarAwayDist();

    /**
     * Changes maximum distance from NPC's location where it's rendered.
     * <i>Note that is {@link Human#isPersistent()} this will be ignored.</i>
     *
     * @param farAwayDist - New distance.
     */
    HumanNPC setFarAwayDist(int farAwayDist);

    /**
     * Adds entry to NPC's dialog.
     *
     * @param entry - Entry to add.
     */
    HumanNPC addEntry(NPCEntry entry);

    /**
     * Adds string entry to NPC's dialog.
     *
     * @param string    - String to add.
     * @param delayNext - Delay before next entry.
     */
    HumanNPC addDialogLine(String string, int delayNext);

    /**
     * Adds string entry to NPC's dialog.
     *
     * @param string - String to add.
     */
    HumanNPC addDialogLine(String string);

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
     */
    String getName();

    /**
     * Forces NPC to stop talking.
     */
    void stopTalking();

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
     * Adds a line of text above the NPC's head.
     *
     * @param text - String.
     */
    HumanNPC addTextAboveHead(String text);

    /**
     * Removes a line of text above the NPC's head.
     *
     * @param index - Index of the line.
     * @throws IndexOutOfBoundsException - If the index is out of bounds.
     */
    HumanNPC removeTextAboveHead(int index);

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
     * Changes NPC's actual location.
     *
     * @param location - New location.
     */
    void setLocation(Location location);

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
     * @param slot    - Slot to put items on.
     * @param item    - Item.
     * @param players - Players who will see the change.
     */
    void setGhostItem(ItemSlot slot, ItemStack item, @Nullable Player... players);

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
     * @param players - Players who NPC will be shown to.
     */
    void show(@Nonnull Player... players);

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
     * Hides NPC for players.
     */
    void hide();

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
    DataWatcher getDataWatcher();

    /**
     * Hides this NPC's name. <i>NPC's name is actually a {@link HumanNPC#getHexName()}.</i>
     */
    void hideTabListName();

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
     * Completely removes NPC.
     */
    void remove();

    /**
     * Hides the NPC from the specified players.
     *
     * @param mappings - Remap the NPC to the players.
     * @param players  - The players to hide the NPC from.
     */
    void hide(boolean mappings, Player... players);

    /**
     * Sets the collision of the NPC.
     *
     * @param flag - true if the NPC should collide with players.
     */
    @InvokePolicy(Policy.ANYTIME)
    HumanNPC setCollision(boolean flag);

    /**
     * Returns the human entity.
     *
     * @return the human entity
     */
    EntityPlayer getHuman();

    /**
     * Hides display name of this NPC.
     */
    void hideDisplayName();

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
     * Returns players that can see this NPC.
     *
     * @return players that can see this NPC.
     */
    Player[] getPlayers();
}
