package me.hapyl.spigotutils.module.nbt.nms;

import me.hapyl.spigotutils.module.annotate.TestedNMS;
import me.hapyl.spigotutils.module.reflect.Reflect;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

@TestedNMS(version = "1.19.4")
public class NBTNative {

    private static final Method CRAFT_AS_NMS_COPY_METHOD = Reflect.getCraftMethod("inventory.CraftItemStack", "asNMSCopy", ItemStack.class);
    private static final Method CRAFT_AS_BUKKIT_COPY_METHOD = Reflect.getCraftMethod(
            "inventory.CraftItemStack",
            "asBukkitCopy",
            net.minecraft.world.item.ItemStack.class
    );

    @Nonnull
    public static CompoundTag getCompound(ItemStack item) {
        final net.minecraft.world.item.ItemStack nms = asNMSCopy(item);
        return nms.getOrCreateTag();
    }

    public static CompoundTag getCompoundOrCreate(net.minecraft.world.item.ItemStack nmsItem) {
        return nmsItem.hasTag() ? nmsItem.getTag() : new CompoundTag();
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

                final CompoundTag compound = NbtUtils.snbtToStructure(nbt);
                final net.minecraft.world.item.ItemStack nmsItem = asNMSCopy(itemStack);

                final CompoundTag original = getCompoundOrCreate(nmsItem);
                original.merge(compound);

                // Set NBT to the item if was empty
                if (!nmsItem.hasTag()) {
                    nmsItem.setTag(compound);
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
