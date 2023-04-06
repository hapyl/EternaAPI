package me.hapyl.spigotutils.module.chat;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;

import javax.annotation.Nullable;

public class LazyEvent {

    /**
     * Generates an open url click event.
     *
     * @param url    - URL to open.
     * @param format - Format the url.
     * @return OPEN_URL ClickEvent.
     */
    public static ClickEvent openUrl(String url, @Nullable Object... format) {
        return LazyClickEvent.OPEN_URL.of(url, format);
    }

    /**
     * Generates an open file click event.
     *
     * @param file   - File to open.
     * @param format - Format the file.
     * @return OPEN_FILE ClickEvent.
     */
    public static ClickEvent openFile(String file, @Nullable Object... format) {
        return LazyClickEvent.OPEN_FILE.of(file, format);
    }

    /**
     * Generates a run command click event.
     *
     * @param command - Command to run.
     * @param format  - Format the command.
     * @return RUN_COMMAND ClickEvent.
     */
    public static ClickEvent runCommand(String command, @Nullable Object... format) {
        return LazyClickEvent.RUN_COMMAND.of(command, format);
    }

    /**
     * Generates a suggest command click event.
     * SUGGEST_COMMAND will put text into players chat.
     *
     * @param command - Command to suggest.
     * @param format  - Format the command.
     * @return SUGGEST_COMMAND ClickEvent.
     */
    public static ClickEvent suggestCommand(String command, @Nullable Object... format) {
        return LazyClickEvent.SUGGEST_COMMAND.of(command, format);
    }

    /**
     * Generates a change page click event.
     * CHANGE_PAGE will change the page of a book.
     *
     * @param page   - Page to change to.
     * @param format - Format the page.
     * @return CHANGE_PAGE ClickEvent.
     */
    public static ClickEvent changePage(String page, @Nullable Object... format) {
        return LazyClickEvent.CHANGE_PAGE.of(page, format);
    }

    /**
     * Generates a copy to clipboard click event.
     * COPY_TO_CLIPBOARD will copy text to the player's clipboard.
     * <p>
     * This directly copies the text to player's clipboard without any notification, so SUGGEST_COMMAND could be more useful as it provides visual feedback.
     *
     * @param copy   - Text to copy.
     * @param format - Format the text.
     * @return COPY_TO_CLIPBOARD ClickEvent.
     */
    public static ClickEvent copyToClipboard(String copy, @Nullable Object... format) {
        return LazyClickEvent.COPY_TO_CLIPBOARD.of(copy, format);
    }

    /**
     * Generates a show text hover event.
     *
     * @param text   - Text to show.
     * @param format - Format the text.
     * @return SHOW_TEXT HoverEvent.
     */
    public static HoverEvent showText(String text, @Nullable Object... format) {
        return LazyHoverEvent.SHOW_TEXT.of(text, format);
    }

    /**
     * Generates a show item hover event.
     *
     * @param item   - Item to show.
     * @param format - Format the item.
     * @return SHOW_ITEM HoverEvent.
     */
    public static HoverEvent showItem(String item, @Nullable Object... format) {
        return LazyHoverEvent.SHOW_ITEM.of(item, format);
    }

    /**
     * Generates a show entity hover event.
     *
     * @param entity - Entity to show.
     * @param format - Format the entity.
     * @return SHOW_ENTITY HoverEvent.
     */
    public static HoverEvent showEntity(String entity, @Nullable Object... format) {
        return LazyHoverEvent.SHOW_ENTITY.of(entity, format);
    }

}
