package me.hapyl.eterna.module.npc;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.function.Function;

/**
 * Represents a look anchor for an {@link Npc}.
 */
public enum LookAnchor implements Function<LivingEntity, Location> {
    
    /**
     * Anchors to look at the entity's eyes.
     */
    EYES {
        @Override
        public Location apply(LivingEntity entity) {
            return entity.getEyeLocation();
        }
    },
    
    /**
     * Anchors to look at the entity's feet.
     */
    FEET {
        @Override
        public Location apply(LivingEntity entity) {
            return entity.getLocation();
        }
    }
    
}
