package me.hapyl.eterna.module.inventory;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.common.collect.*;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.annotate.Super;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.math.Numbers;
import me.hapyl.eterna.module.nbt.NBT;
import me.hapyl.eterna.module.nbt.NBTType;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.registry.Keyed;
import me.hapyl.eterna.module.util.BukkitUtils;
import me.hapyl.eterna.module.util.Nulls;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.inventory.meta.components.JukeboxPlayableComponent;
import org.bukkit.inventory.meta.components.ToolComponent;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.map.MapView;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.Range;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URI;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;

/**
 * Build ItemStack easier. Add names, lore, smart lore, enchants and even click events!
 */
@SuppressWarnings("UnstableApiUsage")
public class ItemBuilder implements Cloneable, Keyed {

    /**
     * The default smart split char limit.
     *
     * @see ItemBuilder#splitString(String)
     */
    public static final int DEFAULT_SMART_SPLIT_CHAR_LIMIT = 35;

    /**
     * The manual separator for lore.
     */
    public static final String NEW_LINE_SEPARATOR = "__";

    /**
     * The default color of lore.
     * Will replace the ugly vanilla purple.
     */
    public static final ChatColor DEFAULT_COLOR = ChatColor.GRAY;

    /**
     * The default color of name.
     */
    public static final ChatColor DEFAULT_NAME_COLOR = ChatColor.GREEN;

    private static final char MANUAL_SPLIT_CHAR = '_';

    private static final java.util.regex.Pattern BASE64_DECODE_PATTERN = java.util.regex.Pattern.compile("\"url\":\"(http[^\"]+)\"");
    private static final String PLUGIN_PATH = "item_key";
    private static final String URL_TEXTURE_LINK = "https://textures.minecraft.net/texture/";

    protected static Map<Key, ItemFunctionList> registeredFunctions = Maps.newHashMap();

    private static AttributeModifier HIDE_ATTRIBUTES_MODIFIER;

    protected ItemStack item;

    @Nonnull private Key key;
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
        this(stack.clone(), Key.empty());
    }

    /**
     * Create a new ItemBuilder with the given Material and ID for events.
     *
     * @param material - Material of the builder.
     * @param key      - Key of the builder.
     */
    public ItemBuilder(@Nonnull Material material, @Nonnull Key key) {
        this(new ItemStack(material), key);
    }

    /**
     * Create a new ItemBuilder from ItemStack with ID for events.
     *
     * @param stack - ItemStack of the builder. Data is cloned.
     * @param key   - Key of the builder.
     */
    @Super
    public ItemBuilder(@Nonnull ItemStack stack, @Nonnull Key key) {
        this.item = stack;
        this.key = key;
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
            functions = new ItemFunctionList();
        }

        return functions;
    }

    /**
     * Clears all functions if there are any.
     */
    public ItemBuilder clearFunctions() {
        if (functions != null) {
            functions.clearFunctions();
        }

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
    public ItemBuilder setMapView(@Nullable MapView view) {
        return modifyMeta(MapMeta.class, Material.FILLED_MAP, meta -> meta.setMapView(view));
    }

    /**
     * Sets the book name.
     * Material will be forced to {@link Material#WRITTEN_BOOK}.
     *
     * @param name - New book name.
     */
    public ItemBuilder setBookName(@Nullable String name) {
        return modifyMeta(BookMeta.class, Material.WRITTEN_BOOK, meta -> meta.setTitle(name != null ? Chat.format(name) : null));
    }

    /**
     * Sets the book author.
     * Material will be forced to {@link Material#WRITTEN_BOOK}.
     *
     * @param author - New book author.
     */
    public ItemBuilder setBookAuthor(@Nonnull String author) {
        return modifyMeta(BookMeta.class, Material.WRITTEN_BOOK, meta -> meta.setAuthor(Chat.format(author)));
    }

    /**
     * Sets the book title.
     * Material will be forced to {@link Material#WRITTEN_BOOK}.
     *
     * @param title - New book title.
     */
    public ItemBuilder setBookTitle(@Nullable String title) {
        return modifyMeta(BookMeta.class, Material.WRITTEN_BOOK, meta -> meta.setTitle(title != null ? Chat.format(title) : null));
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
    public ItemBuilder addClickEvent(@Nonnull Consumer<Player> runnable, @Range(from = 1, to = Integer.MAX_VALUE) @Nonnull Action... actions) {
        if (actions.length < 1) {
            throw new IndexOutOfBoundsException("This requires at least one action.");
        }

        final ItemFunctionList functions = getFunctions();
        final ItemFunction function = new ItemFunction(runnable);

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
    public ItemBuilder addFunction(@Nonnull Function<Key, ItemFunction> function) {
        final ItemFunctionList functions = getFunctions();
        final ItemFunction fn = function.apply(key);

        functions.addFunction(fn);
        return this;
    }

    /**
     * Adds and returns a new {@link ItemFunction}.
     * <br>
     * <b>Do not forget to add a click type! {@link ItemFunction#accept(Action)}</b>
     *
     * @param consumer - Action.
     */
    @Nonnull
    public ItemFunction addFunction(@Nonnull Consumer<Player> consumer) {
        final ItemFunctionList functions = getFunctions();
        final ItemFunction function = new ItemFunction(consumer);

        functions.addFunction(function);
        return function;
    }

    /**
     * Sets persistent data to the given path.
     *
     * @param path  - Path to the data.
     * @param value - Value to set.
     * @deprecated Prefer {@link ItemBuilder#setNbt(String, NBTType, Object)}
     */
    @Deprecated
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
        return setSmartLore(lore, DEFAULT_COLOR.toString(), limit);
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
                strings.set(line, Chat.format(lore));
                meta.setLore(strings);
            }
        });
    }

    /**
     * Sets the item lore.
     * You can use {@link #NEW_LINE_SEPARATOR} to insert a new line,
     * or use {@link #addTextBlockLore(String)}.
     *
     * @param lore        - Lore to set.
     * @param prefixColor - Color of the text after split.
     */
    public ItemBuilder addLore(@Nonnull String lore, @Nonnull ChatColor prefixColor) {
        return modifyMeta(meta -> {
            final List<String> metaLore = getLore();

            for (String value : lore.split(NEW_LINE_SEPARATOR)) {
                metaLore.add(prefixColor + Chat.format(value));
            }

            meta.setLore(metaLore);
        });
    }

    /**
     * Sets the item lore.
     * You can use {@link #NEW_LINE_SEPARATOR} to insert a new line,
     * or use {@link #addTextBlockLore(String)}}.
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
     * Adds lore to the item if the condition is met.
     *
     * @param lore      - Lore to add.
     * @param condition - Condition.
     */
    public ItemBuilder addLoreIf(@Nonnull String lore, boolean condition) {
        if (condition) {
            addLore(lore);
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
        return modifyMeta(meta -> meta.setLore(Arrays.asList(Chat.format(lore).split(separator))));
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
    public ItemBuilder addTextBlockLore(@Nonnull String textBlock) {
        return addTextBlockLore(textBlock, DEFAULT_SMART_SPLIT_CHAR_LIMIT);
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
    public ItemBuilder addTextBlockLore(@Nonnull String textBlock, int charLimit) {
        return addTextBlockLore(textBlock, "&7", charLimit);
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
     * @param textBlock  - Text block.
     * @param linePrefix - Prefix to put before each line.
     */
    public ItemBuilder addTextBlockLore(@Nonnull String textBlock, @Nonnull String linePrefix) {
        return addTextBlockLore(textBlock, linePrefix, DEFAULT_SMART_SPLIT_CHAR_LIMIT);
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
     * @param textBlock  - Text block.
     * @param linePrefix - Prefix to put before each line.
     * @param charLimit  - Char wrap limit.
     */
    public ItemBuilder addTextBlockLore(@Nonnull String textBlock, @Nonnull String linePrefix, int charLimit) {
        return modifyMeta(meta -> {
            final String[] strings = textBlock.split("\n");
            final List<String> lore = getLore();

            for (String string : strings) {
                if (string.equalsIgnoreCase("")) { // paragraph
                    lore.add("");
                    continue;
                }

                final int prefixIndex = string.lastIndexOf(";;");
                String prefix = linePrefix;

                if (prefixIndex > 0) {
                    prefix = string.substring(0, prefixIndex);
                    string = string.substring(prefixIndex + 2);
                }

                final List<String> splitStrings = splitString(prefix, string, charLimit);

                lore.addAll(splitStrings);
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
     * Adds an enchantment to the item.
     *
     * @param enchant - Enchantment to add.
     * @param lvl     - Level of the enchantment.
     */
    public ItemBuilder addEnchant(@Nonnull Enchant enchant, int lvl) {
        return addEnchant(enchant.getAsBukkit(), lvl);
    }

    /**
     * Adds an enchantment to the item.
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
        final PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        final PlayerTextures textures = profile.getTextures();

        try {
            textures.setSkin(new URI(URL_TEXTURE_LINK + url).toURL());
            profile.setTextures(textures);

            modifyMeta(SkullMeta.class, meta -> meta.setPlayerProfile(profile));
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
            meta.setPlayerProfile(Bukkit.createProfile(null, owner));
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
        return addAttribute(attribute, amount, operation, equipmentSlotGroupFromSlot(slot));
    }

    /**
     * Adds an attribute to the item.
     *
     * @param attribute - Attribute to add.
     * @param amount    - Amount of the attribute.
     * @param operation - Operation of the attribute.
     * @param slot      - Slot of the attribute.
     */
    public ItemBuilder addAttribute(@Nonnull Attribute attribute, double amount, @Nonnull AttributeModifier.Operation operation, @Nullable EquipmentSlotGroup slot) {
        return modifyMeta(meta -> {
            meta.addAttributeModifier(
                    attribute,
                    new AttributeModifier(
                            BukkitUtils.createKey(UUID.randomUUID()),
                            amount,
                            operation,
                            slot != null ? slot : EquipmentSlotGroup.ANY
                    )
            );
        });
    }

    /**
     * Hide an item flags for the item.
     *
     * @param flags - Flags to hide.
     */
    public ItemBuilder hideFlag(@Nonnull @Range(from = 1, to = Byte.MAX_VALUE) ItemFlag... flags) {
        for (ItemFlag flag : flags) {
            hideFlag0(flag);
        }

        return this;
    }

    /**
     * Shows item flags for the item.
     *
     * @param flags - Flags to show.
     */
    public ItemBuilder showFlag(@Nonnull @Range(from = 1, to = Byte.MAX_VALUE) ItemFlag... flags) {
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
        return hideFlag(ItemFlag.values());
    }

    /**
     * Hides default attribute from tools.
     * <br>
     * <br>
     * Because mojang reworked ItemStacks, and to hide attributes it requires
     * the item to have attributes modifiers, it means we cannot hide default
     * attributes for tools, armor, etc. without adding a modifier.
     * <p>
     * Spigot has a workaround by adding a dummy modifier, just like you can
     * do in vanilla, but Paper is annoying and refuses to keep the workaround
     * or giving the ability to add empty modifiers.
     * <p>
     * This is why we need to check for default attributes and add a wacky
     * dummy attribute modifier.
     */
    public ItemBuilder hideDefaultAttributesIfApplicable() {
        if (!isItemHasDefaultAttributes(getType())) {
            return this;
        }

        // Store here to not accidentally change it and break the system
        final Attribute dummyAttribute = Attribute.GENERIC_LUCK;

        return modifyMeta(meta -> {
            // Make sure the flag is actually applied
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            // Add dummy modifier
            final AttributeModifier modifier = getHideFlagsAttributeModifier();
            final Collection<AttributeModifier> modifiers = meta.getAttributeModifiers(dummyAttribute);

            // Don't re-add modifier since it throws an error
            if (modifiers != null && modifiers.contains(modifier)) {
                return;
            }

            meta.addAttributeModifier(dummyAttribute, modifier);
        });
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
     * Gets the {@link ItemStack} of this {@link ItemBuilder}.
     *
     * @return the item stack of this builder.
     */
    @Nonnull
    public ItemStack toItemStack() {
        return item;
    }

    /**
     * Clears Item flags, name, lore and makes it unbreakable before returning ItemStack.
     *
     * @return cleaned ItemStack.
     */
    @Nonnull
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
    @Nonnull
    public ItemStack asIcon() {
        hideFlags();
        return build();
    }

    /**
     * Finalizes item meta and returns an ItemStack.
     *
     * @param overrideFunctions - if true and the {@link ItemFunctionList} is already registered with this {@link ItemBuilder} {@link #key}, it will override it.
     * @return finalized ItemStack.
     */
    @Nonnull
    public ItemStack build(boolean overrideFunctions) {
        if (!key.isEmpty()) {
            // Always store Id
            modifyMeta(meta -> NBT.setValue(meta, PLUGIN_PATH, NBTType.STR, key.getKey()));

            if (functions != null && (!isIdRegistered(key) || overrideFunctions)) {
                registeredFunctions.put(key, functions);
            }
        }

        return item;
    }

    /**
     * Finalizes item meta and returns an ItemStack.
     *
     * @return finalized ItemStack.
     */
    @Nonnull
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
        return modifyMeta(meta -> meta.setDisplayName(DEFAULT_NAME_COLOR + Chat.format(name)));
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

    @Nonnull
    public ItemMeta getMetaOrThrow() {
        // how in the world item meta can be null?
        final ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            throw new NullPointerException("ItemMeta is null somehow!");
        }

        return meta;
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

        return meta != null && meta.isUnbreakable();
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
     * Sets the texture of this head from a base64 encoded string.
     * <p>
     * If copying from <a href="https://minecraft-heads.com/custom-heads">Minecraft-Heads</a>, use <code>Other -> Value</code> value,
     * which should look something like this:
     * <code>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODdmYmU5M2E5YzUxODg4MzE4NmEyYWVmYmQyYjdkZjE4YmEwNWE4YTdkZDFhNzU2ZWY2NTY1ZDNlNTgwN2EwYyJ9fX0=</code>
     */
    public ItemBuilder setHeadTextureBase64(@Nonnull String base64) {
        return setHeadTextureUrl(decodeBase64(base64));
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
     * Gets the key of this item.
     *
     * @return the key of this item.
     */
    @Nonnull
    @Override
    public Key getKey() {
        return key;
    }

    /**
     * Sets the key of this item.
     * <p>Keys are used for function identification.</p>
     *
     * @param key - New key.
     */
    public ItemBuilder setKey(@Nonnull Key key) {
        this.key = key;
        return this;
    }

    /**
     * Sets the key of this item from the given string.
     * <p>Keys are used for function identification.</p>
     *
     * @param string - String to convert to the key.
     * @throws IllegalArgumentException if the given string does not match the {@link Key#PATTERN}.
     */
    public ItemBuilder setKey(@Nonnull String string) {
        return setKey(Key.ofString(string));
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
            EternaLogger.severe("An error occurred in ItemBuilder, report this! " + e.getMessage());
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
     */
    public ItemBuilder glow() {
        return setEnchantmentGlintOverride(true);
    }

    /**
     * Sets whenever this item will have enchantment glint, regardless of if it has enchantments or glint by default.
     *
     * @param glow - Should glow.
     */
    public ItemBuilder setEnchantmentGlintOverride(boolean glow) {
        return modifyMeta(meta -> meta.setEnchantmentGlintOverride(glow));
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
     * Sets the custom model data for this item.
     *
     * @param data - Data.
     */
    public ItemBuilder setCustomModelData(int data) {
        return modifyMeta(meta -> meta.setCustomModelData(data));
    }

    /**
     * Completely hides the tooltip of the item while hovering over it.
     *
     * @param hide - Is tooltip hidden.
     */
    public ItemBuilder setHideTooltip(boolean hide) {
        return modifyMeta(meta -> meta.setHideTooltip(hide));
    }

    /**
     * Sets if this item is fire-resistant, like netherite items.
     *
     * @param fireResistant - Is fire-resistant.
     */
    public ItemBuilder setFireResistant(boolean fireResistant) {
        return modifyMeta(meta -> meta.setFireResistant(fireResistant));
    }

    /**
     * Sets the maximum stack size of this item. Must be between 1-99 (inclusive).
     *
     * @param maximumStackSize - Maximum stack size of this item.
     */
    public ItemBuilder setMaximumStackSize(int maximumStackSize) {
        return modifyMeta(meta -> meta.setMaxStackSize(maximumStackSize));
    }

    /**
     * Modifies the {@link FoodComponent} for this item.
     *
     * @param modifier - Modifier.
     * @see FoodComponent
     * @see FoodComponent.FoodEffect
     */
    public ItemBuilder setFood(@Nonnull ComponentModifier<FoodComponent> modifier) {
        return modifyComponent(modifier, ItemMeta::getFood, ItemMeta::setFood);
    }

    /**
     * Modifies the {@link ToolComponent} for this item.
     *
     * @param modifier - Modifier.
     * @see ToolComponent
     * @see ToolComponent.ToolRule
     */
    public ItemBuilder setTool(@Nonnull ComponentModifier<ToolComponent> modifier) {
        return modifyComponent(modifier, ItemMeta::getTool, ItemMeta::setTool);
    }

    /**
     * Modifies the {@link JukeboxPlayableComponent} for this item.
     *
     * @param modifier - Modifier.
     * @see JukeboxPlayableComponent
     */
    public ItemBuilder setJukebox(@Nonnull ComponentModifier<JukeboxPlayableComponent> modifier) {
        return modifyComponent(modifier, ItemMeta::getJukeboxPlayable, ItemMeta::setJukeboxPlayable);
    }

    /**
     * Clears all the banner {@link Pattern}s.
     */
    public ItemBuilder clearBannerPatterns() {
        return modifyMeta(BannerMeta.class, meta -> {
            for (int i = 0; i < meta.numberOfPatterns(); i++) {
                meta.removePattern(i);
            }
        });
    }

    /**
     * Adds a banner {@link Pattern}.
     *
     * @param type  - Pattern type.
     * @param color - Pattern color.
     */
    public ItemBuilder addBannerPattern(@Nonnull PatternType type, @Nonnull DyeColor color) {
        return modifyMeta(BannerMeta.class, Material.WHITE_BANNER, meta -> {
            meta.addPattern(new Pattern(color, type));
        });
    }

    /**
     * Sets the banner {@link Pattern}s.
     *
     * @param patterns - Patterns to set.
     */
    public ItemBuilder setBannerPattern(@Nonnull Pattern... patterns) {
        return modifyMeta(BannerMeta.class, Material.WHITE_BANNER, meta -> {
            meta.setPatterns(Arrays.asList(patterns));
        });
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
     * Creates a copy of this builder.
     *
     * @return a copy of this builder.
     */
    @Override
    @Nonnull
    public ItemBuilder clone() {
        try {
            final ItemBuilder cloned = (ItemBuilder) super.clone();

            cloned.item = new ItemStack(item);
            cloned.functions = functions.clone();

            return cloned;
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private void hideFlag0(ItemFlag flag) {
        modifyMeta(meta -> meta.addItemFlags(flag));

        if (flag == ItemFlag.HIDE_ATTRIBUTES) {
            hideDefaultAttributesIfApplicable();
        }
    }

    private <E> ItemBuilder modifyComponent(ComponentModifier<E> modifier, Function<ItemMeta, E> get, BiConsumer<ItemMeta, E> set) {
        modifyMeta(meta -> {
            final E component = get.apply(meta);

            modifier.modify(component);
            set.accept(meta, component);
        });

        return this;
    }

    /**
     * Returns true if {@link Material} has default attributes without any modifiers.
     * <br>
     * Eg: Tools, Armor, etc.
     *
     * @param material - Material.
     * @return true if material has default attributes without any modifiers, false otherwise.
     */
    public static boolean isItemHasDefaultAttributes(@Nonnull Material material) {
        return switch (material) {
            case WOODEN_SHOVEL, WOODEN_PICKAXE, WOODEN_AXE, WOODEN_HOE, WOODEN_SWORD -> true;
            case STONE_SHOVEL, STONE_PICKAXE, STONE_AXE, STONE_HOE, STONE_SWORD -> true;
            case IRON_SHOVEL, IRON_PICKAXE, IRON_AXE, IRON_HOE, IRON_SWORD -> true;
            case GOLDEN_SHOVEL, GOLDEN_PICKAXE, GOLDEN_AXE, GOLDEN_HOE, GOLDEN_SWORD -> true;
            case DIAMOND_SHOVEL, DIAMOND_PICKAXE, DIAMOND_AXE, DIAMOND_HOE, DIAMOND_SWORD -> true;
            case NETHERITE_SHOVEL, NETHERITE_PICKAXE, NETHERITE_AXE, NETHERITE_HOE, NETHERITE_SWORD -> true;
            case TRIDENT -> true;
            case MACE -> true;
            case LEATHER_HELMET, LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_BOOTS -> true;
            case IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS -> true;
            case GOLDEN_HELMET, GOLDEN_CHESTPLATE, GOLDEN_LEGGINGS, GOLDEN_BOOTS -> true;
            case DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS -> true;
            case NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS -> true;
            case TURTLE_HELMET -> true;
            case LEATHER_HORSE_ARMOR, IRON_HORSE_ARMOR, GOLDEN_HORSE_ARMOR, DIAMOND_HORSE_ARMOR -> true;
            case WOLF_ARMOR -> true;
            default -> false;
        };
    }

    @Nonnull
    public static EquipmentSlotGroup equipmentSlotGroupFromSlot(@Nullable EquipmentSlot slot) {
        if (slot == null) {
            return EquipmentSlotGroup.ANY;
        }

        return switch (slot) {
            case HAND -> EquipmentSlotGroup.MAINHAND;
            case OFF_HAND -> EquipmentSlotGroup.OFFHAND;
            case FEET -> EquipmentSlotGroup.FEET;
            case CHEST -> EquipmentSlotGroup.CHEST;
            case HEAD -> EquipmentSlotGroup.HEAD;
            case LEGS -> EquipmentSlotGroup.LEGS;
            case BODY -> EquipmentSlotGroup.BODY;
        };
    }

    // *=* Static members *=* //

    /**
     * Creates builder of provided ItemStack.
     *
     * @param itemStack - ItemStack.
     */
    @Nonnull
    public static ItemBuilder of(@Nonnull ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    /**
     * Creates builder of provided material.
     *
     * @param material - Material to use.
     */
    @Nonnull
    public static ItemBuilder of(@Nonnull Material material) {
        return new ItemBuilder(material);
    }

    /**
     * Creates builder of provided material with provided name.
     *
     * @param material - Material to use.
     * @param name     - Name to use.
     */
    @Nonnull
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
    @Nonnull
    public static ItemBuilder of(@Nonnull Material material, @Nonnull String name, @Nonnull @Range(from = 1, to = Byte.MAX_VALUE) String... lore) {
        final ItemBuilder builder = new ItemBuilder(material).setName(name);

        for (String str : lore) {
            builder.addLore(str);
        }

        return builder;
    }

    /**
     * Creates player head from texture.
     *
     * @param texture - Texture to use.
     * @deprecated {@link ItemBuilder#playerHeadUrl(String)}
     */
    @Deprecated(forRemoval = true)
    public static ItemBuilder playerHead(@Nonnull String texture) {
        return new ItemBuilder(Material.PLAYER_HEAD).setHeadTextureBase64(texture);
    }

    /**
     * Creates player head from texture from url.
     *
     * @param url - Url to texture.
     */
    @Nonnull
    public static ItemBuilder playerHeadUrl(@Nonnull String url) {
        return new ItemBuilder(Material.PLAYER_HEAD).setHeadTextureUrl(url);
    }

    /**
     * Creates leather helmet with provided color.
     *
     * @param color - Color to use.
     */
    @Nonnull
    public static ItemBuilder leatherHat(@Nullable Color color) {
        return new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(color);
    }

    /**
     * Creates leather chestplate with provided color.
     *
     * @param color - Color to use.
     */
    @Nonnull
    public static ItemBuilder leatherTunic(@Nullable Color color) {
        return new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(color);
    }

    /**
     * Creates leather leggings with provided color.
     *
     * @param color - Color to use.
     */
    @Nonnull
    public static ItemBuilder leatherPants(@Nullable Color color) {
        return new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(color);
    }

    /**
     * Creates leather boots with provided color.
     *
     * @param color - Color to use.
     */
    @Nonnull
    public static ItemBuilder leatherBoots(@Nullable Color color) {
        return new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(color);
    }

    /**
     * Creates air builder.
     */
    @Nonnull
    public static ItemBuilder air() {
        return of(Material.AIR);
    }

    /**
     * Gets the {@link ItemFunctionList} by the given Id; or null if none.
     *
     * @param key - Key.
     * @return the {@link ItemFunctionList} by the given Id; or null if none.
     */
    @Nullable
    public static ItemFunctionList getFunctionListByKey(@Nonnull Key key) {
        return registeredFunctions.get(key);
    }

    /**
     * Gets a copy of registered {@link ItemFunctionList} Ids.
     *
     * @return a copy of registered {@link ItemFunctionList} Ids.
     */
    @Nonnull
    public static Set<Key> getRegisteredKeys() {
        return Sets.newHashSet(registeredFunctions.keySet());
    }

    /**
     * Gets the key of the given item, or {@link Key#empty()} key if there is no id.
     *
     * @param item - Item to get the key from.
     * @return the key of the given item, or {@link Key#empty()} key if there is no id.
     */
    @Nonnull
    public static Key getItemKey(@Nonnull ItemStack item) {
        final ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return Key.empty();
        }

        final String itemKey = NBT.getValue(meta, PLUGIN_PATH, NBTType.STR);
        return itemKey != null ? Key.ofString(itemKey) : Key.empty();
    }

    /**
     * Returns {@code true} if the given item matches the given key, {@code false} otherwise.
     *
     * @param item - Item to check.
     * @param key  - Key to compare to.
     * @return {@code true} if the given item matches the given key, {@code false} otherwise.
     */
    public static boolean compareItemKey(@Nonnull ItemStack item, @Nonnull Key key) {
        return getItemKey(item).equals(key);
    }

    /**
     * Returns {@code true} if the given item has a non-empty {@link Key}, {@code false} otherwise.
     *
     * @param item - Item to check.
     * @return {@code true} if the given item has a non-empty {@link Key}, {@code false} otherwise.
     */
    public static boolean isItemKeyed(@Nonnull ItemStack item) {
        return !getItemKey(item).isEmpty();
    }

    /**
     * Performs a string wrap.
     *
     * @param prefix - Prefix of each new line.
     * @param string - String to wrap.
     * @param limit  - Character limit.
     * @return a list of strings that are less or equals to the limit.
     */
    @Nonnull
    public static List<String> splitString(@Nullable String prefix, @Nonnull String string, int limit) {
        // If the string is less than the limit, return it
        if (calculateStringLengthSkipColors(string) <= limit) {
            return List.of(Chat.format((prefix != null ? prefix : DEFAULT_COLOR) + string));
        }

        final List<String> list = new ArrayList<>();
        final char[] chars = (string + " ").toCharArray();

        StringBuilder lastColor = new StringBuilder(prefix == null ? DEFAULT_COLOR.toString() : prefix);
        StringBuilder builder = new StringBuilder(lastColor);
        int counter = 0;

        for (int i = 0; i < chars.length; i++) {
            final char c = chars[i];
            final boolean nextCharInRange = i + 1 < chars.length;
            final boolean isManualSplit = isManualSplit(chars, i);

            // If out of limit and hit whitespace, then add line.
            final boolean lastChar = i >= chars.length - 1;
            if (lastChar || (counter >= limit && Character.isWhitespace(c)) || isManualSplit) {
                if (isManualSplit) {
                    i++;
                }

                // Don't eat the last char.
                if (lastChar) {
                    builder.append(c);
                }

                list.add(Chat.format(builder.toString().trim()));

                counter = 0;
                builder = new StringBuilder(lastColor);
                lastColor = new StringBuilder(prefix == null ? DEFAULT_COLOR.toString() : prefix);
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

    public static int calculateStringLengthSkipColors(@Nonnull String string) {
        final char[] chars = string.toCharArray();
        int count = 0;

        for (int i = 0; i < chars.length; i++) {
            final char c = chars[i];

            if (isColorCode(c) && (i + 1 < chars.length && isColorChar(chars[i + 1]))) {
                continue;
            }

            count++;
        }

        return count;
    }

    @Deprecated
    public static List<String> splitAfter(@Nonnull String text, int max) {
        return splitAfter(DEFAULT_COLOR.toString(), text, max);
    }

    @Deprecated
    public static List<String> splitAfter(@Nonnull String text, int max, @Nonnull String prefix) {
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
    @Nonnull
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
    @Nonnull
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
    @Nonnull
    public static String getLastColor(@Nonnull String string, String def) {
        final List<String> lastColors = getLastColors(string);

        return lastColors.isEmpty() ? def : lastColors.get(lastColors.size() - 1);
    }

    /**
     * Sets the item name.
     *
     * @param item - Item to set name to.
     * @param name - Name to set.
     */
    public static void setName(@Nonnull ItemStack item, @Nonnull String name) {
        final ItemMeta meta = item.getItemMeta();
        Nulls.runIfNotNull(meta, m -> m.setDisplayName(Chat.format(name)));
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

    @SuppressWarnings("UnstableApiUsage")
    @Nonnull
    public static AttributeModifier getHideFlagsAttributeModifier() {
        if (HIDE_ATTRIBUTES_MODIFIER == null) {
            HIDE_ATTRIBUTES_MODIFIER = new AttributeModifier(
                    BukkitUtils.createKey("hide_attributes"),
                    0,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlotGroup.FEET
            );
        }

        return HIDE_ATTRIBUTES_MODIFIER;
    }

    /**
     * Decodes the base64 texture into a {@link #URL_TEXTURE_LINK} link.
     *
     * @param texture64 - Base64.
     * @return the decoded link without {@link #URL_TEXTURE_LINK}.
     * @throws IllegalArgumentException - If the base64 is invalid.
     */
    @Nonnull
    public static String decodeBase64(@Nonnull String texture64) {
        final String decodedTexture = new String(Base64.getDecoder().decode(texture64));
        final Matcher matcher = BASE64_DECODE_PATTERN.matcher(decodedTexture);

        if (matcher.find()) {
            final String texture = matcher.group(1);

            return texture.replace(URL_TEXTURE_LINK, "");
        }

        throw new IllegalArgumentException("Invalid texture!");
    }

    private static boolean isManualSplit(char[] chars, int index) {
        return (index < chars.length && index + 1 < chars.length)
                && (chars[index] == MANUAL_SPLIT_CHAR && chars[index + 1] == MANUAL_SPLIT_CHAR);
    }

    private static boolean isIdRegistered(@Nonnull Key key) {
        return registeredFunctions.containsKey(key);
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

                list.add(Chat.format(linePrefix + line.substring(0, line.length() - 1).trim()));
                line = "";
                counter = 0;
                i++;
                continue;
            }

            if (counter >= maxChars || i == text.length() - 1) {
                if (Character.isWhitespace(c) || checkLast) {
                    list.add(Chat.format(linePrefix + line.trim()));
                    line = "";
                    counter = 0;
                }
            }
        }

        return list;
    }

    public interface ComponentModifier<E> {

        void modify(E e);

    }

}