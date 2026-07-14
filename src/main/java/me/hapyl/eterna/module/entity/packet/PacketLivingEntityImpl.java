package me.hapyl.eterna.module.entity.packet;

import me.hapyl.eterna.module.entity.PacketAttributes;
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

import java.util.Optional;

public class PacketLivingEntityImpl<T extends LivingEntity> extends PacketEntityImpl<T> implements PacketAttributes {
    
    PacketLivingEntityImpl(@NonNull T entity, @NotNull Location location) {
        super(entity, location);
    }
    
    @Override
    public void show(@NotNull Player player) {
        super.show(player);
        
        this.updateAttributes(player);
    }
    
    @Override
    public @NotNull Optional<AttributeInstance> getAttributeInstance(@NotNull Holder<Attribute> attribute) {
        return Optional.ofNullable(entity.getAttribute(attribute));
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
        Reflect.sendPacket(player, PacketAttributes.createUpdateAttributesPacket(entity));
    }
    
    @Override
    public void updateAttributes() {
        final ClientboundUpdateAttributesPacket packet = PacketAttributes.createUpdateAttributesPacket(entity);
        
        showingTo.forEach(player -> Reflect.sendPacket(player, packet));
    }
    
}