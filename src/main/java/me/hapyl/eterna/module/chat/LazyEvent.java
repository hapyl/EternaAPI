package me.hapyl.eterna.module.chat;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;

import javax.annotation.Nonnull;

public final class LazyEvent {

    private LazyEvent() {
    }

    /**
     * Generates an open url click event.
     *
     * @param url - Utl to open.
     * @return OPEN_URL ClickEvent.
     */
    public static ClickEvent openUrl(@Nonnull String url) {
        return LazyClickEvent.OPEN_URL.of(url);
    }

    /**
     * Generates an open file click event.
     *
     * @param file - File to open.
     * @return OPEN_FILE ClickEvent.
     */
    public static ClickEvent openFile(@Nonnull String file) {
        return LazyClickEvent.OPEN_FILE.of(file);
    }

    /**
     * Generates a run command click event.
     *
     * @param command - Command to run.
     * @return RUN_COMMAND ClickEvent.
     */
    public static ClickEvent runCommand(@Nonnull String command) {
        return LazyClickEvent.RUN_COMMAND.of(command);
    }

    /**
     * Generates a suggest command click event.
     * SUGGEST_COMMAND will put text into player chat.
     *
     * @param command - Command to suggest.
     * @return SUGGEST_COMMAND ClickEvent.
     */
    public static ClickEvent suggestCommand(@Nonnull String command) {
        return LazyClickEvent.SUGGEST_COMMAND.of(command);
    }

    /**
     * Generates a change page click event.
     * CHANGE_PAGE will change the page of a book.
     *
     * @param page - Page to change to.
     * @return CHANGE_PAGE ClickEvent.
     */
    public static ClickEvent changePage(@Nonnull String page) {
        return LazyClickEvent.CHANGE_PAGE.of(page);
    }

    /**
     * Generates a copy to clipboard click event.
     * COPY_TO_CLIPBOARD will copy text to the player's clipboard.
     * <p>
     * This directly copies the text to the player's clipboard without any notification,
     * so {@link LazyEvent#suggestCommand(String)} could be more useful as it provides visual feedback.
     *
     * @param copy - Text to copy.
     * @return COPY_TO_CLIPBOARD ClickEvent.
     */
    public static ClickEvent copyToClipboard(@Nonnull String copy) {
        return LazyClickEvent.COPY_TO_CLIPBOARD.of(copy);
    }

    /**
     * Generates a show text hover event.
     *
     * @param text - Text to show.
     * @return SHOW_TEXT HoverEvent.
     */
    public static HoverEvent showText(@Nonnull String text) {
        return LazyHoverEvent.SHOW_TEXT.of(text);
    }

    /**
     * Generates a show item hover event.
     *
     * @param item - Item to show.
     * @return SHOW_ITEM HoverEvent.
     */
    public static HoverEvent showItem(@Nonnull String item) {
        return LazyHoverEvent.SHOW_ITEM.of(item);
    }

    /**
     * Generates a show entity hover event.
     *
     * @param entity - Entity to show.
     * @return SHOW_ENTITY HoverEvent.
     */
    public static HoverEvent showEntity(@Nonnull String entity) {
        return LazyHoverEvent.SHOW_ENTITY.of(entity);
    }

}
