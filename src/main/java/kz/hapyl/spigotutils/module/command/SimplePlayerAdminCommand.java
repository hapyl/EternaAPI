package kz.hapyl.spigotutils.module.command;

public abstract class SimplePlayerAdminCommand extends SimplePlayerCommand {
    /**
     * Creates a new simple command
     *
     * @param name - Name of the command.
     */
    public SimplePlayerAdminCommand(String name) {
        super(name);
        this.setAllowOnlyOp(true);
    }
}
