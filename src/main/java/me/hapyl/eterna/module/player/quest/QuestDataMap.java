package me.hapyl.eterna.module.player.quest;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;

/**
 * Represents an internal {@link QuestDataMap} implementation.
 */
@ApiStatus.Internal
public /* final */ class QuestDataMap implements Iterable<QuestData> {
    
    protected final Map<Quest, QuestData> data;
    
    private QuestDataMap(@NotNull Map<Quest, QuestData> data) {
        this.data = data;
    }
    
    @NotNull
    @Override
    public Iterator<QuestData> iterator() {
        return data.values().iterator();
    }
    
    @NotNull
    static QuestDataMap createEmpty() {
        return new QuestDataMap(Maps.newHashMap());
    }
    
    @NotNull
    static QuestDataMap createExisting(@NotNull Map<Quest, QuestData> existingMap) {
        return new QuestDataMap(existingMap);
    }
    
}
