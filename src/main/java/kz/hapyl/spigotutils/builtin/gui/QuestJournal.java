package kz.hapyl.spigotutils.builtin.gui;

import kz.hapyl.spigotutils.Rule;
import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.inventory.ItemBuilder;
import kz.hapyl.spigotutils.module.inventory.gui.PlayerGUI;
import kz.hapyl.spigotutils.module.player.PlayerLib;
import kz.hapyl.spigotutils.module.quest.PlayerQuestObjective;
import kz.hapyl.spigotutils.module.quest.QuestManager;
import kz.hapyl.spigotutils.module.quest.QuestProgress;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Set;

public class QuestJournal extends PlayerGUI {

	public QuestJournal(Player player) {
		super(player, "Quest Journal", 5);

		if (!Rule.ALLOW_QUEST_JOURNAL.isTrue()) {
			Chat.sendMessage(player, "&cThis server does not allow Quest Journal!");
			return;
		}

		this.setOpenEvent(target -> PlayerLib.playSound(player, Sound.ITEM_BOOK_PAGE_TURN, 0.75f));
		this.setUpItems();
	}

	private void setUpItems() {
		this.clearEverything();

		this.setItem(4, new ItemBuilder(Material.PAINTING).setName("&aQuest Journal").toItemStack());
		this.setItem(40, new ItemBuilder(Material.BARRIER).setName("&cClose Menu").toItemStack(), this::closeInventory);
		this.setItem(42, new ItemBuilder(Material.NAME_TAG).setName("&aObjective Format").setLore("No Format Available").toItemStack());

		final Set<QuestProgress> quests = QuestManager.current().getActiveQuests(this.getPlayer());

		if (quests.isEmpty()) {
			this.setItem(22, new ItemBuilder(Material.CAULDRON).setName("&cNo Quests")
					.setSmartLore("You don't have any ongoing quests right now!")
					.toItemStack());
		}
		else {
			int slot = 10;
			for (final QuestProgress progress : quests) {
				final ItemBuilder builder = new ItemBuilder(Material.SKULL_BANNER_PATTERN)
						.hideFlags()
						.predicate(progress.isComplete(), ItemBuilder::glow);

				builder.setName(Chat.GREEN + progress.getQuest().getQuestName());
				builder.addLore(progress.isComplete() ? "&aFinished Quest" : "&7Ongoing Quest");
				builder.addLore();
				builder.addLore("&7Stage: &b%s", progress.isComplete()
						? "COMPLETE"
						: progress.getCurrentStage() + 1 + "/" + progress.getTotalStages());
				builder.addLore();

				builder.addLore("&7Objectives:");
				progress.getQuest().getObjectives().forEach((i, obj) -> {
					// cannot use comparing since it's another object
					// complete objective
					if (progress.getCurrentStage() > i) {
						builder.addLore(" &a✔ " + obj.getObjectiveName());
					}
					// current objective
					else if (progress.getCurrentStage() == i) {
						final PlayerQuestObjective current = progress.getCurrentObjective();
						builder.addLore(" &e→ %s%s", obj.getObjectiveName(), current.getPercentComplete());
						builder.addSmartLore(obj.getObjectiveShortInfo(), "  &7&o");
					}
					// next objectives
					else {
						builder.addLore(" &8" + (obj.isHidden() ? "???" : obj.getObjectiveName()));
					}
				});

				this.setItem(slot, builder.toItemStack());

				if (progress.isComplete()) {
					this.setClick(slot, player -> {
						progress.getQuest().grantRewardIfExist(player);
						Chat.sendMessage(player, "&a&lQUEST CLAIMED &7" + progress.getQuest().getQuestName());
						QuestManager.current().completeQuest(progress);
						PlayerLib.plingNote(player, 2.0f);
						this.setUpItems();
					});
				}

				slot += (slot % 9 == 8) ? 2 : 1;
			}
		}

		this.openInventory();
	}

}
