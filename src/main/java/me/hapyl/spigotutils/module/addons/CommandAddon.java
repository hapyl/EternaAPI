package me.hapyl.spigotutils.module.addons;

import me.hapyl.spigotutils.module.addons.annotate.AddonField;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CommandAddon extends Addon {

    @AddonField
    private Player player;
    @AddonField
    private String command;

    protected CommandAddon(String name) {
        super(name, Type.COMMAND);
    }

    @Override
    public boolean parse(FileReader reader) {
        final AddonVar commandVar = reader.nextVar();

        if (!commandVar.key("command")) {
            throw reader.error("Expected 'command: <String>' on line %s.", reader.index());
        }

        this.command = commandVar.getValue();

        reader.iterate(string -> {
            try {

                // field and methods
                if (string.startsWith("$")) {
                    final String substring = string.substring(1);

                    final Field field = getType().fieldByName(substring);
                    if (field != null) {
                        System.out.printf("Read field %s -> %s%n", field.getName(), field.get(this));
                    }

                    final Method method = getType().methodByName(substring);
                    if (method != null) {
                        System.out.printf("Executed method %s -> %s%n", method.getName(), method.invoke(this));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return true;
    }
}
