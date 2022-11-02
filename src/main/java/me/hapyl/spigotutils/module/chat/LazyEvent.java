package me.hapyl.spigotutils.module.chat;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;

import javax.annotation.Nullable;

public class LazyEvent {

    public static ClickEvent openUrl(String url, @Nullable Object... format) {
        return LazyClickEvent.OPEN_URL.of(url, format);
    }

    public static ClickEvent openFile(String file, @Nullable Object... format) {
        return LazyClickEvent.OPEN_FILE.of(file, format);
    }

    public static ClickEvent runCommand(String command, @Nullable Object... format) {
        return LazyClickEvent.RUN_COMMAND.of(command, format);
    }

    public static ClickEvent suggestCommand(String command, @Nullable Object... format) {
        return LazyClickEvent.SUGGEST_COMMAND.of(command, format);
    }

    public static ClickEvent changePage(String page, @Nullable Object... format) {
        return LazyClickEvent.CHANGE_PAGE.of(page, format);
    }

    public static ClickEvent copyToClipboard(String copy, @Nullable Object... format) {
        return LazyClickEvent.COPY_TO_CLIPBOARD.of(copy, format);
    }

    public static HoverEvent showText(String text, @Nullable Object... format) {
        return LazyHoverEvent.SHOW_TEXT.of(text, format);
    }

    public static HoverEvent showItem(String item, @Nullable Object... format) {
        return LazyHoverEvent.SHOW_ITEM.of(item, format);
    }

    public static HoverEvent showEntity(String entity, @Nullable Object... format) {
        return LazyHoverEvent.SHOW_ENTITY.of(entity, format);
    }

}
