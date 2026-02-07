package me.hapyl.eterna.module.text.prefix;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a {@link Prefix} that prefixes the text.
 */
public interface Prefix {
    
    /**
     * Gets the prefix {@link Component}.
     *
     * @return the prefix component.
     */
    @NotNull
    Component getPrefix();
    
    /**
     * Gets the separator {@link Component}.
     *
     * <p>
     * The separator is used in between the text and sub-prefixes.
     * </p>
     *
     * @return the separator component.
     */
    @NotNull
    Component getSeparator();
    
    /**
     * Prefixes the given {@link Component} message and sends it to the given {@link CommandSender}.
     *
     * @param commandSender - The command sender to receive the message.
     * @param message       - The message to prefix and send.
     */
    void sendMessage(@NotNull CommandSender commandSender, @NotNull Component message);
    
    /**
     * Prefixes the given {@link Component} message and broadcasts it to the whole server.
     *
     * @param message - The message to prefix and broadcast.
     */
    void broadcastMessage(@NotNull Component message);
    
    /**
     * Creates a sub-{@link Prefix} from this {@link Prefix}.
     *
     * @param prefix    - The sub-prefix component.
     * @param separator - The sub-prefix component.
     * @return a new prefix with this prefix as a parent.
     */
    @NotNull
    default Prefix subPrefix(@NotNull Component prefix, @Nullable Component separator) {
        return new PrefixImpl(
                Component.empty()
                         .append(this.getPrefix())
                         .append(this.getSeparator())
                         .append(prefix),
                separator != null ? separator : this.getSeparator()
        );
    }
    
    /**
     * Creates a sub-{@link Prefix} from this {@link Prefix}.
     *
     * <p>
     * This method uses the same separator as for this prefix.
     * </p>
     *
     * @param prefix - The sub-prefix component.
     * @return a new prefix with this prefix as a parent.
     */
    @NotNull
    default Prefix subPrefix(@NotNull Component prefix) {
        return subPrefix(prefix, null);
    }
    
    /**
     * A static factory method for creating {@link Prefix}.
     *
     * @param prefix    - The prefix component.
     * @param separator - The separator component.
     * @return a new prefix.
     */
    @NotNull
    static Prefix create(@NotNull Component prefix, @NotNull Component separator) {
        return new PrefixImpl(prefix, separator);
    }
    
}
