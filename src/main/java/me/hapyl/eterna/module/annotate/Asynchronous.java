package me.hapyl.eterna.module.annotate;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.Callable;

/**
 * Indicates that this method or class is handled asynchronous and will throw {@link IllegalStateException}
 * if used within synchronized context without synchronizing it.
 *
 * @see BukkitScheduler#runTask(Plugin, Runnable)
 * @see BukkitScheduler#callSyncMethod(Plugin, Callable)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface Asynchronous {
}
