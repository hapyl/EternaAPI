package me.hapyl.spigotutils.module.inventory.item;

import com.google.common.collect.Lists;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.nbt.NBTType;
import me.hapyl.spigotutils.module.nbt.NBT;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class CustomItem {

    private final String id;
    private final Material material;

    private String customName;
    private List<String> lore;

    public CustomItem(String id, Material material) {
        this.id = id;
        this.material = material;
        this.customName = "";
        this.lore = Lists.newArrayList();
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

    public String getId() {
        return id;
    }

    public Material getType() {
        return material;
    }

    public final void register() {
        CustomItemRegistry.getInstance().register(this.getId(), this);
    }

    public final void unregister() {
        CustomItemRegistry.getInstance().unregister(this.getId());
    }

    /**
     * Creates new ItemStack of this instance.
     */
    public final ItemStack toItemStack() {
        final ItemStack stack = new ItemStack(this.material);
        final ItemMeta meta = stack.getItemMeta();
        if (meta == null) {
            throw new NullPointerException("item meta is null?");
        }
        // Apply Id
        NBT.setString(meta, "Id", this.id);
        NBT.setString(meta, "UniqueId", UUID.randomUUID().toString());
        NBT.setValue(meta, "TimeStamp", NBTType.LONG, System.currentTimeMillis());

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

}
