package kz.hapyl.spigotutils.module.inventory;

import kz.hapyl.spigotutils.module.util.Builder;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

public interface IItemBuilder extends Builder<ItemStack> {

	IItemBuilder setName(String string);

	IItemBuilder setAmount(int amount);

	IItemBuilder setLore(List<String> lore);

	IItemBuilder setLore(String lore);

	IItemBuilder addLore();

	IItemBuilder addLore(List<String> lore);

	IItemBuilder addLore(String lore);

	IItemBuilder setSmartLore(String lore);

	IItemBuilder addSmartLore(String lore);

	IItemBuilder addSmartLore(String lore, int limit);

	IItemBuilder addSmartLore(String prefix, String lore, int limit);

	IItemBuilder addClickEvent(Consumer<Player> action, Action... actions);

	IItemBuilder addClickEvent(Consumer<Player> action);

	@Nonnull
	List<String> getLore();

	Material getType();

	ItemMeta getMeta();


	void setMeta(@Nonnull ItemMeta meta);

	BookBuilder bookBuilder() throws IllegalArgumentException;

	abstract class BookBuilder extends BuilderComponent {

		public BookBuilder(IItemBuilder builder) {
			super(builder);
		}

		public abstract BookBuilder setName(String name);

		public abstract BookBuilder setAuthor(String author);

		public abstract BookBuilder setTitle(String title);

		public abstract BookBuilder setBookPages(List<String> pages);

		public abstract BookBuilder setBookPages(BaseComponent[]... base);

		public abstract BookBuilder setBookPage(int page, BaseComponent[] base);

	}


}
