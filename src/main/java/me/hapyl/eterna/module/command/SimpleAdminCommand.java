package me.hapyl.eterna.module.command;

public abstract class SimpleAdminCommand extends SimpleCommand {
    /**
     * Creates a new simple command for OP players (!)
     */
    public SimpleAdminCommand(String name) {
        super(name);
        this.setAllowOnlyOp(true);
    }
}
