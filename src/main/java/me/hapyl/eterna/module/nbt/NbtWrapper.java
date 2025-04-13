package me.hapyl.eterna.module.nbt;

import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.annotate.NmsWrapper;
import me.hapyl.eterna.module.annotate.TestedOn;
import me.hapyl.eterna.module.annotate.Version;
import me.hapyl.eterna.module.reflect.Reflect;
import net.minecraft.nbt.*;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Represents a wrapper for {@link CompoundTag}, allowing readable method names.
 */
@NmsWrapper(CompoundTag.class)
@TestedOn(version = Version.V1_21_5)
public class NbtWrapper {
    
    private final CompoundTag tag;
    private Map<String, Tag> originalMap;
    
    /**
     * Creates a new wrapper.
     *
     * @param tag - The nbt tag to wrap.
     * @see #of(Entity)
     * @see #of(CompoundTag)
     */
    public NbtWrapper(@Nonnull CompoundTag tag) {
        this.tag = tag;
    }
    
    /**
     * Maps to given {@link Tag} to the given key.
     *
     * @param key - The key to map to.
     * @param tag - The tag to map.
     */
    public void put(@Nonnull String key, @Nonnull Tag tag) {
        this.tag.put(key, tag);
    }
    
    /**
     * Maps to given {@link String} to the given key.
     *
     * @param key   - The key to map to.
     * @param value - The value to map.
     */
    public void put(@Nonnull String key, @Nonnull String value) {
        this.tag.putString(key, value);
    }
    
    /**
     * Maps to given {@link Byte} to the given key.
     *
     * @param key   - The key to map to.
     * @param value - The value to map.
     */
    public void put(@Nonnull String key, byte value) {
        this.tag.putByte(key, value);
    }
    
    /**
     * Maps to given {@link Short} to the given key.
     *
     * @param key   - The key to map to.
     * @param value - The value to map.
     */
    public void put(@Nonnull String key, short value) {
        this.tag.putShort(key, value);
    }
    
    /**
     * Maps to given {@link Integer} to the given key.
     *
     * @param key   - The key to map to.
     * @param value - The value to map.
     */
    public void put(@Nonnull String key, int value) {
        this.tag.putInt(key, value);
    }
    
    /**
     * Maps to given {@link Long} to the given key.
     *
     * @param key   - The key to map to.
     * @param value - The value to map.
     */
    public void put(@Nonnull String key, long value) {
        this.tag.putLong(key, value);
    }
    
    /**
     * Maps to given {@link Float} to the given key.
     *
     * @param key   - The key to map to.
     * @param value - The value to map.
     */
    public void put(@Nonnull String key, float value) {
        this.tag.putFloat(key, value);
    }
    
    /**
     * Maps to given {@link Double} to the given key.
     *
     * @param key   - The key to map to.
     * @param value - The value to map.
     */
    public void put(@Nonnull String key, double value) {
        this.tag.putDouble(key, value);
    }
    
    /**
     * Maps to given {@link Byte}[] to the given key.
     *
     * @param key   - The key to map to.
     * @param value - The value to map.
     */
    public void put(@Nonnull String key, byte[] value) {
        this.tag.putByteArray(key, value);
    }
    
    /**
     * Maps to given {@link Integer}[] to the given key.
     *
     * @param key   - The key to map to.
     * @param value - The value to map.
     */
    public void put(@Nonnull String key, int[] value) {
        this.tag.putIntArray(key, value);
    }
    
    /**
     * Maps to given {@link Long}[] to the given key.
     *
     * @param key   - The key to map to.
     * @param value - The value to map.
     */
    public void put(@Nonnull String key, long[] value) {
        this.tag.putLongArray(key, value);
    }
    
    /**
     * Maps to given {@link Boolean} to the given key.
     *
     * @param key   - The key to map to.
     * @param value - The value to map.
     */
    public void put(@Nonnull String key, boolean value) {
        this.tag.putBoolean(key, value);
    }
    
    /**
     * Returns {@code true} if the nbt contains a value by the given key.
     *
     * @param key - The key to check for mapping.
     */
    public boolean contains(@Nonnull String key) {
        return this.tag.contains(key);
    }
    
    /**
     * Gets a raw {@link Tag} by the given key, may be null.
     *
     * @param key - The key.
     * @return a raw {@link Tag}.
     */
    @Nullable
    public Tag get(@Nonnull String key) {
        return this.tag.get(key);
    }
    
    /**
     * Gets a {@link String} by the given key, or {@code ""} if not mapped or mapped to a different type.
     *
     * @param key - The key.
     * @return a {@link String} by the given key, or {@code ""}  if not mapped or mapped to a different type.
     */
    @Nonnull
    public String getString(@Nonnull String key) {
        return get0(key, Tag.class, Tag::asString, "");
    }
    
    /**
     * Gets a {@link Byte} by the given key, or {@code 0} if not mapped or mapped to a different type.
     *
     * @param key - The key.
     * @return a {@link Byte} by the given key, or {@code 0}  if not mapped or mapped to a different type.
     */
    public byte getByte(@Nonnull String key) {
        return get0(key, NumericTag.class, NumericTag::asByte, (byte) 0);
    }
    
    /**
     * Gets a {@link Short} by the given key, or {@code 0} if not mapped or mapped to a different type.
     *
     * @param key - The key.
     * @return a {@link Short} by the given key, or {@code 0} if not mapped or mapped to a different type.
     */
    public short getShort(@Nonnull String key) {
        return get0(key, NumericTag.class, NumericTag::asShort, (short) 0);
    }
    
    /**
     * Gets a {@link Integer} by the given key, or {@code 0} if not mapped or mapped to a different type.
     *
     * @param key - The key.
     * @return a {@link Integer} by the given key, or {@code 0} if not mapped or mapped to a different type.
     */
    public int getInteger(@Nonnull String key) {
        return get0(key, NumericTag.class, NumericTag::asInt, 0);
    }
    
    /**
     * Gets a {@link Long} by the given key, or {@code 0} if not mapped or mapped to a different type.
     *
     * @param key - The key.
     * @return a {@link Long} by the given key, or {@code 0} if not mapped or mapped to a different type.
     */
    public long getLong(@Nonnull String key) {
        return get0(key, NumericTag.class, NumericTag::asLong, 0L);
    }
    
    /**
     * Gets a {@link Double} by the given key, or {@code 0} if not mapped or mapped to a different type.
     *
     * @param key - The key.
     * @return a {@link Double} by the given key, or {@code 0} if not mapped or mapped to a different type.
     */
    public double getDouble(@Nonnull String key) {
        return get0(key, NumericTag.class, NumericTag::asDouble, 0d);
    }
    
    /**
     * Gets a {@link Float} by the given key, or {@code 0} if not mapped or mapped to a different type.
     *
     * @param key - The key.
     * @return a {@link Float} by the given key, or {@code 0} if not mapped or mapped to a different type.
     */
    public float getFloat(@Nonnull String key) {
        return get0(key, NumericTag.class, NumericTag::asFloat, 0f);
    }
    
    /**
     * Gets a {@link Byte}[] by the given key, or {@code []} if not mapped or mapped to a different type.
     *
     * @param key - The key.
     * @return a {@link Byte}[] by the given key, or {@code []} if not mapped or mapped to a different type.
     */
    public byte[] getByteArray(@Nonnull String key) {
        return get0(key, ByteArrayTag.class, ByteArrayTag::asByteArray, new byte[0]);
    }
    
    /**
     * Gets a {@link Integer}[] by the given key, or {@code []} if not mapped or mapped to a different type.
     *
     * @param key - The key.
     * @return a {@link Integer}[] by the given key, or {@code []} if not mapped or mapped to a different type.
     */
    public int[] getIntegerArray(@Nonnull String key) {
        return get0(key, IntArrayTag.class, IntArrayTag::asIntArray, new int[0]);
    }
    
    /**
     * Gets a {@link Long}[] by the given key, or {@code []} if not mapped or mapped to a different type.
     *
     * @param key - The key.
     * @return a {@link Long}[] by the given key, or {@code []} if not mapped or mapped to a different type.
     */
    public long[] getLongArray(@Nonnull String key) {
        return get0(key, LongArrayTag.class, LongArrayTag::asLongArray, new long[0]);
    }
    
    /**
     * Gets a {@link Boolean} by the given key, or {@code false} if not mapped or mapped to a different type.
     *
     * @param key - The key.
     * @return a {@link Boolean} by the given key, or {@code false} if not mapped or mapped to a different type.
     */
    public boolean getBoolean(@Nonnull String key) {
        return this.tag.getBoolean(key).orElse(false);
    }
    
    /**
     * Gets a {@link CompoundTag} by the given key, or {@code {}} if not mapped or mapped to a different type.
     *
     * @param key - The key.
     * @return a {@link CompoundTag} by the given key, or {@code {}} if not mapped or mapped to a different type.
     */
    @Nonnull
    public CompoundTag getNbt(@Nonnull String key) {
        return this.tag.getCompound(key).orElse(new CompoundTag());
    }
    
    /**
     * Removes the value mapped to the given key.
     *
     * @param key - The key to remove the value from.
     * @return {@code true} if removed, {@code false} otherwise.
     */
    public boolean remove(@Nonnull String key) {
        boolean wasRemoved = this.tag.contains(key);
        this.tag.remove(key);
        
        return wasRemoved;
    }
    
    /**
     * Returns {@code true} if the nbt is empty, {@code false} otherwise.
     *
     * @return {@code true} if the nbt is empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return this.tag.isEmpty();
    }
    
    /**
     * Gets a string representation of this nbt, according to {@link EternaTagVisitor}.
     *
     * @return a string representation of this nbt.
     */
    @Override
    public String toString() {
        return new EternaTagVisitor().toString(this);
    }
    
    /**
     * Merges the given {@link NbtWrapper} to this one.
     *
     * @param other - The nbt to merge.
     */
    @Nonnull
    public NbtWrapper merge(@Nonnull NbtWrapper other) {
        this.tag.merge(other.tag);
        
        return this;
    }
    
    /**
     * Accepts the {@link TagVisitor}.
     *
     * @param visitor - The visitor to accept.
     * @see EternaTagVisitor
     */
    public void accept(@Nonnull TagVisitor visitor) {
        this.tag.accept(visitor);
    }
    
    /**
     * Gets all the keys of this nbt.
     *
     * @return all the keys of this nbt.
     */
    @Nonnull
    public Set<String> keys() {
        return this.tag.keySet();
    }
    
    /**
     * Gets the wrapping {@link CompoundTag}.
     *
     * @return the wrapping nbt.
     */
    @Nonnull
    public final CompoundTag tag() {
        return this.tag;
    }
    
    /**
     * Gets the original {@link Map}, containing the mapped {@link Tag}s.
     * <br>
     * Modifying this map modifies the actual nbt tags!
     *
     * @return the origin map.
     * @see NbtTag#to(Map, String, Object)
     * @see NbtTag#from(Map, String)
     */
    @ApiStatus.Experimental
    @SuppressWarnings("unchecked")
    public final Map<String, Tag> originalMap() {
        if (this.originalMap == null) {
            try {
                final Field field = this.tag.getClass().getDeclaredField("tags");
                field.setAccessible(true);
                
                return (Map<String, Tag>) field.get(tag);
            }
            catch (NoSuchFieldException | IllegalAccessException e) {
                throw EternaLogger.exception(e);
            }
        }
        
        return this.originalMap;
    }
    
    private <T extends Tag, R> R get0(String key, Class<T> clazz, Function<T, Optional<R>> fn, R def) {
        final Tag tag = this.tag.get(key);
        
        if (!clazz.isInstance(tag)) {
            return def;
        }
        
        return fn.apply(clazz.cast(tag)).orElse(def);
    }
    
    @Nonnull
    public static NbtWrapper of(@Nonnull CompoundTag nbt) {
        return new NbtWrapper(nbt);
    }
    
    @Nonnull
    public static NbtWrapper of(@Nonnull Entity entity) {
        return new NbtWrapper(Reflect.getEntityNbt(Reflect.getMinecraftEntity(entity)));
    }
    
}
