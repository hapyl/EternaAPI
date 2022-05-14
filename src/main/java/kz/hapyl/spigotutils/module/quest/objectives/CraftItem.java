package kz.hapyl.spigotutils.module.quest.objectives;

import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.quest.QuestObjective;
import kz.hapyl.spigotutils.module.quest.QuestObjectiveType;
import org.bukkit.Material;

// fixme: 014. 14/05/2022 - fix shift crafting count as 1 item crafted
public class CraftItem extends QuestObjective {

    private final Material material;

    public CraftItem(Material material, long goalTotal) {
        super(QuestObjectiveType.CRAFT_ITEM, goalTotal, "Crafter", String.format("Craft a %s.", Chat.capitalize(material)));
        this.material = material;
    }

    @Override
    public double testQuestCompletion(Object... objects) {
        return super.validateArgument(0, this.material, objects);
    }

}
