package me.hapyl.spigotutils.module.nbt;

import me.hapyl.spigotutils.module.annotate.TestedOn;
import me.hapyl.spigotutils.module.annotate.Version;
import me.hapyl.spigotutils.module.reflect.Reflect;
import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * A native nbt implementation.
 */
@TestedOn(version = Version.V1_20_4)
public class NBTNative {

    private static final Method CRAFT_AS_NMS_COPY_METHOD = Reflect.getCraftMethod("inventory.CraftItemStack", "asNMSCopy", ItemStack.class);
    private static final Method CRAFT_AS_BUKKIT_COPY_METHOD = Reflect.getCraftMethod(
            "inventory.CraftItemStack",
            "asBukkitCopy",
            net.minecraft.world.item.ItemStack.class
    );

    /**
     * Gets the compound for a given item.
     *
     * @param item - Item.
     * @return the compound for a given item.
     */
    @Nonnull
    public static NBTTagCompound getCompound(@Nonnull ItemStack item) {
        return getCompound(asNMSCopy(item));
    }

    /**
     * Gets the compound for a given item.
     *
     * @param nmsItem - NMS Item.
     * @return the compound for a given item.
     */
    @Nonnull
    public static NBTTagCompound getCompound(@Nonnull net.minecraft.world.item.ItemStack nmsItem) {
        return nmsItem.w();
    }

    /**
     * Gets the compound for a given item, or creates it if not present.
     *
     * @param item - Item.
     * @return the compound for a given item, or creates it if not present.
     */
    @Nonnull
    public static NBTTagCompound getCompoundOrCreate(@Nonnull ItemStack item) {
        return getCompoundOrCreate(asNMSCopy(item));
    }

    /**
     * Gets the compound for a given item, or creates it if not present.
     *
     * @param nmsItem - NMS Item.
     * @return the compound for a given item, or creates it if not present.
     */
    @Nonnull
    public static NBTTagCompound getCompoundOrCreate(@Nonnull net.minecraft.world.item.ItemStack nmsItem) {
        final NBTTagCompound compound = nmsItem.v();

        return compound == null || !nmsItem.t() ? new NBTTagCompound() : compound;
    }

    /**
     * Gets the NMS item copy from a bukkit item.
     *
     * @param stack - Bukkit Item.
     * @return the NMS item copy from a bukkit item.
     */
    @Nonnull
    public static net.minecraft.world.item.ItemStack asNMSCopy(@Nonnull ItemStack stack) {
        return (net.minecraft.world.item.ItemStack) Objects.requireNonNull(Reflect.invokeMethod(CRAFT_AS_NMS_COPY_METHOD, null, stack));
    }

    /**
     * Gets the bukkit item copy from a NMS item.
     *
     * @param nmsItem - NMS Item.
     * @return the bukkit item copy from a NMS item.
     */
    public static ItemStack asBukkitCopy(@Nonnull net.minecraft.world.item.ItemStack nmsItem) {
        return (ItemStack) Reflect.invokeMethod(CRAFT_AS_BUKKIT_COPY_METHOD, null, nmsItem);
    }

    /**
     * Sets the raw NBT to an item.
     *
     * @param itemStack - Item.
     * @param nbt       - Raw NBT.
     * @return the same item with NBT applied to it.
     */
    public static ItemStack setNbt(@Nonnull ItemStack itemStack, @Nonnull String nbt) {
        if (!nbt.startsWith("{")) {
            nbt = "{" + nbt;
        }

        if (!nbt.endsWith("}")) {
            nbt = nbt + "}";
        }

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

    @Nonnull
    public static String getString(@Nonnull ItemStack item, @Nonnull String key) {
        return getCompound(item).l(key);
    }

    public static int getInt(@Nonnull ItemStack item, @Nonnull String key) {
        return getCompound(item).h(key);
    }

    public static float getFloat(@Nonnull ItemStack item, @Nonnull String key) {
        return getCompound(item).j(key);
    }

    public static double getDouble(@Nonnull ItemStack item, @Nonnull String key) {
        return getCompound(item).k(key);
    }

    public static boolean getBoolean(@Nonnull ItemStack item, @Nonnull String key) {
        return getCompound(item).q(key);
    }

}
