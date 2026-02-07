package me.hapyl.eterna.module.player.quest;

import me.hapyl.eterna.EternaColors;
import me.hapyl.eterna.module.annotate.RequiresVarargs;
import me.hapyl.eterna.module.component.Components;
import me.hapyl.eterna.module.util.Validate;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * Represents a {@link QuestPreRequirement} that must be {@link #isMet(Player)} before a {@link Quest} can be started.
 */
public interface QuestPreRequirement {
    
    /**
     * Gets whether the given {@link Player} has met the requirement.
     *
     * @param player - The player to check.
     * @return {@code true} if the given player has met the requirement; {@code false} otherwise.
     */
    boolean isMet(@NotNull Player player);
    
    /**
     * Gets the {@link Component} message that will be sent if a {@link Player} has <b>not</b> met the requirement.
     *
     * @return the message that will be sent if a player has not met the requirement.
     */
    @NotNull
    Component getMessage();
    
    /**
     * A static factory method for creating {@link QuestPreRequirement} that requires a {@link Player} to complete the given {@link Quest}.
     *
     * @param quests - The quests that must be completed.
     * @return a new pre-requirements.
     */
    @NotNull
    static QuestPreRequirement ofQuests(@NotNull @RequiresVarargs Quest... quests) {
        Validate.varargs(quests);
        
        return new QuestPreRequirement() {
            @Override
            public boolean isMet(@NotNull Player player) {
                for (Quest quest : quests) {
                    if (!quest.hasCompleted(player)) {
                        return false;
                    }
                }
                
                return true;
            }
            
            @Override
            @NotNull
            public Component getMessage() {
                return Component.empty()
                                .append(Component.text("You must complete ", EternaColors.DARK_RED))
                                .append(Components.makeComponentCommaAnd(quests, Quest::getName).color(EternaColors.DARK_RED))
                                .append(Component.text(" before starting this quest!", EternaColors.DARK_RED));
            }
        };
    }
    
    /**
     * A static factory method for creating {@link QuestPreRequirement}.
     *
     * @param predicate - The predicate to test.
     * @param message   - The message to send if the predicate fails.
     * @return a new pre-requirements.
     */
    @NotNull
    static QuestPreRequirement of(@NotNull Predicate<Player> predicate, @NotNull Component message) {
        return new QuestPreRequirement() {
            @Override
            public boolean isMet(@NotNull Player player) {
                return predicate.test(player);
            }
            
            @NotNull
            @Override
            public Component getMessage() {
                return message;
            }
        };
    }
    
}
