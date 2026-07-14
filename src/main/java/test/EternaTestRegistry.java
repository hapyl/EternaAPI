package test;

import com.google.common.collect.Maps;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.npc.appearance.AppearanceBuilder;
import me.hapyl.eterna.module.reflect.Skin;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.scheduler.SchedulerTask;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@ApiStatus.Internal
public final class EternaTestRegistry {
    
    private static final Map<Key, EternaTest> REGISTRY;
    
    static {
        EternaLogger.info("Tests instantiating...");
        final long startMillis = System.currentTimeMillis();
        
        REGISTRY = Maps.newLinkedHashMap();
        
        // *-* Start Tests *-* //
        
        register("glowing", EternaTestGlowing::new);
        register("hologram", EternaTestHologram::new);
        register("item_builder", EternaTestItemBuilder::new);
        register("player_menu", EternaTestPlayerMenu::new);
        register("player_menu_page", EternaTestPlayerMenuPage::new);
        register("sign_input", EternaTestSignInput::new);
        register("tablist", EternaTestTablist::new);
        register("outline", EternaTestOutline::new);
        register("bd_engine", EternaTestBdEngine::new);
        register("bd_engine_rotation", EternaTestBdEngineRotation::new);
        register("particle_builder", EternaTestParticleBuilder::new);
        register("player_skin", EternaTestPlayerSkin::new);
        register("item_components", EternaTestItemComponents::new);
        register("quest", EternaTestQuest::new);
        register("packet_entity", EternaTestPacketEntity::new);
        register("dialog", EternaTestDialog::new);
        register("parkour", EternaTestParkour::new);
        register("scoreboard", EternaTestScoreboard::new);
        register("npc", EternaTestNpc::new);
        register("sequencer", EternaTestSequencer::new);
        
        EternaLogger.info("Tests successfully instantiated! (%sms)".formatted(System.currentTimeMillis() - startMillis));
    }
    
    @ApiStatus.Internal
    private EternaTestRegistry() {
    }
    
    @Nullable
    @ApiStatus.Internal
    public static EternaTest get(@NotNull Key key) {
        return REGISTRY.get(key);
    }
    
    @NotNull
    public static List<String> listTests() {
        return REGISTRY.keySet().stream().map(Key::getKey).toList();
    }
    
    @ApiStatus.Internal
    static void register(@NotNull String key, @NotNull Function<Key, EternaTest> constructor) {
        if (key.toLowerCase().contains("test")) {
            throw new IllegalArgumentException("Test key cannot contain `test`!");
        }
        
        final Key testKey = Key.ofString(key);
        
        if (REGISTRY.containsKey(testKey)) {
            throw new IllegalArgumentException("A test with id %s is already registered!".formatted(key));
        }
        
        REGISTRY.put(testKey, constructor.apply(testKey));
    }
    
}