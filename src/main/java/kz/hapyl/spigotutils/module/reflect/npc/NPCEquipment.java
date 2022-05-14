package kz.hapyl.spigotutils.module.reflect.npc;

import org.bukkit.Material;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class NPCEquipment {

    private ItemStack helmet = notNullItem();
    private ItemStack chestplate = notNullItem();
    private ItemStack leggings = notNullItem();
    private ItemStack boots = notNullItem();
    private ItemStack hand = notNullItem();
    private ItemStack offhand = notNullItem();

    public NPCEquipment() {
    }

    public void setItem(ItemSlot slot, ItemStack stack) {
        switch (slot) {
            case HEAD -> setHelmet(stack);
            case CHEST -> setChestplate(stack);
            case LEGS -> setLeggings(stack);
            case FEET -> setBoots(stack);
            case MAINHAND -> setHand(stack);
            case OFFHAND -> setOffhand(stack);
        }
    }

    public ItemStack getHelmet() {
        return notNullItem(helmet);
    }

    public void setHelmet(ItemStack helmet) {
        this.helmet = helmet;
    }

    public ItemStack getChestplate() {
        return notNullItem(chestplate);
    }

    public void setChestplate(ItemStack chestplate) {
        this.chestplate = chestplate;
    }

    public ItemStack getLeggings() {
        return notNullItem(leggings);
    }

    public void setLeggings(ItemStack leggings) {
        this.leggings = leggings;
    }

    public ItemStack getBoots() {
        return notNullItem(boots);
    }

    public void setBoots(ItemStack boots) {
        this.boots = boots;
    }

    public ItemStack getHand() {
        return notNullItem(hand);
    }

    public void setHand(ItemStack hand) {
        this.hand = hand;
    }

    public ItemStack getOffhand() {
        return notNullItem(offhand);
    }

    public void setOffhand(ItemStack offhand) {
        this.offhand = offhand;
    }

    public void setEquipment(EntityEquipment equipment) {
        this.helmet = notNullItem(equipment.getHelmet());
        this.chestplate = notNullItem(equipment.getChestplate());
        this.leggings = notNullItem(equipment.getLeggings());
        this.boots = notNullItem(equipment.getBoots());
        this.hand = notNullItem(equipment.getItemInMainHand());
        this.offhand = notNullItem(equipment.getItemInOffHand());
    }

    private ItemStack notNullItem(ItemStack stack) {
        return stack == null ? new ItemStack(Material.AIR) : stack;
    }

    private ItemStack notNullItem() {
        return notNullItem(null);
    }

}
