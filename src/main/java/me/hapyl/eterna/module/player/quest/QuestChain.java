package me.hapyl.eterna.module.player.quest;

import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.registry.Keyed;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Range;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Represents a chain of quests that are automatically handled in sequence.
 * <p>Each quest in the chain must be non-repeatable.</p>
 */
public class QuestChain implements Keyed {

    private final Key key;
    private final Queue<Quest> chainedQuests;

    public QuestChain(@Nonnull Key key) {
        this.key = key;
        this.chainedQuests = new ArrayDeque<>();
    }

    /**
     * Gets the key of this chain.
     */
    @Nonnull
    @Override
    public final Key getKey() {
        return this.key;
    }

    /**
     * Gets a copy of the quest chain.
     *
     * @return a copy of the quest chain.
     */
    @Nonnull
    public Queue<Quest> getQuests() {
        return new ArrayDeque<>(chainedQuests);
    }

    /**
     * Adds the given quest to this quest chain.
     *
     * @param quest - The quest to add.
     * @throws IllegalArgumentException If you attempt to add a repeatable quest to this quest chain.
     */
    public final void addQuest(@Nonnull Quest quest) {
        if (quest.isRepeatable()) {
            throw new IllegalArgumentException("Repeatable quests cannot be chained!");
        }

        quest.questChain = this;

        chainedQuests.add(quest);
    }

    /**
     * Adds the given quests to this quest chain.
     *
     * @param quests - The quests to add.
     * @throws IllegalArgumentException If you attempt to add a repeatable quest to this quest chain.
     */
    public final void addQuests(@Nonnull Quest... quests) {
        for (Quest quest : quests) {
            addQuest(quest);
        }
    }

    /**
     * Gets the next quest in the chain that the player has not yet completed.
     *
     * @param player - The player whose progress is being checked.
     * @return the next incomplete quest for the player, or {@code null} if all quests are completed.
     */
    @Nullable
    public Quest getNextQuest(@Nonnull Player player) {
        for (Quest quest : chainedQuests) {
            if (!quest.hasCompleted(player)) {
                return quest;
            }
        }

        return null;
    }

    /**
     * Gets the number of quests in this chain that the player has completed.
     *
     * @param player - The player whose completed quests count is being retrieved.
     * @return the number of completed quests.
     */
    public int getCompletedQuestsCount(@Nonnull Player player) {
        int count = 0;

        for (Quest quest : chainedQuests) {
            if (quest.hasCompleted(player)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Gets the player's completion progress in this quest chain as a percentage.
     *
     * @param player - The player whose progress is being calculated.
     * @return the completion progress as a double, ranging from 0.0 (no quests completed) to 1.0 (all quests completed).
     */
    public @Range(from = 0, to = 1) double getCompletionProgress(@Nonnull Player player) {
        return (double) getCompletedQuestsCount(player) / chainedQuests.size();
    }

    /**
     * Checks if the player has completed all quests in this chain.
     *
     * @param player - The player whose progress is being checked.
     * @return {@code true} if all quests are completed, {@code false} otherwise.
     */
    public boolean hasCompletedAllQuests(@Nonnull Player player) {
        return getNextQuest(player) == null;
    }

    /**
     * Starts the next quest in the chain for the player, if available.
     *
     * @param player - The player who will start the next quest.
     * @return {@code true} if the next quest was started, {@code false} if there are no more quests to start.
     */
    public boolean startNextQuest(@Nonnull Player player) {
        final Quest nextQuest = getNextQuest(player);

        if (nextQuest != null) {
            nextQuest.start(player);
            return true;
        }

        return false;
    }

}
