package me.hapyl.eterna.module.player.quest;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;

/**
 * Represents a list of runtime quest data, mapped per-quest.
 */
public class QuestDataList implements Iterable<QuestData> {

    public final Player player;
    public final Map<Quest, QuestData> data;

    public QuestDataList(@Nonnull Player player) {
        this.player = player;
        this.data = Maps.newHashMap();
    }

    /**
     * Gets the player of this data list.
     *
     * @return the player of this data list.
     */
    @Nonnull
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns true if the given {@link Quest} is 'active', {@code false} otherwise.
     *
     * @param quest - Quest to test.
     * @return {@code true} if the given quest is 'active', {@code false} otherwise.
     */
    public boolean hasData(@Nonnull Quest quest) {
        return data.containsKey(quest);
    }

    /**
     * Gets or computes the data for given {@link Quest}.
     *
     * @param quest - Quest to get or compute the data for.
     * @return the data for the given quest.
     */
    @Nonnull
    public QuestData getDataOrCompute(@Nonnull Quest quest) {
        return data.computeIfAbsent(quest, _quest -> new QuestData(player, _quest));
    }

    @Nonnull
    @Override
    public final Iterator<QuestData> iterator() {
        return data.values().iterator();
    }

    @Override
    public final Spliterator<QuestData> spliterator() {
        return data.values().spliterator();
    }

}
