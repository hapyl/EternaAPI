package me.hapyl.eterna.module.player.tablist;

import io.papermc.paper.adventure.PaperAdventure;
import me.hapyl.eterna.module.location.LocationHelper;
import me.hapyl.eterna.module.reflect.EternaServerPlayerImpl;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.reflect.Skin;
import me.hapyl.eterna.module.reflect.packet.PacketFactory;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a {@link Tablist} entry, that can be shown in the {@link Tablist}.
 */
public class TablistEntry extends EternaServerPlayerImpl {
    
    /**
     * Defines the default texture used for an entry.
     */
    @NotNull
    public static final EntryTexture DEFAULT_TEXTURE = EntryTexture.DARK_GRAY;
    
    /**
     * Defines the arbitrary tablist priority bound, which is used to calculate
     * the actual position of the entry in the tablist.
     *
     * <p>
     * Using the integer max value guarantees that no natural entries may override the order.
     * </p>
     */
    public static final int PRIORITY_BOUND = Integer.MAX_VALUE;
    
    private static final Location ENTITY_LOCATION = LocationHelper.defaultLocation(0, -64, 0);
    
    private final int index;
    private final Tablist tablist;
    
    private final String[] cachedTextures = { "", "" };
    
    private Component text;
    private Player skinnedPlayer;
    private PingBars bars;
    
    TablistEntry(int index, @NotNull Tablist tablist) {
        super(ENTITY_LOCATION, "", DEFAULT_TEXTURE);
        
        this.index = index;
        this.tablist = tablist;
        
        this.text = Tablist.DEFAULT_ENTRY_NAME;
        this.bars = PingBars.FIVE;
    }
    
    /**
     * Gets the tablist name for this {@link TablistEntry}.
     *
     * <p>
     * Overriding this method is the key for the system to work.
     * </p>
     *
     * @return the tablist name for this entry.
     */
    @Nullable
    @Override
    public final net.minecraft.network.chat.Component getTabListDisplayName() {
        return PaperAdventure.asVanilla(text);
    }
    
    /**
     * Gets the tablist order of this {@link TablistEntry}.
     *
     * <p>
     * Overriding this method if the key for the system to work.
     * </p>
     *
     * @return the tablist order of this entry.
     */
    @Override
    public final int getTabListOrder() {
        return PRIORITY_BOUND - index;
    }
    
    /**
     * Gets the index of this entry.
     *
     * <p>
     * The index defines the ordinal of the entry in the {@link Tablist}, not the
     * position in {@link Player} tablist.
     * </p>
     *
     * @return the index of this entry.
     */
    public int index() {
        return index;
    }
    
    /**
     * Sets the text {@link Component} of this {@link TablistEntry}.
     *
     * @param newText - The new text to set.
     */
    public void setText(@NotNull Component newText) {
        if (this.text.equals(newText)) {
            return;
        }
        
        this.text = newText;
        
        // Send packet
        Reflect.sendPacket(tablist.player, PacketFactory.makePacketPlayerInfoUpdate(this, ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME));
    }
    
    /**
     * Sets the skin of this {@link TablistEntry} to be the given {@link Player}'s skin.
     *
     * @param player - The player to skin. <i>uhhh</i>
     */
    public void setTexture(@NotNull Player player) {
        // Storing a cached player object is much faster even though we cache the textures,
        // because we're using reflection to get the textures, not the paper way.
        if (this.skinnedPlayer != null && this.skinnedPlayer == player) {
            return;
        }
        
        this.skinnedPlayer = player;
        
        setTexture(Skin.ofPlayer(player));
    }
    
    /**
     * Gets the current {@link PingBars} of this {@link TablistEntry}.
     *
     * @return the current ping of this entry.
     */
    @NotNull
    public PingBars getPing() {
        return bars;
    }
    
    /**
     * Sets the {@link PingBars} for this {@link TablistEntry}.
     *
     * @param bars - The ping to set.
     */
    public void setPing(@NotNull PingBars bars) {
        if (this.bars == bars) {
            return;
        }
        
        this.bars = bars;
        
        super.setPing(bars);
        super.updatePing(tablist.player);
    }
    
    /**
     * Sets the {@link Skin} of this {@link TablistEntry}.
     *
     * <p>
     * Note that changing a skin <b>requires</b> for the entry to be removed and added again.
     * </p>
     *
     * <p>
     * This will result in tablist "blinking" for 1 tick; Eterna does its best to cache and prevent it from
     * updating unless necessary, but if you're using {@link Tablist} to display online players, expect
     * flickering when player joins/leaves.
     * </p>
     *
     * @param newTextures - The new textures to set.
     */
    @Override
    public void setTexture(@NotNull Skin newTextures) {
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
    
    private void updateSkin() {
        final ClientboundPlayerInfoRemovePacket packetRemove = PacketFactory.makePacketPlayerInfoRemove(this);
        final ClientboundPlayerInfoUpdatePacket packetAdd = PacketFactory.makePacketPlayerInitialization(this);
        
        Reflect.sendPacket(tablist.player, packetRemove);
        Reflect.sendPacket(tablist.player, packetAdd);
    }
    
}
