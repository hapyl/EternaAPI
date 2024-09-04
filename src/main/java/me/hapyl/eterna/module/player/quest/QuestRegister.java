package me.hapyl.eterna.module.player.quest;

import javax.annotation.Nonnull;

/**
 * Represents a class that can register quests for the given {@link QuestHandler}.
 */
public interface QuestRegister {

    /**
     * Registers all the quest owned by this class to the given {@link QuestHandler}.
     * <pre>{@code
     * class MyQuestHandler extends QuestHandler {
     *      MyQuestHandler() {
     *          questRegister1.registerQuests(this);
     *          questRegister2.registerQuests(this);
     *      }
     * }
     *
     * // Instead of
     * class MyQuestHandler extends QuestHandler {
     *      MyQuestHandler() {
     *          QuestRegister questRegister1 = ...;
     *          register(questRegister1.quest1);
     *          register(questRegister1.quest2);
     *          register(questRegister1.quest3);
     *          register(questRegister1.quest4);
     *          register(questRegister1.quest5);
     *          register(questRegister1.quest6);
     *          register(questRegister1.quest7);
     *      }
     * }
     * }</pre>
     *
     * @param handler - Handler to register quests to.
     */
    void registerQuests(@Nonnull QuestHandler handler);

}