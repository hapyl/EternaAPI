package me.hapyl.eterna.module.npc.appearance;

import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.util.Validate;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.fox.Fox;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

/**
 * Represents a {@link EntityType#FOX} appearance.
 */
public class AppearanceFox extends Appearance {
    
    private final Set<FoxBehaviour> behaviours;
    private FoxType foxType;
    
    public AppearanceFox(@NotNull Npc npc, @NotNull FoxType foxType) {
        super(npc, new Fox(EntityType.FOX, dummyWorld()));
        
        this.behaviours = EnumSet.noneOf(FoxBehaviour.class);
        this.setFoxType(foxType);
    }
    
    /**
     * Gets the fox type.
     *
     * @return the fox type.
     */
    @NotNull
    public FoxType getFoxType() {
        return foxType;
    }
    
    /**
     * Sets the fox type.
     *
     * @param foxType - The new fox type.
     */
    public void setFoxType(@NotNull FoxType foxType) {
        this.foxType = foxType;
        this.updateEntityData();
    }
    
    /**
     * Gets a copy of current fox behaviours.
     *
     * @return a copy of current fox behaviours.
     */
    @NotNull
    public Set<FoxBehaviour> getBehaviours() {
        return Set.copyOf(behaviours);
    }
    
    /**
     * Sets the new behaviours.
     *
     * @param behaviours - The new behaviours.
     */
    public void setBehaviours(@NotNull FoxBehaviour... behaviours) {
        this.behaviours.clear();
        this.behaviours.addAll(Arrays.asList(Validate.varargs(behaviours, "There must be at least one behaviour!")));
        this.updateEntityData();
    }
    
    @NotNull
    @Override
    public Fox getHandle() {
        return (Fox) super.getHandle();
    }
    
    @Override
    public void updateEntityData() {
        final SynchedEntityData metadata = super.getEntityData();
        
        // Write fox type & behaviours
        metadata.set(foxType.getAccessor(), foxType.getValue());
        metadata.set(FoxBehaviour.SITTING.getAccessor(), (byte) behaviours.stream().mapToInt(FoxBehaviour::getValue).reduce(0, (mask, value) -> mask | value));
        
        super.updateEntityData();
    }
    
}
