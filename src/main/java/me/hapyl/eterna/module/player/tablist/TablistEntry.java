package me.hapyl.eterna.module.player.tablist;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.reflect.Skin;
import me.hapyl.eterna.module.reflect.EternaServerPlayerImpl;
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
public class TablistEntry extends EternaServerPlayerImpl {
    
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
        super(PLAYER_LOCATION, "", DEFAULT_TEXTURE);
        
        this.index = index;
        this.tablist = tablist;
        
        this.text = Tablist.DEFAULT_ENTRY_NAME;
        this.bars = PingBars.FIVE;
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
     * <pre>{@code PRIORITY_BOUND - index;}</pre>
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
        Reflect.sendPacket(tablist.player, packetFactory.getPacketUpdateDisplayName());
    }
    
    /**
     * Sets the skin of this {@link TablistEntry} to be the {@link Player}'s skin.
     *
     * @param player - Player to get skin from.
     */
    public void setTexture(@Nonnull Player player) {
        // Storing a cached player object is much faster even though we cache the textures,
        // because we're using reflection to get the textures, not the paper way.
        if (this.skinnedPlayer != null && this.skinnedPlayer == player) {
            return;
        }
        
        this.skinnedPlayer = player;
        
        setTexture(Skin.ofProperty(BukkitUtils.getPlayerTextures(player)));
    }
    
    /**
     * Sets the skin of the entry.
     * <p>
     * Note that changing a skin <b>requires</b> for the entry to be removed and added again.
     * </p>
     * <p>
     * This will result in tablist "blinking" for 1 tick.
     * <i>EternaAPI</i> does its best to cache and prevent it from updating unless necessary,
     * but if you're using {@link Tablist} to display online players, expect flickering when
     * player joins/leaves.
     * </p>
     *
     * @param newTextures
     */
    @Override
    public void setTexture(@Nonnull Skin newTextures) {
        final String newTexture = newTextures.texture();
        final String newSignature = newTextures.signature();
        
        // Because changing skin requires to send a `remove` packet and then an `add` packet,
        // it will result in a 1-tick blinking, so we reduce calls be caching the last known
        // textures and only update when they're different.
        
        // This will still blink when the texture is changed, but as far as I'm aware, there isn't
        // a way to prevent that.
        if (cachedTextures[0].equals(newTexture) && cachedTextures[1].equals(newSignature)) {
            return;
        }
        
        // Actually cache the texture to remove blinking every update dumbass
        cachedTextures[0] = newTexture;
        cachedTextures[1] = newSignature;
        
        super.setTexture(newTextures);
        this.updateSkin();
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
    
}
