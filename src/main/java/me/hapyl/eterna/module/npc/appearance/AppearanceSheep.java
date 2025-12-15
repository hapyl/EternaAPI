package me.hapyl.eterna.module.npc.appearance;

import me.hapyl.eterna.module.npc.Npc;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.sheep.Sheep;

import javax.annotation.Nonnull;

public class AppearanceSheep extends Appearance {
    
    private SheepColor sheepColor;
    private boolean sheared;
    
    public AppearanceSheep(@Nonnull Npc npc, @Nonnull SheepColor color) {
        super(npc, new Sheep(EntityType.SHEEP, dummyWorld()));
        
        this.setColor(color);
    }
    
    @Nonnull
    @Override
    public Sheep getHandle() {
        return (Sheep) super.getHandle();
    }
    
    @Nonnull
    public SheepColor getColor() {
        return this.sheepColor;
    }
    
    public void setColor(@Nonnull SheepColor color) {
        this.sheepColor = color;
        this.updateMetadata();
    }
    
    public boolean isSheared() {
        return sheared;
    }
    
    public void setSheared(boolean sheared) {
        this.sheared = sheared;
        this.updateMetadata();
    }
    
    @Override
    public void updateMetadata() {
        // Write sheep color and sheared flag
        super.getMetadata().set(sheepColor.getAccessor(), (byte) ((sheepColor.ordinal() & 0x0F) | (sheared ? 0x10 : 0)));
        super.updateMetadata();
    }
}
