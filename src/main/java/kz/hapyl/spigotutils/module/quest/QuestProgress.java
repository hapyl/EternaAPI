package kz.hapyl.spigotutils.module.quest;

import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.chat.LazyClickEvent;
import kz.hapyl.spigotutils.module.chat.LazyHoverEvent;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.Queue;

public class QuestProgress {

    private final Player player;
    private final Quest quest;
    private final long totalStages;
    private final boolean autoClaimReward;

    private final Queue<PlayerQuestObjective> objectives;

    private long currentStage;

    public QuestProgress(Player player, Quest quest) {
        this.player = player;
        this.quest = quest;
        this.totalStages = quest.getObjectives().size();
        this.currentStage = 0;
        this.objectives = new LinkedList<>();
        quest.getObjectives().forEach((stage, objective) -> this.objectives.add(objective.cloneAsPlayerObjective(this)));
        this.autoClaimReward = quest.isAutoClaim();
    }

    public boolean isAutoClaimReward() {
        return this.autoClaimReward;
    }

    public Player getPlayer() {
        return this.player;
    }

    public long getTotalStages() {
        return this.totalStages;
    }

    public void nextObjective() {
        final PlayerQuestObjective completedObjective = this.objectives.poll();
        final PlayerQuestObjective nextObjective = this.objectives.peek();
        ++this.currentStage;

        if (completedObjective == null) {
            Chat.sendMessage(
                    player,
                    "&cThere was an error whilst trying to complete Quest objective! Quest rewards were saved and granted."
            );
            this.quest.grantRewardIfExist(player);
            return;
        }

        completedObjective.sendMessage(PlayerQuestObjective.Type.COMPLETE);

        // check quest completion
        if (this.isComplete()) {
            this.sendQuestCompleteInfo(player);
            if (this.isAutoClaimReward()) {
                this.quest.grantRewardIfExist(this.player);
                QuestManager.current().completeQuest(this);
            }
        }
        // else tell what to do next
        else {
            // should never trigger since it's either empty or not, but there is a check just in case
            if (nextObjective == null) {
                Chat.sendMessage(
                        player,
                        "&cThere was an error whilst trying to complete Quest objective! Quest rewards were saved and granted."
                );
                this.quest.grantRewardIfExist(player);
                return;
            }
            nextObjective.sendMessage(PlayerQuestObjective.Type.STARTED);
        }
    }

    private void sendQuestCompleteInfo(Player player) {
        getQuest().getFormatter().sendQuestCompleteFormat(player, quest);
    }

    /**
     * @return a float between 0.0 and 1.0 based of percent of quest objective completion
     */
    public float getCompletion() {
        return Math.min(1.0f, Math.max(0.0f, (float) this.currentStage / this.quest.getObjectives().size()));
    }

    public boolean isComplete() {
        return this.currentStage >= this.totalStages && this.objectives.isEmpty();
    }

    public Quest getQuest() {
        return this.quest;
    }

    public long getCurrentStage() {
        return this.currentStage;
    }

    public PlayerQuestObjective getCurrentObjective() {
        return this.objectives.peek();
    }

    public Queue<PlayerQuestObjective> getObjectives() {
        return this.objectives;
    }

    public static void loadFromData(Player player, String questId, long objective, double progress /*note that progress is not a 0.0 - 1.0 value*/) {
        final Quest quest = QuestManager.current().getById(questId);
        if (quest == null) {
            Chat.sendClickableHoverableMessage(
                    player,
                    LazyClickEvent.SUGGEST_COMMAND.of("QuestProgress={id=%s, objective=%s, progress=%s}".formatted(
                            questId,
                            objective,
                            progress
                    )),
                    LazyHoverEvent.SHOW_TEXT.of("&7Click to copy debug data."),
                    "&cCould not load progress for quest '%s' because it no longer exists!",
                    questId
            );
            return;
        }

        final QuestProgress questProgress = new QuestProgress(player, quest);

        // set progress
        for (int i = 0; i < objective; i++) {
            questProgress.objectives.poll();
        }

        questProgress.currentStage = objective;
        questProgress.getCurrentObjective().incrementGoalSilent(progress);

        QuestManager.current().addQuestProgress(player, questProgress);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("QuestProgress{");
        sb.append("player=").append(player);
        sb.append(", quest=").append(quest);
        sb.append(", totalStages=").append(totalStages);
        sb.append(", objectives=").append(objectives);
        sb.append(", currentStage=").append(currentStage);
        sb.append('}');
        return sb.toString();
    }
}
