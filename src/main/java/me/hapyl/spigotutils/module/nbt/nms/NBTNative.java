package me.hapyl.spigotutils.module.nbt.nms;

import me.hapyl.spigotutils.module.annotate.TestedNMS;
import me.hapyl.spigotutils.module.reflect.Reflect;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

@TestedNMS(version = "1.19.3")
public class NBTNative {

    private static final Method CRAFT_AS_NMS_COPY_METHOD = Reflect.getCraftMethod("inventory.CraftItemStack", "asNMSCopy", ItemStack.class);
    private static final Method CRAFT_AS_BUKKIT_COPY_METHOD = Reflect.getCraftMethod(
            "inventory.CraftItemStack",
            "asBukkitCopy",
            net.minecraft.world.item.ItemStack.class
    );

    @Nonnull
    public static NBTTagCompound getCompound(ItemStack item) {
        final net.minecraft.world.item.ItemStack nms = asNMSCopy(item);
        return nms.v();
    }

    public static NBTTagCompound getCompoundOrCreate(net.minecraft.world.item.ItemStack nmsItem) {
        return nmsItem.t() ? nmsItem.u() : new NBTTagCompound();
    }

    public static net.minecraft.world.item.ItemStack asNMSCopy(ItemStack stack) {
        return (net.minecraft.world.item.ItemStack) Reflect.invokeMethod(CRAFT_AS_NMS_COPY_METHOD, null, stack);
    }

    public static ItemStack asBukkitCopy(net.minecraft.world.item.ItemStack nmsItem) {
        return (ItemStack) Reflect.invokeMethod(CRAFT_AS_BUKKIT_COPY_METHOD, null, nmsItem);
    }

}