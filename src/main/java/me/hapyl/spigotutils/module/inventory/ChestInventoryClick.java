package me.hapyl.spigotutils.module.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ChestInventoryClick {

    private BiConsumer<Player, ChestInventory> theConsumer;
    private final Map<ClickType, BiConsumer<Player, ChestInventory>> perActionConsumer = new HashMap<>();

    public ChestInventoryClick(BiConsumer<Player, ChestInventory> consumer, ClickType... clickActions) {
        if (clickActions.length > 0) {
            for (ClickType action : clickActions) {
                perActionConsumer.put(action, consumer);
            }
        }
        else {
            theConsumer = consumer;
        }
    }

    public void addClick(BiConsumer<Player, ChestInventory> consumer, ClickType... forClicks) {
        if (forClicks.length > 0) {
            for (ClickType click : forClicks) {
                perActionConsumer.put(click, consumer);
            }
        }
    }

    @Nullable
    public BiConsumer<Player, ChestInventory> getConsumerFor(ClickType type) {
        if (theConsumer != null) {
            return theConsumer;
        }
        else {
            return perActionConsumer.getOrDefault(type, null);
        }
    }

}
