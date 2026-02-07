package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.annotate.EventLike;
import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.player.PlayerLib;
import me.hapyl.eterna.module.player.quest.QuestData;
import me.hapyl.eterna.module.text.Capitalizable;
import me.hapyl.eterna.module.util.Validate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link QuestObjective} for the completion of which the {@link Player} must give items to a {@link Npc}.
 *
 * @see QuestObjectiveGiveKeyedItemToNpc
 */
public class QuestObjectiveGiveItemToNpc extends QuestObjective {
    
    private final Npc npc;
    private final ItemToGive itemToGive;
    
    /**
     * Creates a new {@link QuestObjectiveGiveKeyedItemToNpc}.
     *
     * @param npc      - The npc to give the item to.
     * @param material - The material of the item to give.
     * @param amount   - The amount of items to give.
     * @throws IllegalArgumentException if the given material is not {@link Material#isItem()}.
     */
    public QuestObjectiveGiveItemToNpc(@NotNull Npc npc, @NotNull Material material, final int amount) {
        this(
                npc,
                itemStack -> itemStack.getType() == Validate.requireValid(material, Material::isItem, "Material must be an item!"),
                amount,
                Capitalizable.capitalize(material)
        );
    }
    
    @ApiStatus.Internal
    QuestObjectiveGiveItemToNpc(@NotNull Npc npc, @NotNull ItemToGive itemToGive, final int amount, @NotNull String name) {
        super(Component.text("Give %s x %s to %s.".formatted(amount, name, npc.getDefaultName())), amount);
        
        this.npc = npc;
        this.itemToGive = itemToGive;
    }
    
    /**
     * Gets the {@link Npc} who to give the item.
     *
     * @return the npc who to give the item.
     */
    @NotNull
    public Npc getNpc() {
        return npc;
    }
    
    /**
     * Gets the {@link ItemToGive} that must be given to the {@link Npc}.
     *
     * @return the item that must be given to the npc.
     */
    @NotNull
    public ItemToGive getItemToGive() {
        return itemToGive;
    }
    
    /**
     * An event-like method that is called whenever the {@link Player} completes the {@link QuestObjectiveGiveItemToNpc} by giving all the items to the {@link Npc}.
     *
     * @param player - The player who has completed the objective.
     */
    @EventLike
    public void onGiveItemComplete(@NotNull Player player) {
        npc.sendMessage(player, Component.text("Thank you; that's exactly what I wanted!", NamedTextColor.GREEN));
        
        PlayerLib.playSound(player, Sound.ENTITY_VILLAGER_YES, 1.0f);
    }
    
    /**
     * An event-like method that is called whenever the {@link Player} gives the correct item to the {@link Npc}, but needs to give more.
     *
     * @param player   - The player who gave the correct item.
     * @param needMore - The amount how much more to give.
     */
    @EventLike
    public void onGiveItemNeedMore(@NotNull Player player, final int needMore) {
        npc.sendMessage(player, Component.text("Thanks, but I need %s more!".formatted(needMore), NamedTextColor.YELLOW));
        
        PlayerLib.playSound(player, Sound.ENTITY_VILLAGER_TRADE, 1.0f);
    }
    
    /**
     * An event-like method that is called whenever the {@link Player} givens the wrong item to the {@link Npc}.
     *
     * @param player - The player who gave the wrong item.
     * @return {@code true} to fail this objective, {@code false} otherwise.
     */
    @EventLike
    public boolean onGiveItemWrongItem(@NotNull Player player) {
        npc.sendMessage(player, Component.text("That's not what I asked for!", NamedTextColor.RED));
        
        PlayerLib.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f);
        return false;
    }
    
    @NotNull
    @Override
    public Response test(@NotNull QuestData questData, @NotNull QuestObjectArray array) {
        final Player player = questData.getPlayer();
        final Npc npc = array.get(0, Npc.class);
        final ItemStack item = array.get(1, ItemStack.class);
        
        if (!this.npc.equals(npc)) {
            return Response.testFailed();
        }
        
        if (item == null || !this.itemToGive.test(item)) {
            final boolean shouldFailObjective = this.onGiveItemWrongItem(player);
            
            return shouldFailObjective ? Response.objectiveFailed() : Response.testFailed();
        }
        
        final double currentStageProgress = questData.getCurrentStageProgress();
        
        final int amount = item.getAmount();
        final int needMore = (int) (super.goal - currentStageProgress);
        final int canGive = Math.min(amount, needMore);
        
        if (needMore - canGive == 0) {
            this.onGiveItemComplete(player);
        }
        else {
            this.onGiveItemNeedMore(player, needMore - canGive);
        }
        
        // Decrement item count
        item.setAmount(amount - canGive);
        
        return Response.testSucceeded(canGive);
    }
    
    /**
     * Represents the item that must be given to the npc.
     */
    @ApiStatus.NonExtendable
    public interface ItemToGive {
        boolean test(@NotNull ItemStack itemStack);
    }
}
