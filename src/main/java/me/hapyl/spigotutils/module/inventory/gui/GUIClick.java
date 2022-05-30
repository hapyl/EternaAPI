package me.hapyl.spigotutils.module.inventory.gui;

import org.bukkit.event.inventory.ClickType;

import java.util.HashMap;
import java.util.Map;

public class GUIClick {

    private final Map<ClickType, Action> perClickAction;

    public GUIClick(Action click) {
        this.perClickAction = new HashMap<>();
        this.perClickAction.put(ClickType.UNKNOWN, click);
    }

    public void addPerClick(ClickType type, Action action) {
        if (type == ClickType.UNKNOWN) {
            throw new IllegalArgumentException("UNKNOWN Click Type is not allowed!");
        }
        this.perClickAction.put(type, action);
    }

    public Map<ClickType, Action> getEvents() {
        return perClickAction;
    }

    public Action getByClick(ClickType click) {
        return this.perClickAction.getOrDefault(click, null);
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
