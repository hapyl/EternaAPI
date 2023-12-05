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

import java.util.List;

/**
 * This class is a shortcut to packet creating and sending.
 */
@TestedOn(version = Version.V1_20_2)
public final class Packets {

    private Packets() {
        throw new IllegalStateException();
    }

    /**
     * Represents all server packets, or 'Out'.
     */
    public final static class Server {

        public static void spawnEntityLiving(EntityLiving entity, Player... players) {
            Reflect.sendPacket(new PacketPlayOutSpawnEntity(entity), players);
        }

        public static void entityDestroy(Entity entity, Player... players) {
            Reflect.destroyEntity(entity, players);
        }

        public static void entityTeleport(Entity entity, Player... players) {
            Reflect.sendPacket(new PacketPlayOutEntityTeleport(entity), players);
        }

        public static void entityMetadata(Entity entity, DataWatcher dataWatcher, Player... players) {
            Reflect.sendPacket(new PacketPlayOutEntityMetadata(Reflect.getEntityId(entity), dataWatcher.c()), players);
        }

        public static void animation(Entity entity, NPCAnimation type, Player... players) {
            Reflect.sendPacket(new PacketPlayOutAnimation(entity, type.getPos()), players);
        }

        public static void entityEquipment(Entity entity, EntityEquipment equipment, Player... players) {
            final List<Pair<EnumItemSlot, ItemStack>> list = Lists.newArrayList();

            list.add(new Pair<>(ItemSlot.HEAD.getSlot(), Reflect.bukkitItemToNMS(equipment.getHelmet())));
            list.add(new Pair<>(ItemSlot.CHEST.getSlot(), Reflect.bukkitItemToNMS(equipment.getChestplate())));
            list.add(new Pair<>(ItemSlot.LEGS.getSlot(), Reflect.bukkitItemToNMS(equipment.getLeggings())));
            list.add(new Pair<>(ItemSlot.FEET.getSlot(), Reflect.bukkitItemToNMS(equipment.getBoots())));
            list.add(new Pair<>(ItemSlot.MAINHAND.getSlot(), Reflect.bukkitItemToNMS(equipment.getItemInMainHand())));
            list.add(new Pair<>(ItemSlot.OFFHAND.getSlot(), Reflect.bukkitItemToNMS(equipment.getItemInOffHand())));

            Reflect.sendPacket(new PacketPlayOutEntityEquipment(Reflect.getEntityId(entity), list), players);
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

