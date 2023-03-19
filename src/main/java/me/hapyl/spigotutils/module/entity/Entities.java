package me.hapyl.spigotutils.module.entity;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.util.Nulls;
import me.hapyl.spigotutils.module.util.Validate;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.EntitySquid;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.entity.monster.EntityGuardian;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Consumer;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * This class allows to summon entities easier
 * and saves them into a hash set. Unless set not to.
 *
 * @param <T> - Type of the entity.
 * @author hapyl
 */
@SuppressWarnings("unused" /*utility class*/)
public final class Entities<T extends Entity> {

    public static class NMS {

        public static final EntityTypes<EntityGuardian> GUARDIAN = EntityTypes.V;
        public static final EntityTypes<EntitySquid> SQUID = EntityTypes.aT;
        public static final EntityTypes<EntityArmorStand> ARMOR_STAND = EntityTypes.d;

    }

    private static final EternaPlugin ETERNA_PLUGIN = EternaPlugin.getPlugin();

    // Saved all spawned entities per plugin.
    private static final Map<JavaPlugin, Set<Entity>> spawnedByPlugin = Maps.newConcurrentMap();

    // All types by name.
    private static final Map<String, Entities<? extends Entity>> byName = new HashMap<>();

    public static final Entities<ExperienceOrb> EXPERIENCE_ORB = new Entities<>(ExperienceOrb.class);
    public static final Entities<AreaEffectCloud> AREA_EFFECT_CLOUD = new Entities<>(AreaEffectCloud.class);
    public static final Entities<ElderGuardian> ELDER_GUARDIAN = new Entities<>(ElderGuardian.class);
    public static final Entities<WitherSkeleton> WITHER_SKELETON = new Entities<>(WitherSkeleton.class);
    public static final Entities<Stray> STRAY = new Entities<>(Stray.class);
    public static final Entities<Egg> EGG = new Entities<>(Egg.class);
    public static final Entities<LeashHitch> LEASH_HITCH = new Entities<>(LeashHitch.class);
    public static final Entities<Painting> PAINTING = new Entities<>(Painting.class);
    public static final Entities<Arrow> ARROW = new Entities<>(Arrow.class);
    public static final Entities<Snowball> SNOWBALL = new Entities<>(Snowball.class);
    public static final Entities<LargeFireball> FIREBALL = new Entities<>(LargeFireball.class);
    public static final Entities<SmallFireball> SMALL_FIREBALL = new Entities<>(SmallFireball.class);
    public static final Entities<EnderPearl> ENDER_PEARL = new Entities<>(EnderPearl.class);
    public static final Entities<EnderSignal> ENDER_SIGNAL = new Entities<>(EnderSignal.class);
    public static final Entities<ThrownExpBottle> THROWN_EXP_BOTTLE = new Entities<>(ThrownExpBottle.class);
    public static final Entities<ItemFrame> ITEM_FRAME = new Entities<>(ItemFrame.class);
    public static final Entities<WitherSkull> WITHER_SKULL = new Entities<>(WitherSkull.class);
    public static final Entities<TNTPrimed> PRIMED_TNT = new Entities<>(TNTPrimed.class);
    public static final Entities<Husk> HUSK = new Entities<>(Husk.class);
    public static final Entities<SpectralArrow> SPECTRAL_ARROW = new Entities<>(SpectralArrow.class);
    public static final Entities<ShulkerBullet> SHULKER_BULLET = new Entities<>(ShulkerBullet.class);
    public static final Entities<DragonFireball> DRAGON_FIREBALL = new Entities<>(DragonFireball.class);
    public static final Entities<ZombieVillager> ZOMBIE_VILLAGER = new Entities<>(ZombieVillager.class);
    public static final Entities<SkeletonHorse> SKELETON_HORSE = new Entities<>(SkeletonHorse.class);
    public static final Entities<ZombieHorse> ZOMBIE_HORSE = new Entities<>(ZombieHorse.class);
    public static final Entities<ArmorStand> ARMOR_STAND = new Entities<>(ArmorStand.class);
    public static final Entities<Donkey> DONKEY = new Entities<>(Donkey.class);
    public static final Entities<Mule> MULE = new Entities<>(Mule.class);
    public static final Entities<EvokerFangs> EVOKER_FANGS = new Entities<>(EvokerFangs.class);
    public static final Entities<Evoker> EVOKER = new Entities<>(Evoker.class);
    public static final Entities<Vex> VEX = new Entities<>(Vex.class);
    public static final Entities<Vindicator> VINDICATOR = new Entities<>(Vindicator.class);
    public static final Entities<Illusioner> ILLUSIONER = new Entities<>(Illusioner.class);
    public static final Entities<CommandMinecart> MINECART_COMMAND = new Entities<>(CommandMinecart.class);
    public static final Entities<Boat> BOAT = new Entities<>(Boat.class);
    public static final Entities<RideableMinecart> MINECART = new Entities<>(RideableMinecart.class);
    public static final Entities<StorageMinecart> MINECART_CHEST = new Entities<>(StorageMinecart.class);
    public static final Entities<PoweredMinecart> MINECART_FURNACE = new Entities<>(PoweredMinecart.class);
    public static final Entities<ExplosiveMinecart> MINECART_TNT = new Entities<>(ExplosiveMinecart.class);
    public static final Entities<HopperMinecart> MINECART_HOPPER = new Entities<>(HopperMinecart.class);
    public static final Entities<SpawnerMinecart> MINECART_MOB_SPAWNER = new Entities<>(SpawnerMinecart.class);
    public static final Entities<Creeper> CREEPER = new Entities<>(Creeper.class);
    public static final Entities<Skeleton> SKELETON = new Entities<>(Skeleton.class);
    public static final Entities<Spider> SPIDER = new Entities<>(Spider.class);
    public static final Entities<Giant> GIANT = new Entities<>(Giant.class);
    public static final Entities<Zombie> ZOMBIE = new Entities<>(Zombie.class);
    public static final Entities<Slime> SLIME = new Entities<>(Slime.class);
    public static final Entities<Ghast> GHAST = new Entities<>(Ghast.class);
    public static final Entities<PigZombie> ZOMBIFIED_PIGLIN = new Entities<>(PigZombie.class);
    public static final Entities<Enderman> ENDERMAN = new Entities<>(Enderman.class);
    public static final Entities<CaveSpider> CAVE_SPIDER = new Entities<>(CaveSpider.class);
    public static final Entities<Silverfish> SILVERFISH = new Entities<>(Silverfish.class);
    public static final Entities<Blaze> BLAZE = new Entities<>(Blaze.class);
    public static final Entities<MagmaCube> MAGMA_CUBE = new Entities<>(MagmaCube.class);
    public static final Entities<EnderDragon> ENDER_DRAGON = new Entities<>(EnderDragon.class);
    public static final Entities<Wither> WITHER = new Entities<>(Wither.class);
    public static final Entities<Bat> BAT = new Entities<>(Bat.class);
    public static final Entities<Witch> WITCH = new Entities<>(Witch.class);
    public static final Entities<Endermite> ENDERMITE = new Entities<>(Endermite.class);
    public static final Entities<Guardian> GUARDIAN = new Entities<>(Guardian.class);
    public static final Entities<Shulker> SHULKER = new Entities<>(Shulker.class);
    public static final Entities<Pig> PIG = new Entities<>(Pig.class);
    public static final Entities<Sheep> SHEEP = new Entities<>(Sheep.class);
    public static final Entities<Cow> COW = new Entities<>(Cow.class);
    public static final Entities<Chicken> CHICKEN = new Entities<>(Chicken.class);
    public static final Entities<Squid> SQUID = new Entities<>(Squid.class);
    public static final Entities<Wolf> WOLF = new Entities<>(Wolf.class);
    public static final Entities<MushroomCow> MUSHROOM_COW = new Entities<>(MushroomCow.class);
    public static final Entities<Snowman> SNOWMAN = new Entities<>(Snowman.class);
    public static final Entities<Ocelot> OCELOT = new Entities<>(Ocelot.class);
    public static final Entities<IronGolem> IRON_GOLEM = new Entities<>(IronGolem.class);
    public static final Entities<Horse> HORSE = new Entities<>(Horse.class);
    public static final Entities<Rabbit> RABBIT = new Entities<>(Rabbit.class);
    public static final Entities<PolarBear> POLAR_BEAR = new Entities<>(PolarBear.class);
    public static final Entities<Llama> LLAMA = new Entities<>(Llama.class);
    public static final Entities<LlamaSpit> LLAMA_SPIT = new Entities<>(LlamaSpit.class);
    public static final Entities<Parrot> PARROT = new Entities<>(Parrot.class);
    public static final Entities<Villager> VILLAGER = new Entities<>(Villager.class);
    public static final Entities<EnderCrystal> ENDER_CRYSTAL = new Entities<>(EnderCrystal.class);
    public static final Entities<Turtle> TURTLE = new Entities<>(Turtle.class);
    public static final Entities<Phantom> PHANTOM = new Entities<>(Phantom.class);
    public static final Entities<Trident> TRIDENT = new Entities<>(Trident.class);
    public static final Entities<Cod> COD = new Entities<>(Cod.class);
    public static final Entities<Salmon> SALMON = new Entities<>(Salmon.class);
    public static final Entities<PufferFish> PUFFERFISH = new Entities<>(PufferFish.class);
    public static final Entities<TropicalFish> TROPICAL_FISH = new Entities<>(TropicalFish.class);
    public static final Entities<Drowned> DROWNED = new Entities<>(Drowned.class);
    public static final Entities<Dolphin> DOLPHIN = new Entities<>(Dolphin.class);
    public static final Entities<Cat> CAT = new Entities<>(Cat.class);
    public static final Entities<Panda> PANDA = new Entities<>(Panda.class);
    public static final Entities<Pillager> PILLAGER = new Entities<>(Pillager.class);
    public static final Entities<Ravager> RAVAGER = new Entities<>(Ravager.class);
    public static final Entities<TraderLlama> TRADER_LLAMA = new Entities<>(TraderLlama.class);
    public static final Entities<WanderingTrader> WANDERING_TRADER = new Entities<>(WanderingTrader.class);
    public static final Entities<Fox> FOX = new Entities<>(Fox.class);
    public static final Entities<Bee> BEE = new Entities<>(Bee.class);
    public static final Entities<Hoglin> HOGLIN = new Entities<>(Hoglin.class);
    public static final Entities<Piglin> PIGLIN = new Entities<>(Piglin.class);
    public static final Entities<Strider> STRIDER = new Entities<>(Strider.class);
    public static final Entities<Zoglin> ZOGLIN = new Entities<>(Zoglin.class);
    public static final Entities<PiglinBrute> PIGLIN_BRUTE = new Entities<>(PiglinBrute.class);
    public static final Entities<Axolotl> AXOLOTL = new Entities<>(Axolotl.class);
    public static final Entities<GlowItemFrame> GLOW_ITEM_FRAME = new Entities<>(GlowItemFrame.class);
    public static final Entities<GlowSquid> GLOW_SQUID = new Entities<>(GlowSquid.class);
    public static final Entities<Goat> GOAT = new Entities<>(Goat.class);
    public static final Entities<Marker> MARKER = new Entities<>(Marker.class);

    // custom entities with root consumer
    public static final Entities<ArmorStand> ARMOR_STAND_MARKER = new Entities<>(ArmorStand.class, self -> self.setMarker(true));
    public static final Entities<ArmorStand> ARMOR_STAND_HOLOGRAM = new Entities<>(ArmorStand.class, self -> {
        self.setMarker(true);
        self.setSilent(true);
        self.setInvisible(true);
        self.setSmall(true);
    });

    private final Class<T> entityClass;
    private final Consumer<T> rootConsumer;

    private Entities(Class<T> entityClass, Consumer<T> rootConsumer) {
        this.entityClass = entityClass;
        this.rootConsumer = rootConsumer;
        byName.put(this.entityClass.getSimpleName().toLowerCase(Locale.ROOT), this);
    }

    private Entities(Class<T> entityClass) {
        this(entityClass, null);
    }

    /**
     * Spawn entity at provided location.
     *
     * @param location - Location to spawn at.
     * @return spawned entity.
     * @throws NullPointerException if location's world is null
     */
    public T spawn(Location location) {
        return this.spawn(location, null);
    }

    /**
     * Spawn entity at provided location.
     *
     * @param location - Location to spawn at.
     * @param cache    - if true, entity will be cached into a hash set.
     * @return spawned entity.
     * @throws NullPointerException if location's world is null.
     */
    public T spawn(Location location, boolean cache) {
        return spawn(location, null, cache);
    }

    /**
     * Spawn entity at provided location.
     *
     * @param location    - Location to spawn at.
     * @param beforeSpawn - Consumer to apply to entity before it spawns.
     * @return spawned entity.
     * @throws NullPointerException if location's world is null
     */
    public T spawn(Location location, @Nullable Consumer<T> beforeSpawn) {
        return spawn(location, beforeSpawn, true);
    }

    /**
     * Spawn entity at provided location.
     *
     * @param location    - Location to spawn at.
     * @param cache       - if true, entity will be cached into a hash set.
     * @param beforeSpawn - Consumer to apply to entity before it spawns.
     * @return spawned entity.
     * @throws NullPointerException if location's world is null
     */
    public T spawn(Location location, @Nullable Consumer<T> beforeSpawn, boolean cache) {
        return spawn(location, beforeSpawn, cache ? ETERNA_PLUGIN : null);
    }

    /**
     * Spawn entity at provided location.
     *
     * @param location    - Location to spawn at.
     * @param plugin      - Plugin owner of the cached entities. {@link this#getSpawned(JavaPlugin)}
     * @param beforeSpawn - Consumer to apply to entity before it spawns.
     * @return spawned entity.
     * @throws NullPointerException if location's world is null
     */
    public T spawn(Location location, @Nullable Consumer<T> beforeSpawn, @Nullable JavaPlugin plugin) {
        Validate.notNull(location.getWorld(), "world cannot be null");
        final T entity = location.getWorld().spawn(location, this.entityClass, self -> {
            Nulls.runIfNotNull(rootConsumer, r -> r.accept(self));
            Nulls.runIfNotNull(beforeSpawn, r -> r.accept(self));
        });

        // cache
        if (plugin != null) {
            addSpawned(plugin, entity);
        }
        return entity;
    }

    /**
     * Removes all spawned entities for Eterna plugin.
     *
     * @deprecated {@link this#killSpawned(JavaPlugin)}
     */
    @Deprecated
    public static void killSpawned() {
        killSpawned(EternaPlugin.getPlugin());
    }

    /**
     * @deprecated {@link Entities#getSpawned(JavaPlugin)}
     */
    @Deprecated
    public static Set<Entity> getEntities() {
        return getSpawned(ETERNA_PLUGIN);
    }

    /**
     * @deprecated {@link Entities#getSpawned(JavaPlugin)}
     */
    public static Set<Entity> getSpawned() {
        return getSpawned(ETERNA_PLUGIN);
    }

    public static Set<Entity> getSpawned(JavaPlugin plugin) {
        return spawnedByPlugin.computeIfAbsent(plugin, s -> Sets.newHashSet());
    }

    private static void addSpawned(JavaPlugin plugin, Entity entity) {
        getSpawned(plugin).add(entity);
    }

    public static void killSpawned(JavaPlugin plugin) {
        final Set<Entity> set = getSpawned(plugin);
        for (Entity entity : set) {
            entity.remove();
        }
        set.clear();
    }

    @Nullable
    public static Entities<? extends Entity> byName(String name) {
        return byName.getOrDefault(name.toLowerCase(Locale.ROOT), null);
    }


}
