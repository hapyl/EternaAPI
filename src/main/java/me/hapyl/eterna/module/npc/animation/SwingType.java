package me.hapyl.eterna.module.npc.animation;

import net.minecraft.world.item.SwingAnimationType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the swing type.
 */
public enum SwingType {
    
    /**
     * Defines the no swinging animation.
     */
    NONE(SwingAnimationType.NONE),
    
    /**
     * Defines the whack animation, which is used for most attacks.
     */
    WHACK(SwingAnimationType.WHACK),
    
    /**
     * Defines the stab animation, which is used for spears.
     */
    STAB(SwingAnimationType.STAB);
    
    private final SwingAnimationType nms;
    
    SwingType(@NotNull SwingAnimationType nms) {
        this.nms = nms;
    }
    
    /**
     * Gets the vanilla {@link SwingAnimationType}.
     *
     * @return the vanilla swing animation type.
     */
    public @NotNull SwingAnimationType getNms() {
        return nms;
    }
    
}
