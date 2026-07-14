package test;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.inventory.builder.ItemBuilder;
import me.hapyl.eterna.module.location.LocationHelper;
import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.npc.appearance.AppearanceBuilder;
import me.hapyl.eterna.module.player.dialog.Dialog;
import me.hapyl.eterna.module.player.dialog.entry.DialogEntry;
import me.hapyl.eterna.module.player.quest.*;
import me.hapyl.eterna.module.player.quest.objective.QuestObjectiveGiveKeyedItemToNpc;
import me.hapyl.eterna.module.player.quest.objective.QuestObjectiveJump;
import me.hapyl.eterna.module.player.quest.objective.QuestObjectiveTalkToNpc;
import me.hapyl.eterna.module.reflect.Skin;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.util.CollectionUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class EternaTestQuest extends EternaTest {
    
    private final QuestRegistry registry;
    private final Npc npc;
    private final Quest quest;
    
    private final ItemStack itemToGive;
    
    private @Nullable Set<QuestData> cachedQuestData;
    
    EternaTestQuest(@NotNull Key key) {
        super(key);
        
        this.registry = new TestQuestRegistry();
        this.npc = new Npc(
                LocationHelper.defaultLocation(-19, 70, -5),
                Component.text("Herobrine"),
                AppearanceBuilder.ofMannequin(Skin.of(
                        "ewogICJ0aW1lc3RhbXAiIDogMTczODEwMDAwNzExMCwKICAicHJvZmlsZUlkIiA6ICIxNzM1MGE5OWQ3MzQ0NDBjYTY0YzJjMDU3YTNjMWM4ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJHaWxkZWRoZXJvNTY5MSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS80MTczN2FhZTNhMmNiNDFkMzI4ZTdhYzliNjFkZjFhYzg0YWQ1M2I3NDBjZjQ4NTBjOGNjNGY0NGY0MGJiOGYiCiAgICB9CiAgfQp9",
                        "ROX1J4x56zzVAd1M34lRtxgmldowgIVCmcIARhaJa2+F6RSeEj5ujVu2aFmXjVRDaslTz5N5d+0xQxIMnAabwefwFxLyp6FBl/UrXg4yZczfdaZhqHeHXzgoKFhuK9ebpuABOehyRb0eRpO7YLoaC9QaS+EpzUA/kRvDPf/mTdJmQgoKIk0fDCCUNwlqpq0MP/59nF4qPPbQ0EMjzjXLNALXPSzJQRMKKMS0+oQwQE0vPlCbqj2vaZGtiM6GnAHKqQU5ZvloR7CnV0umcLuLuRYmE/agWLiK5LCQVTHC3XmG3FE+GRLttyIc0CqHeGjcbVkMZnu1mN71ZIeqneURpEIfltiYNZVBB8gaJUwu7jLVc9TvXCdsI1aHbW6NyDXl2qOckOogIi/29cf3+eHA/LZIcJM883ydGskcQVI1SEZtzjZ2ZI9vKHZf+vNIqWSaYk/gmU+xMXyWPMMKJRQgWmQ0Ug+NWLKJeurggR/H1d86ksYuSn3wk9GHIMRj9c3p5CQSk7J/D5UmbEB4wvuJZNKap2TC0yhdct6qgj9omAV5leQQ1BuyZKo7bPMaiM2pM9rQnZyScLmMqHjhtOvkpMoJ7Z+oJYhmfQUa4wt+e+g+Z73WTkEZDCJLXXGTP2Bvaol5GGi5yGr7uE6ONAbsJXKVmEM8urAPXJm5iM0bSj8="
                ))
        );
        final Key itemToGiveKey = Key.ofString("item_to_give_to_npc");
        
        this.itemToGive = new ItemBuilder(Material.STONE_SHOVEL, itemToGiveKey)
                .setName(Component.text("Magic Shovel"))
                .setEnchantmentGlintOverride(true)
                .build();
        
        this.quest = new Quest(
                registry,
                Key.ofString("test_quest_0"),
                List.of(
                        new QuestObjectiveJump(5),
                        new QuestObjectiveTalkToNpc(npc, new Dialog(Key.ofString("test_dialog_2"), Component.text("Test Dialog 2"))
                                .addEntry(DialogEntry.ofNpc(npc,
                                                            Component.text("Hello {player}, I'm the real {npc_name}!"),
                                                            Component.text("Send $300 paypal."),
                                                            Component.text("Here is proof ").append(Component.text("👁👁", NamedTextColor.WHITE))
                                ))),
                        new QuestObjectiveGiveKeyedItemToNpc(npc, itemToGiveKey, 1)
                )
        );
        
        this.quest.setName(Component.text("Test Quest"));
        this.quest.setDescription(Component.text("A very cool amazing wow super dooper quest!!!"));
        
        this.quest.setPreRequirement(QuestPreRequirement.of(player -> player.getName().equals("hapyl"), Component.text("You must be `hapyl`!")));
        this.quest.setStartBehaviour(QuestStartBehaviour.talkToNpc(npc, new Dialog(Key.ofString("test_dialog_1"), Component.text("Test Dialog 1"))
                .addEntry(DialogEntry.ofText(
                        Component.text("You start hearing voices inside your head."),
                        Component.text("You begin to wonder whether you're crazy."),
                        Component.text("You realize that it's just a test dialog.")
                ))));
    }
    
    @Override
    public void test(@NotNull EternaTestContext context) throws EternaTestException {
        final Player player = context.player();
        final PlayerInventory inventory = player.getInventory();
        
        inventory.clear();
        inventory.addItem(itemToGive);
        
        npc.show(player);
    }
    
    private class TestQuestRegistry extends QuestRegistry {
        public TestQuestRegistry() {
            super(Eterna.getPlugin());
        }
        
        @Override
        public void save(@NotNull Player player, @NotNull Set<QuestData> questData) {
            cachedQuestData = questData;
            
            EternaLogger.debug("Saved quest data: " + CollectionUtils.wrapToString(questData));
        }
        
        @NotNull
        @Override
        public Set<QuestData> load(@NotNull Player player) {
            if (cachedQuestData != null) {
                return cachedQuestData.stream()
                                      // Explicitly call QuestData.load()
                                      .map(questData -> QuestData.load(
                                              player,
                                              questData.getQuest(),
                                              questData.getCurrentStage(),
                                              questData.getCurrentStageProgress(),
                                              questData.getStartedAt(),
                                              questData.getCompletedAt())
                                      )
                                      .collect(Collectors.toSet());
            }
            
            return Set.of();
        }
    }
}