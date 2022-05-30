package me.hapyl.spigotutils.module.hologram;

import org.bukkit.entity.Player;

public class FollowPlayer extends HologramAction<Player> {

	private final Player playerToFollow;

	public FollowPlayer(Hologram hologram, Player playerToFollow) {
		super(hologram, playerToFollow);
		this.playerToFollow = playerToFollow;
	}

	@Override
	public Player getObject() {
		return playerToFollow;
	}

	public Player getPlayer() {
		return playerToFollow;
	}

	@Override
	public void consume(Player... receivers) {
		this.getHologram().move(this.getObject().getLocation().add(0, 1, 0), receivers);
	}
}
