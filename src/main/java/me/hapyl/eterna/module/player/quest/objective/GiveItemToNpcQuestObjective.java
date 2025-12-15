package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.annotate.EventLike;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.player.PlayerLib;
import me.hapyl.eterna.module.player.quest.QuestData;
import me.hapyl.eterna.module.player.quest.QuestObjectArray;
import me.hapyl.eterna.module.player.quest.QuestObjective;
import me.hapyl.eterna.module.util.Validate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * A {@link QuestObjective} for the completion of which the player must give items to a {@link Npc}.
 */
public class GiveItemToNpcQuestObjective extends QuestObjective {

    public final Npc npc;
    public final Material material;

    /**
     * Creates a new objective for the completion of which the player must give items to a npc.
     *
     * @param npc      - The NPC to give the item to.
     * @param material - The material of the item to give.
     * @param goal     - The number of items to give.
     */
    public GiveItemToNpcQuestObjective(@Nonnull Npc npc, @Nonnull Material material, double goal) {
        super("Give %s %sx %s.".formatted(npc.getDefaultName(), goal, Chat.capitalize(material)), goal);

        this.npc = npc;
        this.material = Validate.requireValid(material, Material::isItem, "Material must be an item!");
    }

    /**
     * Called whenever the player completes the objective by giving all the items to the {@link Npc}.
     *
     * @param player - The player who has completed the objective by giving all the items to the npc.
     */
    @EventLike
    public void onGiveItemComplete(@Nonnull Player player) {
        npc.sendMessage(player, Component.text("Thank you; that's exactly what I wanted!", NamedTextColor.GREEN));

        PlayerLib.playSound(player, Sound.ENTITY_VILLAGER_YES, 1.0f);
    }

    /**
     * Called whenever the player gives the correct item to the {@link Npc}, but needs to give more.
     *
     * @param player - The player who gave the correct item to the {@link Npc}.
     */
    @EventLike
    public void onGiveItemNeedMore(@Nonnull Player player) {
        npc.sendMessage(player, Component.text("Thanks, but I need more!", NamedTextColor.YELLOW));

        PlayerLib.playSound(player, Sound.ENTITY_VILLAGER_TRADE, 1.0f);
    }

    /**
     * Called whenever the player givens the wrong item to the {@link Npc}.
     *
     * @param player - The player who gave the wrong item.
     */
    @EventLike
    public void onGiveItemWrongItem(@Nonnull Player player) {
        npc.sendMessage(player, Component.text("That's not what I asked for!", NamedTextColor.RED));

        PlayerLib.playSound(Sound.ENTITY_VILLAGER_NO, 1.0f);
    }

    @Nonnull
    @Override
    public Response test(@Nonnull QuestData data, @Nonnull QuestObjectArray object) {
        final Player player = data.getPlayer();
        final Npc npc = object.getAs(0, Npc.class);
        final ItemStack item = object.getAs(1, ItemStack.class);

        if (!this.npc.equals(npc)) {
            return Response.testFailed();
        }

        if (item == null || this.material != item.getType()) {
            onGiveItemWrongItem(player);
            return Response.testFailed();
        }

        final double currentStageProgress = data.getCurrentStageProgress();

        final int amount = item.getAmount();
        final int needMore = (int) (getGoal() - currentStageProgress);
        final int canGive = Math.min(amount, needMore);

        // Complete
        if (needMore - canGive == 0) {
            onGiveItemComplete(player);
        }
        else {
            onGiveItemNeedMore(player);
        }

        // Decrement item count
        item.setAmount(amount - canGive);

        return Response.testSucceeded((double) canGive);
    }
}
