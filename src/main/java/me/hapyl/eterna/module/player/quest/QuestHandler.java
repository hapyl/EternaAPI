package me.hapyl.eterna.module.player.quest;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.module.registry.SimplePluginRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

/**
 * Represents a handler for quests.
 * <p>Keep in mind that Eterna only handles runtime of the quests.
 * An implementing plugin <b>must</b> implement a way to save/load quests, etc.</p>
 */
public abstract class QuestHandler extends SimplePluginRegistry<Quest> {
    
    public QuestHandler(@Nonnull JavaPlugin plugin) {
        super(plugin);
        
        Eterna.getManagers().quest.registerHandler(this);
    }
    
    /**
     * Saves the player's quest data. This method is called when a player leaves the game,
     * ensuring that all quest data is persisted.
     * <p>The event triggering this method is called
     * with {@link EventPriority#LOWEST}, ensuring that it is the first event
     * processed during the player's quiting.</p>
     *
     * @param player       – The player whose quest data is being saved.
     * @param questDataSet – The set of quest data to save.
     */
    public abstract void saveQuests(@Nonnull Player player, @Nonnull Set<QuestData> questDataSet);
    
    /**
     * Loads the player's quest data. This method is called when a player joins the game,
     * ensuring that all quest data is retrieved.
     * <p>The event triggering this method is called
     * with {@link EventPriority#MONITOR}, ensuring that it is the last event
     * processed during the player's joining, allowing other events to create databases and
     * load necessary data before loading quests.</p>
     *
     * @param player – The player whose quest data is being loaded
     * @return The set of quest data loaded for the player
     */
    @Nonnull
    public abstract Set<QuestData> loadQuests(@Nonnull Player player);
    
    /**
     * Returns {@code true} if the player has completed the given quest, {@code false} otherwise.
     *
     * @param player - The player to check.
     * @param quest  - The quest to check.
     * @return {@code true} if the player has completed the given quest, {@code false} otherwise.
     */
    public abstract boolean hasCompleted(@Nonnull Player player, @Nonnull Quest quest);
    
    /**
     * Marks the specified quest as completed for the player. This method is called
     * whenever a player completes a quest, and the implementation should store
     * the completion status to prevent the quest from being repeated.
     * <p>This method is not called if the quest is repeatable.</p>
     *
     * @param player – The player who completed the quest
     * @param quest  – The quest that was completed
     */
    public abstract void completeQuest(@Nonnull Player player, @Nonnull Quest quest);
    
    /**
     * Attempts to register the given {@link Quest} for this {@link QuestHandler}.
     *
     * @param quest - The {@link Quest} to register.
     * @return the registered {@link Quest}.
     * @throws IllegalArgumentException If the owning {@link JavaPlugin} of the given {@link Quest} is not {@link #getPlugin()}.
     */
    @Override
    public final Quest register(@Nonnull Quest quest) throws IllegalArgumentException {
        return super.register(validateQuestPlugin(quest));
    }
    
    /**
     * Gets the name of this handler.
     *
     * @return the name of this handler.
     */
    @Nonnull
    public String getName() {
        return getClass().getSimpleName();
    }
    
    /**
     * Gets a copy of all the registered {@link Quest} in this {@link QuestHandler},
     * preserving the insertion order.
     *
     * @return a copy of all registered quests in this handler.
     */
    @Nonnull
    public List<Quest> getQuests() {
        return values();
    }
    
    private Quest validateQuestPlugin(Quest quest) {
        if (quest.getPlugin() != this.getPlugin()) {
            throw new IllegalArgumentException("Illegal quest registration! Quest plugin does not matches the handler plugin!");
        }
        return quest;
    }
    
}
