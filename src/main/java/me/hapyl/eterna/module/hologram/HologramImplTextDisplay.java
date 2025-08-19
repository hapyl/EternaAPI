package me.hapyl.eterna.module.hologram;

import com.google.common.collect.Sets;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.reflect.PacketFactory;
import me.hapyl.eterna.module.reflect.Reflect;
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
        return textDisplay.textDisplay.getTextOpacity();
    }
    
    /**
     * Sets the opacity of this hologram.
     *
     * @param opacity - The new opacity.
     */
    public void opacity(byte opacity) {
        this.textDisplay.textDisplay.setTextOpacity(opacity);
        this.updateMetadataAll();
    }
    
    /**
     * Gets the background color, or {@code null} if there is no color.
     *
     * @return the background color, or {@code null} if there is no color.
     */
    @Nullable
    public Color background() {
        final int color = this.textDisplay.textDisplay.getBackgroundColor();
        
        return color != -1 ? Color.fromARGB(color) : null;
    }
    
    /**
     * Sets the background color.
     *
     * @param color - The color to set.
     */
    public void background(@Nullable Color color) {
        this.textDisplay.textDisplay.getEntityData().set(Display.TextDisplay.DATA_BACKGROUND_COLOR_ID, color != null ? color.asARGB() : -1);
        this.updateMetadataAll();
    }
    
    @Override
    public void setLines(@Nonnull LineSupplier supplier) {
        super.setLines(supplier);
        
        // Update for all players
        this.showingTo.removeIf(Predicate.not(Player::isOnline));
        this.showingTo.forEach(player -> textDisplay.text(player, supplier.supply(player)));
    }
    
    @Nonnull
    @Override
    public Location getLocation() {
        return Reflect.getEntityLocation(textDisplay.textDisplay);
    }
    
    @Nonnull
    @Override
    public World getWorld() {
        return Reflect.getBukkitWorld(textDisplay.textDisplay.level());
    }
    
    @Override
    public void teleport(@Nonnull Location location) {
        this.textDisplay.textDisplay.teleportTo(location.getX(), location.getY(), location.getZ());
        
        this.showingTo.forEach(textDisplay::teleport);
    }
    
    @Override
    public void show(@Nonnull Player player) {
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
        private final Display.TextDisplay textDisplay;
        
        PacketTextDisplay(@Nonnull Location location) {
            this.textDisplay = new Display.TextDisplay(EntityType.TEXT_DISPLAY, Reflect.getMinecraftWorld(location.getWorld()));
            this.textDisplay.teleportTo(location.getX(), location.getY(), location.getZ());
            
            this.textDisplay.setBillboardConstraints(Display.BillboardConstraints.VERTICAL);
        }
        
        protected void hide(@Nonnull Player player) {
            Reflect.sendPacket(player, PacketFactory.makePacketRemoveEntity(textDisplay));
        }
        
        protected void teleport(@Nonnull Player player) {
            Reflect.updateEntityLocation(textDisplay, player);
        }
        
        @SuppressWarnings("deprecation"/* fucking paper */)
        protected void text(@Nonnull Player player, @Nonnull StringArray array) {
            final StringBuilder builder = new StringBuilder();
            
            int index = 0;
            for (String string : array) {
                if (index++ != 0) {
                    builder.append("\n");
                }
                
                // If the string is null or empty we append new line to mimic the armor stand look
                if (string == null || string.isEmpty()) {
                    builder.append("\n");
                }
                else {
                    builder.append(Chat.format(string));
                }
            }
            
            // Actually set the text
            ((TextDisplay) textDisplay.getBukkitEntity()).setText(builder.toString());
            
            // Update metadata for the player
            updateMetadata(player);
        }
        
        protected void show(@Nonnull Player player) {
            Reflect.sendPacket(player, PacketFactory.makePacketAddEntity(textDisplay));
        }
        
        protected void updateMetadata(@Nonnull Player player) {
            Reflect.updateMetadata(textDisplay, player);
        }
        
        @Override
        public String toString() {
            return ((TextDisplay) textDisplay.getBukkitEntity()).getText();
        }
    }
}
