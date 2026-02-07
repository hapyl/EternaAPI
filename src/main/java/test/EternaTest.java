package test;

import com.google.common.collect.Lists;
import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.command.ArgumentList;
import me.hapyl.eterna.module.component.Components;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.scheduler.Scheduler;
import me.hapyl.eterna.module.util.TypeConverter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@ApiStatus.Internal
public abstract class EternaTest {
    
    private final Key key;
    
    @ApiStatus.Internal
    EternaTest(@NotNull Key key) {
        this.key = key;
    }
    
    public final void test0(@NotNull Player player, @NotNull ArgumentList args) {
        final List<Component> warnings = Lists.newArrayList();
        final CompletableFuture<Void> future = new CompletableFuture<>();
        
        future.thenRun(() -> {
                  if (warnings.isEmpty()) {
                      EternaLogger.test(player, Component.text("Test `%s` passed!".formatted(key), NamedTextColor.GREEN));
                  }
                  else {
                      EternaLogger.test(player, Component.text("Test `%s` passed with %s warnings!".formatted(key, warnings.size()), NamedTextColor.GOLD));
                      
                      for (int i = 0; i < warnings.size(); i++) {
                          final Component warning = warnings.get(i);
                          
                          EternaLogger.test(
                                  player,
                                  Component.text("%s. ".formatted(i + 1), NamedTextColor.YELLOW).append(Components.normalizeStyle(warning, Style.style(NamedTextColor.GRAY, TextDecoration.ITALIC)))
                          );
                      }
                  }
              })
              .exceptionally(ex -> {
                  EternaLogger.test(player, Component.text("Test `%s` failed!".formatted(key), NamedTextColor.DARK_RED));
                  EternaLogger.test(player, Component.text(" " + ex.getCause().getMessage(), NamedTextColor.RED));
                  
                  return null;
              });
        
        try {
            EternaLogger.test(player, Component.text("Testing `%s`...".formatted(key), NamedTextColor.YELLOW));
            
            test(new EternaTestContext() {
                @Override
                @NotNull
                public Player player() {
                    return player;
                }
                
                @Override
                @NotNull
                public TypeConverter argument(int index) {
                    return args.get(index);
                }
                
                @Override
                public void info(@NotNull Component info) {
                    EternaLogger.test(player, Component.text(" ", NamedTextColor.GRAY, TextDecoration.ITALIC).append(info));
                }
                
                @Override
                public void warning(@NotNull Component warning) {
                    warnings.add(warning);
                }
                
                @Override
                @NotNull
                public Scheduler scheduler() {
                    return new Scheduler(Eterna.getPlugin());
                }
                
                @Override
                public void assertTestPassed() {
                    future.complete(null);
                }
                
                @Override
                public void assertTestFailed(@NotNull String reason) {
                    assertTestFailed(new EternaTestException(EternaTest.this, reason));
                }
                
                @Override
                public void assertTestFailed(@NotNull Throwable throwable) {
                    future.completeExceptionally(throwable);
                }
                
                @Override
                public void assertEquals(@Nullable Object a, @Nullable Object b) {
                    if (!Objects.equals(a, b)) {
                        assertTestFailed("Objects are not the same! (Expected `%s`, got `%s`)".formatted(a, b));
                    }
                }
                
                @Override
                public void assertNotEquals(@Nullable Object a, @Nullable Object b) {
                    if (Objects.equals(a, b)) {
                        assertTestFailed("Object are the same! (Expected `%s` != `%s)".formatted(a, b));
                    }
                }
                
                @Override
                public void assertTrue(boolean condition, @NotNull String reason) {
                    if (!condition) {
                        assertTestFailed(reason);
                    }
                }
                
                @Override
                public void assertFalse(boolean condition, @NotNull String reason) {
                    if (condition) {
                        assertTestFailed(reason);
                    }
                }
                
                @Override
                public void assertNull(@Nullable Object object) {
                    if (object != null) {
                        assertTestFailed("Expected `null`, got `%s`!".formatted(object));
                    }
                }
                
                @Override
                public void assertNotNull(@Nullable Object object) {
                    if (object == null) {
                        assertTestFailed("Object must not be null!");
                    }
                }
                
                @Override
                public void assertThrows(@NotNull Runnable runnable, @NotNull String reason) {
                    try {
                        runnable.run();
                        assertTestFailed(reason);
                    }
                    catch (Exception ignored) {
                    }
                }
            });
        }
        catch (EternaTestException ex) {
            future.completeExceptionally(ex);
        }
    }
    
    protected abstract void test(@NotNull EternaTestContext context) throws EternaTestException;
    
}
