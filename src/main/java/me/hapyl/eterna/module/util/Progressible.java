package me.hapyl.eterna.module.util;

/**
 * Represents an object that may have progression of sorts.
 */
public interface Progressible {
    
    /**
     * Gets the current progress.
     *
     * @return the current progress.
     */
    double currentProgress();
    
    /**
     * Gets the maximum progress.
     *
     * @return the maximum progress.
     */
    double maxProgress();
    
}
