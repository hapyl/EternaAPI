package me.hapyl.eterna.module.inventory.gui;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Represents a list of actions, mapped per-click for {@link PlayerGUI}.
 */
public class ActionList {
    
    protected final Map<ClickType, Consumer<Player>> perClickAction;
    
    public ActionList() {
        this.perClickAction = Maps.newHashMap();
    }
    
    public ActionList(@Nonnull Consumer<Player> click) {
        this();
        
        setAction(ClickType.LEFT, click);
    }
    
    /**
     * Maps the given action to the given type.
     *
     * @param type   - The click type.
     * @param action - The action.
     */
    public void setAction(@Nonnull ClickType type, @Nonnull Consumer<Player> action) {
        this.perClickAction.put(type, action);
    }
    
    /**
     * Gets the action for the given click type.
     *
     * @param type - The click type.
     * @return the action for the given click type.
     */
    @Nullable
    public Consumer<Player> getAction(@Nonnull ClickType type) {
        return this.perClickAction.get(type);
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("[");
        for (ClickType clickType : perClickAction.keySet()) {
            builder.append(clickType.name()).append(",");
        }
        return builder.append("]").toString();
    }
    
}
