package test;

import me.hapyl.eterna.module.inventory.builder.ItemBuilder;
import me.hapyl.eterna.module.registry.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

public final class EternaTestItemComponents extends EternaTest {
    
    EternaTestItemComponents(@NotNull Key key) {
        super(key);
    }
    
    @Override
    @SuppressWarnings("UnstableApiUsage")
    public void test(@NotNull EternaTestContext context) throws EternaTestException {
        final ItemBuilder itemFood = new ItemBuilder(Material.DIAMOND_SWORD)
                .setName(Component.text("Food"))
                .setFood(food -> {
                    food.setCanAlwaysEat(true);
                    food.setSaturation(100);
                    food.setNutrition(100);
                });
        
        final ItemBuilder itemTool = new ItemBuilder(Material.IRON_INGOT)
                .setName(Component.text("Tool"))
                .addLore(Component.text("Breaks glass fast", NamedTextColor.AQUA))
                .setTool(tool -> {
                    tool.setDamagePerBlock(1);
                    tool.setDefaultMiningSpeed(1.5f);
                    
                    tool.addRule(Material.GLASS, 5f, true);
                });
        
        final ItemBuilder itemEquippable = new ItemBuilder(Material.MAGENTA_BANNER)
                .setName(Component.text("Equippable"))
                .setEquippable(equippable -> {
                    equippable.setSlot(EquipmentSlot.HEAD);
                    equippable.setEquipSound(Sound.ENTITY_BAT_DEATH);
                });
        
        final PlayerInventory inventory = context.player().getInventory();
        inventory.clear();
        
        inventory.addItem(itemFood.build());
        inventory.addItem(itemTool.build());
        inventory.addItem(itemEquippable.build());
    }
    
}
