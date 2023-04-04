package me.hapyl.spigotutils.module.inventory;

import com.google.common.collect.Multimap;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapView;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface ItemStackBuilder {

    /**
     * Returns current EventHandler for this builder, cannot be null.
     *
     * @return current EventHandler for this builder, cannot be null.
     */
    @Nonnull
    ItemEventHandler getEventHandler();

    /**
     * Sets a new event handler.
     *
     * @param handler - New event handler.
     */
    ItemStackBuilder setEventHandler(@Nonnull ItemEventHandler handler);

    /**
     * Returns item's NBT in string format, same as you would use it in give command.
     *
     * @return item's NBT or empty string if none.
     */
    @Nonnull
    String getNbt();

    /**
     * Sets item's NBT.
     * This method uses native minecraft nbt stored in the '<i>tag:{<b>HERE</b>}</i>' compound.
     *
     * @param nbt - New NBT or null to remove.
     */
    void setNbt(@Nullable String nbt);

    /**
     * Sets the builder's ItemMeta.
     *
     * @param meta - New ItemMeta.
     */
    ItemStackBuilder setItemMeta(ItemMeta meta);

    /**
     * Clones the builder.
     *
     * @return Cloned Builder if possible.
     * @throws UnsupportedOperationException if builder has ID.
     */
    @Nullable
    ItemStackBuilder clone();

    /**
     * Returns true if click events from inventory are allowed, false otherwise.
     *
     * @return true if click events from inventory are allowed, false otherwise.
     */
    boolean isAllowInventoryClick();

    /**
     * Sets if click events from inventory are allowed.
     *
     * @param allowInventoryClick - New value.
     */
    ItemStackBuilder setAllowInventoryClick(boolean allowInventoryClick);

    /**
     * Sets the map view for the builder. Material will be forced to be FILLED_MAP.
     *
     * @param view - New map view.
     */
    ItemStackBuilder setMapView(MapView view);

    /**
     * Sets the book name. Material will be forced to WRITTEN_BOOK.
     *
     * @param name - New book name.
     */
    ItemStackBuilder setBookName(String name);

    /**
     * Sets the book author if material is WRITTEN_BOOK.
     *
     * @param author - New book author.
     * @throws IllegalStateException if material is not WRITTEN_BOOK.
     */
    ItemStackBuilder setBookAuthor(String author);

    /**
     * Sets the book title if material is WRITTEN_BOOK.
     *
     * @param title - New book title.
     * @throws IllegalStateException if material is not WRITTEN_BOOK.
     */
    ItemStackBuilder setBookTitle(String title);

    /**
     * Sets the book pages if material is WRITTEN_BOOK.
     *
     * @param pages - New book pages.
     * @throws IllegalStateException if material is not WRITTEN_BOOK.
     */
    ItemStackBuilder setBookPages(List<String> pages);

    /**
     * Sets the book pages from BaseComponent[] if material is WRITTEN_BOOK.
     *
     * @param base - New book pages.
     * @throws IllegalStateException if material is not WRITTEN_BOOK.
     */
    ItemStackBuilder setBookPages(BaseComponent[]... base);

    /**
     * Sets the book page if material is WRITTEN_BOOK.
     *
     * @param page - Page to set.
     * @param base - New book page.
     * @throws IllegalStateException if material is not WRITTEN_BOOK.
     */
    ItemStackBuilder setBookPage(int page, BaseComponent[] base);

    /**
     * Adds cooldown to click event, requires ID.
     *
     * @param ticks - Cooldown in ticks.
     */
    ItemStackBuilder withCooldown(int ticks);

    /**
     * Adds cooldown to click event, requires ID.
     *
     * @param ticks     - Cooldown in ticks.
     * @param predicate - Predicate of the cooldown.
     */
    ItemStackBuilder withCooldown(int ticks, Predicate<Player> predicate);

    /**
     * Adds cooldown to click event, requires ID.
     *
     * @param ticks        - Cooldown in ticks.
     * @param predicate    - Predicate of the cooldown.
     * @param errorMessage - Error message if predicate fails.
     */
    ItemStackBuilder withCooldown(int ticks, Predicate<Player> predicate, String errorMessage);

    /**
     * Removes all click events.
     */
    ItemStackBuilder removeClickEvent();

    /**
     * Adds a click event.
     *
     * @param consumer - Click action.
     * @param act      - Allowed click types.
     */
    ItemStackBuilder addClickEvent(Consumer<Player> consumer, Action... act);

    /**
     * Adds a click event with only right clicks..
     *
     * @param consumer - Click action.
     */
    ItemStackBuilder addClickEvent(Consumer<Player> consumer);

    /**
     * Add persistent data to the item.
     *
     * @param path  - Path to the data.
     * @param value - Value to set.
     */
    ItemStackBuilder setPersistentData(String path, Object value);

    /**
     * Gets the persistent data from the item, or null if not found.
     *
     * @param path  - Path to the data.
     * @param value - PersistentDataType to get.
     * @param <T>   - Type of the data.
     * @return the persistent data from the item, or null if not found.
     */
    <T> T getNbt(String path, PersistentDataType<T, T> value);

    /**
     * Sets smart lore.
     * Smart lore splits automatically at the best places within the chat limit.
     *
     * @param lore - Lore to set.
     */
    ItemStackBuilder setSmartLore(String lore);

    /**
     * Sets smart lore with custom char limit.
     *
     * @param lore  - Lore to set.
     * @param limit - Char limit.
     */
    ItemStackBuilder setSmartLore(String lore, int limit);

    /**
     * Adds smart lore to existing lore with custom char limit.
     *
     * @param lore  - Lore to add.
     * @param limit - Char limit.
     */
    ItemStackBuilder addSmartLore(String lore, int limit);

    /**
     * Adds smart lore to existing lore with custom prefix.
     *
     * @param lore       - Lore to add.
     * @param prefixText - Prefix after every split.
     */
    ItemStackBuilder addSmartLore(String lore, String prefixText);

    /**
     * Adds smart lore to existing lore.
     *
     * @param lore - Lore to add.
     */
    ItemStackBuilder addSmartLore(String lore);

    /**
     * Sets smart lore with custom prefix color.
     *
     * @param lore   - Lore to set.
     * @param prefix - Prefix to set.
     */
    ItemStackBuilder setSmartLore(String lore, String prefix);

    /**
     * Sets smart lore with custom prefix color.
     *
     * @param lore       - Lore to set.
     * @param prefix     - Prefix to set.
     * @param splitAfter - Split after this amount of chars.
     */
    ItemStackBuilder setSmartLore(String lore, String prefix, int splitAfter);

    /**
     * Adds smart lore to existing lore with custom prefix color.
     *
     * @param lore       - Lore to add.
     * @param prefix     - Prefix to set.
     * @param splitAfter - Split after this amount of chars.
     */
    ItemStackBuilder addSmartLore(String lore, String prefix, int splitAfter);

    /**
     * Sets the lore at line.
     *
     * @param line - Index of line.
     * @param lore - Lore to set.
     * @throws IndexOutOfBoundsException if line is out of bounds.
     */
    ItemStackBuilder setLore(int line, String lore);

    /**
     * Sets the item lore. Use __ to split the lines.
     *
     * @param lore            - Lore to set.
     * @param afterSplitColor - Color of the text after split.
     */
    ItemStackBuilder addLore(String lore, ChatColor afterSplitColor);

    /**
     * Sets the item lore. Use __ to split the lines.
     *
     * @param lore - Lore to set.
     */
    ItemStackBuilder addLore(String lore);

    /**
     * Adds the lore to the item.
     *
     * @param lore         - Lore to add.
     * @param replacements - Replacements for the lore.
     */
    ItemStackBuilder addLore(String lore, Object... replacements);

    /**
     * Adds the lore to the item if condition is met.
     *
     * @param lore      - Lore to add.
     * @param condition - Condition.
     */
    ItemStackBuilder addLoreIf(String lore, boolean condition);

    /**
     * Adds the lore to the item if condition is met.
     *
     * @param lore         - Lore to add.
     * @param condition    - Condition.
     * @param replacements - Replacements for the lore.
     */
    ItemStackBuilder addLoreIf(String lore, boolean condition, Object... replacements);

    /**
     * Adds empty lore line.
     */
    ItemStackBuilder addLore();

    /**
     * Sets the item lore with custom separator.
     *
     * @param lore      - Lore to set.
     * @param separator - Separator to split the lines.
     */
    ItemStackBuilder setLore(String lore, String separator);

    /**
     * Removes all the lore from the item.
     */
    ItemStackBuilder removeLore();

    /**
     * Removes lore at provided line.
     *
     * @param line - Line to remove.
     * @throws IndexOutOfBoundsException if line is out of bounds.
     */
    ItemStackBuilder removeLoreLine(int line);

    /**
     * Applies default setting to the item, such as:
     *
     * - Makes item unbreakable.
     * - Hides enchantments.
     */
    ItemStackBuilder applyDefaultSettings();

    /**
     * Applies default setting to the item, such as:
     * - Makes item unbreakable.
     * - Hides enchantments.
     * - Applies curse of binding if applyCurse is true.
     *
     * @param applyCurse - If true, applies curse of binding.
     */
    ItemStackBuilder applyDefaultSettings(boolean applyCurse);

    /**
     * Sets the name of the item.
     *
     * @param name         - Name to set.
     * @param replacements - Replacements for the name.
     */
    ItemStackBuilder setName(String name, Object... replacements);

    /**
     * Adds enchant to the item.
     *
     * @param enchant - Enchantment to add.
     * @param lvl     - Level of the enchantment.
     */
    ItemStackBuilder addEnchant(Enchant enchant, int lvl);

    /**
     * Adds enchant to the item.
     *
     * @param enchantment - Enchantment to add.
     * @param lvl         - Level of the enchantment.
     */
    ItemStackBuilder addEnchant(Enchantment enchantment, int lvl);

    /**
     * Makes the item unbreakable.
     */
    ItemStackBuilder setUnbreakable();

    /**
     * Sets potion meta of the item.
     *
     * @param type     - Potion type.
     * @param lvl      - Potion level.
     * @param duration - Potion duration.
     * @param color    - Potion color.
     * @throws IllegalStateException if item is not a potion.
     */
    ItemStackBuilder setPotionMeta(PotionEffectType type, int lvl, int duration, Color color);

    /**
     * Sets potion color.
     *
     * @param color - Color to set.
     * @throws IllegalArgumentException if item is not a potion.
     */
    ItemStackBuilder setPotionColor(Color color);

    /**
     * Sets leather armor color if item is leather armor.
     *
     * @param color - Color to set.
     * @throws IllegalArgumentException if item is not leather armor.
     */
    ItemStackBuilder setLeatherArmorColor(Color color);

    /**
     * Sets the item texture from UUID and name.
     *
     * @param uuid - UUID of the player.
     * @param name - Name of the player.
     */
    ItemStackBuilder setHeadTexture(UUID uuid, String name);

    /**
     * Sets a link to minecraft skin texture as textures.
     * Use Other->Minecraft-URL from minecraft-heads.
     */
    ItemStackBuilder setHeadTextureUrl(String url);

    /**
     * Sets items skull owner.
     *
     * @param owner - Skull owner.
     */
    ItemStackBuilder setSkullOwner(String owner);

    /**
     * Adds attribute to the item.
     *
     * @param attribute - Attribute to add.
     * @param amount    - Amount of the attribute.
     * @param operation - Operation of the attribute.
     * @param slot      - Slot of the attribute.
     */
    ItemStackBuilder addAttribute(Attribute attribute, double amount, AttributeModifier.Operation operation, EquipmentSlot slot);

    /**
     * Hides items flags.
     *
     * @param flags - Flags to hide.
     */
    ItemStackBuilder hideFlag(ItemFlag... flags);

    /**
     * Shows items flags.
     *
     * @param flags - Flags to show.
     */
    ItemStackBuilder showFlag(ItemFlag... flags);

    /**
     * Hides all the item flags.
     */
    ItemStackBuilder hideFlags();

    /**
     * Shows all the item flags.
     */
    ItemStackBuilder showFlags();

    /**
     * Clears the item name, meaning sets it to empty.
     */
    ItemStackBuilder clearName();

    /**
     * Sets the item durability.
     *
     * @param durability - Durability to set.
     */
    ItemStackBuilder setDurability(int durability);

    /**
     * Returns the type of the builder.
     *
     * @return the type of the builder.
     */
    Material getType();

    /**
     * Changes type of the ItemStack.
     *
     * @param material - New type.
     */
    ItemStackBuilder setType(Material material);

    /**
     * Skips {@link ItemStackBuilder#build(boolean)} ID checks and only applies item meta, then returns ItemStack.
     *
     * @return ItemStack.
     */
    ItemStack toItemStack();

    /**
     * Clears Item flags, name, lore and makes it unbreakable before returning ItemStack.
     * Used to make empty icons items.
     *
     * @return cleaned ItemStack.
     */
    ItemStack cleanToItemSack();

    /**
     * Builds item stack and hides all extra data
     * in the lore of the item.
     *
     * @return icon-themed ItemStack.
     */
    ItemStack asIcon();

    /**
     * Finalizes item meta and returns an ItemStack.
     *
     * @param overrideIfExists - if true, item has ID and item with that ID is already
     *                         registered it will override the stored item, else error
     *                         will be throws if 2 conditions were met.
     * @return finalized ItemStack.
     * @throws ItemBuilder.ItemBuilderException if item has ID which is already registered and overrideIfExists is false.
     */
    ItemStack build(boolean overrideIfExists);

    /**
     * Finalizes item meta and returns an ItemStack.
     *
     * @return finalized ItemStack.
     * @throws ItemBuilder.ItemBuilderException if item has ID which is already registered.
     */
    ItemStack build();

    /**
     * Returns the name of the item.
     */
    @Nonnull
    String getName();

    /**
     * Sets the name of the item.
     *
     * @param name - Name to set.
     */
    ItemStackBuilder setName(String name);

    /**
     * Returns current list of lore, or null if no lore.
     *
     * @return current list of lore, or null if no lore.
     */
    @Nullable
    List<String> getLore();

    /**
     * Sets lore from List<String>.
     *
     * @param lore - Lore to set.
     */
    ItemStackBuilder setLore(List<String> lore);

    /**
     * Sets the item lore. Use __ to split the lines.
     *
     * @param lore - Lore to set.
     */
    ItemStackBuilder setLore(String lore);

    /**
     * Gets items lore from start to end.
     *
     * @param start - start index
     * @param end   - end index
     * @return list of lore
     * @throws IndexOutOfBoundsException if start or end is out of bounds.
     */
    @Nullable
    List<String> getLore(int start, int end);

    /**
     * Returns items meta.
     *
     * @return items meta.
     */
    ItemMeta getMeta();

    /**
     * Returns the amount of the item.
     *
     * @return the amount of the item.
     */
    int getAmount();

    /**
     * Sets the item amount.
     *
     * @param amount - New amount.
     */
    ItemStackBuilder setAmount(int amount);

    /**
     * Returns enchantment map of the item.
     *
     * @return enchantment map of the item.
     */
    Map<Enchantment, Integer> getEnchants();

    /**
     * Returns true if item is unbreakable, false otherwise.
     *
     * @return true if item is unbreakable, false otherwise.
     */
    boolean isUnbreakable();

    /**
     * Changes item unbreakable state.
     *
     * @param unbreakable - New state.
     */
    ItemStackBuilder setUnbreakable(boolean unbreakable);

    /**
     * Returns predicate error.
     *
     * @return predicate error.
     */
    String getError();

    /**
     * Returns original item without meta applied.
     *
     * @return original item without meta applied.
     */
    ItemStack getItem();

    /**
     * Returns repair cost on anvil of this item.
     *
     * @return repair cost on anvil of this item.
     */
    int getRepairCost();

    /**
     * Sets the repair cost on the anvil of the item.
     *
     * @param valueInLevels - Value in levels.
     */
    ItemStackBuilder setRepairCost(int valueInLevels);

    /**
     * Returns color of the leather armor or null if item is not leather armor.
     *
     * @return color of the leather armor or null if item is not leather armor.
     */
    @Nullable
    Color getLeatherColor();

    /**
     * Return the head texture of the item or null if item is not a skull.
     *
     * @return the head texture of the item or null if item is not a skull.
     */
    @Nullable
    String getHeadTexture();

    /**
     * Sets an actual base64 value as textures.
     * Use Other->Value from minecraft-heads.
     */
    ItemStackBuilder setHeadTexture(String base64);

    /**
     * Returns items flags.
     *
     * @return items flags.
     */
    Set<ItemFlag> getFlags();

    /**
     * Returns items attributes.
     *
     * @return items attributes.
     */
    Multimap<Attribute, AttributeModifier> getAttributes();

    /**
     * Returns pure (raw) item damage using GENERIC_ATTACK_DAMAGE.
     *
     * @return pure (raw) item damage using GENERIC_ATTACK_DAMAGE.
     */
    double getPureDamage();

    /**
     * Sets items pure (raw) damage using GENERIC_ATTACK_DAMAGE.
     *
     * @param damage - Damage to set.
     */
    ItemStackBuilder setPureDamage(double damage);

    /**
     * Returns custom ID of the item.
     *
     * @return custom ID of the item.
     */
    @Nullable
    String getId();

    /**
     * Sets the persistent data of the item.
     *
     * @param path  - path to the data
     * @param type  - type of the data
     * @param value - value of the data
     * @param <T>   - type of the data
     */
    <T> ItemStackBuilder setPersistentData(String path, PersistentDataType<T, T> type, T value);

    /**
     * Returns true if item has persistent data at the path.
     *
     * @param path - path to the data
     * @param type - type of the data
     * @param <T>  - type of the data
     * @return true if item has persistent data at the path.
     */
    <T> boolean hasPersistentData(String path, PersistentDataType<T, T> type);

    /**
     * Gets the persistent data of the item.
     *
     * @param path - path to the data
     * @param type - type of the data
     * @param <T>  - type of the data
     * @return persistent data of the item.
     */
    <T> T getPersistentData(String path, PersistentDataType<T, T> type);

    /**
     * Makes the item glow with enchantment glint.
     */
    ItemStackBuilder glow();

    /**
     * Returns item functions. (Click functions)
     *
     * @return item functions. (Click functions)
     */
    Set<ItemAction> getFunctions();

    /**
     * Gets the cooldown of the item.
     *
     * @return the cooldown of the item.
     */
    int getCd();

    /**
     * Returns item predicate for function.
     *
     * @return item predicate for function.
     */
    Predicate<Player> getPredicate();

    /**
     * Returns true if item is canceling clicks.
     *
     * @return true if item is canceling clicks.
     */
    boolean isCancelClicks();

    /**
     * Sets if even should cancel clicks upon click event triggering.
     * Set to false if using custom checks for click.
     *
     * @param cancelClicks - New value.
     */
    ItemStackBuilder setCancelClicks(boolean cancelClicks);

    /**
     * Creates player head from texture.
     *
     * @param texture - Texture to use.
     */
    static ItemStackBuilder playerHead(String texture) {
        return new ItemBuilder(Material.PLAYER_HEAD).setHeadTexture(texture);
    }

    /**
     * Creates player head from texture from url.
     *
     * @param url - Url to texture.
     */
    static ItemStackBuilder playerHeadUrl(String url) {
        return new ItemBuilder(Material.PLAYER_HEAD).setHeadTextureUrl(url);
    }

    /**
     * Creates leather helmet with provided color.
     *
     * @param color - Color to use.
     */
    static ItemStackBuilder leatherHat(Color color) {
        return new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(color);
    }

    /**
     * Creates leather chestplate with provided color.
     *
     * @param color - Color to use.
     */
    static ItemStackBuilder leatherTunic(Color color) {
        return new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(color);
    }

    /**
     * Creates leather leggings with provided color.
     *
     * @param color - Color to use.
     */
    static ItemStackBuilder leatherPants(Color color) {
        return new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(color);
    }

    /**
     * Creates leather boots with provided color.
     *
     * @param color - Color to use.
     */
    static ItemStackBuilder leatherBoots(Color color) {
        return new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(color);
    }

    /**
     * Creates builder of provided material.
     *
     * @param material - Material to use.
     */
    static ItemStackBuilder of(Material material) {
        return new ItemBuilder(material);
    }

    /**
     * Creates builder of provided material with provided name.
     *
     * @param material - Material to use.
     * @param name     - Name to use.
     */
    static ItemStackBuilder of(Material material, String name) {
        return new ItemBuilder(material).setName(name);
    }

    /**
     * Creates builder of provided material with provided name and lore.
     *
     * @param material - Material to use.
     * @param name     - Name to use.
     * @param lore     - Lore to use.
     */
    static ItemStackBuilder of(Material material, String name, String... lore) {
        final ItemStackBuilder builder = new ItemBuilder(material).setName(name);
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
    static ItemStackBuilder air() {
        return of(Material.AIR);
    }
}
