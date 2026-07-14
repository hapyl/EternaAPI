package test;

import me.hapyl.eterna.Runnables;
import me.hapyl.eterna.module.command.ArgumentList;
import me.hapyl.eterna.module.inventory.Equipment;
import me.hapyl.eterna.module.npc.*;
import me.hapyl.eterna.module.npc.appearance.*;
import me.hapyl.eterna.module.npc.tag.TagLayout;
import me.hapyl.eterna.module.npc.tag.TagPart;
import me.hapyl.eterna.module.reflect.Skin;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.scheduler.SchedulerTask;
import me.hapyl.eterna.module.util.ListMaker;
import me.hapyl.eterna.module.util.StringList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class EternaTestNpc extends EternaTest {
    
    private final Map<String, Function<EternaTestContext, AppearanceBuilder<? extends Appearance>>> appearanceBuilders = Map.of(
            "player", context -> AppearanceBuilder.ofMannequin(Skin.ofPlayer(context.player())),
            "sheep", _ -> AppearanceBuilder.ofSheep(SheepColor.RED),
            "fox", _ -> AppearanceBuilder.ofFox(FoxType.RED),
            "husk", _ -> AppearanceBuilder.ofHusk(),
            "villager", _ -> AppearanceBuilder.ofVillager(VillagerVariant.PLAINS, VillagerProfession.LEATHERWORKER, VillagerLevel.NOVICE)
    );
    
    EternaTestNpc(@NotNull Key key) {
        super(key);
        
        // Register custom placeholders
        NpcPlaceholder.register("health", (_npc, _player) -> Component.text("%.1f ❤".formatted(_player.getHealth()), NamedTextColor.RED));
        NpcPlaceholder.register(
                "held_item", (_npc, _player) -> {
                    final ItemStack itemInMainHand = _player.getInventory().getItemInMainHand();
                    
                    return itemInMainHand.isEmpty() ? Component.text("Nothing!", NamedTextColor.AQUA) : itemInMainHand.displayName().color(NamedTextColor.AQUA);
                }
        );
    }
    
    @Override
    public void test(@NotNull EternaTestContext context) throws EternaTestException {
        final Player player = context.player();
        final String appearanceType = context.argument(0).toString().toLowerCase();
        
        final Function<EternaTestContext, AppearanceBuilder<? extends Appearance>> builder = appearanceBuilders.get(appearanceType);
        
        if (builder == null) {
            context.assertTestFailed("Invalid argument `type`: `%s` isn't a valid type!".formatted(appearanceType));
            return;
        }
        
        final Npc npc = new Npc(player.getLocation(), Component.text("Emilie"), builder.apply(context)) {
            @Override
            public void tick() {
                super.tick();
                this.updateHologram();
            }
            
            @Override
            public void onClick(@NotNull Player player, @NotNull ClickType clickType) {
                sendMessage(player, Component.text("Thank you for clicking me, {player}.", NamedTextColor.AQUA));
                sendMessage(player, Component.text("My name is {npc_name}.", NamedTextColor.GOLD));
                sendMessage(player, Component.text("Your health is: {health}"));
                sendMessage(player, Component.text("You're holding: {held_item}"));
            }
        };
        
        npc.setTagLayout(new TagLayout(
                TagPart.literal(Component.text("First Line", NamedTextColor.YELLOW)),
                TagPart.of((_npc, _player) -> Component.text("Hello %s!".formatted(_player.getName()), NamedTextColor.AQUA)),
                TagPart.literal(Component.text("-------------------", NamedTextColor.GRAY, TextDecoration.STRIKETHROUGH)),
                TagPart.name(),
                TagPart.literal(Component.text("-------------------", NamedTextColor.GRAY, TextDecoration.STRIKETHROUGH)),
                TagPart.literal(Component.text("Hello World!", NamedTextColor.BLUE)),
                TagPart.linebreak(),
                TagPart.of((_npc, _player) -> {
                    if (_player.isSneaking()) {
                        return Component.text("You're sneaking!", NamedTextColor.YELLOW);
                    }
                    
                    return null;
                })
        ));
        
        final NpcProperties properties = npc.getProperties();
        
        properties.setViewDistance(20);
        properties.setLookAtClosePlayerDistance(5);
        properties.setCollidable(false);
        properties.setRestPosition(new RestHeadPosition(100.0f, -25.0f));
        
        npc.showAll();
        
        context.info(Component.text("Created `%s` npc".formatted(appearanceType)));
        context.scheduler()
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("Sitting: ").append(Component.text("true", NamedTextColor.GREEN)));
                   npc.setSitting(true);
               }, 40))
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("Sitting: ").append(Component.text("false", NamedTextColor.RED)));
                   npc.setSitting(false);
               }, 40))
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("Shaking: ").append(Component.text("true", NamedTextColor.GREEN)));
                   npc.setShaking(true);
               }, 40))
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("Shaking: ").append(Component.text("false", NamedTextColor.RED)));
                   npc.setShaking(false);
               }, 40))
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("Invisible: ").append(Component.text("true", NamedTextColor.GREEN)));
                   npc.setInvisible(true);
               }, 40))
               .then(SchedulerTask.later(() -> {
                   context.info(Component.text("Invisible: ").append(Component.text("false", NamedTextColor.RED)));
                   npc.setInvisible(false);
               }, 40))
                .then(SchedulerTask.later(() -> {
                    final double scale = 0.5;
                    
                    context.info(Component.text("Set scale to %.1f".formatted(scale)));
                    
                    npc.setAttribute(Attributes.SCALE, scale);
                    npc.updateAttributes();
                }, 40))
               .then(SchedulerTask.await(await -> {
                   int delay = 0;
                   
                   for (NpcAnimation npcAnimation : NpcAnimation.values()) {
                       Runnables.later(() -> {
                           context.info(Component.text("Animation: ").append(Component.text(npcAnimation.name(), NamedTextColor.GREEN)));
                           
                           npc.playAnimation(npcAnimation);
                       }, delay += 10);
                   }
                   
                   Runnables.later(() -> await.complete(null), delay + 10);
               }))
               .then(SchedulerTask.await(await -> {
                   final List<NpcPose> npcPoses
                           = ListMaker.<NpcPose>ofList()
                                      .addAll(NpcPose.values())
                                      // Move SWIMMING -> STANDING around to validate that mojang
                                      // unfucked the poses
                                      .putLast(NpcPose.SWIMMING)
                                      .putLast(NpcPose.STANDING)
                                      .makeList();
                   
                   int delay = 0;
                   
                   for (NpcPose pose : npcPoses) {
                       Runnables.later(() -> {
                           context.info(Component.text("Pose: ").append(Component.text(pose.name(), NamedTextColor.LIGHT_PURPLE)));
                           
                           npc.setPose(pose);
                       }, delay += 10);
                   }
                   
                   Runnables.later(() -> await.complete(null), delay + 10);
               }))
               .then(SchedulerTask.later(() -> {
                   if (npc.getAppearance() instanceof AppearanceHumanoid appearanceHumanoid) {
                       context.info(Component.text("Set equipment"));
                       
                       appearanceHumanoid.setEquipment(
                               Equipment.builder()
                                        .helmet(Material.DIAMOND_HELMET)
                                        .chestPlate(Material.GOLDEN_CHESTPLATE)
                                        .leggings(Material.COPPER_LEGGINGS)
                                        .boots(Material.NETHERITE_BOOTS)
                                        .mainHand(Material.BREEZE_ROD)
                                        .offHand(Material.BEDROCK)
                                        .body(Material.SADDLE)
                                        .build()
                       );
                   }
                   else {
                       context.warning(Component.text("Could not test equipment because appearance isn't a humanoid!"));
                   }
               }, 20))
               .then(SchedulerTask.later(() -> {
                   npc.dispose();
                   context.assertTestPassed();
               }, 40))
               .execute();
    }
    
    @Override
    public @NotNull StringList tabComplete(@NotNull ArgumentList args) {
        return StringList.of(appearanceBuilders.keySet());
    }
    
}