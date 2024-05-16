package me.hapyl.spigotutils.module.entity.packet;

import com.google.common.collect.Maps;
import me.hapyl.spigotutils.module.annotate.TestedOn;
import me.hapyl.spigotutils.module.annotate.Version;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ambient.EntityBat;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.horse.*;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.boss.enderdragon.EntityEnderCrystal;
import net.minecraft.world.entity.boss.enderdragon.EntityEnderDragon;
import net.minecraft.world.entity.boss.wither.EntityWither;
import net.minecraft.world.entity.decoration.*;
import net.minecraft.world.entity.item.EntityFallingBlock;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.entity.item.EntityTNTPrimed;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.entity.monster.hoglin.EntityHoglin;
import net.minecraft.world.entity.monster.piglin.EntityPiglin;
import net.minecraft.world.entity.monster.piglin.EntityPiglinBrute;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.EntityVillager;
import net.minecraft.world.entity.npc.EntityVillagerTrader;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.entity.projectile.windcharge.BreezeWindCharge;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.entity.vehicle.*;
import org.bukkit.entity.EntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * Contains all NMS {@link EntityTypes} mapped with a minecraft key name.
 *
 * @see #fromNms(EntityTypes)
 */
@SuppressWarnings("all")
@TestedOn(version = Version.V1_20_6)
public final class NMSEntityType {

    public static final EntityTypes<Allay> ALLAY;
    public static final EntityTypes<EntityAreaEffectCloud> AREA_EFFECT_CLOUD;
    public static final EntityTypes<Armadillo> ARMADILLO;
    public static final EntityTypes<EntityArmorStand> ARMOR_STAND;
    public static final EntityTypes<EntityTippedArrow> ARROW;
    public static final EntityTypes<Axolotl> AXOLOTL;
    public static final EntityTypes<EntityBat> BAT;
    public static final EntityTypes<EntityBee> BEE;
    public static final EntityTypes<EntityBlaze> BLAZE;
    public static final EntityTypes<Display.BlockDisplay> BLOCK_DISPLAY;
    public static final EntityTypes<EntityBoat> BOAT;
    public static final EntityTypes<Bogged> BOGGED;
    public static final EntityTypes<Breeze> BREEZE;
    public static final EntityTypes<BreezeWindCharge> BREEZE_WIND_CHARGE;
    public static final EntityTypes<Camel> CAMEL;
    public static final EntityTypes<EntityCat> CAT;
    public static final EntityTypes<EntityCaveSpider> CAVE_SPIDER;
    public static final EntityTypes<ChestBoat> CHEST_BOAT;
    public static final EntityTypes<EntityMinecartChest> CHEST_MINECART;
    public static final EntityTypes<EntityChicken> CHICKEN;
    public static final EntityTypes<EntityCod> COD;
    public static final EntityTypes<EntityMinecartCommandBlock> COMMAND_BLOCK_MINECART;
    public static final EntityTypes<EntityCow> COW;
    public static final EntityTypes<EntityCreeper> CREEPER;
    public static final EntityTypes<EntityDolphin> DOLPHIN;
    public static final EntityTypes<EntityHorseDonkey> DONKEY;
    public static final EntityTypes<EntityDragonFireball> DRAGON_FIREBALL;
    public static final EntityTypes<EntityDrowned> DROWNED;
    public static final EntityTypes<EntityEgg> EGG;
    public static final EntityTypes<EntityGuardianElder> ELDER_GUARDIAN;
    public static final EntityTypes<EntityEnderCrystal> END_CRYSTAL;
    public static final EntityTypes<EntityEnderDragon> ENDER_DRAGON;
    public static final EntityTypes<EntityEnderPearl> ENDER_PEARL;
    public static final EntityTypes<EntityEnderman> ENDERMAN;
    public static final EntityTypes<EntityEndermite> ENDERMITE;
    public static final EntityTypes<EntityEvoker> EVOKER;
    public static final EntityTypes<EntityEvokerFangs> EVOKER_FANGS;
    public static final EntityTypes<EntityThrownExpBottle> EXPERIENCE_BOTTLE;
    public static final EntityTypes<EntityExperienceOrb> EXPERIENCE_ORB;
    public static final EntityTypes<EntityEnderSignal> EYE_OF_ENDER;
    public static final EntityTypes<EntityFallingBlock> FALLING_BLOCK;
    public static final EntityTypes<EntityFireworks> FIREWORK_ROCKET;
    public static final EntityTypes<EntityFox> FOX;
    public static final EntityTypes<Frog> FROG;
    public static final EntityTypes<EntityMinecartFurnace> FURNACE_MINECART;
    public static final EntityTypes<EntityGhast> GHAST;
    public static final EntityTypes<EntityGiantZombie> GIANT;
    public static final EntityTypes<GlowItemFrame> GLOW_ITEM_FRAME;
    public static final EntityTypes<GlowSquid> GLOW_SQUID;
    public static final EntityTypes<Goat> GOAT;
    public static final EntityTypes<EntityGuardian> GUARDIAN;
    public static final EntityTypes<EntityHoglin> HOGLIN;
    public static final EntityTypes<EntityMinecartHopper> HOPPER_MINECART;
    public static final EntityTypes<EntityHorse> HORSE;
    public static final EntityTypes<EntityZombieHusk> HUSK;
    public static final EntityTypes<EntityIllagerIllusioner> ILLUSIONER;
    public static final EntityTypes<Interaction> INTERACTION;
    public static final EntityTypes<EntityIronGolem> IRON_GOLEM;
    public static final EntityTypes<EntityItem> ITEM;
    public static final EntityTypes<Display.ItemDisplay> ITEM_DISPLAY;
    public static final EntityTypes<EntityItemFrame> ITEM_FRAME;
    public static final EntityTypes<OminousItemSpawner> OMINOUS_ITEM_SPAWNER;
    public static final EntityTypes<EntityLargeFireball> FIREBALL;
    public static final EntityTypes<EntityLeash> LEASH_KNOT;
    public static final EntityTypes<EntityLightning> LIGHTNING_BOLT;
    public static final EntityTypes<EntityLlama> LLAMA;
    public static final EntityTypes<EntityLlamaSpit> LLAMA_SPIT;
    public static final EntityTypes<EntityMagmaCube> MAGMA_CUBE;
    public static final EntityTypes<Marker> MARKER;
    public static final EntityTypes<EntityMinecartRideable> MINECART;
    public static final EntityTypes<EntityMushroomCow> MOOSHROOM;
    public static final EntityTypes<EntityHorseMule> MULE;
    public static final EntityTypes<EntityOcelot> OCELOT;
    public static final EntityTypes<EntityPainting> PAINTING;
    public static final EntityTypes<EntityPanda> PANDA;
    public static final EntityTypes<EntityParrot> PARROT;
    public static final EntityTypes<EntityPhantom> PHANTOM;
    public static final EntityTypes<EntityPig> PIG;
    public static final EntityTypes<EntityPiglin> PIGLIN;
    public static final EntityTypes<EntityPiglinBrute> PIGLIN_BRUTE;
    public static final EntityTypes<EntityPillager> PILLAGER;
    public static final EntityTypes<EntityPolarBear> POLAR_BEAR;
    public static final EntityTypes<EntityPotion> POTION;
    public static final EntityTypes<EntityPufferFish> PUFFERFISH;
    public static final EntityTypes<EntityRabbit> RABBIT;
    public static final EntityTypes<EntityRavager> RAVAGER;
    public static final EntityTypes<EntitySalmon> SALMON;
    public static final EntityTypes<EntitySheep> SHEEP;
    public static final EntityTypes<EntityShulker> SHULKER;
    public static final EntityTypes<EntityShulkerBullet> SHULKER_BULLET;
    public static final EntityTypes<EntitySilverfish> SILVERFISH;
    public static final EntityTypes<EntitySkeleton> SKELETON;
    public static final EntityTypes<EntityHorseSkeleton> SKELETON_HORSE;
    public static final EntityTypes<EntitySlime> SLIME;
    public static final EntityTypes<EntitySmallFireball> SMALL_FIREBALL;
    public static final EntityTypes<Sniffer> SNIFFER;
    public static final EntityTypes<EntitySnowman> SNOW_GOLEM;
    public static final EntityTypes<EntitySnowball> SNOWBALL;
    public static final EntityTypes<EntityMinecartMobSpawner> SPAWNER_MINECART;
    public static final EntityTypes<EntitySpectralArrow> SPECTRAL_ARROW;
    public static final EntityTypes<EntitySpider> SPIDER;
    public static final EntityTypes<EntitySquid> SQUID;
    public static final EntityTypes<EntitySkeletonStray> STRAY;
    public static final EntityTypes<EntityStrider> STRIDER;
    public static final EntityTypes<Tadpole> TADPOLE;
    public static final EntityTypes<Display.TextDisplay> TEXT_DISPLAY;
    public static final EntityTypes<EntityTNTPrimed> TNT;
    public static final EntityTypes<EntityMinecartTNT> TNT_MINECART;
    public static final EntityTypes<EntityLlamaTrader> TRADER_LLAMA;
    public static final EntityTypes<EntityThrownTrident> TRIDENT;
    public static final EntityTypes<EntityTropicalFish> TROPICAL_FISH;
    public static final EntityTypes<EntityTurtle> TURTLE;
    public static final EntityTypes<EntityVex> VEX;
    public static final EntityTypes<EntityVillager> VILLAGER;
    public static final EntityTypes<EntityVindicator> VINDICATOR;
    public static final EntityTypes<EntityVillagerTrader> WANDERING_TRADER;
    public static final EntityTypes<Warden> WARDEN;
    public static final EntityTypes<WindCharge> WIND_CHARGE;
    public static final EntityTypes<EntityWitch> WITCH;
    public static final EntityTypes<EntityWither> WITHER;
    public static final EntityTypes<EntitySkeletonWither> WITHER_SKELETON;
    public static final EntityTypes<EntityWitherSkull> WITHER_SKULL;
    public static final EntityTypes<EntityWolf> WOLF;
    public static final EntityTypes<EntityZoglin> ZOGLIN;
    public static final EntityTypes<EntityZombie> ZOMBIE;
    public static final EntityTypes<EntityHorseZombie> ZOMBIE_HORSE;
    public static final EntityTypes<EntityZombieVillager> ZOMBIE_VILLAGER;
    public static final EntityTypes<EntityPigZombie> ZOMBIFIED_PIGLIN;
    public static final EntityTypes<EntityHuman> PLAYER;
    public static final EntityTypes<EntityFishingHook> FISHING_BOBBER;

    private static final Map<EntityTypes<?>, EntityType> entityTypeMap;

    static {
        entityTypeMap = Maps.newHashMap();

        ALLAY = of(EntityTypes.a, EntityType.ALLAY);
        AREA_EFFECT_CLOUD = of(EntityTypes.b, EntityType.AREA_EFFECT_CLOUD);
        ARMADILLO = of(EntityTypes.c, EntityType.ARMADILLO);
        ARMOR_STAND = of(EntityTypes.d, EntityType.ARMOR_STAND);
        ARROW = of(EntityTypes.e, EntityType.ARROW);
        AXOLOTL = of(EntityTypes.f, EntityType.AXOLOTL);
        BAT = of(EntityTypes.g, EntityType.BAT);
        BEE = of(EntityTypes.h, EntityType.BEE);
        BLAZE = of(EntityTypes.i, EntityType.BLAZE);
        BLOCK_DISPLAY = of(EntityTypes.j, EntityType.BLOCK_DISPLAY);
        BOAT = of(EntityTypes.k, EntityType.BOAT);
        BOGGED = of(EntityTypes.l, EntityType.BOGGED);
        BREEZE = of(EntityTypes.m, EntityType.BREEZE);
        BREEZE_WIND_CHARGE = of(EntityTypes.n, EntityType.BREEZE_WIND_CHARGE);
        CAMEL = of(EntityTypes.o, EntityType.CAMEL);
        CAT = of(EntityTypes.p, EntityType.CAT);
        CAVE_SPIDER = of(EntityTypes.q, EntityType.CAVE_SPIDER);
        CHEST_BOAT = of(EntityTypes.r, EntityType.CHEST_BOAT);
        CHEST_MINECART = of(EntityTypes.s, EntityType.CHEST_MINECART);
        CHICKEN = of(EntityTypes.t, EntityType.CHICKEN);
        COD = of(EntityTypes.u, EntityType.COD);
        COMMAND_BLOCK_MINECART = of(EntityTypes.v, EntityType.COMMAND_BLOCK_MINECART);
        COW = of(EntityTypes.w, EntityType.COW);
        CREEPER = of(EntityTypes.x, EntityType.CREEPER);
        DOLPHIN = of(EntityTypes.y, EntityType.DOLPHIN);
        DONKEY = of(EntityTypes.z, EntityType.DONKEY);
        DRAGON_FIREBALL = of(EntityTypes.A, EntityType.DRAGON_FIREBALL);
        DROWNED = of(EntityTypes.B, EntityType.DROWNED);
        EGG = of(EntityTypes.C, EntityType.EGG);
        ELDER_GUARDIAN = of(EntityTypes.D, EntityType.ELDER_GUARDIAN);
        END_CRYSTAL = of(EntityTypes.E, EntityType.END_CRYSTAL);
        ENDER_DRAGON = of(EntityTypes.F, EntityType.ENDER_DRAGON);
        ENDER_PEARL = of(EntityTypes.G, EntityType.ENDER_PEARL);
        ENDERMAN = of(EntityTypes.H, EntityType.ENDERMAN);
        ENDERMITE = of(EntityTypes.I, EntityType.ENDERMITE);
        EVOKER = of(EntityTypes.J, EntityType.EVOKER);
        EVOKER_FANGS = of(EntityTypes.K, EntityType.EVOKER_FANGS);
        EXPERIENCE_BOTTLE = of(EntityTypes.L, EntityType.EXPERIENCE_BOTTLE);
        EXPERIENCE_ORB = of(EntityTypes.M, EntityType.EXPERIENCE_ORB);
        EYE_OF_ENDER = of(EntityTypes.N, EntityType.EYE_OF_ENDER);
        FALLING_BLOCK = of(EntityTypes.O, EntityType.FALLING_BLOCK);
        FIREWORK_ROCKET = of(EntityTypes.P, EntityType.FIREWORK_ROCKET);
        FOX = of(EntityTypes.Q, EntityType.FOX);
        FROG = of(EntityTypes.R, EntityType.FROG);
        FURNACE_MINECART = of(EntityTypes.S, EntityType.FURNACE_MINECART);
        GHAST = of(EntityTypes.T, EntityType.GHAST);
        GIANT = of(EntityTypes.U, EntityType.GIANT);
        GLOW_ITEM_FRAME = of(EntityTypes.V, EntityType.GLOW_ITEM_FRAME);
        GLOW_SQUID = of(EntityTypes.W, EntityType.GLOW_SQUID);
        GOAT = of(EntityTypes.X, EntityType.GOAT);
        GUARDIAN = of(EntityTypes.Y, EntityType.GUARDIAN);
        HOGLIN = of(EntityTypes.Z, EntityType.HOGLIN);
        HOPPER_MINECART = of(EntityTypes.aa, EntityType.HOPPER_MINECART);
        HORSE = of(EntityTypes.ab, EntityType.HORSE);
        HUSK = of(EntityTypes.ac, EntityType.HUSK);
        ILLUSIONER = of(EntityTypes.ad, EntityType.ILLUSIONER);
        INTERACTION = of(EntityTypes.ae, EntityType.INTERACTION);
        IRON_GOLEM = of(EntityTypes.af, EntityType.IRON_GOLEM);
        ITEM = of(EntityTypes.ag, EntityType.ITEM);
        ITEM_DISPLAY = of(EntityTypes.ah, EntityType.ITEM_DISPLAY);
        ITEM_FRAME = of(EntityTypes.ai, EntityType.ITEM_FRAME);
        OMINOUS_ITEM_SPAWNER = of(EntityTypes.aj, EntityType.OMINOUS_ITEM_SPAWNER);
        FIREBALL = of(EntityTypes.ak, EntityType.FIREBALL);
        LEASH_KNOT = of(EntityTypes.al, EntityType.LEASH_KNOT);
        LIGHTNING_BOLT = of(EntityTypes.am, EntityType.LIGHTNING_BOLT);
        LLAMA = of(EntityTypes.an, EntityType.LLAMA);
        LLAMA_SPIT = of(EntityTypes.ao, EntityType.LLAMA_SPIT);
        MAGMA_CUBE = of(EntityTypes.ap, EntityType.MAGMA_CUBE);
        MARKER = of(EntityTypes.aq, EntityType.MARKER);
        MINECART = of(EntityTypes.ar, EntityType.MINECART);
        MOOSHROOM = of(EntityTypes.as, EntityType.MOOSHROOM);
        MULE = of(EntityTypes.at, EntityType.MULE);
        OCELOT = of(EntityTypes.au, EntityType.OCELOT);
        PAINTING = of(EntityTypes.av, EntityType.PAINTING);
        PANDA = of(EntityTypes.aw, EntityType.PANDA);
        PARROT = of(EntityTypes.ax, EntityType.PARROT);
        PHANTOM = of(EntityTypes.ay, EntityType.PHANTOM);
        PIG = of(EntityTypes.az, EntityType.PIG);
        PIGLIN = of(EntityTypes.aA, EntityType.PIGLIN);
        PIGLIN_BRUTE = of(EntityTypes.aB, EntityType.PIGLIN_BRUTE);
        PILLAGER = of(EntityTypes.aC, EntityType.PILLAGER);
        POLAR_BEAR = of(EntityTypes.aD, EntityType.POLAR_BEAR);
        POTION = of(EntityTypes.aE, EntityType.POTION);
        PUFFERFISH = of(EntityTypes.aF, EntityType.PUFFERFISH);
        RABBIT = of(EntityTypes.aG, EntityType.RABBIT);
        RAVAGER = of(EntityTypes.aH, EntityType.RAVAGER);
        SALMON = of(EntityTypes.aI, EntityType.SALMON);
        SHEEP = of(EntityTypes.aJ, EntityType.SHEEP);
        SHULKER = of(EntityTypes.aK, EntityType.SHULKER);
        SHULKER_BULLET = of(EntityTypes.aL, EntityType.SHULKER_BULLET);
        SILVERFISH = of(EntityTypes.aM, EntityType.SILVERFISH);
        SKELETON = of(EntityTypes.aN, EntityType.SKELETON);
        SKELETON_HORSE = of(EntityTypes.aO, EntityType.SKELETON_HORSE);
        SLIME = of(EntityTypes.aP, EntityType.SLIME);
        SMALL_FIREBALL = of(EntityTypes.aQ, EntityType.SMALL_FIREBALL);
        SNIFFER = of(EntityTypes.aR, EntityType.SNIFFER);
        SNOW_GOLEM = of(EntityTypes.aS, EntityType.SNOW_GOLEM);
        SNOWBALL = of(EntityTypes.aT, EntityType.SNOWBALL);
        SPAWNER_MINECART = of(EntityTypes.aU, EntityType.SPAWNER_MINECART);
        SPECTRAL_ARROW = of(EntityTypes.aV, EntityType.SPECTRAL_ARROW);
        SPIDER = of(EntityTypes.aW, EntityType.SPIDER);
        SQUID = of(EntityTypes.aX, EntityType.SQUID);
        STRAY = of(EntityTypes.aY, EntityType.STRAY);
        STRIDER = of(EntityTypes.aZ, EntityType.STRIDER);
        TADPOLE = of(EntityTypes.ba, EntityType.TADPOLE);
        TEXT_DISPLAY = of(EntityTypes.bb, EntityType.TEXT_DISPLAY);
        TNT = of(EntityTypes.bc, EntityType.TNT);
        TNT_MINECART = of(EntityTypes.bd, EntityType.TNT_MINECART);
        TRADER_LLAMA = of(EntityTypes.be, EntityType.TRADER_LLAMA);
        TRIDENT = of(EntityTypes.bf, EntityType.TRIDENT);
        TROPICAL_FISH = of(EntityTypes.bg, EntityType.TROPICAL_FISH);
        TURTLE = of(EntityTypes.bh, EntityType.TURTLE);
        VEX = of(EntityTypes.bi, EntityType.VEX);
        VILLAGER = of(EntityTypes.bj, EntityType.VILLAGER);
        VINDICATOR = of(EntityTypes.bk, EntityType.VINDICATOR);
        WANDERING_TRADER = of(EntityTypes.bl, EntityType.WANDERING_TRADER);
        WARDEN = of(EntityTypes.bm, EntityType.WARDEN);
        WIND_CHARGE = of(EntityTypes.bn, EntityType.WIND_CHARGE);
        WITCH = of(EntityTypes.bo, EntityType.WITCH);
        WITHER = of(EntityTypes.bp, EntityType.WITHER);
        WITHER_SKELETON = of(EntityTypes.bq, EntityType.WITHER_SKELETON);
        WITHER_SKULL = of(EntityTypes.br, EntityType.WITHER_SKULL);
        WOLF = of(EntityTypes.bs, EntityType.WOLF);
        ZOGLIN = of(EntityTypes.bt, EntityType.ZOGLIN);
        ZOMBIE = of(EntityTypes.bu, EntityType.ZOMBIE);
        ZOMBIE_HORSE = of(EntityTypes.bv, EntityType.ZOMBIE_HORSE);
        ZOMBIE_VILLAGER = of(EntityTypes.bw, EntityType.ZOMBIE_VILLAGER);
        ZOMBIFIED_PIGLIN = of(EntityTypes.bx, EntityType.ZOMBIFIED_PIGLIN);
        PLAYER = of(EntityTypes.by, EntityType.PLAYER);
        FISHING_BOBBER = of(EntityTypes.bz, EntityType.FISHING_BOBBER);
    }

    /**
     * Gets a {@link EntityType} from a {@link EntityTypes}, or null if not mapped.
     *
     * @param nms - NMS Entity type.
     * @return a entity type or <code>null</code>.
     */
    @Nullable
    public static EntityType fromNms(@Nonnull EntityTypes<?> nms) {
        return entityTypeMap.get(nms);
    }

    /**
     * Gets a {@link EntityType} from a {@link EntityTypes}, or default if not mapped.
     *
     * @param nms - NMS Entity type.
     * @param def - Default value.
     * @return a entity type or default value.
     */
    @Nonnull
    public static EntityType fromNmsOr(@Nonnull EntityTypes<?> nms, @Nonnull EntityType def) {
        return entityTypeMap.getOrDefault(nms, def);
    }

    private static <T extends Entity> EntityTypes<T> of(EntityTypes<T> nms, EntityType bukkit) {
        entityTypeMap.put(nms, bukkit);

        return nms;
    }

}

