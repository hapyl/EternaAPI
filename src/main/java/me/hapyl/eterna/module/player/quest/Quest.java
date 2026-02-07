package me.hapyl.eterna.module.player.quest;

import me.hapyl.eterna.module.annotate.EventLike;
import me.hapyl.eterna.module.annotate.Size;
import me.hapyl.eterna.module.component.Described;
import me.hapyl.eterna.module.player.quest.objective.QuestObjective;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.registry.Keyed;
import me.hapyl.eterna.module.component.Named;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Represents a {@link Quest} that consists of {@link QuestObjective}.
 *
 * <p>
 * Note that to ensure proper quest handling, all quests must be registered on start-up, <b>before</b> any player can join the server.
 * </p>
 */
public class Quest implements Keyed, Named, Described {
    
    private final QuestRegistry registry;
    private final Key key;
    private final List<QuestObjective> objectives;
    
    @NotNull private Component name;
    @NotNull private Component description;
    
    @NotNull private QuestFormatter formatter;
    
    @Nullable private QuestStartBehaviour startBehaviour;
    @Nullable private QuestPreRequirement preRequirement;
    
    /**
     * Creates a new {@link Quest}.
     *
     * <p>
     * The quest will be automatically registered in the given registry.
     * </p>
     *
     * @param registry   - The registry this quest belongs to.
     * @param key        - The key of the quest.
     * @param objectives - The objectives to complete.
     */
    public Quest(@NotNull QuestRegistry registry, @NotNull Key key, @NotNull @Size(from = 1, to = Byte.MAX_VALUE) List<? extends QuestObjective> objectives) {
        this.registry = registry;
        this.key = key;
        this.name = Named.defaultValue();
        this.description = Described.defaultValue();
        this.objectives = copyObjectives(objectives);
        this.formatter = QuestFormatter.DEFAULT;
        this.preRequirement = null;
        this.startBehaviour = null;
        
        // Register the quest
        this.registry.register(this);
    }
    
    /**
     * Gets the {@link QuestRegistry} this {@link Quest} belongs to.
     *
     * @return the registry this quest belongs to.
     */
    @NotNull
    public final QuestRegistry getRegistry() {
        return registry;
    }
    
    /**
     * Gets the {@link Key} of this {@link Quest}.
     *
     * @return the key of this quest.
     */
    @NotNull
    @Override
    public final Key getKey() {
        return this.key;
    }
    
    /**
     * Gets the name of this {@link Quest}.
     *
     * @return the name of this quest.
     */
    @Override
    @NotNull
    public Component getName() {
        return this.name;
    }
    
    /**
     * Sets the name of this {@link Quest}.
     *
     * @param name - The name to set.
     */
    @Override
    public void setName(@NotNull Component name) {
        this.name = name;
    }
    
    /**
     * Gets the description of this {@link Quest}.
     *
     * @return the description of this quest.
     */
    @Override
    @NotNull
    public Component getDescription() {
        return this.description;
    }
    
    /**
     * Sets the description of this {@link Quest}.
     *
     * @param description - The description to set.
     */
    @Override
    public void setDescription(@NotNull Component description) {
        this.description = description;
    }
    
    /**
     * Gets the {@link QuestStartBehaviour} of this {@link Quest}, or {@code null} if the {@link Quest} must be started manually.
     *
     * @return the start behaviour, or {@code null} if the quest must be started manually.
     */
    @Nullable
    public QuestStartBehaviour getStartBehaviour() {
        return startBehaviour;
    }
    
    /**
     * Sets the {@link QuestStartBehaviour} of this {@link Quest}.
     *
     * @param startBehaviour - The start behaviour to set, or {@code null} to remove.
     */
    public void setStartBehaviour(@Nullable QuestStartBehaviour startBehaviour) {
        this.startBehaviour = startBehaviour;
    }
    
    /**
     * Gets the {@link QuestPreRequirement} for this {@link Quest}.
     *
     * @return the pre-requirements for this quest.
     */
    @Nullable
    public QuestPreRequirement getPreRequirement() {
        return preRequirement;
    }
    
    /**
     * Sets the {@link QuestPreRequirement} for this {@link Quest}.
     *
     * @param preRequirement - The pre-requirements to set, or {@code null} to remove.
     */
    public void setPreRequirement(@Nullable QuestPreRequirement preRequirement) {
        this.preRequirement = preRequirement;
    }
    
    /**
     * Gets the {@link QuestFormatter} for this {@link Quest}.
     *
     * @return the formatter for this quest.
     */
    @NotNull
    public QuestFormatter getFormatter() {
        return formatter;
    }
    
    /**
     * Sets the {@link QuestFormatter} for this {@link Quest}.
     *
     * @param formatter - The formatter to set.
     */
    public void setFormatter(@NotNull QuestFormatter formatter) {
        this.formatter = Objects.requireNonNull(formatter, "QuestFormatter must not be null!");
    }
    
    /**
     * Gets the total number of {@link QuestObjective} for this {@link Quest}.
     *
     * @return the total number of objectives for this quest.
     */
    public int getObjectiveCount() {
        return objectives.size();
    }
    
    /**
     * Gets the {@link QuestObjective} at the given {@code stage} (index).
     *
     * @param stage - The objective stage.
     * @return the objective for the given stage, or {@code null} if out of bounds.
     */
    @Nullable
    public QuestObjective getObjective(int stage) {
        return stage < 0 || stage >= objectives.size() ? null : objectives.get(stage);
    }
    
    /**
     * Gets the first {@link QuestObjective} for this {@link Quest}.
     *
     * @return the first objective for this quest.
     */
    @NotNull
    public QuestObjective getFirstObjective() {
        return objectives.getFirst();
    }
    
    /**
     * Gets the last {@link QuestObjective} for this {@link Quest}.
     *
     * @return the last objective for this quest.
     */
    @NotNull
    public QuestObjective getLastObjective() {
        return objectives.getLast();
    }
    
    /**
     * Attempts to start this {@link Quest} for the given {@link Player}.
     *
     * @param player - The player for whom to start the quest.
     * @return the {@link QuestStartResponse}.
     */
    @NotNull
    public QuestStartResponse start(@NotNull Player player) {
        return registry.startQuest(player, this, false);
    }
    
    /**
     * Gets whether the given {@link Player} can start this {@link Quest}.
     *
     * @param player - The player to check.
     * @return the {@link QuestStartResponse}.
     */
    @NotNull
    public QuestStartResponse canStart(@NotNull Player player) {
        return registry.canStartQuest(player, this);
    }
    
    /**
     * Gets whether the given {@link Player} has completed this {@link Quest}.
     *
     * @param player - The player to check.
     * @return {@code true} if the given player has completed this quest; {@code false} otherwise.
     */
    public boolean hasCompleted(@NotNull Player player) {
        return registry.hasCompletedQuest(player, this);
    }
    
    /**
     * An event-like method that is called whenever a {@link Player} starts this {@link Quest}.
     *
     * @param player - The player who started the quest.
     * @param data   - The quest data for the quest.
     */
    @EventLike
    public void onStart(@NotNull Player player, @NotNull QuestData data) {
    }
    
    /**
     * An event-like method that is called whenever a {@link Player} completes this {@link Quest}.
     *
     * @param player - The player who completed the quest.
     * @param data   - The quest data for the quest.
     */
    @EventLike
    public void onComplete(@NotNull Player player, @NotNull QuestData data) {
    }
    
    /**
     * An event-like method that is called whenever a {@link Player} fails this {@link Quest}.
     *
     * @param player - The player who failed the quest.
     * @param data   - The quest data for the quest.
     */
    @EventLike
    public void onFail(@NotNull Player player, @NotNull QuestData data) {
    }
    
    /**
     * Gets the hashcode fot this {@link Quest}.
     *
     * @return the hashcode for this quest.
     */
    @Override
    public final int hashCode() {
        return Objects.hash(this.registry.plugin, this.key);
    }
    
    /**
     * Compares the given {@link Object} to this {@link Quest}.
     *
     * @param object - The object to compare to.
     * @return {@code true} if the given object is a quest, and it's plugin and key matches; {@code false} otherwise.
     */
    @Override
    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        
        final Quest that = (Quest) object;
        return Objects.equals(this.registry.plugin, that.registry.plugin) && Objects.equals(this.key, that.key);
    }
    
    /**
     * Gets a {@link TreeSet} of currently active {@link Quest} for the given {@link Player}, whether they're completed or not.
     *
     * <p>
     * The {@link TreeSet} is sorted based on alphabetical value of the {@link Key}.
     * </p>
     *
     * @param player - The player to get the quests for.
     * @return active quests for the player.
     */
    @NotNull
    public static TreeSet<QuestData> getActiveQuests(@NotNull Player player) {
        return QuestHandler.handler.stream()
                                   .filter(registry -> registry.playerData.containsKey(player))
                                   .map(registry -> registry.playerData.get(player))
                                   .flatMap(questData -> questData.data.values().stream())
                                   .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(questData -> questData.getQuest().getKey()))));
    }
    
    @NotNull
    private static List<QuestObjective> copyObjectives(@NotNull List<? extends QuestObjective> objectives) {
        if (objectives.isEmpty()) {
            throw new IllegalArgumentException("There must be at least one objective!");
        }
        
        return List.copyOf(objectives);
    }
    
}
