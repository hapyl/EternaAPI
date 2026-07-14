package test;

import me.hapyl.eterna.module.particle.ParticleBuilder;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.scheduler.SchedulerTask;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class EternaTestParticleBuilder extends EternaTest {
    
    EternaTestParticleBuilder(@NotNull Key key) {
        super(key);
    }
    
    @Override
    public void test(@NotNull EternaTestContext context) throws EternaTestException {
        final Player player = context.player();
        final Location location = player.getLocation();
        
        context.scheduler()
               .then(SchedulerTask.run(() -> {
                   context.info(Component.text("particle"));
                   ParticleBuilder.particle(Particle.ENCHANT).display(location);
               }))
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("effect"));
                   ParticleBuilder.effect(org.bukkit.Color.fromRGB(0, 255, 0), 0).display(location);
               }, 10))
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("instantEffect"));
                   ParticleBuilder.instantEffect(org.bukkit.Color.fromRGB(30, 39, 227), 0).display(location);
               }, 10))
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("entityEffect"));
                   ParticleBuilder.entityEffect(org.bukkit.Color.fromRGB(35, 46, 124)).display(location);
               }, 10))
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("dust"));
                   ParticleBuilder.dust(org.bukkit.Color.fromRGB(89, 127, 6), 2).display(location);
               }, 10))
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("item"));
                   ParticleBuilder.item(new ItemStack(Material.DIAMOND_AXE)).display(location);
               }, 10))
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("block"));
                   ParticleBuilder.block(Material.LAPIS_BLOCK).display(location);
               }, 10))
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("dragonBreath"));
                   ParticleBuilder.dragonBreath(2).display(location);
               }, 10))
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("fallingDust"));
                   ParticleBuilder.fallingDust(Material.BRICKS).display(location);
               }, 10))
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("flash"));
                   ParticleBuilder.flash(org.bukkit.Color.fromRGB(1, 2, 3)).display(location);
               }, 10))
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("dustColorTransition"));
                   ParticleBuilder.dustColorTransition(org.bukkit.Color.fromRGB(1, 2, 3), org.bukkit.Color.fromRGB(200, 100, 50), 1).display(location);
               }, 10))
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("vibration"));
                   ParticleBuilder.vibration(player.getLocation().add(4, 2, -5), 100).display(location);
               }, 10))
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("sculkCharge"));
                   ParticleBuilder.sculkCharge((float) Math.toRadians(90)).display(location);
               }, 10))
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("tintedLeaves"));
                   ParticleBuilder.tintedLeaves(org.bukkit.Color.fromRGB(69, 69, 69)).display(location);
               }, 10))
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("dustPillar"));
                   ParticleBuilder.dustPillar(Material.GOLD_BLOCK).display(location);
               }, 10))
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("blockCrumble"));
                   ParticleBuilder.blockCrumble(Material.DIAMOND_BLOCK).display(location);
               }, 10))
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("trail"));
                   ParticleBuilder.trail(player.getLocation().add(3, -5, 6), org.bukkit.Color.fromRGB(55, 105, 205), 100).display(location);
               }, 10))
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("blockMarker"));
                   ParticleBuilder.blockMarker(Material.SEA_LANTERN).display(location);
               }, 10))
               .then(SchedulerTask.later(() -> {
                   context.assertThrows(() -> ParticleBuilder.block(Material.DIAMOND));
                   context.assertThrows(() -> ParticleBuilder.fallingDust(Material.GOLD_NUGGET));
                   context.assertThrows(() -> ParticleBuilder.blockMarker(Material.EMERALD));
                   
                   context.assertTestPassed();
               }, 10))
               .execute()
               .exceptionally(ex -> {
                   context.assertTestFailed(ex);
                   return null;
               });
        
    }
    
}