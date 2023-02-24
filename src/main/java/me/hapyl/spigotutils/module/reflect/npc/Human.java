package me.hapyl.spigotutils.module.reflect.npc;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Human {
    void setFreezeTicks(int ticks);

    int getFreezeTicks();

    void setShoulderEntity(boolean shoulder, boolean status);

    boolean isShowingTo(Player player);

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
     * {@link HumanNPC#updateEquipment(Player...)} will reset NPC's "ghost" equipment.
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
     * Updates NPC's DataWatcher. Used after calling {@link HumanNPC#setDataWatcherByteValue(int, byte, Player...)}
     */
    void updateDataWatcher();

    /**
     * Hides display name of this NPC.
     */
    void hideDisplayName();

    Player bukkitEntity();

}
