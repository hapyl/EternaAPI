package test;

import me.hapyl.eterna.module.location.LocationHelper;
import me.hapyl.eterna.module.parkour.Parkour;
import me.hapyl.eterna.module.parkour.ParkourPosition;
import me.hapyl.eterna.module.registry.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public final class EternaTestParkour extends EternaTest {
    
    private Parkour parkour;
    
    EternaTestParkour(@NotNull Key key) {
        super(key);
    }
    
    @Override
    public void test(@NotNull EternaTestContext context) throws EternaTestException {
        register(context);
    }
    
    private void register(@NotNull EternaTestContext context) {
        if (parkour == null) {
            final World defaultWorld = LocationHelper.defaultWorld();
            
            parkour = new Parkour(
                    Key.ofString("test_parkour"),
                    Component.text("My Test Parkour"),
                    ParkourPosition.builder(
                                           ParkourPosition.of(defaultWorld, -44, 74, 2),
                                           ParkourPosition.of(defaultWorld, -37, 77, 5)
                                   )
                                   .checkpoint(ParkourPosition.of(defaultWorld, -41, 75, 2, 0, 0))
                                   .checkpoint(ParkourPosition.of(defaultWorld, -38, 76, 2, 0, 0))
                                   .checkpoint(ParkourPosition.of(defaultWorld, -40, 77, 5, 0, 0))
            );
            
            parkour.setQuitLocation(LocationHelper.defaultLocation(-44, 74, -1));
            
            context.player().teleport(parkour.getStart().getLocation().add(1, 0, 0));
            
            context.info(Component.text("Registered parkour"));
            context.assertTestPassed();
        }
        else {
            context.player().teleport(parkour.getStart().getLocation().add(1, 0, 0));
            
            context.warning(Component.text("Parkour already created!"));
            context.assertTestPassed();
        }
    }
    
}