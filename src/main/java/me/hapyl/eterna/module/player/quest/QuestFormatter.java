package me.hapyl.eterna.module.player.quest;

import me.hapyl.eterna.Runnables;
import me.hapyl.eterna.module.component.Components;
import me.hapyl.eterna.module.component.Formatter;
import me.hapyl.eterna.module.player.PlayerLib;
import me.hapyl.eterna.module.player.quest.objective.QuestObjective;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link QuestFormatter} for a {@link Quest}.
 */
public interface QuestFormatter extends Formatter {
    
    /**
     * Defins the default formatter used.
     */
    @NotNull
    QuestFormatter DEFAULT = new QuestFormatter() {
        @Override
        public void objectiveStarted(@NotNull Player player, @NotNull QuestObjective objective) {
            outlined(player, NamedTextColor.GOLD, () -> {
                player.sendMessage(Component.text("ɴᴇᴡ ᴏʙᴊᴇᴄᴛɪᴠᴇ", NamedTextColor.YELLOW, TextDecoration.BOLD));
                player.sendMessage(Component.text(" ").append(objective.getDescription().color(NamedTextColor.GRAY)));
                
                final Component detailedDescription = objective.getDetailedDescription();
                
                if (!Components.isEmpty(detailedDescription)) {
                    player.sendMessage(Component.text("  ").append(detailedDescription).color(NamedTextColor.DARK_GRAY));
                }
            });
            
            PlayerLib.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.25f);
        }
        
        @Override
        public void objectiveComplete(@NotNull Player player, @NotNull QuestObjective objective) {
            outlined(player, NamedTextColor.GREEN, () -> {
                player.sendMessage(Component.text("ᴏʙᴊᴇᴄᴛɪᴠᴇ ᴄᴏᴍᴘʟᴇᴛᴇ", NamedTextColor.DARK_GREEN, TextDecoration.BOLD));
                player.sendMessage(Component.text(" ✔ ").append(objective.getDescription()).color(NamedTextColor.GREEN));
            });
            
            PlayerLib.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f);
        }
        
        @Override
        public void objectiveFailed(@NotNull Player player, @NotNull QuestObjective objective, @NotNull QuestObjective.FailType failType) {
            outlined(player, NamedTextColor.DARK_RED, () -> {
                player.sendMessage(Component.text("ᴏʙᴊᴇᴄᴛɪᴠᴇ ꜰᴀɪʟᴇᴅ", NamedTextColor.RED, TextDecoration.BOLD));
                player.sendMessage(Component.text(" ❌ ").append(objective.getDescription()).color(NamedTextColor.RED));
                
                if (failType == QuestObjective.FailType.RESTART_OBJECTIVE) {
                    player.sendMessage(Component.empty());
                    player.sendMessage(Component.text("It's ok, try again!", NamedTextColor.GRAY));
                }
            });
            
            PlayerLib.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 0.0f);
        }
        
        @Override
        public void questStarted(@NotNull Player player, @NotNull Quest quest) {
            outlined(player, NamedTextColor.YELLOW, () -> {
                player.sendMessage(Component.text("QUEST STARTED!", NamedTextColor.GOLD, TextDecoration.BOLD));
                player.sendMessage(Component.text(" ").append(quest.getName()).color(NamedTextColor.GREEN));
                player.sendMessage(Component.empty());
                
                player.sendMessage(Component.text("ᴏʙᴊᴇᴄᴛɪᴠᴇ", NamedTextColor.GREEN, TextDecoration.BOLD));
                player.sendMessage(Component.text(" ").append(quest.getFirstObjective().getDescription()).color(NamedTextColor.GRAY).decorate(TextDecoration.ITALIC));
            });
        }
        
        @Override
        public void questComplete(@NotNull Player player, @NotNull Quest quest) {
            outlined(player, NamedTextColor.YELLOW, () -> {
                player.sendMessage(Component.text("QUEST COMPLETED", NamedTextColor.GOLD, TextDecoration.BOLD));
                player.sendMessage(Component.text(" ").append(quest.getName()).color(NamedTextColor.WHITE));
            });
            
            PlayerLib.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 2.0f);
        }
        
        @Override
        public void questFailed(@NotNull Player player, @NotNull Quest quest) {
            outlined(player, NamedTextColor.DARK_RED, () -> {
                player.sendMessage(Component.text("QUEST FAILED", NamedTextColor.RED, TextDecoration.BOLD));
                player.sendMessage(Component.text(" ").append(quest.getName()).color(NamedTextColor.WHITE));
            });
        }
        
        @Override
        public void cannotStartQuestAlreadyComplete(@NotNull Player player, @NotNull Quest quest) {
            player.sendMessage(Component.text("You have already completed this quest!", NamedTextColor.RED));
        }
        
        @Override
        public void cannotStartQuestPreRequirementNotMet(@NotNull Player player, @NotNull QuestPreRequirement preRequirement) {
            player.sendMessage(preRequirement.getMessage().color(NamedTextColor.RED));
        }
        
        @Override
        public void cannotStartQuestAlreadyStarted(@NotNull Player player, @NotNull Quest quest) {
            player.sendMessage(Component.text("You have already started this quest!", NamedTextColor.RED));
        }
        
        private static void outlined(@NotNull Player player, @NotNull TextColor color, @NotNull Runnable then) {
            final TextComponent line = Component.text("═══════════════════════════════════", color).shadowColor(null);
            
            player.sendMessage(line);
            then.run();
            player.sendMessage(line);
        }
    };
    
    @NotNull
    @Override
    default Component defineFormatter() {
        return Component.text("quest");
    }
    
    /**
     * Sends a message for when a {@link QuestObjective} is started.
     *
     * @param player    - The player for whom the objective started.
     * @param objective - The started objective.
     */
    void objectiveStarted(@NotNull Player player, @NotNull QuestObjective objective);
    
    /**
     * Sends a message for when a {@link QuestObjective} is complete.
     *
     * @param player    - The player who completed the objective.
     * @param objective - The completed objective.
     */
    void objectiveComplete(@NotNull Player player, @NotNull QuestObjective objective);
    
    /**
     * Sends a message for when a {@link QuestObjective} is failed.
     *
     * @param player    - The player who failed the objective.
     * @param objective - The failed objective.
     * @param failType  - The fail type.
     */
    void objectiveFailed(@NotNull Player player, @NotNull QuestObjective objective, @NotNull QuestObjective.FailType failType);
    
    /**
     * Sends a message for when a {@link Quest} is started.
     *
     * @param player - The player for whom the quest started.
     * @param quest  - The started quest.
     */
    void questStarted(@NotNull Player player, @NotNull Quest quest);
    
    /**
     * Sends a message for when a {@link Quest} is complete.
     *
     * @param player - The player complete the quest.
     * @param quest  - The completed quest.
     */
    void questComplete(@NotNull Player player, @NotNull Quest quest);
    
    /**
     * Sends a message for when a {@link Quest} is failed.
     *
     * @param player - The player who failed the quest.
     * @param quest  - The failed quest.
     */
    void questFailed(@NotNull Player player, @NotNull Quest quest);
    
    /**
     * Sends a message for when a {@link Player} attempts to start a {@link Quest} that is already complete.
     *
     * @param player - The player who attempted to start the quest.
     * @param quest  - The quest that player has attempted to start.
     */
    void cannotStartQuestAlreadyComplete(@NotNull Player player, @NotNull Quest quest);
    
    /**
     * Sends a message for when a {@link Player} attempts to start a {@link Quest} that is currently in progress.
     *
     * @param player - The player who attempted to start the quest.
     * @param quest  - The quest that player has attempted to start.
     */
    void cannotStartQuestAlreadyStarted(@NotNull Player player, @NotNull Quest quest);
    
    /**
     * Sends a message for when a {@link Player} attempts to start a {@link Quest} but has not the {@link QuestPreRequirement}.
     *
     * @param player         - The player who attempted to start the quest.
     * @param preRequirement - The pre-requirements that has not been met.
     */
    void cannotStartQuestPreRequirementNotMet(@NotNull Player player, @NotNull QuestPreRequirement preRequirement);
    
    /**
     * Defines the internal {@link Timings} for {@link QuestFormatter}.
     */
    @ApiStatus.Internal
    enum Timings {
        OBJECTIVE_STARTED(30),
        OBJECTIVE_COMPLETE(10),
        OBJECTIVE_FAILED(10),
        
        QUEST_STARTED(-1),
        QUEST_COMPLETE(30),
        
        // Quests can only fail if objective is failed, and it's fail type is FAIL_QUEST,
        // so delay by +10 ticks from OBJECTIVE_FAILED.
        QUEST_FAILED(OBJECTIVE_FAILED.delay + 10);
        
        private final int delay;
        
        Timings(int delay) {
            this.delay = delay;
        }
        
        void schedule(@NotNull Runnable runnable) {
            if (delay == -1) {
                runnable.run();
            }
            else {
                Runnables.later(runnable, delay);
            }
        }
        
    }
    
}
