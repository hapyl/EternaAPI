package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.player.dialog.Dialog;
import me.hapyl.eterna.module.player.dialog.DialogEndType;
import me.hapyl.eterna.module.player.quest.QuestData;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * A {@link QuestObjective} for the completion of which the {@link Player} must <b><u>complete</u></b> a {@link Dialog}.
 *
 * @see QuestObjectiveTalkToNpc
 */
public class QuestObjectiveCompleteDialog extends QuestObjective {
    
    /**
     * Defines the default {@link DialogEndType} that advance the objective.
     */
    public static final Set<DialogEndType> DEFAULT_ALLOWED_END_TYPES = Set.of(DialogEndType.COMPLETED, DialogEndType.SKIPPED);
    
    private final Dialog dialog;
    private final Set<DialogEndType> allowedEndTypes;
    
    /**
     * Creates a new objective {@link QuestObjectiveCompleteDialog}.
     *
     * @param dialog          - The dialog to complete.
     * @param allowedEndTypes - The allowed end types that advance this objective, or {@code null} to allow all.
     */
    public QuestObjectiveCompleteDialog(@NotNull Dialog dialog, @Nullable DialogEndType... allowedEndTypes) {
        this(
                Component.empty()
                         .append(Component.text("Complete `"))
                         .append(dialog.getName())
                         .append(Component.text("` dialog.")),
                dialog,
                allowedEndTypes
        );
    }
    
    @ApiStatus.Internal
    QuestObjectiveCompleteDialog(@NotNull Component description, @NotNull Dialog dialog, @Nullable DialogEndType... allowedEndTypes) {
        super(description, 1);
        
        this.dialog = dialog;
        this.allowedEndTypes = (allowedEndTypes != null && allowedEndTypes.length != 0) ? Set.of(allowedEndTypes) : DEFAULT_ALLOWED_END_TYPES;
    }
    
    /**
     * Gets the {@link Dialog} to complete.
     *
     * @return the dialog to complete.
     */
    @NotNull
    public Dialog getDialog() {
        return dialog;
    }
    
    /**
     * Gets an <b>immutable</b> {@link Set} containing the allowed {@link DialogEndType} that advance this objective.
     *
     * @return an <b>immutable</b> set containing the allowed dialog end types that advance this objective.
     */
    @NotNull
    public Set<DialogEndType> getAllowedEndTypes() {
        return allowedEndTypes;
    }
    
    /**
     * Gets whether the given {@link DialogEndType} is allowed to advance this {@link QuestObjectiveCompleteDialog}.
     *
     * @param dialogEndType - The dialog end type to check.
     * @return {@code true} if the given dialog end type is allowed to advance this objective; {@code false} otherwise.
     */
    public boolean isAllowedEndType(@NotNull DialogEndType dialogEndType) {
        return allowedEndTypes.contains(dialogEndType);
    }
    
    @NotNull
    @Override
    public Response test(@NotNull QuestData questData, @NotNull QuestObjectArray array) {
        final Dialog dialog = array.get(0, Dialog.class);
        final DialogEndType dialogEndType = array.get(1, DialogEndType.class);
        
        if (dialog == null || dialogEndType == null) {
            return Response.testFailed();
        }
        
        return Response.ofBoolean(this.dialog.equals(dialog) && allowedEndTypes.contains(dialogEndType));
    }
    
}
