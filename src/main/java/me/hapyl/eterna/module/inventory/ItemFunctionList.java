package me.hapyl.eterna.module.inventory;

import com.google.common.collect.Sets;
import org.bukkit.event.block.Action;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ItemFunctionList implements Cloneable {
    
    protected Set<ItemFunction> functions;
    protected ItemEventHandler handler;
    
    public ItemFunctionList() {
        this.functions = Sets.newHashSet();
    }
    
    /**
     * Adds an {@link ItemFunction}.
     *
     * @param function - A function to add.
     */
    public void addFunction(@Nonnull ItemFunction function) {
        functions.add(function);
    }
    
    /**
     * Gets a {@link Set} of {@link ItemFunction} that accepts the given {@link Action}s.
     *
     * @param actions - Actions.
     * @return a set of functions that accepts the given actions.
     */
    @Nonnull
    public Set<ItemFunction> getFunctions(@Nonnull Action... actions) {
        final Set<ItemFunction> functions = Sets.newHashSet();
        
        if (actions.length == 0) {
            return functions;
        }
        
        for (ItemFunction function : this.functions) {
            if (function.accepts(actions)) {
                functions.add(function);
            }
        }
        
        return functions;
    }
    
    @Nullable
    public ItemFunction getFunction(@Nonnull Action... actions) {
        final Set<ItemFunction> functions = getFunctions(actions);
        
        for (ItemFunction function : functions) {
            return function;
        }
        
        return null;
    }
    
    @Nullable
    public ItemEventHandler getHandler() {
        return handler;
    }
    
    public void setHandler(@Nullable ItemEventHandler handler) {
        this.handler = handler;
    }
    
    @Override
    public ItemFunctionList clone() {
        try {
            final ItemFunctionList clone = (ItemFunctionList) super.clone();
            clone.handler = this.handler;
            clone.functions = this.functions.stream()
                                            .map(ItemFunction::clone)
                                            .collect(Collectors.toCollection(HashSet::new));
            
            return clone;
        }
        catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
    
    protected void clearFunctions() {
        functions.clear();
    }
    
}
