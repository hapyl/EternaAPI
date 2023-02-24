package me.hapyl.spigotutils.module.reflect.npc;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
     * Sets the remaining freeze ticks for NPC.
     *
     * @param ticks - Ticks.
     */
    void setFreezeTicks(int ticks);

    /**
     * Returns remaining freeze ticks.
     *
     * @return remaining freeze ticks.
     */
    int getFreezeTicks();

    /**
     * Changes NPC's should entity.
     *
     * @param shoulder - Shoulder.
     * @param status   - Enable or disable entity.
     */
    void setShoulderEntity(Shoulder shoulder, boolean status);

    /**
     * Returns true if NPC can be seen by the player.
     *
     * @param player - The player.
     * @return true if NPC can be seen by the player.
     */
    boolean isShowingTo(Player player);

    /**
     * Changes NPC pose.
     *
     * @param pose - New pose.
     */
    HumanNPC setPose(NPCPose pose);

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
     * Hides this NPC's name. <i>NPC's name is actually a {@link HumanNPC#getHexName()}.</i>
     */
    void hideTabListName();

    /**
     * Updates skins layers of this NPC.
     */
    void updateSkin();

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
     * Hides display name of this NPC.
     */
    void hideDisplayName();

    Player bukkitEntity();

}
