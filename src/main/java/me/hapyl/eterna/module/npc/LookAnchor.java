package me.hapyl.eterna.module.npc;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Represents a look anchor for an {@link Npc}.
 */
public enum LookAnchor implements Function<Entity, Location> {
    
    /**
     * Anchors to look at the entity's eyes.
     */
    EYES {
        @Override
        public Location apply(@NotNull Entity entity) {
            if (entity instanceof LivingEntity livingEntity) {
                return livingEntity.getEyeLocation();
            }
            else {
                final Location location = entity.getLocation();
                
                location.setY(entity.getBoundingBox().getMaxY());
                return location;
            }
        }
    },
    
    /**
     * Anchors to look at the entity's feet.
     */
    FEET {
        @Override
        public Location apply(@NotNull Entity entity) {
            return entity.getLocation();
        }
    }
    
}
