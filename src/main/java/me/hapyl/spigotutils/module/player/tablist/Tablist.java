package me.hapyl.spigotutils.module.player.tablist;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

// TODO: 0003, Oct 3 2022 -> Not Finished yet
public class Tablist {

    private static final ProtocolManager PROTOCOL = ProtocolLibrary.getProtocolManager();

    private final Map<Integer, Entry> entries;
    private final Set<Player> viewers;

    public Tablist() {
        this.viewers = Sets.newHashSet();
        this.entries = Maps.newLinkedHashMap();

        for (int i = 0; i < 80; i++) {
            this.entries.put(i, new Entry(i));
        }
    }

    @Nonnull
    public Entry getEntry(int index) {
        if (index >= this.entries.size()) {
            throw new IndexOutOfBoundsException("%s >= %s".formatted(index, this.entries.size()));
        }

        return this.entries.get(index);
    }

    public Entry getEntry(int column, int line) {
        return getEntry(line + (column * 20));
    }

    public void setEntry(int index, String line, PingBars ping, String textureValue, String textureSignature) {
        getEntry(index).setText(line).setPing(ping).setTexture(textureValue, textureSignature);
        updateLines();
    }

    public void setEntry(int column, int line, String text) {
        getEntry(column, line).setText(text);
        updateLines();
    }

    public void setEntry(int index, String line, PingBars ping) {
        setEntry(index, line, ping, null, null);
    }

    public void setEntry(int index, String line) {
        setEntry(index, line, null);
    }

    public void show(Player player) {
        if (viewers.contains(player)) {
            return;
        }

        viewers.add(player);
        hideRealPlayers();
        createLines();
        showRealPlayers();
    }

    public void hide(Player player) {
        fetchEntries(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
        viewers.remove(player);
    }

    public void destroy() {
        viewers.forEach(this::hide);
        entries.clear();
    }

    private void createLines() {
        fetchPacket(
                data -> entries.forEach((index, entry) -> data.add(entry.createPlayerInfoData())),
                EnumWrappers.PlayerInfoAction.ADD_PLAYER
        );
    }

    public void updateLines() {
        fetchEntries(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
        fetchEntries(EnumWrappers.PlayerInfoAction.ADD_PLAYER);
    }

    private void hideRealPlayers() {
        fetchPacket(data -> Bukkit.getOnlinePlayers().forEach(player -> {
            data.add(new PlayerInfoData(WrappedGameProfile.fromPlayer(player), 0, EnumWrappers.NativeGameMode.ADVENTURE, null));
        }), EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
    }

    private void showRealPlayers() {
        fetchPacket(data -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                data.add(new PlayerInfoData(
                        WrappedGameProfile.fromPlayer(player),
                        player.getPing(),
                        EnumWrappers.NativeGameMode.fromBukkit(player.getGameMode()),
                        WrappedChatComponent.fromText(player.getDisplayName())
                ));
            });
        }, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
    }

    private void fetchEntries(EnumWrappers.PlayerInfoAction action) {
        fetchPacket(data -> entries.forEach((index, entry) -> data.add(entry.createPlayerInfoData())), action);
    }

    private void fetchPacket(Consumer<List<PlayerInfoData>> data, EnumWrappers.PlayerInfoAction action) {
        final PacketContainer packet = PROTOCOL.createPacket(PacketType.Play.Server.PLAYER_INFO);
        final List<PlayerInfoData> playerInfoData = Lists.newArrayList();

        data.accept(playerInfoData);
        packet.getPlayerInfoAction().write(0, action);
        packet.getPlayerInfoDataLists().write(0, playerInfoData);

        sendPacket(packet);
    }

    private void sendPacket(PacketContainer packet) {
        try {
            for (Player player : viewers) {
                PROTOCOL.sendServerPacket(player, packet);
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}