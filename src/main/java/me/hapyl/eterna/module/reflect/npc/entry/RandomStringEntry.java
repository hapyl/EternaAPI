package me.hapyl.eterna.module.reflect.npc.entry;

import me.hapyl.eterna.module.reflect.npc.HumanNPC;
import me.hapyl.eterna.module.util.CollectionUtils;
import me.hapyl.eterna.module.util.Validate;
import org.bukkit.entity.Player;

public class RandomStringEntry extends NPCEntry {

    private final String[] strings;

    public RandomStringEntry(int delay, String... strings) {
        super(delay);
        Validate.isTrue(
                strings.length > 1,
                "there must be at least 2 strings, for primitive entries use StringEntry!"
        );
        this.strings = strings;
    }

    @Override
    public void invokeEntry(HumanNPC npc, Player player) {
        npc.sendNpcMessage(player, CollectionUtils.randomElement(strings, "I don't know what else to say!"));
    }
}
