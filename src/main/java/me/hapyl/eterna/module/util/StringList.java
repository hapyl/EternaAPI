package me.hapyl.eterna.module.util;

import me.hapyl.eterna.module.command.ArgumentList;
import me.hapyl.eterna.module.command.SimpleCommand;
import me.hapyl.eterna.module.registry.Registry;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * Represents an <b>immutable</b> {@link List} of {@link String} representation, which is mainly intended to be used for tab-completions in
 * {@link SimpleCommand#tabComplete(CommandSender, ArgumentList)}, but offers helpful factory method for creating lists.
 *
 * <p>
 * {@link StringList} does <b>not</b> permit {@code null} values.
 * </p>
 */
public final class StringList implements List<String> {
    
    private static final StringList EMPTY_STRING_LIST = new StringList(new String[0]);
    
    private final @NotNull String[] elements;
    
    private StringList(@NotNull String[] array /* Trusted copy */) {
        this.elements = array;
    }
    
    @Override
    public int size() {
        return elements.length;
    }
    
    @Override
    public boolean isEmpty() {
        return elements.length == 0;
    }
    
    @Override
    public boolean contains(@NotNull Object object) {
        for (String element : elements) {
            if (element.equals(object)) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public @NotNull Iterator<String> iterator() {
        return new StringListIterator(this, 0);
    }
    
    @Override
    public @NotNull Object @NotNull [] toArray() {
        return Arrays.copyOf(elements, elements.length);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    @Deprecated
    public @NotNull <T> T @NotNull [] toArray(@Nullable T @NotNull [] array) {
        if (array.getClass() != String[].class) {
            throw new IllegalArgumentException("Cannot convert to a non-String array!");
        }
        
        return (T[]) Arrays.copyOf(elements, elements.length, array.getClass());
    }
    
    @Override
    public boolean add(@NotNull String string) {
        throw uoe("add");
    }
    
    @Override
    public boolean remove(@NotNull Object object) {
        throw uoe("remove");
    }
    
    @Override
    public boolean containsAll(@NotNull Collection<?> collection) {
        for (Object object : collection) {
            if (!contains(object)) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    @Deprecated(forRemoval = true)
    public boolean addAll(@NotNull Collection<? extends String> collection) {
        throw uoe("addAll");
    }
    
    @Override
    @Deprecated(forRemoval = true)
    public boolean addAll(int index, @NotNull Collection<? extends String> collection) {
        throw uoe("addAll");
    }
    
    @Override
    @Deprecated(forRemoval = true)
    public boolean removeAll(@NotNull Collection<?> collection) {
        throw uoe("removeAll");
    }
    
    @Override
    @Deprecated(forRemoval = true)
    public boolean retainAll(@NotNull Collection<?> collection) {
        throw uoe("retainAll");
    }
    
    @Override
    @Deprecated(forRemoval = true)
    public void replaceAll(@NotNull UnaryOperator<String> operator) {
        throw uoe("replaceAll");
    }
    
    @Override
    @Deprecated(forRemoval = true)
    public void sort(Comparator<? super String> c) {
        throw uoe("sort");
    }
    
    @Override
    @Deprecated(forRemoval = true)
    public void clear() {
        throw uoe("clear");
    }
    
    @Override
    public String get(int index) {
        if (index < 0 || index >= elements.length) {
            throw new IndexOutOfBoundsException(index);
        }
        
        return elements[index];
    }
    
    @Override
    @Deprecated(forRemoval = true)
    public String set(int index, String element) {
        throw uoe("set");
    }
    
    @Override
    @Deprecated(forRemoval = true)
    public void add(int index, String element) {
        throw uoe("add");
    }
    
    @Override
    @Deprecated(forRemoval = true)
    public String remove(int index) {
        throw uoe("remove");
    }
    
    @Override
    public int indexOf(@NotNull Object object) {
        for (int i = 0; i < elements.length; i++) {
            if (elements[i].equals(object)) {
                return i;
            }
        }
        
        return -1;
    }
    
    @Override
    public int lastIndexOf(@NotNull Object object) {
        for (int i = elements.length - 1; i >= 0; i--) {
            if (elements[i].equals(object)) {
                return i;
            }
        }
        
        return -1;
    }
    
    @Override
    public @NotNull ListIterator<String> listIterator() {
        return new StringListIterator(this, 0);
    }
    
    @Override
    public @NotNull ListIterator<String> listIterator(int index) {
        return new StringListIterator(this, this.validateIndex(index));
    }
    
    @Override
    public @NotNull List<String> subList(int fromIndex, int toIndex) {
        if (fromIndex > toIndex) {
            throw new IndexOutOfBoundsException("fromIndex(%s) cannot be higher than toIndex(%s)".formatted(fromIndex, toIndex));
        }
        
        return new StringList(Arrays.copyOfRange(elements, validateIndex(fromIndex), validateIndex(toIndex)));
    }
    
    @Override
    public @NotNull Spliterator<String> spliterator() {
        return Arrays.spliterator(toStringArray());
    }
    
    @Override
    @Deprecated(forRemoval = true)
    public void addFirst(String element) {
        throw uoe("addFirst");
    }
    
    @Override
    @Deprecated(forRemoval = true)
    public void addLast(String element) {
        throw uoe("addLast");
    }
    
    @Override
    public String removeFirst() {
        throw uoe("removeFirst");
    }
    
    @Override
    public String removeLast() {
        throw uoe("removeLast");
    }
    
    @Override
    @Deprecated(forRemoval = true)
    public boolean removeIf(@NotNull Predicate<? super String> filter) {
        throw uoe("removeIf");
    }
    
    /**
     * Gets a copy of this list elements as a new array of {@link String}.
     *
     * @return a copy of this list elements as a new array of strings.
     */
    @NotNull
    public String[] toStringArray() {
        return Arrays.copyOf(elements, elements.length);
    }
    
    @Override
    public String toString() {
        return Arrays.toString(elements);
    }
    
    private int validateIndex(int index) {
        if (index < 0 || index >= elements.length) {
            throw new IndexOutOfBoundsException(index);
        }
        
        return index;
    }
    
    /**
     * Gets the {@link Collector} for {@link Stream} mappings.
     *
     * @return the collector for stream mappings.
     */
    public static @NotNull Collector<String, ?, StringList> collector() {
        return Collector.of(
                ArrayList::new,
                List::add,
                (left, right) -> {
                    left.addAll(right);
                    return left;
                },
                StringList::collectionToStringList
        );
    }
    
    /**
     * A static factory method for retrieving an empty {@link StringList} instance.
     *
     * @return the empty string list instance.
     */
    public static @NotNull StringList of() {
        return EMPTY_STRING_LIST;
    }
    
    /**
     * A static factory method for creating a {@link StringList} from the given varargs.
     *
     * <p>
     * If empty varargs are passed, an empty instance is returned.
     * </p>
     *
     * @param varargs - The string from which to create a string list.
     * @return a new string list.
     */
    
    public static @NotNull StringList of(@NotNull String... varargs) {
        return varargs.length == 0 ? EMPTY_STRING_LIST : new StringList(Arrays.copyOf(varargs, varargs.length));
    }
    
    /**
     * A static factory method for creating a {@link StringList} from the given {@link Collection}.
     *
     * @param collection - The collection from which to create a string list.
     * @return a new string list.
     */
    public static @NotNull StringList of(@NotNull Collection<?> collection) {
        return collection.stream().map(String::valueOf).collect(collector());
    }
    
    /**
     * A static factory method for creating a {@link StringList} from the constant names of the given {@link Enum}.
     *
     * @param enumClass - The enum class.
     * @return a new string list.
     */
    public static @NotNull StringList ofEnumConstantNames(@NotNull Class<? extends Enum<?>> enumClass) {
        return collectionToStringList(Enums.getValueNamesAsList(enumClass));
    }
    
    /**
     * A static factory method for creating a {@link StringList} from the constant lowercase names of the given {@link Enum}.
     *
     * @param enumClass - The enum class.
     * @return a new string list.
     */
    public static @NotNull StringList ofEnumConstantLowercaseNames(@NotNull Class<? extends Enum<?>> enumClass) {
        return collectionToStringList(Enums.getValueLowercaseNamesAsList(enumClass));
    }
    
    /**
     * A static factory method for creating a {@link StringList} from the keys of the given {@link Registry}.
     *
     * @param registry - The registry.
     * @return a new string list.
     */
    
    public static @NotNull StringList ofRegistryKeys(@NotNull Registry<?> registry) {
        return collectionToStringList(registry.keysAsString());
    }
    
    /**
     * A static factory method for creating a {@link StringList} from the names of online players.
     *
     * @return a new string list.
     */
    public static @NotNull StringList ofOnlinePlayers() {
        return Bukkit.getOnlinePlayers().stream()
                     .map(Player::getName)
                     .collect(StringList.collector());
    }
    
    private static @NotNull UnsupportedOperationException uoe(@NotNull String operation) {
        throw new UnsupportedOperationException(operation);
    }
    
    private static @NotNull StringList collectionToStringList(@NotNull Collection<? extends String> collection) {
        return new StringList(collection.toArray(String[]::new));
    }
    
    @ApiStatus.Internal
    static final class StringListIterator implements ListIterator<String> {
        
        private final StringList stringList;
        private int index;
        
        private StringListIterator(@NotNull StringList stringList, int index) {
            this.stringList = stringList;
            this.index = index;
        }
        
        @Override
        public boolean hasNext() {
            return index < stringList.size();
        }
        
        @Override
        public String next() {
            return stringList.get(index++);
        }
        
        @Override
        public boolean hasPrevious() {
            return index > 0;
        }
        
        @Override
        public String previous() {
            if (index - 1 < 0) {
                throw new NoSuchElementException();
            }
            
            return stringList.get(index--);
        }
        
        @Override
        public int nextIndex() {
            return Math.min(index + 1, stringList.size());
        }
        
        @Override
        public int previousIndex() {
            return Math.max(index - 1, -1);
        }
        
        @Override
        public void remove() {
            throw uoe("remove");
        }
        
        @Override
        public void set(@NotNull String s) {
            throw uoe("set");
        }
        
        @Override
        public void add(@NotNull String s) {
            throw uoe("add");
        }
    }
    
}