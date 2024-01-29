package me.hapyl.spigotutils.module.block.display;

import com.google.common.collect.Lists;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.joml.Matrix4f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a display entity.
 * An entity consists of a head (always present) and children.
 */
public class DisplayEntity implements Iterable<Display>, IDisplay {

    private final BlockDisplay head;
    private final List<Display> children;

    public DisplayEntity(@Nonnull BlockDisplay head) {
        this.head = head;
        this.children = Lists.newArrayList();
        this.children.add(head);
    }

    /**
     * Teleports the entity to the given location.
     *
     * @param location - Location to teleport to.
     */
    public void teleport(@Nonnull Location location) {
        head.teleport(location);
        children.forEach(display -> display.teleport(location));
    }

    /**
     * Removes the entity, including the head and children.
     */
    public void remove() {
        head.remove();

        children.forEach(Display::remove);
        children.clear();
    }

    /**
     * Gets a copy of all entities, including head and children.
     *
     * @return Gets a copy of all entities, including head and children.
     */
    @Nonnull
    public List<Display> getEntities() {
        return Lists.newArrayList(children);
    }

    /**
     * Gets the head of this entity.
     *
     * @return the head of this entity.
     */
    @Nonnull
    public BlockDisplay getHead() {
        return head;
    }

    /**
     * Gets an iterator for this entity.
     *
     * @return an iterator.
     */
    @Nonnull
    @Override
    public Iterator<Display> iterator() {
        return children.iterator();
    }

    @Nonnull
    @Override
    public Transformation getTransformation() {
        return head.getTransformation();
    }

    @Override
    public void setTransformation(@Nonnull Transformation transformation) {
        head.setTransformation(transformation);
    }

    @Override
    public void setTransformationMatrix(@Nonnull Matrix4f transformationMatrix) {
        head.setTransformationMatrix(transformationMatrix);
    }

    @Override
    public int getInterpolationDuration() {
        return head.getInterpolationDuration();
    }

    @Override
    public void setInterpolationDuration(int duration) {
        head.setInterpolationDuration(duration);
    }

    @Override
    public int getTeleportDuration() {
        return head.getTeleportDuration();
    }

    @Override
    public void setTeleportDuration(int duration) {
        forEach(display -> display.setTeleportDuration(duration));
    }

    @Override
    public float getViewRange() {
        return head.getViewRange();
    }

    @Override
    public void setViewRange(float range) {
        forEach(display -> display.setViewRange(range));
    }

    @Override
    public float getShadowRadius() {
        return head.getShadowRadius();
    }

    @Override
    public void setShadowRadius(float radius) {
        forEach(display -> display.setShadowRadius(radius));
    }

    @Override
    public float getShadowStrength() {
        return head.getShadowStrength();
    }

    @Override
    public void setShadowStrength(float strength) {
        forEach(display -> display.setShadowStrength(strength));
    }

    @Override
    public float getDisplayWidth() {
        return head.getDisplayWidth();
    }

    @Override
    public void setDisplayWidth(float width) {
        forEach(display -> display.setDisplayWidth(width));
    }

    @Override
    public float getDisplayHeight() {
        return head.getDisplayHeight();
    }

    @Override
    public void setDisplayHeight(float height) {
        forEach(display -> display.setDisplayHeight(height));
    }

    @Override
    public int getInterpolationDelay() {
        return head.getInterpolationDelay();
    }

    @Override
    public void setInterpolationDelay(int ticks) {
        forEach(display -> display.setInterpolationDelay(ticks));
    }

    @Nonnull
    @Override
    public Display.Billboard getBillboard() {
        return head.getBillboard();
    }

    @Override
    public void setBillboard(@Nonnull Display.Billboard billboard) {
        forEach(display -> display.setBillboard(billboard));
    }

    @Nullable
    @Override
    public Color getGlowColorOverride() {
        return head.getGlowColorOverride();
    }

    @Override
    public void setGlowColorOverride(@Nullable Color color) {
        forEach(display -> display.setGlowColorOverride(color));
    }

    @Nullable
    @Override
    public Display.Brightness getBrightness() {
        return head.getBrightness();
    }

    @Override
    public void setBrightness(@Nullable Display.Brightness brightness) {
        forEach(display -> display.setBrightness(brightness));
    }

    /**
     * Adds a display as a child.
     *
     * @param display  - Display to add.
     * @param consumer - Consumer if needed.
     */
    protected void append(@Nonnull Display display, @Nullable Consumer<Display> consumer) {
        head.addPassenger(display);
        children.add(display);

        if (consumer != null) {
            consumer.accept(display);
        }
    }
}
