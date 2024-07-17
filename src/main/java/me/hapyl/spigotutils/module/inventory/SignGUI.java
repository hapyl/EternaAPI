package me.hapyl.spigotutils.module.inventory;

import me.hapyl.spigotutils.module.annotate.*;
import me.hapyl.spigotutils.module.reflect.Reflect;
import me.hapyl.spigotutils.module.util.Runnables;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Opens a SIGN that can be used as input.
 */
@TestedOn(version = Version.V1_21)
public abstract class SignGUI {

    public static final String DASHED_LINE = "^^^^^^^^^^^^^^";

    private static final Map<Player, SignGUI> saved = new HashMap<>();

    private final SignType type;
    private final Player player;
    private final String[] lines;
    private final Location location;

    /**
     * Creates a new SignGUI.
     *
     * @param player - Player for this gui to.
     * @param prompt - Prompt of the sign.
     *               <b>Input and prompt is separated by line of '^' characters.</b>
     *               <b>Prompt cannot be longer than 28 characters or it will be appended by '...'</b>
     */
    public SignGUI(@Nonnull Player player, @Nullable String prompt) {
        this(player, SignType.OAK, prompt);
    }

    /**
     * Create a new SignGUI.
     *
     * @param player - Player
     * @param type   - Type of the sign.
     * @param prompt - Prompt of the sign.
     *               <b>Input and prompt is separated by line of '^' characters.</b>
     *               <b>Prompt cannot be longer than 28 characters or it will be appended by '...'</b>
     */
    public SignGUI(@Nonnull Player player, @Nonnull SignType type, @Nullable String prompt) {
        this.player = player;
        this.type = type;
        this.location = player.getLocation();
        this.location.setY(Math.max(-64, location.getY() - 5)); // the sign now closes when too far away, thanks mojang
        this.lines = new String[] { "", "", "", "" };

        if (prompt != null) {
            final List<String> splits = ItemBuilder.splitString(prompt, 14);
            if (splits.isEmpty() || prompt.isBlank()) {
                this.lines[3] = DASHED_LINE;
            }
            else if (splits.size() == 1) {
                this.lines[2] = DASHED_LINE;
                this.lines[3] = splits.get(0);
            }
            else {
                final String lastLine = splits.get(1);
                this.lines[1] = DASHED_LINE;
                this.lines[2] = splits.get(0);
                this.lines[3] = (splits.size() > 2) ? (lastLine.substring(0, lastLine.length() - 3) + "...") : lastLine;
            }
        }

        Runnables.runLater(this::createPacketAndSend, 1L);
    }

    /**
     * Creates a new SignGUI.
     *
     * @param player - Player to
     * @param prompt - Up to 4 lines of prompt text.
     *               <b>This method will NOT include '^' character.</b>
     * @throws IllegalArgumentException if prompt lines are longer than 4 lines.
     */
    public SignGUI(Player player, @Nonnull @Range(max = 4) String... prompt) {
        this(player, SignType.OAK, prompt);
    }

    /**
     * Creates a new SignGUI.
     *
     * @param player - Player to
     * @param type   - Type of this sign.
     * @param prompt - Up to 4 lines of prompt text.
     *               <b>This method will NOT include '^' character.</b>
     * @throws IllegalArgumentException if prompt lines are longer than 4 lines.
     */
    public SignGUI(@Nonnull Player player, @Nonnull SignType type, @Nonnull @Range(max = 4) String... prompt) {
        this(player, type, (String) null);
        if (prompt.length > 4) {
            throw new IllegalArgumentException("Prompt cannot be longer than 4 lines!");
        }

        switch (prompt.length) {
            case 1 -> this.lines[3] = prompt[0];
            case 2 -> {
                this.lines[2] = prompt[0];
                this.lines[3] = prompt[1];
            }
            case 3 -> {
                this.lines[1] = prompt[0];
                this.lines[2] = prompt[1];
                this.lines[3] = prompt[2];
            }
            case 4 -> System.arraycopy(prompt, 0, this.lines, 0, prompt.length);
        }
    }

    /**
     * Gets lines for this sign.
     *
     * @return the lines for this sign.
     */
    public String[] getLines() {
        return lines;
    }

    /**
     * Gets the location of this sign. The location is calculated to be 5 blocks below the player or -64 at max.
     *
     * @return the location of this sign.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Gets the player for whom this sign will be opened.
     *
     * @return the player for this sign.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Clears the fake sign player for the player.
     */
    public void clearSign() {
        player.sendBlockChange(location, location.getBlock().getBlockData());
    }

    /**
     * Response to execute after player closes this sign.
     * <h1>THIS IS NOT SYNC SAVE</h1>
     * Use {@link Response#runSync(Runnable)} to synchronize the calls.
     *
     * @param response - Response.
     */
    @AsyncNotSafe
    public abstract void onResponse(Response response);

    /**
     * Concat strings from output without the input query.
     *
     * @param array - Output array.
     * @return formatted string.
     */
    public String concatString(String[] array) {
        final StringBuilder builder = new StringBuilder();
        for (String str : array) {
            if (isLine(str)) {
                break;
            }

            builder.append(str.trim()).append(" ");
        }
        return builder.toString().trim();
    }

    protected void runSync(Runnable runnable) {
        Runnables.runSync(runnable);
    }

    private boolean isLine(String str) {
        for (String line : lines) {
            if (!line.isBlank() && !line.isEmpty() && line.equals(str)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    private void createPacketAndSend() {
        player.sendBlockChange(location, type.material.createBlockData());
        player.sendSignChange(location, lines);

        // Open sign editor
        Reflect.sendPacket(player, new ClientboundOpenSignEditorPacket(getBlockPosition(), true));

        saved.put(player, this);
    }

    private BlockPos getBlockPosition() {
        return new BlockPos(
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ()
        );
    }

    private void createSignRaw() {
        final SignBlockEntity sign = new SignBlockEntity(getBlockPosition(), Blocks.OAK_SIGN.defaultBlockState());

        sign.setText(new SignText(
                new Component[] {
                        Component.literal(lines[0]),
                        Component.literal(lines[1]),
                        Component.literal(lines[2]),
                        Component.literal(lines[3])
                },
                new Component[] { Component.empty(), Component.empty(), Component.empty(), Component.empty() },
                DyeColor.BLACK, false
        ), true);

        Reflect.sendPacket(player, sign.getUpdatePacket());
    }

    public static Map<Player, SignGUI> getMap() {
        return saved;
    }

    public static void remove(Player player) {
        saved.remove(player);
    }

}
