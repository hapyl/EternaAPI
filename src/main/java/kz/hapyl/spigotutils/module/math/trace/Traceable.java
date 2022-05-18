package kz.hapyl.spigotutils.module.math.trace;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public interface Traceable {

    void trace();

    static List<LivingEntity> getNearbyLivingEntities(Location location, double radius) {
        return getNearbyLivingEntities(location, radius, null);
    }

    static List<LivingEntity> getNearbyLivingEntities(Location location, double radius, @Nullable Predicate<LivingEntity> predicate) {
        List<LivingEntity> entities = new ArrayList<>();
        if (location.getWorld() == null) {
            return entities;
        }

        location.getWorld().getNearbyEntities(
                location,
                radius,
                radius,
                radius,
                entity -> entity instanceof LivingEntity living && (predicate == null || predicate.test(living))
        ).forEach(living -> entities.add((LivingEntity) living));

        return entities;
    }

}
