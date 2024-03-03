package me.hapyl.spigotutils.module.command.builder;

import me.hapyl.spigotutils.module.command.SimpleCommand;
import me.hapyl.spigotutils.module.util.Builder;
import me.hapyl.spigotutils.module.util.TypeConverter;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class CommandBuilder implements Builder<SimpleCommand> {

    private final String name;

    public CommandBuilder(String name) {
        this.name = name;
    }

    public CommandBuilderComponent literal(@Nonnull String literal) {
        return literal(literal, t -> true);
    }

    public CommandBuilderComponent literal(@Nonnull String literal, @Nonnull Function<TypeConverter, Boolean> fn) {
        return new CommandBuilderComponent(this).literal(literal, fn);
    }

    @Nonnull
    @Override
    public SimpleCommand build() {
        return null;
    }
}
