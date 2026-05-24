package me.hapyl.eterna.module.entity.packet;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a {@link PacketEntity} that may have {@link Attribute}, which is only available for living entities.
 */
public interface PacketEntityAttributable {
    
    /**
     * Gets the value associated with the given {@link Attribute}, or {@code null} if the attribute is not supported.
     *
     * @param attribute - The attribute for which to retrieve the value.
     * @return an attribute value or {@code null}, if the attribute is not supported.
     * @see Attributes
     */
    @Nullable
    Double getAttribute(@NotNull Holder<Attribute> attribute);
    
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
    
}
