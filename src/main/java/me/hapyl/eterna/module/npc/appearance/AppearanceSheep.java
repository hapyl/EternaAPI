package me.hapyl.eterna.module.npc.appearance;

import me.hapyl.eterna.module.npc.Npc;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.sheep.Sheep;

import javax.annotation.Nonnull;

/**
 * Represents a {@link EntityType#SHEEP} appearance.
 */
public class AppearanceSheep extends Appearance {
    
    private SheepColor sheepColor;
    private boolean sheared;
    
    public AppearanceSheep(@Nonnull Npc npc, @Nonnull SheepColor color) {
        super(npc, new Sheep(EntityType.SHEEP, dummyWorld()));
        
        this.setColor(color);
    }
    
    /**
     * Gets the {@link SheepColor}.
     *
     * @return the {@link SheepColor}.
     */
    @Nonnull
    public SheepColor getColor() {
        return this.sheepColor;
    }
    
    /**
     * Sets the {@link SheepColor}.
     *
     * @param color - The new sheep color.
     */
    public void setColor(@Nonnull SheepColor color) {
        this.sheepColor = color;
        this.updateEntityData();
    }
    
    /**
     * Gets whether the sheep is sheared.
     *
     * @return {@code true} if the sheep is sheared, {@code false} otherwise.
     */
    public boolean isSheared() {
        return sheared;
    }
    
    /**
     * Sets whether the sheep is sheared.
     *
     * @param sheared - Whether the sheep is sheared.
     */
    public void setSheared(boolean sheared) {
        this.sheared = sheared;
        this.updateEntityData();
    }
    
    @Nonnull
    @Override
    public Sheep getHandle() {
        return (Sheep) super.getHandle();
    }
    
    @Override
    public void updateEntityData() {
        // Write sheep color and sheared flag
        super.getEntityData().set(sheepColor.getAccessor(), (byte) ((sheepColor.ordinal() & 0x0F) | (sheared ? 0x10 : 0)));
        super.updateEntityData();
    }
}
