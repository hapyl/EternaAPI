package me.hapyl.eterna.module.entity;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.module.util.Validate;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

/**
 * Creates a rope between two locations.
 */
public class Rope implements Spawnable<Rope> {

    private final Location start;
    private final Location end;
    private final LivingEntity[] entities;

    public Rope(Location start, Location end) {
        Validate.nonNull(start);
        Validate.nonNull(end);
        Validate.isTrue(start.getWorld() == end.getWorld(), "Locations must be in the same world!");

        this.start = start;
        this.end = end;
        this.entities = new LivingEntity[2];
    }

    /**
     * Creates the rope if not already created.
     */
    @Override
    public @NotNull Rope spawn() {
        if (isSpawned()) {
            return this;
        }

        entities[0] = createEntity(start);
        entities[1] = createEntity(end);

        entities[0].setLeashHolder(entities[1]);

        Eterna.getManagers().rope.register(this);

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
    }

    @Override
    public boolean isSpawned() {
        final LivingEntity entity = entities[0];
        final LivingEntity otherEntity = entities[1];

        return (entity != null && otherEntity != null) && (!entity.isDead() && !otherEntity.isDead());
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
