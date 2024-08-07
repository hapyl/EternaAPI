package me.hapyl.eterna.module.inventory;

import com.google.common.collect.Sets;
import org.bukkit.event.block.Action;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

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
            if (function.isAcceptsAny(actions)) {
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

    @Nonnull
    public ItemFunction first() throws IllegalStateException {
        for (ItemFunction function : functions) {
            return function;
        }

        throw new IllegalArgumentException("Cannot get the first function because there are none!");
    }

    @Override
    public ItemFunctionList clone() {
        try {
            final ItemFunctionList clone = (ItemFunctionList) super.clone();

            clone.handler = handler;
            clone.functions = Sets.newHashSet(functions);

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    protected void clearFunctions() {
        functions.clear();
    }

}
