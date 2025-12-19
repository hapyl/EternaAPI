package me.hapyl.eterna.module.npc.appearance;

import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.util.Bitmask;
import me.hapyl.eterna.module.util.Validate;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.fox.Fox;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

/**
 * Represents a {@link EntityType#FOX} appearance.
 */
public class AppearanceFox extends Appearance {
    
    private final Set<FoxBehaviour> behaviours;
    private FoxType foxType;
    
    public AppearanceFox(@Nonnull Npc npc, @Nonnull FoxType foxType) {
        super(npc, new Fox(EntityType.FOX, dummyWorld()));
        
        this.behaviours = EnumSet.noneOf(FoxBehaviour.class);
        this.setFoxType(foxType);
    }
    
    /**
     * Gets the fox type.
     *
     * @return the fox type.
     */
    @Nonnull
    public FoxType getFoxType() {
        return foxType;
    }
    
    /**
     * Sets the fox type.
     *
     * @param foxType - The new fox type.
     */
    public void setFoxType(@Nonnull FoxType foxType) {
        this.foxType = foxType;
        this.updateMetadata();
    }
    
    /**
     * Gets a copy of current fox behaviours.
     *
     * @return a copy of current fox behaviours.
     */
    @Nonnull
    public Set<FoxBehaviour> getBehaviours() {
        return Set.copyOf(behaviours);
    }
    
    /**
     * Sets the new behaviours.
     *
     * @param behaviours - The new behaviours.
     */
    public void setBehaviours(@Nonnull FoxBehaviour... behaviours) {
        this.behaviours.clear();
        this.behaviours.addAll(Arrays.asList(Validate.varargs(behaviours, "There must be at least one behaviour!")));
        this.updateMetadata();
    }
    
    @Nonnull
    @Override
    public Fox getHandle() {
        return (Fox) super.getHandle();
    }
    
    @Override
    public void updateMetadata() {
        final SynchedEntityData metadata = super.getMetadata();
        
        // Write fox type
        metadata.set(foxType.getAccessor(), foxType.getAccessorValue());
        
        // Write fox behaviors
        metadata.set(FoxBehaviour.SITTING.getAccessor(), Bitmask.make(behaviours, FoxBehaviour::getAccessorValue));
        
        super.updateMetadata();
    }
    
}
