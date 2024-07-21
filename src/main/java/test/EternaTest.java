package test;

import me.hapyl.spigotutils.EternaLogger;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.util.ArgumentList;
import me.hapyl.spigotutils.module.util.Runnables;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public abstract class EternaTest {

    private final String name;

    boolean skipFail = false;

    EternaTest(String name) {
        this.name = name;
    }

    public abstract boolean test(@Nonnull Player player, @Nonnull ArgumentList args) throws EternaTestException;

    @Override
    public String toString() {
        return name;
    }

    protected boolean doShowFeedback() {
        return true;
    }

    // *=* Helpers *=* //

    protected void info(@Nonnull Player player, @Nonnull String info) {
        Chat.sendMessage(player, EternaLogger.TEST_PREFIX + "&7&o" + info);
    }

    protected void later(@Nonnull Runnable runnable, int i) {
        Runnables.runLater(runnable, i);
    }

    // *=* Assertions *=* //

    protected void assertTestPassed() {
        EternaRuntimeTest.handleTestPassed(this);
    }

    protected void assertSkipFail() {
        skipFail = true;
    }

    protected void assertFail(String reason) {
        throw new EternaTestException(this, reason);
    }

    protected void assertEquals(@Nonnull Object a, Object b) {
        if (!a.equals(b)) {
            throw new EternaTestException(this, "Objects are not the same! (Expected '%s', got '%s'!)".formatted(b, a));
        }
    }

    protected void assertNotEquals(Object a, Object b) {
        if (a != null && a.equals(b)) {
            throw new EternaTestException(this, "Object '%s' and '%s' are the same!".formatted(a, b));
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
        } catch (Exception ignored) {
        }
    }
}
