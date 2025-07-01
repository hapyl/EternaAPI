package me.hapyl.eterna.module.command.completer;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A simpler way of {@link Checker} without the argument array.
 */
public interface CheckerNoArgs extends Checker {
    
    @Nullable
    String check(@Nonnull Player player, @Nonnull String arg);
    
    @Nullable
    @Override
    default String check(@Nonnull Player player, @Nonnull String arg, @Nonnull String[] args) {
        return check(player, arg);
    }
}
