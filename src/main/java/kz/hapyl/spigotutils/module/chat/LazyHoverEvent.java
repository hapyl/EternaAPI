package kz.hapyl.spigotutils.module.chat;

import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;

public enum LazyHoverEvent {

	TEXT(HoverEvent.Action.SHOW_TEXT),
	SHOW_TEXT(HoverEvent.Action.SHOW_TEXT),
	SHOW_ITEM(HoverEvent.Action.SHOW_ITEM),
	SHOW_ENTITY(HoverEvent.Action.SHOW_ENTITY),
	@Deprecated
	SHOW_ACHIEVEMENT(HoverEvent.Action.SHOW_ACHIEVEMENT);

	private final HoverEvent.Action action;

	LazyHoverEvent(HoverEvent.Action action) {
		this.action = action;
	}

	public HoverEvent of(String value) {
		return new HoverEvent(this.action, new Text(Chat.format(value)));
	}

}
