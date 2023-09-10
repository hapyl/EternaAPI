package me.hapyl.spigotutils.module.command.completer;

import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

/**
 * Completer Handler for SimpleCommand.
 * <p>
 * It is used to provide visual feedback to the user when they are typing a command.
 * Look at wiki for more details.
 */
public class CompleterHandler {

    private final int index;
    private String valid;
    private String invalid;

    private Checker checker;

    private CompleterHandler(int index) {
        this.index = index - 1;
    }

    public static CompleterHandler of(int index) {
        return new CompleterHandler(index);
    }

    /**
     * String to display if the value is valid.
     *
     * @param string - String to display.
     */
    public CompleterHandler ifValidValue(String string) {
        valid = string;
        return this;
    }

    /**
     * String to display if the value is invalid.
     *
     * @param string - String to display.
     */
    public CompleterHandler ifInvalidValue(String string) {
        invalid = string;
        return this;
    }

    /**
     * Adds custom checker to display.
     *
     * @param checker - Checker.
     */
    public CompleterHandler custom(@Nonnull Checker checker) {
        this.checker = checker;
        return this;
    }

    public final void handle(Player player, String[] args, List<String> list) {
        if (args.length <= index) {
            return;
        }

        final String arg = args[index];

        // Custom checker
        if (checker != null) {
            final String check = checker instanceof Checker2 ? ((Checker2) checker).check(player, arg, args) : checker.check(player, arg);

            if (check != null) {
                list.add(format(check, arg));
            }
        }

        // Type check
        if (list.contains(arg.toLowerCase(Locale.ROOT))) {
            if (valid != null && !valid.isEmpty()) {
                list.add(format(valid, arg));
            }
        }
        else if (invalid != null && !invalid.isEmpty()) {
            list.add(format(invalid, arg));
        }
    }

    private String format(String str, String arg) {
        return Chat.format(str).replace("{}", arg);
    }

    public final int getIndex() {
        return index;
    }

}
