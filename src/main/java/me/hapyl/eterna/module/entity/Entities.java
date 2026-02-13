package me.hapyl.eterna.module.entity;

import com.google.common.collect.Lists;
import me.hapyl.eterna.module.util.Disposable;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Allows spawning entities into the world, with the support of {@link EntityCache}.
 *
 * @param <T> - The type of the entity.
 */
@SuppressWarnings("unused")
public final class Entities<T extends Entity> {
    
    public static final Entities<ExperienceOrb> EXPERIENCE_ORB;
    public static final Entities<AreaEffectCloud> AREA_EFFECT_CLOUD;
    public static final Entities<ElderGuardian> ELDER_GUARDIAN;
    public static final Entities<WitherSkeleton> WITHER_SKELETON;
    public static final Entities<Stray> STRAY;
    public static final Entities<Egg> EGG;
    public static final Entities<LeashHitch> LEASH_HITCH;
    public static final Entities<Painting> PAINTING;
    public static final Entities<Arrow> ARROW;
    public static final Entities<Snowball> SNOWBALL;
    public static final Entities<LargeFireball> FIREBALL;
    public static final Entities<SmallFireball> SMALL_FIREBALL;
    public static final Entities<EnderPearl> ENDER_PEARL;
    public static final Entities<EnderSignal> ENDER_SIGNAL;
    public static final Entities<ThrownExpBottle> THROWN_EXP_BOTTLE;
    public static final Entities<ItemFrame> ITEM_FRAME;
    public static final Entities<WitherSkull> WITHER_SKULL;
    public static final Entities<TNTPrimed> PRIMED_TNT;
    public static final Entities<Husk> HUSK;
    public static final Entities<SpectralArrow> SPECTRAL_ARROW;
    public static final Entities<ShulkerBullet> SHULKER_BULLET;
    public static final Entities<DragonFireball> DRAGON_FIREBALL;
    public static final Entities<ZombieVillager> ZOMBIE_VILLAGER;
    public static final Entities<SkeletonHorse> SKELETON_HORSE;
    public static final Entities<ZombieHorse> ZOMBIE_HORSE;
    public static final Entities<ArmorStand> ARMOR_STAND;
    public static final Entities<Donkey> DONKEY;
    public static final Entities<Mule> MULE;
    public static final Entities<EvokerFangs> EVOKER_FANGS;
    public static final Entities<Evoker> EVOKER;
    public static final Entities<Vex> VEX;
    public static final Entities<Vindicator> VINDICATOR;
    public static final Entities<Illusioner> ILLUSIONER;
    public static final Entities<CommandMinecart> MINECART_COMMAND;
    public static final Entities<Boat> BOAT;
    public static final Entities<RideableMinecart> MINECART;
    public static final Entities<StorageMinecart> MINECART_CHEST;
    public static final Entities<PoweredMinecart> MINECART_FURNACE;
    public static final Entities<ExplosiveMinecart> MINECART_TNT;
    public static final Entities<HopperMinecart> MINECART_HOPPER;
    public static final Entities<SpawnerMinecart> MINECART_MOB_SPAWNER;
    public static final Entities<Creeper> CREEPER;
    public static final Entities<Skeleton> SKELETON;
    public static final Entities<Spider> SPIDER;
    public static final Entities<Giant> GIANT;
    public static final Entities<Zombie> ZOMBIE;
    public static final Entities<Slime> SLIME;
    public static final Entities<Ghast> GHAST;
    public static final Entities<PigZombie> ZOMBIFIED_PIGLIN;
    public static final Entities<Enderman> ENDERMAN;
    public static final Entities<CaveSpider> CAVE_SPIDER;
    public static final Entities<Silverfish> SILVERFISH;
    public static final Entities<Blaze> BLAZE;
    public static final Entities<MagmaCube> MAGMA_CUBE;
    public static final Entities<EnderDragon> ENDER_DRAGON;
    public static final Entities<Wither> WITHER;
    public static final Entities<Bat> BAT;
    public static final Entities<Witch> WITCH;
    public static final Entities<Endermite> ENDERMITE;
    public static final Entities<Guardian> GUARDIAN;
    public static final Entities<Shulker> SHULKER;
    public static final Entities<Pig> PIG;
    public static final Entities<Sheep> SHEEP;
    public static final Entities<Cow> COW;
    public static final Entities<Chicken> CHICKEN;
    public static final Entities<Squid> SQUID;
    public static final Entities<Wolf> WOLF;
    public static final Entities<MushroomCow> MUSHROOM_COW;
    public static final Entities<Snowman> SNOWMAN;
    public static final Entities<Ocelot> OCELOT;
    public static final Entities<IronGolem> IRON_GOLEM;
    public static final Entities<Horse> HORSE;
    public static final Entities<Rabbit> RABBIT;
    public static final Entities<PolarBear> POLAR_BEAR;
    public static final Entities<Llama> LLAMA;
    public static final Entities<LlamaSpit> LLAMA_SPIT;
    public static final Entities<Parrot> PARROT;
    public static final Entities<Villager> VILLAGER;
    public static final Entities<EnderCrystal> ENDER_CRYSTAL;
    public static final Entities<Turtle> TURTLE;
    public static final Entities<Phantom> PHANTOM;
    public static final Entities<Trident> TRIDENT;
    public static final Entities<Cod> COD;
    public static final Entities<Salmon> SALMON;
    public static final Entities<PufferFish> PUFFERFISH;
    public static final Entities<TropicalFish> TROPICAL_FISH;
    public static final Entities<Drowned> DROWNED;
    public static final Entities<Dolphin> DOLPHIN;
    public static final Entities<Cat> CAT;
    public static final Entities<Panda> PANDA;
    public static final Entities<Pillager> PILLAGER;
    public static final Entities<Ravager> RAVAGER;
    public static final Entities<TraderLlama> TRADER_LLAMA;
    public static final Entities<WanderingTrader> WANDERING_TRADER;
    public static final Entities<Fox> FOX;
    public static final Entities<Bee> BEE;
    public static final Entities<Hoglin> HOGLIN;
    public static final Entities<Piglin> PIGLIN;
    public static final Entities<Strider> STRIDER;
    public static final Entities<Zoglin> ZOGLIN;
    public static final Entities<PiglinBrute> PIGLIN_BRUTE;
    public static final Entities<Axolotl> AXOLOTL;
    public static final Entities<GlowItemFrame> GLOW_ITEM_FRAME;
    public static final Entities<GlowSquid> GLOW_SQUID;
    public static final Entities<Goat> GOAT;
    public static final Entities<Marker> MARKER;
    public static final Entities<Allay> ALLAY;
    public static final Entities<ChestBoat> CHEST_BOAT;
    public static final Entities<Frog> FROG;
    public static final Entities<Tadpole> TADPOLE;
    public static final Entities<Warden> WARDEN;
    public static final Entities<Camel> CAMEL;
    public static final Entities<Interaction> INTERACTION;
    public static final Entities<BlockDisplay> BLOCK_DISPLAY;
    public static final Entities<ItemDisplay> ITEM_DISPLAY;
    public static final Entities<TextDisplay> TEXT_DISPLAY;
    public static final Entities<FallingBlock> FALLING_BLOCK;
    public static final Entities<ThrownPotion> POTION;
    public static final Entities<Sniffer> SNIFFER;
    public static final Entities<Breeze> BREEZE;
    public static final Entities<Creaking> CREAKING;
    public static final Entities<HappyGhast> HAPPY_GHAST;
    public static final Entities<Nautilus> NAUTILUS;
    public static final Entities<ZombieNautilus> ZOMBIE_NAUTILUS;
    public static final Entities<Parched> PARCHED;
    public static final Entities<CamelHusk> CAMEL_HUSK;
    
    private static final List<Entities<?>> values;
    private static EntityCache defaultCache;
    
    static {
        values = Lists.newArrayList();
        defaultCache = new EntityCache();
        
        // Register entity types
        EXPERIENCE_ORB = register(ExperienceOrb.class);
        AREA_EFFECT_CLOUD = register(AreaEffectCloud.class);
        ELDER_GUARDIAN = register(ElderGuardian.class);
        WITHER_SKELETON = register(WitherSkeleton.class);
        STRAY = register(Stray.class);
        EGG = register(Egg.class);
        LEASH_HITCH = register(LeashHitch.class);
        PAINTING = register(Painting.class);
        ARROW = register(Arrow.class);
        SNOWBALL = register(Snowball.class);
        FIREBALL = register(LargeFireball.class);
        SMALL_FIREBALL = register(SmallFireball.class);
        ENDER_PEARL = register(EnderPearl.class);
        ENDER_SIGNAL = register(EnderSignal.class);
        THROWN_EXP_BOTTLE = register(ThrownExpBottle.class);
        ITEM_FRAME = register(ItemFrame.class);
        WITHER_SKULL = register(WitherSkull.class);
        PRIMED_TNT = register(TNTPrimed.class);
        HUSK = register(Husk.class);
        SPECTRAL_ARROW = register(SpectralArrow.class);
        SHULKER_BULLET = register(ShulkerBullet.class);
        DRAGON_FIREBALL = register(DragonFireball.class);
        ZOMBIE_VILLAGER = register(ZombieVillager.class);
        SKELETON_HORSE = register(SkeletonHorse.class);
        ZOMBIE_HORSE = register(ZombieHorse.class);
        ARMOR_STAND = register(ArmorStand.class);
        DONKEY = register(Donkey.class);
        MULE = register(Mule.class);
        EVOKER_FANGS = register(EvokerFangs.class);
        EVOKER = register(Evoker.class);
        VEX = register(Vex.class);
        VINDICATOR = register(Vindicator.class);
        ILLUSIONER = register(Illusioner.class);
        MINECART_COMMAND = register(CommandMinecart.class);
        BOAT = register(Boat.class);
        MINECART = register(RideableMinecart.class);
        MINECART_CHEST = register(StorageMinecart.class);
        MINECART_FURNACE = register(PoweredMinecart.class);
        MINECART_TNT = register(ExplosiveMinecart.class);
        MINECART_HOPPER = register(HopperMinecart.class);
        MINECART_MOB_SPAWNER = register(SpawnerMinecart.class);
        CREEPER = register(Creeper.class);
        SKELETON = register(Skeleton.class);
        SPIDER = register(Spider.class);
        GIANT = register(Giant.class);
        ZOMBIE = register(Zombie.class);
        SLIME = register(Slime.class);
        GHAST = register(Ghast.class);
        ZOMBIFIED_PIGLIN = register(PigZombie.class);
        ENDERMAN = register(Enderman.class);
        CAVE_SPIDER = register(CaveSpider.class);
        SILVERFISH = register(Silverfish.class);
        BLAZE = register(Blaze.class);
        MAGMA_CUBE = register(MagmaCube.class);
        ENDER_DRAGON = register(EnderDragon.class);
        WITHER = register(Wither.class);
        BAT = register(Bat.class);
        WITCH = register(Witch.class);
        ENDERMITE = register(Endermite.class);
        GUARDIAN = register(Guardian.class);
        SHULKER = register(Shulker.class);
        PIG = register(Pig.class);
        SHEEP = register(Sheep.class);
        COW = register(Cow.class);
        CHICKEN = register(Chicken.class);
        SQUID = register(Squid.class);
        WOLF = register(Wolf.class);
        MUSHROOM_COW = register(MushroomCow.class);
        SNOWMAN = register(Snowman.class);
        OCELOT = register(Ocelot.class);
        IRON_GOLEM = register(IronGolem.class);
        HORSE = register(Horse.class);
        RABBIT = register(Rabbit.class);
        POLAR_BEAR = register(PolarBear.class);
        LLAMA = register(Llama.class);
        LLAMA_SPIT = register(LlamaSpit.class);
        PARROT = register(Parrot.class);
        VILLAGER = register(Villager.class);
        ENDER_CRYSTAL = register(EnderCrystal.class);
        TURTLE = register(Turtle.class);
        PHANTOM = register(Phantom.class);
        TRIDENT = register(Trident.class);
        COD = register(Cod.class);
        SALMON = register(Salmon.class);
        PUFFERFISH = register(PufferFish.class);
        TROPICAL_FISH = register(TropicalFish.class);
        DROWNED = register(Drowned.class);
        DOLPHIN = register(Dolphin.class);
        CAT = register(Cat.class);
        PANDA = register(Panda.class);
        PILLAGER = register(Pillager.class);
        RAVAGER = register(Ravager.class);
        TRADER_LLAMA = register(TraderLlama.class);
        WANDERING_TRADER = register(WanderingTrader.class);
        FOX = register(Fox.class);
        BEE = register(Bee.class);
        HOGLIN = register(Hoglin.class);
        PIGLIN = register(Piglin.class);
        STRIDER = register(Strider.class);
        ZOGLIN = register(Zoglin.class);
        PIGLIN_BRUTE = register(PiglinBrute.class);
        AXOLOTL = register(Axolotl.class);
        GLOW_ITEM_FRAME = register(GlowItemFrame.class);
        GLOW_SQUID = register(GlowSquid.class);
        GOAT = register(Goat.class);
        MARKER = register(Marker.class);
        ALLAY = register(Allay.class);
        CHEST_BOAT = register(ChestBoat.class);
        FROG = register(Frog.class);
        TADPOLE = register(Tadpole.class);
        WARDEN = register(Warden.class);
        CAMEL = register(Camel.class);
        INTERACTION = register(Interaction.class);
        BLOCK_DISPLAY = register(BlockDisplay.class);
        ITEM_DISPLAY = register(ItemDisplay.class);
        TEXT_DISPLAY = register(TextDisplay.class);
        FALLING_BLOCK = register(FallingBlock.class);
        POTION = register(ThrownPotion.class);
        SNIFFER = register(Sniffer.class);
        BREEZE = register(Breeze.class);
        CREAKING = register(Creaking.class);
        HAPPY_GHAST = register(HappyGhast.class);
        NAUTILUS = register(Nautilus.class);
        ZOMBIE_NAUTILUS = register(ZombieNautilus.class);
        PARCHED = register(Parched.class);
        CAMEL_HUSK = register(CamelHusk.class);
    }
    
    private final Class<T> entityClass;
    
    Entities(@NotNull Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    /**
     * Spawns this entity at the given {@link Location}.
     * <p>A default {@link EntityCache} is used for this spawn method.</p>
     *
     * @param location - The location where to spawn the entity.
     * @return a spawned entity.
     */
    @NotNull
    public T spawn(@NotNull Location location) {
        return spawn(location, null, defaultCache);
    }
    
    /**
     * Spawns this entity at the given {@link Location}.
     *
     * @param location - The location where to spawn the entity.
     * @param cache    - The cache where the entity will be stored.
     * @return a spawned entity.
     */
    @NotNull
    public T spawn(@NotNull Location location, @NotNull EntityCache cache) {
        return spawn(location, null, cache);
    }
    
    /**
     * Spawns this entity at the given {@link Location}.
     * <p>A default {@link EntityCache} is used for this spawn method.</p>
     *
     * @param location    - The location where to spawn the entity.
     * @param beforeSpawn - The function to apply before the entity spawn.
     * @return a spawned entity.
     */
    @NotNull
    public T spawn(@NotNull Location location, @NotNull Consumer<T> beforeSpawn) {
        return spawn(location, beforeSpawn, defaultCache);
    }
    
    /**
     * Spawns this entity at the given {@link Location}.
     *
     * @param location    - The location where to spawn the entity.
     * @param beforeSpawn - The function to apply before the entity spawn.
     * @param cache       - The cache where the entity will be stored.
     * @return a spawned entity.
     */
    @NotNull
    public T spawn(@NotNull Location location, @Nullable Consumer<T> beforeSpawn, @NotNull EntityCache cache) {
        final T entity = location.getWorld().spawn(
                location, entityClass, self -> {
                    if (beforeSpawn != null) {
                        beforeSpawn.accept(self);
                    }
                }
        );
        
        cache.add(entity);
        return entity;
    }
    
    /**
     * Gets the default {@link EntityCache} for the server.
     *
     * @return the default {@link EntityCache} for the server.
     */
    @NotNull
    public static EntityCache defaultCache() {
        return defaultCache;
    }
    
    /**
     * Sets the default {@link EntityCache} for the server.
     * <p>Previous cache will be {@link EntityCache#dispose()} before setting a new cache, meaning all spawned entity will be removed!</p>
     *
     * @param cache - The new cache.
     */
    public static void defaultCache(@NotNull EntityCache cache) {
        Objects.requireNonNull(cache, "Cache must not be null!");
        
        defaultCache.dispose();
        defaultCache = cache;
    }
    
    /**
     * Gets an immutable copy of all registered {@link Entities}.
     *
     * @return an immutable copy of all registered {@link Entities}.
     */
    @NotNull
    public static List<Entities<?>> values() {
        return List.copyOf(values);
    }
    
    @NotNull
    @ApiStatus.Internal
    private static <T extends Entity> Entities<T> register(@NotNull Class<T> entityClass) {
        final Entities<T> registered = new Entities<>(entityClass);
        
        values.add(registered);
        return registered;
    }
    
    /**
     * Represents an {@link EntityCache} to store spawned entities.
     */
    public static class EntityCache extends HashSet<Entity> implements Disposable {
        
        /**
         * Disposes of the entities by removing all spawned entities.
         */
        @Override
        public final void dispose() {
            this.forEach(Entity::remove);
            this.clear();
        }
    }
    
    
}
