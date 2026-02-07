package me.hapyl.eterna;

import com.google.common.collect.Maps;
import me.hapyl.eterna.module.util.Streamable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

@ApiStatus.Internal
public class EternaHandler<K, V> extends EternaKeyed implements Streamable<V> {
    
    protected final EternaPlugin eterna;
    protected final Map<K, V> registry;
    
    @ApiStatus.Internal
    public EternaHandler(@NotNull EternaKey key, @NotNull EternaPlugin eterna) {
        super(key);
        
        this.eterna = eterna;
        this.registry = makeNewMap();
    }
    
    public void register(@NotNull K k, @NotNull V v) {
        if (this.registry.containsKey(k)) {
            throw new IllegalArgumentException("Already registered: `%s`".formatted(k));
        }
        
        this.registry.put(k, v);
    }
    
    @Nullable
    public V unregister(@NotNull K k) {
        return this.registry.remove(k);
    }
    
    public boolean isRegistered(@NotNull K k) {
        return this.registry.containsKey(k);
    }
    
    @NotNull
    public Optional<V> get(@NotNull K k) {
        return Optional.ofNullable(this.registry.get(k));
    }
    
    @Nullable
    public V getOrNull(@NotNull K k) {
        return this.registry.get(k);
    }
    
    @NotNull
    public V getOrCompute(@NotNull K k, @NotNull Function<K, V> computeFunctions) {
        return this.registry.computeIfAbsent(k, computeFunctions);
    }
    
    public boolean keyPresent(@NotNull K k) {
        return this.registry.containsKey(k);
    }
    
    public boolean valuePresent(@NotNull V v) {
        return this.registry.containsValue(v);
    }
    
    public void forEach(@NotNull BiConsumer<K, V> consumer) {
        this.registry.forEach(consumer);
    }
    
    public void forEach(@NotNull Consumer<V> consumer) {
        this.registry.values().forEach(consumer);
    }
    
    @NotNull
    public Stream<V> stream() {
        return this.registry.values().stream();
    }
    
    @NotNull
    protected Map<K, V> makeNewMap() {
        return Maps.newHashMap();
    }
    
    @ApiStatus.Internal
    protected static void tryCatch(@NotNull Runnable runnable) {
        try {
            runnable.run();
        }
        catch (Exception ex) {
            throw EternaLogger.acknowledgeException(ex);
        }
    }
}
