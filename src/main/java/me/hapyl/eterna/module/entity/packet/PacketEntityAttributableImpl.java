package me.hapyl.eterna.module.entity.packet;

import me.hapyl.eterna.module.reflect.Reflect;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public class PacketEntityAttributableImpl<T extends LivingEntity> extends PacketEntityImpl<T> implements PacketEntityAttributable {
    
    PacketEntityAttributableImpl(@NonNull T entity, @NotNull Location location) {
        super(entity, location);
    }
    
    @Override
    public void show(@NotNull Player player) {
        super.show(player);
        
        this.updateAttributes(player);
    }
    
    @Nullable
    @Override
    public Double getAttribute(@NotNull Holder<Attribute> attribute) {
        final AttributeInstance attributeInstance = entity.getAttribute(attribute);
        
        return attributeInstance != null ? attributeInstance.getBaseValue() : null;
    }
    
    @Override
    public void setAttribute(@NotNull Holder<Attribute> attribute, double value) {
        final AttributeInstance attributeInstance = entity.getAttribute(attribute);
        
        if (attributeInstance != null) {
            attributeInstance.setBaseValue(value);
        }
    }
    
    @Override
    public void updateAttributes(@NotNull Player player) {
        Reflect.sendPacket(player, new ClientboundUpdateAttributesPacket(entity.getId(), entity.getAttributes().getSyncableAttributes()));
    }
    
    @Override
    public void updateAttributes() {
        showingTo.forEach(this::updateAttributes);
    }
    
}