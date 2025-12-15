package me.hapyl.eterna.module.nms;

import com.google.common.collect.EnumBiMap;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Function;

@ApiStatus.Internal
public class NmsConverterEnum<B extends Enum<B>, N extends Enum<N>> implements NmsConverter<B, N> {
    private final EnumBiMap<B, N> nmsMapped;
    
    NmsConverterEnum(@Nonnull Class<B> bukkitClass, @Nonnull Class<N> nmsClass, @Nonnull Function<B, N> lookup) {
        this.nmsMapped = EnumBiMap.create(bukkitClass, nmsClass);
        
        for (B enumConstant : bukkitClass.getEnumConstants()) {
            nmsMapped.put(enumConstant, lookup.apply(enumConstant));
        }
    }
    
    @Nonnull
    @Override
    public B toBukkit(@Nonnull N n) {
        return Objects.requireNonNull(nmsMapped.inverse().get(n));
    }
    
    @Nonnull
    @Override
    public N toNms(@Nonnull B b) {
        return Objects.requireNonNull(nmsMapped.get(b));
    }
    
    @Nonnull
    static <B extends Enum<B>, N extends Enum<N>> NmsConverterEnum<B, N> build(@Nonnull Class<B> bukkitClass, @Nonnull Class<N> nmsClass, @Nonnull Function<B, N> lookup) {
        return new NmsConverterEnum<>(bukkitClass, nmsClass, lookup);
    }
    
}
