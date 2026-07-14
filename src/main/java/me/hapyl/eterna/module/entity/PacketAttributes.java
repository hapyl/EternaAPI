package me.hapyl.eterna.module.entity;

import net.minecraft.core.Holder;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Represents an entity that supports {@link Attributes} via packets.
 *
 * <p>
 * Note that packet attributes only support modifying base values.
 * </p>
 */
public interface PacketAttributes {
    
    /**
     * Gets the {@link AttributeInstance} for the given {@link Attribute} wrapped in an optional.
     *
     * @param attribute - The attribute type.
     * @return an optional containing the attribute instance or an empty optional.
     */
    @NotNull Optional<AttributeInstance> getAttributeInstance(@NotNull Holder<Attribute> attribute);
    
    /**
     * Gets the value associated with the given {@link Attribute}, or {@code null} if the attribute is not supported.
     *
     * @param attribute - The attribute for which to retrieve the value.
     * @return an attribute value or {@code null}, if the attribute is not supported.
     * @see Attributes
     */
    @Nullable Double getAttribute(@NotNull Holder<Attribute> attribute);
    
    /**
     * Sets the attribute value for this entity.
     *
     * <p>
     * Note that mutating attributes after showing the entity requires manual update via {@link #updateAttributes(Player)} or {@link #updateAttributes()}.
     * </p>
     *
     * @param attribute - The attribute to set.
     * @param value     - The new value.
     * @see Attributes
     */
    void setAttribute(@NotNull Holder<Attribute> attribute, double value);
    
    /**
     * Updates the attributes of this entity for the given {@link Player}.
     *
     * @param player - The player for whom to update.
     */
    void updateAttributes(@NotNull Player player);
    
    /**
     * Updates the attributes of this entity for all players who can see it.
     */
    void updateAttributes();
    
    /**
     * An internal method for creating a {@link ClientboundUpdateAttributesPacket} for the given entity.
     *
     * @param entity - The entity for whom to update attributes.
     * @return the packet.
     */
    static @NotNull ClientboundUpdateAttributesPacket createUpdateAttributesPacket(@NotNull LivingEntity entity) {
        return new ClientboundUpdateAttributesPacket(entity.getId(), entity.getAttributes().getSyncableAttributes());
    }
    
}
