package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.location.Position;
import me.hapyl.eterna.module.player.quest.QuestData;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link QuestObjective} for the completion of which the {@link Player} must travel to a {@link Position}.
 */
public class QuestObjectiveTravelTo extends QuestObjective {
    
    private final Position position;
    
    /**
     * Creates a new {@link QuestObjectiveTravelTo}.
     *
     * @param position - The position to travel to.
     */
    public QuestObjectiveTravelTo(@NotNull Position position) {
        super(Component.text("Travel to around %s.".formatted(position.toStringCentre())), 1);
        
        this.position = position;
    }
    
    @NotNull
    @Override
    public Response test(@NotNull QuestData questData, @NotNull QuestObjectArray array) {
        final Location location = array.get(0, Location.class);
        
        return Response.ofBoolean(location != null && this.position.contains(location));
    }
}
