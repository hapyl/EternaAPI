package me.hapyl.eterna.module.hologram;

import com.google.common.collect.Sets;
import me.hapyl.eterna.module.component.ComponentListSupplier;
import me.hapyl.eterna.module.component.Components;
import me.hapyl.eterna.module.reflect.packet.PacketFactory;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.component.ComponentList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

public class HologramImplTextDisplay extends AbstractHologram {
    
    private final PacketTextDisplay textDisplay;
    private final Set<Player> showingTo;
    
    public HologramImplTextDisplay(@Nonnull Location location) {
        super(location);
        
        this.textDisplay = new PacketTextDisplay(location);
        this.showingTo = Sets.newHashSet();
    }
    
    /**
     * Gets the opacity of this hologram.
     *
     * @return the opacity of this hologram.
     */
    public byte opacity() {
        return textDisplay.entity.getTextOpacity();
    }
    
    /**
     * Sets the opacity of this hologram.
     *
     * @param opacity - The new opacity.
     */
    public void opacity(byte opacity) {
        this.textDisplay.entity.setTextOpacity(opacity);
        this.updateMetadataAll();
    }
    
    /**
     * Gets the background color, or {@code null} if there is no color.
     *
     * @return the background color, or {@code null} if there is no color.
     */
    @Nullable
    public Color background() {
        final int color = this.textDisplay.entity.getBackgroundColor();
        
        return color != -1 ? Color.fromARGB(color) : null;
    }
    
    /**
     * Sets the background color.
     *
     * @param color - The color to set.
     */
    public void background(@Nullable Color color) {
        this.textDisplay.entity.getEntityData().set(Display.TextDisplay.DATA_BACKGROUND_COLOR_ID, color != null ? color.asARGB() : -1);
        this.updateMetadataAll();
    }
    
    @Override
    public void setLines(@Nonnull ComponentListSupplier supplier) {
        super.setLines(supplier);
        
        // Update for all players
        this.showingTo.removeIf(Predicate.not(Player::isOnline));
        this.showingTo.forEach(player -> textDisplay.text(player, supplier.supply(player)));
    }
    
    @Nonnull
    @Override
    public Location getLocation() {
        return Reflect.getEntityLocation(textDisplay.entity);
    }
    
    @Nonnull
    @Override
    public World getWorld() {
        return Reflect.getBukkitWorld(textDisplay.entity.level());
    }
    
    @Override
    public void teleport(@Nonnull Location location) {
        this.textDisplay.entity.teleportTo(location.getX(), location.getY(), location.getZ());
        
        this.showingTo.forEach(textDisplay::teleport);
    }
    
    @Override
    public void show(@Nonnull Player player) {
        if (isShowingTo(player)) {
            return;
        }
        
        this.textDisplay.show(player);
        this.textDisplay.text(player, supplier.supply(player));
        
        this.showingTo.add(player);
    }
    
    @Override
    public void hide(@Nonnull Player player) {
        this.textDisplay.hide(player);
        
        this.showingTo.remove(player);
    }
    
    @Nonnull
    @Override
    public Collection<Player> showingTo() {
        return Set.copyOf(showingTo);
    }
    
    @Override
    public void destroy() {
        this.showingTo.forEach(textDisplay::hide);
        this.showingTo.clear();
    }
    
    private void updateMetadataAll() {
        this.showingTo.forEach(textDisplay::updateMetadata);
    }
    
    @ApiStatus.Internal
    public static class PacketTextDisplay {
        private final Display.TextDisplay entity;
        private final TextDisplay bukkitEntity;
        
        PacketTextDisplay(@Nonnull Location location) {
            this.entity = new Display.TextDisplay(EntityType.TEXT_DISPLAY, Reflect.getHandle(location.getWorld()));
            this.entity.teleportTo(location.getX(), location.getY(), location.getZ());
            this.entity.setBillboardConstraints(Display.BillboardConstraints.CENTER);
            
            this.bukkitEntity = (TextDisplay) entity.getBukkitEntity();
        }
        
        protected void hide(@Nonnull Player player) {
            Reflect.sendPacket(player, PacketFactory.makePacketRemoveEntity(entity));
        }
        
        protected void teleport(@Nonnull Player player) {
            Reflect.updateEntityLocation(entity, player);
        }
        
        protected void text(@Nonnull Player player, @Nonnull ComponentList components) {
            final TextComponent.Builder builder = Component.text();
            
            int index = 0;
            for (Component component : components) {
                if (index++ != 0) {
                    builder.appendNewline();
                }
                
                // If the string is null or empty we append new line to mimic the armor stand look
                if (component != null && !Components.isEmptyOrNewLine(component)) {
                    builder.append(component);
                }
            }
            
            // Actually set the text
            bukkitEntity.text(builder.build());
            
            // Update metadata for the player
            updateMetadata(player);
        }
        
        protected void show(@Nonnull Player player) {
            Reflect.sendPacket(player, PacketFactory.makePacketAddEntity(entity));
        }
        
        protected void updateMetadata(@Nonnull Player player) {
            Reflect.updateEntityData(entity, player);
        }
        
        @Override
        public String toString() {
            return Components.toString(bukkitEntity.text());
        }
    }
}
