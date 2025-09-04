package me.hapyl.eterna.module.component;

import com.google.common.collect.Lists;
import me.hapyl.eterna.module.annotate.SelfReturn;
import me.hapyl.eterna.module.util.Streamer;
import net.kyori.adventure.text.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a simple {@link List} implementation for {@link Component}.
 */
public class ComponentList implements Iterable<Component>, Streamer<Component> {
    
    protected final List<Component> components;
    
    ComponentList(@Nonnull Component... components) {
        this.components = Lists.newArrayList(components);
    }
    
    /**
     * Appends the given {@link Component} to this list.
     *
     * @param component - The component to append.
     */
    @SelfReturn
    public ComponentList append(@Nonnull Component component) {
        components.add(component);
        return this;
    }
    
    /**
     * Appends the given {@link ComponentList} to this list.
     *
     * @param other - The list to append.
     */
    @SelfReturn
    public ComponentList append(@Nonnull ComponentList other) {
        components.addAll(other.components);
        return this;
    }
    
    /**
     * Appends an empty {@link Component} to this list.
     */
    @SelfReturn
    public ComponentList appendEmpty() {
        components.add(Component.empty());
        return this;
    }
    
    /**
     * Retrieves a {@link Component} at the given {@code index}, or {@code null} if index is out of bounds.
     *
     * @param index - The index to retrieve a {@link Component} at.
     * @return the retrieved component.
     */
    @Nullable
    public Component get(int index) {
        return index < 0 || index >= size() ? null : components.get(index);
    }
    
    /**
     * Gets the size of this {@link ComponentList}.
     *
     * @return the size of this {@link ComponentList}.
     */
    public int size() {
        return components.size();
    }
    
    /**
     * Gets an immutable copy of this {@link ComponentList} as {@link List}.
     *
     * @return an immutable copy of this {@link ComponentList} as {@link List}.
     */
    @Nonnull
    public List<Component> toList() {
        return List.copyOf(components);
    }
    
    /**
     * Gets a mutable copy of this {@link ComponentList} as an array.
     *
     * @return mutable copy of this {@link ComponentList} as an array.
     */
    @Nonnull
    public Component[] toArray() {
        return components.toArray(Component[]::new);
    }
    
    /**
     * Gets an {@link Iterator} for this {@link ComponentList}.
     *
     * @return an {@link Iterator} for this {@link ComponentList}.
     */
    @Nonnull
    @Override
    public Iterator<Component> iterator() {
        return components.iterator();
    }
    
    /**
     * Gets a {@link Stream} for this {@link ComponentList}.
     *
     * @return a {@link Stream} for this {@link ComponentList}.
     */
    @Nonnull
    @Override
    public Stream<Component> stream() {
        return components.stream();
    }
    
    /**
     * Gets a string representation of this {@link ComponentList}.
     *
     * @return a string representation of this {@link ComponentList}.
     */
    @Override
    public String toString() {
        return "[" + components.stream()
                               .map(Components::toString)
                               .collect(Collectors.joining(", ")) + "]";
    }
    
    /**
     * Creates a new {@link ComponentList} from the given {@link Component}.
     *
     * @param components - The components to create a list from.
     * @return a new {@link ComponentList} containing the given {@link Component}.
     */
    @Nonnull
    public static ComponentList of(@Nonnull Component... components) {
        return new ComponentList(components);
    }
    
    /**
     * Converts the given objects into a legacy {@link ComponentList}.
     *
     * @param legacy - The objects to convert.
     * @return a {@link ComponentList} containing the legacy {@link Component}.
     */
    @Nonnull
    public static ComponentList ofLegacy(@Nonnull String... legacy) {
        return Arrays.stream(legacy)
                     .map(Components::ofLegacyOrNull)
                     .collect(ComponentList.collector());
    }
    
    /**
     * Creates a new empty {@link ComponentList}.
     *
     * @return a new empty {@link ComponentList}.
     */
    @Nonnull
    public static ComponentList empty() {
        return new ComponentList();
    }
    
    /**
     * Creates a {@link Collector} for {@link Stream} operations.
     *
     * @return a new collector.
     */
    @Nonnull
    public static Collector<? super Component, ComponentList, ComponentList> collector() {
        return Collector.of(
                ComponentList::new,
                ComponentList::append,
                (list1, list2) -> {
                    list1.append(list2);
                    
                    return list1;
                }
        );
    }
}
