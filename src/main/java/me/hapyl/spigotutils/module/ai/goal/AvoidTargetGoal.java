package me.hapyl.spigotutils.module.ai.goal;

import me.hapyl.spigotutils.module.ai.AI;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.horse.*;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import org.bukkit.entity.EntityType;

import javax.annotation.Nonnull;

/**
 * Adds a goal to avoid an entity type.
 */
public class AvoidTargetGoal extends Goal {

    /**
     * Adds goal to avoid an entity type.
     *
     * @param ai                  - AI reference.
     * @param avoid               - Entity type to avoid.
     * @param maxDistance         - Max distance to avoid.
     * @param walkSpeedModifier   - Walk speed modifier.
     * @param sprintSpeedModifier - Sprint speed modifier.
     */
    public AvoidTargetGoal(AI ai, EntityType avoid, float maxDistance, double walkSpeedModifier, double sprintSpeedModifier) {
        super(new AvoidEntityGoal<>(
                (PathfinderMob) ai.getMob(),
                entityLivingClassFromType(avoid),
                maxDistance,
                walkSpeedModifier,
                sprintSpeedModifier
        ));
    }

    @Nonnull
    public static Class<? extends LivingEntity> entityLivingClassFromType(EntityType type) {
        return switch (type) {
            case PLAYER -> ServerPlayer.class;
            case CREEPER -> Creeper.class;
            case SKELETON -> Skeleton.class;
            case SPIDER -> Spider.class;
            case GIANT -> Giant.class;
            case ZOMBIE -> Zombie.class;
            case SLIME -> Slime.class;
            case GHAST -> Ghast.class;
            case ZOMBIFIED_PIGLIN -> ZombifiedPiglin.class;
            case ENDERMAN -> EnderMan.class;
            case CAVE_SPIDER -> CaveSpider.class;
            case SILVERFISH -> Silverfish.class;
            case BLAZE -> Blaze.class;
            case MAGMA_CUBE -> MagmaCube.class;
            case ENDER_DRAGON -> EnderDragon.class;
            case WITHER -> WitherBoss.class;
            case BAT -> Bat.class;
            case WITCH -> Witch.class;
            case ENDERMITE -> Endermite.class;
            case GUARDIAN -> Guardian.class;
            case SHULKER -> Shulker.class;
            case PIG -> Pig.class;
            case SHEEP -> Sheep.class;
            case COW -> Cow.class;
            case CHICKEN -> Chicken.class;
            case SQUID -> Squid.class;
            case WOLF -> Wolf.class;
            case MOOSHROOM -> MushroomCow.class;
            case SNOW_GOLEM -> SnowGolem.class;
            case OCELOT -> Ocelot.class;
            case IRON_GOLEM -> IronGolem.class;
            case HORSE -> Horse.class;
            case RABBIT -> Rabbit.class;
            case POLAR_BEAR -> PolarBear.class;
            case LLAMA -> Llama.class;
            case PARROT -> Parrot.class;
            case VILLAGER -> Villager.class;
            case TURTLE -> Turtle.class;
            case PHANTOM -> Phantom.class;
            case COD -> Cod.class;
            case SALMON -> Salmon.class;
            case PUFFERFISH -> Pufferfish.class;
            case TROPICAL_FISH -> TropicalFish.class;
            case DROWNED -> Drowned.class;
            case DOLPHIN -> Dolphin.class;
            case CAT -> Cat.class;
            case PANDA -> Panda.class;
            case PILLAGER -> Pillager.class;
            case RAVAGER -> Ravager.class;
            case TRADER_LLAMA -> TraderLlama.class;
            case WANDERING_TRADER -> WanderingTrader.class;
            case FOX -> Fox.class;
            case BEE -> Bee.class;
            case HOGLIN -> Hoglin.class;
            case STRIDER -> Strider.class;
            case ZOGLIN -> Zoglin.class;
            case PIGLIN -> Piglin.class;
            case HUSK -> Husk.class;
            case STRAY -> Stray.class;
            case ZOMBIE_VILLAGER -> ZombieVillager.class;
            case SKELETON_HORSE -> SkeletonHorse.class;
            case ZOMBIE_HORSE -> ZombieHorse.class;
            case DONKEY -> Donkey.class;
            case MULE -> Mule.class;
            case EVOKER -> Evoker.class;
            case VEX -> Vex.class;
            case VINDICATOR -> Vindicator.class;
            case ILLUSIONER -> Illusioner.class;
            case FROG -> Frog.class;
            case GOAT -> Goat.class;
            case CAMEL -> Camel.class;
            case WARDEN -> Warden.class;
            case AXOLOTL -> Axolotl.class;
            case SNIFFER -> Sniffer.class;
            case TADPOLE -> Tadpole.class;
            case ALLAY -> Allay.class;
            case GLOW_SQUID -> GlowSquid.class;

            default -> throw new IllegalArgumentException(type + " is not supported for AvoidTargetGoal");
        };
    }

}
