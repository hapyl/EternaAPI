package me.hapyl.spigotutils.module.inventory;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.annotate.Super;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.math.Numbers;
import me.hapyl.spigotutils.module.nbt.LazyType;
import me.hapyl.spigotutils.module.nbt.NBT;
import me.hapyl.spigotutils.module.nbt.nms.NBTNative;
import me.hapyl.spigotutils.module.util.Nulls;
import me.hapyl.spigotutils.module.util.Validate;
import net.md_5.bungee.api.chat.BaseComponent;
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
import org.bukkit.map.MapView;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.regex.PatternSyntaxException;

/**
 * Build ItemStack easier. Add names, lore, smart lore, enchants and even click events!
 */
public class ItemBuilder implements ItemStackBuilder {

    private static final String PLUGIN_PATH = "ItemBuilderId";
    private final static String URL_TEXTURE_FORMAT = "{textures: {SKIN: {url: \"%s\"}}}";
    private final static String URL_TEXTURE_LINK = "https://textures.minecraft.net/texture/";
    protected static Map<String, ItemBuilder> itemsWithEvents = new HashMap<>();
    private final String id;
    private final Set<ItemAction> functions;
    private ItemStack item;
    private ItemMeta meta;
    private int cd;
    private Predicate<Player> predicate;
    private String error;
    private boolean allowInventoryClick;
    private boolean cancelClicks;
    private String nativeNbt;
    private ItemEventHandler handler;

    /**
     * Create new ItemBuilder instance.
     *
     * @param material - Material of the builder.
     */
    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    /**
     * Create new ItemBuilder instance.
     *
     * @param stack - ItemStack of the builder. Data is cloned.
     */
    public ItemBuilder(ItemStack stack) {
        this(stack.clone(), null);
    }

    /**
     * Create new ItemBuilder instance with ID for events.
     *
     * @param material - Material of the builder.
     * @param id       - ID of the builder. Must be unique and will be force to lower case.
     */
    public ItemBuilder(Material material, String id) {
        this(new ItemStack(material), id);
    }

    /**
     * Create new ItemBuilder instance with ID for events.
     *
     * @param stack - ItemStack of the builder. Data is cloned.
     * @param id    - ID of the builder. Must be unique and will be force to lower case.
     */
    @Super
    public ItemBuilder(ItemStack stack, String id) {
        this.item = stack;
        this.meta = stack.getItemMeta();
        this.id = id;
        this.functions = new HashSet<>();
        this.allowInventoryClick = false;
        this.cancelClicks = true;
        this.nativeNbt = "";
        this.handler = ItemEventHandler.EMPTY;
    }

    @Override
    @Nonnull
    public ItemEventHandler getEventHandler() {
        return handler;
    }

    @Override
    public final ItemBuilder setEventHandler(@Nonnull ItemEventHandler handler) {
        Validate.notNull(handler, "event handler cannot be null");
        this.handler = handler;
        return this;
    }

    @Override
    @Nonnull
    public String getNbt() {
        return nativeNbt;
    }

    @Override
    public void setNbt(@Nullable String nbt) {
        if (nbt == null) {
            this.nativeNbt = "";
            return;
        }
        this.nativeNbt = nbt;
    }

    @Override
    public ItemBuilder setItemMeta(ItemMeta meta) {
        this.meta = meta;
        return this;
    }

    /**
     * Performs a predicate and applying action if predicate returned true, does nothing otherwise.
     *
     * @param predicate - Predicate.
     * @param action    - Action to perform if predicate returned true.
     */
    public ItemBuilder predicate(boolean predicate, Consumer<ItemBuilder> action) {
        if (predicate) {
            action.accept(this);
        }
        return this;
    }

    @Override
    @Nullable
    public ItemBuilder clone() {
        try {
            final ItemBuilder clone = (ItemBuilder) super.clone();
            if (!this.id.isEmpty()) {
                throw new UnsupportedOperationException("Clone does not support ID's!");
            }
            return new ItemBuilder(this.item).setItemMeta(this.meta);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean isAllowInventoryClick() {
        return allowInventoryClick;
    }

    @Override
    public ItemBuilder setAllowInventoryClick(boolean allowInventoryClick) {
        this.allowInventoryClick = allowInventoryClick;
        return this;
    }

    @Override
    public ItemBuilder setMapView(MapView view) {
        setType(Material.FILLED_MAP);

        final MapMeta meta = (MapMeta) this.meta;
        meta.setMapView(view);
        this.item.setItemMeta(meta);
        return this;
    }

    @Override
    public ItemBuilder setBookName(String name) {
        setType(Material.WRITTEN_BOOK);

        final BookMeta bookMeta = (BookMeta) this.meta;
        bookMeta.setDisplayName(colorize(name));
        this.item.setItemMeta(bookMeta);
        return this;
    }

    @Override
    public ItemBuilder setBookAuthor(String author) {
        this.validateBookMeta();
        final BookMeta bookMeta = (BookMeta) this.meta;
        bookMeta.setAuthor(colorize(author));
        this.item.setItemMeta(bookMeta);
        return this;
    }

    @Override
    public ItemBuilder setBookTitle(String title) {
        this.validateBookMeta();
        final BookMeta bookMeta = (BookMeta) this.meta;
        bookMeta.setTitle(colorize(title));
        this.item.setItemMeta(bookMeta);
        return this;
    }

    @Override
    public ItemBuilder setBookPages(List<String> pages) {
        this.validateBookMeta();
        final BookMeta bookMeta = (BookMeta) this.meta;
        bookMeta.setPages(pages);
        this.item.setItemMeta(bookMeta);
        return this;
    }

    @Override
    public ItemBuilder setBookPages(BaseComponent[]... base) {
        this.validateBookMeta();
        final BookMeta meta = (BookMeta) this.meta;
        meta.spigot().setPages(base);
        this.item.setItemMeta(meta);
        return this;
    }

    @Override
    public ItemBuilder setBookPage(int page, BaseComponent[] base) {
        this.validateBookMeta();
        final BookMeta meta = (BookMeta) this.meta;
        meta.spigot().setPage(page, base);
        this.item.setItemMeta(meta);
        return this;
    }

    @Override
    public ItemBuilder withCooldown(int ticks) {
        withCooldown(ticks, null);
        return this;
    }

    @Override
    public ItemBuilder withCooldown(int ticks, Predicate<Player> predicate) {
        withCooldown(ticks, predicate, "&cCannot use that!");
        return this;
    }

    @Override
    public ItemBuilder withCooldown(int ticks, Predicate<Player> predicate, String errorMessage) {
        this.predicate = predicate;
        this.cd = ticks;
        this.error = errorMessage;
        return this;
    }

    @Override
    public ItemBuilder removeClickEvent() {
        this.functions.clear();
        return this;
    }

    @Override
    public ItemBuilder addClickEvent(Consumer<Player> consumer, Action... act) {
        if (act.length < 1) {
            throw new IndexOutOfBoundsException("This requires at least 1 action.");
        }
        this.functions.add(new ItemAction(consumer, act));
        return this;
    }

    @Override
    public ItemBuilder addClickEvent(Consumer<Player> consumer) {
        this.addClickEvent(consumer, Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR);
        return this;
    }

    @Override
    public ItemBuilder setPersistentData(String path, Object value) {
        if (value instanceof String) {
            this.setPersistentData(path, PersistentDataType.STRING, (String) value);
        }
        if (value instanceof Byte) {
            this.setPersistentData(path, PersistentDataType.BYTE, (byte) value);
        }
        if (value instanceof Short) {
            this.setPersistentData(path, PersistentDataType.SHORT, (short) value);
        }
        if (value instanceof Integer) {
            this.setPersistentData(path, PersistentDataType.INTEGER, (int) value);
        }
        if (value instanceof Long) {
            this.setPersistentData(path, PersistentDataType.LONG, (long) value);
        }
        if (value instanceof Float) {
            this.setPersistentData(path, PersistentDataType.FLOAT, (float) value);
        }
        if (value instanceof Double) {
            this.setPersistentData(path, PersistentDataType.DOUBLE, (double) value);
        }
        return this;
    }

    @Override
    public <T> T getNbt(String path, PersistentDataType<T, T> value) {
        return this.getPersistentData(path, value);
    }

    @Override
    public ItemBuilder setSmartLore(String lore) {
        this.meta.setLore(splitString(lore, 30));
        return this;
    }

    @Override
    public ItemBuilder setSmartLore(String lore, final int limit) {
        this.meta.setLore(splitString(lore, limit));
        return this;
    }

    @Override
    public ItemBuilder addSmartLore(String lore, final int limit) {
        this.addSmartLore(lore, "&7", limit);
        return this;
    }

    @Override
    public ItemBuilder addSmartLore(String lore, String prefixText) {
        this.addSmartLore(lore, prefixText, 30);
        return this;
    }

    @Override
    public ItemBuilder addSmartLore(String lore) {
        addSmartLore(lore, 30);
        return this;
    }

    @Override
    public ItemBuilder setSmartLore(String lore, String prefix) {
        this.setSmartLore(lore, prefix, 30);
        return this;
    }

    @Override
    public ItemBuilder setSmartLore(String lore, String prefix, int splitAfter) {
        this.meta.setLore(splitString(prefix, lore, splitAfter));
        return this;
    }

    @Override
    public ItemBuilder addSmartLore(String lore, String prefix, int splitAfter) {
        List<String> metaLore = this.meta.getLore() != null ? this.meta.getLore() : Lists.newArrayList();
        metaLore.addAll(splitString(prefix, lore, splitAfter));
        this.meta.setLore(metaLore);
        return this;
    }

    @Override
    public ItemBuilder setLore(int line, String lore) {
        List<String> oldLore = this.meta.getLore() == null ? Lists.newArrayList() : this.meta.getLore();
        oldLore.set(line, colorize(lore));
        this.meta.setLore(oldLore);
        return this;
    }

    @Override
    public ItemBuilder addLore(final String lore, ChatColor afterSplitColor) {
        List<String> metaLore = this.meta.getLore() != null ? this.meta.getLore() : Lists.newArrayList();
        for (String value : lore.split("__")) {
            metaLore.add(afterSplitColor + colorize(value));
        }
        this.meta.setLore(metaLore);
        return this;
    }

    @Override
    public ItemBuilder addLore(final String lore) {
        return this.addLore(lore, ChatColor.GRAY);
    }

    @Override
    public ItemBuilder addLore(final String lore, final Object... replacements) {
        this.addLore(Chat.format(lore, replacements));
        return this;
    }

    @Override
    public ItemBuilder addLoreIf(final String lore, final boolean condition) {
        this.addLoreIf(lore, condition, "");
        return this;
    }

    @Override
    public ItemBuilder addLoreIf(final String lore, final boolean condition, final Object... replacements) {
        if (condition) {
            this.addLore(lore, replacements);
        }
        return this;
    }

    @Override
    public ItemBuilder addLore() {
        return this.addLore("");
    }

    @Override
    public ItemBuilder setLore(final String lore, final String separator) {
        try {
            this.meta.setLore(Arrays.asList(colorize(lore).split(separator)));
        } catch (PatternSyntaxException ex) {
            Bukkit.getConsoleSender()
                    .sendMessage(colorize("&4[ERROR] &cChar &e" + separator + " &cused as separator for lore!"));
        }
        return this;
    }

    @Override
    public ItemBuilder removeLore() {
        if (this.meta.getLore() != null) {
            this.meta.setLore(null);
        }
        return this;
    }

    @Override
    public ItemBuilder removeLoreLine(int line) {
        if (this.meta.getLore() == null) {
            throw new NullPointerException("ItemMeta doesn't have any lore!");
        }
        if (line > this.meta.getLore().size()) {
            throw new IndexOutOfBoundsException(
                    "ItemMeta has only " + this.meta.getLore().size() + " lines! Given " + line);
        }
        List<String> old = this.meta.getLore();
        old.remove(line);
        this.meta.setLore(old);
        return this;

    }

    @Override
    public ItemBuilder applyDefaultSettings() {
        return applyDefaultSettings(true);
    }

    @Override
    public ItemBuilder applyDefaultSettings(boolean applyCurse) {
        if (applyCurse) {
            this.meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
        }
        this.meta.setUnbreakable(true);
        this.meta.addItemFlags(ItemFlag.values());
        return this;
    }

    @Override
    public ItemBuilder setName(String name, Object... replacements) {
        this.setName(Chat.format(name, replacements));
        return this;
    }

    @Override
    public ItemBuilder addEnchant(Enchant enchant, int lvl) {
        return addEnchant(enchant.getAsBukkit(), lvl);
    }

    @Override
    public ItemBuilder addEnchant(Enchantment enchantment, int lvl) {
        this.meta.addEnchant(enchantment, lvl, true);
        return this;
    }

    @Override
    public ItemBuilder setUnbreakable() {
        this.meta.setUnbreakable(true);
        return this;
    }

    @Override
    public ItemBuilder setPotionMeta(PotionEffectType type, int lvl, int duration, Color color) {
        switch (this.item.getType()) {
            case POTION, SPLASH_POTION, LINGERING_POTION, TIPPED_ARROW -> {
                PotionMeta meta = (PotionMeta) this.meta;
                meta.addCustomEffect(new PotionEffect(type, duration, lvl), false);
                meta.setColor(color);
            }
        }
        return this;
    }

    @Override
    public ItemBuilder setPotionColor(Color color) {
        this.validatePotionMeta();
        final PotionMeta meta = (PotionMeta) this.meta;
        meta.setColor(color);
        this.item.setItemMeta(meta);
        return this;
    }

    @Override
    public ItemBuilder setLeatherArmorColor(Color color) {
        final Material type = this.item.getType();
        switch (type) {
            case LEATHER_BOOTS, LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_HELMET -> {
                LeatherArmorMeta meta = (LeatherArmorMeta) this.meta;
                meta.setColor(color);
                this.item.setItemMeta(meta);
                return this;
            }
            default -> throw new IllegalStateException(
                    "Cannot apply leather armor meta! Material must be LEATHER_BOOTS, LEATHER_CHESTPLATE, LEATHER_LEGGINGS or LEATHER_HELMET!"
                    //"Material must be LEATHER_BOOTS, LEATHER_CHESTPLATE, LEATHER_LEGGINGS or LEATHER_HELMET to use this!"
            );
        }
    }

    @Override
    public ItemBuilder setHeadTexture(UUID uuid, String name) {
        final SkullMeta skullMeta = (SkullMeta) this.meta;
        skullMeta.setOwnerProfile(Bukkit.createPlayerProfile(uuid, name));
        return this;
    }

    @Override
    public ItemBuilder setHeadTextureUrl(String url) {
        url = URL_TEXTURE_FORMAT.formatted(url.contains(URL_TEXTURE_LINK) ? url : URL_TEXTURE_LINK + url);
        setHeadTexture(new String(Base64.getEncoder().encode(url.getBytes(StandardCharsets.UTF_8))));
        return this;
    }

    @Override
    public ItemBuilder setSkullOwner(String owner) {
        if (this.item.getType() == Material.PLAYER_HEAD) {
            SkullMeta meta = (SkullMeta) this.meta;
            meta.setOwner(owner);
            return this;
        }
        return this;
    }

    @Override
    public ItemBuilder addAttribute(Attribute attribute, double amount, AttributeModifier.Operation operation, EquipmentSlot slot) {
        this.meta.addAttributeModifier(
                attribute,
                new AttributeModifier(UUID.randomUUID(), attribute.toString(), amount, operation, slot)
        );
        return this;
    }

    @Override
    public ItemBuilder hideFlag(ItemFlag... flags) {
        this.meta.addItemFlags(flags);
        return this;
    }

    @Override
    public ItemBuilder showFlag(ItemFlag... flags) {
        this.meta.removeItemFlags(flags);
        return this;
    }

    @Override
    public ItemBuilder showFlags() {
        this.meta.removeItemFlags(ItemFlag.values());
        return this;
    }

    @Override
    public ItemBuilder hideFlags() {
        this.meta.addItemFlags(ItemFlag.values());
        return this;
    }

    @Override
    public ItemBuilder clearName() {
        this.meta.setDisplayName("");
        return this;
    }

    @Override
    public ItemBuilder setDurability(int durability) {
        Damageable meta = (Damageable) this.meta;
        meta.setDamage(durability);
        return this;
    }

    @Override
    public Material getType() {
        return item.getType();
    }

    @Override
    public ItemBuilder setType(Material material) {
        this.item.setType(material);
        return this;
    }

    @Override
    public ItemStack toItemStack() {
        this.item.setItemMeta(this.meta);
        return this.item;
    }

    @Override
    public ItemStack cleanToItemSack() {
        this.hideFlags();
        this.setName("&0");
        this.removeLore();
        this.setUnbreakable(true);
        return this.toItemStack();
    }

    @Override
    public ItemStack asIcon() {
        hideFlags();
        return build();
    }

    @Override
    public ItemStack build(boolean overrideIfExists) {
        if (this.id != null) {
            if (isIdRegistered(this.id) && !overrideIfExists) {
                sendErrorMessage(
                        "Could not build ItemBuilder! ID \"%s\" is already registered. Use \"toItemStack\" if you wish to clone it or \"build(true)\" to override existing item!",
                        this.getItem().getType()
                );
                return item;
            }
            NBT.setValue(this.meta, PLUGIN_PATH, LazyType.STR, id);
            itemsWithEvents.put(this.id, this);
        }

        else if (!this.functions.isEmpty()) {
            sendErrorMessage(
                    "Could not build ItemBuilder! ID is required to add click events. \"new ItemBuilder(%s, ID)\"",
                    this.getItem().getType()
            );
            return item;
        }

        this.item.setItemMeta(this.meta);

        // Apply native NBT
        if (!nativeNbt.isBlank()) {
            this.item = NBTNative.setNbt(this.item, nativeNbt);
        }

        return item;
    }

    @Override
    public ItemStack build() {
        return this.build(false);
    }

    @Override
    @Nonnull
    public String getName() {
        return this.meta.getDisplayName();
    }

    @Override
    public ItemBuilder setName(String name) {
        this.meta.setDisplayName(ChatColor.GREEN + colorize(name));
        return this;
    }

    @Override
    @Nullable
    public List<String> getLore() {
        return this.meta.getLore();
    }

    @Override
    public ItemBuilder setLore(List<String> lore) {
        this.meta.setLore(lore);
        return this;
    }

    @Override
    public ItemBuilder setLore(String lore) {
        this.setLore(lore, "__");
        return this;
    }

    @Override
    @Nullable
    public List<String> getLore(int start, int end) {
        final List<String> hash = new ArrayList<>();
        final List<String> lore = this.getLore();

        if (lore == null) {
            return hash;
        }

        if (end > lore.size()) {
            throw new IndexOutOfBoundsException("There is either no lore or given more that there is lines.");
        }

        for (int i = start; i < end; i++) {
            hash.add(lore.get(i));
        }
        return hash;
    }

    @Override
    public ItemMeta getMeta() {
        return meta;
    }

    @Override
    public int getAmount() {
        return this.item.getAmount();
    }

    @Override
    public ItemBuilder setAmount(int amount) {
        this.item.setAmount(Numbers.clamp(amount, 0, Byte.MAX_VALUE));
        return this;
    }

    @Override
    public Map<Enchantment, Integer> getEnchants() {
        return this.meta.getEnchants();
    }

    @Override
    public boolean isUnbreakable() {
        return this.meta.isUnbreakable();
    }

    @Override
    public ItemBuilder setUnbreakable(boolean unbreakable) {
        this.meta.setUnbreakable(unbreakable);
        return this;
    }

    @Override
    public String getError() {
        return error;
    }

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public int getRepairCost() {
        return ((Repairable) this.meta).getRepairCost();
    }

    @Override
    public ItemBuilder setRepairCost(int valueInLevels) {
        Repairable r = (Repairable) this.meta;
        r.setRepairCost(valueInLevels);
        return this;
    }

    @Override
    @Nullable
    public Color getLeatherColor() {
        if (meta instanceof LeatherArmorMeta leatherArmorMeta) {
            return leatherArmorMeta.getColor();
        }

        return null;
    }

    @Override
    @Nullable
    public String getHeadTexture() {
        try {
            return (String) this.meta.getClass().getDeclaredField("profile").get(this.meta);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ItemBuilder setHeadTexture(String base64) {
        final GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", base64));

        try {
            final Field field = FieldUtils.getDeclaredField(this.meta.getClass(), "profile", true);
            field.set(this.meta, profile);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return this;
    }

    @Override
    public Set<ItemFlag> getFlags() {
        return this.meta.getItemFlags();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributes() {
        return this.meta.getAttributeModifiers();
    }

    @Override
    public double getPureDamage() {
        double most = 0;
        for (AttributeModifier t : getAttributes().get(Attribute.GENERIC_ATTACK_DAMAGE)) {
            final double current = t.getAmount();
            most = Math.max(current, most);
        }
        return most;
    }

    @Override
    public ItemBuilder setPureDamage(double damage) {
        this.addAttribute(
                Attribute.GENERIC_ATTACK_DAMAGE,
                damage,
                AttributeModifier.Operation.ADD_NUMBER,
                EquipmentSlot.HAND
        );
        return this;
    }

    @Override
    @Nullable
    public String getId() {
        return id;
    }

    @Override
    public <T> ItemBuilder setPersistentData(String path, PersistentDataType<T, T> type, T value) {
        try {
            this.meta.getPersistentDataContainer().set(new NamespacedKey(EternaPlugin.getPlugin(), path), type, value);
        } catch (IllegalArgumentException er) {
            Chat.broadcastOp("&4An error occurred whilst trying to perform this action. Check the console!");
            throw new ItemBuilderException(
                    "Plugin call before plugin initiated. Make sure to register ItemBuilder BEFORE you register commands, events etc!");
        }
        return this;
    }

    @Override
    public <T> boolean hasPersistentData(String path, PersistentDataType<T, T> type) {
        return this.meta.getPersistentDataContainer().has(new NamespacedKey(EternaPlugin.getPlugin(), path), type);
    }

    @Override
    public <T> T getPersistentData(String path, PersistentDataType<T, T> type) {
        return this.meta.getPersistentDataContainer().get(new NamespacedKey(EternaPlugin.getPlugin(), path), type);
    }

    @Override
    public ItemBuilder glow() {
        this.addEnchant(Enchantment.LUCK, 1);
        this.hideFlag(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    @Override
    public Set<ItemAction> getFunctions() {
        return this.functions;
    }

    @Override
    public int getCd() {
        return this.cd;
    }

    @Override
    public Predicate<Player> getPredicate() {
        return this.predicate;
    }

    @Override
    public boolean isCancelClicks() {
        return cancelClicks;
    }

    @Override
    public ItemBuilder setCancelClicks(boolean cancelClicks) {
        this.cancelClicks = cancelClicks;
        return this;
    }

    private void validateBookMeta() {
        final Material type = this.getItem().getType();
        if (type != Material.WRITTEN_BOOK) {
            throw new IllegalStateException("Material must be WRITTEN_BOOK, not " + type);
        }
    }

    private void sendErrorMessage(String msg, Object... dot) {
        final String message = Chat.format(msg, dot);
        Bukkit.getLogger().log(Level.SEVERE, message);
        new ItemBuilderException(message).printStackTrace();
    }

    private void displayWarning(String warning, Object... objects) {
        Bukkit.getLogger().warning(ChatColor.YELLOW + warning.formatted(objects));
    }

    private void validatePotionMeta() {
        final Material type = this.item.getType();
        switch (type) {
            case LINGERING_POTION, POTION, SPLASH_POTION, TIPPED_ARROW -> {
            }
            default -> throw new IllegalStateException(
                    "Cannot apply potion meta! Material must be POTION, SPLASH_POTION, LINGERING_POTION or TIPPED_ARROW!"
                    //"Material must be POTION, SPLASH_POTION, LINGERING_POTION or TIPPED_ARROW to use this!"
            );
        }
    }

    /**
     * Creates player head from texture.
     *
     * @param texture - Texture to use.
     */
    public static ItemBuilder playerHead(String texture) {
        return new ItemBuilder(Material.PLAYER_HEAD).setHeadTexture(texture);
    }

    /**
     * Creates player head from texture from url.
     *
     * @param url - Url to texture.
     */
    public static ItemBuilder playerHeadUrl(String url) {
        return new ItemBuilder(Material.PLAYER_HEAD).setHeadTextureUrl(url);
    }

    /**
     * Creates leather helmet with provided color.
     *
     * @param color - Color to use.
     */
    public static ItemBuilder leatherHat(Color color) {
        return new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(color);
    }

    /**
     * Creates leather chestplate with provided color.
     *
     * @param color - Color to use.
     */
    public static ItemBuilder leatherTunic(Color color) {
        return new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(color);
    }

    /**
     * Creates leather leggings with provided color.
     *
     * @param color - Color to use.
     */
    public static ItemBuilder leatherPants(Color color) {
        return new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(color);
    }

    /**
     * Creates leather boots with provided color.
     *
     * @param color - Color to use.
     */
    public static ItemBuilder leatherBoots(Color color) {
        return new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(color);
    }

    /**
     * Creates builder of provided material.
     *
     * @param material - Material to use.
     */
    public static ItemBuilder of(Material material) {
        return new ItemBuilder(material);
    }

    /**
     * Creates builder of provided material with provided name.
     *
     * @param material - Material to use.
     * @param name     - Name to use.
     */
    public static ItemBuilder of(Material material, String name) {
        return new ItemBuilder(material).setName(name);
    }

    /**
     * Creates builder of provided material with provided name and lore.
     *
     * @param material - Material to use.
     * @param name     - Name to use.
     * @param lore     - Lore to use.
     */
    public static ItemBuilder of(Material material, String name, String... lore) {
        final ItemBuilder builder = new ItemBuilder(material).setName(name);
        if (lore != null) {
            for (String str : lore) {
                builder.addLore(str);
            }
        }
        return builder;
    }

    /**
     * Creates air builder.
     */
    public static ItemBuilder air() {
        return of(Material.AIR);
    }

    public static ItemBuilder fromItemStack(ItemStack stack) {
        return new ItemBuilder(stack);
    }

    public static void clear() {
        itemsWithEvents.clear();
    }

    @Nullable
    public static ItemStack getItemByID(String id) {
        if (itemsWithEvents.containsKey(id)) {
            return itemsWithEvents.get(id).item;
        }
        return null;
    }

    @Deprecated
    public static void broadcastRegisteredIDs() {
        Bukkit.getLogger().info("[ItemBuilder] Registered Custom Items:");
        System.out.println(itemsWithEvents.keySet());
    }

    public static Set<String> getRegisteredIDs() {
        return itemsWithEvents.keySet();
    }

    @Nullable
    public static String getItemID(ItemStack item) {
        if (item == null) {
            return null;
        }

        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        }
        return NBT.getString(meta, PLUGIN_PATH, null);
    }

    @Nullable
    public static ItemBuilder getBuilderFromItem(ItemStack item) {
        final String id = getItemID(item);
        if (id == null) {
            return null;
        }

        return itemsWithEvents.get(id);
    }

    public static boolean itemHasID(ItemStack item, String id) {
        final String itemId = getItemID(item);
        return itemHasID(item) && itemId != null && itemId.equalsIgnoreCase(id);
    }

    public static boolean itemContainsId(ItemStack item, String id) {
        final String itemId = getItemID(item);
        return itemHasID(item) && itemId != null && itemId.contains(id);
    }

    public static boolean itemHasID(ItemStack item) {
        return getItemID(item) != null;
    }

    public static List<String> splitString(@Nullable String prefix, String string, int limit) {
        final List<String> list = new ArrayList<>();
        final char[] chars = string.toCharArray();

        StringBuilder builder = new StringBuilder();
        int counter = 0;

        for (int i = 0; i < chars.length; i++) {
            final char c = chars[i];
            final boolean isManualSplit = (isManualSplitChar(c) &&
                    (i + 1 < chars.length && isManualSplitChar(chars[i + 1])));

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

                list.add(colorize((prefix == null ? "" : prefix) + builder.toString().trim()));
                counter = 0;
                builder = new StringBuilder();
                continue;
            }

            builder.append(c);
            ++counter;
        }

        return list;
    }

    @Deprecated
    public static List<String> splitAfter(String text, int max) {
        return splitAfter("&7", text, max);
    }

    @Deprecated
    public static List<String> splitAfter(String text, int max, String prefix) {
        return splitAfter(prefix, text, max);
    }

    public static List<String> splitString(String text, int max) {
        return splitString(null, text, max);
    }

    public static List<String> splitString(String text) {
        return splitString(text, 35);
    }

    @Nonnull
    public static List<String> getLastColors(String text, String def) {
        final char[] chars = (text + " ").toCharArray();
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

    public static String getLastColor(String text, String def) {
        final List<String> lastColors = getLastColors(text, def);

        return lastColors.isEmpty() ? def : lastColors.get(lastColors.size() - 1);
    }

    private static boolean isColorChar(char c) {
        return "0123456789abcdefklmnor".indexOf(c) != -1;
    }

    // Item Value Setters
    public static void setName(ItemStack item, String name) {
        final ItemMeta meta = item.getItemMeta();
        Nulls.runIfNotNull(meta, m -> m.setDisplayName(colorize(name)));
        item.setItemMeta(meta);
    }

    public static void setLore(ItemStack item, String lore) {
        final ItemMeta meta = item.getItemMeta();
        Nulls.runIfNotNull(meta, m -> m.setLore(Collections.singletonList(lore)));
        item.setItemMeta(meta);
    }

    private static boolean isIdRegistered(String id) {
        return id != null && itemsWithEvents.containsKey(id);
    }

    private static boolean isManualSplitChar(char c) {
        return c == '_';
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
            line = line.concat(c + "");
            counter++;

            if (c == '_' && text.charAt(i + 1) == '_') {
                // this fixes an extra space before manual split.
                // it's not a bug and it works as indented, but it's
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

    private static String colorize(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    protected static class ItemBuilderException extends RuntimeException {
        private ItemBuilderException() {
            super();
        }

        private ItemBuilderException(String args) {
            super(args);
        }
    }

}