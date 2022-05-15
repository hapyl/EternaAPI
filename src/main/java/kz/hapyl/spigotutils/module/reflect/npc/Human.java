package kz.hapyl.spigotutils.module.reflect.npc;

import kz.hapyl.spigotutils.module.annotate.InsuredViewers;
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

    @InsuredViewers
    HumanNPC setPose(NPCPose pose, @Nullable Player... players);

    /**
     * Changes NPC's head position to look at entity.
     *
     * @param entity  - Entity to look at.
     * @param players - Players who will see the change.
     */
    @InsuredViewers
    void lookAt(Entity entity, @Nullable Player... players);

    /**
     * Changes NPC's head position to look at location.
     *
     * @param location - Location to look at.
     * @param players  - Players who will see the change.
     */
    @InsuredViewers
    void lookAt(Location location, @Nullable Player... players);

    /**
     * Changes NPC's actual location.
     *
     * @param location - New location.
     * @param players  - Players who will see the change.
     */
    @InsuredViewers
    void setLocation(Location location, @Nullable Player... players);

    /**
     * Changes NPC's head rotation.
     *
     * @param yaw     - Yaw.
     * @param players - Players who will see the change.
     */
    @InsuredViewers
    void setHeadRotation(float yaw, @Nullable Player... players);

    /**
     * Forces NPC to play swing animation with its main hand.
     *
     * @param players - Players who will see the animation.
     */
    @InsuredViewers
    void swingMainHand(@Nullable Player... players);

    /**
     * Forces NPC to play swing animation with its offhand.
     *
     * @param players - Players who will see the animation.
     */
    @InsuredViewers
    void swingOffHand(@Nullable Player... players);

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
     * @param slot    - Slot to put items on.
     * @param item    - Item.
     * @param players - Players who will see the change.
     */
    @InsuredViewers
    void setItem(ItemSlot slot, ItemStack item, @Nullable Player... players);

    /**
     * Updates NPC's equipment and shows it to players.
     *
     * @param players - Players who will see the change.
     */
    @InsuredViewers
    void updateEquipment(@Nullable Player... players);

    /**
     * Shows this NPC to players.
     * <i>Players provided via this arguments are saved and used
     * for further use in method annotated with {@link InsuredViewers}</i>
     *
     * @param players - Players who NPC will be shown to.
     */
    void show(@Nonnull Player... players);

    /**
     * Reloads (respawn) NPC to update its textures.
     *
     * @param players - Players who will see the change.
     */
    void reloadNpcData(@Nullable Player... players);

    /**
     * Changes NPC's actual equipment.
     *
     * @param equipment - Equipment to change to.
     * @param players   - Players who will see the change.
     */
    @InsuredViewers
    void setEquipment(EntityEquipment equipment, @Nullable Player... players);

    /**
     * Hides NPC for players.
     *
     * @param players - Players who will see the change.
     */
    @InsuredViewers
    void hide(@Nullable Player... players);

    /**
     * Changes byte data watcher of this NPC.
     *
     * @param key     - Index.
     * @param value   - Value.
     * @param viewers - Player who will see the change.
     */
    @InsuredViewers
    void setDataWatcherByteValue(int key, byte value, @Nullable Player... viewers);

    /**
     * Hides this NPC's name. <i>NPC's name is actually a {@link HumanNPC#getHexName()}.</i>
     *
     * @param players - Players who will see the change.
     */
    @InsuredViewers
    void hideTabListName(@Nullable Player... players);

    /**
     * Updates skins layers of this NPC.
     *
     * @param players - Players who will see the change.
     */
    @InsuredViewers
    void updateSkin(@Nullable Player... players);

    /**
     * Plays provided animation to players.
     *
     * @param animation - Animation to play.
     * @param players   - Players who will see the animation.
     */
    @InsuredViewers
    void playAnimation(NPCAnimation animation, @Nullable Player... players);

    /**
     * Updates NPC's DataWatcher. Used after calling {@link HumanNPC#setDataWatcherByteValue(int, byte, Player...)}
     *
     * @param players - Players who will see the change.
     */
    @InsuredViewers
    void updateDataWatcher(@Nullable Player... players);

    /**
     * Hides display name of this NPC.
     *
     * @param players - Players who display name will be hidden for.
     */
    void hideDisplayName(@Nullable Player... players);
}
