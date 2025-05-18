package me.hapyl.eterna.module.player.tablist;

import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.reflect.npc.EternaPlayer;
import me.hapyl.eterna.module.util.BukkitUtils;
import me.hapyl.eterna.module.util.SupportsColorFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a {@link Tablist} entry, that can be shown in the {@link Tablist}.
 */
public class TablistEntry extends EternaPlayer {
    
    private static final Location PLAYER_LOCATION = BukkitUtils.defLocation(0, -64, 0);
    private static final EntryTexture DEFAULT_TEXTURE = EntryTexture.DARK_GRAY;
    private static final int PRIORITY_BOUND = Integer.MAX_VALUE;
    
    private final int index;
    private final Tablist tablist;
    
    private final String[] cachedTextures = { "", "" };
    
    private String text;
    private Player skinnedPlayer;
    private PingBars bars;
    
    TablistEntry(int index, @Nonnull Tablist tablist) {
        super(PLAYER_LOCATION, ""); // Keeping the actual GameProfile name empty is the key for the system to work
        
        this.index = index;
        this.tablist = tablist;
        
        this.text = Tablist.DEFAULT_ENTRY_NAME;
        this.bars = PingBars.FIVE;
        
        // Hardcoding default textures so it's not steves lol
        setTextureRaw(DEFAULT_TEXTURE.getValue(), DEFAULT_TEXTURE.getSignature());
    }
    
    /**
     * Gets the tablist name for this entry.
     * <p>Overriding this method is the key for the system to work.</p>
     *
     * @return the tablist name for this entry.
     */
    @Nullable
    @Override
    public Component getTabListDisplayName() {
        return Chat.component(text);
    }
    
    /**
     * Gets the index of this entry.
     * <p>The index represents the ordinal of the entry in the Tablist.</p>
     *
     * @return the index of this entry.
     */
    public int index() {
        return index;
    }
    
    /**
     * Gets the tablist order of this entry.
     * <p>The order is calculated as follows:</p>
     * <pre>{@code return PRIORITY_BOUND - index;}</pre>
     *
     * @return the tablist order of this entry.
     */
    @Override
    public int getTabListOrder() {
        return PRIORITY_BOUND - index;
    }
    
    /**
     * Changes the text of this entry.
     *
     * @param newText - New text.
     */
    public void setText(@Nonnull @SupportsColorFormatting String newText) {
        if (this.text.equals(newText)) {
            return;
        }
        
        this.text = newText;
        
        // Update text
        Reflect.sendPacket(tablist.player, packetFactory.getPacketUpdatePlayer());
    }
    
    /**
     * Sets the skin of this {@link TablistEntry} to be the {@link Player}'s skin.
     *
     * @param player - Player to get skin from.
     */
    public TablistEntry setTexture(@Nonnull Player player) {
        if (skinnedPlayer != null && skinnedPlayer == player) {
            return this;
        }
        
        final Property textures = BukkitUtils.getPlayerTextures(player);
        
        skinnedPlayer = player;
        return setTexture(textures.value(), textures.signature());
    }
    
    /**
     * Sets the skin of this {@link TablistEntry} from value and signature.
     * <p>
     * You can use something like <a href="https://mineskin.org/">MineSkin</a> to get the value and the signature.
     *
     * @param value     - Value.
     * @param signature - Signature.
     */
    public TablistEntry setTexture(@Nonnull String value, @Nullable String signature) {
        // Reduce calls since it probably will be updated a lot
        if (cachedTextures[0].equals(value) && cachedTextures[1].equals(signature)) {
            return this;
        }
        
        setTextureRaw(value, signature);
        updateSkin();
        return this;
    }
    
    /**
     * Sets the skin of this {@link TablistEntry} from the {@link EntryTexture}.
     *
     * @param skin - Skin to set.
     */
    public TablistEntry setTexture(@Nonnull EntryTexture skin) {
        return setTexture(skin.getValue(), skin.getSignature());
    }
    
    /**
     * Updates the skin for all players who can see this {@link TablistEntry}.
     */
    public void updateSkin() {
        final ClientboundPlayerInfoRemovePacket packetRemove = packetFactory.getPacketRemovePlayer();
        final ClientboundPlayerInfoUpdatePacket packetAdd = packetFactory.getPacketInitPlayer();
        
        Reflect.sendPacket(tablist.player, packetRemove);
        Reflect.sendPacket(tablist.player, packetAdd);
    }
    
    @Override
    @Deprecated
    public void show(@Nonnull Player player) {
    }
    
    @Override
    public void hide(@Nonnull Player player) {
        Reflect.sendPacket(player, packetFactory.getPacketRemovePlayer());
    }
    
    /**
     * Gets the current {@link PingBars} of this {@link TablistEntry}.
     *
     * @return the current ping of this entry.
     */
    @Nonnull
    public PingBars getPing() {
        return bars;
    }
    
    public void setPing(@Nonnull PingBars bars) {
        if (this.bars == bars) { // optimization
            return;
        }
        
        this.bars = bars;
        super.setPing(bars);
        super.updatePing(tablist.player);
    }
    
    protected void setTextureRaw(String value, String signature) {
        final PropertyMap properties = getProfile().getProperties();
        
        properties.removeAll("textures");
        properties.put("textures", new Property("textures", value, signature));
        
        cachedTextures[0] = value;
        cachedTextures[1] = signature;
    }
    
}
