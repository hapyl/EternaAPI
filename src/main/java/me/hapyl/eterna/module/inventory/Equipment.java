package me.hapyl.eterna.module.inventory;

import com.mojang.datafixers.util.Pair;
import me.hapyl.eterna.module.annotate.SelfReturn;
import me.hapyl.eterna.module.nms.NmsConverters;
import me.hapyl.eterna.module.nms.NmsWrapper;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.util.Buildable;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A {@link EntityEquipment} implementation supporting all {@link EquipmentSlot}.
 */
public class Equipment implements EntityEquipment, NmsWrapper<List<Pair<net.minecraft.world.entity.EquipmentSlot, net.minecraft.world.item.ItemStack>>> {
    
    private final ItemStack[] items;
    
    /**
     * Creates a new {@link Equipment}.
     */
    public Equipment() {
        this.items = new ItemStack[EquipmentSlot.values().length];
    }
    
    /**
     * Equips this {@link Equipment} to the given {@link LivingEntity}.
     *
     * @param entity - The entity to equip to.
     */
    public void equip(@NotNull LivingEntity entity) {
        equip(entity, false);
    }
    
    /**
     * Equips this {@link Equipment} to the given {@link LivingEntity}.
     *
     * @param entity - The entity to equip to.
     * @param silent - {@code true} to equip without a sound, {@code false} otherwise.
     */
    public void equip(@NotNull LivingEntity entity, boolean silent) {
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
    public void setItem(@NotNull EquipmentSlot slot, @Nullable ItemStack item) {
        this.items[slot.ordinal()] = item;
    }
    
    @Override
    public void setItem(@NotNull EquipmentSlot slot, @Nullable ItemStack item, boolean silent) {
        setItem(slot, item);
    }
    
    @NotNull
    @Override
    public ItemStack getItem(@NotNull EquipmentSlot slot) {
        return byIndex(slot.ordinal());
    }
    
    @NotNull
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
    
    @NotNull
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
    
    @NotNull
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
    
    @NotNull
    @Override
    public ItemStack @NotNull [] getArmorContents() {
        return Arrays.copyOf(items, items.length);
    }
    
    @Override
    public void setArmorContents(@NotNull ItemStack @NotNull [] items) {
        System.arraycopy(items, 0, this.items, 0, this.items.length);
    }
    
    @Override
    public void clear() {
        Arrays.fill(this.items, null);
    }
    
    @NotNull
    @Override
    public List<Pair<net.minecraft.world.entity.EquipmentSlot, net.minecraft.world.item.ItemStack>> toNms() {
        return Arrays.stream(EquipmentSlot.values())
                     .map(equipmentSlot -> {
                         final ItemStack itemOnSlot = getItem(equipmentSlot);
                         
                         // We filter null afterwards
                         if (itemOnSlot.isEmpty()) {
                             return null;
                         }
                         
                         return Pair.of(
                                 NmsConverters.EQUIPMENT_SLOT.toNms(equipmentSlot),
                                 Reflect.bukkitItemAsVanilla(itemOnSlot)
                         );
                     })
                     .filter(Objects::nonNull)
                     .toList();
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
    
    @NotNull
    @Override
    @Deprecated
    public Entity getHolder() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    @Deprecated
    public float getDropChance(@NotNull EquipmentSlot slot) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    @Deprecated
    public void setDropChance(@NotNull EquipmentSlot slot, float chance) {
        throw new UnsupportedOperationException();
    }
    
    @NotNull
    private ItemStack byIndex(int index) {
        if (index < 0 || index >= items.length) {
            return new ItemStack(Material.AIR);
        }
        
        final ItemStack item = items[index];
        return item != null ? item : new ItemStack(Material.AIR);
    }
    
    /**
     * A static factory method to create an {@link Equipment}, copying the given {@link LivingEntity} equipment.
     *
     * @param entity - The entity whose equipment to copy.
     * @return {@code equipment} with items copied from the {@code entity}.
     */
    @NotNull
    public static Equipment copyOf(@NotNull LivingEntity entity) {
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
     * A static factory method to create an {@link Equipment}, copying the items from the given {@link Equipment}.
     *
     * @param equipment - The equipment to copy.
     * @return {@code equipment} with items copied from the given {@code equipment}.
     */
    @NotNull
    public static Equipment copyOf(@Nullable Equipment equipment) {
        final Equipment copy = new Equipment();
        
        if (equipment != null) {
            for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                copy.setItem(equipmentSlot, new ItemStack(equipment.getItem(equipmentSlot)));
            }
        }
        
        return copy;
    }
    
    /**
     * Creates a new {@link Builder}.
     *
     * @return a new builder.
     */
    @NotNull
    public static Builder builder() {
        return new Builder();
    }
    
    @Nullable
    private static ItemStack itemCopyOrNull(@Nullable ItemStack item) {
        return item != null ? new ItemStack(item) : null;
    }
    
    /**
     * Builder for creating {@link Equipment}.
     */
    public static class Builder implements Buildable<Equipment> {
        
        private final Equipment equipment;
        
        private Builder() {
            this.equipment = new Equipment();
        }
        
        /**
         * Sets the item in {@link EquipmentSlot#HAND}.
         *
         * @param item - The item to equip.
         */
        @SelfReturn
        public Builder mainHand(@NotNull ItemStack item) {
            return set(EquipmentSlot.HAND, item);
        }
        
        /**
         * Sets the material in {@link EquipmentSlot#HAND}.
         *
         * @param material - The material to equip.
         */
        @SelfReturn
        public Builder mainHand(@NotNull Material material) {
            return mainHand(new ItemStack(material));
        }
        
        /**
         * Sets the item in {@link EquipmentSlot#OFF_HAND}.
         *
         * @param item - The item to equip.
         */
        @SelfReturn
        public Builder offHand(@NotNull ItemStack item) {
            return set(EquipmentSlot.OFF_HAND, item);
        }
        
        /**
         * Sets the material in {@link EquipmentSlot#OFF_HAND}.
         *
         * @param material - The material to equip.
         */
        @SelfReturn
        public Builder offHand(@NotNull Material material) {
            return offHand(new ItemStack(material));
        }
        
        /**
         * Sets the item in {@link EquipmentSlot#FEET}.
         *
         * @param item - The item to equip.
         */
        @SelfReturn
        public Builder boots(@NotNull ItemStack item) {
            return set(EquipmentSlot.FEET, item);
        }
        
        /**
         * Sets the material in {@link EquipmentSlot#FEET}.
         *
         * @param material - The material to equip.
         */
        @SelfReturn
        public Builder boots(@NotNull Material material) {
            return boots(new ItemStack(material));
        }
        
        /**
         * Sets the item in {@link EquipmentSlot#LEGS}.
         *
         * @param item - The item to equip.
         */
        @SelfReturn
        public Builder leggings(@NotNull ItemStack item) {
            return set(EquipmentSlot.LEGS, item);
        }
        
        /**
         * Sets the material in {@link EquipmentSlot#LEGS}.
         *
         * @param material - The material to equip.
         */
        @SelfReturn
        public Builder leggings(@NotNull Material material) {
            return leggings(new ItemStack(material));
        }
        
        /**
         * Sets the item in {@link EquipmentSlot#CHEST}.
         *
         * @param item - The item to equip.
         */
        @SelfReturn
        public Builder chestPlate(@NotNull ItemStack item) {
            return set(EquipmentSlot.CHEST, item);
        }
        
        /**
         * Sets the material in {@link EquipmentSlot#CHEST}.
         *
         * @param material - The material to equip.
         */
        @SelfReturn
        public Builder chestPlate(@NotNull Material material) {
            return chestPlate(new ItemStack(material));
        }
        
        /**
         * Sets the item in {@link EquipmentSlot#HEAD}.
         *
         * @param item - The item to equip.
         */
        @SelfReturn
        public Builder helmet(@NotNull ItemStack item) {
            return set(EquipmentSlot.HEAD, item);
        }
        
        /**
         * Sets the material in {@link EquipmentSlot#HEAD}.
         *
         * @param material - The material to equip.
         */
        @SelfReturn
        public Builder helmet(@NotNull Material material) {
            return helmet(new ItemStack(material));
        }
        
        /**
         * Sets the item in {@link EquipmentSlot#BODY}.
         *
         * @param item - The item to equip.
         */
        @SelfReturn
        public Builder body(@NotNull ItemStack item) {
            return set(EquipmentSlot.BODY, item);
        }
        
        /**
         * Sets the material in {@link EquipmentSlot#BODY}.
         *
         * @param material - The material to equip.
         */
        @SelfReturn
        public Builder body(@NotNull Material material) {
            return body(new ItemStack(material));
        }
        
        /**
         * Sets the item in the given {@link EquipmentSlot}.
         *
         * @param slot - The slot to set.
         * @param item - The item to equip.
         */
        @SelfReturn
        public Builder set(@NotNull EquipmentSlot slot, @NotNull ItemStack item) {
            this.equipment.setItem(slot, item);
            return this;
        }
        
        /**
         * Sets the material in the given {@link EquipmentSlot}.
         *
         * @param slot     - The slot to set.
         * @param material - The material to equip.
         */
        @SelfReturn
        public Builder set(@NotNull EquipmentSlot slot, @NotNull Material material) {
            return set(slot, new ItemStack(material));
        }
        
        /**
         * Builds the configured {@link Equipment}.
         */
        @NotNull
        @Override
        public Equipment build() {
            return this.equipment;
        }
    }
    
}
