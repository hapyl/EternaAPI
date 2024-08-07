package me.hapyl.eterna.module.quest.objectives;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.quest.QuestObjective;
import me.hapyl.eterna.module.quest.QuestObjectiveType;
import me.hapyl.eterna.module.reflect.npc.HumanNPC;
import me.hapyl.eterna.module.util.Validate;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveItemsToNpc extends QuestObjective {

    private final Material material;
    private final HumanNPC npc;

    public GiveItemsToNpc(Material material, int amount, HumanNPC npc) {
        super(
                QuestObjectiveType.GIVE_ITEMS_TO_NPC,
                amount,
                "Giver",
                String.format("Give x%s %s to %s.", amount, Chat.capitalize(material), npc.getName())
        );
        this.material = material;
        this.npc = npc;
    }

    public void setNpcLineFinished(String npcLineFinished) {
        this.npc.getNPCResponses().setQuestGiveItemsFinish(npcLineFinished);
    }

    public void setNpcLineIncrement(String npcLineIncrement) {
        this.npc.getNPCResponses().setQuestGiveItemsNeedMore(npcLineIncrement);
    }

    public void setNpcLineInvalidItem(String npcLineInvalidItem) {
        this.npc.getNPCResponses().setQuestGiveItemsInvalidItem(npcLineInvalidItem);
    }

    @Override
    public void afterObjectiveIncrement(Player player, double a) {
        final ItemStack mainHand = player.getInventory().getItemInMainHand();
        mainHand.setAmount(mainHand.getAmount() - (int) a);
    }

    @Override
    public double testQuestCompletion(Object... objects) {
        if (objects.length == 3) {
            final int amount = Validate.getInt(objects[1]);
            if (objects[0].equals(material) && amount > 0 && objects[2].equals(this.npc)) {
                return amount;
            }
        }
        return -1.0d;
    }
}
