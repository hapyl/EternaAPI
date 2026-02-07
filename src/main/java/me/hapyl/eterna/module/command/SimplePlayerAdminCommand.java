package me.hapyl.eterna.module.command;

import org.bukkit.entity.Player;

/**
 * Creates a new {@link SimpleCommand} that is only available for {@link Player} who are operators.
 */
public abstract class SimplePlayerAdminCommand extends SimplePlayerCommand {
    
    /**
     * Creates a new {@link SimplePlayerAdminCommand}.
     *
     * @param name - The name of the command.
     */
    public SimplePlayerAdminCommand(String name) {
        super(name);
        
        this.setAllowOnlyOp(true);
        this.setAllowOnlyPlayer(true);
    }
}
