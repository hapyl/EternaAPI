package me.hapyl.eterna.module.quest.objectives;

import me.hapyl.eterna.module.quest.QuestObjective;
import me.hapyl.eterna.module.quest.QuestObjectiveType;
import me.hapyl.eterna.module.util.BukkitUtils;
import org.bukkit.Location;

public class TravelTo extends QuestObjective {

    private final Location location;
    private final double error;

    public TravelTo(Location location, double error) {
        super(
                QuestObjectiveType.TRAVEL_TO,
                1,
                "I know exactly where to go!",
                String.format("Travel to %s.", BukkitUtils.locationToString(location))
        );
        this.location = location;
        this.error = error;
    }

    @Override
    public double testQuestCompletion(Object... objects) {
        if (objects.length == 1) {
            return (((Location) objects[0]).distance(location) <= this.error) ? 1.0d : -1.0d;
        }
        return -1.0d;
    }
}
