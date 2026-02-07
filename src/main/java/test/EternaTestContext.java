package test;

import me.hapyl.eterna.module.scheduler.Scheduler;
import me.hapyl.eterna.module.util.TypeConverter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
@ApiStatus.NonExtendable
public interface EternaTestContext {
    
    @NotNull
    Player player();
    
    @NotNull
    TypeConverter argument(int index);
    
    void info(@NotNull Component info);
    
    void warning(@NotNull Component warning);
    
    @NotNull
    Scheduler scheduler();
    
    void assertTestPassed();
    
    void assertTestFailed(@NotNull String reason);
    
    void assertTestFailed(@NotNull Throwable throwable);
    
    void assertEquals(@Nullable Object a, @Nullable Object b);
    
    void assertNotEquals(@Nullable Object a, @Nullable Object b);
    
    default void assertTrue(boolean condition) {
        assertTrue(condition, "Expected `true`, got `false`!");
    }
    
    void assertTrue(boolean condition, @NotNull String reason);
    
    default void assertFalse(boolean condition) {
        assertFalse(condition, "Expected `false`, got `true`!");
    }
    
    void assertFalse(boolean condition, @NotNull String reason);
    
    void assertNull(@Nullable Object object);
    
    void assertNotNull(@Nullable Object object);
    
    default void assertThrows(@NotNull Runnable runnable) {
        assertThrows(runnable, "Expected an exception, but nothing was thrown!");
    }
    
    void assertThrows(@NotNull Runnable runnable, @NotNull String reason);
    
}
