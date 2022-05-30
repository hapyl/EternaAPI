package me.hapyl.spigotutils.module.quest.objectives;

import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.quest.QuestObjective;
import me.hapyl.spigotutils.module.quest.QuestObjectiveType;
import org.bukkit.Material;

public class BreakItem extends QuestObjective {

    private final Material material;

    public BreakItem(Material material) {
        this(material, 1);
    }

    public BreakItem(Material material, int times) {
        super(QuestObjectiveType.BREAK_ITEM, times,
              "I didn't mean to, I swear!",
              String.format("Break %s.", Chat.capitalize(material))
        );
        this.material = material;
    }

    @Override
    public double testQuestCompletion(Object... objects) {
        return super.validateArgument(0, this.material, objects);
    }
}
