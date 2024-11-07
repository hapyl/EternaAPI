package me.hapyl.eterna.module.reflect.access;

import me.hapyl.eterna.module.reflect.Reflect;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class ReflectPlayerAccess extends IAccess<Player, ServerPlayer> {

    protected ReflectPlayerAccess() {
    }

    @Nonnull
    @Override
    protected ServerPlayer createAccess(@Nonnull Player player) {
        return Reflect.getMinecraftPlayer(player);
    }
}
