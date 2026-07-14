package test;

import me.hapyl.eterna.module.entity.packet.PacketBlockDisplay;
import me.hapyl.eterna.module.entity.packet.PacketGuardian;
import me.hapyl.eterna.module.entity.packet.PacketItem;
import me.hapyl.eterna.module.entity.packet.PacketSquid;
import me.hapyl.eterna.module.inventory.builder.ItemBuilder;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.scheduler.SchedulerTask;
import net.kyori.adventure.text.Component;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public final class EternaTestPacketEntity extends EternaTest {
    
    EternaTestPacketEntity(@NotNull Key key) {
        super(key);
    }
    
    @Override
    public void test(@NotNull EternaTestContext context) throws EternaTestException {
        final Player player = context.player();
        final Location location = player.getLocation();
        
        // Packet item
        final ItemStack itemStackInitial = new ItemBuilder(Material.STONE).asItemStack();
        final ItemStack itemStackReplace = new ItemBuilder(Material.COPPER_SWORD).addEnchant(Enchantment.SWEEPING_EDGE, 1).asItemStack();
        
        final PacketItem packetItem = new PacketItem(location, itemStackInitial);
        packetItem.setGravity(false);
        packetItem.showAll();
        
        // Packet block display
        final PacketBlockDisplay packetBlockDisplay = new PacketBlockDisplay(location);
        
        // Packet guardian & squid
        final PacketGuardian packetGuardian = new PacketGuardian(location.clone().add(0, 5, 0));
        final PacketSquid packetSquid = new PacketSquid(location);
        
        context.info(Component.text("Spawned packet item"));
        
        context.scheduler()
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("Replace item"));
                   
                   packetItem.setItem(itemStackReplace);
               }, 40))
               .then(SchedulerTask.later(() -> {
                   packetItem.dispose();
                   
                   context.info(Component.text("Spawned packet block display"));
                   
                   packetBlockDisplay.setBlockData(Material.STONE);
                   packetBlockDisplay.setTransformation(new Matrix4f(
                           0.5000f, 0.0000f, 0.0000f, -0.2500f,
                           0.0000f, 0.5000f, 0.0000f, -0.2500f,
                           0.0000f, 0.0000f, 0.5000f, -0.2500f,
                           0.0000f, 0.0000f, 0.0000f, 1.0000f
                   ));
                   
                   packetBlockDisplay.showAll();
               }, 40))
               .then(SchedulerTask.later(() -> {
                   packetBlockDisplay.dispose();
                   
                   context.info(Component.text("Spawned packet guardian & squid"));
                   
                   packetGuardian.setVisible(false);
                   packetSquid.setVisible(true);
                   
                   packetGuardian.setAttribute(Attributes.SCALE, 0.1);
                   
                   packetGuardian.showAll();
                   packetSquid.showAll();
                   
                   packetSquid.setAttribute(Attributes.SCALE, 2);
                   packetSquid.updateAttributes();
                   
                   packetGuardian.setBeamTarget(packetSquid);
               }, 40))
               .then(SchedulerTask.later(() -> {
                   packetGuardian.dispose();
                   packetSquid.dispose();
                   
                   context.assertTestPassed();
               }, 40))
               .execute();
    }
    
}