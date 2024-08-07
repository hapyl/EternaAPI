package me.hapyl.eterna.module.quest;

import me.hapyl.eterna.module.reflect.npc.HumanNPC;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.entity.Breedable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public enum QuestObjectiveType {

    // TRADE_WITH_VILLAGER(Villager.class),

    /**
     * Kill an entity.
     */
    SLAY_ENTITY(LivingEntity.class, long.class),
    /**
     * Place a block type.
     */
    PLACE_BLOCKS(Material.class),
    /**
     * Break a block type.
     */
    BREAK_BLOCKS(Material.class),
    /**
     * Talk to {@link HumanNPC}.
     */
    TALK_TO_NPC(HumanNPC.class),
    /**
     * Finish dialogue of {@link HumanNPC}.
     */
    FINISH_DIALOGUE(HumanNPC.class),
    /**
     * Deal any damage.
     */
    DEAL_DAMAGE(double.class),
    /**
     * Deal any damage to {@link LivingEntity}.
     */
    DEAL_DAMAGE_TO(double.class, LivingEntity.class),
    /**
     * Take damage from any source.
     */
    TAKE_DAMAGE(double.class),
    /**
     * Take damage from {@link LivingEntity}.
     */
    TAKE_DAMAGE_FROM(double.class, LivingEntity.class),
    /**
     * Take damage from specific cause.
     */
    TAKE_DAMAGE_FROM_CAUSE(double.class, EntityDamageEvent.DamageCause.class),
    /**
     * Give {@link Material} to {@link HumanNPC}.
     */
    GIVE_ITEMS_TO_NPC(Material.class, int.class, HumanNPC.class),
    /**
     * Give {@link ItemStack} to {@link HumanNPC}.
     */
    GIVE_ITEM_STACK_TO_NPC(ItemStack.class, HumanNPC.class),
    /**
     * Craft item.
     */
    CRAFT_ITEM(Material.class),
    /**
     * Break a tool.
     */
    BREAK_ITEM(Damageable.class),
    /**
     * Travel distance of {@link TravelType}.
     */
    TRAVEL_DISTANCE(TravelType.class, double.class),
    /**
     * Travel to specific location.
     */
    TRAVEL_TO(Location.class, double.class),
    /**
     * Play note on a note block.
     */
    PLAY_NOTE(Instrument.class, Note.class),
    /**
     * Talk in chat.
     */
    SAY_IN_CHAT(String.class),
    /**
     * Use {@link me.hapyl.eterna.module.inventory.item.CustomItem}.
     */
    USE_CUSTOM_ITEM(String.class/*id*/),
    /**
     * Jump.
     */
    JUMP(),
    /**
     * Breed animals.
     */
    BREED_ANIMALS(Breedable.class),
    /**
     * Used for custom events.
     */
    CUSTOM();

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
            throw new IllegalArgumentException(String.format(
                    "%s accepts %s objects, not %s!",
                    this.name(),
                    this.accepting.length,
                    objects.length
            ));
        }
        for (int i = 0; i < this.accepting.length; i++) {
            final Class<?> requiredClass = this.accepting[i];
            final Class<?> comparingClass = objects[i].getClass();
            if (requiredClass != comparingClass) {
                throw new IllegalArgumentException(String.format("%s requires %s at %s, not %s!",
                                                                 this.name(),
                                                                 requiredClass.getSimpleName(), i,
                                                                 comparingClass.getSimpleName()
                ));
            }
        }
    }

    public void validateAcceptingClasses(Class<?>... classes) {
        if (this.accepting.length == 0) {
            return;
        }
        if (classes.length != this.accepting.length) {
            throw new IllegalArgumentException(String.format(
                    "%s accepts %s classes, not %s!",
                    this.name(),
                    this.accepting.length,
                    classes.length
            ));
        }
        for (int i = 0; i < this.accepting.length; i++) {
            final Class<?> requiredClass = this.accepting[i];
            final Class<?> comparingClass = classes[i];
            if (requiredClass != comparingClass) {
                throw new IllegalArgumentException(String.format("%s requires %s at %s, not %s!",
                                                                 this.name(),
                                                                 requiredClass.getSimpleName(), i,
                                                                 comparingClass.getSimpleName()
                ));
            }
        }
    }

    public boolean canBeOverridden() {
        return allowOverride;
    }
}
