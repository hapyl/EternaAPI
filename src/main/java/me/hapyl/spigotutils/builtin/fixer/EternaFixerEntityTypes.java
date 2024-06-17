package me.hapyl.spigotutils.builtin.fixer;

import net.minecraft.core.RegistryBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.entity.EntityType;

import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

public class EternaFixerEntityTypes implements EternaFixer {

    @Override
    public void fix(FileWriter fileWriter) throws Exception {
        final RegistryBlocks<EntityTypes<?>> registry = BuiltInRegistries.f;

        final StringBuilder builder = new StringBuilder();
        final StringBuilder builderStatic = new StringBuilder("static {\n");

        for (Field field : EntityTypes.class.getDeclaredFields()) {
            final int modifiers = field.getModifiers();

            if (!Modifier.isStatic(modifiers)) {
                continue;
            }

            if (!Modifier.isPublic(modifiers)) {
                continue;
            }

            final EntityTypes<?> type = (EntityTypes<?>) field.get(null);
            final MinecraftKey minecraftKey = registry.b(type);
            final String key = minecraftKey.a().toUpperCase();

            final Type generic = field.getGenericType();
            String genericName = generic.toString();
            genericName = genericName.substring(genericName.lastIndexOf(".") + 1, genericName.lastIndexOf(">"));
            genericName = genericName.replace("$", "."); // fix inner

            builder.append("public static final EntityTypes<%s> %s;\n".formatted(genericName, key));
            builderStatic.append("%s = of(EntityTypes.%s, EntityType.%s);\n".formatted(key, field.getName(), fromNms(key)));
        }

        builderStatic.append("}");

        fileWriter.append("""
                // STATIC FIELDS
                """);
        fileWriter.append(builder.toString());

        fileWriter.append("""
                // STATIC INITIALIZATION
                """);
        fileWriter.append(builderStatic.toString());
    }

    private EntityType fromNms(String key) {
        key = key.toLowerCase();

        for (EntityType value : EntityType.values()) {
            try {
                if (value.getKey().getKey().toLowerCase().equals(key)) {
                    return value;
                }
            } catch (Exception ignored) {
                return null; // why throw exception bruh
            }
        }

        return null;
    }
}
