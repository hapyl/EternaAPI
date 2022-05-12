package kz.hapyl.spigotutils.module.inventory;

import kz.hapyl.spigotutils.module.annotate.AsyncWarning;
import kz.hapyl.spigotutils.module.reflect.ReflectPacket;
import kz.hapyl.spigotutils.module.util.Runnables;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.protocol.game.PacketPlayOutOpenSignEditor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Opens a SIGN that can be used as input.
 */
public abstract class SignGUI {
    public static final Map<Player, SignGUI> saved = new HashMap<>();
    private final Player player;
    private final String[] lines;
    private final Location location;

    public SignGUI(Player player, String prompt) {
        this.player = player;
        final Location clone = player.getLocation().clone();
        clone.setY(-64);
        this.location = clone;
        this.lines = new String[] { "", "", "", "" };

        final List<String> splits = ItemBuilder.splitString(prompt, 14);

        if (splits.size() == 0 || prompt.isBlank()) {
            this.lines[3] = "^^^^^^^^^^^^^^";
        }
        else if (splits.size() == 1) {
            this.lines[2] = "^^^^^^^^^^^^^^";
            this.lines[3] = splits.get(0);
        }
        else {
            this.lines[1] = "^^^^^^^^^^^^^^";
            this.lines[2] = splits.get(0);
            final String lastLine = splits.get(1);
            this.lines[3] = (splits.size() > 2) ? (lastLine.substring(0, lastLine.length() - 3) + "...") : lastLine;
        }

    }

    public void openMenu() {
        createPacketAndSend();
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

    /**
     * @param player   - Player.
     * @param response - Four lines of a sign, including ^^^^^^^^^^^^^^ and prompt.
     *                 Use {@link this#onResponse(Player, String)} for player's response excluding prompt.
     */
    @AsyncWarning
    public abstract void onResponse(Player player, String[] response);

    public void onResponse(Player player, String string) {

    }

    public String concatString(String[] array) {
        final StringBuilder builder = new StringBuilder();
        for (String str : array) {
            if (str.contains("^^^^^^^^^^^^^^")) {
                break;
            }

            builder.append(str).append(" ");
        }
        return builder.toString().trim();
    }

    protected void runSync(Runnable runnable) {
        Runnables.runSync(runnable);
    }

    protected String getResponseValue(String[] response, int index) {
        return index >= response.length ? "empty" : response[index];
    }

    private void createPacketAndSend() {
        player.sendBlockChange(location, Material.OAK_SIGN.createBlockData());
        player.sendSignChange(location, this.lines);
        new ReflectPacket(new PacketPlayOutOpenSignEditor(new BlockPosition(
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ()
        ))).sendPackets(player);
        saved.put(player, this);
    }

}
