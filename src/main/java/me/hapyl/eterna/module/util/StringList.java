package me.hapyl.eterna.module.util;

import me.hapyl.eterna.module.command.ArgumentList;
import me.hapyl.eterna.module.command.SimpleCommand;
import me.hapyl.eterna.module.registry.Registry;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * Represents a {@link List} of {@link String}.
 *
 * <p>
 * It's intended use if for the {@link SimpleCommand#tabComplete(CommandSender, ArgumentList)} completions.
 * </p>
 */
public class StringList extends ArrayList<String> {
    
    /**
     * Creates a new {@link StringList} instance.
     */
    public StringList() {
        super();
    }
    
    @ApiStatus.Internal
    private StringList(@NotNull Collection<? extends String> c) {
        super(c);
    }
    
    /**
     * Gets the {@link Collector} for {@link Stream} mappings.
     *
     * @return the collector for stream mappings.
     */
    @NotNull
    public static Collector<String, ?, StringList> collector() {
        return Collector.of(
                StringList::new,
                StringList::add,
                (left, right) -> {
                    left.addAll(right);
                    return left;
                }
        );
    }
    
    /**
     * A static factory method for creating a {@link StringList} from the given varargs.
     *
     * @param varargs - The string from which to create a string list.
     * @return a new string list.
     */
    @NotNull
    public static StringList of(@NotNull String... varargs) {
        return new StringList(Arrays.asList(varargs));
    }
    
    /**
     * A static factory method for creating a {@link StringList} from the given {@link Collection}.
     *
     * @param collection - The collection from which to create a string list.
     * @return a new string list.
     */
    @NotNull
    public static StringList of(@NotNull Collection<?> collection) {
        return collection.stream()
                         .map(String::valueOf)
                         .collect(collector());
    }
    
    /**
     * A static factory method for creating a {@link StringList} from the constant names of the given {@link Enum}.
     *
     * @param enumClass - The enum class.
     * @return a new string list.
     */
    @NotNull
    public static StringList ofEnumConstantNames(@NotNull Class<? extends Enum<?>> enumClass) {
        return new StringList(Enums.getValueNamesAsList(enumClass));
    }
    
    /**
     * A static factory method for creating a {@link StringList} from the constant lowercase names of the given {@link Enum}.
     *
     * @param enumClass - The enum class.
     * @return a new string list.
     */
    @NotNull
    public static StringList ofEnumConstantLowercaseNames(@NotNull Class<? extends Enum<?>> enumClass) {
        return new StringList(Enums.getValueLowercaseNamesAsList(enumClass));
    }
    
    /**
     * A static factory method for creating a {@link StringList} from the keys of the given {@link Registry}.
     *
     * @param registry - The registry.
     * @return a new string list.
     */
    @NotNull
    public static StringList ofRegistryKeys(@NotNull Registry<?> registry) {
        return new StringList(registry.keysAsString());
    }
    
    /**
     * A static factory method for creating a {@link StringList} from the names of online players.
     *
     * @return a new string list.
     */
    @NotNull
    public static StringList ofOnlinePlayers() {
        return Bukkit.getOnlinePlayers().stream()
                     .map(Player::getName)
                     .collect(StringList.collector());
    }
    
}