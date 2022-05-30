package me.hapyl.spigotutils.module.quest.objectives;

import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.quest.QuestObjective;
import me.hapyl.spigotutils.module.quest.QuestObjectiveType;
import me.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveItemStackToNpc extends QuestObjective {

    private final ItemStack stack;
    private final HumanNPC npc;

    public GiveItemStackToNpc(ItemStack stack, HumanNPC npc) {
        super(
                QuestObjectiveType.GIVE_ITEM_STACK_TO_NPC,
                1,
                "Gifter",
                String.format("Give %s to %s.", Chat.stringifyItemStack(stack), npc.getName())
        );
        this.stack = stack;
        this.npc = npc;
    }

    @Override
    public void afterObjectiveIncrement(Player player, double value) {
        final ItemStack item = player.getInventory().getItemInMainHand();
        item.setAmount(item.getAmount() - (int) value);
    }

    @Override
    public double testQuestCompletion(Object... objects) {
        return (((ItemStack) objects[0]).isSimilar(this.stack) && objects[1].equals(this.npc)) ? this.stack.getAmount() : -1;
    }
}
