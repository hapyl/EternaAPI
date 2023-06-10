package me.hapyl.spigotutils.module.inventory;

import me.hapyl.spigotutils.module.annotate.ArraySize;
import me.hapyl.spigotutils.module.annotate.AsyncNotSafe;
import me.hapyl.spigotutils.module.annotate.TestedNMS;
import me.hapyl.spigotutils.module.annotate.Version;
import me.hapyl.spigotutils.module.reflect.Reflect;
import me.hapyl.spigotutils.module.reflect.ReflectPacket;
import me.hapyl.spigotutils.module.util.Runnables;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutOpenSignEditor;
import net.minecraft.world.item.EnumColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.entity.TileEntitySign;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Opens a SIGN that can be used as input.
 */
@TestedNMS(version = Version.V1_20)
public abstract class SignGUI {

    public static final String DASHED_LINE = "^^^^^^^^^^^^^^";
    public static boolean IS_SPIGOT_SEND_SIGN_CHANGE_BROKEN = true;

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
            if (splits.size() == 0 || prompt.isBlank()) {
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
    public SignGUI(Player player, @Nonnull @ArraySize(max = 4) String... prompt) {
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
    public SignGUI(@Nonnull Player player, @Nonnull SignType type, @Nonnull @ArraySize(max = 4) String... prompt) {
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

    @Deprecated
    public void openMenu() {
        final RuntimeException exception = new RuntimeException("Deprecated 'openMenu()' call on SignGUI.");
        final Logger logger = Bukkit.getLogger();

        logger.warning("Deprecated call 'openMenu()'!");
        logger.warning(Arrays.toString(exception.getStackTrace()));
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

    private void createPacketAndSend() {
        player.sendBlockChange(location, type.material.createBlockData());

        if (IS_SPIGOT_SEND_SIGN_CHANGE_BROKEN) {
            createSignRaw();
        }
        else {
            player.sendSignChange(location, this.lines);
        }

        // Open sign editor
        ReflectPacket.send(new PacketPlayOutOpenSignEditor(getBlockPosition(), true), player);

        saved.put(player, this);
    }

    private BlockPosition getBlockPosition() {
        return new BlockPosition(
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ()
        );
    }

    // Spigot broke sendSignChange, using this ugly code for now
    private void createSignRaw() {
        final TileEntitySign sign = new TileEntitySign(getBlockPosition(), Blocks.cE.n());

        sign.a(new SignText(
                new IChatBaseComponent[] {
                        IChatBaseComponent.a(lines[0]),
                        IChatBaseComponent.a(lines[1]),
                        IChatBaseComponent.a(lines[2]),
                        IChatBaseComponent.a(lines[3])
                },
                new IChatBaseComponent[] { CommonComponents.a, CommonComponents.a, CommonComponents.a, CommonComponents.a },
                EnumColor.p, false
        ), true);

        Reflect.sendPacket(player, sign.j());
    }

    public static Map<Player, SignGUI> getMap() {
        return saved;
    }

    public static void remove(Player player) {
        saved.remove(player);
    }

}
