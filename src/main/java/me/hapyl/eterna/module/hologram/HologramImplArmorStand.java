package me.hapyl.eterna.module.hologram;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.papermc.paper.adventure.PaperAdventure;
import me.hapyl.eterna.module.component.ComponentList;
import me.hapyl.eterna.module.component.ComponentListSupplier;
import me.hapyl.eterna.module.component.Components;
import me.hapyl.eterna.module.location.LocationHelper;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.reflect.packet.PacketFactory;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Represents an armor stand based {@link Hologram}.
 */
public class HologramImplArmorStand extends AbstractHologram {
    
    /**
     * Defines the default offset between armor stands.
     */
    public static final double DEFAULT_ARMOR_STAND_OFFSET = 0.25;
    
    private final Map<Player, List<PacketArmorStand>> showingTo;
    private double armorStandOffset;
    
    @ApiStatus.Internal
    HologramImplArmorStand(@NotNull Location location) {
        super(location);
        
        this.showingTo = Maps.newHashMap();
        this.armorStandOffset = DEFAULT_ARMOR_STAND_OFFSET;
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
    public void setLines(@NotNull ComponentListSupplier supplier) {
        super.setLines(supplier);
        
        // Update armor stands for all players
        this.showingTo.keySet().removeIf(Predicate.not(Player::isOnline));
        this.showingTo.forEach((player, holograms) -> createOrUpdate(player));
    }
    
    @Override
    public void teleport(@NotNull Location location) {
        this.location = LocationHelper.copyOf(location);
        
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
    public void show(@NotNull Player player) {
        if (isShowingTo(player)) {
            return;
        }
        
        createOrUpdate(player);
    }
    
    @Override
    public void hide(@NotNull Player player) {
        final List<PacketArmorStand> existingLines = showingTo.remove(player);
        
        if (existingLines != null) {
            existingLines.forEach(PacketArmorStand::hide);
            existingLines.clear();
        }
    }
    
    @Override
    public void dispose() {
        this.showingTo.values().forEach(holograms -> holograms.forEach(PacketArmorStand::hide));
        this.showingTo.clear();
    }
    
    @NotNull
    @Override
    public Collection<Player> showingTo() {
        return Set.copyOf(showingTo.keySet());
    }
    
    protected void createOrUpdate(@NotNull Player player) {
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
        
        PacketArmorStand(@NotNull Player player, @NotNull Location location, @Nullable Component text) {
            this.player = player;
            this.armorStand = new net.minecraft.world.entity.decoration.ArmorStand(
                    Reflect.getHandle(location.getWorld()),
                    location.getX(),
                    location.getY(),
                    location.getZ()
            );
            
            this.armorStand.teleportTo(location.getX(), location.getY(), location.getZ());
            
            this.armorStand.setInvisible(true);
            this.armorStand.setSmall(true);
            this.armorStand.setMarker(true);
            
            this.show();
            this.text(text);
        }
        
        @Override
        public String toString() {
            return armorStand.getBukkitEntity().getName();
        }
        
        protected void show() {
            Reflect.sendPacket(player, PacketFactory.makePacketAddEntity(armorStand));
            
            // We only need to update metadata for index 0 and 15, which are invisibility and armor stand data, like marker, small
            Reflect.updateEntityData(
                    player, armorStand, armorStand.getEntityData().packAll()
                                                  .stream()
                                                  .filter(dataValue -> {
                                                      final int id = dataValue.id();
                                                      
                                                      return id == 0 || id == 15;
                                                  })
                                                  .toList()
            );
        }
        
        protected void hide() {
            Reflect.sendPacket(player, PacketFactory.makePacketRemoveEntity(armorStand));
        }
        
        protected void text(@Nullable Component name) {
            final boolean isEmpty = name == null || Components.isEmpty(name);
            
            // Set the name either to empty component or name
            armorStand.setCustomName(isEmpty ? net.minecraft.network.chat.Component.empty() : PaperAdventure.asVanilla(name));
            
            // Manually set the custom name visible bit
            armorStand.setCustomNameVisible(!isEmpty);
            
            // Refresh name
            update();
        }
        
        protected void update() {
            // Explicitly use all values, because `isCustomNameVisible` defaults to `false`
            Reflect.updateEntityData(player, armorStand, armorStand.getEntityData().packAll());
        }
        
        protected void teleport(@NotNull Location location) {
            this.armorStand.teleportTo(location.getX(), location.getY(), location.getZ());
            Reflect.updateEntityLocation(armorStand, player);
        }
    }
}
