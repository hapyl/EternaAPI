package me.hapyl.spigotutils.module.reflect.packet;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import me.hapyl.spigotutils.module.annotate.TestedNMS;
import me.hapyl.spigotutils.module.reflect.Reflect;
import me.hapyl.spigotutils.module.reflect.npc.ItemSlot;
import me.hapyl.spigotutils.module.reflect.npc.NPCAnimation;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;

import java.util.List;

/**
 * This class is a shortcut to packet creating and sending.
 */
@TestedNMS(version = "1.19.4")
public final class Packets {

    private Packets() {
        throw new IllegalStateException();
    }

    /**
     * Represents all server packets, or 'Out'.
     */
    public final static class Server {

        public static void spawnEntityLiving(LivingEntity entity, Player... players) {
            Reflect.createEntity(entity, players);
        }

        public static void entityDestroy(Entity entity, Player... players) {
            Reflect.destroyEntity(entity, players);
        }

        public static void entityTeleport(Entity entity, Player... players) {
            Reflect.sendPacket(new ClientboundTeleportEntityPacket(entity), players);
        }

        public static void entityMetadata(Entity entity, SynchedEntityData dataWatcher, Player... players) {
            Reflect.updateMetadata(entity, dataWatcher, players);
        }

        public static void animation(Entity entity, NPCAnimation type, Player... players) {
            Reflect.sendPacket(new ClientboundAnimatePacket(entity, type.getPos()), players);
        }

        public static void entityEquipment(Entity entity, EntityEquipment equipment, Player... players) {
            final List<Pair<EquipmentSlot, ItemStack>> list = Lists.newArrayList();

            list.add(new Pair<>(ItemSlot.HEAD.getSlot(), Reflect.bukkitItemToNMS(equipment.getHelmet())));
            list.add(new Pair<>(ItemSlot.CHEST.getSlot(), Reflect.bukkitItemToNMS(equipment.getChestplate())));
            list.add(new Pair<>(ItemSlot.LEGS.getSlot(), Reflect.bukkitItemToNMS(equipment.getLeggings())));
            list.add(new Pair<>(ItemSlot.FEET.getSlot(), Reflect.bukkitItemToNMS(equipment.getBoots())));
            list.add(new Pair<>(ItemSlot.MAINHAND.getSlot(), Reflect.bukkitItemToNMS(equipment.getItemInMainHand())));
            list.add(new Pair<>(ItemSlot.OFFHAND.getSlot(), Reflect.bukkitItemToNMS(equipment.getItemInOffHand())));

            Reflect.sendPacket(new ClientboundSetEquipmentPacket(Reflect.getEntityId(entity), list), players);
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

