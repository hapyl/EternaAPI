package test;

import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

public class TestCommand extends SimplePlayerAdminCommand {
    private TestCommand(String name) {
        super(name);
    }

    protected static TestCommand create(String name, BiConsumer<Player, String[]> consumer) {
        return new TestCommand(name) {
            @Override
            protected void execute(Player player, String[] args) {
                consumer.accept(player, args);
            }
        };
    }

    @Override
    protected void execute(Player player, String[] args) {

    }
}
