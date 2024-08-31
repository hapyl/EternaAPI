package me.hapyl.eterna.module.inventory.item;

import com.google.common.collect.Lists;
import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.module.annotate.Event;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.nbt.NBTType;
import me.hapyl.eterna.module.nbt.NBT;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.registry.Keyed;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class CustomItem implements Keyed {

    private final Key key;
    private final Material material;

    private String customName;
    private List<String> lore;

    public CustomItem(@Nonnull Key key, @Nonnull Material material) {
        this.key = key;
        this.material = material;
        this.customName = "";
        this.lore = Lists.newArrayList();
    }

    @Override
    @Nonnull
    public final Key getKey() {
        return this.key;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public void addLore(String lore) {
        this.lore.add(lore);
    }

    public void setSmartLore(String lore) {
        this.lore = ItemBuilder.splitString(lore, 35);
    }

    public void addSmartLore(String lore) {
        this.lore.addAll(ItemBuilder.splitString(lore, 35));
    }

    @Event
    public void onClick(Player player, ItemStack item, PlayerInteractEvent event) {
    }

    @Event
    public void onPlace(Player player, ItemStack item, BlockPlaceEvent event) {
    }

    @Event
    public void onInventoryClick(Player player, ItemStack item, InventoryClickEvent event) {
    }

    @Event
    public void onDrop(Player player, ItemStack item, PlayerDropItemEvent event) {
    }

    @Event
    public void onPickup(Player player, ItemStack item, EntityPickupItemEvent event) {
    }

    public Material getType() {
        return material;
    }

    public final void register() {
        Eterna.getManagers().customItem.register(key, this);
    }

    public final void unregister() {
        Eterna.getManagers().customItem.unregister(key);
    }

    /**
     * Creates new ItemStack of this instance.
     */
    public final ItemStack toItemStack() {
        final ItemStack stack = new ItemStack(this.material);
        final ItemMeta meta = stack.getItemMeta();

        // Apply Id
        new Data(this.key, UUID.randomUUID(), System.currentTimeMillis())
                .apply(stack);

        // Apply Meta
        meta.setDisplayName(Chat.format(this.customName));
        meta.setLore(colorizeLore());

        stack.setItemMeta(meta);
        return stack;
    }

    private List<String> colorizeLore() {
        final List<String> lore = new ArrayList<>();
        for (final String s : this.lore) {
            lore.add(Chat.format(s));
        }
        return lore;
    }

    public record Data(@Nonnull Key key, @Nonnull UUID uuid, long timeStamp) {

        public void apply(@Nonnull ItemStack item) {
            final ItemMeta meta = item.getItemMeta();

            NBT.setString(meta, "key", this.key.getKey());
            NBT.setString(meta, "uuid", this.uuid.toString());
            NBT.setValue(meta, "time_stamp", NBTType.LONG, this.timeStamp);
        }

        @Nullable
        public static Data of(@Nonnull ItemStack item) {
            final ItemMeta meta = item.getItemMeta();

            try {
                final Key key = Key.ofString(NBT.getString(meta, "key"));
                final UUID uuid = UUID.fromString(NBT.getString(meta, "uuid"));
                final Long timeStamp = NBT.getValue(meta, "time_stamp", NBTType.LONG);

                return new Data(key, uuid, timeStamp != null ? timeStamp : 0L);
            } catch (Exception e) {
                return null;
            }
        }

    }

}
