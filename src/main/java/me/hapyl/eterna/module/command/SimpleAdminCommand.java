package me.hapyl.eterna.module.command;

import org.jetbrains.annotations.NotNull;

/**
 * Creates a new {@link SimpleCommand} that is only available for operators.
 */
public abstract class SimpleAdminCommand extends SimpleCommand {
    
    /**
     * Creates a new {@link SimpleAdminCommand}.
     *
     * @param name - The name of the command.
     */
    public SimpleAdminCommand(@NotNull String name) {
        super(name);
        this.setAllowOnlyOp(true);
    }
}
