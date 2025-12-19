package me.hapyl.eterna.module.entity.packet;

import com.google.common.collect.Maps;
import me.hapyl.eterna.module.annotate.ObsoleteOnceOfficialMojangMappingsAreReleasedIn2026;
import me.hapyl.eterna.module.annotate.TestedOn;
import me.hapyl.eterna.module.annotate.Version;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.cow.MushroomCow;
import net.minecraft.world.entity.animal.dolphin.Dolphin;
import net.minecraft.world.entity.animal.equine.*;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.feline.Ocelot;
import net.minecraft.world.entity.animal.fish.Cod;
import net.minecraft.world.entity.animal.fish.Pufferfish;
import net.minecraft.world.entity.animal.fish.Salmon;
import net.minecraft.world.entity.animal.fish.TropicalFish;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.entity.animal.happyghast.HappyGhast;
import net.minecraft.world.entity.animal.panda.Panda;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.polarbear.PolarBear;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.animal.squid.GlowSquid;
import net.minecraft.world.entity.animal.squid.Squid;
import net.minecraft.world.entity.animal.turtle.Turtle;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.GlowItemFrame;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.decoration.painting.Painting;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.entity.monster.creaking.Creaking;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.illager.Evoker;
import net.minecraft.world.entity.monster.illager.Illusioner;
import net.minecraft.world.entity.monster.illager.Pillager;
import net.minecraft.world.entity.monster.illager.Vindicator;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.skeleton.Bogged;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.monster.skeleton.Stray;
import net.minecraft.world.entity.monster.skeleton.WitherSkeleton;
import net.minecraft.world.entity.monster.spider.CaveSpider;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.monster.zombie.*;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.entity.projectile.arrow.SpectralArrow;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.entity.projectile.hurtingprojectile.DragonFireball;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball;
import net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.BreezeWindCharge;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge;
import net.minecraft.world.entity.projectile.throwableitemprojectile.*;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.entity.vehicle.minecart.*;
import org.bukkit.entity.EntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * Contains all NMS {@link net.minecraft.world.entity.EntityType} mapped with a minecraft key name.
 *
 * @see #fromNms(net.minecraft.world.entity.EntityType)
 */
@SuppressWarnings("all")
@ObsoleteOnceOfficialMojangMappingsAreReleasedIn2026
@TestedOn(version = Version.V1_21_11)
public final class NMSEntityType {

    public static final net.minecraft.world.entity.EntityType<Allay> ALLAY;
    public static final net.minecraft.world.entity.EntityType<AreaEffectCloud> AREA_EFFECT_CLOUD;
    public static final net.minecraft.world.entity.EntityType<Armadillo> ARMADILLO;
    public static final net.minecraft.world.entity.EntityType<ArmorStand> ARMOR_STAND;
    public static final net.minecraft.world.entity.EntityType<Arrow> ARROW;
    public static final net.minecraft.world.entity.EntityType<Axolotl> AXOLOTL;
    public static final net.minecraft.world.entity.EntityType<Bat> BAT;
    public static final net.minecraft.world.entity.EntityType<Bee> BEE;
    public static final net.minecraft.world.entity.EntityType<Blaze> BLAZE;
    public static final net.minecraft.world.entity.EntityType<Display.BlockDisplay> BLOCK_DISPLAY;
    public static final net.minecraft.world.entity.EntityType<Boat> BOAT;
    public static final net.minecraft.world.entity.EntityType<Bogged> BOGGED;
    public static final net.minecraft.world.entity.EntityType<Breeze> BREEZE;
    public static final net.minecraft.world.entity.EntityType<BreezeWindCharge> BREEZE_WIND_CHARGE;
    public static final net.minecraft.world.entity.EntityType<Camel> CAMEL;
    public static final net.minecraft.world.entity.EntityType<Cat> CAT;
    public static final net.minecraft.world.entity.EntityType<CaveSpider> CAVE_SPIDER;
    public static final net.minecraft.world.entity.EntityType<ChestBoat> CHEST_BOAT;
    public static final net.minecraft.world.entity.EntityType<MinecartChest> CHEST_MINECART;
    public static final net.minecraft.world.entity.EntityType<Chicken> CHICKEN;
    public static final net.minecraft.world.entity.EntityType<Cod> COD;
    public static final net.minecraft.world.entity.EntityType<MinecartCommandBlock> COMMAND_BLOCK_MINECART;
    public static final net.minecraft.world.entity.EntityType<Cow> COW;
    public static final net.minecraft.world.entity.EntityType<Creeper> CREEPER;
    public static final net.minecraft.world.entity.EntityType<Dolphin> DOLPHIN;
    public static final net.minecraft.world.entity.EntityType<Donkey> DONKEY;
    public static final net.minecraft.world.entity.EntityType<DragonFireball> DRAGON_FIREBALL;
    public static final net.minecraft.world.entity.EntityType<Drowned> DROWNED;
    public static final net.minecraft.world.entity.EntityType<ThrownEgg> EGG;
    public static final net.minecraft.world.entity.EntityType<ElderGuardian> ELDER_GUARDIAN;
    public static final net.minecraft.world.entity.EntityType<EndCrystal> END_CRYSTAL;
    public static final net.minecraft.world.entity.EntityType<EnderDragon> ENDER_DRAGON;
    public static final net.minecraft.world.entity.EntityType<ThrownEnderpearl> ENDER_PEARL;
    public static final net.minecraft.world.entity.EntityType<EnderMan> ENDERMAN;
    public static final net.minecraft.world.entity.EntityType<Endermite> ENDERMITE;
    public static final net.minecraft.world.entity.EntityType<Evoker> EVOKER;
    public static final net.minecraft.world.entity.EntityType<EvokerFangs> EVOKER_FANGS;
    public static final net.minecraft.world.entity.EntityType<ThrownExperienceBottle> EXPERIENCE_BOTTLE;
    public static final net.minecraft.world.entity.EntityType<ExperienceOrb> EXPERIENCE_ORB;
    public static final net.minecraft.world.entity.EntityType<EyeOfEnder> EYE_OF_ENDER;
    public static final net.minecraft.world.entity.EntityType<FallingBlockEntity> FALLING_BLOCK;
    public static final net.minecraft.world.entity.EntityType<FireworkRocketEntity> FIREWORK_ROCKET;
    public static final net.minecraft.world.entity.EntityType<Fox> FOX;
    public static final net.minecraft.world.entity.EntityType<Frog> FROG;
    public static final net.minecraft.world.entity.EntityType<MinecartFurnace> FURNACE_MINECART;
    public static final net.minecraft.world.entity.EntityType<Ghast> GHAST;
    public static final net.minecraft.world.entity.EntityType<Giant> GIANT;
    public static final net.minecraft.world.entity.EntityType<GlowItemFrame> GLOW_ITEM_FRAME;
    public static final net.minecraft.world.entity.EntityType<GlowSquid> GLOW_SQUID;
    public static final net.minecraft.world.entity.EntityType<Goat> GOAT;
    public static final net.minecraft.world.entity.EntityType<Guardian> GUARDIAN;
    public static final net.minecraft.world.entity.EntityType<Hoglin> HOGLIN;
    public static final net.minecraft.world.entity.EntityType<MinecartHopper> HOPPER_MINECART;
    public static final net.minecraft.world.entity.EntityType<Horse> HORSE;
    public static final net.minecraft.world.entity.EntityType<Husk> HUSK;
    public static final net.minecraft.world.entity.EntityType<Illusioner> ILLUSIONER;
    public static final net.minecraft.world.entity.EntityType<Interaction> INTERACTION;
    public static final net.minecraft.world.entity.EntityType<IronGolem> IRON_GOLEM;
    public static final net.minecraft.world.entity.EntityType<ItemEntity> ITEM;
    public static final net.minecraft.world.entity.EntityType<Display.ItemDisplay> ITEM_DISPLAY;
    public static final net.minecraft.world.entity.EntityType<ItemFrame> ITEM_FRAME;
    public static final net.minecraft.world.entity.EntityType<OminousItemSpawner> OMINOUS_ITEM_SPAWNER;
    public static final net.minecraft.world.entity.EntityType<LargeFireball> FIREBALL;
    public static final net.minecraft.world.entity.EntityType<LeashFenceKnotEntity> LEASH_KNOT;
    public static final net.minecraft.world.entity.EntityType<LightningBolt> LIGHTNING_BOLT;
    public static final net.minecraft.world.entity.EntityType<Llama> LLAMA;
    public static final net.minecraft.world.entity.EntityType<LlamaSpit> LLAMA_SPIT;
    public static final net.minecraft.world.entity.EntityType<MagmaCube> MAGMA_CUBE;
    public static final net.minecraft.world.entity.EntityType<Marker> MARKER;
    public static final net.minecraft.world.entity.EntityType<Minecart> MINECART;
    public static final net.minecraft.world.entity.EntityType<MushroomCow> MOOSHROOM;
    public static final net.minecraft.world.entity.EntityType<Mule> MULE;
    public static final net.minecraft.world.entity.EntityType<Ocelot> OCELOT;
    public static final net.minecraft.world.entity.EntityType<Painting> PAINTING;
    public static final net.minecraft.world.entity.EntityType<Panda> PANDA;
    public static final net.minecraft.world.entity.EntityType<Parrot> PARROT;
    public static final net.minecraft.world.entity.EntityType<Phantom> PHANTOM;
    public static final net.minecraft.world.entity.EntityType<Pig> PIG;
    public static final net.minecraft.world.entity.EntityType<Piglin> PIGLIN;
    public static final net.minecraft.world.entity.EntityType<PiglinBrute> PIGLIN_BRUTE;
    public static final net.minecraft.world.entity.EntityType<Pillager> PILLAGER;
    public static final net.minecraft.world.entity.EntityType<PolarBear> POLAR_BEAR;
    public static final net.minecraft.world.entity.EntityType<ThrownSplashPotion> SPLASH_POTION;
    public static final net.minecraft.world.entity.EntityType<ThrownLingeringPotion> LINGERING_POTION;
    public static final net.minecraft.world.entity.EntityType<Pufferfish> PUFFERFISH;
    public static final net.minecraft.world.entity.EntityType<Rabbit> RABBIT;
    public static final net.minecraft.world.entity.EntityType<Ravager> RAVAGER;
    public static final net.minecraft.world.entity.EntityType<Salmon> SALMON;
    public static final net.minecraft.world.entity.EntityType<Sheep> SHEEP;
    public static final net.minecraft.world.entity.EntityType<Shulker> SHULKER;
    public static final net.minecraft.world.entity.EntityType<ShulkerBullet> SHULKER_BULLET;
    public static final net.minecraft.world.entity.EntityType<Silverfish> SILVERFISH;
    public static final net.minecraft.world.entity.EntityType<Skeleton> SKELETON;
    public static final net.minecraft.world.entity.EntityType<SkeletonHorse> SKELETON_HORSE;
    public static final net.minecraft.world.entity.EntityType<Slime> SLIME;
    public static final net.minecraft.world.entity.EntityType<SmallFireball> SMALL_FIREBALL;
    public static final net.minecraft.world.entity.EntityType<Sniffer> SNIFFER;
    public static final net.minecraft.world.entity.EntityType<SnowGolem> SNOW_GOLEM;
    public static final net.minecraft.world.entity.EntityType<Snowball> SNOWBALL;
    public static final net.minecraft.world.entity.EntityType<MinecartSpawner> SPAWNER_MINECART;
    public static final net.minecraft.world.entity.EntityType<SpectralArrow> SPECTRAL_ARROW;
    public static final net.minecraft.world.entity.EntityType<Spider> SPIDER;
    public static final net.minecraft.world.entity.EntityType<Squid> SQUID;
    public static final net.minecraft.world.entity.EntityType<Stray> STRAY;
    public static final net.minecraft.world.entity.EntityType<Strider> STRIDER;
    public static final net.minecraft.world.entity.EntityType<Tadpole> TADPOLE;
    public static final net.minecraft.world.entity.EntityType<Display.TextDisplay> TEXT_DISPLAY;
    public static final net.minecraft.world.entity.EntityType<PrimedTnt> TNT;
    public static final net.minecraft.world.entity.EntityType<MinecartTNT> TNT_MINECART;
    public static final net.minecraft.world.entity.EntityType<TraderLlama> TRADER_LLAMA;
    public static final net.minecraft.world.entity.EntityType<ThrownTrident> TRIDENT;
    public static final net.minecraft.world.entity.EntityType<TropicalFish> TROPICAL_FISH;
    public static final net.minecraft.world.entity.EntityType<Turtle> TURTLE;
    public static final net.minecraft.world.entity.EntityType<Vex> VEX;
    public static final net.minecraft.world.entity.EntityType<Villager> VILLAGER;
    public static final net.minecraft.world.entity.EntityType<Vindicator> VINDICATOR;
    public static final net.minecraft.world.entity.EntityType<WanderingTrader> WANDERING_TRADER;
    public static final net.minecraft.world.entity.EntityType<Warden> WARDEN;
    public static final net.minecraft.world.entity.EntityType<WindCharge> WIND_CHARGE;
    public static final net.minecraft.world.entity.EntityType<Witch> WITCH;
    public static final net.minecraft.world.entity.EntityType<WitherBoss> WITHER;
    public static final net.minecraft.world.entity.EntityType<WitherSkeleton> WITHER_SKELETON;
    public static final net.minecraft.world.entity.EntityType<WitherSkull> WITHER_SKULL;
    public static final net.minecraft.world.entity.EntityType<Wolf> WOLF;
    public static final net.minecraft.world.entity.EntityType<Zoglin> ZOGLIN;
    public static final net.minecraft.world.entity.EntityType<Zombie> ZOMBIE;
    public static final net.minecraft.world.entity.EntityType<ZombieHorse> ZOMBIE_HORSE;
    public static final net.minecraft.world.entity.EntityType<ZombieVillager> ZOMBIE_VILLAGER;
    public static final net.minecraft.world.entity.EntityType<ZombifiedPiglin> ZOMBIFIED_PIGLIN;
    public static final net.minecraft.world.entity.EntityType<Player> PLAYER;
    public static final net.minecraft.world.entity.EntityType<FishingHook> FISHING_BOBBER;
    public static final net.minecraft.world.entity.EntityType<Creaking> CREAKING;
    public static final net.minecraft.world.entity.EntityType<HappyGhast> HAPPY_GHAST;

    private static final Map<net.minecraft.world.entity.EntityType<?>, EntityType> entityTypeMap;

    static {
        entityTypeMap = Maps.newHashMap();

        ALLAY = of(net.minecraft.world.entity.EntityType.ALLAY, EntityType.ALLAY);
        AREA_EFFECT_CLOUD = of(net.minecraft.world.entity.EntityType.AREA_EFFECT_CLOUD, EntityType.AREA_EFFECT_CLOUD);
        ARMADILLO = of(net.minecraft.world.entity.EntityType.ARMADILLO, EntityType.ARMADILLO);
        ARMOR_STAND = of(net.minecraft.world.entity.EntityType.ARMOR_STAND, EntityType.ARMOR_STAND);
        ARROW = of(net.minecraft.world.entity.EntityType.ARROW, EntityType.ARROW);
        AXOLOTL = of(net.minecraft.world.entity.EntityType.AXOLOTL, EntityType.AXOLOTL);
        BAT = of(net.minecraft.world.entity.EntityType.BAT, EntityType.BAT);
        BEE = of(net.minecraft.world.entity.EntityType.BEE, EntityType.BEE);
        BLAZE = of(net.minecraft.world.entity.EntityType.BLAZE, EntityType.BLAZE);
        BLOCK_DISPLAY = of(net.minecraft.world.entity.EntityType.BLOCK_DISPLAY, EntityType.BLOCK_DISPLAY);
        BOAT = of(net.minecraft.world.entity.EntityType.OAK_BOAT, EntityType.OAK_BOAT);
        BOGGED = of(net.minecraft.world.entity.EntityType.BOGGED, EntityType.BOGGED);
        BREEZE = of(net.minecraft.world.entity.EntityType.BREEZE, EntityType.BREEZE);
        BREEZE_WIND_CHARGE = of(net.minecraft.world.entity.EntityType.BREEZE_WIND_CHARGE, EntityType.BREEZE_WIND_CHARGE);
        CAMEL = of(net.minecraft.world.entity.EntityType.CAMEL, EntityType.CAMEL);
        CAT = of(net.minecraft.world.entity.EntityType.CAT, EntityType.CAT);
        CAVE_SPIDER = of(net.minecraft.world.entity.EntityType.CAVE_SPIDER, EntityType.CAVE_SPIDER);
        CHEST_BOAT = of(net.minecraft.world.entity.EntityType.OAK_CHEST_BOAT, EntityType.OAK_CHEST_BOAT);
        CHEST_MINECART = of(net.minecraft.world.entity.EntityType.CHEST_MINECART, EntityType.CHEST_MINECART);
        CHICKEN = of(net.minecraft.world.entity.EntityType.CHICKEN, EntityType.CHICKEN);
        COD = of(net.minecraft.world.entity.EntityType.COD, EntityType.COD);
        COMMAND_BLOCK_MINECART = of(net.minecraft.world.entity.EntityType.COMMAND_BLOCK_MINECART, EntityType.COMMAND_BLOCK_MINECART);
        COW = of(net.minecraft.world.entity.EntityType.COW, EntityType.COW);
        CREEPER = of(net.minecraft.world.entity.EntityType.CREEPER, EntityType.CREEPER);
        DOLPHIN = of(net.minecraft.world.entity.EntityType.DOLPHIN, EntityType.DOLPHIN);
        DONKEY = of(net.minecraft.world.entity.EntityType.DONKEY, EntityType.DONKEY);
        DRAGON_FIREBALL = of(net.minecraft.world.entity.EntityType.DRAGON_FIREBALL, EntityType.DRAGON_FIREBALL);
        DROWNED = of(net.minecraft.world.entity.EntityType.DROWNED, EntityType.DROWNED);
        EGG = of(net.minecraft.world.entity.EntityType.EGG, EntityType.EGG);
        ELDER_GUARDIAN = of(net.minecraft.world.entity.EntityType.ELDER_GUARDIAN, EntityType.ELDER_GUARDIAN);
        END_CRYSTAL = of(net.minecraft.world.entity.EntityType.END_CRYSTAL, EntityType.END_CRYSTAL);
        ENDER_DRAGON = of(net.minecraft.world.entity.EntityType.ENDER_DRAGON, EntityType.ENDER_DRAGON);
        ENDER_PEARL = of(net.minecraft.world.entity.EntityType.ENDER_PEARL, EntityType.ENDER_PEARL);
        ENDERMAN = of(net.minecraft.world.entity.EntityType.ENDERMAN, EntityType.ENDERMAN);
        ENDERMITE = of(net.minecraft.world.entity.EntityType.ENDERMITE, EntityType.ENDERMITE);
        EVOKER = of(net.minecraft.world.entity.EntityType.EVOKER, EntityType.EVOKER);
        EVOKER_FANGS = of(net.minecraft.world.entity.EntityType.EVOKER_FANGS, EntityType.EVOKER_FANGS);
        EXPERIENCE_BOTTLE = of(net.minecraft.world.entity.EntityType.EXPERIENCE_BOTTLE, EntityType.EXPERIENCE_BOTTLE);
        EXPERIENCE_ORB = of(net.minecraft.world.entity.EntityType.EXPERIENCE_ORB, EntityType.EXPERIENCE_ORB);
        EYE_OF_ENDER = of(net.minecraft.world.entity.EntityType.EYE_OF_ENDER, EntityType.EYE_OF_ENDER);
        FALLING_BLOCK = of(net.minecraft.world.entity.EntityType.FALLING_BLOCK, EntityType.FALLING_BLOCK);
        FIREWORK_ROCKET = of(net.minecraft.world.entity.EntityType.FIREWORK_ROCKET, EntityType.FIREWORK_ROCKET);
        FOX = of(net.minecraft.world.entity.EntityType.FOX, EntityType.FOX);
        FROG = of(net.minecraft.world.entity.EntityType.FROG, EntityType.FROG);
        FURNACE_MINECART = of(net.minecraft.world.entity.EntityType.FURNACE_MINECART, EntityType.FURNACE_MINECART);
        GHAST = of(net.minecraft.world.entity.EntityType.GHAST, EntityType.GHAST);
        GIANT = of(net.minecraft.world.entity.EntityType.GIANT, EntityType.GIANT);
        GLOW_ITEM_FRAME = of(net.minecraft.world.entity.EntityType.GLOW_ITEM_FRAME, EntityType.GLOW_ITEM_FRAME);
        GLOW_SQUID = of(net.minecraft.world.entity.EntityType.GLOW_SQUID, EntityType.GLOW_SQUID);
        GOAT = of(net.minecraft.world.entity.EntityType.GOAT, EntityType.GOAT);
        GUARDIAN = of(net.minecraft.world.entity.EntityType.GUARDIAN, EntityType.GUARDIAN);
        HOGLIN = of(net.minecraft.world.entity.EntityType.HOGLIN, EntityType.HOGLIN);
        HOPPER_MINECART = of(net.minecraft.world.entity.EntityType.HOPPER_MINECART, EntityType.HOPPER_MINECART);
        HORSE = of(net.minecraft.world.entity.EntityType.HORSE, EntityType.HORSE);
        HUSK = of(net.minecraft.world.entity.EntityType.HUSK, EntityType.HUSK);
        ILLUSIONER = of(net.minecraft.world.entity.EntityType.ILLUSIONER, EntityType.ILLUSIONER);
        INTERACTION = of(net.minecraft.world.entity.EntityType.INTERACTION, EntityType.INTERACTION);
        IRON_GOLEM = of(net.minecraft.world.entity.EntityType.IRON_GOLEM, EntityType.IRON_GOLEM);
        ITEM = of(net.minecraft.world.entity.EntityType.ITEM, EntityType.ITEM);
        ITEM_DISPLAY = of(net.minecraft.world.entity.EntityType.ITEM_DISPLAY, EntityType.ITEM_DISPLAY);
        ITEM_FRAME = of(net.minecraft.world.entity.EntityType.ITEM_FRAME, EntityType.ITEM_FRAME);
        OMINOUS_ITEM_SPAWNER = of(net.minecraft.world.entity.EntityType.OMINOUS_ITEM_SPAWNER, EntityType.OMINOUS_ITEM_SPAWNER);
        FIREBALL = of(net.minecraft.world.entity.EntityType.FIREBALL, EntityType.FIREBALL);
        LEASH_KNOT = of(net.minecraft.world.entity.EntityType.LEASH_KNOT, EntityType.LEASH_KNOT);
        LIGHTNING_BOLT = of(net.minecraft.world.entity.EntityType.LIGHTNING_BOLT, EntityType.LIGHTNING_BOLT);
        LLAMA = of(net.minecraft.world.entity.EntityType.LLAMA, EntityType.LLAMA);
        LLAMA_SPIT = of(net.minecraft.world.entity.EntityType.LLAMA_SPIT, EntityType.LLAMA_SPIT);
        MAGMA_CUBE = of(net.minecraft.world.entity.EntityType.MAGMA_CUBE, EntityType.MAGMA_CUBE);
        MARKER = of(net.minecraft.world.entity.EntityType.MARKER, EntityType.MARKER);
        MINECART = of(net.minecraft.world.entity.EntityType.MINECART, EntityType.MINECART);
        MOOSHROOM = of(net.minecraft.world.entity.EntityType.MOOSHROOM, EntityType.MOOSHROOM);
        MULE = of(net.minecraft.world.entity.EntityType.MULE, EntityType.MULE);
        OCELOT = of(net.minecraft.world.entity.EntityType.OCELOT, EntityType.OCELOT);
        PAINTING = of(net.minecraft.world.entity.EntityType.PAINTING, EntityType.PAINTING);
        PANDA = of(net.minecraft.world.entity.EntityType.PANDA, EntityType.PANDA);
        PARROT = of(net.minecraft.world.entity.EntityType.PARROT, EntityType.PARROT);
        PHANTOM = of(net.minecraft.world.entity.EntityType.PHANTOM, EntityType.PHANTOM);
        PIG = of(net.minecraft.world.entity.EntityType.PIG, EntityType.PIG);
        PIGLIN = of(net.minecraft.world.entity.EntityType.PIGLIN, EntityType.PIGLIN);
        PIGLIN_BRUTE = of(net.minecraft.world.entity.EntityType.PIGLIN_BRUTE, EntityType.PIGLIN_BRUTE);
        PILLAGER = of(net.minecraft.world.entity.EntityType.PILLAGER, EntityType.PILLAGER);
        POLAR_BEAR = of(net.minecraft.world.entity.EntityType.POLAR_BEAR, EntityType.POLAR_BEAR);
        SPLASH_POTION = of(net.minecraft.world.entity.EntityType.SPLASH_POTION, EntityType.SPLASH_POTION);
        LINGERING_POTION = of(net.minecraft.world.entity.EntityType.LINGERING_POTION, EntityType.LINGERING_POTION);
        PUFFERFISH = of(net.minecraft.world.entity.EntityType.PUFFERFISH, EntityType.PUFFERFISH);
        RABBIT = of(net.minecraft.world.entity.EntityType.RABBIT, EntityType.RABBIT);
        RAVAGER = of(net.minecraft.world.entity.EntityType.RAVAGER, EntityType.RAVAGER);
        SALMON = of(net.minecraft.world.entity.EntityType.SALMON, EntityType.SALMON);
        SHEEP = of(net.minecraft.world.entity.EntityType.SHEEP, EntityType.SHEEP);
        SHULKER = of(net.minecraft.world.entity.EntityType.SHULKER, EntityType.SHULKER);
        SHULKER_BULLET = of(net.minecraft.world.entity.EntityType.SHULKER_BULLET, EntityType.SHULKER_BULLET);
        SILVERFISH = of(net.minecraft.world.entity.EntityType.SILVERFISH, EntityType.SILVERFISH);
        SKELETON = of(net.minecraft.world.entity.EntityType.SKELETON, EntityType.SKELETON);
        SKELETON_HORSE = of(net.minecraft.world.entity.EntityType.SKELETON_HORSE, EntityType.SKELETON_HORSE);
        SLIME = of(net.minecraft.world.entity.EntityType.SLIME, EntityType.SLIME);
        SMALL_FIREBALL = of(net.minecraft.world.entity.EntityType.SMALL_FIREBALL, EntityType.SMALL_FIREBALL);
        SNIFFER = of(net.minecraft.world.entity.EntityType.SNIFFER, EntityType.SNIFFER);
        SNOW_GOLEM = of(net.minecraft.world.entity.EntityType.SNOW_GOLEM, EntityType.SNOW_GOLEM);
        SNOWBALL = of(net.minecraft.world.entity.EntityType.SNOWBALL, EntityType.SNOWBALL);
        SPAWNER_MINECART = of(net.minecraft.world.entity.EntityType.SPAWNER_MINECART, EntityType.SPAWNER_MINECART);
        SPECTRAL_ARROW = of(net.minecraft.world.entity.EntityType.SPECTRAL_ARROW, EntityType.SPECTRAL_ARROW);
        SPIDER = of(net.minecraft.world.entity.EntityType.SPIDER, EntityType.SPIDER);
        SQUID = of(net.minecraft.world.entity.EntityType.SQUID, EntityType.SQUID);
        STRAY = of(net.minecraft.world.entity.EntityType.STRAY, EntityType.STRAY);
        STRIDER = of(net.minecraft.world.entity.EntityType.STRIDER, EntityType.STRIDER);
        TADPOLE = of(net.minecraft.world.entity.EntityType.TADPOLE, EntityType.TADPOLE);
        TEXT_DISPLAY = of(net.minecraft.world.entity.EntityType.TEXT_DISPLAY, EntityType.TEXT_DISPLAY);
        TNT = of(net.minecraft.world.entity.EntityType.TNT, EntityType.TNT);
        TNT_MINECART = of(net.minecraft.world.entity.EntityType.TNT_MINECART, EntityType.TNT_MINECART);
        TRADER_LLAMA = of(net.minecraft.world.entity.EntityType.TRADER_LLAMA, EntityType.TRADER_LLAMA);
        TRIDENT = of(net.minecraft.world.entity.EntityType.TRIDENT, EntityType.TRIDENT);
        TROPICAL_FISH = of(net.minecraft.world.entity.EntityType.TROPICAL_FISH, EntityType.TROPICAL_FISH);
        TURTLE = of(net.minecraft.world.entity.EntityType.TURTLE, EntityType.TURTLE);
        VEX = of(net.minecraft.world.entity.EntityType.VEX, EntityType.VEX);
        VILLAGER = of(net.minecraft.world.entity.EntityType.VILLAGER, EntityType.VILLAGER);
        VINDICATOR = of(net.minecraft.world.entity.EntityType.VINDICATOR, EntityType.VINDICATOR);
        WANDERING_TRADER = of(net.minecraft.world.entity.EntityType.WANDERING_TRADER, EntityType.WANDERING_TRADER);
        WARDEN = of(net.minecraft.world.entity.EntityType.WARDEN, EntityType.WARDEN);
        WIND_CHARGE = of(net.minecraft.world.entity.EntityType.WIND_CHARGE, EntityType.WIND_CHARGE);
        WITCH = of(net.minecraft.world.entity.EntityType.WITCH, EntityType.WITCH);
        WITHER = of(net.minecraft.world.entity.EntityType.WITHER, EntityType.WITHER);
        WITHER_SKELETON = of(net.minecraft.world.entity.EntityType.WITHER_SKELETON, EntityType.WITHER_SKELETON);
        WITHER_SKULL = of(net.minecraft.world.entity.EntityType.WITHER_SKULL, EntityType.WITHER_SKULL);
        WOLF = of(net.minecraft.world.entity.EntityType.WOLF, EntityType.WOLF);
        ZOGLIN = of(net.minecraft.world.entity.EntityType.ZOGLIN, EntityType.ZOGLIN);
        ZOMBIE = of(net.minecraft.world.entity.EntityType.ZOMBIE, EntityType.ZOMBIE);
        ZOMBIE_HORSE = of(net.minecraft.world.entity.EntityType.ZOMBIE_HORSE, EntityType.ZOMBIE_HORSE);
        ZOMBIE_VILLAGER = of(net.minecraft.world.entity.EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIE_VILLAGER);
        ZOMBIFIED_PIGLIN = of(net.minecraft.world.entity.EntityType.ZOMBIFIED_PIGLIN, EntityType.ZOMBIFIED_PIGLIN);
        PLAYER = of(net.minecraft.world.entity.EntityType.PLAYER, EntityType.PLAYER);
        FISHING_BOBBER = of(net.minecraft.world.entity.EntityType.FISHING_BOBBER, EntityType.FISHING_BOBBER);
        CREAKING = of(net.minecraft.world.entity.EntityType.CREAKING, EntityType.CREAKING);
        HAPPY_GHAST = of(net.minecraft.world.entity.EntityType.HAPPY_GHAST, EntityType.HAPPY_GHAST);
    }

    /**
     * Gets a {@link EntityType} from a {@link net.minecraft.world.entity.EntityType}, or null if not mapped.
     *
     * @param nms - NMS Entity type.
     * @return a entity type or <code>null</code>.
     */
    @Nullable
    public static EntityType fromNms(@Nonnull net.minecraft.world.entity.EntityType<?> nms) {
        return entityTypeMap.get(nms);
    }

    /**
     * Gets a {@link EntityType} from a {@link net.minecraft.world.entity.EntityType}, or default if not mapped.
     *
     * @param nms - NMS Entity type.
     * @param def - Default value.
     * @return a entity type or default value.
     */
    @Nonnull
    public static EntityType fromNmsOr(@Nonnull net.minecraft.world.entity.EntityType<?> nms, @Nonnull EntityType def) {
        return entityTypeMap.getOrDefault(nms, def);
    }

    private static <T extends Entity> net.minecraft.world.entity.EntityType<T> of(net.minecraft.world.entity.EntityType<T> nms, EntityType bukkit) {
        entityTypeMap.put(nms, bukkit);

        return nms;
    }

}

