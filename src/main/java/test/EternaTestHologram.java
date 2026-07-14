package test;

import me.hapyl.eterna.module.command.ArgumentList;
import me.hapyl.eterna.module.component.ComponentList;
import me.hapyl.eterna.module.component.Keybind;
import me.hapyl.eterna.module.hologram.Hologram;
import me.hapyl.eterna.module.hologram.HologramImplTextDisplay;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.scheduler.SchedulerTask;
import me.hapyl.eterna.module.util.StringList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class EternaTestHologram extends EternaTest {
    
    EternaTestHologram(@NotNull Key key) {
        super(key);
    }
    
    @Override
    public void test(@NotNull EternaTestContext context) throws EternaTestException {
        final Player player = context.player();
        final Location location = player.getLocation().add(0, 1, 0);
        
        final String hologramType = context.argument(0).toString().toLowerCase();
        final Hologram hologram;
        
        if (hologramType.equalsIgnoreCase("text_display")) {
            hologram = Hologram.ofTextDisplay(location);
        }
        else if (hologramType.equalsIgnoreCase("armor_stand")) {
            hologram = Hologram.ofArmorStand(location);
        }
        else {
            context.assertTestFailed("Invalid argument, must be either `text_display` or `armor_stand`, not `%s`".formatted(hologramType));
            return;
        }
        
        hologram.setLines(_player -> ComponentList.empty().append(Component.keybind(Keybind.SPRINT)));
        hologram.showAll();
        
        context.info(Component.text("Created `%s` hologram!".formatted(hologram.getClass().getSimpleName())));
        
        context.scheduler()
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("Expanded"));
                   
                   hologram.setLines(_player -> ComponentList.of(
                           Component.text()
                                    .append(Component.text(12345678, NamedTextColor.BLUE))
                                    .append(Component.text(" Hello World", NamedTextColor.RED))
                                    .build(),
                           Component.text(123),
                           null,
                           Component.text("No?")
                   ));
                   
                   if (hologram instanceof HologramImplTextDisplay textDisplay) {
                       textDisplay.background(org.bukkit.Color.fromARGB(100, 0, 0, 0));
                   }
               }, 40))
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("Shrunk"));
                   
                   hologram.setLines(_player -> ComponentList.of(
                           Component.text("Hello", NamedTextColor.RED),
                           Component.text("World", NamedTextColor.BLUE)
                   ));
               }, 40))
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("Per-player lines"));
                   
                   hologram.setLines(_player -> {
                       final String playerName = _player.getName();
                       final ComponentList list = ComponentList.empty().append(Component.text("Your name is %s".formatted(playerName), NamedTextColor.GREEN));
                       
                       if (playerName.equalsIgnoreCase("hapyl")) {
                           list.append(Component.text("Extra line just for you!", NamedTextColor.GOLD));
                       }
                       
                       return list;
                   });
               }, 40))
               .then(SchedulerTask.later(() -> {
                   hologram.dispose();
                   context.assertTestPassed();
               }, 20))
               .execute();
    }
    
    @Override
    public @NotNull StringList tabComplete(@NotNull ArgumentList args) {
        return StringList.of("text_display", "armor_stand");
    }
    
}