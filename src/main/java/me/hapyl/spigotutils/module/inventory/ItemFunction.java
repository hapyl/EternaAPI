package me.hapyl.spigotutils.module.inventory;

import com.google.common.collect.Sets;
import me.hapyl.spigotutils.module.annotate.ForceLowercase;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.quest.QuestManager;
import me.hapyl.spigotutils.module.quest.QuestObjectiveType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ItemFunction {

    private final String id;
    private final Consumer<Player> runnable;
    private final Set<Action> actions;

    protected boolean isAllowInventoryClick;
    protected boolean isCancelClicks;
    protected int cd;

    private Predicate<Player> predicate;
    private String errorMessage;

    public ItemFunction(@Nonnull @ForceLowercase String id, @Nonnull Consumer<Player> runnable) {
        this.id = id;
        this.runnable = runnable;
        this.actions = Sets.newHashSet();
        this.isCancelClicks = true;
        this.errorMessage = "Cannot use this!";
    }

    @Nonnull
    public String getId() {
        return id;
    }

    public boolean isAllowInventoryClick() {
        return isAllowInventoryClick;
    }

    public ItemFunction setAllowInventoryClick(boolean allowInventoryClick) {
        isAllowInventoryClick = allowInventoryClick;
        return this;
    }

    public boolean isCancelClicks() {
        return isCancelClicks;
    }

    public ItemFunction setCancelClicks(boolean cancelClicks) {
        isCancelClicks = cancelClicks;
        return this;
    }

    public int getCd() {
        return cd;
    }

    public ItemFunction setCd(int cd) {
        this.cd = cd;
        return this;
    }

    public ItemFunction setCdSec(int cd) {
        return setCd(cd * 20);
    }

    public final void run(@Nonnull Player player) {
        run(player, null);
    }

    public final void run(@Nonnull Player player, @Nullable ItemStack item) {
        // When clicked via an item, additional operations can be used, like cooldowns.
        if (item != null) {
            final Material material = item.getType();

            // Predicate
            if (predicate != null && !predicate.test(player)) {
                if (errorMessage != null) {
                    Chat.sendMessage(player, ChatColor.RED + errorMessage);
                }

                return;
            }

            // Cooldown
            if (cd > 0) {
                if (player.hasCooldown(material)) {
                    return;
                }

                player.setCooldown(material, cd);
            }
        }

        runnable.accept(player);

        // Progress USE_CUSTOM_ITEM
        QuestManager.current().checkActiveQuests(player, QuestObjectiveType.USE_CUSTOM_ITEM, id);
    }

    public boolean isAccepts(@Nonnull Action action) {
        return actions.contains(action);
    }

    public boolean isAcceptsAny(@Nonnull Action[] actions) {
        for (Action action : actions) {
            if (this.actions.contains(action)) {
                return true;
            }
        }

        return false;
    }

    public ItemFunction accept(@Nonnull Action action) {
        actions.add(action);
        return this;
    }

    public ItemFunction acceptAll(@Nonnull Action... actions) {
        this.actions.addAll(Arrays.asList(actions));
        return this;
    }

    @Nullable
    public Predicate<Player> getPredicate() {
        return predicate;
    }

    public ItemFunction setPredicate(@Nullable Predicate<Player> predicate) {
        this.predicate = predicate;
        return this;
    }

    @Nonnull
    public String getErrorMessage() {
        return errorMessage;
    }

    public ItemFunction setErrorMessage(@Nonnull String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }
}
