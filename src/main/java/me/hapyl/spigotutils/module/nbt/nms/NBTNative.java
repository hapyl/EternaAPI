package me.hapyl.spigotutils.module.nbt.nms;

import me.hapyl.spigotutils.module.reflect.Reflect;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

public class NBTNative {

    private static final Method CRAFT_AS_NMS_COPY_METHOD = Reflect.getCraftMethod("inventory.CraftItemStack", "asNMSCopy");

    @Nonnull
    public static NBTTagCompound getCompound(ItemStack item) {
        final net.minecraft.world.item.ItemStack nms = asNMSCopy(item);
        return nms.v();
    }

    private static net.minecraft.world.item.ItemStack asNMSCopy(ItemStack stack) {
        return (net.minecraft.world.item.ItemStack) Reflect.invokeMethod(CRAFT_AS_NMS_COPY_METHOD, null, stack);

    }

}
