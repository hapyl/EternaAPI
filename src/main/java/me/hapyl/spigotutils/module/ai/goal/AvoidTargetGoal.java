package me.hapyl.spigotutils.module.ai.goal;

import me.hapyl.spigotutils.module.ai.AI;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.ai.goal.PathfinderGoalAvoidTarget;
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
import net.minecraft.world.entity.boss.enderdragon.EntityEnderDragon;
import net.minecraft.world.entity.boss.wither.EntityWither;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.hoglin.EntityHoglin;
import net.minecraft.world.entity.monster.piglin.EntityPiglin;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.EntityVillager;
import net.minecraft.world.entity.npc.EntityVillagerTrader;
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
        super(new PathfinderGoalAvoidTarget<>(
                (EntityCreature) ai.getMob(),
                entityLivingClassFromType(avoid),
                maxDistance,
                walkSpeedModifier,
                sprintSpeedModifier
        ));
    }

    @Nonnull
    public static Class<? extends EntityLiving> entityLivingClassFromType(EntityType type) {
        return switch (type) {
            case PLAYER -> EntityPlayer.class;
            case CREEPER -> EntityCreeper.class;
            case SKELETON -> EntitySkeleton.class;
            case SPIDER -> EntitySpider.class;
            case GIANT -> EntityGiantZombie.class;
            case ZOMBIE -> EntityZombie.class;
            case SLIME -> EntitySlime.class;
            case GHAST -> EntityGhast.class;
            case ZOMBIFIED_PIGLIN -> EntityPigZombie.class;
            case ENDERMAN -> EntityEnderman.class;
            case CAVE_SPIDER -> EntityCaveSpider.class;
            case SILVERFISH -> EntitySilverfish.class;
            case BLAZE -> EntityBlaze.class;
            case MAGMA_CUBE -> EntityMagmaCube.class;
            case ENDER_DRAGON -> EntityEnderDragon.class;
            case WITHER -> EntityWither.class;
            case BAT -> EntityBat.class;
            case WITCH -> EntityWitch.class;
            case ENDERMITE -> EntityEndermite.class;
            case GUARDIAN -> EntityGuardian.class;
            case SHULKER -> EntityShulker.class;
            case PIG -> EntityPig.class;
            case SHEEP -> EntitySheep.class;
            case COW -> EntityCow.class;
            case CHICKEN -> EntityChicken.class;
            case SQUID -> EntitySquid.class;
            case WOLF -> EntityWolf.class;
            case MOOSHROOM -> EntityMushroomCow.class;
            case SNOW_GOLEM -> EntitySnowman.class;
            case OCELOT -> EntityOcelot.class;
            case IRON_GOLEM -> EntityIronGolem.class;
            case HORSE -> EntityHorse.class;
            case RABBIT -> EntityRabbit.class;
            case POLAR_BEAR -> EntityPolarBear.class;
            case LLAMA -> EntityLlama.class;
            case PARROT -> EntityParrot.class;
            case VILLAGER -> EntityVillager.class;
            case TURTLE -> EntityTurtle.class;
            case PHANTOM -> EntityPhantom.class;
            case COD -> EntityCod.class;
            case SALMON -> EntitySalmon.class;
            case PUFFERFISH -> EntityPufferFish.class;
            case TROPICAL_FISH -> EntityTropicalFish.class;
            case DROWNED -> EntityDrowned.class;
            case DOLPHIN -> EntityDolphin.class;
            case CAT -> EntityCat.class;
            case PANDA -> EntityPanda.class;
            case PILLAGER -> EntityPillager.class;
            case RAVAGER -> EntityRavager.class;
            case TRADER_LLAMA -> EntityLlamaTrader.class;
            case WANDERING_TRADER -> EntityVillagerTrader.class;
            case FOX -> EntityFox.class;
            case BEE -> EntityBee.class;
            case HOGLIN -> EntityHoglin.class;
            case STRIDER -> EntityStrider.class;
            case ZOGLIN -> EntityZoglin.class;
            case PIGLIN -> EntityPiglin.class;
            case HUSK -> EntityZombieHusk.class;
            case STRAY -> EntitySkeletonStray.class;
            case ZOMBIE_VILLAGER -> EntityZombieVillager.class;
            case SKELETON_HORSE -> EntityHorseSkeleton.class;
            case ZOMBIE_HORSE -> EntityHorseZombie.class;
            case DONKEY -> EntityHorseDonkey.class;
            case MULE -> EntityHorseMule.class;
            case EVOKER -> EntityEvoker.class;
            case VEX -> EntityVex.class;
            case VINDICATOR -> EntityVindicator.class;
            case ILLUSIONER -> EntityIllagerIllusioner.class;
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
