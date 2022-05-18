package kz.hapyl.spigotutils.module.inventory;

import kz.hapyl.spigotutils.module.quest.QuestManager;
import kz.hapyl.spigotutils.module.quest.QuestObjectiveType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Set;
import java.util.function.Predicate;

public class ItemBuilderListener implements Listener {

    @EventHandler()
    private void handleClick(PlayerInteractEvent ev) {
        // I just don't like double clicking.
        if (ev.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }

        final Player player = ev.getPlayer();
        final Action action = ev.getAction();
        final ItemStack item = player.getInventory().getItemInMainHand();
        final String itemId = ItemBuilder.getItemID(item);

        if (ItemBuilder.itemsWithEvents.isEmpty() || !ItemBuilder.itemsWithEvents.containsKey(itemId)) {
            return;
        }

        final ItemBuilder builder = ItemBuilder.itemsWithEvents.getOrDefault(itemId, null);

        if (builder == null) {
            return;
        }

        final Set<ItemAction> functions = builder.getFunctions();
        functions.stream().filter(f -> f.hasAction(action)).forEach(func -> {
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
        });

    }


}
