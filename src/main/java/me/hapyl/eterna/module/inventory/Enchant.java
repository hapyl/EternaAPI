package me.hapyl.eterna.module.inventory;

import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * A simple enum that contains all enchantments in Minecraft, with their bukkit enchantment, material, description and max level.
 * Some are renamed to represent their minecraft name.
 *
 * @apiNote Spigot renamed these to be the same as vanilla as of 1.20.6
 */
public enum Enchant {

    SHARPNESS(Enchantment.SHARPNESS, Material.IRON_SWORD, "Increases damage dealt to all entities.", 5),
    SMITE(Enchantment.SMITE, Material.ROTTEN_FLESH, "Increases damage dealt to undead monsters.", 5),
    BANE_OF_ARTHROPODS(Enchantment.BANE_OF_ARTHROPODS, Material.SPIDER_EYE, "Increases damage dealt to spiders.", 5),
    UNBREAKING(Enchantment.UNBREAKING, Material.DIAMOND, "Increases durability of the item.", 3),
    PROTECTION(Enchantment.PROTECTION, Material.IRON_CHESTPLATE, "Increases protection from all sources.", 4),
    BLAST_PROTECTION(Enchantment.BLAST_PROTECTION, Material.GUNPOWDER, "Increases protection from explosions.", 4),
    FIRE_PROTECTION(Enchantment.FIRE_PROTECTION, Material.BLAZE_POWDER, "Increases protection from fire.", 4),
    PROJECTILE_PROTECTION(Enchantment.PROJECTILE_PROTECTION, Material.ARROW, "Increases protection from all arrows.", 4),
    FEATHER_FALLING(Enchantment.FEATHER_FALLING, Material.FEATHER, "Reduces fall damage.", 4),
    EFFICIENCY(Enchantment.EFFICIENCY, Material.IRON_PICKAXE, "Increases speed of the tool.", 5),
    SILK_TOUCH(Enchantment.SILK_TOUCH, Material.VINE, "Blocks drop their natural form instead of drop table.", 1),
    FORTUNE(Enchantment.FORTUNE, Material.RABBIT_FOOT, "Increases drops from loot tables.", 3),
    LUCK_OF_THE_SEA(Enchantment.LUCK_OF_THE_SEA, Material.PRISMARINE_CRYSTALS, "Increases chance of getting treasure.", 3),
    LURE(Enchantment.LURE, Material.TROPICAL_FISH, "Decreases fishing time.", 3),
    RESPIRATION(Enchantment.RESPIRATION, Material.PRISMARINE_CRYSTALS, "Extends underwater breathing time.", 3),
    AQUA_AFFINITY(Enchantment.AQUA_AFFINITY, Material.TURTLE_HELMET, "Ignores the water mining speed penalty.", 1),
    THORNS(Enchantment.THORNS, Material.CACTUS, "Bounds damage taken back to the damager.", 4),
    DEPTH_STRIDER(Enchantment.DEPTH_STRIDER, Material.WATER_BUCKET, "Increases underwater walk speed.", 3),
    FROST_WALKER(Enchantment.FROST_WALKER, Material.ICE, "Transforms nearby water into ice.", 2),
    SOUL_SPEED(Enchantment.SOUL_SPEED, Material.SOUL_SAND, "Increases walk speed while on Soul Sand.", 3),
    KNOCKBACK(Enchantment.KNOCKBACK, Material.SLIME_BALL, "Knocks hit enemies back.", 2),
    FIRE_ASPECT(Enchantment.FIRE_ASPECT, Material.BLAZE_POWDER, "Sets enemies on fire.", 1),
    LOOTING(Enchantment.LOOTING, Material.LEATHER, "Increases the amount of drops from mobs.", 3),
    SWEEPING_EDGE(
            Enchantment.SWEEPING_EDGE,
            Material.DIAMOND_SWORD,
            "Increases the damage dealt to mobs by each hit from a sweep attack.",
            3
    ),
    POWER(Enchantment.POWER, Material.ARROW, "Increases arrow damage.", 5),
    PUNCH(Enchantment.PUNCH, Material.SLIME_BALL, "Knocks hit mobs back.", 2),
    FLAME(Enchantment.FLAME, Material.BLAZE_POWDER, "Shoots fire arrows that sets hit mobs on fire.", 1),
    INFINITY(Enchantment.INFINITY, Material.SPECTRAL_ARROW, "Allows to shoot infinite arrows if you have at least 1 arrow.", 1),
    LOYALTY(Enchantment.LOYALTY, Material.LEAD, "Flies back to you upon hit.", 3),
    IMPALING(Enchantment.IMPALING, Material.PRISMARINE_SHARD, "Increases damage dealt to water mobs.", 5),
    RIPTIDE(Enchantment.RIPTIDE, Material.ELYTRA, "Launches player forward whenever they're on water.", 3),
    CHANNELING(Enchantment.CHANNELING, Material.NETHER_STAR, "Summons a lightning bolt when a mob is hit.", 1),
    MULTISHOT(Enchantment.MULTISHOT, Material.ARROW, "Shoots 3 arrows on each shot, sending them in different directions.", 1),
    QUICK_CHARGE(Enchantment.QUICK_CHARGE, Material.DRIED_KELP, "Reduces the time to load a crossbow.", 5),
    PIERCING(Enchantment.PIERCING, Material.POWERED_RAIL, "Arrows can pass through entities.", 4),
    MENDING(Enchantment.MENDING, Material.EXPERIENCE_BOTTLE, "Repairs item upon picking up experience orb", 1),
    CURSE_OF_BINDING(Enchantment.BINDING_CURSE, Material.BARRIER, "Prevents removal of cursed item.", 1),
    CURSE_OF_VANISHING(Enchantment.VANISHING_CURSE, Material.LAVA_BUCKET, "Item vanish upon death.", 1);

    private final static Map<Enchantment, Enchant> byBukkit = Maps.newHashMap();

    static {
        for (Enchant value : values()) {
            byBukkit.put(value.getAsBukkit(), value);
        }
    }

    private final Enchantment bukkit;
    private final Material material;
    private final String description;
    private final int vanillaMaxLvl;

    Enchant(Enchantment bukkit, Material material, String description, int vanillaMaxLvl) {
        this.bukkit = bukkit;
        this.material = material;
        this.description = description;
        this.vanillaMaxLvl = vanillaMaxLvl;
    }

    /**
     * Returns the material of this enchantment.
     * These are <b>my</b> opinion of the material that could be used.
     *
     * @return the material of this enchantment
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Returns the description of this enchantment.
     * These are from wikipedia.
     *
     * @return the description of this enchantment
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the maximum level of this enchantment that is allowed in vanilla.
     *
     * @return the maximum level of this enchantment that is allowed in vanilla
     */
    public int getVanillaMaxLvl() {
        return vanillaMaxLvl;
    }

    /**
     * Enchants the given item stack with this enchantment.
     *
     * @param stack                     - the item stack to enchant.
     * @param lvl                       - the level of the enchantment.
     * @param ignoreVanillaRestrictions - whether to ignore the vanilla restrictions.
     */
    public void enchantItem(ItemStack stack, int lvl, boolean ignoreVanillaRestrictions) {
        Meta.of(stack).enchant(this, ignoreVanillaRestrictions ? lvl : Math.min(lvl, vanillaMaxLvl));
    }

    /**
     * Returns bukkit enchantment.
     *
     * @return bukkit enchantment
     */
    @Nonnull
    public Enchantment getAsBukkit() {
        return bukkit;
    }

    /**
     * Returns Enchant from bukkit enchantment.
     *
     * @param enchantment - bukkit enchantment
     * @return Enchant from bukkit enchantment
     */
    @Nonnull
    public static Enchant fromBukkit(Enchantment enchantment) {
        return byBukkit.getOrDefault(enchantment, UNBREAKING);
    }

}
