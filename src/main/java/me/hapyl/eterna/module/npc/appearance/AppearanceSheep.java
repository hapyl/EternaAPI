package me.hapyl.eterna.module.npc.appearance;

import me.hapyl.eterna.module.npc.Npc;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.sheep.Sheep;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link Sheep} appearance.
 */
public class AppearanceSheep extends Appearance {
    
    private SheepColor sheepColor;
    private boolean sheared;
    
    public AppearanceSheep(@NotNull Npc npc, @NotNull SheepColor color) {
        super(npc, new Sheep(EntityTypes.SHEEP, dummyWorld()));
        
        this.sheepColor = color;
    }
    
    /**
     * Gets the {@link SheepColor}.
     *
     * @return the {@link SheepColor}.
     */
    @NotNull
    public SheepColor getColor() {
        return this.sheepColor;
    }
    
    /**
     * Sets the {@link SheepColor}.
     *
     * @param color - The new sheep color.
     */
    public void setColor(@NotNull SheepColor color) {
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
    
    @Override
    public void onDataUpdated(@NotNull SynchedEntityData entityData) {
        entityData.set(sheepColor.getAccessor(), (byte) ((sheepColor.ordinal() & 0x0F) | (sheared ? 0x10 : 0)));
    }
    
    @NotNull
    @Override
    public Sheep getHandle() {
        return (Sheep) super.getHandle();
    }
    
}
