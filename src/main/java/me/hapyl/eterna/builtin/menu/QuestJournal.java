package me.hapyl.eterna.builtin.menu;

import me.hapyl.eterna.module.component.Components;
import me.hapyl.eterna.module.inventory.builder.ItemBuilder;
import me.hapyl.eterna.module.inventory.menu.ChestSize;
import me.hapyl.eterna.module.inventory.menu.PlayerMenu;
import me.hapyl.eterna.module.inventory.menu.PlayerMenuType;
import me.hapyl.eterna.module.inventory.menu.pattern.SlotPattern;
import me.hapyl.eterna.module.inventory.menu.pattern.SlotPatternApplier;
import me.hapyl.eterna.module.player.PlayerLib;
import me.hapyl.eterna.module.player.quest.Quest;
import me.hapyl.eterna.module.player.quest.QuestData;
import me.hapyl.eterna.module.player.quest.objective.QuestObjective;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Set;

/**
 * Built in menu for {@link QuestJournal}.
 */
public final class QuestJournal extends PlayerMenu {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yy hh:mm:ss a v");
    private static final Style DETAILED_DESCRIPTION_STYLE = Style.style(NamedTextColor.DARK_GRAY, TextDecoration.ITALIC);
    
    public QuestJournal(@NotNull Player player) {
        super(player, Component.text("Quest Journal"), PlayerMenuType.chest(ChestSize.SIZE_5));
        
        openMenu();
    }
    
    @Override
    public void onOpen() {
        final Set<QuestData> activeQuests = Quest.getActiveQuests(player);
        final SlotPatternApplier component = newSlotPatternApplier(SlotPattern.INNER_LEFT_TO_RIGHT, ChestSize.SIZE_2);
        
        if (activeQuests.isEmpty()) {
            setItem(22, new ItemBuilder(Material.CAULDRON)
                    .setName(Component.text("No Quests!"))
                    .asIcon());
        }
        else {
            activeQuests.forEach(data -> {
                final Quest quest = data.getQuest();
                
                final ItemBuilder builder = new ItemBuilder(Material.CREEPER_BANNER_PATTERN);
                
                builder.setName(quest.getName());
                builder.addWrappedLore(quest.getDescription());
                builder.addLore();
                
                if (data.isCompleted()) {
                    builder.addLore(Component.text("Completed!", NamedTextColor.GREEN));
                    builder.addLore(Component.text(Instant.ofEpochMilli(data.getCompletedAt()).atZone(ZoneId.systemDefault()).format(FORMATTER), NamedTextColor.DARK_GRAY));
                }
                else {
                    final QuestObjective currentObjective = data.getCurrentObjective().orElse(null);
                    
                    builder.addLore(Component.text("Current Objective:", NamedTextColor.YELLOW));
                    
                    if (currentObjective == null) {
                        builder.addLore(Component.text(" None!", NamedTextColor.GRAY));
                    }
                    else {
                        builder.addLore(Component.text(" ").append(currentObjective.getDescription()).color(NamedTextColor.GRAY));
                        
                        final Component detailedDescription = currentObjective.getDetailedDescription();
                        
                        if (!Components.isEmpty(detailedDescription)) {
                            builder.addWrappedLore(detailedDescription, _component -> Component.text("  ").append(_component.style(DETAILED_DESCRIPTION_STYLE)));
                        }
                        
                        builder.addLore();
                        builder.addLore(Component.text("Progress: ").append(Components.makeComponentFractional(data.getCurrentStageProgress(), currentObjective.getGoal())));
                    }
                    
                    builder.addLore();
                    builder.addLore(Component.text(Instant.ofEpochMilli(data.getStartedAt()).atZone(ZoneId.systemDefault()).format(FORMATTER), NamedTextColor.DARK_GRAY));
                }
                
                component.add(builder.asIcon());
            });
        }
        
        component.apply();
        
        PlayerLib.playSound(player, Sound.ITEM_BOOK_PAGE_TURN, 0.75f);
    }
    
}
