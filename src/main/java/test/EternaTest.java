package test;

import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.registry.Keyed;
import me.hapyl.eterna.module.util.ArgumentList;
import me.hapyl.eterna.module.util.Runnables;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Function;

public abstract class EternaTest implements Keyed {
    
    private final Key key;
    
    boolean skipFail = false;
    
    EternaTest(@Nonnull String key) {
        this.key = Key.ofString(key);
    }
    
    @Nonnull
    @Override
    public final Key getKey() {
        return key;
    }
    
    @Override
    public final boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        final EternaTest that = (EternaTest) o;
        return Objects.equals(this.key, that.key);
    }
    
    @Override
    public final int hashCode() {
        return Objects.hashCode(this.key);
    }
    
    @Override
    public final String toString() {
        return key.toString();
    }
    
    public abstract boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException;
    
    protected boolean doShowFeedback() {
        return true;
    }
    
    // *=* Helpers *=* //
    
    protected void info(@Nonnull Object info) {
        EternaLogger.test("&7&o " + info);
    }
    
    protected void later(int delay, @Nonnull Runnable... runnables) {
        try {
            int total = 0;
            
            for (Runnable runnable : runnables) {
                Runnables.runLater(runnable, total += delay);
            }
        }
        catch (Exception ex) {
            throw new EternaTestException(this, ex.getMessage());
        }
    }
    
    // *=* Assertions *=* //
    
    protected void assertTestPassed() {
        EternaRuntimeTest.staticTest.handleTestPassed();
    }
    
    protected void assertSkipFail() {
        this.skipFail = true;
    }
    
    protected void assertFail(String reason) {
        throw new EternaTestException(this, reason);
    }
    
    protected void assertEquals(@Nonnull Object a, @Nullable Object b) {
        if (!a.equals(b)) {
            throw new EternaTestException(this, "Objects are not the same! (Expected '%s', got '%s'!)".formatted(b, a));
        }
    }
    
    protected void assertNotEquals(@Nonnull Object a, @Nullable Object b) {
        if (a.equals(b)) {
            throw new EternaTestException(this, "Objects are the same! (Expected '%s' != '%s')".formatted(a, b));
        }
    }
    
    protected void assertNull(@Nullable Object a) {
        if (a != null) {
            throw new EternaTestException(this, "Object '%s' must be null!".formatted(a));
        }
    }
    
    protected void assertNotNull(@Nullable Object a) {
        if (a == null) {
            throw new EternaTestException(this, "Object must be null!");
        }
    }
    
    protected void assertTrue(boolean val) {
        assertTrue(val, "Expected 'true', but got 'false'!");
    }
    
    protected void assertTrue(boolean val, @Nonnull String reason) {
        if (!val) {
            throw new EternaTestException(this, reason);
        }
    }
    
    protected <O> void assertTrue(O o, Function<O, Boolean> predicate) {
        final Boolean apply = predicate.apply(o);
        
        if (!apply) {
            throw new EternaTestException(this, "Object check failed! " + o);
        }
    }
    
    protected void assertFalse(boolean val) {
        assertFalse(val, "Expected 'false', but got 'true'!");
    }
    
    protected void assertFalse(boolean val, @Nonnull String reason) {
        if (val) {
            throw new EternaTestException(this, reason);
        }
    }
    
    protected void assertThrows(@Nonnull Runnable runnable) {
        assertThrows(runnable, "Should have thrown an error, but didn't!");
    }
    
    protected void assertThrows(@Nonnull Runnable runnable, @Nonnull String reason) {
        try {
            runnable.run();
            assertFail(reason);
        }
        catch (Exception ignored) {
        }
    }
    
}
