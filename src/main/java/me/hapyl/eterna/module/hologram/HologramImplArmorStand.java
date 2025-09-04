package me.hapyl.eterna.module.hologram;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.eterna.module.component.Components;
import me.hapyl.eterna.module.locaiton.LocationHelper;
import me.hapyl.eterna.module.reflect.PacketFactory;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.util.BukkitUtils;
import me.hapyl.eterna.module.component.ComponentList;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class HologramImplArmorStand extends AbstractHologram {
    
    public static final double defaultArmorStandOffset = 0.25;
    
    private final Map<Player, List<PacketArmorStand>> showingTo;
    private double armorStandOffset;
    
    public HologramImplArmorStand(@Nonnull Location location) {
        super(location);
        
        this.showingTo = Maps.newHashMap();
        this.armorStandOffset = defaultArmorStandOffset;
    }
    
    /**
     * Gets the offset between armor stands.
     *
     * @return the offset between armor stand.
     */
    public double armorStandOffset() {
        return this.armorStandOffset;
    }
    
    /**
     * Sets the offset between armor stands.
     *
     * @param armorStandOffset - The new offset.
     */
    public void armorStandOffset(double armorStandOffset) {
        this.armorStandOffset = armorStandOffset;
    }
    
    @Override
    public void setLines(@Nonnull ComponentSupplier supplier) {
        super.setLines(supplier);
        
        // Update armor stands for all players
        this.showingTo.keySet().removeIf(Predicate.not(Player::isOnline));
        this.showingTo.forEach((player, holograms) -> createOrUpdate(player));
    }
    
    @Override
    public void teleport(@Nonnull Location location) {
        this.location = BukkitUtils.newLocation(location);
        
        // Update location
        this.showingTo.values().forEach(holograms -> {
            final int size = holograms.size();
            
            for (int i = 0; i < size; i++) {
                final PacketArmorStand hologram = holograms.get(i);
                final double offset = (size - i - 1) * armorStandOffset;
                
                LocationHelper.offset(location, 0, offset, 0, hologram::teleport);
            }
        });
    }
    
    @Override
    public void show(@Nonnull Player player) {
        if (isShowingTo(player)) {
            return;
        }
        
        createOrUpdate(player);
    }
    
    @Override
    public void hide(@Nonnull Player player) {
        final List<PacketArmorStand> existingLines = showingTo.remove(player);
        
        if (existingLines != null) {
            existingLines.forEach(PacketArmorStand::hide);
            existingLines.clear();
        }
    }
    
    @Override
    public void destroy() {
        this.showingTo.values().forEach(holograms -> holograms.forEach(PacketArmorStand::hide));
        this.showingTo.clear();
    }
    
    @Nonnull
    @Override
    public Collection<Player> showingTo() {
        return Set.copyOf(showingTo.keySet());
    }
    
    protected void createOrUpdate(@Nonnull Player player) {
        final ComponentList components = supplier.supply(player);
        final List<PacketArmorStand> existingLines = showingTo.computeIfAbsent(player, _player -> Lists.newArrayList());
        
        // If length matches, no need to re-create the stands, just update the names
        if (components.size() == existingLines.size()) {
            for (int i = 0; i < components.size(); i++) {
                existingLines.get(i).text(components.get(i));
            }
        }
        // Otherwise, we need to re-create the armor stands
        else {
            // Remove existing lines
            existingLines.forEach(PacketArmorStand::hide);
            existingLines.clear();
            
            final int toCreate = components.size();
            
            for (int i = 0; i < toCreate; i++) {
                final double yOffset = (toCreate - i - 1) * armorStandOffset;
                final Component text = components.get(i);
                
                LocationHelper.offset(
                        location, 0, yOffset, 0, () -> existingLines.add(new PacketArmorStand(player, location, text))
                );
            }
        }
    }
    
    @ApiStatus.Internal
    public static class PacketArmorStand {
        private final Player player;
        private final net.minecraft.world.entity.decoration.ArmorStand armorStand;
        
        PacketArmorStand(@Nonnull Player player, @Nonnull Location location, @Nullable Component text) {
            this.player = player;
            this.armorStand = new net.minecraft.world.entity.decoration.ArmorStand(
                    Reflect.getMinecraftWorld(location.getWorld()),
                    location.getX(),
                    location.getY(),
                    location.getZ()
            );
            
            this.armorStand.teleportTo(location.getX(), location.getY(), location.getZ());
            
            text(text);
            
            this.armorStand.setInvisible(true);
            this.armorStand.setSmall(true);
            this.armorStand.setMarker(true);
            this.armorStand.setCustomNameVisible(true);
            
            this.show();
        }
        
        @Override
        public String toString() {
            return armorStand.getBukkitEntity().getName();
        }
        
        protected void show() {
            Reflect.sendPacket(player, PacketFactory.makePacketAddEntity(armorStand));
            update();
        }
        
        protected void hide() {
            Reflect.sendPacket(player, PacketFactory.makePacketRemoveEntity(armorStand));
        }
        
        protected void update() {
            Reflect.updateMetadata(armorStand, player);
        }
        
        protected void text(@Nullable Component name) {
            final boolean isEmpty = name == null || Components.isEmptyOrNewLine(name);
            
            final Entity bukkitEntity = armorStand.getBukkitEntity();
            bukkitEntity.customName(!isEmpty ? name : Component.empty());
            
            // If the name is null, we actually hide the name of this armor stand to no see the | in the name
            // FIXME @Sep 04, 2025 (xanyjl) -> This doesn't seem to work?
            armorStand.setCustomNameVisible(!isEmpty);
            
            // Refresh name
            update();
        }
        
        protected void teleport(@Nonnull Location location) {
            this.armorStand.teleportTo(location.getX(), location.getY(), location.getZ());
            Reflect.updateEntityLocation(armorStand, player);
        }
    }
}
