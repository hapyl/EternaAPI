package kz.hapyl.spigotutils.module.command.jcmd;

import kz.hapyl.spigotutils.module.command.jcmd.argument.Argument;
import kz.hapyl.spigotutils.module.util.Action;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;

public class CommandArgument<T> {

	private final JCommand parent;
	private final Map<Integer, Argument<T>> map;
	private int index;

	protected CommandArgument(JCommand parent) {
		this.parent = parent;
		this.map = new LinkedHashMap<>();
		this.index = 0;
	}

	public JCommand with(Action<Player> player) {
		return this.parent;
	}

	public CommandArgument<T> push() {
		if (map.get(index) == null) {
			throw new IllegalStateException("Cannot push null element.");
		}
		++index;
		return this;
	}
}
