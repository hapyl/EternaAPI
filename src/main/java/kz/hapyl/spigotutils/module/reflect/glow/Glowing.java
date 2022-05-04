package kz.hapyl.spigotutils.module.reflect.glow;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import kz.hapyl.spigotutils.EternaPlugin;
import kz.hapyl.spigotutils.module.reflect.Ticking;
import kz.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Map;
import java.util.Set;

public class Glowing implements Ticking {

    private final ProtocolManager manager = ProtocolLibrary.getProtocolManager();

    private ChatColor color;
    private int duration;
    private boolean everyoneIsListener;

    private boolean status;

    private final Set<Player> viewers;
    private final Map<Player, ChatColor> oldColor = Maps.newHashMap();
    private final Entity entity;

    public Glowing(Entity entity, int duration) {
        this(entity, ChatColor.WHITE, duration);
    }

    public Glowing(Entity entity, ChatColor color, int duration) {
        this.entity = entity;
        this.duration = duration;
        this.everyoneIsListener = false;
        this.status = false;
        this.viewers = Sets.newHashSet();
        this.setColor(color);

        EternaPlugin.getPlugin().getGlowingManager().addGlowing(entity, this);
    }

    public void setColor(ChatColor color) {
        Validate.isTrue(color.isColor(), "color must be color, not formatter!");
        this.color = color;
        if (isGlowing()) {
            updateTeamColor();
        }
    }

    protected void createPacket(boolean flag) {
        final PacketContainer packet = manager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, entity.getEntityId());

        final WrappedDataWatcher data = new WrappedDataWatcher();
        final WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);

        final WrappedDataWatcher realData = WrappedDataWatcher.getEntityWatcher(entity);

        final byte initByte = realData.getByte(0);
        final byte bitMask = (byte) 0x40;

        data.setEntity(entity);
        data.setObject(0, serializer, (byte) (flag ? (initByte | bitMask) : (initByte & ~bitMask)));
        packet.getWatchableCollectionModifier().write(0, data.getWatchableObjects());

        // send packet
        try {
            if (isEveryoneIsListener()) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    manager.sendServerPacket(player, packet);
                }
            }
            else {
                for (Player player : viewers) {
                    manager.sendServerPacket(player, packet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public final void tick() {
        if (!status) {
            return;
        }

        boolean lastTick = duration-- <= 0;

        if (lastTick) {
            createPacket(false);
            createTeam(false);
            EternaPlugin.getPlugin().getGlowingManager().removeGlowing(entity, this);
        }
    }

    public boolean isEveryoneIsListener() {
        return everyoneIsListener;
    }

    public void setEveryoneIsListener(boolean everyoneIsListener) {
        this.everyoneIsListener = everyoneIsListener;
    }

    public void addViewer(Player player) {
        this.viewers.add(player);
    }

    public void removeViewer(Player player) {
        this.viewers.remove(player);
    }

    public boolean isViewer(Player player) {
        return this.viewers.contains(player);
    }

    public int getDuration() {
        return duration;
    }

    public void glow() {
        this.start();
    }

    public void start() {
        this.status = true;
        createPacket(true);
        createTeam(true);
    }

    public boolean isGlowing() {
        return status && duration > 0;
    }

    public void stop() {
        this.duration = 0;
    }

    private void updateTeamColor() {
        viewers.forEach(player -> getTeamOrCreate(player).setColor(this.color));
    }

    public void createTeam(boolean flag) {
        if (viewers.isEmpty()) {
            return;
        }

        // Create team
        viewers.forEach(player -> {
            if (flag) {
                final Team team = getTeamOrCreate(player);
                team.addEntry(getEntityName());
            }
            else {
                deleteTeam(player);
            }
        });
    }


    private void deleteTeam(Player player) {
        final Team entryTeam = player.getScoreboard().getEntryTeam(getEntityName());
        if (entryTeam != null) {
            // Change team color to previous one or delete if fake team.
            entryTeam.setColor(oldColor.getOrDefault(player, ChatColor.WHITE));
            oldColor.remove(player);

            if (entryTeam.getName().equals("ETGlowing")) {
                entryTeam.unregister();
            }
        }
    }

    /**
     * If players already has a team, just change the team color and store old team color.
     * Else create new team and apply changes.
     * <p>
     * Probably should be using packets for this as well.
     */
    private Team getTeamOrCreate(Player player) {
        final Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getEntryTeam(getEntityName());

        if (team == null) {
            team = scoreboard.registerNewTeam("ETGlowing");
        }
        else {
            oldColor.putIfAbsent(player, team.getColor());
        }

        team.setColor(this.color);
        return team;
    }

    private String getEntityName() {
        return this.entity instanceof Player ? this.entity.getName() : this.entity.getUniqueId().toString();
    }

}
