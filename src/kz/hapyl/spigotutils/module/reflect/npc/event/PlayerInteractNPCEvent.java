package kz.hapyl.spigotutils.module.reflect.npc.event;

import kz.hapyl.spigotutils.module.reflect.npc.ClickType;
import kz.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.EquipmentSlot;

public class PlayerInteractNPCEvent extends PlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private final HumanNPC npc;
	private final ClickType click;
	private final EquipmentSlot hand;
	private boolean cancelled;

	public PlayerInteractNPCEvent(Player who, HumanNPC npc, ClickType click, EquipmentSlot hand) {
		super(who);
		this.npc = npc;
		this.click = click;
		this.hand = hand;
	}

	public HumanNPC getNpc() {
		return npc;
	}

	public ClickType getClick() {
		return click;
	}

	public EquipmentSlot getHand() {
		return hand;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean b) {
		this.cancelled = b;
	}
}
