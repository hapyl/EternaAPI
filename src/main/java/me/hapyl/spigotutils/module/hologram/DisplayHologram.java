package me.hapyl.spigotutils.module.hologram;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import java.util.List;

public class DisplayHologram {

    private TextDisplay display;
    private final List<String> strings;

    public DisplayHologram() {
        strings = Lists.newArrayList();
    }

}
