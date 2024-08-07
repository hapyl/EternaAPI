package me.hapyl.eterna.module.reflect.npc.entry;

import me.hapyl.eterna.module.reflect.npc.HumanNPC;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class ActionEntry extends NPCEntry {

    private final Consumer<Player> action;

    public ActionEntry(Consumer<Player> action, int delay) {
        super(delay);
        this.action = action;
    }

    @Override
    public void invokeEntry(HumanNPC npc, Player player) {
        this.action.accept(player);
    }
}
