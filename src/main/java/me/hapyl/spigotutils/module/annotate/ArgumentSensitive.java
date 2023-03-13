package me.hapyl.spigotutils.module.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated method can
 * be targeted by {@link me.hapyl.spigotutils.module.command.CommandProcessor}
 * to call it if {@link ArgumentSensitive#targetSize()} and {@link ArgumentSensitive#targetString()}
 * matches to execute it.
 *
 * Notice that annotated method must be a member of {@link me.hapyl.spigotutils.module.command.SimpleCommand} class
 * and <b>must</b> have exactly two arguments where first is {@link org.bukkit.command.CommandSender}
 * and the second is {@link String[]}!
 *
 * Example:
 * {@code
 *  @ArgumentSensitive(targetSize=0, targetString="hello")
 *  public void myMethod(CommandSender sender, String[] args) {
 *      sender.sendMessage("argument sensitive method works");
 *  }
 * }
 *
 * Example method will be executed if command is executed with
 * "hello" as it's first argument (args[0]) <b>after</b> execution of
 * the command itself.
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ArgumentSensitive {

    int targetSize();

    String targetString();

}

