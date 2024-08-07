package me.hapyl.eterna.module.quest.objectives;

import me.hapyl.eterna.module.quest.TravelType;
import me.hapyl.eterna.module.reflect.npc.HumanNPC;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class Objectives {

    public static BreakBlocks breakBlocks(Material material, int amount) {
        return new BreakBlocks(material, amount);
    }

    public static BreakItem breakItem(Material material, int times) {
        return new BreakItem(material, times);
    }

    public static BreedAnimals breedAnimals(EntityType type, int times) {
        return new BreedAnimals(type, times);
    }

    public static CraftItem craftItem(Material material, int times) {
        return new CraftItem(material, times);
    }

    public static DealDamage dealDamage(long/*long?*/ damage) {
        return new DealDamage(damage);
    }

    public static DealDamageTo dealDamageTo(EntityType type, long damage) {
        return new DealDamageTo(damage, type);
    }

    public static FinishDialogue finishDialogue(HumanNPC npc) {
        return new FinishDialogue(npc);
    }

    public static GiveItemStackToNpc giveItemStackToNpc(HumanNPC npc, ItemStack item) {
        return new GiveItemStackToNpc(item, npc);
    }

    public static GiveItemsToNpc giveItemsToNpc(HumanNPC npc, Material type, int amount) {
        return new GiveItemsToNpc(type, amount, npc);
    }

    public static Jump jump(int jump) {
        return new Jump(jump);
    }

    public static PlaceBlocks placeBlocks(Material type, int times) {
        return new PlaceBlocks(type, times);
    }

    public static PlayNote playNote(Instrument instrument, Note note, int times) {
        return new PlayNote(instrument, note, times);
    }

    public static SayInChat talkInChat(String message, boolean ignoreCase) {
        return new SayInChat(message, ignoreCase);
    }

    public static SlayEntity slayEntity(EntityType type, int times) {
        return new SlayEntity(type, times);
    }

    public static TakeDamage takeDamage(double amount) {
        return new TakeDamage(amount);
    }

    public static TakeDamageFrom takeDamageFrom(EntityType type, double amount) {
        return new TakeDamageFrom(amount, type);
    }

    public static TakeDamageFromCause takeDamageFromCause(EntityDamageEvent.DamageCause cause, double amount) {
        return new TakeDamageFromCause(amount, cause);
    }

    public static TalkToNpc talkToNpc(HumanNPC npc, int times) {
        return new TalkToNpc(npc, times);
    }

    public static TravelDistance travelDistance(TravelType type, double distance) {
        return new TravelDistance(type, distance);
    }

    public static TravelTo travelTo(Location location, double error) {
        return new TravelTo(location, error);
    }

    public static UseCustomItem useCustomItem(String itemId, int times) {
        return new UseCustomItem(itemId, times);
    }

}
