package me.hapyl.spigotutils.module.inventory;

import me.hapyl.spigotutils.module.quest.QuestManager;
import me.hapyl.spigotutils.module.quest.QuestObjectiveType;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.function.Predicate;

public class ItemBuilderListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleInventoryClick(InventoryClickEvent ev) {
        final HumanEntity player = ev.getWhoClicked();
        final ItemStack item = ev.getCurrentItem();
        final ClickType action = ev.getClick();

        if (item == null) {
            return;
        }

        final ItemBuilder builder = ItemBuilder.getBuilderFromItem(item);
        if (builder == null || !builder.isAllowInventoryClick()) {
            return;
        }

        ev.setCancelled(true);

        builder
                .getFunctions()
                .stream()
                .filter(a -> action.isLeftClick() ? (a.hasAction(Action.LEFT_CLICK_AIR) || a.hasAction(Action.LEFT_CLICK_BLOCK)) : (
                        a.hasAction(Action.RIGHT_CLICK_AIR) || a.hasAction(Action.RIGHT_CLICK_BLOCK)))
                .forEach(func -> execute((Player) player, builder, func));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleClick(PlayerInteractEvent ev) {
        // I just don't like double clicking.
        if (ev.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }

        final Player player = ev.getPlayer();
        final Action action = ev.getAction();
        final ItemStack item = player.getInventory().getItemInMainHand();
        final ItemBuilder builder = ItemBuilder.getBuilderFromItem(item);

        if (builder == null) {
            return;
        }

        builder.getFunctions().stream().filter(f -> f.hasAction(action)).forEach(func -> execute(player, builder, func));

        if (!builder.getFunctions().isEmpty() && builder.isCancelClicks()) {
            ev.setCancelled(true);
        }
    }

    private void execute(Player player, ItemBuilder builder, ItemAction func) {
        // cooldown check
        if (builder.getCd() > 0) {
            final Predicate<Player> predicate = builder.getPredicate();
            if (predicate != null && predicate.test(player)) {
                final String error = builder.getError();
                if (!error.isEmpty()) {
                    player.sendMessage(ChatColor.RED + error);
                }
                return;
            }

            if (player.hasCooldown(builder.getItem().getType())) {
                return;
            }

            player.setCooldown(builder.getItem().getType(), builder.getCd());
        }

        func.execute(player);
        // Progress USE_CUSTOM_ITEM
        QuestManager.current().checkActiveQuests(player, QuestObjectiveType.USE_CUSTOM_ITEM, builder.getId());
    }


}
