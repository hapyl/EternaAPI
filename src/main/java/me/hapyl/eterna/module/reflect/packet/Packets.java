package me.hapyl.eterna.module.reflect.packet;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import me.hapyl.eterna.module.annotate.TestedOn;
import me.hapyl.eterna.module.annotate.Version;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.reflect.npc.ItemSlot;
import me.hapyl.eterna.module.reflect.npc.NPCAnimation;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * This class is a shortcut to packet creating and sending.
 */
@TestedOn(version = Version.V1_21)
public final class Packets {

    private Packets() {
        throw new IllegalStateException();
    }

    /**
     * Represents all server packets, or 'Out'.
     */
    public final static class Server {

        public static void spawnEntityLiving(@Nonnull LivingEntity entity, @Nonnull Player player) {
            Reflect.sendPacket(player, new ClientboundAddEntityPacket(entity, entity.getId(), Reflect.getEntityBlockPosition(entity)));
        }

        public static void entityDestroy(@Nonnull Entity entity, @Nonnull Player player) {
            Reflect.destroyEntity(entity, player);
        }

        public static void entityTeleport(@Nonnull Entity entity, @Nonnull Player player) {
            Reflect.sendPacket(player, new ClientboundTeleportEntityPacket(entity));
        }

        public static void entityMetadata(@Nonnull Entity entity, @Nonnull SynchedEntityData dataWatcher, @Nonnull Player player) {
            Reflect.sendPacket(player, new ClientboundSetEntityDataPacket(Reflect.getEntityId(entity), dataWatcher.getNonDefaultValues()));
        }

        public static void animation(@Nonnull Entity entity, @Nonnull NPCAnimation type, @Nonnull Player player) {
            Reflect.sendPacket(player, new ClientboundAnimatePacket(entity, type.getPos()));
        }

        public static void entityEquipment(Entity entity, EntityEquipment equipment, Player player) {
            final List<Pair<EquipmentSlot, ItemStack>> list = Lists.newArrayList();

            list.add(new Pair<>(ItemSlot.HEAD.getSlot(), Reflect.bukkitItemToNMS(equipment.getHelmet())));
            list.add(new Pair<>(ItemSlot.CHEST.getSlot(), Reflect.bukkitItemToNMS(equipment.getChestplate())));
            list.add(new Pair<>(ItemSlot.LEGS.getSlot(), Reflect.bukkitItemToNMS(equipment.getLeggings())));
            list.add(new Pair<>(ItemSlot.FEET.getSlot(), Reflect.bukkitItemToNMS(equipment.getBoots())));
            list.add(new Pair<>(ItemSlot.MAINHAND.getSlot(), Reflect.bukkitItemToNMS(equipment.getItemInMainHand())));
            list.add(new Pair<>(ItemSlot.OFFHAND.getSlot(), Reflect.bukkitItemToNMS(equipment.getItemInOffHand())));

            Reflect.sendPacket(player, new ClientboundSetEquipmentPacket(Reflect.getEntityId(entity), list));
        }

    }

    /**
     * Represents all client packets, or 'In'.
     */
    public static class Client {

        public static void clientPacketsNotYetImplemented() {
            throw new NotImplementedException("client packets not yet implemented");
        }

        static {
        }
    }


}

