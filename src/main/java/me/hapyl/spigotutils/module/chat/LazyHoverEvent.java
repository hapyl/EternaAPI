package me.hapyl.spigotutils.module.chat;

import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Easy access hover event builder.
 * {@link LazyEvent}
 */
public enum LazyHoverEvent {

    TEXT(HoverEvent.Action.SHOW_TEXT),
    SHOW_TEXT(HoverEvent.Action.SHOW_TEXT),
    SHOW_ITEM(HoverEvent.Action.SHOW_ITEM),
    SHOW_ENTITY(HoverEvent.Action.SHOW_ENTITY);

    private final HoverEvent.Action action;

    LazyHoverEvent(HoverEvent.Action action) {
        this.action = action;
    }

    @Nonnull
    public HoverEvent of(@Nonnull Object value) {
        return new HoverEvent(this.action, new Text(Chat.format(value)));
    }

}
