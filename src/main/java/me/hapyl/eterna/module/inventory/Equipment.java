package me.hapyl.eterna.module.inventory;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Objects;

/**
 * A {@link EntityEquipment} implementation supporting all {@link EquipmentSlot}s.
 */
public class Equipment implements EntityEquipment {
    
    private static final ItemStack AIR = new ItemStack(Material.AIR);
    
    private final ItemStack[] items;
    
    public Equipment() {
        this.items = new ItemStack[EquipmentSlot.values().length];
    }
    
    /**
     * Equips this equipment to the given entity.
     *
     * @param entity - The entity to equip to.
     */
    public void equip(@Nonnull LivingEntity entity) {
        equip(entity, false);
    }
    
    /**
     * Equips this equipment to the given entity.
     *
     * @param entity - The entity to equip to.
     * @param silent - Whether the equipment should be silent.
     */
    public void equip(@Nonnull LivingEntity entity, boolean silent) {
        // zero fucking docs why it can be null so requiring non-null
        final EntityEquipment equipment = Objects.requireNonNull(entity.getEquipment(), "%s does not support equipment!".formatted(entity));
        
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (!entity.canUseEquipmentSlot(slot)) {
                continue;
            }
            
            equipment.setItem(slot, getItem(slot), silent);
        }
    }
    
    @Override
    public void setItem(@Nonnull EquipmentSlot slot, @Nullable ItemStack item) {
        this.items[slot.ordinal()] = item;
    }
    
    @Override
    public void setItem(@Nonnull EquipmentSlot slot, @Nullable ItemStack item, boolean silent) {
        setItem(slot, item);
    }
    
    @Nonnull
    @Override
    public ItemStack getItem(@Nonnull EquipmentSlot slot) {
        return itemByIndex(slot.ordinal());
    }
    
    @Nonnull
    @Override
    public ItemStack getItemInMainHand() {
        return getItem(EquipmentSlot.HAND);
    }
    
    @Override
    public void setItemInMainHand(@Nullable ItemStack item) {
        setItem(EquipmentSlot.HAND, item);
    }
    
    @Override
    public void setItemInMainHand(@Nullable ItemStack item, boolean silent) {
        setItemInMainHand(item);
    }
    
    @Nonnull
    @Override
    public ItemStack getItemInOffHand() {
        return getItem(EquipmentSlot.OFF_HAND);
    }
    
    @Override
    public void setItemInOffHand(@Nullable ItemStack item) {
        setItem(EquipmentSlot.OFF_HAND, item);
    }
    
    @Override
    public void setItemInOffHand(@Nullable ItemStack item, boolean silent) {
        setItemInOffHand(item);
    }
    
    @Nonnull
    @Override
    public ItemStack getItemInHand() {
        return getItemInMainHand();
    }
    
    @Override
    public void setItemInHand(@Nullable ItemStack stack) {
        setItemInMainHand(stack);
    }
    
    @Override
    public ItemStack getHelmet() {
        return getItem(EquipmentSlot.HEAD);
    }
    
    @Override
    public void setHelmet(@Nullable ItemStack helmet) {
        setItem(EquipmentSlot.HEAD, helmet);
    }
    
    @Override
    public void setHelmet(@Nullable ItemStack helmet, boolean silent) {
        setHelmet(helmet);
    }
    
    @Override
    public ItemStack getChestplate() {
        return getItem(EquipmentSlot.CHEST);
    }
    
    @Override
    public void setChestplate(@Nullable ItemStack chestplate) {
        setItem(EquipmentSlot.CHEST, chestplate);
    }
    
    @Override
    public void setChestplate(@Nullable ItemStack chestplate, boolean silent) {
        setChestplate(chestplate);
    }
    
    @Override
    public ItemStack getLeggings() {
        return getItem(EquipmentSlot.LEGS);
    }
    
    @Override
    public void setLeggings(@Nullable ItemStack leggings) {
        setItem(EquipmentSlot.LEGS, leggings);
    }
    
    @Override
    public void setLeggings(ItemStack leggings, boolean silent) {
        setLeggings(leggings);
    }
    
    @Override
    public ItemStack getBoots() {
        return getItem(EquipmentSlot.FEET);
    }
    
    @Override
    public void setBoots(@Nullable ItemStack boots) {
        setItem(EquipmentSlot.FEET, boots);
    }
    
    @Override
    public void setBoots(@Nullable ItemStack boots, boolean silent) {
        setBoots(boots);
    }
    
    @Nonnull
    @Override
    public ItemStack[] getArmorContents() {
        return Arrays.copyOf(items, items.length);
    }
    
    @Override
    public void setArmorContents(@Nonnull ItemStack[] items) {
        System.arraycopy(items, 0, this.items, 0, this.items.length);
    }
    
    @Override
    public void clear() {
        Arrays.fill(this.items, null);
    }
    
    @Override
    @Deprecated
    public float getItemInHandDropChance() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    @Deprecated
    public void setItemInHandDropChance(float chance) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    @Deprecated
    public float getItemInMainHandDropChance() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    @Deprecated
    public void setItemInMainHandDropChance(float chance) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    @Deprecated
    public float getItemInOffHandDropChance() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    @Deprecated
    public void setItemInOffHandDropChance(float chance) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    @Deprecated
    public float getHelmetDropChance() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    @Deprecated
    public void setHelmetDropChance(float chance) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    @Deprecated
    public float getChestplateDropChance() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    @Deprecated
    public void setChestplateDropChance(float chance) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    @Deprecated
    public float getLeggingsDropChance() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    @Deprecated
    public void setLeggingsDropChance(float chance) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    @Deprecated
    public float getBootsDropChance() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    @Deprecated
    public void setBootsDropChance(float chance) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
    
    @Nonnull
    @Override
    @Deprecated
    public Entity getHolder() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public float getDropChance(@NotNull EquipmentSlot slot) {
        return 0;
    }
    
    @Override
    public void setDropChance(@NotNull EquipmentSlot slot, float chance) {
    }
    
    @Nonnull
    private ItemStack itemByIndex(int index) {
        if (index < 0 || index >= items.length) {
            return AIR;
        }
        
        final ItemStack item = items[index];
        return item != null ? item : AIR;
    }
    
    /**
     * Creates {@link Equipment} from the given entity.
     *
     * @param entity - The entity whose equipment to clone.
     * @return new equipment from the given entity.
     */
    @Nonnull
    public static Equipment of(@Nonnull LivingEntity entity) {
        final Equipment equipment = new Equipment();
        final EntityEquipment entityEquipment = entity.getEquipment();
        
        if (entityEquipment != null) {
            equipment.items[0] = itemCopyOrNull(entityEquipment.getItemInMainHand());
            equipment.items[1] = itemCopyOrNull(entityEquipment.getItemInOffHand());
            equipment.items[2] = itemCopyOrNull(entityEquipment.getBoots());
            equipment.items[3] = itemCopyOrNull(entityEquipment.getLeggings());
            equipment.items[4] = itemCopyOrNull(entityEquipment.getChestplate());
            equipment.items[5] = itemCopyOrNull(entityEquipment.getHelmet());
        }
        
        return equipment;
    }
    
    /**
     * Creates a new {@link Builder}.
     *
     * @return a new builder.
     */
    @Nonnull
    public static Builder builder() {
        return new Builder();
    }
    
    private static ItemStack itemCopyOrNull(ItemStack item) {
        return item != null ? new ItemStack(item) : null;
    }
    
    public static class Builder implements me.hapyl.eterna.module.util.Builder<Equipment> {
        
        private final Equipment equipment;
        
        private Builder() {
            this.equipment = new Equipment();
        }
        
        public Builder mainHand(@Nonnull ItemStack item) {
            return set(EquipmentSlot.HAND, item);
        }
        
        public Builder mainHand(@Nonnull Material material) {
            return mainHand(new ItemStack(material));
        }
        
        public Builder offHand(@Nonnull ItemStack item) {
            return set(EquipmentSlot.OFF_HAND, item);
        }
        
        public Builder offHand(@Nonnull Material material) {
            return offHand(new ItemStack(material));
        }
        
        public Builder boots(@Nonnull ItemStack item) {
            return set(EquipmentSlot.FEET, item);
        }
        
        public Builder boots(@Nonnull Material material) {
            return boots(new ItemStack(material));
        }
        
        public Builder leggings(@Nonnull ItemStack item) {
            return set(EquipmentSlot.LEGS, item);
        }
        
        public Builder leggings(@Nonnull Material material) {
            return leggings(new ItemStack(material));
        }
        
        public Builder chestPlate(@Nonnull ItemStack item) {
            return set(EquipmentSlot.CHEST, item);
        }
        
        public Builder chestPlate(@Nonnull Material material) {
            return chestPlate(new ItemStack(material));
        }
        
        public Builder helmet(@Nonnull ItemStack item) {
            return set(EquipmentSlot.HEAD, item);
        }
        
        public Builder helmet(@Nonnull Material material) {
            return helmet(new ItemStack(material));
        }
        
        public Builder body(@Nonnull ItemStack item) {
            return set(EquipmentSlot.BODY, item);
        }
        
        public Builder body(@Nonnull Material material) {
            return body(new ItemStack(material));
        }
        
        public Builder set(@Nonnull EquipmentSlot slot, @Nonnull ItemStack item) {
            this.equipment.setItem(slot, item);
            return this;
        }
        
        public Builder set(@Nonnull EquipmentSlot slot, @Nonnull Material material) {
            return set(slot, new ItemStack(material));
        }
        
        @Nonnull
        @Override
        public Equipment build() {
            return this.equipment;
        }
    }
    
    
}
