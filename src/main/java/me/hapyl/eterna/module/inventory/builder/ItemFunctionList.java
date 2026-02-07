package me.hapyl.eterna.module.inventory.builder;

import com.google.common.collect.Sets;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.registry.Keyed;
import org.bukkit.event.block.Action;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a {@link Set} of {@link ItemFunction} supported on an {@link ItemBuilder}.
 */
@ApiStatus.Internal
public final class ItemFunctionList implements Cloneable, Keyed {
    
    private final Key key;
    
    Set<ItemFunction> functions;
    ItemEventListener listener;
    
    @ApiStatus.Internal
    ItemFunctionList(@NotNull Key key) {
        this.key = key;
        this.functions = Sets.newHashSet();
    }
    
    @NotNull
    @Override
    public Key getKey() {
        return key;
    }
    
    /**
     * Adds the given {@link ItemFunction}.
     *
     * @param function - The function to add.
     */
    public void add(@NotNull ItemFunction function) {
        this.functions.add(function);
    }
    
    /**
     * Remove the given {@link ItemFunction}.
     *
     * @param function - The function to remove.
     */
    public void remove(@NotNull ItemFunction function) {
        this.functions.remove(function);
    }
    
    /**
     * Sets the {@link ItemEventListener}.
     *
     * @param listener - The listener to set.
     */
    public void listener(@Nullable ItemEventListener listener) {
        this.listener = listener;
    }
    
    /**
     * Matches the given functions against the given set of actions.
     *
     * @param actions - The actions to match.
     * @return a set of matching functions.
     */
    @NotNull
    @ApiStatus.Internal
    public Set<ItemFunction> matchFunctions(@NotNull Set<Action> actions) {
        return this.functions.stream()
                             .filter(function -> !Collections.disjoint(function.actions, actions))
                             .collect(Collectors.toSet());
    }
    
    /**
     * Matches the given functions against the given action.
     *
     * @param action - The action to match.
     * @return a set of matching functions.
     */
    @NotNull
    @ApiStatus.Internal
    public Set<ItemFunction> matchFunctions(@NotNull Action action) {
        return this.functions.stream()
                             .filter(function -> function.actions.contains(action))
                             .collect(Collectors.toSet());
    }
    
    /**
     * Clones this {@link ItemFunctionList}.
     *
     * @return a cloned functions.
     */
    @Override
    public ItemFunctionList clone() {
        try {
            final ItemFunctionList clone = (ItemFunctionList) super.clone();
            
            clone.listener = this.listener;
            clone.functions = this.functions.stream()
                                            .map(ItemFunction::clone)
                                            .collect(Collectors.toCollection(HashSet::new));
            
            return clone;
        }
        catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
    
}
