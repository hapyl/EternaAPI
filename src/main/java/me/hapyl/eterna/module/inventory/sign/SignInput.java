package me.hapyl.eterna.module.inventory.sign;

import com.google.common.collect.Maps;
import me.hapyl.eterna.module.annotate.*;
import me.hapyl.eterna.module.location.LocationHelper;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.reflect.packet.PacketFactory;
import me.hapyl.eterna.module.text.Strings;
import me.hapyl.eterna.module.util.CollectionUtils;
import me.hapyl.eterna.Runnables;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Represents a {@link Sign} ui for player input.
 *
 * <p>This class handles inputs via packets and does not actually affect the world in any way.</p>
 */
@TestedOn(version = Version.V1_21_11)
public abstract class SignInput {
    
    /**
     * Defines the dashed line inserted in between prompt and player input for wrapped prompt.
     */
    public static final String DASHED_LINE = "^^^^^^^^^^^^^^";
    
    /**
     * Defines the max length for prompt wrap.
     */
    public static final int PROMPT_WRAP_MAX_LENGTH = 14;
    
    static final Map<Player, SignInput> awaitingResponse = Maps.newHashMap();
    
    private final Player player;
    private final SignType signType;
    
    private final String[] lines;
    private final Location location;
    
    /**
     * Creates a new {@link SignInput} for the given {@link Player} and opens it.
     *
     * @param player   - The player whom to prompt.
     * @param signType - The sign type.
     * @param prompt   - The prompt, must be between {@code 0} - {@code 4} lines (inclusive).
     *                 <p>It is recommended to use {@link SignInput#SignInput(Player, SignType, String)} for an automatically wrapped prompt.</p>
     */
    public SignInput(@NotNull Player player, @NotNull SignType signType, @NotNull @Size(from = 0, to = 4) String... prompt) {
        this.player = player;
        this.signType = signType;
        
        this.lines = CollectionUtils.arrayCopy(prompt, 4, () -> "");
        this.location = player.getLocation();
        
        // Since a random minecraft version, signs now close when too far away from a player, so we
        // can't just place it at the world min height, oh well, just subtract 5 from player location
        final World world = location.getWorld();
        this.location.setY(Math.clamp(location.getY() - 5, world.getMinHeight() + 1, world.getMaxHeight()));
        
        // Schedule the packets
        Runnables.later(this::setSign, 1L);
    }
    
    /**
     * Creates a new {@link SignInput} for given {@link Player} and opens it.
     *
     * @param player   - The player whom to prompt.
     * @param signType - The sign type.
     * @param prompt   - The prompt.
     *                 <p>The prompt will be wrapped on spaces and fit from bottom to top, separated via {@link #DASHED_LINE} for player input.</p>
     */
    public SignInput(@NotNull Player player, @NotNull SignType signType, @NotNull String prompt) {
        this(player, signType, wrapPrompt(prompt));
    }
    
    /**
     * Creates a new {@link SignInput} for the given {@link Player} and opens it.
     *
     * @param player - The player whom to prompt.
     * @param prompt - The prompt.
     *               <p>The prompt will be wrapped on spaces and fit from bottom to top, separated via {@link #DASHED_LINE} for player input.</p>
     */
    public SignInput(@NotNull Player player, @NotNull String prompt) {
        this(player, SignType.OAK, prompt);
    }
    
    /**
     * Gets the {@link Player} this prompt belongs to.
     *
     * @return the player thi prompt belongs to.
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Gets the {@link SignType}.
     *
     * @return the sign type.
     */
    @NotNull
    public SignType getSignType() {
        return signType;
    }
    
    /**
     * Gets a copy of this {@link SignInput} {@link Location}, where the sign is shown for the player.
     *
     * @return a copy of this sign location.
     */
    @NotNull
    public Location getLocation() {
        return LocationHelper.copyOf(location);
    }
    
    /**
     * Callback to use for when the {@link Player} presses {@code Done} button or closes the sign.
     *
     * <p><b>The response is handled asynchronously and must be synchronized before executing it on main thread!</b></p>
     *
     * @param response - The response.
     * @see SignResponse#synchronize(Plugin, Runnable)
     */
    @Asynchronous
    public abstract void onResponse(@NotNull SignResponse response);
    
    @ApiStatus.Internal
    @PacketOperation
    void setSign() {
        final BlockData signData = signType.material.createBlockData();
        final Sign signState = (Sign) signData.createBlockState();
        
        final SignSide side = signState.getSide(Side.FRONT);
        
        for (int i = 0; i < lines.length; i++) {
            side.line(i, Component.text(lines[i]));
        }
        
        player.sendBlockChange(location, signData);
        player.sendBlockUpdate(location, signState);
        
        // Open sign editor
        Reflect.sendPacket(player, PacketFactory.makePacketOpenSignEditor(location, true));
        
        awaitingResponse.put(player, this);
    }
    
    @ApiStatus.Internal
    @PacketOperation
    void clearSign() {
        player.sendBlockChange(location, location.getBlock().getBlockData());
        awaitingResponse.remove(player);
    }
    
    @NotNull
    private static String[] wrapPrompt(@NotNull String prompt) {
        final List<String> promptWrapped = Strings.wrapString(prompt, PROMPT_WRAP_MAX_LENGTH);
        final int promptSize = promptWrapped.size();
        
        // If length is longer or equal to 4, pass as it
        if (promptSize >= 4) {
            // The constructor does a copy up to 4 elements, so we don't care about limiting the length
            return promptWrapped.toArray(String[]::new);
        }
        else {
            final int startIndex = 4 - promptSize;
            final int dashIndex = Math.max(0, startIndex - 1);
            
            final String[] output = { "", "", "", "" };
            
            for (int i = 0; i < promptWrapped.size(); i++) {
                output[startIndex + i] = promptWrapped.get(i);
            }
            
            output[dashIndex] = DASHED_LINE;
            output[0] = "";
            
            return output;
        }
    }
    
}