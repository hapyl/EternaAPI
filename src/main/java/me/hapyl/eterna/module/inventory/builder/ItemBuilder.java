package me.hapyl.eterna.module.inventory.builder;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import io.papermc.paper.datacomponent.item.UseCooldown;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.annotate.MethodApplicableTo;
import me.hapyl.eterna.module.annotate.RequiresVarargs;
import me.hapyl.eterna.module.annotate.SelfReturn;
import me.hapyl.eterna.module.component.ComponentList;
import me.hapyl.eterna.module.component.ComponentMapper;
import me.hapyl.eterna.module.component.Components;
import me.hapyl.eterna.module.reflect.Skin;
import me.hapyl.eterna.module.registry.CloneableKeyed;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.registry.KeyLike;
import me.hapyl.eterna.module.registry.Keyed;
import me.hapyl.eterna.module.util.BukkitUtils;
import me.hapyl.eterna.module.util.Validate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.inventory.meta.components.*;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.tag.DamageTypeTags;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;

/**
 * Represents an {@link ItemStack} builder, allowing deep customization of items, including, but not limited to:
 * <ul>
 *     <li>Custom names:
 *     <ul>
 *         <li>{@link #setName(Component)}
 *     </ul>
 *
 *     <li>Custom lore:
 *     <ul>
 *         <li>{@link #addLore(Component)}
 *         <li>{@link #addWrappedLore(Component)}
 *     </ul>
 *
 *     <li>Enchants:
 *     <ul>
 *         <li>{@link #addEnchant(Enchantment, int)}
 *     </ul>
 *
 *     <li>Components:
 *     <ul>
 *         <li>{@link #setFood(ComponentModifier)}
 *         <li>{@link #setTool(ComponentModifier)}
 *         <li>{@link #setEquippable(ComponentModifier)}
 *     </ul>
 *
 *     <li>Item functions:
 *     <ul>
 *         <li>{@link #addClickAction(Consumer)}
 *         <li>{@link #addFunction(ItemFunction)}
 *     </ul>
 * </ul>
 * <p>Most of the edits, be it setting name or adding lore, are applied right away to the {@link ItemMeta} and do not require calling {@link #build()} specifically,
 * but click events are first stored locally and only registered on {@link #build()}; they also require {@link Key} to be set.</p>
 */
@SuppressWarnings("UnstableApiUsage")
public class ItemBuilder implements CloneableKeyed, Keyed {
    
    /**
     * Defines the default {@link Component} wrap limit for {@link #addWrappedLore(Component)}.
     */
    public static final int DEFAULT_COMPONENT_WRAP_LIMIT;
    
    /**
     * Defines the default {@link Style} for {@link #setName(Component)}, unless a color is explicitly set.
     */
    public static final Style DEFAULT_NAME_STYLE;
    
    /**
     * Defines the default {@link Style} for {@link #addLore(Component)} (and similar), unless a color is explicitly set.
     */
    public static final Style DEFAULT_LORE_STYLE;
    
    private static final Map<Key, ItemFunctionList> FUNCTIONS;
    
    private static final java.util.regex.Pattern BASE64_DECODE_PATTERN;
    private static final java.util.regex.Pattern TEXTURE_PATTERN;
    
    private static final NamespacedKey FUNCTION_KEY_PATH;
    private static final String URL_TEXTURE_LINK;
    
    private static final Set<DataComponentType> COMPONENTS_WITH_TOOLTIPS;
    private static final TooltipDisplay HIDE_FLAGS_TOOLTIP;
    
    static {
        DEFAULT_COMPONENT_WRAP_LIMIT = 38;
        
        DEFAULT_NAME_STYLE = Style.style()
                                  .color(NamedTextColor.DARK_GREEN)
                                  .build();
        
        DEFAULT_LORE_STYLE = Style.style()
                                  .color(NamedTextColor.GRAY)
                                  .decoration(TextDecoration.ITALIC, false)
                                  .build();
        
        FUNCTIONS = Maps.newHashMap();
        
        BASE64_DECODE_PATTERN = java.util.regex.Pattern.compile("\"url\"\\s*:\\s*\"(http[^\"]+)\"");
        TEXTURE_PATTERN = java.util.regex.Pattern.compile("https?://textures\\.minecraft\\.net/texture/");
        
        FUNCTION_KEY_PATH = BukkitUtils.createKey("function_key");
        URL_TEXTURE_LINK = "https://textures.minecraft.net/texture/";
        
        COMPONENTS_WITH_TOOLTIPS = Set.of(
                DataComponentTypes.DAMAGE,
                DataComponentTypes.UNBREAKABLE,
                DataComponentTypes.ENCHANTMENTS,
                DataComponentTypes.CAN_PLACE_ON,
                DataComponentTypes.CAN_BREAK,
                DataComponentTypes.ATTRIBUTE_MODIFIERS,
                DataComponentTypes.STORED_ENCHANTMENTS,
                DataComponentTypes.DYED_COLOR,
                DataComponentTypes.MAP_ID,
                DataComponentTypes.CHARGED_PROJECTILES,
                DataComponentTypes.BUNDLE_CONTENTS,
                DataComponentTypes.POTION_CONTENTS,
                DataComponentTypes.POTION_DURATION_SCALE,
                DataComponentTypes.WRITABLE_BOOK_CONTENT,
                DataComponentTypes.WRITTEN_BOOK_CONTENT,
                DataComponentTypes.TRIM,
                DataComponentTypes.PROVIDES_TRIM_MATERIAL,
                DataComponentTypes.PROVIDES_BANNER_PATTERNS,
                DataComponentTypes.FIREWORKS,
                DataComponentTypes.BANNER_PATTERNS,
                DataComponentTypes.JUKEBOX_PLAYABLE,
                DataComponentTypes.INSTRUMENT
        );
        
        HIDE_FLAGS_TOOLTIP = TooltipDisplay.tooltipDisplay().hiddenComponents(COMPONENTS_WITH_TOOLTIPS).build();
    }
    
    @NotNull private final Key key;
    
    @NotNull private ItemStack itemStack;
    @Nullable private ItemFunctionList localFunctions;
    
    /**
     * Creates a new {@link ItemBuilder} with the given {@link Material}.
     *
     * @param material - The material of the item; must be {@link Material#isItem()}.
     */
    public ItemBuilder(@NotNull Material material) {
        this(new ItemStack(material), Key.empty());
    }
    
    /**
     * Creates a new {@link ItemBuilder} from the given {@link ItemStack}.
     *
     * @param itemStack - The underlying item stack.
     */
    public ItemBuilder(@NotNull ItemStack itemStack) {
        this(itemStack.clone(), Key.empty());
    }
    
    /**
     * Creates a new {@link ItemBuilder} with the given {@link Material}.
     *
     * @param material - The material of the item; must be {@link Material#isItem()}.
     * @param key      - The key of the builder; required in order to use functions.
     */
    public ItemBuilder(@NotNull Material material, @NotNull Key key) {
        this(new ItemStack(material), key);
    }
    
    /**
     * Creates a new {@link ItemBuilder} from the given {@link ItemStack}.
     *
     * @param stack - The underlying item stack.
     * @param key   - The key of the builder; required in order to user functions.
     */
    public ItemBuilder(@NotNull ItemStack stack, @NotNull Key key) {
        this.itemStack = stack;
        this.key = key;
        this.localFunctions = key.isEmpty() ? null : new ItemFunctionList(key);
        
        // Set the cooldown key right away, allowing overriding it if needed
        if (!key.isEmpty()) {
            this.setCooldown(cooldown -> cooldown.setCooldownGroup(key.asNamespacedKey()));
        }
    }
    
    // *-* Click actions *-* //
    
    /**
     * Adds a {@link ItemFunction} to this {@link ItemBuilder}.
     * <p>This method defaults to {@link Action#RIGHT_CLICK_BLOCK} and {@link Action#RIGHT_CLICK_AIR} click types.</p>
     *
     * @param clickAction - The action to perform.
     */
    @SelfReturn
    public ItemBuilder addClickAction(@NotNull Consumer<Player> clickAction) {
        return addClickAction(clickAction, Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR);
    }
    
    /**
     * Adds a {@link ItemFunction} to this {@link ItemBuilder}.
     *
     * @param clickAction - The action to perform.
     * @param clickTypes  - The click types this action is applicable to.
     * @throws IllegalArgumentException if there is no click types provided
     */
    @SelfReturn
    public ItemBuilder addClickAction(@NotNull Consumer<Player> clickAction, @Range(from = 1, to = Integer.MAX_VALUE) @NotNull Action... clickTypes) {
        Validate.isTrue(clickTypes.length > 0, "There must be at least one click type!");
        
        localFunctions().add(ItemFunction.builder(clickAction).accepts(Arrays.asList(clickTypes)).build());
        return this;
    }
    
    /**
     * Adds a {@link ItemFunction} to this {@link ItemBuilder}.
     *
     * @param function - The function to add.
     * @see ItemFunction#builder(Consumer)
     */
    @SelfReturn
    public ItemBuilder addFunction(@NotNull ItemFunction function) {
        localFunctions().add(function);
        return this;
    }
    
    /**
     * Sets a {@link ItemEventListener} for this {@link ItemBuilder}.
     *
     * @param listener - The listener to set.
     */
    @SelfReturn
    public ItemBuilder setListener(@NotNull ItemEventListener listener) {
        localFunctions().listener = listener;
        return this;
    }
    
    /**
     * Clears all the <i>local</i> {@link ItemFunction} on this {@link ItemBuilder}.
     */
    @SelfReturn
    public ItemBuilder clearFunctions() {
        final ItemFunctionList functions = localFunctions();
        
        functions.functions.clear();
        functions.listener = null;
        
        return this;
    }
    
    // *-* Lore *-* //
    
    /**
     * Adds the given {@link List} of {@link Component} to this {@link ItemBuilder} lore.
     * <p>This will normalize each component with {@link #DEFAULT_LORE_STYLE} and append them to the end of the lore.</p>
     *
     * @param lore - The list of components to add.
     * @see Components#normalizeStyle(Component, Style)
     */
    @SelfReturn
    public ItemBuilder addLore(@NotNull List<? extends Component> lore) {
        return editLore(existingLore -> existingLore.addAll(
                lore.stream()
                    .map(_component -> Components.normalizeStyle(_component, DEFAULT_LORE_STYLE))
                    .toList()
        ));
    }
    
    /**
     * Adds the given {@link Component} to this {@link ItemBuilder} lore.
     * <p>This will normalize the component with {@link #DEFAULT_LORE_STYLE} and append it to the end of the lore.</p>
     *
     * @param lore - The component to add.
     */
    @SelfReturn
    public ItemBuilder addLore(@NotNull Component lore) {
        return addLore(List.of(lore));
    }
    
    /**
     * Adds an {@link Component#empty()} to this {@link ItemBuilder} lore.
     * <p>This will result in an empty line in lore.</p>
     */
    @SelfReturn
    public ItemBuilder addLore() {
        return addLore(Component.empty());
    }
    
    /**
     * Adds the given {@link Component} to this {@link ItemBuilder} lore if the {@code condition} is {@code true}.
     *
     * @param lore      - The component to add.
     * @param condition - The condition to check.
     */
    @SelfReturn
    public ItemBuilder addLoreIf(@NotNull Component lore, boolean condition) {
        if (condition) {
            addLore(lore);
        }
        
        return this;
    }
    
    /**
     * Wraps the given {@link Component} and adds it to this {@link ItemBuilder} lore.
     *
     * @param component - The component to wrap.
     * @param mapper    - The mapper to handle each component after it has been wrapped.
     *                  <p>Note that the mapper is applied on a clean text {@link Component}, meaning that the default behaviour of {@code dark_purple} color and {@code italic} decoration is
     *                  <b>preserved</b>, unless a {@code null} {@link ComponentMapper} is provided, in which case a {@link #DEFAULT_LORE_STYLE} is applied.</p>
     * @param maxLength - The max length of each line.
     * @see Components#wrap(Component, int)
     */
    @SelfReturn
    public ItemBuilder addWrappedLore(@NotNull Component component, @Nullable ComponentMapper mapper, int maxLength) {
        return editLore(existingLore -> existingLore.addAll(
                Components.wrap(component, maxLength)
                          .stream()
                          .map(_component -> mapper != null ? mapper.map(_component) : _component.style(DEFAULT_LORE_STYLE))
                          .toList()
        ));
    }
    
    /**
     * Wraps the given {@link Component} and adds it to this {@link ItemBuilder} lore.
     * <p>This method uses {@link #DEFAULT_COMPONENT_WRAP_LIMIT} as a limit for component wrap.</p>
     *
     * @param component - The component to wrap.
     * @param mapper    - The mapper to handle each component after it has been wrapped.
     *                  <p>Note that the mapper is applied on a clean text {@link Component}, meaning that the default behaviour of {@code dark_purple} color and {@code italic} decoration is
     *                  <b>preserved</b>, unless a {@code null} {@link ComponentMapper} is provided, in which case a {@link #DEFAULT_LORE_STYLE} is applied.</p>
     * @see Components#wrap(Component, int)
     */
    @SelfReturn
    public ItemBuilder addWrappedLore(@NotNull Component component, @Nullable ComponentMapper mapper) {
        return addWrappedLore(component, mapper, DEFAULT_COMPONENT_WRAP_LIMIT);
    }
    
    /**
     * Wraps the given {@link Component} and adds it to this {@link ItemBuilder} lore.
     * <p>This method uses {@link #DEFAULT_COMPONENT_WRAP_LIMIT} as a limit for component wrap.</p>
     *
     * @param component - The component to wrap.
     * @see Components#wrap(Component, int)
     */
    @SelfReturn
    public ItemBuilder addWrappedLore(@NotNull Component component, int maxLength) {
        return addWrappedLore(component, null, maxLength);
    }
    
    /**
     * Wraps the given {@link Component} and adds it to this {@link ItemBuilder} lore.
     * <p>This method uses {@link #DEFAULT_COMPONENT_WRAP_LIMIT} as a limit for component wrap.</p>
     *
     * @param component - The component to wrap.
     * @see Components#wrap(Component, int)
     */
    @SelfReturn
    public ItemBuilder addWrappedLore(@NotNull Component component) {
        return addWrappedLore(component, null, DEFAULT_COMPONENT_WRAP_LIMIT);
    }
    
    /**
     * Sets the given {@link List} of {@link Component} as this {@link ItemBuilder} lore, replacing all existing lore.
     *
     * @param lore - The lore to set.
     */
    @SelfReturn
    public ItemBuilder setLore(@NotNull List<? extends @NotNull Component> lore) {
        return editMeta(meta -> meta.lore(lore));
    }
    
    /**
     * Sets the given {@link ComponentList} as this {@link ItemBuilder} lore, replacing all existing lore.
     *
     * @param lore - The lore to set.
     */
    @SelfReturn
    public ItemBuilder setLore(@NotNull ComponentList lore) {
        return editMeta(meta -> meta.lore(
                lore.stream()
                    .map(component -> Components.normalizeStyle(component, DEFAULT_LORE_STYLE))
                    .toList()
        ));
    }
    
    /**
     * Clears this {@link ItemBuilder} lore, removing all existing lore.
     */
    @SelfReturn
    public ItemBuilder clearLore() {
        return editMeta(meta -> meta.lore(null));
    }
    
    /**
     * Clears this {@link ItemBuilder} lore, from {@code startIndex} to {@code endIndex}.
     * <p>If any index is out of bounds, this method silently fails.</p>
     *
     * @param startIndex - The start index (inclusive).
     * @param endIndex   - The end index (exclusive).
     */
    @SelfReturn
    public ItemBuilder clearLore(int startIndex, int endIndex) {
        return editMeta(meta -> {
            final List<Component> existingLore = meta.lore();
            
            // Fail silently if no lore or either of indexes are out of bounds
            if (existingLore == null || startIndex < 0 || startIndex >= endIndex || endIndex > existingLore.size()) {
                return;
            }
            
            existingLore.subList(startIndex, endIndex).clear();
            meta.lore(existingLore);
        });
    }
    
    /**
     * Sets this {@link ItemBuilder} custom name.
     *
     * @param name - The name to set.
     */
    @SelfReturn
    public ItemBuilder setName(@NotNull Component name) {
        return editMeta(meta -> meta.customName(Components.normalizeStyle(name, DEFAULT_NAME_STYLE)));
    }
    
    /**
     * Sets this {@link ItemBuilder} custom name to {@code null}, making it use the default {@link Material} name.
     */
    @SelfReturn
    public ItemBuilder setDefaultName() {
        return editMeta(meta -> meta.customName(null));
    }
    
    /**
     * Clears this {@link ItemBuilder} custom name by making it {@link Component#empty()}.
     */
    @SelfReturn
    public ItemBuilder clearName() {
        return editMeta(meta -> meta.customName(Component.empty()));
    }
    
    // *-* Meta edits *-* //
    
    /**
     * Edits the {@link BookMeta} on this {@link ItemBuilder}.
     * <p>Only applicable if material is {@link Material#WRITTEN_BOOK}.</p>
     *
     * @param edit - The editor.
     */
    @SelfReturn
    @MethodApplicableTo(values = Material.WRITTEN_BOOK)
    public ItemBuilder editBookMeta(@NotNull Consumer<BookMeta> edit) {
        return editMeta(BookMeta.class, edit);
    }
    
    /**
     * Edits the {@link PotionMeta} on this {@link ItemBuilder}.
     * <p>Only applicable if material is {@link Material#POTION}, {@link Material#SPLASH_POTION}, {@link Material#LINGERING_POTION} or {@link Material#TIPPED_ARROW}.</p>
     *
     * @param edit - The editor.
     */
    @SelfReturn
    @MethodApplicableTo(values = { Material.POTION, Material.SPLASH_POTION, Material.LINGERING_POTION, Material.TIPPED_ARROW })
    public ItemBuilder editPotionMeta(@NotNull Consumer<PotionMeta> edit) {
        return editMeta(PotionMeta.class, edit);
    }
    
    // *-* Adders & Setters *-* //
    
    /**
     * Adds the given {@link Enchantment} to this {@link ItemBuilder}.
     *
     * @param enchantment - The enchantment to add.
     * @param level       - The enchantment level.
     */
    @SelfReturn
    public ItemBuilder addEnchant(@NotNull Enchantment enchantment, int level) {
        return editMeta(meta -> meta.addEnchant(enchantment, level, true));
    }
    
    /**
     * Sets the {@link PotionMeta} {@link Color} for this {@link ItemBuilder}.
     * <p>Only applicable if material is {@link Material#POTION}, {@link Material#SPLASH_POTION}, {@link Material#LINGERING_POTION} or {@link Material#TIPPED_ARROW}.</p>
     *
     * @param color - The color to set, or {@code null} to remove color.
     */
    @SelfReturn
    @MethodApplicableTo(values = { Material.POTION, Material.SPLASH_POTION, Material.LINGERING_POTION, Material.TIPPED_ARROW })
    public ItemBuilder setPotionColor(@Nullable Color color) {
        return editMeta(PotionMeta.class, meta -> meta.setColor(color));
    }
    
    /**
     * Sets the {@link LeatherArmorMeta} {@link Color} for this {@link ItemBuilder}.
     * <p>Only applicable if material is {@link Material#LEATHER_HELMET}, {@link Material#LEATHER_CHESTPLATE}, {@link Material#LEATHER_LEGGINGS}, {@link Material#LEATHER_BOOTS},
     * {@link Material#LEATHER_HORSE_ARMOR} or {@link Material#WOLF_ARMOR}.</p>
     *
     * @param color - The color to set, or {@code null} to remove color.
     */
    @SelfReturn
    @MethodApplicableTo(values = { Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS, Material.LEATHER_HORSE_ARMOR, Material.WOLF_ARMOR })
    public ItemBuilder setLeatherArmorColor(@Nullable Color color) {
        return editMeta(LeatherArmorMeta.class, meta -> meta.setColor(color));
    }
    
    /**
     * Makes the item unbreakable.
     */
    @SelfReturn
    public ItemBuilder setUnbreakable() {
        return editMeta(meta -> meta.setUnbreakable(true));
    }
    
    /**
     * Sets the head texture of this {@link ItemBuilder}.
     * <p>The texture must point to a {@code textures.minecraft.net/textures/} url but only the hash, as example: {@code da8adca36d7756cca2975d1a1f6b5ab56cda82d88f9de0d3de595332c8035cb0}.</p>
     * <p>Only applicable if material is {@link Material#PLAYER_HEAD}.</p>
     *
     * @param textureHash - The texture hash.
     */
    @SelfReturn
    @MethodApplicableTo(values = Material.PLAYER_HEAD)
    public ItemBuilder setHeadTexture(@NotNull String textureHash) {
        final PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        final PlayerTextures textures = profile.getTextures();
        
        try {
            textures.setSkin(BukkitUtils.url(URL_TEXTURE_LINK + textureHash));
            profile.setTextures(textures);
            
            return editMeta(SkullMeta.class, meta -> meta.setPlayerProfile(profile));
        }
        catch (Exception e) {
            throw EternaLogger.acknowledgeException(e);
        }
    }
    
    /**
     * Sets the head texture of this {@link ItemBuilder}.
     * <p>Only applicable if material is {@link Material#PLAYER_HEAD}.</p>
     *
     * @param skin - The skin to fetch the texture from.
     */
    @SelfReturn
    @MethodApplicableTo(values = Material.PLAYER_HEAD)
    public ItemBuilder setHeadTexture(@NotNull Skin skin) {
        return setHeadTextureBase64(skin.texture());
    }
    
    /**
     * Sets the head texture of this {@link ItemBuilder}.
     * <p>This method should generally be avoided in {@link #setHeadTexture(String)} favor.</p>
     * <p>Only applicable if material is {@link Material#PLAYER_HEAD}.</p>
     *
     * @param base64 - The base64 of the texture.
     */
    @SelfReturn
    @MethodApplicableTo(values = Material.PLAYER_HEAD)
    @ApiStatus.Obsolete
    public ItemBuilder setHeadTextureBase64(@NotNull String base64) {
        return setHeadTexture(decodeBase64(base64));
    }
    
    /**
     * Adds the given {@link Attribute} to this {@link ItemBuilder}.
     *
     * @param attribute - The attribute type.
     * @param amount    - The attribute amount.
     * @param operation - The addition operation.
     * @param slot      - The applicable slots.
     */
    @SelfReturn
    public ItemBuilder addAttribute(@NotNull Attribute attribute, double amount, @NotNull AttributeModifier.Operation operation, @Nullable EquipmentSlotGroup slot) {
        return editMeta(meta -> meta.addAttributeModifier(
                attribute,
                new AttributeModifier(
                        BukkitUtils.createKey(UUID.randomUUID()),
                        amount,
                        operation,
                        slot != null ? slot : EquipmentSlotGroup.ANY
                )
        ));
    }
    
    /**
     * Adds the given {@link AttributeModifier} to this {@link ItemBuilder}.
     *
     * @param attribute - The attribute type.
     * @param modifier  - The modifier to add.
     */
    @SelfReturn
    public ItemBuilder addAttribute(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
        return editMeta(meta -> meta.addAttributeModifier(attribute, modifier));
    }
    
    /**
     * Hides the given {@link DataComponentType} on this {@link ItemBuilder}.
     *
     * <p>
     * Note that some tooltips <b>cannot</b> be hidden because Mojang is a small indie company
     * that does not know how their own game works. If you need a clean item without any tooltips,
     * use {@link #unsetComponent(DataComponentType)}, which will <b>remove</b> the component from the item,
     * which makes them unfunctional for that components, but at least it <b>will</b> hide the tooltip.
     * </p>
     *
     * @param types - The components to hide.
     * @see DataComponentTypes
     */
    @SelfReturn
    public ItemBuilder hideComponents(@NotNull DataComponentType... types) {
        this.itemStack.setData(
                DataComponentTypes.TOOLTIP_DISPLAY,
                TooltipDisplay.tooltipDisplay()
                              .hiddenComponents(Set.of(types))
                              .build()
        );
        return this;
    }
    
    /**
     * Hides all the {@link DataComponentType} on this {@link ItemBuilder}.
     *
     * <p>
     * Note that some tooltips <b>cannot</b> be hidden because Mojang is a small indie company
     * that does not know how their own game works. If you need a clean item without any tooltips,
     * use {@link #unsetComponent(DataComponentType)}, which will <b>remove</b> the component from the item,
     * which makes them unfunctional for that components, but at least it <b>will</b> hide the tooltip.
     * </p>
     */
    @SelfReturn
    public ItemBuilder hideComponents() {
        this.itemStack.setData(DataComponentTypes.TOOLTIP_DISPLAY, HIDE_FLAGS_TOOLTIP);
        return this;
    }
    
    /**
     * Sets the durability of this {@link ItemBuilder}.
     * <p>Only applicable if item is {@link Damageable}.</p>
     *
     * @param durability - The durability to set.
     */
    @SelfReturn
    public ItemBuilder setDurability(int durability) {
        return editMeta(Damageable.class, meta -> meta.setDamage(durability));
    }
    
    /**
     * Gets the {@link Material} of this {@link ItemBuilder}.
     *
     * @return the material of this builder.
     */
    @NotNull
    public Material getType() {
        return itemStack.getType();
    }
    
    /**
     * Sets the {@link Material} of this {@link ItemBuilder}.
     *
     * @param material - The material to set; must be {@link Material#isItem()}.
     */
    @SelfReturn
    public ItemBuilder setType(@NotNull Material material) {
        this.itemStack = this.itemStack.withType(material);
        return this;
    }
    
    /**
     * Sets the {@code amount} of this {@link ItemBuilder}.
     * <p>The maximum stack size will be adjusted if the given amount exceeds the current maximum size.</p>
     *
     * @param amount - The amount to set; must be within range.
     */
    @SelfReturn
    public ItemBuilder setAmount(@Range(from = 1, to = 99) int amount) {
        final int maxStackSize = itemStack.getMaxStackSize();
        final int amountClamped = Math.clamp(amount, 1, 99);
        
        if (amount > maxStackSize) {
            itemStack.setData(DataComponentTypes.MAX_STACK_SIZE, amountClamped);
        }
        
        itemStack.setAmount(amountClamped);
        return this;
    }
    
    /**
     * Sets the repair cost of this {@link ItemBuilder} in levels.
     * <p>Only applicable if item is {@link Repairable}.</p>
     *
     * @param valueInLevels - The repair cost in levels.
     */
    @SelfReturn
    public ItemBuilder setRepairCost(int valueInLevels) {
        return editMeta(Repairable.class, meta -> meta.setRepairCost(valueInLevels));
    }
    
    /**
     * Sets the persistent data of this {@link ItemBuilder}.
     *
     * @param key   - The key of the data.
     * @param type  - The type of the data.
     * @param value - The value to set.
     * @param <T>   - The type of the data.
     * @see PersistentDataContainer
     */
    @SelfReturn
    public <T> ItemBuilder setPersistentData(@NotNull Key key, @NotNull PersistentDataType<T, T> type, @NotNull T value) {
        return editMeta(meta -> meta.getPersistentDataContainer().set(key.asNamespacedKey(), type, value));
    }
    
    /**
     * Gets the value from the persistent data of this {@link ItemBuilder}.
     *
     * @param key  - The key of the data.
     * @param type - The type of the data.
     * @param <T>  - The type of the data.
     * @return the value of persistent data, or {@code null} if unset.
     */
    @Nullable
    public <T> T getPersistentData(@NotNull Key key, @NotNull PersistentDataType<T, T> type) {
        return this.itemStack.getItemMeta().getPersistentDataContainer().get(key.asNamespacedKey(), type);
    }
    
    /**
     * Gets whether a persistent data is set for the given {@link Key} and {@link PersistentDataType}.
     *
     * @param key  - The key of the data.
     * @param type - The type of the data.
     * @param <T>  - The type of the data.
     * @return {@code true} if the value is set; {@code false} otherwise.
     * @see PersistentDataContainer
     */
    public <T> boolean hasPersistentData(@NotNull Key key, @NotNull PersistentDataType<T, T> type) {
        return this.itemStack.getItemMeta().getPersistentDataContainer().has(key.asNamespacedKey(), type);
    }
    
    /**
     * Sets whether to override the enchantment glint.
     *
     * @param glow - {@code true} to add glint, {@code false} to remove.
     */
    @SelfReturn
    public ItemBuilder setEnchantmentGlintOverride(boolean glow) {
        return editMeta(meta -> meta.setEnchantmentGlintOverride(glow));
    }
    
    /**
     * Sets the {@link ArmorTrim} to this {@link ItemBuilder}.
     * <p>Only applicable on {@link ArmorMeta}.</p>
     *
     * @param pattern  - The trim pattern.
     * @param material - The trim material.
     */
    @SelfReturn
    public ItemBuilder setArmorTrim(@NotNull TrimPattern pattern, @NotNull TrimMaterial material) {
        return editMeta(ArmorMeta.class, meta -> meta.setTrim(new ArmorTrim(material, pattern)));
    }
    
    /**
     * Sets whether to hide the tooltip, making it so hovering over an item doesn't display anything.
     *
     * @param hideTooltip - {@code true} to hide tooltip, {@code false} to show.
     */
    @SelfReturn
    public ItemBuilder setHideTooltip(boolean hideTooltip) {
        return editMeta(meta -> meta.setHideTooltip(hideTooltip));
    }
    
    /**
     * Sets the {@link DamageType} this {@link ItemBuilder} is resistant to.
     *
     * @param damageType - The damage types.
     * @see DamageTypeTags
     */
    @SelfReturn
    public ItemBuilder setDamageResistant(@NotNull Tag<DamageType> damageType) {
        return editMeta(meta -> meta.setDamageResistant(damageType));
    }
    
    /**
     * Sets the maximum stack size of this {@link ItemBuilder}.
     *
     * @param maximumStackSize - The maximum stack size.
     */
    @SelfReturn
    public ItemBuilder setMaximumStackSize(@Range(from = 0, to = 99) int maximumStackSize) {
        return editMeta(meta -> meta.setMaxStackSize(Math.clamp(maximumStackSize, 0, 99)));
    }
    
    /**
     * Sets the {@link FoodComponent} of this {@link ItemBuilder}.
     * <p>This method will silently make this item {@link Consumable} in order for food to actually work.</p>
     *
     * @param modifier - The component modifier.
     */
    @SelfReturn
    public ItemBuilder setFood(@NotNull ComponentModifier<FoodComponent> modifier) {
        // We have to add consumable in order for food to work
        itemStack.setData(DataComponentTypes.CONSUMABLE, Consumable.consumable().build());
        
        return editComponent(modifier, ItemMeta::getFood, ItemMeta::setFood);
    }
    
    /**
     * Sets the {@link UseCooldownComponent} of this {@link ItemBuilder}.
     * <p>Note that this is only applicable to vanilla use of the item, {@link ItemFunction} support cooldowns per-function.</p>
     *
     * @param modifier - The component modifier.
     */
    @SelfReturn
    @ApiStatus.Obsolete
    public ItemBuilder setCooldown(@NotNull ComponentModifier<UseCooldownComponent> modifier) {
        return editComponent(modifier, ItemMeta::getUseCooldown, ItemMeta::setUseCooldown);
    }
    
    /**
     * Sets the {@link ToolComponent} of this {@link ItemBuilder}.
     *
     * @param modifier - The component modifier.
     */
    @SelfReturn
    public ItemBuilder setTool(@NotNull ComponentModifier<ToolComponent> modifier) {
        return editComponent(modifier, ItemMeta::getTool, ItemMeta::setTool);
    }
    
    /**
     * Sets the {@link JukeboxPlayableComponent} of this {@link ItemBuilder}.
     *
     * @param modifier - The component modifier.
     */
    @SelfReturn
    public ItemBuilder setJukebox(@NotNull ComponentModifier<JukeboxPlayableComponent> modifier) {
        return editComponent(modifier, ItemMeta::getJukeboxPlayable, ItemMeta::setJukeboxPlayable);
    }
    
    /**
     * Sets the {@link EquippableComponent} of this {@link ItemBuilder}.
     *
     * @param modifier - The component modifier.
     */
    @SelfReturn
    public ItemBuilder setEquippable(@NotNull ComponentModifier<EquippableComponent> modifier) {
        return editComponent(modifier, ItemMeta::getEquippable, ItemMeta::setEquippable);
    }
    
    /**
     * Adds a {@link Pattern} to this {@link ItemBuilder}.
     *
     * @param type  - The pattern type.
     * @param color - The pattern color.
     */
    @SelfReturn
    @MethodApplicableTo(values = {
            Material.WHITE_BANNER,
            Material.LIGHT_GRAY_BANNER,
            Material.GRAY_BANNER,
            Material.BLACK_BANNER,
            Material.BROWN_BANNER,
            Material.RED_BANNER,
            Material.ORANGE_BANNER,
            Material.YELLOW_BANNER,
            Material.LIME_BANNER,
            Material.GREEN_BANNER,
            Material.CYAN_BANNER,
            Material.LIGHT_BLUE_BANNER,
            Material.BLUE_BANNER,
            Material.PURPLE_BANNER,
            Material.MAGENTA_BANNER,
            Material.PINK_BANNER
    })
    public ItemBuilder addBannerPattern(@NotNull PatternType type, @NotNull DyeColor color) {
        return editMeta(BannerMeta.class, meta -> meta.addPattern(new Pattern(color, type)));
    }
    
    /**
     * Sets the given {@link Pattern} on this {@link ItemBuilder}.
     *
     * @param patterns - The patterns to set.
     * @throws IllegalArgumentException if the given patterns are not varargs.
     */
    @SelfReturn
    @MethodApplicableTo(values = {
            Material.WHITE_BANNER,
            Material.LIGHT_GRAY_BANNER,
            Material.GRAY_BANNER,
            Material.BLACK_BANNER,
            Material.BROWN_BANNER,
            Material.RED_BANNER,
            Material.ORANGE_BANNER,
            Material.YELLOW_BANNER,
            Material.LIME_BANNER,
            Material.GREEN_BANNER,
            Material.CYAN_BANNER,
            Material.LIGHT_BLUE_BANNER,
            Material.BLUE_BANNER,
            Material.PURPLE_BANNER,
            Material.MAGENTA_BANNER,
            Material.PINK_BANNER
    })
    public ItemBuilder setBannerPattern(@NotNull @RequiresVarargs Pattern... patterns) {
        return editMeta(BannerMeta.class, meta -> meta.setPatterns(Arrays.asList(Validate.varargs(patterns))));
    }
    
    /**
     * Sets the {@link ItemBuilder} model.
     *
     * @param material - The model to set.
     */
    @SelfReturn
    public ItemBuilder setItemModel(@NotNull Material material) {
        return setItemModel(material.getKey());
    }
    
    /**
     * Sets the {@link ItemBuilder} model.
     *
     * @param key - The model to set.
     */
    @SelfReturn
    public ItemBuilder setItemModel(@NotNull NamespacedKey key) {
        return editMeta(meta -> meta.setItemModel(key));
    }
    
    /**
     * Sets the {@link DataComponentType} on this {@link ItemBuilder}.
     *
     * @param type  - The component type.
     * @param value - The value to set.
     * @param <T>   - The component type.
     * @see DataComponentTypes
     */
    @SelfReturn
    public <T> ItemBuilder setComponent(@NotNull DataComponentType.Valued<T> type, @NotNull T value) {
        this.itemStack.setData(type, value);
        return this;
    }
    
    /**
     * Sets the {@code non-valued} {@link DataComponentType} on this {@link ItemBuilder}.
     *
     * @param type - The component type.
     * @see DataComponentTypes
     */
    @SelfReturn
    public ItemBuilder setComponent(@NotNull DataComponentType.NonValued type) {
        this.itemStack.setData(type);
        return this;
    }
    
    /**
     * Unsets the given {@link DataComponentType} on this {@link ItemBuilder}.
     *
     * <p>
     * This will remove that component from the item, making the item unfunctional for that component, which will also remove unhidable tooltips.
     * </p>
     *
     * @param type - The component type.
     */
    @SelfReturn
    public ItemBuilder unsetComponent(@NotNull DataComponentType type) {
        this.itemStack.unsetData(type);
        return this;
    }
    
    /**
     * Unsets all {@link DataComponentType} defined by the {@link #COMPONENTS_WITH_TOOLTIPS} on this {@link ItemBuilder},
     * which essentially makes the item non-functional for those components, but also guarantees the removal of tooltips that
     * are impossible to hide.
     */
    @SelfReturn
    public ItemBuilder unsetComponents() {
        COMPONENTS_WITH_TOOLTIPS.forEach(itemStack::unsetData);
        
        return this;
    }
    
    // *-* Build operations *-* //
    
    /**
     * Gets a copy of the underlying {@link ItemStack}.
     *
     * <p><b>Calling this method will not register the functions, you must use {@link #build()} for that.</b></p>
     *
     * @return a copy of the underlying {@link ItemStack}.
     */
    @NotNull
    public ItemStack asItemStack() {
        return new ItemStack(this.itemStack);
    }
    
    /**
     * Gets a copy of the underlying {@link ItemStack} as an "icon", which means <b>removing</b> all of its functional components, making the icon
     * non-functional, which is ideas for an "icon".
     *
     * <p><b>Calling this method will not register the functions, you must use {@link #build()} for that.</b></p>
     *
     * @return a copy of the underlying item as an "icon".
     * @see #unsetComponents()
     */
    @NotNull
    public ItemStack asIcon() {
        return unsetComponents().asItemStack();
    }
    
    /**
     * Builds and returns a copy of the underlying {@link ItemStack}, registering local functions is provided.
     *
     * @param overrideFunctions - {@code true} to override function is they're already registered, {@code false} to not.
     * @return a copy of the built item stack.
     */
    @NotNull
    public ItemStack build(boolean overrideFunctions) {
        if (!key.isEmpty()) {
            // Always store the key, even if no functions exist
            editMeta(meta -> meta.getPersistentDataContainer().set(FUNCTION_KEY_PATH, PersistentDataType.STRING, key.getKey()));
            
            // Register functions
            if (localFunctions != null && (!FUNCTIONS.containsKey(key) || overrideFunctions)) {
                FUNCTIONS.put(key, localFunctions);
            }
        }
        
        return new ItemStack(itemStack);
    }
    
    /**
     * Builds and returns a copy of the underlying {@link ItemStack}, registering local functions is provided.
     *
     * @return a copy of the built item stack.
     */
    @NotNull
    public ItemStack build() {
        return build(false);
    }
    
    /**
     * Gets the {@link Key} of this {@link ItemBuilder}.
     *
     * @return the key of this builder.
     */
    @NotNull
    @Override
    public final Key getKey() {
        return key;
    }
    
    // *-* Other helpers *-* //
    
    /**
     * Makes this {@link ItemBuilder} "glow" by overriding the enchantment glint.
     */
    @SelfReturn
    public ItemBuilder glow() {
        return setEnchantmentGlintOverride(true);
    }
    
    /**
     * Applies the given {@link Consumer} on this {@link ItemBuilder} if the given {@code predicate} succeeds.
     *
     * @param predicate - The predicate to check.
     * @param action    - The action to perform.
     */
    @SelfReturn
    public ItemBuilder predicate(boolean predicate, @NotNull Consumer<ItemBuilder> action) {
        if (predicate) {
            action.accept(this);
        }
        
        return this;
    }
    
    /**
     * Edits the {@link ItemMeta} on this {@link ItemBuilder}.
     *
     * @param consumer - The editor.
     */
    @SelfReturn
    public ItemBuilder editMeta(@NotNull Consumer<ItemMeta> consumer) {
        return editMeta(ItemMeta.class, consumer);
    }
    
    @SelfReturn
    public ItemBuilder editLore(@NotNull Consumer<List<Component>> consumer) {
        return editMeta(meta -> {
            final List<Component> existingLore = meta.lore() != null ? meta.lore() : Lists.newArrayList();
            
            consumer.accept(existingLore);
            meta.lore(existingLore);
        });
    }
    
    /**
     * Edits the {@link ItemMeta} on this {@link ItemBuilder}.
     * <p>If the given {@link ItemMeta} {@code class} is not applicable to this builder, it will be silently ignored.</p>
     *
     * @param clazz    - The meta class to edit.
     * @param consumer - The editor.
     * @param <T>      - The type of the meta.
     */
    @SelfReturn
    public <T extends ItemMeta> ItemBuilder editMeta(@NotNull Class<T> clazz, @NotNull Consumer<T> consumer) {
        final ItemMeta meta = itemStack.getItemMeta();
        
        if (clazz.isInstance(meta)) {
            final T cast = clazz.cast(meta);
            
            consumer.accept(cast);
            this.itemStack.setItemMeta(cast);
        }
        
        return this;
    }
    
    /**
     * Clones this {@link ItemBuilder} with a new {@link Key}.
     *
     * @param key - The key of the cloned object.
     * @return a cloned {@link ItemBuilder}.
     */
    @NotNull
    @Override
    public ItemBuilder cloneAs(@NotNull Key key) {
        final ItemBuilder clone = new ItemBuilder(itemStack.clone(), key.nonEmpty());
        
        if (localFunctions != null) {
            clone.localFunctions = localFunctions.clone();
        }
        
        return clone;
    }
    
    @NotNull
    private ItemFunctionList localFunctions() throws IllegalStateException {
        if (this.localFunctions == null) {
            throw new IllegalStateException("Functions aren't supported for anonymous builders!");
        }
        
        return this.localFunctions;
    }
    
    @SelfReturn
    private <E> ItemBuilder editComponent(@NotNull ComponentModifier<E> modifier, @NotNull Function<ItemMeta, E> get, @NotNull BiConsumer<ItemMeta, E> set) {
        editMeta(meta -> {
            final E component = get.apply(meta);
            
            modifier.modify(component);
            set.accept(meta, component);
        });
        
        return this;
    }
    
    /**
     * A static factory method for creating {@link ItemBuilder}.
     *
     * @param itemStack - The item stack to create the builder from.
     * @return a new item builder.
     */
    @NotNull
    public static ItemBuilder of(@NotNull ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }
    
    /**
     * A static factory method for creating {@link ItemBuilder}.
     *
     * @param material - The material to create the builder from.
     * @return a new item builder.
     */
    @NotNull
    public static ItemBuilder of(@NotNull Material material) {
        return new ItemBuilder(material);
    }
    
    /**
     * A static factory method for creating {@link ItemBuilder} with an initial {@code name}.
     *
     * @param material - The material to create the builder from.
     * @param name     - The initial custom name.
     * @return a new item builder.
     */
    @NotNull
    public static ItemBuilder of(@NotNull Material material, @NotNull Component name) {
        return new ItemBuilder(material).setName(name);
    }
    
    /**
     * A static factory method for creating {@link ItemBuilder} with an initial {@code name} and {@code lore}.
     *
     * @param material - The material to create the builder from.
     * @param name     - The initial custom name.
     * @param lore     - The initial lore.
     * @return a new item builder.
     */
    @NotNull
    public static ItemBuilder of(@NotNull Material material, @NotNull Component name, @NotNull @RequiresVarargs @Range(from = 1, to = Byte.MAX_VALUE) Component... lore) {
        final ItemBuilder builder = new ItemBuilder(material).setName(name);
        
        return builder.setLore(Arrays.asList(Validate.varargs(lore)));
    }
    
    /**
     * A static factory method for creating {@link ItemBuilder} of a player head.
     *
     * @param textureHash - The texture hash; Must point to {@code https://textures.minecraft.net/texture/} but without the url part.
     * @return a new item builder.
     */
    @NotNull
    public static ItemBuilder playerHead(@NotNull String textureHash) {
        return new ItemBuilder(Material.PLAYER_HEAD).setHeadTexture(textureHash);
    }
    
    /**
     * A static factory method for creating {@link ItemBuilder} of {@link Material#LEATHER_HELMET} with the given {@link Color}.
     *
     * @param color - The color of the helmet.
     * @return a new item builder.
     */
    @NotNull
    public static ItemBuilder leatherHat(@Nullable Color color) {
        return new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(color);
    }
    
    /**
     * A static factory method for creating {@link ItemBuilder} of {@link Material#LEATHER_CHESTPLATE} with the given {@link Color}.
     *
     * @param color - The color of the chestplate.
     * @return a new item builder.
     */
    @NotNull
    public static ItemBuilder leatherTunic(@Nullable Color color) {
        return new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(color);
    }
    
    /**
     * A static factory method for creating {@link ItemBuilder} of {@link Material#LEATHER_LEGGINGS} with the given {@link Color}.
     *
     * @param color - The color of the leggings.
     * @return a new item builder.
     */
    @NotNull
    public static ItemBuilder leatherPants(@Nullable Color color) {
        return new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(color);
    }
    
    /**
     * A static factory method for creating {@link ItemBuilder} of {@link Material#LEATHER_BOOTS} with the given {@link Color}.
     *
     * @param color - The color of the boots.
     * @return a new item builder.
     */
    @NotNull
    public static ItemBuilder leatherBoots(@Nullable Color color) {
        return new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(color);
    }
    
    /**
     * Internal method for retrieving {@link ItemFunctionList} by a {@link Key}.
     *
     * @param key - The key.
     * @return item functions registered by that key, or {@code null} if not registered.
     */
    @Nullable
    @ApiStatus.Internal
    public static ItemFunctionList functionByKey(@NotNull Key key) {
        return FUNCTIONS.get(key);
    }
    
    /**
     * Gets the {@link ItemBuilder} {@link Key} from the given {@link ItemStack}.
     *
     * @param item - The item to retrieve the key for.
     * @return the item key, or {@link Key#empty()} if not keyed.
     */
    @NotNull
    public static Key getItemKey(@NotNull ItemStack item) {
        final ItemMeta meta = item.getItemMeta();
        
        // Meta could be null for AIR items, so handle that
        if (meta == null) {
            return Key.empty();
        }
        
        final String stringKey = meta.getPersistentDataContainer().get(FUNCTION_KEY_PATH, PersistentDataType.STRING);
        
        return stringKey != null ? Key.ofString(stringKey) : Key.empty();
    }
    
    /**
     * Decodes the given {@code base64} texture into a {@code https://textures.minecraft.net/texture/} but without the url part.
     *
     * @param base64 - The base64 to decode.
     * @return a decoded base64, or empty string if failed to decode.
     */
    @NotNull
    public static String decodeBase64(@NotNull String base64) {
        try {
            final String decodedTexture = new String(Base64.getDecoder().decode(base64));
            final Matcher matcher = BASE64_DECODE_PATTERN.matcher(decodedTexture);
            
            if (matcher.find()) {
                final String texture = matcher.group(1);
                
                return TEXTURE_PATTERN.matcher(texture).replaceFirst("");
            }
        }
        catch (Exception ex) {
            throw EternaLogger.acknowledgeException(ex);
        }
        
        return "";
    }
    
    /**
     * Creates a dummy {@link ItemStack} with a {@link UseCooldownComponent#setCooldownGroup(NamespacedKey)} set to the given {@link Key}.
     *
     * <p>
     * Note that this item is <b>not</b> meant to be given as an actual {@link ItemStack}, and only intended to use as a dummy {@code cooldown}!
     * </p>
     *
     * @param key - The key for the cooldown.
     * @return a new item stack.
     */
    @NotNull
    public static ItemStack createDummyCooldownItem(@NotNull KeyLike key) {
        // Switch to use raw ItemStack because its faster
        final ItemStack itemStack = new ItemStack(Material.EGG);
        itemStack.setData(DataComponentTypes.USE_COOLDOWN, UseCooldown.useCooldown(0.01f).cooldownGroup(key.key().asNamespacedKey()).build());
        
        return itemStack;
    }
    
}