package me.hapyl.spigotutils.module.reflect.packet;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import me.hapyl.spigotutils.module.annotate.TestedOn;
import me.hapyl.spigotutils.module.annotate.Version;
import me.hapyl.spigotutils.module.reflect.Reflect;
import me.hapyl.spigotutils.module.reflect.npc.ItemSlot;
import me.hapyl.spigotutils.module.reflect.npc.NPCAnimation;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EnumItemSlot;
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

        public static void spawnEntityLiving(@Nonnull EntityLiving entity, @Nonnull Player player) {
            Reflect.sendPacket(player, new PacketPlayOutSpawnEntity(entity, Reflect.getEntityId(entity), Reflect.getEntityBlockPosition(entity)));
        }

        public static void entityDestroy(@Nonnull Entity entity, @Nonnull Player player) {
            Reflect.destroyEntity(entity, player);
        }

        public static void entityTeleport(@Nonnull Entity entity, @Nonnull Player player) {
            Reflect.sendPacket(player, new PacketPlayOutEntityTeleport(entity));
        }

        public static void entityMetadata(@Nonnull Entity entity, @Nonnull DataWatcher dataWatcher, @Nonnull Player player) {
            Reflect.sendPacket(player, new PacketPlayOutEntityMetadata(Reflect.getEntityId(entity), dataWatcher.c()));
        }

        public static void animation(@Nonnull Entity entity, @Nonnull NPCAnimation type, @Nonnull Player player) {
            Reflect.sendPacket(player, new PacketPlayOutAnimation(entity, type.getPos()));
        }

        public static void entityEquipment(Entity entity, EntityEquipment equipment, Player player) {
            final List<Pair<EnumItemSlot, ItemStack>> list = Lists.newArrayList();

            list.add(new Pair<>(ItemSlot.HEAD.getSlot(), Reflect.bukkitItemToNMS(equipment.getHelmet())));
            list.add(new Pair<>(ItemSlot.CHEST.getSlot(), Reflect.bukkitItemToNMS(equipment.getChestplate())));
            list.add(new Pair<>(ItemSlot.LEGS.getSlot(), Reflect.bukkitItemToNMS(equipment.getLeggings())));
            list.add(new Pair<>(ItemSlot.FEET.getSlot(), Reflect.bukkitItemToNMS(equipment.getBoots())));
            list.add(new Pair<>(ItemSlot.MAINHAND.getSlot(), Reflect.bukkitItemToNMS(equipment.getItemInMainHand())));
            list.add(new Pair<>(ItemSlot.OFFHAND.getSlot(), Reflect.bukkitItemToNMS(equipment.getItemInOffHand())));

            Reflect.sendPacket(player, new PacketPlayOutEntityEquipment(Reflect.getEntityId(entity), list));
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

