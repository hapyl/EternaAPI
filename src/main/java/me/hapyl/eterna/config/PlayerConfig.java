package me.hapyl.eterna.config;

import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.config.Config;
import me.hapyl.eterna.module.quest.QuestManager;
import me.hapyl.eterna.module.quest.QuestProgress;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Player config file managing all the data.
 * The config file is stored in the yaml format.
 */
public class PlayerConfig extends Config {

    private final String pathQuestObjective = "quest.%s.objective";
    private final String pathQuestProgress = "quest.%s.progress";

    private final Player player;

    // creates a new yaml file in plugins/EternaAPI/players/<uuid>
    // to save persistent values such as quest progress etc.
    protected PlayerConfig(Player player) {
        super(EternaPlugin.getPlugin(), "/players", player.getUniqueId().toString());
        this.player = player;
    }

    // path for quest progress is such
    // {
    //   quest:
    //     <quest_id>:
    //       objective: 0
    //       progress: 1.0
    // }
    public void saveQuestProgress() {
        final Set<QuestProgress> activeQuests = QuestManager.current().getActiveQuests(player);
        final YamlConfiguration config = getConfig();

        // reset data
        final ConfigurationSection section = config.getConfigurationSection("quest");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                config.set(section.getName() + "." + key, null);
            }
        }

        // write ongoing quest data
        for (QuestProgress progress : activeQuests) {
            config.set(pathQuestObjective.formatted(progress.getQuest().getQuestId()), progress.getCurrentStage());
            config.set(pathQuestProgress.formatted(progress.getQuest().getQuestId()), progress.getCurrentObjective().getGoalCurrent());
        }
    }

    public void loadQuestProgress() {
        final YamlConfiguration config = getConfig();
        final ConfigurationSection section = config.getConfigurationSection("quest");
        if (section == null) {
            return;
        }

        final Set<String> keys = section.getKeys(false);
        for (String key : keys) {
            final long objective = config.getLong(pathQuestObjective.formatted(key));
            final double progress = config.getDouble(pathQuestProgress.formatted(key));
            QuestProgress.loadFromData(player, key, objective, progress);
        }
    }

    public void saveAll() {
        saveQuestProgress();
        save();
    }

    public void loadAll() {
        loadQuestProgress();
    }

}
