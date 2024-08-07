package me.hapyl.eterna.module.command.builder;

import me.hapyl.eterna.module.util.TypeConverter;

import java.util.function.Function;

public class CommandBuilderComponent {

    private final CommandBuilder builder;

    public CommandBuilderComponent(CommandBuilder builder) {
        this.builder = builder;
    }

    public CommandBuilderComponent literal(String literal, Function<TypeConverter, Boolean> fn) {
        return null;
    }
}
