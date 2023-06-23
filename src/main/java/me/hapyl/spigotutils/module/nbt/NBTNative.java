package me.hapyl.spigotutils.module.nbt.nms;

import me.hapyl.spigotutils.module.annotate.TestedOn;
import me.hapyl.spigotutils.module.annotate.Version;
import me.hapyl.spigotutils.module.reflect.Reflect;
import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

@TestedOn(version = Version.V1_20)
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
        return nms.w();
    }

    public static NBTTagCompound getCompoundOrCreate(net.minecraft.world.item.ItemStack nmsItem) {
        return nmsItem.t() ? nmsItem.v() : new NBTTagCompound();
    }

    public static net.minecraft.world.item.ItemStack asNMSCopy(ItemStack stack) {
        return (net.minecraft.world.item.ItemStack) Reflect.invokeMethod(CRAFT_AS_NMS_COPY_METHOD, null, stack);
    }

    public static ItemStack asBukkitCopy(net.minecraft.world.item.ItemStack nmsItem) {
        return (ItemStack) Reflect.invokeMethod(CRAFT_AS_BUKKIT_COPY_METHOD, null, nmsItem);
    }

    public static ItemStack setNbt(@Nonnull ItemStack itemStack, @Nonnull String nbt) {
        if (nbt.startsWith("{") && nbt.endsWith("}")) {
            try {
                final NBTTagCompound compound = MojangsonParser.a(nbt);
                final net.minecraft.world.item.ItemStack nmsItem = asNMSCopy(itemStack);

                final NBTTagCompound original = getCompoundOrCreate(nmsItem);
                original.a(compound);

                // Set NBT to the item if was empty
                if (!nmsItem.t()) {
                    nmsItem.c(compound);
                }

                return NBTNative.asBukkitCopy(nmsItem);
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalArgumentException("Could not parse NBT! " + e.getMessage());
            }
        }
        throw new IllegalArgumentException("nbt must be wrapped between {}!");
    }

}
