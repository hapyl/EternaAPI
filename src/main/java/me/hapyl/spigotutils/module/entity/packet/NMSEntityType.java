package me.hapyl.spigotutils.module.entity.packet;

import me.hapyl.spigotutils.module.annotate.TestedOn;
import me.hapyl.spigotutils.module.annotate.Version;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ambient.EntityBat;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.allay.Allay;
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
import net.minecraft.world.entity.vehicle.*;

import java.util.Map;

/**
 * Contains all NMS entity types, mapped with a readable name.
 */
@SuppressWarnings("unused")
@TestedOn(version = Version.V1_20_4)
public final class NMSEntityType {

    public static final EntityTypes<Allay> ALLAY = EntityTypes.b;
    public static final EntityTypes<EntityAreaEffectCloud> AREA_EFFECT_CLOUD = EntityTypes.c;
    public static final EntityTypes<EntityArmorStand> ARMOR_STAND = EntityTypes.d;
    public static final EntityTypes<EntityTippedArrow> TIPPED_ARROW = EntityTypes.e;
    public static final EntityTypes<Axolotl> AXOLOTL = EntityTypes.f;
    public static final EntityTypes<EntityBat> BAT = EntityTypes.g;
    public static final EntityTypes<EntityBee> BEE = EntityTypes.h;
    public static final EntityTypes<EntityBlaze> BLAZE = EntityTypes.i;
    public static final EntityTypes<Display.BlockDisplay> BLOCK_DISPLAY = EntityTypes.j;
    public static final EntityTypes<EntityBoat> BOAT = EntityTypes.k;
    public static final EntityTypes<Breeze> BREEZE = EntityTypes.l;
    public static final EntityTypes<Camel> CAMEL = EntityTypes.m;
    public static final EntityTypes<EntityCat> CAT = EntityTypes.n;
    public static final EntityTypes<EntityCaveSpider> CAVE_SPIDER = EntityTypes.o;
    public static final EntityTypes<ChestBoat> CHEST_BOAT = EntityTypes.p;
    public static final EntityTypes<EntityMinecartChest> MINECART_CHEST = EntityTypes.q;
    public static final EntityTypes<EntityChicken> CHICKEN = EntityTypes.r;
    public static final EntityTypes<EntityCod> COD = EntityTypes.s;
    public static final EntityTypes<EntityMinecartCommandBlock> MINECART_COMMAND_BLOCK = EntityTypes.t;
    public static final EntityTypes<EntityCow> COW = EntityTypes.u;
    public static final EntityTypes<EntityCreeper> CREEPER = EntityTypes.v;
    public static final EntityTypes<EntityDolphin> DOLPHIN = EntityTypes.w;
    public static final EntityTypes<EntityHorseDonkey> HORSE_DONKEY = EntityTypes.x;
    public static final EntityTypes<EntityDragonFireball> DRAGON_FIREBALL = EntityTypes.y;
    public static final EntityTypes<EntityDrowned> DROWNED = EntityTypes.z;
    public static final EntityTypes<EntityEgg> EGG = EntityTypes.A;
    public static final EntityTypes<EntityGuardianElder> GUARDIAN_ELDER = EntityTypes.B;
    public static final EntityTypes<EntityEnderCrystal> ENDER_CRYSTAL = EntityTypes.C;
    public static final EntityTypes<EntityEnderDragon> ENDER_DRAGON = EntityTypes.D;
    public static final EntityTypes<EntityEnderPearl> ENDER_PEARL = EntityTypes.E;
    public static final EntityTypes<EntityEnderman> ENDERMAN = EntityTypes.F;
    public static final EntityTypes<EntityEndermite> ENDERMITE = EntityTypes.G;
    public static final EntityTypes<EntityEvoker> EVOKER = EntityTypes.H;
    public static final EntityTypes<EntityEvokerFangs> EVOKER_FANGS = EntityTypes.I;
    public static final EntityTypes<EntityThrownExpBottle> THROWN_EXP_BOTTLE = EntityTypes.J;
    public static final EntityTypes<EntityExperienceOrb> EXPERIENCE_ORB = EntityTypes.K;
    public static final EntityTypes<EntityEnderSignal> ENDER_SIGNAL = EntityTypes.L;
    public static final EntityTypes<EntityFallingBlock> FALLING_BLOCK = EntityTypes.M;
    public static final EntityTypes<EntityFireworks> FIREWORKS = EntityTypes.N;
    public static final EntityTypes<EntityFox> FOX = EntityTypes.O;
    public static final EntityTypes<Frog> FROG = EntityTypes.P;
    public static final EntityTypes<EntityMinecartFurnace> MINECART_FURNACE = EntityTypes.Q;
    public static final EntityTypes<EntityGhast> GHAST = EntityTypes.R;
    public static final EntityTypes<EntityGiantZombie> GIANT_ZOMBIE = EntityTypes.S;
    public static final EntityTypes<GlowItemFrame> GLOW_ITEM_FRAME = EntityTypes.T;
    public static final EntityTypes<GlowSquid> GLOW_SQUID = EntityTypes.U;
    public static final EntityTypes<Goat> GOAT = EntityTypes.V;
    public static final EntityTypes<EntityGuardian> GUARDIAN = EntityTypes.W;
    public static final EntityTypes<EntityHoglin> HOGLIN = EntityTypes.X;
    public static final EntityTypes<EntityMinecartHopper> MINECART_HOPPER = EntityTypes.Y;
    public static final EntityTypes<EntityHorse> HORSE = EntityTypes.Z;
    public static final EntityTypes<EntityZombieHusk> ZOMBIE_HUSK = EntityTypes.aa;
    public static final EntityTypes<EntityIllagerIllusioner> ILLAGER_ILLUSIONER = EntityTypes.ab;
    public static final EntityTypes<Interaction> INTERACTION = EntityTypes.ac;
    public static final EntityTypes<EntityIronGolem> IRON_GOLEM = EntityTypes.ad;
    public static final EntityTypes<EntityItem> ITEM = EntityTypes.ae;
    public static final EntityTypes<Display.ItemDisplay> ITEM_DISPLAY = EntityTypes.af;
    public static final EntityTypes<EntityItemFrame> ITEM_FRAME = EntityTypes.ag;
    public static final EntityTypes<EntityLargeFireball> LARGE_FIREBALL = EntityTypes.ah;
    public static final EntityTypes<EntityLeash> LEASH = EntityTypes.ai;
    public static final EntityTypes<EntityLightning> LIGHTNING = EntityTypes.aj;
    public static final EntityTypes<EntityLlama> LLAMA = EntityTypes.ak;
    public static final EntityTypes<EntityLlamaSpit> LLAMA_SPIT = EntityTypes.al;
    public static final EntityTypes<EntityMagmaCube> MAGMA_CUBE = EntityTypes.am;
    public static final EntityTypes<Marker> MARKER = EntityTypes.an;
    public static final EntityTypes<EntityMinecartRideable> MINECART_RIDEABLE = EntityTypes.ao;
    public static final EntityTypes<EntityMushroomCow> MUSHROOM_COW = EntityTypes.ap;
    public static final EntityTypes<EntityHorseMule> HORSE_MULE = EntityTypes.aq;
    public static final EntityTypes<EntityOcelot> OCELOT = EntityTypes.ar;
    public static final EntityTypes<EntityPainting> PAINTING = EntityTypes.as;
    public static final EntityTypes<EntityPanda> PANDA = EntityTypes.at;
    public static final EntityTypes<EntityParrot> PARROT = EntityTypes.au;
    public static final EntityTypes<EntityPhantom> PHANTOM = EntityTypes.av;
    public static final EntityTypes<EntityPig> PIG = EntityTypes.aw;
    public static final EntityTypes<EntityPiglin> PIGLIN = EntityTypes.ax;
    public static final EntityTypes<EntityPiglinBrute> PIGLIN_BRUTE = EntityTypes.ay;
    public static final EntityTypes<EntityPillager> PILLAGER = EntityTypes.az;
    public static final EntityTypes<EntityPolarBear> POLAR_BEAR = EntityTypes.aA;
    public static final EntityTypes<EntityPotion> POTION = EntityTypes.aB;
    public static final EntityTypes<EntityPufferFish> PUFFER_FISH = EntityTypes.aC;
    public static final EntityTypes<EntityRabbit> RABBIT = EntityTypes.aD;
    public static final EntityTypes<EntityRavager> RAVAGER = EntityTypes.aE;
    public static final EntityTypes<EntitySalmon> SALMON = EntityTypes.aF;
    public static final EntityTypes<EntitySheep> SHEEP = EntityTypes.aG;
    public static final EntityTypes<EntityShulker> SHULKER = EntityTypes.aH;
    public static final EntityTypes<EntityShulkerBullet> SHULKER_BULLET = EntityTypes.aI;
    public static final EntityTypes<EntitySilverfish> SILVERFISH = EntityTypes.aJ;
    public static final EntityTypes<EntitySkeleton> SKELETON = EntityTypes.aK;
    public static final EntityTypes<EntityHorseSkeleton> HORSE_SKELETON = EntityTypes.aL;
    public static final EntityTypes<EntitySlime> SLIME = EntityTypes.aM;
    public static final EntityTypes<EntitySmallFireball> SMALL_FIREBALL = EntityTypes.aN;
    public static final EntityTypes<Sniffer> SNIFFER = EntityTypes.aO;
    public static final EntityTypes<EntitySnowman> SNOWMAN = EntityTypes.aP;
    public static final EntityTypes<EntitySnowball> SNOWBALL = EntityTypes.aQ;
    public static final EntityTypes<EntityMinecartMobSpawner> MINECART_MOB_SPAWNER = EntityTypes.aR;
    public static final EntityTypes<EntitySpectralArrow> SPECTRAL_ARROW = EntityTypes.aS;
    public static final EntityTypes<EntitySpider> SPIDER = EntityTypes.aT;
    public static final EntityTypes<EntitySquid> SQUID = EntityTypes.aU;
    public static final EntityTypes<EntitySkeletonStray> SKELETON_STRAY = EntityTypes.aV;
    public static final EntityTypes<EntityStrider> STRIDER = EntityTypes.aW;
    public static final EntityTypes<Tadpole> TADPOLE = EntityTypes.aX;
    public static final EntityTypes<Display.TextDisplay> TEXT_DISPLAY = EntityTypes.aY;
    public static final EntityTypes<EntityTNTPrimed> TNT_PRIMED = EntityTypes.aZ;
    public static final EntityTypes<EntityMinecartTNT> MINECART_TNT = EntityTypes.ba;
    public static final EntityTypes<EntityLlamaTrader> LLAMA_TRADER = EntityTypes.bb;
    public static final EntityTypes<EntityThrownTrident> THROWN_TRIDENT = EntityTypes.bc;
    public static final EntityTypes<EntityTropicalFish> TROPICAL_FISH = EntityTypes.bd;
    public static final EntityTypes<EntityTurtle> TURTLE = EntityTypes.be;
    public static final EntityTypes<EntityVex> VEX = EntityTypes.bf;
    public static final EntityTypes<EntityVillager> VILLAGER = EntityTypes.bg;
    public static final EntityTypes<EntityVindicator> VINDICATOR = EntityTypes.bh;
    public static final EntityTypes<EntityVillagerTrader> VILLAGER_TRADER = EntityTypes.bi;
    public static final EntityTypes<Warden> WARDEN = EntityTypes.bj;
    public static final EntityTypes<WindCharge> WIND_CHARGE = EntityTypes.bk;
    public static final EntityTypes<EntityWitch> WITCH = EntityTypes.bl;
    public static final EntityTypes<EntityWither> WITHER = EntityTypes.bm;
    public static final EntityTypes<EntitySkeletonWither> SKELETON_WITHER = EntityTypes.bn;
    public static final EntityTypes<EntityWitherSkull> WITHER_SKULL = EntityTypes.bo;
    public static final EntityTypes<EntityWolf> WOLF = EntityTypes.bp;
    public static final EntityTypes<EntityZoglin> ZOGLIN = EntityTypes.bq;
    public static final EntityTypes<EntityZombie> ZOMBIE = EntityTypes.br;
    public static final EntityTypes<EntityHorseZombie> HORSE_ZOMBIE = EntityTypes.bs;
    public static final EntityTypes<EntityZombieVillager> ZOMBIE_VILLAGER = EntityTypes.bt;
    public static final EntityTypes<EntityPigZombie> PIG_ZOMBIE = EntityTypes.bu;
    public static final EntityTypes<EntityHuman> HUMAN = EntityTypes.bv;
    public static final EntityTypes<EntityFishingHook> FISHING_HOOK = EntityTypes.bw;

}
