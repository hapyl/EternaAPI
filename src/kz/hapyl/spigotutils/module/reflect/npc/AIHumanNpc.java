package kz.hapyl.spigotutils.module.reflect.npc;

import kz.hapyl.spigotutils.SpigotUtilsPlugin;
import kz.hapyl.spigotutils.module.collection.Pairs;
import kz.hapyl.spigotutils.module.player.PlayerLib;
import kz.hapyl.spigotutils.module.reflect.Ticking;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.EntityZombieHusk;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

/**
 * This class "simulates" NPC AI by using a "ghost" entity. Not the best way of doing this of course. but I'm not the
 */
public class AIHumanNpc extends HumanNPC implements Ticking {

	public static final Pairs<EntityLiving, AIHumanNpc> pairs = new Pairs<>();

	private final EntityLiving ghost;
	private double health;

	public AIHumanNpc(Location location, @Nullable String npcName) {
		this(location, npcName, npcName);
	}

	public AIHumanNpc(Location location, @Nullable String npcName, @Nullable String skinOwner) {
		super(location, npcName, skinOwner);
		this.ghost = new EntityZombieHusk(EntityTypes.N, this.getHuman().getWorldServer()) {
			@Override
			protected void initPathfinder() {
				AIHumanNpc.this.initPathfinder(this, this.bP);
			}
		};
		this.ghost.setPosition(location.getX(), location.getY(), location.getZ());
		health = this.ghost.getHealth();
		pairs.put(this.ghost, this);
	}

	public void initPathfinder(EntityZombieHusk entity, PathfinderGoalSelector pgs) {
		pgs.a(0, new PathfinderGoalMoveTowardsRestriction(entity, 1.0d));
		pgs.a(2, new PathfinderGoalRandomStrollLand(entity, 1.0d));
		pgs.a(4, new PathfinderGoalRandomLookaround(entity));
		pgs.a(8, new PathfinderGoalLookAtPlayer(entity, EntityPlayer.class, 8.0f));
	}

	protected EntityLiving getGhost() {
		return ghost;
	}

	@Override
	public void show(Player... players) {
		super.show(players);
		this.getHuman().getWorldServer().addEntity(this.ghost);
		SpigotUtilsPlugin.runTaskLater((task) -> {
			this.ghost.setInvisible(true);
			this.ghost.setSilent(true);
			this.ghost.collides = false;
		}, 2);
	}

	@Override
	public void tick() {
		if (ghost.isRemoved()) {
			AIHumanNpc.pairs.removeByRight(this);
			this.remove();
			return;
		}
		final double x = ghost.locX();
		final double y = ghost.locY();
		final double z = ghost.locZ();
		final float yaw = ghost.getYRot();
		final float pitch = ghost.getXRot();
		this.teleport(x, y, z, yaw, pitch);
		this.setHeadRotation(yaw);

		if (this.ghost.getHealth() < health) {
			this.playAnimation(NPCAnimation.TAKE_DAMAGE);
			PlayerLib.playSound(this.getLocation(), Sound.ENTITY_PLAYER_HURT, 1.0f);
			health = this.ghost.getHealth();
		}

	}

	public static void removeAll() {
		pairs.forEach((a, b) -> b.remove());
	}

	@Override
	public void remove() {
		pairs.removeByLeft(this.ghost);
		this.ghost.die();
		super.remove();
	}
}
