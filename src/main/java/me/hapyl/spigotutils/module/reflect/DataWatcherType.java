package me.hapyl.spigotutils.module.reflect;

import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.core.Vector3f;
import net.minecraft.core.particles.ParticleParam;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.network.syncher.DataWatcherSerializer;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.IBlockData;
import org.joml.Quaternionf;

import java.util.Optional;

public class DataWatcherType<T> {

    public static final DataWatcherType<Byte> BYTE = new DataWatcherType<>(Byte.class, DataWatcherRegistry.a);
    public static final DataWatcherType<Integer> INT = new DataWatcherType<>(Integer.class, DataWatcherRegistry.b);
    public static final DataWatcherType<Float> FLOAT = new DataWatcherType<>(Float.class, DataWatcherRegistry.d);
    public static final DataWatcherType<String> STR = new DataWatcherType<>(String.class, DataWatcherRegistry.e);
    public static final DataWatcherType<Boolean> BOOL = new DataWatcherType<>(Boolean.class, DataWatcherRegistry.k);
    public static final DataWatcherType<IChatBaseComponent> CHAT = new DataWatcherType<>(IChatBaseComponent.class, DataWatcherRegistry.f);
    public static final DataWatcherType<ItemStack> ITEM = new DataWatcherType<>(ItemStack.class, DataWatcherRegistry.h);
    public static final DataWatcherType<IBlockData> BLOCK_DATA = new DataWatcherType<>(IBlockData.class, DataWatcherRegistry.i);
    public static final DataWatcherType<ParticleParam> PARTICLE = new DataWatcherType<>(ParticleParam.class, DataWatcherRegistry.l);
    public static final DataWatcherType<Vector3f> VECTOR3F = new DataWatcherType<>(Vector3f.class, DataWatcherRegistry.n);
    public static final DataWatcherType<BlockPosition> BLOCK_POS = new DataWatcherType<>(BlockPosition.class, DataWatcherRegistry.o);
    public static final DataWatcherType<EnumDirection> DIRECTION = new DataWatcherType<>(EnumDirection.class, DataWatcherRegistry.q);
    public static final DataWatcherType<VillagerData> VILLAGER = new DataWatcherType<>(VillagerData.class, DataWatcherRegistry.u);
    public static final DataWatcherType<EntityPose> ENTITY_POSE = new DataWatcherType<>(EntityPose.class, DataWatcherRegistry.w);
    public static final DataWatcherType<Quaternionf> QUATERNION = new DataWatcherType<>(Quaternionf.class, DataWatcherRegistry.E);

    private final Class<T> clazz;
    private final DataWatcherSerializer<T> fieldName;

    private DataWatcherType(Class<T> clazz, DataWatcherSerializer<T> fieldName) {
        this.clazz = clazz;
        this.fieldName = fieldName;
    }

    public Class<?> getClazz() {
        return this.clazz;
    }

    public DataWatcherSerializer<T> get() {
        return fieldName;
    }

}
