package me.hapyl.eterna.module.player.quest;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.EternaAPI;
import me.hapyl.eterna.builtin.manager.QuestManager;
import me.hapyl.eterna.module.annotate.EventLike;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.registry.Keyed;
import me.hapyl.eterna.module.util.Described;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;

/**
 * A {@link Quest} implementation.
 * <br>
 * Note about quests:
 * <p>
 * {@link EternaAPI} <b>only</b> handles the runtime of {@link Quest}s.
 * The implementing plugin <b>must</b> handle quest loading/saving using a {@link QuestHandler}.
 * </p>
 */
public class Quest implements Keyed, Described {

    private final JavaPlugin plugin;
    private final Key key;
    private final LinkedList<QuestObjective> objectives;
    private final Set<QuestStartBehaviour> startBehaviours;

    QuestChain questChain;

    private String name;
    private String description;

    private boolean isRepeatable;

    @Nonnull private QuestFormatter formatter;

    @Nullable private QuestPreRequirement preRequirement;

    public Quest(@Nonnull JavaPlugin plugin, @Nonnull Key key) {
        this.plugin = plugin;
        this.key = key;
        this.name = "Unnamed Quest";
        this.description = "No description.";
        this.objectives = Lists.newLinkedList();
        this.formatter = QuestFormatter.DEFAULT;
        this.startBehaviours = Sets.newHashSet();
        this.preRequirement = null;
        this.isRepeatable = false;
    }

    /**
     * Gets the {@link QuestChain} this quest belongs to, or {@code null} if it doesn't belong to any.
     *
     * @return the {@link QuestChain} this quest belongs to, or {@code null} if it doesn't belong to any.
     */
    @Nullable
    public QuestChain getQuestChain() {
        return this.questChain;
    }

    /**
     * Returns {@code true} if this quest belongs to a chain, {@code false} otherwise.
     *
     * @return {@code true} if this quest belongs to a chain, {@code false} otherwise.
     */
    public boolean isChainedQuest() {
        return this.questChain != null;
    }

    /**
     * Gets a {@link Set} of {@link QuestStartBehaviour} applicable to this quest, or an empty {@link Set} if this quest it started manually.
     *
     * @return a {@link Set} of {@link QuestStartBehaviour} applicable to this quest, or an empty {@link Set} if this quest it started manually.
     */
    @Nonnull
    public Set<QuestStartBehaviour> getStartBehaviours() {
        return new HashSet<>(startBehaviours);
    }

    /**
     * Gets a {@link Set} of {@link QuestStartBehaviour} applicable to this quest
     * if this is the next {@link Quest} in the {@link QuestChain} for the given {@link Player},
     * an empty {@link Set} otherwise.
     *
     * @param player - The player to get the start behaviours for.
     * @return - a {@link Set} of {@link QuestStartBehaviour} applicable to this quest
     * if this is the next {@link Quest} in the {@link QuestChain} for the given {@link Player},
     * an empty {@link Set} otherwise.
     */
    @Nonnull
    public Set<QuestStartBehaviour> getStartBehaviours(@Nonnull Player player) {
        if (questChain != null) {
            final Quest nextQuest = questChain.getNextQuest(player);

            if (!this.equals(nextQuest)) {
                return Set.of();
            }
        }

        return getStartBehaviours();
    }

    /**
     * Adds a {@link QuestStartBehaviour} to this quest.
     *
     * @param startBehaviour - The start behaviour to add.
     */
    public void addStartBehaviour(@Nonnull QuestStartBehaviour startBehaviour) {
        this.startBehaviours.add(startBehaviour);
    }

    /**
     * Returns {@code true} if this quest has {@link QuestStartBehaviour}, {@code false} otherwise.
     *
     * @return {@code true} if this quest has {@link QuestStartBehaviour}, {@code false} otherwise.
     */
    public boolean hasStartBehaviours() {
        return !startBehaviours.isEmpty();
    }

    /**
     * Gets the pre-requirement that must be met before the {@link Quest} can be started, or {@code null} if none.
     *
     * @return the pre-requirement that must be met before the {@link Quest} can be started, or {@code null} if none.
     */
    @Nullable
    public QuestPreRequirement getPreRequirement() {
        return preRequirement;
    }

    /**
     * Sets the {@link Quest} pre-requirement that must be met before the {@link Quest} can be started.
     *
     * @param preRequirement - New pre-requirement.
     */
    public void setPreRequirement(@Nullable QuestPreRequirement preRequirement) {
        this.preRequirement = preRequirement;
    }

    /**
     * Returns {@code true} if this {@link Quest} is repeatable, false otherwise.
     * <p>
     * Repeatable quests don't call {@link QuestHandler#completeQuest(Player, Quest)} whenever the player completes the {@link Quest}.
     * </p>
     *
     * @return {@code true} if this {@link Quest} is repeatable, false otherwise.
     */
    public boolean isRepeatable() {
        return isRepeatable;
    }

    /**
     * Sets if this {@link Quest} is repeatable.
     * <p>
     * Repeatable quests don't call {@link QuestHandler#completeQuest(Player, Quest)} whenever the player completes the {@link Quest}.
     * </p>
     *
     * @param repeatable - Is repeatable.
     */
    public void setRepeatable(boolean repeatable) {
        isRepeatable = repeatable;
    }

    /**
     * Gets the {@link JavaPlugin} owning this {@link Quest}.
     *
     * @return the plugin owning this quest.
     */
    @Nonnull
    public final JavaPlugin getPlugin() {
        return plugin;
    }

    /**
     * Gets the {@link Key} of this {@link Quest}.
     *
     * @return the key of this quest.
     */
    @Nonnull
    @Override
    public final Key getKey() {
        return this.key;
    }

    /**
     * Gets the {@link QuestFormatter} for this {@link Quest}.
     *
     * @return the formatter for this quest.
     */
    @Nonnull
    public QuestFormatter getFormatter() {
        return formatter;
    }

    /**
     * Sets the {@link QuestFormatter} for this {@link Quest}.
     *
     * @param formatter - The new formatter.
     */
    public void setFormatter(@Nonnull QuestFormatter formatter) {
        this.formatter = Objects.requireNonNull(formatter, "QuestFormatter must not be null!");
    }

    /**
     * Gets the {@link QuestObjective} at the given stage, or {@code null} if there is no objective at the given.
     *
     * @param stage - Stage of the quest.
     * @return the objective at the given stage, or {@code null} if there is no objective at the given stage.
     */
    @Nullable
    public QuestObjective getObjective(int stage) {
        return stage >= 0 && stage < objectives.size() ? objectives.get(stage) : null;
    }

    /**
     * Gets the first {@link QuestObjective} of this {@link Quest}.
     *
     * @return the first objective of this quest.
     * @throws IllegalStateException If there are no objectives yet.
     */
    @Nonnull
    public QuestObjective getFirstObjective() throws IllegalStateException {
        if (objectives.isEmpty()) {
            throw new IllegalStateException("Quest must contain at least one objective!");
        }

        return objectives.getFirst();
    }

    /**
     * Adds a {@link QuestObjective} to this {@link Quest}.
     *
     * @param objective - Objective to add.
     */
    public void addObjective(@Nonnull QuestObjective objective) {
        this.objectives.add(objective);
    }

    /**
     * Gets the name of this {@link Quest}.
     *
     * @return the name of this quest.
     */
    @Nonnull
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of this {@link Quest}.
     *
     * @param name - The name to set.
     */
    @Override
    public void setName(@Nonnull String name) {
        this.name = name;
    }

    /**
     * Gets the description of this {@link Quest}.
     *
     * @return the description of this quest.
     */
    @Nonnull
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the description of this {@link Quest}.
     *
     * @param description - The description to set.
     */
    @Override
    public void setDescription(@Nonnull String description) {
        this.description = description;
    }

    /**
     * Starts this {@link Quest} for the given {@link Player}.
     *
     * @param player - Player to start the quest for.
     * @see QuestManager#startQuest(Player, Quest, boolean, boolean)
     */
    public void start(@Nonnull Player player) {
        Eterna.getManagers().quest.startQuest(player, this, true, true);
    }

    /**
     * Returns {@code true} if the given {@link Player} has completed this {@link Quest}, {@code false} otherwise.
     *
     * @param player - The player to check for.
     * @return {@code true} if the given {@link Player} has completed this {@link Quest}, {@code false} otherwise.
     */
    public boolean hasCompleted(@Nonnull Player player) {
        return Eterna.getManagers().quest.getHandler(getPlugin()).hasCompleted(player, this);
    }

    /**
     * Called whenever the {@link Player} completes this {@link Quest}.
     *
     * @param player - Player who has completed the quest.
     * @param data   - The data handling the quest completion.
     */
    @EventLike
    public void onComplete(@Nonnull Player player, @Nonnull QuestData data) {
    }

    /**
     * Returns {@code true} if the given object is a {@link Quest} and it's {@link JavaPlugin} and {@link Key} matching this one,
     * {@code false} otherwise.
     *
     * @param object - Object to compare to.
     * @return {@code true} if the given object is a {@link Quest} and it's {@link JavaPlugin} and {@link Key} matching this one,
     * {@code false} otherwise.
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
        return Objects.equals(this.plugin, that.plugin) && Objects.equals(this.key, that.key);
    }

    /**
     * Gets the hash code of this {@link Quest}.
     *
     * @return the hash code of this quest.
     */
    @Override
    public final int hashCode() {
        return Objects.hash(this.plugin, this.key);
    }

}
