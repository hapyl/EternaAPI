package me.hapyl.spigotutils.module.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

// this used for addClickEvent
public class ItemAction {

	private final Set<Action> actions;
	private final Consumer<Player> consumer;
	private boolean cancelEvent;

	ItemAction(Consumer<Player> p, Action... t) {
		this.actions = new HashSet<>(Arrays.asList(t));
		this.consumer = p;
		this.cancelEvent = true;
	}

	public void setCancelEvent(boolean cancelEvent) {
		this.cancelEvent = cancelEvent;
	}

	public boolean isCancelEvent() {
		return cancelEvent;
	}

	public void execute(Player player) {
		this.consumer.accept(player);
	}

	public boolean hasAction(Action a) {
		return actions.contains(a);
	}

}