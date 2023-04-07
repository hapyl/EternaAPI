package me.hapyl.spigotutils.module.entity;

import me.hapyl.spigotutils.module.util.Validate;
import me.hapyl.spigotutils.registry.EternaRegistry;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import javax.annotation.Nonnull;

/**
 * Creates a rope between two locations.
 */
public class Rope implements IdHolder, Spawnable<Rope> {

    private final Location start;
    private final Location end;
    private final LivingEntity[] entities;

    private int id;

    public Rope(Location start, Location end) {
        Validate.notNull(start);
        Validate.notNull(end);
        Validate.isTrue(start.getWorld() == end.getWorld(), "Locations must be in the same world!");

        this.start = start;
        this.end = end;
        this.entities = new LivingEntity[2];
        this.id = -1;
    }

    /**
     * Creates the rope if not already created.
     */
    @Override
    public Rope spawn() {
        if (isSpawned()) {
            return this;
        }

        entities[0] = createEntity(start);
        entities[1] = createEntity(end);

        entities[0].setLeashHolder(entities[1]);

        EternaRegistry.getRopeRegistry().registerValue(this);

        return this;
    }

    public void remove() {
        if (!isSpawned()) {
            return;
        }

        for (LivingEntity entity : entities) {
            entity.setLeashHolder(null);
            entity.remove();
        }

        id = -2;
    }

    @Override
    public boolean isSpawned() {
        final LivingEntity entity = entities[0];
        final LivingEntity otherEntity = entities[1];

        return (entity != null && otherEntity != null) && (!entity.isDead() && !otherEntity.isDead());
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Nonnull
    private LivingEntity createEntity(Location location) {
        return Entities.SLIME.spawn(location, self -> {
            self.setSize(1);
            self.setInvulnerable(true);
            self.setInvisible(true);
            self.setAI(false);
            self.setSilent(true);
            self.setGravity(false);
            self.getScoreboardTags().add("EternaRope");
        });
    }
}
