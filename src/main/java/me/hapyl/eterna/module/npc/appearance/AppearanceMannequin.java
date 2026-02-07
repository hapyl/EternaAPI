package me.hapyl.eterna.module.npc.appearance;

import com.mojang.authlib.GameProfile;
import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.reflect.Skin;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.item.component.ResolvableProfile;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

/**
 * Represents a {@link EntityType#MANNEQUIN} appearance.
 */
public class AppearanceMannequin extends AppearanceHumanoid {
    
    private final Set<SkinPart> skinParts;
    @NotNull private Skin skin;
    
    public AppearanceMannequin(@NotNull Npc npc, @NotNull Skin skin) {
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
    @NotNull
    public Skin getSkin() {
        return skin;
    }
    
    /**
     * Sets the skin of the {@link Npc}.
     *
     * @param skin - The new skin.
     */
    public void setSkin(@NotNull Skin skin) {
        this.skin = skin;
        this.updateEntityData();
    }
    
    /**
     * Gets a copy of {@link SkinPart} of the appearance.
     *
     * @return a copy of {@link SkinPart} of the appearance.
     */
    @NotNull
    public Set<SkinPart> getSkinParts() {
        return Set.copyOf(skinParts);
    }
    
    /**
     * Sets the {@link SkinPart} visible on the appearance.
     *
     * @param parts - The visible skin parts.
     */
    public void setSkinParts(@NotNull SkinPart... parts) {
        this.skinParts.clear();
        this.skinParts.addAll(Arrays.asList(parts));
        
        this.updateEntityData();
    }
    
    @NotNull
    @Override
    public Mannequin getHandle() {
        return (Mannequin) super.getHandle();
    }
    
    @Override
    public void updateEntityData() {
        // Write skin
        getHandle().setProfile(ResolvableProfile.createResolved(new GameProfile(getUuid(), "", skin.asPropertyMap())));
        
        // Write skin parts
        super.getEntityData().set(SkinPart.CAPE.getAccessor(), (byte) skinParts.stream().mapToInt(SkinPart::getValue).reduce(0, (mask, value) -> mask | value));
        
        super.updateEntityData();
    }
    
}
