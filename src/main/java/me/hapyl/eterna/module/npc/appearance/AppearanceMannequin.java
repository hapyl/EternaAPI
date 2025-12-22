package me.hapyl.eterna.module.npc.appearance;

import com.mojang.authlib.GameProfile;
import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.reflect.Skin;
import me.hapyl.eterna.module.util.Bitmask;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.item.component.ResolvableProfile;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

/**
 * Represents a {@link EntityType#MANNEQUIN} appearance.
 */
public class AppearanceMannequin extends AppearanceHumanoid {
    
    private final Set<SkinPart> skinParts;
    @Nonnull private Skin skin;
    
    public AppearanceMannequin(@Nonnull Npc npc, @Nonnull Skin skin) {
        super(npc, new Mannequin(EntityType.MANNEQUIN, dummyWorld()));
        
        this.skinParts = EnumSet.allOf(SkinPart.class);
        this.skin = skin;
    }
    
    @Override
    public double chairYOffset() {
        return Npc.CHAIR_Y_OFFSET;
    }
    
    /**
     * Gets the current {@link Skin}.
     *
     * @return the current skin.
     */
    @Nonnull
    public Skin getSkin() {
        return skin;
    }
    
    /**
     * Sets the skin of the {@link Npc}.
     *
     * @param skin - The new skin.
     */
    public void setSkin(@Nonnull Skin skin) {
        this.skin = skin;
        this.updateEntityData();
    }
    
    @Nonnull
    public Set<SkinPart> getSkinParts() {
        return Set.copyOf(skinParts);
    }
    
    public void setSkinParts(@Nonnull SkinPart... parts) {
        this.skinParts.clear();
        this.skinParts.addAll(Arrays.asList(parts));
        
        this.updateEntityData();
    }
    
    @Override
    public void updateEntityData() {
        // Write skin
        getHandle().setProfile(ResolvableProfile.createResolved(new GameProfile(getUuid(), "", skin.asPropertyMap())));
        
        // Write skin parts
        super.getEntityData().set(SkinPart.CAPE.getAccessor(), Bitmask.make(skinParts, SkinPart::getAccessorValue));
        
        super.updateEntityData();
    }
    
    @Nonnull
    @Override
    public Mannequin getHandle() {
        return (Mannequin) super.getHandle();
    }
    
}
