package kz.hapyl.spigotutils.module.command;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ArgumentProcessor {

    private final SimpleCommand command;
    private final List<SimpleSwitch> switches = new ArrayList<>();

    public ArgumentProcessor(SimpleCommand command) {
        this.command = command;
    }

    public SimpleSwitch when(int pos, String value) {
        final SimpleSwitch simpleSwitch = new SimpleSwitch(this, pos, value);
        switches.add(simpleSwitch);
        return simpleSwitch;
    }

    public boolean compareAndInvoke(CommandSender sender, String[] str) {
        if (switches.isEmpty()) {
            return false;
        }

        for (final SimpleSwitch sw : switches) {
            if (sw.test(str)) {
                sw.invoke(sender);
            }
        }

        return false;
    }

    public void tabComplete(List<String> strings, String[] args) {
        for (final SimpleSwitch sw : switches) {
            sw.tabComplete(strings, args);
        }
    }
}
