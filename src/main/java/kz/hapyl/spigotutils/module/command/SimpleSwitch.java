package kz.hapyl.spigotutils.module.command;

import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class SimpleSwitch {

	private final ArgumentProcessor    processor;
	private final Map<Integer, String> complex;

	private Consumer<CommandSender> action;

	public SimpleSwitch(ArgumentProcessor processor, int pos, String string) {
		this.processor = processor;
		this.complex = new HashMap<>();
		this.complex.put(pos, string);
	}

	public ArgumentProcessor then(Consumer<CommandSender> action) {
		this.action = action;
		return this.processor;
	}

	public SimpleSwitch and(int nextPos, String str) {
		if (this.complex.size() - 1 >= nextPos) {
			throw new IllegalArgumentException("nextPos must be greater than current pos");
		}
		this.complex.put(nextPos, str);
		return this;
	}

	public SimpleSwitch andNext(String str) {
		return this.and(this.latest(), str);
	}

	public void invoke(CommandSender sender) {
		if (this.action != null) {
			this.action.accept(sender);
		}
	}

	public boolean test(String[] args) {
		boolean flag = false;
		for (final Integer i : this.complex.keySet()) {
			flag = args.length - 1 >= i && this.complex.get(i).equalsIgnoreCase(args[i]);
		}
		return flag;
	}

	private int latest() {
		final Set<Integer> keys = this.complex.keySet();
		int pos = 0;
		for (final Integer integer : keys) {
			if (pos == keys.size() - 1) {
				return integer;
			}
			++pos;
		}
		return this.complex.size();
	}

	public void tabComplete(List<String> strings, String[] args) {
	}
}
