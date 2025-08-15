package me.hapyl.eterna.module.nbt;

import me.hapyl.eterna.module.annotate.TestedOn;
import me.hapyl.eterna.module.annotate.Version;
import net.minecraft.nbt.*;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@TestedOn(version = Version.V1_21_8)
public interface NbtTag<T extends Tag, A> {
    
    @Nonnull NbtTag<StringTag, String> STRING = of(StringTag::asString, StringTag::valueOf, "");
    @Nonnull NbtTag<IntTag, Integer> INTEGER = of(IntTag::asInt, IntTag::valueOf, 0);
    @Nonnull NbtTag<ShortTag, Short> SHORT = of(ShortTag::asShort, ShortTag::valueOf, (short) 0);
    @Nonnull NbtTag<LongTag, Long> LONG = of(LongTag::asLong, LongTag::valueOf, 0L);
    @Nonnull NbtTag<ByteTag, Byte> BYTE = of(ByteTag::asByte, ByteTag::valueOf, (byte) 0x0);
    @Nonnull NbtTag<FloatTag, Float> FLOAT = of(FloatTag::asFloat, FloatTag::valueOf, 0f);
    @Nonnull NbtTag<DoubleTag, Double> DOUBLE = of(DoubleTag::asDouble, DoubleTag::valueOf, 0d);
    
    @Nonnull NbtTag<ByteArrayTag, byte[]> BYTE_ARRAY = of(ByteArrayTag::asByteArray, ByteArrayTag::new, new byte[0]);
    @Nonnull NbtTag<IntArrayTag, int[]> INTEGER_ARRAY = of(IntArrayTag::asIntArray, IntArrayTag::new, new int[0]);
    @Nonnull NbtTag<LongArrayTag, long[]> LONG_ARRAY = of(LongArrayTag::asLongArray, LongArrayTag::new, new long[0]);
    
    @Nonnull
    T to(@Nonnull A a);
    
    default void to(@Nonnull Map<String, Tag> map, @Nonnull String key, @Nonnull A a) {
        map.put(key, this.to(a));
    }
    
    @Nonnull
    A from(@Nonnull T t);
    
    @SuppressWarnings("unchecked")
    default A from(@Nonnull Map<String, Tag> map, @Nonnull String key) throws ClassCastException {
        return this.from((T) map.get(key));
    }
    
    private static <T extends Tag, A> NbtTag<T, A> of(Function<T, Optional<A>> from, Function<A, T> to, A def) {
        return new NbtTag<>() {
            @Nonnull
            @Override
            public T to(@Nonnull A a) {
                return to.apply(a);
            }
            
            @Nonnull
            @Override
            public A from(@Nonnull T t) {
                return from.apply(t).orElse(def);
            }
        };
    }
    
}