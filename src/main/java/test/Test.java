package test;

import org.bukkit.event.Listener;

public abstract class Test implements Listener {

    public void test() {
        Commands.createCommands();
    }

}
