package me.hapyl.eterna.module.nbt;

import net.minecraft.nbt.*;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.Function;

public interface NbtTag<T extends Tag, A> {

    @Nonnull NbtTag<StringTag, String> STRING = of(StringTag::getAsString, StringTag::valueOf);
    @Nonnull NbtTag<IntTag, Integer> INTEGER = of(IntTag::getAsInt, IntTag::valueOf);
    @Nonnull NbtTag<ShortTag, Short> SHORT = of(ShortTag::getAsShort, ShortTag::valueOf);
    @Nonnull NbtTag<LongTag, Long> LONG = of(LongTag::getAsLong, LongTag::valueOf);
    @Nonnull NbtTag<ByteTag, Byte> BYTE = of(ByteTag::getAsByte, ByteTag::valueOf);
    @Nonnull NbtTag<FloatTag, Float> FLOAT = of(FloatTag::getAsFloat, FloatTag::valueOf);
    @Nonnull NbtTag<DoubleTag, Double> DOUBLE = of(DoubleTag::getAsDouble, DoubleTag::valueOf);

    @Nonnull NbtTag<ByteArrayTag, byte[]> BYTE_ARRAY = of(ByteArrayTag::getAsByteArray, ByteArrayTag::new);
    @Nonnull NbtTag<IntArrayTag, int[]> INTEGER_ARRAY = of(IntArrayTag::getAsIntArray, IntArrayTag::new);
    @Nonnull NbtTag<LongArrayTag, long[]> LONG_ARRAY = of(LongArrayTag::getAsLongArray, LongArrayTag::new);

    @Nonnull
    T to(@Nonnull A a);

    default void to(@Nonnull Map<String, Tag> map, @Nonnull String key, @Nonnull A a) {
        map.put(key, to(a));
    }

    @Nonnull
    A from(@Nonnull T t);

    @SuppressWarnings("unchecked")
    default A from(@Nonnull Map<String, Tag> map, @Nonnull String key) throws ClassCastException {
        return from((T) map.get(key));
    }

    private static <T extends Tag, A> NbtTag<T, A> of(Function<T, A> from, Function<A, T> to) {
        return new NbtTag<>() {
            @Nonnull
            @Override
            public T to(@Nonnull A a) {
                return to.apply(a);
            }

            @Nonnull
            @Override
            public A from(@Nonnull T t) {
                return from.apply(t);
            }
        };
    }

}