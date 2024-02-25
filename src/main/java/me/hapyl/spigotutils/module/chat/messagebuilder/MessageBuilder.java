package me.hapyl.spigotutils.module.chat.messagebuilder;

import com.google.common.collect.Lists;
import me.hapyl.spigotutils.module.annotate.NotFormatted;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * A {@link ComponentBuilder} wrapper with custom methods and <b>color codes</b> support.
 *
 * <br>
 * <h1>Big Note!</h1>
 * This builder default to {@link ComponentBuilder#append(String, ComponentBuilder.FormatRetention)} with a {@link ComponentBuilder.FormatRetention#NONE} retention, meaning using {@link #append(String)} will <b>not</b> carry any formatting nor events.
 */
public class MessageBuilder {

    public static final ComponentBuilder.FormatRetention DEFAULT_FORMAT_RETENTION = ComponentBuilder.FormatRetention.NONE;

    private final ComponentBuilder builder;

    public MessageBuilder() {
        this.builder = new ComponentBuilder();
    }

    /**
     * Appends a {@link String} and makes it the current the <code>target component</code>.
     *
     * @param string - String.
     */
    public MessageBuilder append(@Nonnull String string) {
        builder.append(Chat.color(string), DEFAULT_FORMAT_RETENTION);
        return this;
    }

    /**
     * Appends a {@link BaseComponent} and makes it the current <code>target component</code>.
     *
     * @param component - Component.
     */
    public MessageBuilder append(@Nonnull @NotFormatted BaseComponent component) {
        return append(component, DEFAULT_FORMAT_RETENTION);
    }

    /**
     * Appends a {@link BaseComponent} and makes it the current <code>target component</code>.
     *
     * @param component - Component.
     * @param retention - Formatting to retain.
     */
    public MessageBuilder append(@Nonnull @NotFormatted BaseComponent component, @Nonnull ComponentBuilder.FormatRetention retention) {
        builder.append(component, retention);
        return this;
    }

    /**
     * Adds a {@link ClickEvent} to the current <code>target component</code>.
     * <br>
     * When using {@link ClickEvent.Action#RUN_COMMAND}, remember to prefix with a <code>'/'</code>!
     *
     * @param action - Click action.
     * @param value  - Value.
     */
    public MessageBuilder event(@Nonnull ClickEvent.Action action, @Nonnull @NotFormatted String value) {
        builder.event(new ClickEvent(action, value));
        return this;
    }

    /**
     * Adds {@link HoverEvent} to the current <code>target component</code>.
     *
     * @param hoverEvent - Hover event.
     */
    public MessageBuilder event(@Nonnull HoverEvent hoverEvent) {
        builder.event(hoverEvent);
        return this;
    }

    /**
     * Adds {@link HoverEvent} to the current <code>target component</code>.
     *
     * @param hoverEventBuilder - Hover event builder.
     */
    public MessageBuilder event(@Nonnull HoverEventBuilder hoverEventBuilder) {
        return event(hoverEventBuilder.build());
    }

    /**
     * Adds {@link HoverEvent} to the current <code>target component</code>.
     *
     * @param action - Hover action.
     * @param values - Values to show.
     * @throws IllegalArgumentException if values are null or empty.
     */
    public MessageBuilder event(@Nonnull HoverEvent.Action action, @Nonnull String... values) {
        final Text[] text = new Text[getArrayLengthOrThrow(values)];

        for (int i = 0; i < values.length; i++) {
            text[i] = text(values[i]);
        }

        builder.event(new HoverEvent(action, text));
        return this;
    }

    /**
     * Adds a {@link HoverEvent} with a {@link HoverEvent.Action#SHOW_TEXT} action.
     *
     * @param values - Values to show; each line will be treated as a new line.
     * @throws IllegalArgumentException if values are null or empty.
     */
    public MessageBuilder eventShowText(@Nonnull String... values) {
        getArrayLengthOrThrow(values);

        final HoverEvent event = new HoverEvent(HoverEvent.Action.SHOW_TEXT, text(values[0]));
        event.addContent(text("\n"));

        for (int i = 1; i < values.length; i++) {
            if (i != 1) {
                event.addContent(nl());
            }

            event.addContent(text(values[i]));
        }

        builder.event(event);
        return this;
    }

    /**
     * Adds a {@link HoverEvent} with a {@link HoverEvent.Action#SHOW_TEXT} action.
     * <pre>
     * builder.eventShowTextBlock("""
     *                     This is the first line.
     *
     *                     &c;;This is a red text that will be wrapped after a given number of chars.
     *
     *
     *                     Another line!
     *                     """);
     * </pre>
     * will be formatted as such:
     * <pre>
     *  This is the first line.
     *
     *  &cThis is a red text that will be wrapped
     *  &cafter a given number of chars.
     *
     *
     *  Another line!
     * </pre>
     *
     * @param textBlock - Text block to add, each new line will be treated as a new line.
     */
    public MessageBuilder eventShowTextBlock(@Nonnull String textBlock) {
        return eventShowTextBlock(textBlock, ItemBuilder.DEFAULT_SMART_SPLIT_CHAR_LIMIT);
    }

    /**
     * Adds a {@link HoverEvent} with a {@link HoverEvent.Action#SHOW_TEXT} action with a custom character limit.
     *
     * @param textBlock - Text block.
     * @param limit     - Character limit.
     */
    public MessageBuilder eventShowTextBlock(@Nonnull String textBlock, int limit) {
        final String[] strings = textBlock.split("\n");
        final List<Text> textList = Lists.newArrayList();

        for (String string : strings) {
            if (string.isEmpty() || string.isBlank() || string.equalsIgnoreCase(" ")) { // paragraph
                textList.add(nl());
                continue;
            }

            final int prefixIndex = string.lastIndexOf(";;");
            String prefix = "";

            if (prefixIndex > 0) {
                prefix = string.substring(0, prefixIndex);
                string = string.substring(prefixIndex + 2);
            }

            ItemBuilder.splitString(prefix, string, limit).forEach(split -> textList.add(text(split)));
        }

        final HoverEvent event = new HoverEvent(HoverEvent.Action.SHOW_TEXT, textList.get(0));

        for (int i = 1; i < textList.size(); i++) {
            final Text text = textList.get(i);
            final boolean isNl = text.getValue().equals("\n");

            if (i != 1) {
                event.addContent(nl());

                if (isNl) {
                    continue;
                }
            }

            event.addContent(text);
        }

        builder.event(event);
        return this;
    }

    /**
     * Sets the font of the <code>target component</code>.
     *
     * @param font - Font.
     */
    public MessageBuilder font(@Nonnull String font) {
        builder.font(font);
        return this;
    }

    /**
     * Builds the message and sends it to the given player.
     *
     * @param player - Player.
     */
    public void send(@Nonnull Player player) {
        player.spigot().sendMessage(builder.create());
    }

    /**
     * Builds the message.
     * <br>
     * Use Player.spigot(){@link Player#spigot()#sendMessage()} to send it, <b>not</b> the {@link Player#sendMessage(String)}!
     *
     * @return the built message.
     */
    public BaseComponent[] build() {
        return builder.create();
    }

    /**
     * Gets the {@link ComponentBuilder} of this wrapper.
     *
     * @return the component builder of this wrapper.
     */
    @Nonnull
    public ComponentBuilder getBuilder() {
        return builder;
    }

    private int getArrayLengthOrThrow(Object[] array) throws IllegalArgumentException {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array cannot be null nor empty.");
        }

        return array.length;
    }

    @Nonnull
    public static Text text(@Nonnull String string) {
        return new Text(Chat.color(string));
    }

    private static Text nl() {
        return new Text("\n");
    }

}
