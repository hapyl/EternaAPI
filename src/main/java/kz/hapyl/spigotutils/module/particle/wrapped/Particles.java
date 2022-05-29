package kz.hapyl.spigotutils.module.particle.wrapped;

import org.bukkit.Particle;
import org.bukkit.Vibration;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

public final class Particles {

    public static final WrappedParticle EXPLOSION_NORMAL = create(Particle.EXPLOSION_NORMAL);
    public static final WrappedParticle EXPLOSION_LARGE = create(Particle.EXPLOSION_LARGE);
    public static final WrappedParticle EXPLOSION_HUGE = create(Particle.EXPLOSION_HUGE);
    public static final WrappedParticle FIREWORKS_SPARK = create(Particle.FIREWORKS_SPARK);
    public static final WrappedParticle WATER_BUBBLE = create(Particle.WATER_BUBBLE);
    public static final WrappedParticle WATER_SPLASH = create(Particle.WATER_SPLASH);
    public static final WrappedParticle WATER_WAKE = create(Particle.WATER_WAKE);
    public static final WrappedParticle SUSPENDED = create(Particle.SUSPENDED);
    public static final WrappedParticle SUSPENDED_DEPTH = create(Particle.SUSPENDED_DEPTH);
    public static final WrappedParticle CRIT = create(Particle.CRIT);
    public static final WrappedParticle CRIT_MAGIC = create(Particle.CRIT_MAGIC);
    public static final WrappedParticle SMOKE_NORMAL = create(Particle.SMOKE_NORMAL);
    public static final WrappedParticle SMOKE_LARGE = create(Particle.SMOKE_LARGE);
    public static final WrappedParticle SPELL = create(Particle.SPELL);
    public static final WrappedParticle SPELL_INSTANT = create(Particle.SPELL_INSTANT);
    public static final WrappedParticle SPELL_MOB = create(Particle.SPELL_MOB);
    public static final WrappedParticle SPELL_MOB_AMBIENT = create(Particle.SPELL_MOB_AMBIENT);
    public static final WrappedParticle SPELL_WITCH = create(Particle.SPELL_WITCH);
    public static final WrappedParticle DRIP_WATER = create(Particle.DRIP_WATER);
    public static final WrappedParticle DRIP_LAVA = create(Particle.DRIP_LAVA);
    public static final WrappedParticle VILLAGER_ANGRY = create(Particle.VILLAGER_ANGRY);
    public static final WrappedParticle VILLAGER_HAPPY = create(Particle.VILLAGER_HAPPY);
    public static final WrappedParticle TOWN_AURA = create(Particle.TOWN_AURA);
    public static final WrappedParticle NOTE = create(Particle.NOTE);
    public static final WrappedParticle PORTAL = create(Particle.PORTAL);
    public static final WrappedParticle ENCHANTMENT_TABLE = create(Particle.ENCHANTMENT_TABLE);
    public static final WrappedParticle FLAME = create(Particle.FLAME);
    public static final WrappedParticle LAVA = create(Particle.LAVA);
    public static final WrappedParticle CLOUD = create(Particle.CLOUD);
    public static final WrappedParticle SNOWBALL = create(Particle.SNOWBALL);
    public static final WrappedParticle SNOW_SHOVEL = create(Particle.SNOW_SHOVEL);
    public static final WrappedParticle SLIME = create(Particle.SLIME);
    public static final WrappedParticle HEART = create(Particle.HEART);
    public static final WrappedParticle WATER_DROP = create(Particle.WATER_DROP);
    public static final WrappedParticle MOB_APPEARANCE = create(Particle.MOB_APPEARANCE);
    public static final WrappedParticle DRAGON_BREATH = create(Particle.DRAGON_BREATH);
    public static final WrappedParticle END_ROD = create(Particle.END_ROD);
    public static final WrappedParticle DAMAGE_INDICATOR = create(Particle.DAMAGE_INDICATOR);
    public static final WrappedParticle SWEEP_ATTACK = create(Particle.SWEEP_ATTACK);
    public static final WrappedParticle TOTEM = create(Particle.TOTEM);
    public static final WrappedParticle SPIT = create(Particle.SPIT);
    public static final WrappedParticle SQUID_INK = create(Particle.SQUID_INK);
    public static final WrappedParticle BUBBLE_POP = create(Particle.BUBBLE_POP);
    public static final WrappedParticle CURRENT_DOWN = create(Particle.CURRENT_DOWN);
    public static final WrappedParticle BUBBLE_COLUMN_UP = create(Particle.BUBBLE_COLUMN_UP);
    public static final WrappedParticle NAUTILUS = create(Particle.NAUTILUS);
    public static final WrappedParticle DOLPHIN = create(Particle.DOLPHIN);
    public static final WrappedParticle SNEEZE = create(Particle.SNEEZE);
    public static final WrappedParticle CAMPFIRE_COSY_SMOKE = create(Particle.CAMPFIRE_COSY_SMOKE);
    public static final WrappedParticle CAMPFIRE_SIGNAL_SMOKE = create(Particle.CAMPFIRE_SIGNAL_SMOKE);
    public static final WrappedParticle COMPOSTER = create(Particle.COMPOSTER);
    public static final WrappedParticle FLASH = create(Particle.FLASH);
    public static final WrappedParticle FALLING_LAVA = create(Particle.FALLING_LAVA);
    public static final WrappedParticle LANDING_LAVA = create(Particle.LANDING_LAVA);
    public static final WrappedParticle FALLING_WATER = create(Particle.FALLING_WATER);
    public static final WrappedParticle DRIPPING_HONEY = create(Particle.DRIPPING_HONEY);
    public static final WrappedParticle FALLING_HONEY = create(Particle.FALLING_HONEY);
    public static final WrappedParticle LANDING_HONEY = create(Particle.LANDING_HONEY);
    public static final WrappedParticle FALLING_NECTAR = create(Particle.FALLING_NECTAR);
    public static final WrappedParticle SOUL_FIRE_FLAME = create(Particle.SOUL_FIRE_FLAME);
    public static final WrappedParticle ASH = create(Particle.ASH);
    public static final WrappedParticle CRIMSON_SPORE = create(Particle.CRIMSON_SPORE);
    public static final WrappedParticle WARPED_SPORE = create(Particle.WARPED_SPORE);
    public static final WrappedParticle SOUL = create(Particle.SOUL);
    public static final WrappedParticle DRIPPING_OBSIDIAN_TEAR = create(Particle.DRIPPING_OBSIDIAN_TEAR);
    public static final WrappedParticle FALLING_OBSIDIAN_TEAR = create(Particle.FALLING_OBSIDIAN_TEAR);
    public static final WrappedParticle LANDING_OBSIDIAN_TEAR = create(Particle.LANDING_OBSIDIAN_TEAR);
    public static final WrappedParticle REVERSE_PORTAL = create(Particle.REVERSE_PORTAL);
    public static final WrappedParticle WHITE_ASH = create(Particle.WHITE_ASH);
    public static final WrappedParticle FALLING_SPORE_BLOSSOM = create(Particle.FALLING_SPORE_BLOSSOM);
    public static final WrappedParticle SPORE_BLOSSOM_AIR = create(Particle.SPORE_BLOSSOM_AIR);
    public static final WrappedParticle SMALL_FLAME = create(Particle.SMALL_FLAME);
    public static final WrappedParticle SNOWFLAKE = create(Particle.SNOWFLAKE);
    public static final WrappedParticle DRIPPING_DRIPSTONE_LAVA = create(Particle.DRIPPING_DRIPSTONE_LAVA);
    public static final WrappedParticle FALLING_DRIPSTONE_LAVA = create(Particle.FALLING_DRIPSTONE_LAVA);
    public static final WrappedParticle DRIPPING_DRIPSTONE_WATER = create(Particle.DRIPPING_DRIPSTONE_WATER);
    public static final WrappedParticle FALLING_DRIPSTONE_WATER = create(Particle.FALLING_DRIPSTONE_WATER);
    public static final WrappedParticle GLOW_SQUID_INK = create(Particle.GLOW_SQUID_INK);
    public static final WrappedParticle GLOW = create(Particle.GLOW);
    public static final WrappedParticle WAX_ON = create(Particle.WAX_ON);
    public static final WrappedParticle WAX_OFF = create(Particle.WAX_OFF);
    public static final WrappedParticle ELECTRIC_SPARK = create(Particle.ELECTRIC_SPARK);
    public static final WrappedParticle SCRAPE = create(Particle.SCRAPE);

    public static final WrappedDataTypeParticle<Particle.DustOptions> REDSTONE = createWithData(Particle.REDSTONE);
    public static final WrappedDataTypeParticle<ItemStack> ITEM_CRACK = createWithData(Particle.ITEM_CRACK);
    public static final WrappedDataTypeParticle<BlockData> BLOCK_CRACK = createWithData(Particle.BLOCK_CRACK);
    public static final WrappedDataTypeParticle<BlockData> BLOCK_DUST = createWithData(Particle.BLOCK_DUST);
    public static final WrappedDataTypeParticle<BlockData> FALLING_DUST = createWithData(Particle.FALLING_DUST);
    public static final WrappedDataTypeParticle<Particle.DustTransition> DUST_COLOR_TRANSITION = createWithData(Particle.DUST_COLOR_TRANSITION);
    public static final WrappedDataTypeParticle<Vibration> VIBRATION = createWithData(Particle.VIBRATION);

    private Particles() {
    }

    private static WrappedParticle create(Particle particle) {
        return new WrappedParticle(particle);
    }

    private static <T> WrappedDataTypeParticle<T> createWithData(Particle particle) {
        return new WrappedDataTypeParticle<>(particle);
    }

}
