package me.hapyl.eterna.module.npc.appearance;

/**
 * Abstract appearance for entities that may be of a baby variant.
 */
public interface AppearanceBaby {
    
    /**
     * Sets whether the appearance is of a baby variant.
     *
     * @param isBaby - Whether the appearance is of a baby variant.
     */
    void setBaby(boolean isBaby);
    
    /**
     * Gets whether the appearance is of a baby variant.
     *
     * @return whether the appearance is of a baby variant.
     */
    boolean isBaby();
    
}
