package me.hapyl.spigotutils.module.addons;

import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.util.Validate;
import me.hapyl.spigotutils.registry.Registry;

import java.io.File;
import java.io.FileNotFoundException;

public final class AddonRegistry extends Registry<String, Addon> {

    public AddonRegistry(EternaPlugin plugin) {
        super(plugin);

        loadAll();
    }

    public static AddonRegistry get() {
        return null;
    }

    public void loadAll() {
        // Create folder
        final File folder = new File(EternaPlugin.getPlugin().getDataFolder() + "/addons");

        if (!folder.exists()) {
            folder.mkdir();
        }

        final File[] files = folder.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            final String addonName = file.getName();

            if (!file.isFile() || !addonName.endsWith(".addon")) {
                continue;
            }

            try {
                final FileReader reader = new FileReader(file);
                final AddonVar typeVar = reader.nextVar(); // Read type (Always first line)

                if (!typeVar.key("type")) {
                    throw new AddonParseException(addonName, "Expected 'type: <Type>' at line %s!", reader.index());
                }

                Type type = Validate.getEnumValue(Type.class, typeVar.getValue());

                if (type == null) {
                    throw new AddonParseException(addonName, "%s is not a valid addon type!", typeVar.getValue());
                }

                switch (type) {
                    case COMMAND -> {
                        final CommandAddon addon = new CommandAddon(addonName);
                        addon.parseSuper();
                        addon.parse(reader);
                    }
                }

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        }

    }

    public void unloadAll() {

    }
}
