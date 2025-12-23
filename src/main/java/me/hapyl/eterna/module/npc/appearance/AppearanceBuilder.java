package me.hapyl.eterna.module.npc.appearance;

import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.reflect.Skin;

import javax.annotation.Nonnull;
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
    @Nonnull
    T build(@Nonnull Npc npc);
    
    @Nonnull
    static <T extends Appearance> AppearanceBuilder<T> builder(@Nonnull AppearanceBuilder<T> builder, @Nonnull Consumer<T> consumer) {
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
    @Nonnull
    static AppearanceBuilder<AppearanceMannequin> ofMannequin(@Nonnull Skin skin) {
        return npc -> new AppearanceMannequin(npc, skin);
    }
    
    /**
     * Creates a new {@link AppearanceSheep} with the given {@link SheepColor}.
     *
     * @param sheepColor - The sheep color.
     * @return a new {@link AppearanceSheep}.
     */
    @Nonnull
    static AppearanceBuilder<AppearanceSheep> ofSheep(@Nonnull SheepColor sheepColor) {
        return npc -> new AppearanceSheep(npc, sheepColor);
    }
    
    /**
     * Creates a new {@link AppearanceFox} with the given {@link FoxType}.
     *
     * @param foxType - The fox type.
     * @return a new {@link AppearanceFox}.
     */
    @Nonnull
    static AppearanceBuilder<AppearanceFox> ofFox(@Nonnull FoxType foxType) {
        return npc -> new AppearanceFox(npc, foxType);
    }
    
    /**
     * Creates a new {@link AppearanceHusk}.
     *
     * @return a new {@link AppearanceHusk}.
     */
    @Nonnull
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
    @Nonnull
    static AppearanceBuilder<AppearanceVillager> ofVillager(@Nonnull VillagerVariant variant, @Nonnull VillagerProfession profession, @Nonnull VillagerLevel level) {
        return npc -> new AppearanceVillager(npc, variant, profession, level);
    }
    
}
