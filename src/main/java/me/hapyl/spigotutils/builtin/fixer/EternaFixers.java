package me.hapyl.spigotutils.builtin.fixer;

import me.hapyl.spigotutils.EternaLogger;
import me.hapyl.spigotutils.EternaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;

public enum EternaFixers {

    ENTITY_TYPES("NMSEntityType", new EternaFixerEntityTypes()),

    ;

    private final String className;
    private final EternaFixer fixer;

    EternaFixers(String className, EternaFixer fixer) {
        this.className = className;
        this.fixer = fixer;
    }

    public void tryFix() {
        final EternaPlugin plugin = EternaPlugin.getPlugin();

        EternaLogger.info("Fixing %s...".formatted(name()));

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    final File folder = new File(plugin.getDataFolder() + "/fixer");

                    if (!folder.exists()) {
                        folder.mkdirs();
                    }

                    final File file = new File(folder, name() + ".java");

                    if (!file.exists()) {
                        file.createNewFile();
                    }

                    try (FileWriter fileWriter = new FileWriter(file)) {
                        fileWriter.append("""
                                // FIXER FOR CLASS %s
                                // GENERATED AT %s
                                """.formatted(className, new Date().toString()));
                        fileWriter.append("\n");

                        fixer.fix(fileWriter);

                        fileWriter.append("\n\n");
                    }

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            EternaLogger.info("Fix written into %s!".formatted(file.getPath()));

                            final Desktop desktop = Desktop.getDesktop();

                            try {
                                desktop.open(file);
                            } catch (Exception e) {
                                // don't care
                            }
                        }
                    }.runTask(plugin);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    private String[] getPackageAndClassName(String string) {
        final int lastIndexOfDot = string.lastIndexOf(".");

        final String packageName = string.substring(0, lastIndexOfDot);
        final String className = string.substring(lastIndexOfDot + 1);

        return new String[] { packageName, className };
    }
}
