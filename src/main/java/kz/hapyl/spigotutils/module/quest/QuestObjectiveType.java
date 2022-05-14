package kz.hapyl.spigotutils.module.quest;

import kz.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.entity.Breedable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public enum QuestObjectiveType {

	// TRADE_WITH_VILLAGER(Villager.class),

	SLAY_ENTITY(LivingEntity.class, long.class),
	PLACE_BLOCKS(Material.class),
	BREAK_BLOCKS(Material.class),
	TALK_TO_NPC(HumanNPC.class),
	FINISH_DIALOGUE(HumanNPC.class),
	DEAL_DAMAGE(double.class),
	DEAL_DAMAGE_TO(double.class, LivingEntity.class),
	TAKE_DAMAGE(double.class),
	TAKE_DAMAGE_FROM(double.class, LivingEntity.class),
	GIVE_ITEMS_TO_NPC(Material.class, int.class, HumanNPC.class),
	GIVE_ITEM_STACK_TO_NPC(ItemStack.class, HumanNPC.class),
	CRAFT_ITEM(Material.class),
	BREAK_ITEM(Damageable.class),
	TRAVEL_DISTANCE(TravelType.class, double.class),
	TRAVEL_TO(Location.class, double.class),
	PLAY_NOTE(Instrument.class, Note.class),
	SAY_IN_CHAT(String.class),
	USE_CUSTOM_ITEM(String.class/*id*/),
	JUMP(),
	BREED_ANIMALS(Breedable.class),
	CUSTOM(/* use for custom events checks */);

	private final Class<?>[] accepting;
	private final boolean allowOverride;

	QuestObjectiveType(Class<?>... accepting) {
		this.accepting = accepting;
		this.allowOverride = true;
	}

	public void validateAcceptingClasses(Object... objects) {
		if (this.accepting.length == 0) {
			return;
		}
		if (this.accepting.length != objects.length) {
			throw new IllegalArgumentException(String.format("%s accepts %s objects, not %s!", this.name(), this.accepting.length, objects.length));
		}
		for (int i = 0; i < this.accepting.length; i++) {
			final Class<?> requiredClass = this.accepting[i];
			final Class<?> comparingClass = objects[i].getClass();
			if (requiredClass != comparingClass) {
				throw new IllegalArgumentException(String.format("%s requires %s at %s, not %s!",
						this.name(),
						requiredClass.getSimpleName(), i,
						comparingClass.getSimpleName()));
			}
		}
	}

	public void validateAcceptingClasses(Class<?>... classes) {
		if (this.accepting.length == 0) {
			return;
		}
		if (classes.length != this.accepting.length) {
			throw new IllegalArgumentException(String.format("%s accepts %s classes, not %s!", this.name(), this.accepting.length, classes.length));
		}
		for (int i = 0; i < this.accepting.length; i++) {
			final Class<?> requiredClass = this.accepting[i];
			final Class<?> comparingClass = classes[i];
			if (requiredClass != comparingClass) {
				throw new IllegalArgumentException(String.format("%s requires %s at %s, not %s!",
						this.name(),
						requiredClass.getSimpleName(), i,
						comparingClass.getSimpleName()));
			}
		}
	}

	public boolean canBeOverridden() {
		return allowOverride;
	}
}
