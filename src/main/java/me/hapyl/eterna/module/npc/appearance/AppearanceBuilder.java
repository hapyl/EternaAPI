package me.hapyl.eterna.module.npc.appearance;

import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.reflect.Skin;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public interface AppearanceBuilder<T extends Appearance> {
    
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
    
    @Nonnull
    static AppearanceBuilder<AppearanceMannequin> ofMannequin(@Nonnull Skin skin) {
        return npc -> new AppearanceMannequin(npc, skin);
    }
    
    @Nonnull
    static AppearanceBuilder<AppearanceSheep> ofSheep(@Nonnull SheepColor sheepColor) {
        return npc -> new AppearanceSheep(npc, sheepColor);
    }
    
    @Nonnull
    static AppearanceBuilder<AppearanceFox> ofFox(@Nonnull FoxType foxType) {
        return npc -> new AppearanceFox(npc, foxType);
    }
    
    @Nonnull
    static AppearanceBuilder<AppearanceHusk> ofHusk() {
        return AppearanceHusk::new;
    }
    
    @Nonnull
    static AppearanceBuilder<AppearanceVillager> ofVillager(@Nonnull VillagerVariant variant, @Nonnull VillagerProfession profession, @Nonnull VillagerLevel level) {
        return npc -> new AppearanceVillager(npc, variant, profession, level);
    }
    
}
