package me.hapyl.eterna.builtin.gui;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.inventory.gui.Interactable;
import me.hapyl.eterna.module.inventory.gui.PlayerGUI;
import me.hapyl.eterna.module.inventory.gui.SlotPattern;
import me.hapyl.eterna.module.inventory.gui.SmartComponent;
import me.hapyl.eterna.module.player.PlayerLib;
import me.hapyl.eterna.module.player.quest.Quest;
import me.hapyl.eterna.module.player.quest.QuestData;
import me.hapyl.eterna.module.player.quest.QuestObjective;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Built in GUI for {@link QuestJournal}.
 */
public final class QuestJournal extends PlayerGUI implements Interactable {

    public QuestJournal(@Nonnull Player player) {
        super(player, "Quest Journal", 5);
        
        openInventory();
    }
    
    @Override
    public void onUpdate() {
        final Set<QuestData> activeQuests = Eterna.getManagers().quest.getActiveQuests(player);
        final SmartComponent component = newSmartComponent();
        
        if (activeQuests.isEmpty()) {
            setItem(22, new ItemBuilder(Material.CAULDRON)
                    .setName("No Quests!")
                    .asIcon());
        }
        
        activeQuests.forEach(data -> {
            final Quest quest = data.getQuest();
            
            if (quest.hasCompleted(player)) {
                return;
            }
            
            final ItemBuilder builder = new ItemBuilder(Material.CREEPER_BANNER_PATTERN);
            
            builder.setName(quest.getName());
            builder.addLore();
            builder.addTextBlockLore(quest.getDescription());
            builder.addLore();
            
            builder.addLore("Current Objective:");
            
            final QuestObjective currentObjective = data.getCurrentObjective();
            
            if (currentObjective == null) {
                builder.addLore(" &8None! (somehow?)");
            }
            else {
                builder.addLore(" " + currentObjective.getName());
                builder.addLore("  &8&o" + currentObjective.getDescription());
                builder.addLore();
                builder.addLore("Progress: %s/%s".formatted(data.getCurrentStageProgress(), currentObjective.getGoal()));
            }
            
            component.add(builder.asIcon());
        });
        
        component.apply(this, SlotPattern.INNER_LEFT_TO_RIGHT, 1);
    }
    
    @Override
    public void onOpen() {
        PlayerLib.playSound(player, Sound.ITEM_BOOK_PAGE_TURN, 0.75f);
    }
    
    @Override
    public void onClose() {
    }
    
    @Override
    public void onReopen() {
    }
    
    @Override
    public void onClick(int slot, @Nonnull InventoryClickEvent event) {
    }
    
}
