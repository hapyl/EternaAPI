package me.hapyl.eterna.module.inventory.gui;

import com.google.common.collect.Lists;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.annotate.Mutates;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.player.PlayerLib;
import me.hapyl.eterna.module.util.Described;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Allows filtering elements by an {@link Enum} constants.
 *
 * @param <E> - Elements to filter.
 * @param <S> - Filtering enum.
 */
public abstract class Filter<E, S extends Enum<S>> {

    private final Class<S> clazz;
    private final List<S> values;
    private S current;

    public Filter(@Nonnull Class<S> clazz) {
        this.clazz = clazz;
        this.values = Lists.newArrayList();
        this.current = null;

        initConstants();
    }

    public final int ordinal(@Nullable S s) {
        return s == null ? -1 : values.indexOf(s);
    }

    /**
     * Returns {@code true} if the given element should be kept while calling {@link #filter(List)}, {@code false} otherwise.
     *
     * @param e - Elene to check.
     * @param s - Filter.
     * @return {@code true} if the given element should be kept while calling {@link #filter(List)}, {@code false} otherwise.
     */
    public abstract boolean isKeep(@Nonnull E e, @Nonnull S s);

    /**
     * Filters the given {@link List} of elements based on {@link #isKeep(Object, Enum)}
     *
     * @param contents - {@link List} of elements to filter.
     * @return the same filtered {@link List}.
     */
    @Nonnull
    public List<E> filter(@Nonnull @Mutates("List#removeIf()") List<E> contents) {
        contents.removeIf(e -> current != null && !isKeep(e, current));
        return contents;
    }

    /**
     * Creates and sets the 'filter' {@link ItemStack} to the given {@link PlayerGUI} at the given {@code slot}.
     *
     * @param gui     - {@link PlayerGUI} to set the {@link ItemStack} to.
     * @param slot    - Slot to set the item to.
     * @param onClick - Action to perform with the next/previous element, which can be {@code null} if it's "None".
     */
    public void setFilterItem(@Nonnull PlayerGUI gui, int slot, @Nonnull BiConsumer<PlayerGUI, S> onClick) {
        final ItemBuilder item = ItemBuilder.of(Material.NAME_TAG, "Filter", "&8Filter by " + clazz.getSimpleName()).addLore();

        item.addLoreIf("&a➥ &nNone", current == null);
        item.addLoreIf(" &8None", current != null);

        for (S value : values) {
            final boolean currentValue = current == value;

            item.addLore((currentValue ? "&a➥ " : "&8 ") + value.toString());
            if (currentValue && value instanceof Described described) {
                item.addSmartLore(described.getDescription(), " &7&o");
            }
        }

        item.addLore();
        item.addLore("&7Left Click to cycle.");
        item.addLore("&7Right Click to cycle backwards.");

        gui.setItem(slot, item.asIcon());

        gui.setClick(slot, player -> {
            onClick.accept(gui, next());
            PlayerLib.playSound(player, Sound.BLOCK_LEVER_CLICK, 1);
        }, ClickType.LEFT, ClickType.SHIFT_LEFT);

        gui.setClick(slot, player -> {
            onClick.accept(gui, previous());
            PlayerLib.playSound(player, Sound.BLOCK_LEVER_CLICK, 1);
        }, ClickType.RIGHT, ClickType.SHIFT_RIGHT);
    }

    /**
     * Gets the current sort element, null if set to "None."
     *
     * @return the current sort element, null if set to "None."
     */
    @Nullable
    public S current() {
        return current;
    }

    /**
     * Switches to the next element and get it, or null if it is "None."
     *
     * @return the next element and get it, or null if it is "None."
     */
    @Nullable
    public S next() {
        if (current == null) {
            current = values.getFirst();
        }
        else {
            final int nextOrdinal = ordinal() + 1;

            if (nextOrdinal >= values.size()) {
                current = null;
            }
            else {
                current = values.get(nextOrdinal);
            }
        }

        return current;
    }

    /**
     * Switches to the previous element and get it, or null if it is "None."
     *
     * @return the previous element and get it, or null if it is "None."
     */
    @Nullable
    public S previous() {
        if (current == null) {
            current = values.getLast();
        }
        else {
            final int previousOrdinal = ordinal() - 1;

            if (previousOrdinal < 0) {
                current = null;
            }
            else {
                current = values.get(previousOrdinal);
            }
        }

        return current;
    }

    private int ordinal() {
        return ordinal(current);
    }

    private void initConstants() {
        values.clear();

        for (S s : clazz.getEnumConstants()) {
            try {
                final Field field = clazz.getField(s.name());
                if (field.isAnnotationPresent(ExcludeInFilter.class)) {
                    continue;
                }
            } catch (Exception e) {
                EternaLogger.exception(e);
            }

            values.add(s);
        }
    }

}
