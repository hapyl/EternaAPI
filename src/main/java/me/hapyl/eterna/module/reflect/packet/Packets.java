package me.hapyl.eterna.module.reflect.packet;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import me.hapyl.eterna.module.annotate.TestedOn;
import me.hapyl.eterna.module.annotate.UtilityClass;
import me.hapyl.eterna.module.annotate.Version;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.reflect.npc.ItemSlot;
import me.hapyl.eterna.module.reflect.npc.NPCAnimation;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.item.ItemStack;
import org.bukkit.inventory.EntityEquipment;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

/**
 * A static utility class that allows creating and sending packets easily.
 */
@TestedOn(version = Version.V1_21_3)
@UtilityClass
public final class Packets {

    private Packets() {
        UtilityClass.Validator.throwIt();
    }

    @Nonnull
    private static <P extends Packet<?>> IPacket<P> make(@Nonnull P packet) {
        return IPacket.of(packet);
    }

    /**
     * Represents all client bound packets, or 'Out'.
     */
    @UtilityClass
    public static final class Clientbound {

        private Clientbound() {
            UtilityClass.Validator.throwIt();
        }

        @Nonnull
        public static IPacket<ClientboundAddEntityPacket> spawnEntity(@Nonnull LivingEntity entity) {
            return make(new ClientboundAddEntityPacket(entity, entity.getId(), Reflect.getEntityBlockPosition(entity)));
        }

        @Nonnull
        public static IPacket<ClientboundRemoveEntitiesPacket> destroyEntity(@Nonnull Entity entity) {
            return make(new ClientboundRemoveEntitiesPacket(entity.getId()));
        }

        @Nonnull
        public static IPacket<ClientboundTeleportEntityPacket> teleportEntity(@Nonnull Entity entity) {
            return make(ClientboundTeleportEntityPacket.teleport(
                    entity.getId(),
                    PositionMoveRotation.of(entity),
                    Set.of(),
                    entity.onGround
            ));
        }

        @Nonnull
        public static IPacket<ClientboundSetEntityDataPacket> entityMetadata(@Nonnull Entity entity, @Nonnull SynchedEntityData dataWatcher) {
            return make(new ClientboundSetEntityDataPacket(Reflect.getEntityId(entity), dataWatcher.getNonDefaultValues()));
        }

        @Nonnull
        public static IPacket<ClientboundAnimatePacket> entityAnimation(@Nonnull Entity entity, @Nonnull NPCAnimation type) {
            return make(new ClientboundAnimatePacket(entity, type.getPos()));
        }

        @Nonnull
        public static IPacket<ClientboundSetEquipmentPacket> entityEquipment(@Nonnull Entity entity, @Nonnull EntityEquipment equipment) {
            final List<Pair<EquipmentSlot, ItemStack>> list = Lists.newArrayList();

            list.add(new Pair<>(ItemSlot.HEAD.getSlot(), Reflect.bukkitItemToNMS(equipment.getHelmet())));
            list.add(new Pair<>(ItemSlot.CHEST.getSlot(), Reflect.bukkitItemToNMS(equipment.getChestplate())));
            list.add(new Pair<>(ItemSlot.LEGS.getSlot(), Reflect.bukkitItemToNMS(equipment.getLeggings())));
            list.add(new Pair<>(ItemSlot.FEET.getSlot(), Reflect.bukkitItemToNMS(equipment.getBoots())));
            list.add(new Pair<>(ItemSlot.MAINHAND.getSlot(), Reflect.bukkitItemToNMS(equipment.getItemInMainHand())));
            list.add(new Pair<>(ItemSlot.OFFHAND.getSlot(), Reflect.bukkitItemToNMS(equipment.getItemInOffHand())));

            return make(new ClientboundSetEquipmentPacket(Reflect.getEntityId(entity), list));
        }

    }

    /**
     * Represents all server bound packets, or 'In'.
     *
     * @deprecated Not implemented yet.
     */
    @Deprecated
    public static final class Serverbound {
    }


}

