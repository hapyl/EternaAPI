package me.hapyl.eterna.module.player.quest;

import com.google.common.collect.Maps;
import me.hapyl.eterna.module.annotate.Immutable;
import me.hapyl.eterna.module.annotate.SingletonBehaviour;
import me.hapyl.eterna.module.registry.SimpleRegistry;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Represents a {@link QuestRegistry}.
 *
 * <p>
 * A {@link QuestRegistry} handles registering and storing {@link Quest} and a runtime {@link QuestData}.
 * </p>
 *
 * <p>
 * A {@link QuestRegistry} <b>must</b> implement {@link #save(Player, Set)} and {@link #load(Player)} methods to properly preserve
 * runtime quest data.
 * </p>
 */
@SingletonBehaviour
public abstract class QuestRegistry extends SimpleRegistry<Quest> {
    
    protected final Plugin plugin;
    protected final Map<Player, QuestDataMap> playerData;
    
    /**
     * Creates a new {@link QuestRegistry} for the given {@link Plugin}.
     *
     * @param plugin - The plugin to create for.
     * @throws IllegalArgumentException if a registry already exists for the given plugin.
     */
    public QuestRegistry(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.playerData = Maps.newHashMap();
        
        // Pass into the handler
        QuestHandler.handler.register(plugin, this);
    }
    
    /**
     * Gets the {@link Plugin} this {@link QuestRegistry} belongs to.
     *
     * @return the plugin this quest registry belongs to.
     */
    @NotNull
    public final Plugin getPlugin() {
        return plugin;
    }
    
    /**
     * Gets an <b>immutable</b> {@link List} containing all registered {@link Quest}.
     *
     * @return an <b>immutable</b> list containing all registered quests.
     */
    @NotNull
    public List<Quest> getQuests() {
        return values();
    }
    
    /**
     * Saves the given {@link QuestData} for the given {@link Player}.
     *
     * <p>
     * This method is called whenever the {@link Player} quits the server to preserve the runtime {@link QuestData} into {@link QuestRegistry} preferred storage method,
     * which is fully dependent on the implementing registry.
     * </p>
     *
     * <p>
     * The implementing registry is permitted to call this method in order to manually save the data, but it is handled by {@link QuestHandler}.
     * </p>
     *
     * @param player       - The player for whom to save the data.
     * @param questDataSet - The data to save.
     */
    public abstract void save(@NotNull Player player, @NotNull @Immutable Set<QuestData> questDataSet);
    
    /**
     * Loads the {@link QuestData} for the given {@link Player}.
     *
     * <p>
     * This method is called whenever a {@link Player} joins the server to load the runtime {@link QuestData} from {@link QuestRegistry} preferred storage method,
     * which is fully dependent on the implementing registry.
     * </p>
     *
     * <p>
     * The data must be loaded via {@link QuestData#load(Player, Quest, int, double, long, long)}.
     * </p>
     *
     * <p>
     * The implementing registry is <b><u>not</u></b> permitted to call this method manually, as it is handled by the {@link QuestHandler}.
     * </p>
     *
     * @param player - The player for whom to load the data.
     * @return the quest data to load.
     */
    @NotNull
    @ApiStatus.OverrideOnly
    public abstract Set<QuestData> load(@NotNull Player player);
    
    /**
     * Gets whether the given {@link Player} has completed the given {@link Quest}.
     *
     * @param player - The player to check.
     * @param quest  - The quest to check.
     * @return {@code true} if the player has completed the quest.
     */
    public boolean hasCompletedQuest(@NotNull Player player, @NotNull Quest quest) {
        final QuestDataMap questDataMap = playerData.get(player);
        
        if (questDataMap == null) {
            return false;
        }
        
        final QuestData questData = questDataMap.data.get(quest);
        
        return questData != null && questData.isCompleted();
    }
    
    /**
     * Gets the {@link QuestStartResponse} for the given {@link Player} and {@link Quest}.
     *
     * @param player - The player to get the response for.
     * @param quest  - The quest to get the response for.
     * @return the quest start response.
     */
    @NotNull
    public final QuestStartResponse canStartQuest(@NotNull Player player, @NotNull Quest quest) {
        final QuestDataMap questData = getOrComputePlayerData(player);
        final QuestData data = questData.data.get(quest);
        
        // If data already contains the quest, it means it's already in progress
        if (data != null) {
            // Check for whether the quest is completed
            if (data.isCompleted()) {
                return QuestStartResponse.ERROR_ALREADY_COMPLETE;
            }
            
            // If not ALREADY_COMPLETE, then it's ALREADY_STARTED
            return QuestStartResponse.ERROR_ALREADY_STARTED;
        }
        
        // Check for pre-requirements
        final QuestPreRequirement preRequirement = quest.getPreRequirement();
        
        if (preRequirement != null && !preRequirement.isMet(player)) {
            return QuestStartResponse.ERROR_PRE_REQUIREMENTS_NOT_MET;
        }
        
        // Otherwise it's OK to start the quest
        return QuestStartResponse.OK;
    }
    
    /**
     * Attempts to start the given {@link Quest} for the given {@link Player}.
     *
     * @param player - The player for whom to attempt to start the quest.
     * @param quest  - The quest to start.
     * @param silent - {@code true} to not send formatter messages for start and errors; {@code false} otherwise.
     * @return the quest response.
     */
    @NotNull
    public final QuestStartResponse startQuest(@NotNull Player player, @NotNull Quest quest, boolean silent) {
        final QuestStartResponse startResponse = canStartQuest(player, quest);
        final QuestFormatter formatter = quest.getFormatter();
        
        // If ok, just start forcefully
        if (startResponse.isOk()) {
            startQuestForcefully(player, quest, silent);
        }
        // Otherwise, handle the response
        else {
            if (!silent) {
                switch (startResponse) {
                    case ERROR_ALREADY_STARTED -> formatter.cannotStartQuestAlreadyStarted(player, quest);
                    case ERROR_PRE_REQUIREMENTS_NOT_MET -> formatter.cannotStartQuestPreRequirementNotMet(player, Objects.requireNonNull(
                            quest.getPreRequirement(),
                            "canStartQuest() returned `ERROR_PRE_REQUIREMENTS_NOT_MET` but quest doesn't have pre-requirements? Doesn't make sense!"
                    ));
                    case ERROR_ALREADY_COMPLETE -> formatter.cannotStartQuestAlreadyComplete(player, quest);
                }
            }
        }
        
        return startResponse;
    }
    
    private void startQuestForcefully(@NotNull Player player, @NotNull Quest quest, boolean silent) {
        final QuestDataMap questDataMap = getOrComputePlayerData(player);
        // Compute is fine I guess? It should never exist unless illegally called, but better than losing the data by replacing it
        final QuestData questData = questDataMap.data.computeIfAbsent(quest, _quest -> new QuestData(player, _quest, System.currentTimeMillis()));
        
        if (!silent) {
            QuestFormatter.Timings.QUEST_STARTED.schedule(() -> quest.getFormatter().questStarted(player, quest));
        }
        
        quest.onStart(player, questData);
    }
    
    @NotNull
    private QuestDataMap getOrComputePlayerData(@NotNull Player player) {
        return this.playerData.computeIfAbsent(player, _player -> QuestDataMap.createEmpty());
    }
    
    @ApiStatus.Internal
    void tryStartQuest(@NotNull Player player, @NotNull Predicate<Quest> predicate) {
        registered.values().forEach(quest -> {
            if (canStartQuest(player, quest).isOk() && predicate.test(quest)) {
                startQuestForcefully(player, quest, false);
            }
        });
    }
}
