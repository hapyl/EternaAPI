package me.hapyl.spigotutils.module.chat.messagebuilder;

import com.google.common.collect.Lists;
import me.hapyl.spigotutils.module.util.Builder;
import me.hapyl.spigotutils.module.util.SupportsColorFormatting;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * A {@link HoverEvent} {@link HoverEvent.Action#SHOW_TEXT} builder.
 */
public class HoverEventBuilder implements Builder<HoverEvent> {

    private final List<String> strings;

    public HoverEventBuilder() {
        this.strings = Lists.newArrayList();
    }

    /**
     * Appends a new line.
     *
     * @param string - String to append.
     */
    public HoverEventBuilder append(@Nonnull @SupportsColorFormatting(hex = false) String string) {
        this.strings.add(string);
        return this;
    }

    /**
     * Appends an empty line.
     */
    public HoverEventBuilder append() {
        return append("");
    }

    /**
     * Builds a new {@link HoverEvent}.
     *
     * @return a new hover event.
     */
    @Nonnull
    @Override
    public HoverEvent build() {
        final Text[] text = new Text[strings.size()];

        for (int i = 0; i < strings.size(); i++) {
            final boolean isLast = i == strings.size() - 1;
            final String string = strings.get(i);

            text[i] = MessageBuilder.text(string + (!isLast ? "\n" : ""));
        }

        return new HoverEvent(HoverEvent.Action.SHOW_TEXT, text);
    }

}
