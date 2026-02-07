package me.hapyl.eterna.module.npc.appearance;

import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.reflect.Skin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Represents a helper builder class for {@link Appearance}.
 * <p>Most of the {@link Appearance} data may be changed after the {@link Npc} has spawned via {@link Npc#getAppearance(Class)}.</p>
 *
 * @param <T> - The appearance type.
 */
public interface AppearanceBuilder<T extends Appearance> {
    
    /**
     * Builds the appearance for the given {@link Npc}.
     *
     * @param npc - The npc to build to appearance for.
     * @return the built appearance.
     */
    @NotNull
    T build(@NotNull Npc npc);
    
    /**
     * A static factory method for creating {@link AppearanceBuilder}.
     *
     * @param builder  - The builder.
     * @param consumer - The build action.
     * @param <T>      - The appearance type.
     * @return a new {@link AppearanceBuilder}.
     */
    @NotNull
    static <T extends Appearance> AppearanceBuilder<T> builder(@NotNull AppearanceBuilder<T> builder, @NotNull Consumer<T> consumer) {
        return npc -> {
            final T appearance = builder.build(npc);
            consumer.accept(appearance);
            
            return appearance;
        };
    }
    
    /**
     * Creates a new {@link AppearanceMannequin} with the given {@link Skin}.
     *
     * @param skin - The mannequin skin.
     * @return a new {@link AppearanceMannequin}.
     */
    @NotNull
    static AppearanceBuilder<AppearanceMannequin> ofMannequin(@NotNull Skin skin) {
        return npc -> new AppearanceMannequin(npc, skin);
    }
    
    /**
     * Creates a new {@link AppearanceSheep} with the given {@link SheepColor}.
     *
     * @param sheepColor - The sheep color.
     * @return a new {@link AppearanceSheep}.
     */
    @NotNull
    static AppearanceBuilder<AppearanceSheep> ofSheep(@NotNull SheepColor sheepColor) {
        return npc -> new AppearanceSheep(npc, sheepColor);
    }
    
    /**
     * Creates a new {@link AppearanceFox} with the given {@link FoxType}.
     *
     * @param foxType - The fox type.
     * @return a new {@link AppearanceFox}.
     */
    @NotNull
    static AppearanceBuilder<AppearanceFox> ofFox(@NotNull FoxType foxType) {
        return npc -> new AppearanceFox(npc, foxType);
    }
    
    /**
     * Creates a new {@link AppearanceHusk}.
     *
     * @return a new {@link AppearanceHusk}.
     */
    @NotNull
    static AppearanceBuilder<AppearanceHusk> ofHusk() {
        return AppearanceHusk::new;
    }
    
    /**
     * Creates a new {@link AppearanceVillager} with the given {@link VillagerVariant}, {@link VillagerProfession} and {@link VillagerLevel}.
     *
     * @param variant    - The villager variant.
     * @param profession - The villager profession.
     * @param level      - The villager level.
     * @return a new {@link AppearanceVillager}.
     */
    @NotNull
    static AppearanceBuilder<AppearanceVillager> ofVillager(@NotNull VillagerVariant variant, @NotNull VillagerProfession profession, @NotNull VillagerLevel level) {
        return npc -> new AppearanceVillager(npc, variant, profession, level);
    }
    
}
