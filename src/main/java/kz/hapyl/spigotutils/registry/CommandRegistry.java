package kz.hapyl.spigotutils.registry;

import com.google.common.collect.Sets;
import kz.hapyl.spigotutils.module.command.CommandProcessor;
import kz.hapyl.spigotutils.module.command.SimpleCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public class CommandRegistry implements Registry<SimpleCommand> {

    private final Set<SimpleCommand> registry = Sets.newHashSet();

    @Override
    public void register(SimpleCommand command) {
        registry.add(command);
        new CommandProcessor().registerCommand(command);
    }

    public void register(JavaPlugin plugin, SimpleCommand command) {
        registry.remove(command);
        new CommandProcessor(plugin).registerCommand(command);
    }

    @Override
    public void unregister(SimpleCommand command) {
        registry.remove(command);
    }

    @Override
    public Set<SimpleCommand> registeredItems() {
        return registry;
    }
}
