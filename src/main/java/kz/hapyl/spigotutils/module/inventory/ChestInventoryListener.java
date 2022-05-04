package kz.hapyl.spigotutils.module.inventory;

import kz.hapyl.spigotutils.EternaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

import static kz.hapyl.spigotutils.module.inventory.ChestInventory.getPlayerInventory;

public class ChestInventoryListener implements Listener {

    @EventHandler
    public void handleInventoryOpen(InventoryOpenEvent ev) {
        final Player player = (Player) ev.getPlayer();
        final ChestInventory playerInventory = getPlayerInventory(player);

        if (playerInventory == null) {
            return;
        }

        if (playerInventory.isValidMenu(ev.getInventory())) {

            if (playerInventory.getAtOpen() != null) {
                playerInventory.getAtOpen().accept(player, playerInventory);
            }

            if (playerInventory.getOpenSound() != null) {
                player.playSound(player.getLocation(), playerInventory.getOpenSound(),
                                 SoundCategory.MASTER, 2, playerInventory.getOpenSoundPitch()
                );
            }
        }
    }

    @EventHandler()
    public void handleInventoryDrag(InventoryDragEvent ev) {
        final Player player = (Player) ev.getWhoClicked();
        final ChestInventory playerInventory = getPlayerInventory(player);

        if (playerInventory == null) {
            return;
        }

        if (playerInventory.isValidMenu(ev.getInventory())) {
            if (!playerInventory.isAllowDrag()) {
                ev.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void handleInventoryClick(InventoryClickEvent ev) {
        final Player player = (Player) ev.getWhoClicked();
        final ChestInventory currentInventory = getPlayerInventory(player);
        final ClickType clickType = ev.getClick();
        final int rawSlot = ev.getRawSlot();

        if (currentInventory == null) {
            return;
        }

        if (currentInventory.isValidMenu(ev.getInventory())) {

            if (!currentInventory.isPredicate()) {
                player.sendMessage(ChatColor.RED + "This menu is temporary disabled!");
                player.closeInventory();
                ev.setCancelled(true);
                return;
            }

            if (currentInventory.isCancelClick()) {
                if (currentInventory.isCancelOnlyMenu()) {
                    if (rawSlot < currentInventory.getSize()) {
                        if (!currentInventory.isSlotIgnored(rawSlot)) {
                            ev.setCancelled(true);
                        }
                    }
                }
                else {
                    if (!currentInventory.isSlotIgnored(rawSlot)) {
                        ev.setCancelled(true);
                    }
                }
            }

            if (!currentInventory.isBuilt()) {
                throw new ChestInventoryError("Menu you trying access havent't built yet!");
            }

            if (clickType == ClickType.SWAP_OFFHAND && !currentInventory.isAllowShiftClick()) {
                ev.setCancelled(true);
                player.getInventory().setItemInOffHand(player.getInventory().getItemInOffHand());
            }

            if (clickType == ClickType.DOUBLE_CLICK && !currentInventory.isAllowDoubleClick()) {
                ev.setCancelled(true);
                return;
            }

            if (clickType.isShiftClick()) {
                if (rawSlot <= currentInventory.getSize()) {
                    if (!currentInventory.isAllowShiftClickMenu()) {
                        ev.setCancelled(true);
                    }
                }
                else if (!currentInventory.isAllowShiftClick()) {
                    ev.setCancelled(true);
                }
            }

            // runnable impl
            if (!currentInventory.getRunnablePerSlot().isEmpty()) {
                final Map<Integer, ChestInventoryRunnable> hash = new HashMap<>(currentInventory.getRunnablePerSlot());
                final ChestInventoryRunnable run = hash.get(rawSlot);
                if (run != null) {
                    final Runnable currentClick = run.getRunnable(clickType);
                    if (currentClick != null) {
                        currentClick.run();
                    }
                }
            }

            if (currentInventory.getOnEveryClick() != null) {
                if (currentInventory.getOnEveryClickDelay() > 0) {
                    Bukkit.getScheduler().runTaskLater(EternaPlugin.getPlugin(), () -> {
                        // Check if it's still a thing, since if menu changed it will still do the action
                        if (currentInventory.isValidMenu(ev.getClickedInventory())) {
                            currentInventory.getOnEveryClick().accept(player, currentInventory);
                        }
                    }, currentInventory.getOnEveryClickDelay());
                }
                else {
                    currentInventory.getOnEveryClick().accept(player, currentInventory);
                }
            }

            if (currentInventory.getCloseMenuSlot() == rawSlot) {
                player.closeInventory();
                return;
            }

            if (currentInventory.getOutside() != null && rawSlot == -999) {
                currentInventory.getOutside().accept(player, currentInventory);
            }

            if (currentInventory.getConsumerClickEvent() != null) {
                currentInventory.getConsumerClickEvent().accept(ev, currentInventory);
            }

            if (ev.isCancelled()) {
                ev.setCancelled(true);
            }

            final Map<Integer, ChestInventoryClick> hash = new HashMap<>(currentInventory.getEventPerSlot());

            if (!hash.isEmpty()) {
                hash.forEach((slot, pack) -> {
                    if (slot == rawSlot) {
                        final BiConsumer<Player, ChestInventory> consumer = pack.getConsumerFor(clickType);
                        if (consumer != null) {
                            consumer.accept(player, currentInventory);
                        }
                    }
                });
            }
        }
    }

    @EventHandler
    private void handleEventClose(InventoryCloseEvent ev) {

        final Player player = (Player) ev.getPlayer();
        final ChestInventory playerInventory = getPlayerInventory(player);

        if (playerInventory == null) {
            return;
        }

        if (playerInventory.isValidMenu(ev.getInventory())) {

            if (playerInventory.getCloseSound() != null) {
                player.playSound(player.getLocation(), playerInventory.getCloseSound(),
                                 SoundCategory.MASTER, 2, playerInventory.getCloseSoundPitch()
                );
            }

            if (!playerInventory.isIgnoreCloseEvent()) {
                if (playerInventory.getReturnItems().length > 0) {
                    for (int returnItem : playerInventory.getReturnItems()) {
                        final ItemStack item = playerInventory.getInventory().getItem(returnItem);
                        if (item != null) {
                            player.getInventory().addItem(item);
                        }
                    }
                }
                else if (playerInventory.isReturnAllItems()) {
                    boolean dropped = false;
                    for (ItemStack item : playerInventory.getInventory().getContents()) {
                        if (item == null || item.getType().isAir()) {
                            return;
                        }
                        if (player.getInventory().firstEmpty() != -1) {
                            player.getInventory().addItem(item);
                        }
                        else {
                            player.getWorld().dropItemNaturally(player.getLocation(), item);
                            dropped = true;
                        }
                    }
                    if (dropped) {
                        player.sendMessage(ChatColor.RED + "Some of items could not fit in your inventory and were dropped on the ground.");
                    }
                }

                if (playerInventory.getAtClose() != null) {
                    playerInventory.getAtClose().accept(player, playerInventory);
                }
            }

            // Remove current menu and make it last menu.
            final UUID uuid = player.getUniqueId();
            ChestInventory.perPlayerLastMenu.put(uuid, playerInventory);
            //_null->perPlayerMenu.remove(uuid);

        }

    }

}
