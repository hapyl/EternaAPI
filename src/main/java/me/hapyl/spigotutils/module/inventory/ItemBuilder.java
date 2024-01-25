package me.hapyl.spigotutils.module.inventory;

import com.google.common.collect.*;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.hapyl.spigotutils.EternaLogger;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.annotate.ForceLowercase;
import me.hapyl.spigotutils.module.annotate.Range;
import me.hapyl.spigotutils.module.annotate.Super;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.math.Numbers;
import me.hapyl.spigotutils.module.nbt.NBT;
import me.hapyl.spigotutils.module.nbt.NBTNative;
import me.hapyl.spigotutils.module.nbt.NBTType;
import me.hapyl.spigotutils.module.util.Nulls;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.map.MapView;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.PatternSyntaxException;

/**
 * Build ItemStack easier. Add names, lore, smart lore, enchants and even click events!
 */
public class ItemBuilder implements Cloneable {

    public static final int DEFAULT_SMART_SPLIT_CHAR_LIMIT = 35;
    public static final String NEW_LINE_SEPARATOR = "__";
    public static final char MANUAL_SPLIT_CHAR = '_';
    public static final String DEFAULT_COLOR = "&7";

    private static final String PLUGIN_PATH = "ItemBuilderId";
    private static final String URL_TEXTURE_LINK = "https://textures.minecraft.net/texture/";

    protected static Map<String, ItemFunctionList> registeredFunctions = Maps.newHashMap();

    private final String id;

    protected ItemStack item;
    private String nativeNbt;
    private ItemFunctionList functions;

    /**
     * Creates a new ItemBuilder from material.
     *
     * @param material - Material of the builder.
     */
    public ItemBuilder(@Nonnull Material material) {
        this(new ItemStack(material));
    }

    /**
     * Create a new ItemBuilder from ItemStack.
     *
     * @param stack - ItemStack of the builder. Data is cloned.
     */
    public ItemBuilder(@Nonnull ItemStack stack) {
        this(stack.clone(), null);
    }

    /**
     * Create a new ItemBuilder with the given Material and ID for events.
     *
     * @param material - Material of the builder.
     * @param id       - ID of the builder.
     */
    public ItemBuilder(@Nonnull Material material, @Nullable @ForceLowercase String id) {
        this(new ItemStack(material), id);
    }

    /**
     * Create a new ItemBuilder from ItemStack with ID for events.
     *
     * @param stack - ItemStack of the builder. Data is cloned.
     * @param id    - ID of the builder.
     */
    @Super
    public ItemBuilder(@Nonnull ItemStack stack, @Nullable @ForceLowercase String id) {
        this.item = stack;
        this.id = id;
        this.nativeNbt = "";
    }

    /**
     * Sets a new event handler.
     *
     * @param handler - New event handler.
     * @throws IllegalStateException if item has no Id.
     */
    public final ItemBuilder setEventHandler(@Nonnull ItemEventHandler handler) {
        getFunctions().handler = handler;
        return this;
    }

    /**
     * Gets the {@link ItemFunctionList} for this builder.
     *
     * @return the {@link ItemFunctionList} list.
     * @throws IllegalArgumentException if this item has no Id.
     */
    @Nonnull
    public ItemFunctionList getFunctions() {
        if (functions == null) {
            if (id == null) {
                throw new IllegalStateException("Id is not set, cannot get the function list!");
            }

            functions = new ItemFunctionList(id);
        }

        return functions;
    }

    /**
     * Gets item's NBT in string format, same as you would use it in give command.
     *
     * @return item's NBT or empty string if none.
     */
    @Nonnull
    public String getNbt() {
        return nativeNbt;
    }

    /**
     * Set item's raw NBT.
     * This method uses native minecraft nbt stored in the '<i>tag:{<b>HERE</b>}</i>' compound.
     *
     * @param nbt - New NBT or null to remove.
     */
    public ItemBuilder setNbt(@Nullable String nbt) {
        this.nativeNbt = nbt == null ? "" : nbt;
        return this;
    }

    /**
     * Sets item's NBT.
     *
     * @param path  - Path to set.
     * @param type  - Type of nbt.
     * @param value - Value to set.
     */
    public <A, T> ItemBuilder setNbt(@Nonnull String path, @Nonnull NBTType<A, T> type, @Nonnull T value) {
        return modifyMeta(meta -> NBT.setValue(meta, path, type, value));
    }

    /**
     * Sets the builder's ItemMeta.
     *
     * @param meta - New ItemMeta.
     */
    public ItemBuilder setItemMeta(@Nullable ItemMeta meta) {
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Performs a predicate and applying action if predicate returned true, does nothing otherwise.
     *
     * @param predicate - Predicate.
     * @param action    - Action to perform if predicate returned true.
     */
    public ItemBuilder predicate(boolean predicate, @Nonnull Consumer<ItemBuilder> action) {
        if (predicate) {
            action.accept(this);
        }
        return this;
    }

    /**
     * Creates a copy of this builder.
     *
     * @return a copy of this builder.
     */
    @Override
    @Nonnull
    public ItemBuilder clone() {
        try {
            return (ItemBuilder) super.clone();
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    /**
     * Sets if click events from inventory are allowed.
     *
     * @param allowInventoryClick - New value.
     * @see #addFunction(ItemFunction)
     * @deprecated allow inventory clicks is not per-functions
     */
    @Deprecated
    public ItemBuilder setAllowInventoryClick(boolean allowInventoryClick) {
        final ItemFunctionList functions = getFunctions();

        functions.first().setAllowInventoryClick(allowInventoryClick);
        return this;
    }

    /**
     * Sets the map view for the builder.
     * Material will be forced to {@link Material#FILLED_MAP}.
     *
     * @param view - New map view.
     */
    public ItemBuilder setMapView(MapView view) {
        return modifyMeta(MapMeta.class, Material.FILLED_MAP, meta -> meta.setMapView(view));
    }

    /**
     * Sets the book name.
     * Material will be forced to {@link Material#WRITTEN_BOOK}.
     *
     * @param name - New book name.
     */
    public ItemBuilder setBookName(@Nullable String name) {
        return modifyMeta(BookMeta.class, Material.WRITTEN_BOOK, meta -> meta.setTitle(colorize(name)));
    }

    /**
     * Sets the book author.
     * Material will be forced to {@link Material#WRITTEN_BOOK}.
     *
     * @param author - New book author.
     */
    public ItemBuilder setBookAuthor(String author) {
        return modifyMeta(BookMeta.class, Material.WRITTEN_BOOK, meta -> meta.setAuthor(colorize(author)));
    }

    /**
     * Sets the book title.
     * Material will be forced to {@link Material#WRITTEN_BOOK}.
     *
     * @param title - New book title.
     */
    public ItemBuilder setBookTitle(@Nullable String title) {
        return modifyMeta(BookMeta.class, Material.WRITTEN_BOOK, meta -> meta.setTitle(colorize(title)));
    }

    /**
     * Sets the book pages.
     * Material will be forced to {@link Material#WRITTEN_BOOK}.
     *
     * @param pages - New book pages.
     */
    public ItemBuilder setBookPages(@Nonnull List<String> pages) {
        return modifyMeta(BookMeta.class, Material.WRITTEN_BOOK, meta -> meta.setPages(pages));
    }

    /**
     * Sets the book pages.
     * Material will be forced to {@link Material#WRITTEN_BOOK}.
     *
     * @param pages - New book pages.
     */
    public ItemBuilder setBookPages(@Nonnull String... pages) {
        return setBookPages(Arrays.asList(pages));
    }

    /**
     * Sets the book page contents at the given page.
     * Material will be forced to {@link Material#WRITTEN_BOOK}.
     *
     * @param page     - Page to set.
     * @param contents - Page contents.
     */
    public ItemBuilder setBookPage(int page, @Nonnull String contents) {
        return modifyMeta(BookMeta.class, Material.WRITTEN_BOOK, meta -> meta.setPage(page, contents));
    }

    /**
     * Sets the cooldown for click events.
     *
     * @param ticks - Cooldown in ticks.
     * @see #addFunction(ItemFunction)
     * @deprecated Cooldowns are now per-function.
     */
    @Deprecated
    public ItemBuilder withCooldown(int ticks) {
        return withCooldown(ticks, null);
    }

    /**
     * Sets the cooldown for click events with predicate.
     *
     * @param ticks     - Cooldown in ticks.
     * @param predicate - Predicate of the cooldown.
     * @see #addFunction(ItemFunction)
     * @deprecated Cooldowns are now per-function.
     */
    @Deprecated
    public ItemBuilder withCooldown(int ticks, @Nullable Predicate<Player> predicate) {
        return withCooldown(ticks, predicate, "&cCannot use that!");
    }

    /**
     * Sets the cooldown for the <b>first</b> {@link ItemFunction}.
     *
     * @param ticks        - Cooldown in ticks.
     * @param predicate    - Predicate of the cooldown.
     * @param errorMessage - Error message if predicate fails.
     * @see #addFunction(ItemFunction)
     * @deprecated Cooldowns are now per-function.
     */
    @Deprecated
    public ItemBuilder withCooldown(int ticks, @Nullable Predicate<Player> predicate, @Nonnull String errorMessage) {
        final ItemFunction function = getFunctions().first();

        function.setCd(ticks);
        function.setPredicate(predicate);
        function.setErrorMessage(errorMessage);
        return this;
    }

    /**
     * Removes all click events.
     */
    public ItemBuilder removeClickEvent() {
        final ItemFunctionList functions = getFunctions();

        functions.handler = null;
        functions.clearFunctions();

        return this;
    }

    /**
     * Adds a click event.
     *
     * @param runnable - Runnable.
     * @param actions  - Allowed click types.
     * @throws IndexOutOfBoundsException if there are no actions.
     */
    public ItemBuilder addClickEvent(@Nonnull Consumer<Player> runnable, @Range(min = 1, throwsError = true) @Nonnull Action... actions) {
        if (actions.length < 1) {
            throw new IndexOutOfBoundsException("This requires at least one action.");
        }

        final ItemFunctionList functions = getFunctions();
        final ItemFunction function = new ItemFunction(id, runnable);

        for (Action action : actions) {
            function.accept(action);
        }

        functions.addFunction(function);

        return this;
    }

    /**
     * Adds a click event for RIGHT clicks at blocks and air.
     *
     * @param consumer - Click action.
     */
    public ItemBuilder addClickEvent(@Nonnull Consumer<Player> consumer) {
        return addClickEvent(consumer, Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR);
    }

    /**
     * Adds an {@link ItemFunction}.
     *
     * @param function - function.
     */
    public ItemBuilder addFunction(@Nonnull ItemFunction function) {
        getFunctions().addFunction(function);
        return this;
    }

    /**
     * Adds an {@link ItemFunction}.
     *
     * @param function - functions.
     */
    public ItemBuilder addFunction(@Nonnull Function<String, ItemFunction> function) {
        final ItemFunctionList functions = getFunctions();
        final ItemFunction fn = function.apply(id);

        functions.addFunction(fn);
        return this;
    }

    /**
     * Adds and returns a new {@link ItemFunction}.
     *
     * @param consumer - Action.
     */
    @Nonnull
    public ItemFunction addFunction(@Nonnull Consumer<Player> consumer) {
        final ItemFunctionList functions = getFunctions();
        final ItemFunction function = new ItemFunction(id, consumer);

        functions.addFunction(function);
        return function;
    }

    /**
     * Sets persistent data to the given path.
     *
     * @param path  - Path to the data.
     * @param value - Value to set.
     */
    public ItemBuilder setPersistentData(@Nonnull String path, @Nonnull Object value) {
        if (value instanceof String) {
            setPersistentData(path, PersistentDataType.STRING, (String) value);
        }
        if (value instanceof Byte) {
            setPersistentData(path, PersistentDataType.BYTE, (byte) value);
        }
        if (value instanceof Short) {
            setPersistentData(path, PersistentDataType.SHORT, (short) value);
        }
        if (value instanceof Integer) {
            setPersistentData(path, PersistentDataType.INTEGER, (int) value);
        }
        if (value instanceof Long) {
            setPersistentData(path, PersistentDataType.LONG, (long) value);
        }
        if (value instanceof Float) {
            setPersistentData(path, PersistentDataType.FLOAT, (float) value);
        }
        if (value instanceof Double) {
            setPersistentData(path, PersistentDataType.DOUBLE, (double) value);
        }
        return this;
    }

    /**
     * Gets the persistent data from the item; or null if not found.
     *
     * @param path  - Path to the data.
     * @param value - PersistentDataType to get.
     * @param <T>   - Type of the data.
     * @return the persistent data from the item; or null if not found.
     */
    public <T> T getNbt(@Nonnull String path, @Nonnull PersistentDataType<T, T> value) {
        return getPersistentData(path, value);
    }

    /**
     * Sets smart lore.
     * This will remove the existing lore.
     * <p>
     * Smart lore wraps automatically at the best places within the chat limit.
     *
     * @param lore - Lore to set.
     */
    public ItemBuilder setSmartLore(@Nonnull String lore) {
        return setSmartLore(lore, DEFAULT_SMART_SPLIT_CHAR_LIMIT);
    }

    /**
     * Sets smart lore with custom character limit.
     * <p>
     * Smart lore wraps automatically at the best places within the chat limit.
     *
     * @param lore  - Lore to set.
     * @param limit - Char limit.
     */
    public ItemBuilder setSmartLore(@Nonnull String lore, final int limit) {
        return setSmartLore(lore, "&7", limit);
    }

    /**
     * Sets smart lore with custom prefix color.
     *
     * @param lore   - Lore to set.
     * @param prefix - Prefix to set.
     */
    public ItemBuilder setSmartLore(@Nonnull String lore, @Nonnull String prefix) {
        return setSmartLore(lore, prefix, DEFAULT_SMART_SPLIT_CHAR_LIMIT);
    }

    /**
     * Adds smart lore to existing lore with custom char limit.
     *
     * @param lore  - Lore to add.
     * @param limit - Char limit.
     */
    public ItemBuilder addSmartLore(@Nonnull String lore, final int limit) {
        return addSmartLore(lore, null, limit);
    }

    /**
     * Adds smart lore to existing lore with custom prefix.
     *
     * @param lore       - Lore to add.
     * @param prefixText - Prefix after every split.
     */
    public ItemBuilder addSmartLore(@Nonnull String lore, @Nonnull String prefixText) {
        return addSmartLore(lore, prefixText, DEFAULT_SMART_SPLIT_CHAR_LIMIT);
    }

    /**
     * Adds smart lore to existing lore.
     *
     * @param lore - Lore to add.
     */
    public ItemBuilder addSmartLore(@Nonnull String lore) {
        return addSmartLore(lore, DEFAULT_SMART_SPLIT_CHAR_LIMIT);
    }

    /**
     * Sets smart lore with custom prefix color.
     *
     * @param lore      - Lore to set.
     * @param prefix    - Prefix to set.
     * @param wrapAfter - Wrap after this number of chars.
     */
    public ItemBuilder setSmartLore(@Nonnull String lore, @Nullable String prefix, int wrapAfter) {
        return modifyMeta(meta -> meta.setLore(splitString(prefix, lore, wrapAfter)));
    }

    /**
     * Adds smart lore to existing lore with custom prefix color.
     *
     * @param lore      - Lore to add.
     * @param prefix    - Prefix to set.
     * @param wrapAfter - Wrap after this number of chars.
     */
    public ItemBuilder addSmartLore(@Nonnull String lore, String prefix, int wrapAfter) {
        return modifyMeta(meta -> {
            final List<String> metaLore = getLore();

            metaLore.addAll(splitString(prefix, lore, wrapAfter));
            meta.setLore(metaLore);
        });
    }

    /**
     * Sets the lore at the given line.
     *
     * @param line - Index of line.
     * @param lore - Lore to set.
     */
    public ItemBuilder setLore(int line, @Nonnull String lore) {
        return modifyMeta(meta -> {
            final List<String> strings = getLore();

            if (line < strings.size()) {
                strings.set(line, colorize(lore));
                meta.setLore(strings);
            }
        });
    }

    /**
     * Sets the item lore.
     * You can use {@link #NEW_LINE_SEPARATOR} to insert a new line,
     * or use {@link #addTextBlockLore(String, Object...)}.
     *
     * @param lore        - Lore to set.
     * @param prefixColor - Color of the text after split.
     */
    public ItemBuilder addLore(@Nonnull String lore, @Nonnull ChatColor prefixColor) {
        return modifyMeta(meta -> {
            final List<String> metaLore = getLore();

            for (String value : lore.split(NEW_LINE_SEPARATOR)) {
                metaLore.add(prefixColor + colorize(value));
            }

            meta.setLore(metaLore);
        });
    }

    /**
     * Sets the item lore.
     * You can use {@link #NEW_LINE_SEPARATOR} to insert a new line,
     * or use {@link #addTextBlockLore(String, Object...)}.
     *
     * @param lore - Lore to set.
     */
    public ItemBuilder addLore(@Nonnull String lore) {
        return addLore(lore, ChatColor.GRAY);
    }

    /**
     * Gets the lore of this item; or empty list if none.
     *
     * @return the lore of this item; or empty list if none.
     */
    @Nonnull
    public List<String> getLore() {
        final ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return Lists.newArrayList();
        }

        final List<String> lore = meta.getLore();

        return lore == null ? Lists.newArrayList() : lore;
    }

    /**
     * Sets lore from a list of strings.
     *
     * @param lore - Lore to set.
     */
    public ItemBuilder setLore(@Nonnull List<String> lore) {
        return modifyMeta(meta -> meta.setLore(lore));
    }

    /**
     * Sets the item lore.
     * {@link #NEW_LINE_SEPARATOR} can be used as a new line separator.
     *
     * @param lore - Lore to set.
     */
    public ItemBuilder setLore(@Nonnull String lore) {
        return setLore(lore, NEW_LINE_SEPARATOR);
    }

    /**
     * Adds lore to the item.
     *
     * @param lore   - Lore to add.
     * @param format - Format for the lore.
     */
    public ItemBuilder addLore(@Nonnull String lore, @Nullable Object... format) {
        return addLore(format(lore, format));
    }

    /**
     * Adds lore to the item if the condition is met.
     *
     * @param lore      - Lore to add.
     * @param condition - Condition.
     */
    public ItemBuilder addLoreIf(@Nonnull String lore, boolean condition) {
        return addLoreIf(lore, condition, "");
    }

    /**
     * Adds lore to the item if the condition is met.
     *
     * @param lore         - Lore to add.
     * @param condition    - Condition.
     * @param replacements - Replacements for the lore.
     */
    public ItemBuilder addLoreIf(@Nonnull String lore, boolean condition, @Nullable Object... replacements) {
        if (condition) {
            addLore(lore, replacements);
        }
        return this;
    }

    /**
     * Adds an empty lore line.
     */
    public ItemBuilder addLore() {
        return addLore("");
    }

    /**
     * Sets the item lore with custom new line separator.
     * The default line separator is {@link #NEW_LINE_SEPARATOR}.
     *
     * @param lore      - Lore to set.
     * @param separator - Separator to split the lines.
     */
    public ItemBuilder setLore(@Nonnull String lore, @Nonnull String separator) {
        try {
            return modifyMeta(meta -> meta.setLore(Arrays.asList(colorize(lore).split(separator))));
        } catch (PatternSyntaxException ex) {
            Bukkit.getConsoleSender().sendMessage(format("&4[ERROR] &cChar &e%s &cused as separator for lore!", separator));
        }

        return this;
    }

    /**
     * Adds text block as smart lore to the item.
     * Each line will be treated as a paragraph.
     * <p>
     * {@link #NEW_LINE_SEPARATOR} can be used as a custom separator.
     * <p>
     * Prefix can be added by using <code>;;</code> before the actual string, ex:
     * <pre>
     *     &c;;Hello World__Goodbye World
     * </pre>
     * will be formatted as:
     * <pre>
     *     &cHello World
     *     &cGoodbye World
     * </pre>
     *
     * @param textBlock - Text block.
     */
    public ItemBuilder addTextBlockLore(@Nonnull String textBlock, @Nullable Object... format) {
        return addTextBlockLore(textBlock, DEFAULT_SMART_SPLIT_CHAR_LIMIT, format);
    }

    /**
     * Adds text block as smart lore to the item.
     * Each line will be treated as a paragraph.
     * <p>
     * {@link #NEW_LINE_SEPARATOR} can be used as a custom separator.
     * <p>
     * Prefix can be added by using <code>;;</code> before the actual string, ex:
     * <pre>
     *     &c;;Hello World__Goodbye World
     * </pre>
     * will be formatted as:
     * <pre>
     *     &cHello World
     *     &cGoodbye World
     * </pre>
     *
     * @param textBlock - Text block.
     * @param charLimit - Char wrap limit.
     */
    public ItemBuilder addTextBlockLore(@Nonnull String textBlock, int charLimit, @Nullable Object... format) {
        return addTextBlockLore(textBlock, "&7", charLimit, format);
    }

    public ItemBuilder addTextBlockLore(@Nonnull String textBlock, @Nonnull String linePrefix, int charLimit, @Nullable Object... format) {
        return modifyMeta(meta -> {
            final String[] strings = textBlock.formatted(format).split("\n");
            final List<String> lore = getLore();

            for (String string : strings) {
                if (string.equalsIgnoreCase("")) { // paragraph
                    lore.add("");
                }

                final int prefixIndex = string.lastIndexOf(";;");
                String prefix = linePrefix;

                if (prefixIndex > 0) {
                    prefix = string.substring(0, prefixIndex);
                    string = string.substring(prefixIndex + 2);
                }

                lore.addAll(splitString(prefix, string, charLimit));
            }

            meta.setLore(lore);
        });
    }

    /**
     * Removes all the lore from the item.
     */
    public ItemBuilder removeLore() {
        return modifyMeta(meta -> meta.setLore(null));
    }

    /**
     * Removes lore at the given line.
     *
     * @param line - Line to remove.
     */
    public ItemBuilder removeLoreLine(int line) {
        return modifyMeta(meta -> {
            if (meta.getLore() == null) {
                return;
            }

            final List<String> lore = getLore();

            if (line < lore.size()) {
                lore.remove(line);
                meta.setLore(lore);
            }
        });
    }

    /**
     * Applies default setting to the item, such as:
     * <ul>
     *     <li>Makes item unbreakable.</li>
     *     <li>Hides enchantments.</li>
     * </ul>
     */
    public ItemBuilder applyDefaultSettings() {
        return applyDefaultSettings(true);
    }

    /**
     * Applies default setting to the item, such as:
     * <ul>
     *     <li>Makes item unbreakable.</li>
     *     <li>Hides enchantments.</li>
     *     <li>Applies curse of binding if <code>applyCurse</code> is true.</li>
     * </ul>
     *
     * @param applyCurse - If true, applies curse of binding.
     */
    public ItemBuilder applyDefaultSettings(boolean applyCurse) {
        return modifyMeta(meta -> {
            if (applyCurse) {
                meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
            }

            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.values());
        });
    }

    /**
     * Sets the name of the item.
     *
     * @param name   - Name to set.
     * @param format - Format for the name.
     */
    public ItemBuilder setName(@Nonnull String name, @Nullable Object... format) {
        return setName(format(name, format));
    }

    /**
     * Adds enchant to the item.
     *
     * @param enchant - Enchantment to add.
     * @param lvl     - Level of the enchantment.
     */
    public ItemBuilder addEnchant(@Nonnull Enchant enchant, int lvl) {
        return addEnchant(enchant.getAsBukkit(), lvl);
    }

    /**
     * Adds enchant to the item.
     *
     * @param enchantment - Enchantment to add.
     * @param lvl         - Level of the enchantment.
     */
    public ItemBuilder addEnchant(@Nonnull Enchantment enchantment, int lvl) {
        return modifyMeta(meta -> meta.addEnchant(enchantment, lvl, true));
    }

    /**
     * Makes the item unbreakable.
     */
    public ItemBuilder setUnbreakable() {
        return modifyMeta(meta -> meta.setUnbreakable(true));
    }

    /**
     * Sets potion meta of the item.
     * Material must be set to one of those:
     * <ul>
     *     <li>POTION</li>
     *     <li>SPLASH_POTION</li>
     *     <li>LINGERING_POTION</li>
     *     <li>TIPPED_ARROW</li>
     * </ul>
     *
     * @param type     - Potion type.
     * @param lvl      - Potion level.
     * @param duration - Potion duration.
     * @param color    - Potion color.
     */
    public ItemBuilder setPotionMeta(@Nonnull PotionEffectType type, int lvl, int duration, @Nullable Color color) {
        return modifyMeta(PotionMeta.class, meta -> {
            meta.addCustomEffect(new PotionEffect(type, duration, lvl), true);
            meta.setColor(color);
        });
    }

    /**
     * Sets potion color.
     * Material must be set to one of those:
     * <ul>
     *     <li>POTION</li>
     *     <li>SPLASH_POTION</li>
     *     <li>LINGERING_POTION</li>
     *     <li>TIPPED_ARROW</li>
     * </ul>
     *
     * @param color - Color to set.
     */
    public ItemBuilder setPotionColor(@Nullable Color color) {
        return modifyMeta(PotionMeta.class, meta -> meta.setColor(color));
    }

    /**
     * Sets leather armor color.
     * Material must be set to one of those:
     * <ul>
     *     <li>LEATHER_HELMET</li>
     *     <li>LEATHER_CHESTPLATE</li>
     *     <li>LEATHER_LEGGINGS</li>
     *     <li>LEATHER_BOOTS</li>
     * </ul>
     *
     * @param color - Color to set.
     */
    public ItemBuilder setLeatherArmorColor(@Nullable Color color) {
        return modifyMeta(LeatherArmorMeta.class, meta -> meta.setColor(color));
    }

    /**
     * Sets the item texture from UUID and name.
     *
     * @param uuid - UUID of the player.
     * @param name - Name of the player.
     */
    public ItemBuilder setHeadTexture(@Nonnull UUID uuid, @Nonnull String name) {
        return modifyMeta(SkullMeta.class, meta -> meta.setOwnerProfile(Bukkit.createPlayerProfile(uuid, name)));
    }

    /**
     * Sets a head texture from a minecraft url link.
     * <p>
     * If copying from <a href="https://minecraft-heads.com/custom-heads">Minecraft-Heads</a>, use <code>Other -> Minecraft-URL</code> value,
     * which should look something like this:
     * <code>87fbe93a9c518883186a2aefbd2b7df18ba05a8a7dd1a756ef6565d3e5807a0c</code>
     */
    public ItemBuilder setHeadTextureUrl(@Nonnull String url) {
        final PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
        final PlayerTextures textures = profile.getTextures();

        try {
            textures.setSkin(new URL(URL_TEXTURE_LINK + url));
            profile.setTextures(textures);

            modifyMeta(SkullMeta.class, meta -> meta.setOwnerProfile(profile));
        } catch (Exception e) {
            EternaLogger.exception(e);
        }

        return this;
    }

    /**
     * Sets items skull owner from a Minecraft username.
     *
     * @param owner - Skull owner.
     */
    public ItemBuilder setSkullOwner(@Nonnull String owner) {
        return setSkullOwner(owner, null);
    }

    /**
     * Sets the item skull owner from a Minecraft username with a sound.
     *
     * @param owner - Owner.
     * @param sound - Sound
     */
    public ItemBuilder setSkullOwner(@Nonnull String owner, @Nullable Sound sound) {
        return modifyMeta(SkullMeta.class, meta -> {
            meta.setOwner(owner);
            meta.setNoteBlockSound(sound != null ? sound.getKey() : null);
        });
    }

    /**
     * Adds an attribute to the item.
     *
     * @param attribute - Attribute to add.
     * @param amount    - Amount of the attribute.
     * @param operation - Operation of the attribute.
     * @param slot      - Slot of the attribute.
     */
    public ItemBuilder addAttribute(@Nonnull Attribute attribute, double amount, @Nonnull AttributeModifier.Operation operation, @Nullable EquipmentSlot slot) {
        return modifyMeta(meta -> {
            meta.addAttributeModifier(
                    attribute,
                    new AttributeModifier(UUID.randomUUID(), attribute.toString(), amount, operation, slot)
            );
        });
    }

    /**
     * Hide an item flags for the item.
     *
     * @param flags - Flags to hide.
     */
    public ItemBuilder hideFlag(@Nonnull @Range(min = 1) ItemFlag... flags) {
        return modifyMeta(meta -> meta.addItemFlags(flags));
    }

    /**
     * Shows item flags for the item.
     *
     * @param flags - Flags to show.
     */
    public ItemBuilder showFlag(@Nonnull @Range(min = 1) ItemFlag... flags) {
        return modifyMeta(meta -> meta.removeItemFlags(flags));
    }

    /**
     * Shows all the item flags.
     */
    public ItemBuilder showFlags() {
        return modifyMeta(meta -> meta.removeItemFlags(ItemFlag.values()));
    }

    /**
     * Hides all the item flags.
     */
    public ItemBuilder hideFlags() {
        return modifyMeta(meta -> meta.addItemFlags(ItemFlag.values()));
    }

    /**
     * Clears the item name, meaning setting it to empty.
     */
    public ItemBuilder clearName() {
        return modifyMeta(meta -> meta.setDisplayName(""));
    }

    /**
     * Sets the item durability.
     *
     * @param durability - Durability to set.
     */
    public ItemBuilder setDurability(int durability) {
        return modifyMeta(Damageable.class, meta -> {
            meta.setDamage(durability);
        });
    }

    /**
     * Returns the type of the builder.
     *
     * @return the type of the builder.
     */
    public Material getType() {
        return item.getType();
    }

    /**
     * Set the type of the ItemStack.
     *
     * @param material - New type.
     */
    public ItemBuilder setType(@Nonnull Material material) {
        item.setType(material);
        return this;
    }

    /**
     * Skips {@link ItemBuilder#build(boolean)} ID checks and only finalized item stack.
     *
     * @return ItemStack.
     */
    public ItemStack toItemStack() {
        //item.setItemMeta(getMetaNonNull());
        return item;
    }

    /**
     * Clears Item flags, name, lore and makes it unbreakable before returning ItemStack.
     *
     * @return cleaned ItemStack.
     */
    public ItemStack cleanToItemSack() {
        hideFlags();
        setName("&0");
        removeLore();
        setUnbreakable(true);

        return toItemStack();
    }

    /**
     * Builds item stack and hides all extra data
     * in the lore of the item.
     *
     * @return icon-themed ItemStack.
     */
    public ItemStack asIcon() {
        hideFlags();
        return build();
    }

    /**
     * Finalizes item meta and returns an ItemStack.
     *
     * @param overrideFunctions - if true and the {@link ItemFunctionList} is already registered with this {@link ItemBuilder} {@link #id}, it will override it.
     * @return finalized ItemStack.
     */
    @Nonnull
    public ItemStack build(boolean overrideFunctions) {
        if (id != null) {
            // Always store Id
            modifyMeta(meta -> NBT.setValue(meta, PLUGIN_PATH, NBTType.STR, id));

            if (functions != null && (!isIdRegistered(id) || overrideFunctions)) {
                registeredFunctions.put(id, functions);
            }
        }

        //this.item.setItemMeta(getMetaNonNull());

        // Apply native NBT
        if (!nativeNbt.isBlank()) {
            item = NBTNative.setNbt(item, nativeNbt);
        }

        return item;
    }

    /**
     * Finalizes item meta and returns an ItemStack.
     *
     * @return finalized ItemStack.
     */
    public ItemStack build() {
        return build(false);
    }

    /**
     * Returns the name of the item.
     */
    @Nonnull
    public String getName() {
        final ItemMeta meta = getMeta();

        return meta == null ? "" : meta.getDisplayName();
    }

    /**
     * Sets the name of the item.
     * Default to green color.
     *
     * @param name - Name to set.
     */
    public ItemBuilder setName(@Nonnull String name) {
        return modifyMeta(meta -> meta.setDisplayName(ChatColor.GREEN + colorize(name)));
    }

    /**
     * Gets a sublist of item's lore.
     *
     * @param start - start index
     * @param end   - end index
     * @return a sublist of item's lore, or empty list if <code>end >= lore.size()</code> or there is no lore.
     */
    @Nullable
    public List<String> getLore(int start, int end) {
        final List<String> lore = getLore();

        if (lore.isEmpty() || end >= lore.size()) {
            return Lists.newArrayList();
        }

        return lore.subList(start, end);
    }

    /**
     * Gets the current meta.
     *
     * @return the current meta.
     */
    @Nullable
    public ItemMeta getMeta() {
        return item.getItemMeta();
    }

    /**
     * Gets the meta cast to the given class; or null if not applicable.
     *
     * @param clazz - Meta class.
     * @return the meta cast to the given class; or null if not applicable.
     */
    @Nullable
    public <T extends ItemMeta> T getMeta(@Nonnull Class<T> clazz) {
        final ItemMeta meta = getMeta();

        if (clazz.isInstance(meta)) {
            return clazz.cast(meta);
        }

        return null;
    }

    /**
     * Returns the amount of the item.
     *
     * @return the amount of the item.
     */
    public int getAmount() {
        return item.getAmount();
    }

    /**
     * Sets the item amount.
     *
     * @param amount - New amount.
     */
    public ItemBuilder setAmount(int amount) {
        item.setAmount(Numbers.clamp(amount, 0, Byte.MAX_VALUE));
        return this;
    }

    /**
     * Gets a copy of enchantment map for this item.
     *
     * @return a copy of enchantment map for this item.
     */
    public Map<Enchantment, Integer> getEnchants() {
        final ItemMeta meta = getMeta();

        return meta == null ? Maps.newHashMap() : meta.getEnchants();
    }

    /**
     * Returns true if item is unbreakable; false otherwise.
     *
     * @return true if item is unbreakable; false otherwise.
     */
    public boolean isUnbreakable() {
        final ItemMeta meta = getMeta();

        return meta == null ? false : meta.isUnbreakable();
    }

    /**
     * Sets item's readability.
     *
     * @param unbreakable - Is unbreakable.
     */
    public ItemBuilder setUnbreakable(boolean unbreakable) {
        return modifyMeta(meta -> meta.setUnbreakable(unbreakable));
    }

    /**
     * Returns predicate error.
     *
     * @return predicate error.
     */
    public String getError() {
        return getFunctions().first().getErrorMessage();
    }

    /**
     * Returns original item without the meta applied.
     *
     * @return original item without the meta applied.
     */
    public ItemStack getItem() {
        return item;
    }

    /**
     * Returns repair cost on anvil of this item; or <code>0</code> if not repairable.
     *
     * @return repair cost on anvil of this item; or <code>0</code> if not repairable.
     */
    public int getRepairCost() {
        final ItemMeta meta = getMeta();

        if (meta instanceof Repairable repairable) {
            return repairable.getRepairCost();
        }

        return 0;
    }

    /**
     * Sets the repair cost on the anvil of the item.
     *
     * @param valueInLevels - Value in levels.
     */
    public ItemBuilder setRepairCost(int valueInLevels) {
        final ItemMeta meta = getMeta();

        if (meta instanceof Repairable repairable) {
            repairable.setRepairCost(valueInLevels);
        }

        return this;
    }

    /**
     * Gets the color of leather armor; or null if item is not leather armor.
     *
     * @return Gets the color of leather armor; or null if item is not leather armor.
     */
    @Nullable
    public Color getLeatherColor() {
        final LeatherArmorMeta meta = getMeta(LeatherArmorMeta.class);

        if (meta == null) {
            return null;
        }

        return meta.getColor();
    }

    /**
     * Gets the head texture of the item; or null if item is not a skull.
     *
     * @return the head texture of the item; or null if item is not a skull.
     */
    @Nullable
    public String getHeadTexture() {
        try {
            final ItemMeta meta = getMeta();
            if (meta == null) {
                return null;
            }

            return (String) meta.getClass().getDeclaredField("profile").get(meta);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sets an actual base64 value as textures.
     * <p>
     * If copying from <a href="https://minecraft-heads.com/custom-heads">Minecraft-Heads</a>, use <code>Other -> Value</code> value,
     * which should look something like this:
     * <code>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODdmYmU5M2E5YzUxODg4MzE4NmEyYWVmYmQyYjdkZjE4YmEwNWE4YTdkZDFhNzU2ZWY2NTY1ZDNlNTgwN2EwYyJ9fX0=</code>
     *
     * @deprecated Dropping support for base64, also not fixing the console spam.
     */
    @Deprecated
    public ItemBuilder setHeadTexture(@Nonnull String base64) {
        final GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", base64));

        modifyMeta(meta -> {
            try {
                final Field field = FieldUtils.getDeclaredField(meta.getClass(), "profile", true);
                field.set(meta, profile);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        return this;
    }

    /**
     * Gets a set of flags present on this item.
     * The set is immutable.
     *
     * @return the set of flags present on this item.
     */
    @Nonnull
    public Set<ItemFlag> getFlags() {
        final ItemMeta meta = getMeta();

        return meta == null ? Sets.newHashSet() : meta.getItemFlags();
    }

    /**
     * Gets a multimap of attributes on this item.
     * The map is immutable.
     *
     * @return a multimap of attributes on this item.
     */
    public Multimap<Attribute, AttributeModifier> getAttributes() {
        final ItemMeta meta = getMeta();

        return meta == null ? ImmutableMultimap.of() : meta.getAttributeModifiers();
    }

    /**
     * Gets the sum of {@link Attribute#GENERIC_ATTACK_DAMAGE} attributes from this item.
     *
     * @return the sum of {@link Attribute#GENERIC_ATTACK_DAMAGE} attributes from this item.
     */
    public double getPureDamage() {
        double damage = 0.0d;

        for (AttributeModifier t : getAttributes().get(Attribute.GENERIC_ATTACK_DAMAGE)) {
            final double current = t.getAmount();
            damage = Math.max(current, damage);
        }

        return damage;
    }

    /**
     * Adds a {@link Attribute#GENERIC_ATTACK_DAMAGE} with a given value.
     *
     * @param damage - Damage value of the attribute.
     */
    public ItemBuilder setPureDamage(double damage) {
        return addAttribute(
                Attribute.GENERIC_ATTACK_DAMAGE,
                damage,
                AttributeModifier.Operation.ADD_NUMBER,
                EquipmentSlot.HAND
        );
    }

    /**
     * Returns custom ID of the item.
     *
     * @return custom ID of the item.
     */
    @Nullable
    public String getId() {
        return id;
    }

    /**
     * Sets the persistent data of the item.
     *
     * @param path  - path to the data
     * @param type  - type of the data
     * @param value - value of the data
     * @param <T>   - type of the data
     */
    public <T> ItemBuilder setPersistentData(@Nonnull String path, @Nonnull PersistentDataType<T, T> type, @Nonnull T value) {
        try {
            return modifyMeta(meta -> meta.getPersistentDataContainer().set(new NamespacedKey(EternaPlugin.getPlugin(), path), type, value));
        } catch (IllegalArgumentException e) {
            EternaLogger.error("An error occurred in ItemBuilder, report this! " + e.getMessage());
        }

        return this;
    }

    /**
     * Returns true if item has persistent data at the path; false otherwise.
     *
     * @param path - path to the data
     * @param type - type of the data
     * @param <T>  - type of the data
     * @return true if item has persistent data at the path; false otherwise.
     */
    public <T> boolean hasPersistentData(@Nonnull String path, @Nonnull PersistentDataType<T, T> type) {
        final ItemMeta meta = getMeta();

        return meta != null && meta.getPersistentDataContainer().has(new NamespacedKey(EternaPlugin.getPlugin(), path), type);
    }

    /**
     * Gets the persistent data of the item.
     *
     * @param path - path to the data
     * @param type - type of the data
     * @param <T>  - type of the data
     * @return persistent data of the item.
     */
    public <T> T getPersistentData(@Nonnull String path, @Nonnull PersistentDataType<T, T> type) {
        final ItemMeta meta = getMeta();

        return meta == null ? null : meta.getPersistentDataContainer().get(new NamespacedKey(EternaPlugin.getPlugin(), path), type);
    }

    /**
     * Makes the item glow with enchantment glint.
     * This method will add {@link Enchantment#LUCK} and will hide item's enchants.
     */
    public ItemBuilder glow() {
        addEnchant(Enchantment.LUCK, 1);
        hideFlag(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    /**
     * Gets the cooldown of the item.
     *
     * @return the cooldown of the item.
     * @see #addFunction(ItemFunction)
     * @deprecated cooldowns are now per-functions
     */
    @Deprecated
    public int getCd() {
        return getFunctions().first().getCd();
    }

    /**
     * Gets the item predicate for function.
     *
     * @return item predicate for function.
     * @see #addFunction(ItemFunction)
     * @deprecated predicates are now per-functions
     */
    @Deprecated
    @Nullable
    public Predicate<Player> getPredicate() {
        return getFunctions().first().getPredicate();
    }

    /**
     * Returns true if item is canceling clicks.
     *
     * @return true if item is canceling clicks.
     * @see #addFunction(ItemFunction)
     * @deprecated cancel clicks is now per-functions
     */
    @Deprecated
    public boolean isCancelClicks() {
        return getFunctions().first().isCancelClicks;
    }

    /**
     * Sets if even should cancel clicks upon click event triggering.
     * Set to false if using custom checks for click.
     *
     * @param cancelClicks - New value.
     * @see #addFunction(ItemFunction)
     * @deprecated cancel clicks is now per-functions
     */
    @Deprecated
    public ItemBuilder setCancelClicks(boolean cancelClicks) {
        getFunctions().first().isCancelClicks = cancelClicks;
        return this;
    }

    /**
     * Gets the current armor trim; or null if no trim or item is not armor.
     *
     * @return the current armor trim; or null if no trim or item is not armor.
     */
    @Nullable
    public ArmorTrim getArmorTrim() {
        final ArmorMeta meta = getMeta(ArmorMeta.class);

        if (meta == null) {
            return null;
        }

        return meta.getTrim();
    }

    /**
     * Applies the armor trim to this armor, does nothing if item is not armor.
     *
     * @param pattern  - Trim pattern.
     * @param material - Trim material.
     */
    public ItemBuilder setArmorTrim(@Nonnull TrimPattern pattern, @Nonnull TrimMaterial material) {
        return modifyMeta(ArmorMeta.class, meta -> meta.setTrim(new ArmorTrim(material, pattern)));
    }

    /**
     * Modifies the {@link ItemMeta} and applies it to the item.
     *
     * @param consumer - Consumer.
     */
    public ItemBuilder modifyMeta(@Nonnull Consumer<ItemMeta> consumer) {
        final ItemMeta meta = item.getItemMeta();
        consumer.accept(meta);

        item.setItemMeta(meta);
        return this;
    }

    /**
     * Modifies the {@link ItemMeta} of the given class and applies it to the item.
     *
     * @param clazz    - Meta class.
     * @param consumer - Consumer.
     */
    public <T extends ItemMeta> ItemBuilder modifyMeta(@Nonnull Class<T> clazz, @Nonnull Consumer<T> consumer) {
        return modifyMeta(clazz, null, consumer);
    }

    /**
     * Sets the item {@link Material}, modifies the {@link ItemMeta} of the given class and applies it to the item.
     *
     * @param clazz    - Meta class.
     * @param material - Material.
     * @param consumer - Consumer.
     */
    public <T extends ItemMeta> ItemBuilder modifyMeta(@Nonnull Class<T> clazz, @Nullable Material material, @Nonnull Consumer<T> consumer) {
        if (material != null) {
            setType(material);
        }

        final ItemMeta meta = item.getItemMeta();

        if (clazz.isInstance(meta)) {
            final T cast = clazz.cast(meta);

            consumer.accept(cast);
            item.setItemMeta(cast);
        }

        return this;
    }

    /**
     * Creates builder of provided ItemStack.
     *
     * @param itemStack - ItemStack.
     */
    public static ItemBuilder of(ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    /**
     * Creates player head from texture.
     *
     * @param texture - Texture to use.
     * @deprecated {@link ItemBuilder#playerHeadUrl(String)}
     */
    @Deprecated
    public static ItemBuilder playerHead(@Nonnull String texture) {
        return new ItemBuilder(Material.PLAYER_HEAD).setHeadTexture(texture);
    }

    /**
     * Creates player head from texture from url.
     *
     * @param url - Url to texture.
     */
    public static ItemBuilder playerHeadUrl(@Nonnull String url) {
        return new ItemBuilder(Material.PLAYER_HEAD).setHeadTextureUrl(url);
    }

    /**
     * Creates leather helmet with provided color.
     *
     * @param color - Color to use.
     */
    public static ItemBuilder leatherHat(@Nullable Color color) {
        return new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(color);
    }

    /**
     * Creates leather chestplate with provided color.
     *
     * @param color - Color to use.
     */
    public static ItemBuilder leatherTunic(@Nullable Color color) {
        return new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(color);
    }

    /**
     * Creates leather leggings with provided color.
     *
     * @param color - Color to use.
     */
    public static ItemBuilder leatherPants(@Nullable Color color) {
        return new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(color);
    }

    /**
     * Creates leather boots with provided color.
     *
     * @param color - Color to use.
     */
    public static ItemBuilder leatherBoots(@Nullable Color color) {
        return new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(color);
    }

    /**
     * Creates builder of provided material.
     *
     * @param material - Material to use.
     */
    public static ItemBuilder of(@Nonnull Material material) {
        return new ItemBuilder(material);
    }

    /**
     * Creates builder of provided material with provided name.
     *
     * @param material - Material to use.
     * @param name     - Name to use.
     */
    public static ItemBuilder of(@Nonnull Material material, @Nonnull String name) {
        return new ItemBuilder(material).setName(name);
    }

    /**
     * Creates builder of provided material with provided name and lore.
     *
     * @param material - Material to use.
     * @param name     - Name to use.
     * @param lore     - Lore to use.
     */
    public static ItemBuilder of(@Nonnull Material material, @Nonnull String name, @Nonnull @Range(min = 1) String... lore) {
        final ItemBuilder builder = new ItemBuilder(material).setName(name);

        for (String str : lore) {
            builder.addLore(str);
        }

        return builder;
    }

    /**
     * Creates air builder.
     */
    public static ItemBuilder air() {
        return of(Material.AIR);
    }

    /**
     * Create builder from existing ItemStack.
     *
     * @param stack - Item Stack.
     */
    public static ItemBuilder fromItemStack(@Nonnull ItemStack stack) {
        return new ItemBuilder(stack);
    }

    /**
     * Gets the item by its ID; or null if it doesn't exist.
     *
     * @param id - Id.
     * @return the item by its ID; or null if it doesn't exist.
     */


    /**
     * Gets the {@link ItemFunctionList} by the given Id; or null if none.
     *
     * @param id - Id.
     * @return the {@link ItemFunctionList} by the given Id; or null if none.
     */
    @Nullable
    public static ItemFunctionList getFunctionListById(@Nonnull @ForceLowercase String id) {
        return registeredFunctions.get(id);
    }

    /**
     * Gets a copy of registered {@link ItemFunctionList} Ids.
     *
     * @return a copy of registered {@link ItemFunctionList} Ids.
     */
    @Nonnull
    public static Set<String> getRegisteredIDs() {
        return Sets.newHashSet(registeredFunctions.keySet());
    }

    /**
     * Gets the {@link ItemStack} id; or empty string.
     * The id is stored in {@link #PLUGIN_PATH}.
     *
     * @param item - Item to get ID from.
     * @return the {@link ItemStack} id; or empty string.
     */
    @Nonnull
    public static String getItemID(@Nonnull ItemStack item) {
        final ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return "";
        }

        final String id = NBT.getString(meta, PLUGIN_PATH, "");
        return (id.isEmpty() || id.isBlank()) ? "" : id;
    }

    /**
     * Returns true if the given item's ID matches the given ID.
     *
     * @param item - Item to check.
     * @param id   - ID to compare to.
     * @return true if the given item's ID matches the given ID; false otherwise.
     */
    public static boolean itemHasID(@Nonnull ItemStack item, @Nonnull String id) {
        final String itemId = getItemID(item);
        return itemHasID(item) && itemId != null && itemId.equalsIgnoreCase(id);
    }

    /**
     * Returns true if the given item's ID contains a part of the given ID.
     *
     * @param item - Item to check.
     * @param id   - Id to check.
     * @return true if the given item's ID contains a part of the given ID; false otherwise.
     */
    public static boolean itemContainsId(@Nonnull ItemStack item, @Nonnull String id) {
        final String itemId = getItemID(item);
        return itemHasID(item) && itemId != null && itemId.contains(id);
    }

    /**
     * Returns true if item has any ID.
     *
     * @param item - Item to check.
     * @return true if an item has any ID; false otherwise.
     */
    public static boolean itemHasID(ItemStack item) {
        return getItemID(item) != null;
    }

    /**
     * Performs a string wrap.
     *
     * @param prefix - Prefix of each new line.
     * @param string - String to wrap.
     * @param limit  - Character limit.
     * @return a list of strings that are less or equals to the limit.
     */
    public static List<String> splitString(@Nullable String prefix, @Nonnull String string, int limit) {
        final List<String> list = new ArrayList<>();
        final char[] chars = string.toCharArray();

        StringBuilder lastColor = new StringBuilder(prefix == null ? DEFAULT_COLOR : prefix);
        StringBuilder builder = new StringBuilder(lastColor);
        int counter = 0;

        for (int i = 0; i < chars.length; i++) {
            final char c = chars[i];
            final boolean nextCharInRange = i + 1 < chars.length;
            final boolean isManualSplit = isManualSplit(chars, i);

            // If out of limit and hit whitespace then add line.
            final boolean lastChar = i == chars.length - 1;
            if (lastChar || (counter >= limit && Character.isWhitespace(c)) || isManualSplit) {
                if (isManualSplit) {
                    i++;
                }

                // Don't eat the last char.
                if (lastChar) {
                    builder.append(c);
                }

                list.add(colorize(builder.toString().trim()));

                counter = 0;
                builder = new StringBuilder(lastColor);
                lastColor = new StringBuilder(prefix == null ? DEFAULT_COLOR : prefix);
                continue;
            }

            builder.append(c);

            // don't count colors
            if (isColorCode(c) && nextCharInRange && isColorChar(chars[i + 1])) {
                final char nextChar = chars[++i];

                builder.append(nextChar);
                lastColor.append(c).append(nextChar);
                continue;
            }

            ++counter;
        }

        return list;
    }

    public static boolean isColorCode(char c) {
        return c == ChatColor.COLOR_CHAR || c == '&';
    }

    public static boolean isColorChar(char c) {
        return net.md_5.bungee.api.ChatColor.ALL_CODES.indexOf(c) != -1;
    }

    @Deprecated
    public static List<String> splitAfter(String text, int max) {
        return splitAfter("&7", text, max);
    }

    @Deprecated
    public static List<String> splitAfter(String text, int max, String prefix) {
        return splitAfter(prefix, text, max);
    }

    /**
     * Performs a string wrap.
     * Defaults to no prefix.
     *
     * @param string - String to wrap.
     * @param limit  - Character limit.
     * @return a list of strings that are less or equals to the limit.
     */
    public static List<String> splitString(@Nonnull String string, int limit) {
        return splitString(null, string, limit);
    }

    /**
     * Performs a string wrap.
     * Default to no prefix and {@link #DEFAULT_SMART_SPLIT_CHAR_LIMIT}.
     *
     * @param string - String to wrap.
     * @return a list of strings that are less or equals to the limit.
     */
    public static List<String> splitString(@Nonnull String string) {
        return splitString(string, DEFAULT_SMART_SPLIT_CHAR_LIMIT);
    }

    /**
     * Gets the last colors of the given string.
     *
     * @param string - String to get last colors from.
     * @return the last colors of the given string.
     */
    @Nonnull
    public static List<String> getLastColors(String string) {
        final char[] chars = (string + " ").toCharArray();
        final List<String> colors = new ArrayList<>();

        StringBuilder currentColor = new StringBuilder();
        boolean isColor = false;

        for (final char c : chars) {
            if (isColor && isColorChar(c)) {
                currentColor.append(c);
                isColor = false;
                continue;
            }

            // mark as color
            if (c == '&') {
                currentColor.append(c);
                isColor = true;
                continue;
            }

            if (!currentColor.isEmpty()) {
                colors.add(currentColor.toString());
                currentColor = new StringBuilder();
            }
        }

        return colors;
    }

    /**
     * Gets the last color from the given string; or default is none.
     *
     * @param string - String to get last color from.
     * @param def    - Default value.
     * @return the last color from the given string; or default is none.
     */
    public static String getLastColor(@Nonnull String string, String def) {
        final List<String> lastColors = getLastColors(string);

        return lastColors.isEmpty() ? def : lastColors.get(lastColors.size() - 1);
    }

    // static helpers

    /**
     * Sets the item name.
     *
     * @param item - Item to set name to.
     * @param name - Name to set.
     */
    public static void setName(@Nonnull ItemStack item, @Nonnull String name) {
        final ItemMeta meta = item.getItemMeta();
        Nulls.runIfNotNull(meta, m -> m.setDisplayName(colorize(name)));
        item.setItemMeta(meta);
    }

    /**
     * Sets the item lore.
     *
     * @param item - Item to set lore to.
     * @param lore - Lore to set.
     */
    public static void setLore(@Nonnull ItemStack item, @Nonnull String lore) {
        final ItemMeta meta = item.getItemMeta();
        Nulls.runIfNotNull(meta, m -> m.setLore(Collections.singletonList(lore)));
        item.setItemMeta(meta);
    }

    public static String format(@Nonnull String string, @Nullable Object... format) {
        if (format == null || format.length == 0) {
            return ChatColor.translateAlternateColorCodes('&', string);
        }

        return Chat.format(string, format);
    }

    private static boolean isManualSplit(char[] chars, int index) {
        return (index < chars.length && index + 1 < chars.length)
                && (chars[index] == MANUAL_SPLIT_CHAR && chars[index + 1] == MANUAL_SPLIT_CHAR);
    }

    private static String colorize(String s) {
        return format(s);
    }

    private static boolean isIdRegistered(String id) {
        return id != null && registeredFunctions.containsKey(id);
    }

    private static boolean isManualSplitChar(char c) {
        return c == MANUAL_SPLIT_CHAR;
    }

    /**
     * @see ItemBuilder#splitString(String, String, int)
     * @deprecated improved system a little
     */
    @Deprecated
    private static List<String> splitAfter(String linePrefix, String text, int maxChars) {
        List<String> list = new ArrayList<>();
        String line = "";
        int counter = 0;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            final boolean checkLast = c == text.charAt(text.length() - 1);
            line = line.concat(String.valueOf(c));
            counter++;

            if (c == '_' && text.charAt(i + 1) == '_') {
                // this fixes an extra space before manual split.
                // it's not a bug, and it works as indented, but it's
                // getting quite annoying at times to fix.
                if (list.size() > 1) {
                    final String lastValue = list.get(list.size() - 1).trim().replace("§", "&");
                    if (lastValue.isEmpty() || lastValue.isBlank() || lastValue.equalsIgnoreCase(linePrefix)) {
                        list.remove(list.size() - 1);
                    }
                }

                list.add(colorize(linePrefix + line.substring(0, line.length() - 1).trim()));
                line = "";
                counter = 0;
                i++;
                continue;
            }

            if (counter >= maxChars || i == text.length() - 1) {
                if (Character.isWhitespace(c) || checkLast) {
                    list.add(colorize(linePrefix + line.trim()));
                    line = "";
                    counter = 0;
                }
            }
        }

        return list;
    }

}