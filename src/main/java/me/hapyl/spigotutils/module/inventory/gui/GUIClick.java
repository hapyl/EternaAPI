package me.hapyl.spigotutils.module.inventory.gui;

import com.google.common.collect.Maps;
import org.bukkit.event.inventory.ClickType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a click event for a GUI, stored per ClickType.
 */
public class GUIClick {

    private final Map<ClickType, Action> perClickAction;

    public GUIClick() {
        this.perClickAction = Maps.newHashMap();
    }

    public GUIClick(@Nonnull Action click) {
        this();

        setAction(ClickType.LEFT, click);
    }

    @Nonnull
    public Action getFirstAction() {
        for (final Action action : this.perClickAction.values()) {
            return action;
        }

        throw new IllegalStateException("No actions!");
    }

    public void setAction(@Nonnull ClickType type, @Nonnull Action action) {
        this.perClickAction.put(type, action);
    }

    @Nullable
    public Action getAction(@Nonnull ClickType type) {
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

    @Nonnull
    protected Map<ClickType, Action> getEvents() {
        return perClickAction;
    }
}
