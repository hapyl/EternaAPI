package me.hapyl.eterna.module.chat;

import net.md_5.bungee.api.chat.ClickEvent;

import javax.annotation.Nonnull;

/**
 * Easy access click event builder.
 * {@link LazyEvent}
 */
public enum LazyClickEvent {

    OPEN_URL(ClickEvent.Action.OPEN_URL),
    OPEN_FILE(ClickEvent.Action.OPEN_FILE),
    RUN_COMMAND(ClickEvent.Action.RUN_COMMAND),
    SUGGEST_COMMAND(ClickEvent.Action.SUGGEST_COMMAND),
    CHANGE_PAGE(ClickEvent.Action.CHANGE_PAGE),
    COPY_TO_CLIPBOARD(ClickEvent.Action.COPY_TO_CLIPBOARD);

    private final ClickEvent.Action action;

    LazyClickEvent(ClickEvent.Action action) {
        this.action = action;
    }

    @Nonnull
    public ClickEvent of(@Nonnull Object value) {
        return new ClickEvent(this.action, Chat.format(value));
    }

}
