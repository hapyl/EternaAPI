package me.hapyl.eterna.module.quest.objectives;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.quest.QuestObjective;
import me.hapyl.eterna.module.quest.QuestObjectiveType;
import org.bukkit.Material;

public class PlaceBlocks extends QuestObjective {

    private final Material material;

    public PlaceBlocks(Material materialType, long goalTotal) {
        super(
                QuestObjectiveType.PLACE_BLOCKS,
                goalTotal,
                Chat.stringifyItemName(materialType) + " Placer",
                String.format("Place %s blocks of %s.", goalTotal, Chat.stringifyItemName(materialType))
        );
        this.material = materialType;
    }

    @Override
    public double testQuestCompletion(Object... objects) {
        return super.validateArgument(0, this.material, objects);
        //		return ((Block) objects[0]).getType() == this.material ? 1.0d : -1.0d;
    }
}
