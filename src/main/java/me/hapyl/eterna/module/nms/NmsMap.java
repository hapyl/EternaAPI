package me.hapyl.eterna.module.nms;

import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;
import java.util.EnumMap;

@ApiStatus.Internal
public class NmsMap<K extends Enum<K>, V extends Enum<V> & NmsEnum<K>> extends EnumMap<K, V> {
    
    private final Class<V> valueType;
    
    NmsMap(@Nonnull Class<K> keyType, @Nonnull Class<V> valueType) {
        super(keyType);
        
        this.valueType = valueType;
        
        // Populate map
        for (V enumConstant : valueType.getEnumConstants()) {
            this.put(enumConstant.toNms(), enumConstant);
        }
    }
    
    @Override
    public V get(Object key) {
        if (!containsKey(key)) {
            throw new IllegalArgumentException("Invalid key for enum %s!".formatted(valueType.getSimpleName()));
        }
        
        return super.get(key);
    }
    
    @Nonnull
    public static <K extends Enum<K>, V extends Enum<V> & NmsEnum<K>> NmsMap<K, V> create(@Nonnull Class<K> keyClass, @Nonnull Class<V> valueClass) {
        return new NmsMap<>(keyClass, valueClass);
    }
    
}
