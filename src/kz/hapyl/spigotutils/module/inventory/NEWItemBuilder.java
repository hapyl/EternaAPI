package kz.hapyl.spigotutils.module.inventory;

import kz.hapyl.spigotutils.module.annotate.Super;
import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.math.Numbers;
import kz.hapyl.spigotutils.module.util.Validate;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class NEWItemBuilder implements IItemBuilder {

	private final Material material;
	private final ItemStack item;
	private final ItemMeta meta;

	private final String id;
	private final Set<ItemAction> functions;

	@Super
	public NEWItemBuilder(@Nonnull ItemStack stack, @Nullable String id) {
		if (true) {
			throw new IllegalStateException("do no use yet");
		}

		Validate.notNull(stack);
		Validate.notNull(stack.getItemMeta());

		this.item = stack;
		this.meta = stack.getItemMeta();
		this.material = stack.getType();
		this.id = id;
		this.functions = new HashSet<>();
	}

	public NEWItemBuilder(@Nonnull Material material, @Nullable String id) {
		this(new ItemStack(material), id);
	}

	public NEWItemBuilder(@Nonnull Material material) {
		this(material, null);
	}

	@Override
	public NEWItemBuilder setName(String string) {
		meta.setDisplayName(Chat.format(string));
		return this;
	}

	@Override
	public NEWItemBuilder setAmount(int amount) {
		item.setAmount(Numbers.clamp(amount, 0, Byte.MAX_VALUE));
		return this;
	}

	@Override
	public NEWItemBuilder addClickEvent(Consumer<Player> action, Action... actions) {
		functions.add(new ItemAction(action, actions));
		return this;
	}

	@Override
	public NEWItemBuilder addClickEvent(Consumer<Player> action) {
		return addClickEvent(action, Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK);
	}

	@Override
	public NEWItemBuilder setLore(List<String> lore) {
		meta.setLore(lore);
		return this;
	}

	@Override
	public NEWItemBuilder setLore(String lore) {
		meta.setLore(Arrays.asList(Chat.format(lore).split("__")));
		return this;
	}

	@Override
	public NEWItemBuilder addLore() {
		return addLore("");
	}

	@Override
	public NEWItemBuilder addLore(List<String> lore) {
		getLore().addAll(lore);
		return this;
	}

	@Override
	public NEWItemBuilder addLore(String lore) {
		getLore().addAll(Arrays.asList(lore.split("__")));
		return this;
	}

	@Override
	public NEWItemBuilder setSmartLore(String lore) {
		meta.setLore(ItemBuilder.splitString(lore));
		return this;
	}

	@Override
	public NEWItemBuilder addSmartLore(String lore) {
		getLore().addAll(ItemBuilder.splitString(lore));
		return this;
	}

	@Override
	public NEWItemBuilder addSmartLore(String lore, int limit) {
		getLore().addAll(ItemBuilder.splitString(lore, limit));
		return this;
	}

	@Override
	public NEWItemBuilder addSmartLore(String prefix, String lore, int limit) {
		getLore().addAll(ItemBuilder.splitString(prefix, lore, limit));
		return this;
	}

	@Override
	@Nonnull
	public List<String> getLore() {
		return meta.getLore() == null ? new ArrayList<>() : meta.getLore();
	}

	@Override
	public Material getType() {
		return material;
	}

	@Override
	public ItemMeta getMeta() {
		return meta;
	}

	@Override
	public void setMeta(@Nonnull ItemMeta meta) {
		item.setItemMeta(meta);
	}

	@Override
	public BookBuilder bookBuilder() throws IllegalArgumentException {
		if (getType() != Material.WRITTEN_BOOK) {
			throw new IllegalArgumentException("Material must be WRITTEN_BOOK to use BookBuilder!");
		}

		return new BookBuilder(this) {
			private final BookMeta meta = (BookMeta)builder().getMeta();

			@Override
			public BookBuilder setName(String name) {
				meta.setDisplayName(Chat.format(name));
				builder().setMeta(meta);
				return this;
			}

			@Override
			public BookBuilder setAuthor(String author) {
				meta.setAuthor(Chat.format(author));
				builder().setMeta(meta);
				return this;
			}

			@Override
			public BookBuilder setTitle(String title) {
				meta.setTitle(Chat.format(title));
				builder().setMeta(meta);
				return this;
			}

			@Override
			public BookBuilder setBookPages(List<String> pages) {
				meta.setPages(pages);
				builder().setMeta(meta);
				return this;
			}

			@Override
			public BookBuilder setBookPages(BaseComponent[]... base) {
				meta.spigot().setPages(base);
				builder().setMeta(meta);
				return this;
			}

			@Override
			public BookBuilder setBookPage(int page, BaseComponent[] base) {
				meta.spigot().setPage(page, base);
				builder().setMeta(meta);
				return this;
			}
		};
	}

	@Override
	public ItemStack build() {
		return null;
	}
}
