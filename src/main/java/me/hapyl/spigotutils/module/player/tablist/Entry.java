package me.hapyl.spigotutils.module.player.tablist;

import com.comphenix.protocol.wrappers.*;
import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.UUID;

public class Entry {

    private final WrappedGameProfile profile;
    private String text;
    private PingBars ping;

    protected Entry(int numeral) {
        final UUID uuid = UUID.randomUUID();
        this.profile = new WrappedGameProfile(uuid, ChatColor.BLACK + "" + (numeral + 100));
        this.ping = PingBars.FIVE;
        this.text = "";
        setTexture(EntryTexture.GRAY);
    }

    public Entry setText(String text) {
        this.text = text == null ? "" : text;
        return this;
    }

    public Entry setPing(PingBars ping) {
        this.ping = ping == null ? PingBars.NO_PING : ping;
        return this;
    }

    public Entry setTexture(Player player) {
        // TODO: 0003, Oct 3 2022
        return this;
    }

    public Entry setTexture(@Nullable String value, @Nullable String signature) {
        this.profile.getProperties().removeAll("textures");
        this.profile.getProperties().put("textures", new WrappedSignedProperty("textures", value, signature));
        return this;
    }

    public Entry setTexture(EntryTexture texture) {
        return setTexture(texture.getValue(), texture.getSignature());
    }

    public String getText() {
        return text;
    }

    public PingBars getPing() {
        return ping;
    }

    protected WrappedGameProfile getProfile() {
        return profile;
    }

    protected PlayerInfoData createPlayerInfoData() {
        return new PlayerInfoData(
                profile,
                ping.getValue(),
                EnumWrappers.NativeGameMode.ADVENTURE,
                WrappedChatComponent.fromText(Chat.format(text))
        );
    }
}
