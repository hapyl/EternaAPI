package me.hapyl.eterna.module.util;

import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.annotate.Asynchronous;
import me.hapyl.eterna.module.annotate.Synchronized;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * A {@link Promise} represents an asynchronous operation that can have a subsequent task executed after its completion,
 * and can catch exceptions that occur during the execution.
 */
@Asynchronous
public final class Promise {

    private Runnable andThen;
    private Consumer<RuntimeException> caught;

    private Promise() {
        this.andThen = () -> {
        };
        this.caught = ex -> {
        };
    }

    /**
     * Specifies the task to be executed after the asynchronous operation completes.
     *
     * @param then - The task to execute after the promise is fulfilled.
     */
    @Synchronized
    public Promise then(@Nonnull Runnable then) {
        this.andThen = then;
        return this;
    }

    /**
     * Specifies a consumer to handle any {@link RuntimeException} thrown during the execution of the promise.
     *
     * @param exception - The consumer to handle exceptions.
     */
    @Synchronized
    public Promise caught(@Nonnull Consumer<RuntimeException> exception) {
        this.caught = exception;
        return this;
    }

    /**
     * Creates a {@link Promise}, then executes the given {@link Runnable}.
     *
     * @param runnable - The runnable to execute.
     * @return a new promise.
     */
    @Nonnull
    public static Promise promise(@Nonnull Runnable runnable) {
        final BukkitScheduler scheduler = Bukkit.getScheduler();
        final EternaPlugin plugin = EternaPlugin.getPlugin();

        final Promise promise = new Promise();

        scheduler.runTaskAsynchronously(plugin, () -> {
            try {
                runnable.run();
            } catch (@Nonnull final RuntimeException exception) {
                promise.caught.accept(exception);
            }

            scheduler.runTask(plugin, () -> promise.andThen.run());
        });

        return promise;
    }

}
