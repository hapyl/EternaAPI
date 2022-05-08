package kz.hapyl.spigotutils.module.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.BiConsumer;

public class Config {

    private final String path;
    private final String name;

    private final JavaPlugin plugin;
    private File file;
    private YamlConfiguration config;

    public Config(JavaPlugin plugin, @Nullable String path, String name) {
        this.plugin = plugin;
        this.path = path == null ? null : path.replace("\\", "/");
        this.name = name.replace(".yml", "");
        loadFile();
    }

    public Config(JavaPlugin plugin, String name) {
        this(plugin, null, name);
    }

    public File getFile() {
        return file;
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public void set(String path, Object object) {
        config.set(path, object);
    }

    public int getInt(String path, int def) {
        return config.getInt(path, def);
    }

    public String getString(String path, String def) {
        return config.getString(path, def);
    }

    public boolean getBoolean(String path, boolean def) {
        return config.getBoolean(path, def);
    }

    public double getDouble(String path, double def) {
        return config.getDouble(path, def);
    }

    public float getFloat(String path, float def) {
        return (float) getDouble(path, def);
    }

    public byte getByte(String path, byte def) {
        return (byte) config.getInt(path, def);
    }

    /**
     * Loads all DataField fields from the config.
     *
     * @return amount of fields loaded.
     * @throws IllegalArgumentException if field is final.
     */
    public final int loadDataFields() {
        return dataFieldWorker((d, f) -> {
            try {
                f.set(this, config.get(d.path()));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Writes all DataField fields to the config with their path().
     * <p>
     * Note:
     * This doesn't actually save values to the config, only writes them.
     * You must call {@link this#save()} to save the config.
     *
     * @return amount of fields written.
     * @throws IllegalArgumentException if field is final.
     * @implNote
     */
    public final int saveDataFields() {
        return dataFieldWorker((d, f) -> {
            try {
                set(d.path(), f.get(this));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private int dataFieldWorker(BiConsumer<DataField, Field> consumer) {
        int fields = 0;
        try {
            for (Field field : getClass().getDeclaredFields()) {
                final DataField dataField = field.getAnnotation(DataField.class);
                if (dataField == null) {
                    continue;
                }

                if (Modifier.isFinal(field.getModifiers())) {
                    throw new IllegalArgumentException("DataField must not be final.");
                }

                final boolean access = field.canAccess(this);
                if (!access) {
                    field.setAccessible(true);
                }

                consumer.accept(dataField, field);
                field.setAccessible(access);
                ++fields;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fields;
    }

    /**
     * Saves the config to the file.
     * You must call this method to actually save the config,
     * {@link this#set(String, Object)} only sets the value to configuration section.
     *
     * @see org.bukkit.configuration.ConfigurationSection
     */
    public final void save() {
        try {
            this.config.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves and loads config.
     */
    public final void reloadConfig() {
        save();
        loadFile();
    }

    /**
     * Saves current data fields values, reloads the config and loads data fields.
     */
    public final void reloadDataFields() {
        final int saved = saveDataFields();
        reloadConfig();
        final int loaded = loadDataFields();
        if (saved != loaded) {
            Bukkit.getLogger().warning("Saved %s data fields while loaded only %s!".formatted(saved, loaded));
        }
    }

    private void loadFile() {
        try {
            this.file = new File(getPath(), name + ".yml");
            this.config = YamlConfiguration.loadConfiguration(this.file);
            this.config.options().copyDefaults(true);
            this.save();
        } catch (Exception error) {
            error.printStackTrace();
            Bukkit.getLogger().severe("Unable to create config %s for plugin %s.".formatted(getPath(), plugin.getName()));
        }
    }

    private String getPath() {
        return plugin.getDataFolder() + (path == null ? "" : path);
    }

}
