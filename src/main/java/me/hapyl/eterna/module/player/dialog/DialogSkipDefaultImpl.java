package me.hapyl.eterna.module.player.dialog;

import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import me.hapyl.eterna.EternaColors;
import me.hapyl.eterna.module.inventory.menu.PlayerMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a {@link DialogSkip} implementation in a {@link PlayerMenu} format that displays the {@link Dialog} summary and awaits {@link Player} confirmation.
 */
@ApiStatus.Internal
@SuppressWarnings("UnstableApiUsage")
public final class DialogSkipDefaultImpl implements DialogSkip {
    
    private final Dialog dialog;
    private final CompletableFuture<Boolean> future;
    
    DialogSkipDefaultImpl(@NotNull Dialog dialog) {
        this.dialog = dialog;
        this.future = new CompletableFuture<>();
    }
    
    @NotNull
    @Override
    public CompletableFuture<Boolean> prompt(@NotNull Player player) {
        player.showDialog(
                io.papermc.paper.dialog.Dialog.create(builder -> {
                    builder.empty()
                           .base(makeBase())
                           .type(DialogType.confirmation(
                                   ActionButton.builder(Component.text("Confirm", EternaColors.GREEN))
                                               .tooltip(Component.text("Click to skip the dialog!", EternaColors.GRAY))
                                               .action(DialogAction.customClick(
                                                       (response, audience) -> future.complete(true),
                                                       ClickCallback.Options.builder()
                                                                            .uses(1)
                                                                            .lifetime(ClickCallback.DEFAULT_LIFETIME)
                                                                            .build()
                                               ))
                                               .build(),
                                   ActionButton.builder(Component.text("Cancel", EternaColors.RED))
                                               .tooltip(Component.text("Click to resume the dialog!", EternaColors.GRAY))
                                               .action(DialogAction.customClick(
                                                       (response, audience) -> future.complete(false),
                                                       ClickCallback.Options.builder()
                                                                            .uses(1)
                                                                            .lifetime(ClickCallback.DEFAULT_LIFETIME)
                                                                            .build()
                                               ))
                                               .build()
                           ));
                })
        );
        
        return future;
    }
    
    @NotNull
    private DialogBase makeBase() {
        final Component summary = dialog.getSummary();
        final List<DialogBody> dialogBody;
        
        if (summary == null) {
            dialogBody = List.of();
        }
        else {
            dialogBody = List.of(
                    DialogBody.plainMessage(Component.text("Summary", EternaColors.GRAY)),
                    DialogBody.plainMessage(Component.empty()),
                    DialogBody.plainMessage(summary)
            );
        }
        
        return DialogBase.builder(Component.text("Skip Dialog?"))
                         .body(dialogBody)
                         .build();
    }
    
}
