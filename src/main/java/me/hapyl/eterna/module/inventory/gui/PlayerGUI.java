package me.hapyl.eterna.module.inventory.gui;

import com.google.common.collect.Maps;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Range;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.*;
import java.util.function.Consumer;

/**
 * todo
 */
public abstract class PlayerGUI {
    
    public static final char ARROW_FORWARD = '➜';
    public static final Material ITEM_ARROW_BACK = Material.AIR;
    public static final Material ITEM_CLOSE_MENU = Material.BARRIER;
    
    public static final ClickType[] ALLOWED_CLICK_TYPES = {
            ClickType.LEFT,
            ClickType.SHIFT_LEFT,
            ClickType.RIGHT,
            ClickType.SHIFT_RIGHT,
            ClickType.WINDOW_BORDER_LEFT,
            ClickType.WINDOW_BORDER_RIGHT,
            ClickType.MIDDLE,
            ClickType.NUMBER_KEY,
            ClickType.DOUBLE_CLICK,
            ClickType.DROP,
            ClickType.CONTROL_DROP,
            ClickType.CREATIVE
    };
    
    protected static final Map<UUID, PlayerGUI> playerInventory = Maps.newHashMap();
    
    protected final Player player;
    
    private final String name;
    private final int size;
    private final Properties properties;
    private final Map<Integer, ActionList> actions;
    private final Set<Integer> ignoredClicks;
    private final Inventory inventory;
    
    protected boolean reopen;
    
    private CancelType cancelType;
    
    public PlayerGUI(@Nonnull Player player, @Nonnull String name, int rows) {
        this.player = player;
        this.name = name;
        this.size = Math.clamp(rows * 9L, 9, 54);
        this.cancelType = CancelType.EITHER;
        this.actions = new HashMap<>();
        this.ignoredClicks = new HashSet<>();
        this.properties = new Properties();
        this.inventory = Bukkit.createInventory(null, this.size, Chat.color(name));
    }
    
    public abstract void onUpdate();
    
    @Nonnull
    public Player player() {
        return player;
    }
    
    @Nonnull
    public CancelType getCancelType() {
        return cancelType;
    }
    
    public void setCancelType(@Nonnull CancelType cancelType) {
        this.cancelType = cancelType;
    }
    
    @Nonnull
    public Properties getProperties() {
        return properties;
    }
    
    public boolean isIgnoredSlot(int slot) {
        return this.ignoredClicks.contains(slot);
    }
    
    public void addIgnoredSlots(int... slots) {
        for (int slot : slots) {
            ignoredClicks.add(slot);
        }
    }
    
    public final void removeIgnoredSlots(int... slots) {
        for (int slot : slots) {
            ignoredClicks.remove(slot);
        }
    }
    
    public void resetItem(int slot) {
        setItem(slot, null);
    }
    
    public void resetAction(int slot) {
        actions.remove(slot);
    }
    
    public void reset(int slot) {
        resetItem(slot);
        resetAction(slot);
    }
    
    @Nullable
    public ItemStack getItem(int slot) {
        return inventory.getItem(slot);
    }
    
    @Nullable
    public ActionList getAction(int slot) {
        return actions.get(slot);
    }
    
    public void clearActions() {
        actions.clear();
    }
    
    public final void clearItems() {
        for (int i = 0; i < size; i++) {
            setItem(i, null);
        }
    }
    
    @Nonnull
    public Map<Integer, ActionList> getActions() {
        return Map.copyOf(actions);
    }
    
    public void setItem(int slot, @Nullable ItemStack item, @Nullable Consumer<Player> action) {
        setItem(slot, item, action != null ? new ActionList(action) : null);
    }
    
    public void setItem(int slot, @Nonnull ItemStack item, @Nonnull StrictAction action) {
        setItem(slot, item, action.makeAction());
    }
    
    public void setItem(int slot, @Nullable ItemStack item, @Nonnull Consumer<Player> action, @Range(from = 1, to = Byte.MAX_VALUE) ClickType... types) {
        Validate.isTrue(types.length > 0, "There must be at least one click type!");
        
        final ActionList actionList = actions.getOrDefault(slot, new ActionList());
        
        for (ClickType type : types) {
            actionList.setAction(type, action);
        }
        
        setItem(slot, item, actionList);
    }
    
    public void setItem(int slot, @Nullable ItemStack item) {
        setItem(slot, item, (ActionList) null);
    }
    
    public void setAction(int slot, @Nonnull Consumer<Player> action) {
        actions.put(slot, new ActionList(action));
    }
    
    public void setAction(int slot, @Nonnull Consumer<Player> leftClick, @Nonnull Consumer<Player> rightClick) {
        setAction(slot, leftClick, ClickType.LEFT);
        setAction(slot, rightClick, ClickType.RIGHT);
    }
    
    public void addClick(int slot, @Nonnull Consumer<Player> action, @Nonnull ClickType... types) {
        setAction(slot, action, types);
    }
    
    public void setAction(int slot, @Nonnull Consumer<Player> action, @Nonnull ClickType... types) {
        Validate.isTrue(types.length > 0, "There must be at least one click type!");
        
        final ActionList actionList = actions.getOrDefault(slot, new ActionList());
        
        for (final ClickType type : types) {
            actionList.setAction(type, action);
        }
        
        actions.put(slot, actionList);
    }
    
    public void setArrowBack(@Nonnull PlayerGUI guiTo) {
        setArrowBack(size - 5, guiTo);
    }
    
    public void setArrowBack(@Nonnull String to, @Nonnull Consumer<Player> action) {
        this.setArrowBack(size - 5, to, action);
    }
    
    public void setArrowBack(int slot, @Nonnull PlayerGUI guiTo) {
        setArrowBack(slot, guiTo.getName(), p -> guiTo.openInventory());
    }
    
    public void setArrowBack(int slot, @Nonnull String to, @Nonnull Consumer<Player> action) {
        setItem(slot, new ItemBuilder(ITEM_ARROW_BACK).setName("&aGo Back").addLore("To " + to).toItemStack(), action);
    }
    
    public void setCloseMenuItem(int slot) {
        setItem(slot, new ItemBuilder(ITEM_CLOSE_MENU).setName("&cClose Menu").toItemStack(), HumanEntity::closeInventory);
    }
    
    public void setCloseMenuItem() {
        setCloseMenuItem(size - 4);
    }
    
    public void fillItem(int from, int to, @Nonnull ItemStack item) {
        fillItem(from, to, item, null);
    }
    
    public void fillItem(int from, int to, @Nonnull ItemStack item, @Nullable Consumer<Player> action) {
        for (int i = from; i <= to; i++) {
            setItem(i, item);
            
            if (action != null) {
                setAction(i, action);
            }
        }
    }
    
    public void fillLine(int line, @Nonnull ItemStack stack) {
        fillLine(line, stack, null);
    }
    
    public void fillLine(int line, @Nonnull ItemStack stack, @Nullable Consumer<Player> action) {
        line = Math.clamp(line, 0, size / 9);
        
        for (int i = 0; i < size; ++i) {
            if (i / 9 == line) {
                setItem(i, stack, action);
            }
        }
    }
    
    public void fillColumn(@Range(from = 0, to = 8) int column, @Nonnull ItemStack stack) {
        fillColumn(column, stack, null);
    }
    
    public void fillColumn(@Range(from = 0, to = 8) int column, @Nonnull ItemStack stack, @Nullable Consumer<Player> action) {
        column = Math.clamp(column, 0, 8);
        
        for (int i = 0; i < getSize(); ++i) {
            if (i % 9 == column) {
                setItem(i, stack, action);
            }
        }
    }
    
    public void fillOuter(@Nonnull ItemStack item) {
        fillOuter(item, null);
    }
    
    public void fillOuter(@Nonnull ItemStack item, @Nullable Consumer<Player> action) {
        for (int i = 0; i < size; i++) {
            if ((i > 0 && i < 9) || (i > size - 9 && i < size - 1) || (i % 9 == 0) || (i % 9 == 8)) {
                setItem(i, item, action);
            }
        }
    }
    
    public void fillInner(@Nonnull ItemStack item) {
        fillInner(item, null);
    }
    
    public void fillInner(@Nonnull ItemStack item, @Nullable Consumer<Player> action) {
        for (int i = 10; i < size - 10; i++) {
            if ((i % 9 != 0) && (i % 9 != 8)) {
                setItem(i, item, action);
            }
        }
    }
    
    @Nonnull
    public String getName() {
        return name;
    }
    
    @Nonnull
    public final SmartComponent newSmartComponent() {
        return new SmartComponent();
    }
    
    @Nonnull
    public Inventory getInventory() {
        return inventory;
    }
    
    @OverridingMethodsMustInvokeSuper
    public void openInventory() {
        if (this instanceof DisabledGUI disabledGUI) {
            Chat.sendMessage(player, disabledGUI.message());
            return;
        }
        
        final PlayerGUI currentGUI = playerInventory.get(player.getUniqueId());
        
        // If the player currently has this GUI while calling openInventory(),
        // it means it was "re-opened", so we mark it as such to prevent calling onClose() etc.
        if (currentGUI != null && currentGUI.equals(this)) {
            reopen = true;
        }
        
        // Store in hashmap
        playerInventory.put(player.getUniqueId(), this);
        
        // GUIs are now self-clearing after each update
        clearActions();
        clearItems();
        
        // Call onUpdate
        onUpdate();
        
        // And finally open the inventory
        player.openInventory(inventory);
    }
    
    public int getSize() {
        return size;
    }
    
    public int getRows() {
        return size / 9;
    }
    
    public final boolean compareInventory(Inventory inventory) {
        return this.inventory == inventory;
    }
    
    protected void setItem(int slot, @Nullable ItemStack item, @Nullable ActionList action) {
        if (slot < 0 || slot > size) {
            throw new IndexOutOfBoundsException("Index %s is out of bounds for %s slots!".formatted(slot, this.size));
        }
        
        inventory.setItem(slot, item != null ? item : new ItemStack(Material.AIR));
        
        // Only set action if it's not null to not override the existing action
        if (action != null) {
            actions.put(slot, action);
        }
    }
    
    final void acceptEvent(int slot, Player player, ClickType clickType) {
        // Respect item clicks means there must be an item on a given slot for click to work
        if (properties.respectItemClick && getItem(slot) == null) {
            return;
        }
        
        final ActionList actionList = actions.get(slot);
        
        if (actionList == null) {
            return;
        }
        
        
        actionList.perClickAction.forEach((type, action) -> {
            if (type == clickType) {
                action.accept(player);
            }
        });
    }
    
    final boolean hasEvent(int slot) {
        return actions.get(slot) != null;
    }
    
    @Nonnull
    public static String menuArrowSplit(@Nonnull String... strings) {
        if (strings.length == 0) {
            return "Default Name";
        }
        if (strings.length == 1) {
            return strings[0];
        }
        
        final StringBuilder builder = new StringBuilder();
        
        for (int i = 0; i < strings.length; i++) {
            if (i != 0) {
                builder.append(" ").append(ARROW_FORWARD).append(" ");
            }
            
            builder.append(strings[i].trim());
        }
        
        return builder.toString();
    }
    
    /**
     * Get player's current GUI that is opened, or null if no GUI.
     *
     * @param player - Player.
     * @return player current GUI that is opened, or null if no GUI.
     */
    @Nullable
    public static PlayerGUI getPlayerGUI(@Nonnull Player player) {
        return playerInventory.getOrDefault(player.getUniqueId(), null);
    }
    
}
