package me.hapyl.eterna.module.npc;

import com.google.common.collect.Lists;
import me.hapyl.eterna.module.annotate.ForceLowercase;
import me.hapyl.eterna.module.annotate.Mutates;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

/**
 * Represents a placeholder for {@link Npc} message.
 *
 * <p>Registered placeholder will be replaced via {@link Npc#sendMessage(Player, Component)}, as example:</p>
 * <pre>{@code
 * Player player = player("hapyl");
 * Npc npc = npc("My Npc");
 *
 * npc.sendMessage(
 *         player,
 *         Component.text()
 *                  .append(Component.text("Hello, {player}, "))
 *                  .append(Component.text("my name is {npc_name}!"))
 *                  .build()
 * );
 *
 * // Will look like this: "Hello, hapyl, my name is My Npc!"
 * }</pre>
 * <p></p>
 */
public abstract class NpcPlaceholder {
    private static final LinkedList<NpcPlaceholder> placeholders;
    
    static {
        placeholders = Lists.newLinkedList();
        
        // Register default placeholders
        register("player", (npc, player) -> player.name());
        register("npc_name", Npc::getName);
    }
    
    private final Pattern pattern;
    
    NpcPlaceholder(@NotNull String key) {
        this.pattern = Pattern.compile("\\{%s\\}".formatted(key));
    }
    
    /**
     * Resolves placeholders using the given {@link Npc} and {@link Player} and returns the resulting {@link Component}.
     *
     * @param npc    - The npc used as a placeholder context.
     * @param player - The npc used as a placeholder context.
     */
    @NotNull
    public abstract Component placehold(@NotNull Npc npc, @NotNull Player player);
    
    /**
     * Gets the {@link String} representation of this placeholder, this being the placeholder key formatted as {@code {key}}.
     *
     * @return the string representation of this placeholder.
     */
    @Override
    public String toString() {
        return pattern.pattern();
    }
    
    /**
     * Compares the given object to this placeholder.
     *
     * @param object - The object to compare.
     * @return {@code true} if the placeholder key is the same, {@code false} otherwise.
     */
    @Override
    public final boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        
        final NpcPlaceholder that = (NpcPlaceholder) object;
        return Objects.equals(this.pattern, that.pattern);
    }
    
    /**
     * Gets the hash code of this placeholder.
     *
     * @return the hash code of this placeholder.
     */
    @Override
    public final int hashCode() {
        return Objects.hashCode(this.pattern);
    }
    
    /**
     * A static factory method for registering a {@link NpcPlaceholder}.
     *
     * @param key - The key of the placeholder.
     * @param fn  - The placeholder function.
     * @throws IllegalStateException if the placeholder with that key is already registered.
     */
    public static void register(@NotNull @ForceLowercase String key, @NotNull BiFunction<Npc, Player, Component> fn) {
        final NpcPlaceholder placeholder = new NpcPlaceholder(key) {
            @NotNull
            @Override
            public Component placehold(@NotNull Npc npc, @NotNull Player player) {
                return fn.apply(npc, player);
            }
        };
        
        // Make sure that this placeholder doesn't exist already
        if (placeholders.contains(placeholder)) {
            throw new IllegalStateException("Placeholder '%s' already registered!".formatted(placeholder.toString()));
        }
        
        // Don't forget to actually register the placeholder
        placeholders.add(placeholder);
    }
    
    @ApiStatus.Internal
    @NotNull
    static Component doPlacehold(@NotNull @Mutates Component original, @NotNull Npc npc, @NotNull Player player) {
        for (NpcPlaceholder placeholder : placeholders) {
            original = original.replaceText(
                    builder -> builder.match(placeholder.pattern).replacement(placeholder.placehold(npc, player))
            );
        }
        
        return original;
    }
}
