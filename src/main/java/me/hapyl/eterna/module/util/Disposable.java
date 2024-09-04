package me.hapyl.eterna.module.util;

/**
 * Represents an object that can be disposed of, releasing or clearing all of its resources or data.
 */
public interface Disposable {

    /**
     * Releases or clears all resources or data held by this object.
     */
    void dispose();

}
