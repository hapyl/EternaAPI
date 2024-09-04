package me.hapyl.eterna.module.util;

import me.hapyl.eterna.module.reflect.npc.Human;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.function.Function;

/**
 * Represents a placeholder that can be used to format strings by replacing
 * placeholder patterns with values derived from objects of a specific type.
 *
 * @param <T> the type of object that the placeholder will format.
 */
public abstract class Placeholder<T> {

    private static final LinkedList<Placeholder<?>> PLACEHOLDERS;

    static {
        PLACEHOLDERS = new LinkedList<>();

        // Default placeholders
        of("player", Player.class, Player::getName);

        of("npc_name", Human.class, Human::getName);
        of("npc_location", Human.class, human -> BukkitUtils.locationToString(human.getLocation()));
    }

    private final String pattern;
    private final Class<T> clazz;

    Placeholder(@Nonnull String name, @Nonnull Class<T> clazz) {
        this.pattern = "{" + name + "}";
        this.clazz = clazz;
    }

    /**
     * Gets a {@link String} from the given {@link Object} to replace in the {@link Placeholder}.
     *
     * @param t - The {@link Object} to get the {@link String} from.
     */
    @Nonnull
    public abstract String format(@Nonnull T t);

    private String format0(String string, Object object) {
        if (!string.contains("{") || !string.contains("}")) {
            return string;
        }

        if (clazz.isInstance(object)) {
            string = string.replace(pattern, format(clazz.cast(object)));
        }

        return string;
    }

    /**
     * Formats the given {@link String} by replacing all registered placeholders with
     * corresponding values derived from the provided {@link Object}.
     *
     * @param string  - The {@link String} to format.
     * @param objects - The objects to use for replacement.
     * @return the formatted string.
     */
    @Nonnull
    public static String format(@Nonnull String string, @Nonnull Object... objects) {
        for (Placeholder<?> placeholder : PLACEHOLDERS) {
            for (Object object : objects) {
                string = placeholder.format0(string, object);
            }
        }

        return string;
    }

    /**
     * Registers a new {@link Placeholder} with a name, {@link Class} type, and formatting {@link Function}.
     *
     * @param name  - The name of the placeholder (without braces).
     * @param clazz - The {@link Class} type of the objects to be formatted.
     * @param fn    - The {@link Function} to format objects of the specified type.
     * @return the registered {@link Placeholder}.
     * @throws IllegalArgumentException if a placeholder with the same name is already registered.
     */
    @Nonnull
    public static <T> Placeholder<T> of(@Nonnull String name, @Nonnull Class<T> clazz, @Nonnull Function<T, String> fn) {
        final Placeholder<T> placeholder = new Placeholder<>(name, clazz) {
            @Nonnull
            @Override
            public String format(@Nonnull T t) {
                return fn.apply(t);
            }
        };

        if (PLACEHOLDERS.contains(placeholder)) {
            throw new IllegalArgumentException("Placeholder '%s' is already registered!".formatted(placeholder.pattern));
        }

        PLACEHOLDERS.add(placeholder);
        return placeholder;
    }

}
