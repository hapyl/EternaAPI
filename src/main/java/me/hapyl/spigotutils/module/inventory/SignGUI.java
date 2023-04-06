package me.hapyl.spigotutils.module.inventory;

import me.hapyl.spigotutils.module.annotate.ArraySize;
import me.hapyl.spigotutils.module.annotate.AsyncNotSafe;
import me.hapyl.spigotutils.module.annotate.TestedNMS;
import me.hapyl.spigotutils.module.reflect.ReflectPacket;
import me.hapyl.spigotutils.module.util.Runnables;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
@TestedNMS(version = "1.19.4")
public abstract class SignGUI {

    public static final String DASHED_LINE = "^^^^^^^^^^^^^^";

    private static final Map<Player, SignGUI> saved = new HashMap<>();

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
    public SignGUI(Player player, @Nullable String prompt) {
        this.player = player;
        final Location clone = player.getLocation().clone();
        clone.setY(-64);
        this.location = clone;
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
        this(player, (String) null);
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

    public String[] getLines() {
        return lines;
    }

    public Location getLocation() {
        return location;
    }

    public Player getPlayer() {
        return player;
    }

    public void clearSign() {
        player.sendBlockChange(location, location.getBlock().getBlockData());
    }

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

    private boolean isLine(String str) {
        for (String line : lines) {
            if (!line.isBlank() && !line.isEmpty() && line.equals(str)) {
                return true;
            }
        }
        return false;
    }

    protected void runSync(Runnable runnable) {
        Runnables.runSync(runnable);
    }

    private void createPacketAndSend() {
        player.sendBlockChange(location, Material.OAK_SIGN.createBlockData());
        player.sendSignChange(location, this.lines);
        ReflectPacket.send(new ClientboundOpenSignEditorPacket(new BlockPos(
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ()
        )), player);
        saved.put(player, this);
    }

    public static Map<Player, SignGUI> getMap() {
        return saved;
    }

    public static void remove(Player player) {
        saved.remove(player);
    }

}
