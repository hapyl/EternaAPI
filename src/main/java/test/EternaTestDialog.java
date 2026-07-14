package test;

import me.hapyl.eterna.module.command.ArgumentList;
import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.npc.appearance.AppearanceBuilder;
import me.hapyl.eterna.module.npc.appearance.SheepColor;
import me.hapyl.eterna.module.player.dialog.Dialog;
import me.hapyl.eterna.module.player.dialog.entry.DialogEntry;
import me.hapyl.eterna.module.player.dialog.entry.DialogEntryOptions;
import me.hapyl.eterna.module.player.dialog.entry.DialogEntryText;
import me.hapyl.eterna.module.player.dialog.entry.OptionIndex;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.util.StringList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class EternaTestDialog extends EternaTest {
    
    private Npc npc;
    
    EternaTestDialog(@NotNull Key key) {
        super(key);
    }
    
    @Override
    public void test(@NotNull EternaTestContext context) throws EternaTestException {
        final Npc npc = createNpc(context);
        final Player player = context.player();
        
        if (context.argument(0).toString().equalsIgnoreCase("skip")) {
            Dialog.getCurrentDialog(player).ifPresentOrElse(
                    instance -> {
                        instance.skip();
                        context.assertTestPassed();
                    },
                    () -> {
                        context.assertTestFailed("There aren't any dialogs to skip!");
                    }
            );
        }
        else {
            new Dialog(Key.ofString("test_dialog"), Component.text("Test Dialog")) {
                @Override
                public int getEntryDelay(@NotNull Player player, @NotNull DialogEntry entry) {
                    return 20;
                }
            }
                    .addEntry(DialogEntry.ofText(
                            Component.text("Hello there, stranger!"),
                            Component.text("This is a test dialog, that is working as intended!"),
                            Component.text("Why? Because I rewrote dialogs to use components!"),
                            Component.text("Yes, and it works perfectly!")
                    ))
                    .addEntry(
                            DialogEntry.ofNpc(npc,
                                              Component.text("Oh yes I'm the npc!"),
                                              Component.text("And I'm talking to you...")
                            )
                    )
                    .addEntry(
                            DialogEntry.ofSelectableOptions(DialogEntry.ofText(Component.text("Do you like how the system works?")))
                                       .setOption(
                                               OptionIndex.OPTION_1,
                                               DialogEntryOptions.builder(Component.text("It's nice!"))
                                                                 .append(DialogEntry.ofText(
                                                                         Component.text("Oh stop it!"),
                                                                         Component.text("I know it's nice but don't flatter me!")
                                                                 ))
                                       )
                                       .setOption(
                                               OptionIndex.OPTION_2,
                                               DialogEntryOptions.builder(Component.text("No, it sucks."))
                                                                 .append(DialogEntry.ofText(
                                                                         Component.text("Damn.")
                                                                 ))
                                       )
                                       .setOption(
                                               OptionIndex.OPTION_3,
                                               DialogEntryOptions.builder(Component.text("It's okay I guess..."))
                                                                 .append(DialogEntry.ofText(
                                                                         Component.text("Your guess is correct!"),
                                                                         Component.text("It's indeed... okay.'")
                                                                 ))
                                       )
                                       .setOption(
                                               OptionIndex.OPTION_4, DialogEntryOptions.goodbye(Component.text("Leave..."))
                                       )
                    )
                    .addEntry(
                            new DialogEntryText(
                                    Key.ofString("explaining"),
                                    Component.text("So you need to do this and that and this and that and this and that.", NamedTextColor.GREEN)
                            )
                    )
                    .addEntry(
                            DialogEntry.ofSelectableOptions(DialogEntry.ofText(Component.text("Got all that?")))
                                       .setOption(
                                               OptionIndex.OPTION_1,
                                               DialogEntryOptions.builder(Component.text("Got it!")).advanceDialog(true)
                                       )
                                       .setOption(
                                               OptionIndex.OPTION_2,
                                               DialogEntryOptions.builder(Component.text("Can you repeat that?"))
                                                                 .append(DialogEntry.ofEntry(instance -> instance.jumpToEntry(Key.ofString("explaining"))))
                                       )
                    )
                    .addEntry(
                            DialogEntry.ofText(Component.text("All right then, goodbye!"))
                    )
                    .addEntry(
                            DialogEntry.ofEntry(instance -> context.assertTestPassed())
                    )
                    .setSummary(Component.text("Nothing important, just some useless dialog..."))
                    .start(player);
        }
    }
    
    private @NotNull Npc createNpc(@NotNull EternaTestContext context) {
        if (npc == null) {
            npc = new Npc(context.player().getLocation(), Component.text("My Name is Jeff"), AppearanceBuilder.ofSheep(SheepColor.RED));
            npc.showAll();
        }
        
        return npc;
    }
    
    @Override
    public @NotNull StringList tabComplete(@NotNull ArgumentList args) {
        return StringList.of("skip");
    }
}