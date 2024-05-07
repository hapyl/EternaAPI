package me.hapyl.spigotutils.module.reflect.metadata;

/**
 * Defines all indexes of entity metadata.
 * <br>
 * Learn more at <a href="https://wiki.vg/Entity_metadata">wiki.vg</a>
 */
public interface MetadataIndex {

    /**
     * Index defined for {@link org.bukkit.entity.Entity}.
     */
    interface Entity {
        int BITMASK = 0;
        int AIR_TICKS = 1;
        int CUSTOM_NAME = 2;
        int CUSTOM_NAME_VISIBILITY = 3;
        int IS_SILENT = 4;
        int HAS_NO_GRAVITY = 5;
        int POSE = 6;
        int FROZEN_TICKS = 7;
    }

    /**
     * Index defined for {@link org.bukkit.entity.Interaction}.
     */
    interface Interaction {
        int WIDTH = 8;
        int HEIGHT = 9;
        int RESPONSIVE = 10;
    }

}
