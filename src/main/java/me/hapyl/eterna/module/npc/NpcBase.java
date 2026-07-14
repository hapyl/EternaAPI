package me.hapyl.eterna.module.npc;

import net.minecraft.network.syncher.SynchedEntityData;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Represents a base {@link Npc} functionality.
 */
public interface NpcBase {
    
    /**
     * Sets whether this {@link Npc} should be shaking.
     *
     * @param shaking - Whether the npc should be shaking.
     */
    void setShaking(boolean shaking);
    
    /**
     * Sets whether this {@link Npc} should be invisible.
     *
     * @param invisible - Whether the npc should be shaking.
     */
    void setInvisible(boolean invisible);
    
    /**
     * Edits the entity data of this {@link Npc} and updates it.
     *
     * @param editor - The entity data editor.
     */
    void editEntityData(@NotNull Consumer<? super SynchedEntityData> editor);
    
    /**
     * Gets the {@link NpcPose} of this {@link Npc}.
     *
     * @return the pose of this npc.
     */
    @NotNull NpcPose getPose();
    
    /**
     * Sets the {@link NpcPose} of this {@link Npc}.
     *
     * @param pose - The pose to set.
     * @return {@code true} if the pose was successfully set; {@code false} otherwise.
     */
    boolean setPose(@NotNull NpcPose pose);
    
}
