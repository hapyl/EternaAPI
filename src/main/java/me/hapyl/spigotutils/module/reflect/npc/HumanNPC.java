package me.hapyl.spigotutils.module.reflect.npc;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.EternaRegistry;
import me.hapyl.spigotutils.module.annotate.InsuredViewers;
import me.hapyl.spigotutils.module.annotate.TestedNMS;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.hologram.Hologram;
import me.hapyl.spigotutils.module.math.Numbers;
import me.hapyl.spigotutils.module.math.nn.IntInt;
import me.hapyl.spigotutils.module.player.PlayerLib;
import me.hapyl.spigotutils.module.quest.PlayerQuestObjective;
import me.hapyl.spigotutils.module.quest.QuestManager;
import me.hapyl.spigotutils.module.quest.QuestObjectiveType;
import me.hapyl.spigotutils.module.reflect.DataWatcherType;
import me.hapyl.spigotutils.module.reflect.Reflect;
import me.hapyl.spigotutils.module.reflect.ReflectPacket;
import me.hapyl.spigotutils.module.reflect.npc.entry.NPCEntry;
import me.hapyl.spigotutils.module.reflect.npc.entry.StringEntry;
import me.hapyl.spigotutils.module.util.BukkitUtils;
import me.hapyl.spigotutils.module.util.Helper;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EnumItemSlot;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

/**
 * Allows to create SIMPLE player NPCs with support of clicks.
 * For complex NPCs use CitizensAPI
 */
@SuppressWarnings("unused")
@TestedNMS(version = "1.18")
public class HumanNPC implements Intractable, Human {

    public static final String chatFormat = "&e[NPC] &a{NAME}: " + ChatColor.WHITE + "{MESSAGE}";

    private final ProtocolManager manager = ProtocolLibrary.getProtocolManager();

    private final ReflectPacket packetAddPlayer;
    private final ReflectPacket packetRemovePlayer;
    private final ReflectPacket packetSpawn;
    private final ReflectPacket packetDestroy;

    private final GameProfile profile;
    private final EntityPlayer human;
    private final Hologram aboveHead;
    private final String npcName;
    private final String hexName;
    private final UUID uuid;
    private final NPCResponse responses = new NPCResponse();
    private final NPCEquipment equipment;

    private Location location;
    private String skinOwner;
    private String chatPrefix;
    private boolean alive;
    private boolean stopTalking = false;

    private long interactionDelay = 0L;
    private final Map<UUID, Long> interactedAt = new HashMap<>();

    private String cannotInteractMessage = "Not now.";

    private boolean persistent;
    private int farAwayDist = 40;
    private int lookAtCloseDist;

    protected final HashMap<Player, Boolean> showingTo = new HashMap<>();
    private final List<NPCEntry> entries = new ArrayList<>();

    private HumanNPC() {
        this(BukkitUtils.getSpawnLocation(), "", "");
    }

    public HumanNPC(Location location, @Nullable String npcName) {
        this(location, npcName, npcName);
    }

    /**
     * Create instance of HumanNPC class.
     *
     * @param location  - Location of NPC.
     * @param npcName   - Name of NPC.
     *                  Null will convert NPC's name to "&e&lCLICK".
     *                  Keep blank string to remove name.
     * @param skinOwner - Skin owner.
     *                  Leave empty or null to apply skin later.
     *                  Only use online players names for this. See {@link HumanNPC#setSkinAsync(String)} disclaimer.
     */
    public HumanNPC(Location location, @Nullable String npcName, @Nullable String skinOwner) {
        if (npcName == null) {
            npcName = "&e&lCLICK";
        }

        if (npcName.length() > 32) {
            throw new IndexOutOfBoundsException("NPC name cannot be longer than 32.");
        }

        if (!npcName.isEmpty()) {
            this.chatPrefix = npcName;
        }

        this.uuid = UUID.randomUUID();
        this.location = location.clone();
        this.skinOwner = skinOwner;
        this.npcName = npcName;
        this.hexName = ("§8[NPC] " + uuid.toString().replace("-", "")).substring(0, 16);
        this.aboveHead = new Hologram().addLine(this.npcName).create(this.location.clone().subtract(0.0d, 0.51d, 0.0d));
        this.profile = new GameProfile(this.uuid, this.hexName);
        this.equipment = new NPCEquipment();

        if (skinOwner != null && !skinOwner.isEmpty()) {
            setSkin(skinOwner);
        }

        this.human = new EntityPlayer(
                Reflect.getMinecraftServer(),
                (WorldServer) Reflect.getMinecraftWorld(location.getWorld()),
                profile,
                null
        );

        this.human.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        this.packetAddPlayer = new ReflectPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, this.human));
        this.packetRemovePlayer = new ReflectPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, this.human));
        this.packetSpawn = new ReflectPacket(new PacketPlayOutNamedEntitySpawn(this.human));
        this.packetDestroy = new ReflectPacket(new PacketPlayOutEntityDestroy(this.human.getBukkitEntity().getEntityId()));

        EternaRegistry.getNpcRegistry().register(this);
        this.alive = true;

    }

    public static boolean isNPC(int entityId) {
        return EternaRegistry.getNpcRegistry().isRegistered(entityId);
    }

    @Override
    public void setFreezeTicks(int ticks) {
        Reflect.setDataWatcherValue(human, DataWatcherType.INT, 7, ticks);
        updateDataWatcher();
    }

    @Override
    public int getFreezeTicks() {
        return bukkitEntity().getFreezeTicks();
    }

    @Override
    public void setShoulderEntity(boolean shoulder, boolean status) {
        throw new NotImplementedException("setShoulderEntity not yet implemented");
    }

    public Player bukkitEntity() {
        return this.human.getBukkitEntity();
    }

    public static void hideAllNames(Scoreboard score) {
        for (final HumanNPC value : EternaRegistry.getNpcRegistry().getRegistered().values()) {
            value.hideTabListName();
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public Set<Player> getViewers() {
        return showingTo.keySet();
    }

    private String getPrefix() {
        return this.npcName == null ? "&aNPC" : this.npcName;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public boolean isShowingTo(Player player) {
        return this.showingTo.containsKey(player) && this.showingTo.get(player);
    }

    public String getNpcName() {
        return npcName;
    }

    public String getHexName() {
        return hexName;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getSkinOwner() {
        return skinOwner;
    }

    public void setSkinOwner(String skinOwner) {
        this.skinOwner = skinOwner;
    }

    public String getChatPrefix() {
        return chatPrefix;
    }

    public void setChatPrefix(String chatPrefix) {
        this.chatPrefix = chatPrefix;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public int getFarAwayDist() {
        return farAwayDist;
    }

    public HumanNPC setFarAwayDist(int farAwayDist) {
        this.farAwayDist = farAwayDist;
        return this;
    }

    public void sendNpcMessage(Player player, String msg) {
        // Placeholders
        if (msg.contains("{") && msg.contains("}")) {
            msg = placeHold(msg, player);
        }

        final String finalMessage = chatFormat.replace("{NAME}", this.getPrefix()).replace("{MESSAGE}", msg);
        Chat.sendMessage(player, finalMessage);

    }

    public HumanNPC addEntry(NPCEntry entry) {
        this.entries.add(entry);
        return this;
    }

    public HumanNPC addDialogLine(String string, int delayNext) {
        entries.add(new StringEntry(string, delayNext));
        return this;
    }

    public HumanNPC addDialogLine(String string) {
        return this.addDialogLine(string, Numbers.clamp(string.length(), 20, 100));
    }

    public final boolean onClickAuto(Player player) {

        // Can interact?
        if (!this.canInteract(player)) {
            final String cannotInteract = this.getNPCResponses().getCannotInteract();
            if (cannotInteract.isEmpty()) {
                return false;
            }
            sendNpcMessage(player, cannotInteract);
            return false;
        }

        // Do the entry magic
        final QuestManager quest = QuestManager.current();
        if (!this.entries.isEmpty()) {
            final IntInt nextDelay = new IntInt();
            final IntInt i = new IntInt();
            for (final NPCEntry entry : this.entries) {
                EternaPlugin.runTaskLater((task) -> {

                    if (!this.exists() || !player.isOnline() || stopTalking) {
                        stopTalking = false;
                        task.cancel();
                        return;
                    }

                    // Progress FINISH_DIALOG
                    if (i.get() == (this.entries.size() - 1)) {
                        quest.checkActiveQuests(player, QuestObjectiveType.FINISH_DIALOGUE, this);
                    }

                    i.increment();
                    entry.invokeEntry(this, player);

                }, nextDelay.get());
                nextDelay.addAndGet(entry.getDelay());
            }
            this.interactionDelay = nextDelay.get() + 20L;
            this.setInteractDelay(player);
        }

        // Progress TALK_TO_NPC
        quest.checkActiveQuests(player, QuestObjectiveType.TALK_TO_NPC, this);

        // Progress GIVE_ITEM_STACK_TO_NPC
        final ItemStack item = player.getInventory().getItemInMainHand();
        if (!item.getType().isAir()) {

            // Item Stack test
            quest.checkActiveQuests(player, QuestObjectiveType.GIVE_ITEM_STACK_TO_NPC, item, this);

            // Material test
            if (quest.hasQuestsOfType(player, QuestObjectiveType.GIVE_ITEMS_TO_NPC)) {
                for (final PlayerQuestObjective obj : quest.getActiveObjectivesOfType(player, QuestObjectiveType.GIVE_ITEMS_TO_NPC)) {
                    final int amount = item.getAmount();
                    // Check for the correct item
                    if (obj.testQuestCompletion(item.getType(), amount, this) >= 0.0d) {

                        final int needMore = (int) (obj.getCompletionGoal() - obj.getGoalCurrent());
                        final int canGive = Numbers.clamp(amount - needMore, needMore, amount);

                        obj.incrementGoal(canGive, true);

                        if (obj.isFinished()) {
                            this.sendNpcMessage(player, this.getNPCResponses().getQuestGiveItemsFinish());
                            PlayerLib.villagerYes(player);
                        }
                        else {
                            this.sendNpcMessage(player, this.getNPCResponses().getQuestGiveItemsNeedMore());
                            PlayerLib.playSound(player, Sound.ENTITY_VILLAGER_TRADE, 1.0f);
                        }
                    }
                    // Not correct item
                    else {
                        this.sendNpcMessage(player, this.getNPCResponses().getQuestGiveItemsInvalidItem());
                        PlayerLib.villagerNo(player);
                    }
                }
            }

        }
        return true;
    }

    public NPCResponse getNPCResponses() {
        return responses;
    }


    public void setInteractDelay(Player player) {
        this.interactedAt.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public boolean exists() {
        return EternaRegistry.getNpcRegistry().isRegistered(getId());
    }

    public String getName() {
        return npcName;
    }

    public void stopTalking() {
        this.stopTalking = true;
    }

    private boolean canInteract(Player player) {
        final long talkedAt = this.interactedAt.getOrDefault(player.getUniqueId(), 0L);
        final long ticksToMillis = this.interactionDelay * 50L;
        return System.currentTimeMillis() >= (talkedAt + ticksToMillis);
    }

    public String getCannotInteractMessage() {
        return cannotInteractMessage;
    }

    public HumanNPC setCannotInteractMessage(String cannotInteractMessage) {
        this.cannotInteractMessage = cannotInteractMessage;
        return this;
    }

    public HumanNPC setInteractionDelay(long interactionDelay) {
        this.interactionDelay = interactionDelay;
        return this;
    }

    public long getInteractionDelay() {
        return interactionDelay;
    }

    private String placeHold(String entry, Player player) {
        final String[] splits = entry.split(" ");
        final StringBuilder builder = new StringBuilder();
        for (String split : splits) {
            split = split
                    .replace(Placeholders.PLAYER.get(), player.getName())
                    .replace(Placeholders.NAME.get(), this.getName())
                    .replace(Placeholders.LOCATION.get(), BukkitUtils.locationToString(this.getLocation()));
            builder.append(split);
            builder.append(" ");
        }
        return builder.toString().trim();
    }

    public int getLookAtCloseDist() {
        return lookAtCloseDist;
    }

    public HumanNPC setLookAtCloseDist(int lookAtCloseDist) {
        this.lookAtCloseDist = lookAtCloseDist;
        return this;
    }

    public HumanNPC addTextAboveHead(String text) {
        this.aboveHead.addLine(text);
        this.aboveHead.updateLines(true);
        return this;
    }

    public HumanNPC removeTextAboveHead(int index) {
        this.aboveHead.removeLine(index);
        this.aboveHead.updateLines(true);
        return this;
    }


    public HumanNPC setPose(NPCPose pose) {
        return setPose(pose, (Player) null);
    }

    @Override
    public void onClick(Player player, HumanNPC npc, ClickType clickType) {

    }

    @Override
    @InsuredViewers
    public HumanNPC setPose(NPCPose pose, @Nullable Player... players) {
        human.b(pose.getNMSValue());
        updateDataWatcher(players);
        return this;
    }

    @Override
    @InsuredViewers
    public void lookAt(Entity entity, Player... players) {
        lookAt(entity.getLocation(), players);
    }

    @Override
    @InsuredViewers
    public void lookAt(Location location, @Nullable Player... players) {
        players = insureViewers(players);
        this.location.setDirection(location.clone().subtract(this.location).toVector());
        this.setHeadRotation(this.location.getYaw(), players);
        ReflectPacket.send(new PacketPlayOutEntity.PacketPlayOutEntityLook(
                this.getId(),
                (byte) (this.location.getYaw() * 256 / 360),
                (byte) (this.location.getPitch() * 256 / 360),
                true
        ), players);
    }

    public void teleport(double x, double y, double z, float yaw, float pitch) {
        this.location.setX(x);
        this.location.setY(y);
        this.location.setZ(z);
        this.location.setYaw(yaw);
        this.location.setPitch(pitch);
        this.setLocation(this.location);
        syncText();
    }

    @Override
    @InsuredViewers
    public void setLocation(Location location, Player... players) {
        players = insureViewers(players);
        this.location = location;
        this.human.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        this.setHeadRotation(this.location.getYaw(), players);
        new ReflectPacket(new PacketPlayOutEntityTeleport(this.human)).sendPackets(players);
        syncText();
    }

    @Override
    @InsuredViewers
    public void setHeadRotation(float yaw, Player... players) {
        players = insureViewers(players);
        new ReflectPacket(new PacketPlayOutEntityHeadRotation(this.human, (byte) ((yaw * 256) / 360))).sendPackets(players);
    }

    @Override
    @InsuredViewers
    public void swingMainHand(Player... players) {
        swingArm(true, insureViewers(players));
    }

    @Override
    @InsuredViewers
    public void swingOffHand(Player... players) {
        swingArm(false, insureViewers(players));
    }

    private void swingArm(boolean b, Player... insured) {
        new ReflectPacket(new PacketPlayOutAnimation(this.human, b ? 0 : 3)).sendPackets(insured);
    }

    public HumanNPC setSkin(String texture, String signature) {
        this.profile.getProperties().removeAll("textures");
        this.profile.getProperties().put("textures", new Property("textures", texture, signature));
        return this;
    }

    public boolean hasSkin() {
        final Collection<Property> skin = getSkin();
        return skin != null && !skin.isEmpty();
    }

    public Collection<Property> getSkin() {
        return this.profile.getProperties().get("textures");
    }

    public HumanNPC setSkin(String skinOwner) {
        final Player player = Bukkit.getPlayer(skinOwner);
        if (player == null) {
            setSkinAsync(skinOwner);
        }
        else {
            setSkinSync(skinOwner);
        }
        return this;
    }

    public void setSkinSync(String skinOwner) {
        final Player owner = Bukkit.getPlayer(skinOwner);
        if (owner == null) {
            return;
        }
        final String[] skin = this.getSkin(skinOwner);
        this.setSkin(skin[0], skin[1]);
    }

    /**
     * <b>Warning, please read before using.</b>
     * This method grabs texture from Mojang API asynchronously,
     * which will result in delay. After textures will be applied,
     * NPC will reload, resetting all per-player packet changes
     * applied before this.
     *
     * I recommend use {@link HumanNPC#setSkin(String, String)}
     * to apply textures and signature manually, instead of grabbing
     * it from API.
     *
     * You can use <a href="https://mineskin.org/gallery">https://mineskin.org/gallery</a>
     * to grab textures and signature for skins that are permanent.
     *
     * Grabbing skin from players name also means that skin is not
     * permanent and will change upon players changing it.
     *
     * @param skinOwner - Name (nick) of minecraft account.
     */
    public void setSkinAsync(String skinOwner) {
        runAsync(skinOwner, callback -> {
            this.setSkin(callback[0], callback[1]);
            this.reloadNpcData();
        });
    }

    private void runAsync(String str, Consumer<String[]> callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                callback.accept(getSkin(str));
            }
        }.runTaskAsynchronously(EternaPlugin.getPlugin());
    }

    @TestedNMS(version = "1.18")
    public String[] getSkin(String targetName) {
        // if player on the server just get their game profile
        if (Bukkit.getPlayer(targetName) != null) {
            try {
                final Player player = Bukkit.getPlayer(targetName);
                final EntityPlayer nmsEntity = Reflect.getMinecraftPlayer(player);
                final GameProfile profile = Reflect.getGameProfile(nmsEntity);

                final Property textures = profile
                        .getProperties()
                        .get("textures")
                        .stream()
                        .findFirst()
                        .orElse(new Property(targetName, targetName));
                return new String[] { textures.getValue(), textures.getSignature() };
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new String[] { "null", "null" };
        }
        // if name longer than max name than have to assume it's a texture
        if (targetName.length() > 16) {
            return new String[] { "invalidValue", "invalidSignature" };
        }
        try {
            final URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + targetName);
            final InputStreamReader reader = new InputStreamReader(url.openStream());
            final String uuid = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();
            final URL sessionUrl = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            final InputStreamReader sessionReader = new InputStreamReader(sessionUrl.openStream());
            final JsonObject property = new JsonParser()
                    .parse(sessionReader)
                    .getAsJsonObject()
                    .get("properties")
                    .getAsJsonArray()
                    .get(0)
                    .getAsJsonObject();
            return new String[] { property.get("value").getAsString(), property.get("signature").getAsString() };
        } catch (Exception error) {
            return new String[] { targetName, targetName };
        }
    }

    @Override
    public void setGhostItem(ItemSlot slot, ItemStack item, Player... players) {
        sendEquipmentChange(slot, item, players);
    }

    @Override
    @InsuredViewers
    public void setItem(ItemSlot slot, ItemStack item, Player... players) {
        players = insureViewers(players);

        equipment.setItem(slot, item);
        updateEquipment(players);
    }

    @Override
    @InsuredViewers
    public void updateEquipment(Player... players) {
        players = insureViewers(players);
        if (this.equipment != null) {
            sendEquipmentChange(ItemSlot.HEAD, equipment.getHelmet(), players);
            sendEquipmentChange(ItemSlot.CHEST, equipment.getChestplate(), players);
            sendEquipmentChange(ItemSlot.LEGS, equipment.getLeggings(), players);
            sendEquipmentChange(ItemSlot.FEET, equipment.getBoots(), players);
            sendEquipmentChange(ItemSlot.MAINHAND, equipment.getHand(), players);
            sendEquipmentChange(ItemSlot.OFFHAND, equipment.getOffhand(), players);
        }
    }

    private void sendEquipmentChange(ItemSlot slot, ItemStack stack, Player... players) {
        final List<Pair<EnumItemSlot, net.minecraft.world.item.ItemStack>> list = new ArrayList<>();
        list.add(new Pair<>(slot.getSlot(), toNMSItemStack(stack)));
        ReflectPacket.send(new PacketPlayOutEntityEquipment(this.getId(), list), players);
    }

    public NPCEquipment getEquipment() {
        return equipment;
    }

    public void showAll() {
        for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            this.show(onlinePlayer);
        }
    }

    private void show(boolean init, Player... players) {
        if (init) {
            aboveHead.show(players);
            hideDisplayName(players);
        }

        packetAddPlayer.sendPackets(players);
        packetSpawn.sendPackets(players);

        setLocation(this.location, players);

        updateSkin(players);
        updateEquipment(players);
        updateDataWatcher(players);

        if (init) {
            for (final Player player : players) {
                this.showingTo.put(player, true);
            }
        }

        this.hideTabListName(players);
    }

    @Override
    public void show(@Nonnull Player... players) {
        show(true, players);
    }

    @Override
    public void reloadNpcData(Player... players) {
        players = insureViewers(players);
        hide(false, players);
        show(false, players);
    }

    /* Methods with insured viewers. See: {@link InsuredViewers} */

    @Override
    @InsuredViewers
    public void setEquipment(EntityEquipment equipment, @Nullable Player... players) {
        this.equipment.setEquipment(equipment);
        updateEquipment(insureViewers(players));
    }

    @Override
    @InsuredViewers
    public void hide(@Nullable Player... players) {
        hide(true, players);
    }

    @Override
    @InsuredViewers
    public void setDataWatcherByteValue(int key, byte value, @Nullable Player... viewers) {
        human.ai().a(DataWatcherRegistry.a.a(key), value);
        updateDataWatcher(insureViewers(viewers));
    }

    @Override
    @InsuredViewers
    public void hideTabListName(@Nullable Player... players) {
        Bukkit.getScheduler().runTaskLater(EternaPlugin.getPlugin(), () -> packetRemovePlayer.sendPackets(insureViewers(players)), 20L);
    }

    @Override
    @InsuredViewers
    public void updateSkin(@Nullable Player... players) {
        Reflect.setDataWatcherValue(this.human, DataWatcherType.BYTE, 17, (byte) (0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40));
        updateDataWatcher(players);
    }

    @Override
    @InsuredViewers
    public void playAnimation(NPCAnimation animation, @Nullable Player... players) {
        ReflectPacket.send(new PacketPlayOutAnimation(this.getHuman(), animation.getPos()), insureViewers(players));
    }

    @Override
    @InsuredViewers
    public void updateDataWatcher(@Nullable Player... players) {
        ReflectPacket.send(new PacketPlayOutEntityMetadata(this.getId(), this.human.ai(), true), insureViewers(players));
    }

    /**
     * Completely removes NPC.
     */
    public void remove() {
        this.alive = false;
        this.hide();
        this.showingTo.clear();
        EternaRegistry.getNpcRegistry().remove(getId());
    }

    private void hide(boolean b, Player... players) {
        players = insureViewers(players);

        if (b) {
            for (final Player player : players) {
                this.showingTo.put(player, false);
            }

            this.aboveHead.hide(players);
        }
        this.packetRemovePlayer.sendPackets(players);
        this.packetDestroy.sendPackets(players);
    }

    public HumanNPC setCollision(boolean flag) {
        for (final Player viewer : this.getViewers()) {
            final Team team = Helper.getNpcTeam(viewer.getScoreboard());
            team.setOption(Team.Option.COLLISION_RULE, flag ? Team.OptionStatus.ALWAYS : Team.OptionStatus.NEVER);
            team.addEntry(this.hexName);
        }
        return this;
    }

    public int getId() {
        return bukkitEntity().getEntityId();
    }

    public EntityPlayer getHuman() {
        return human;
    }

    @Override
    public void hideDisplayName(Player... players) {
        for (final Player player : players) {
            final Team team = Helper.getNpcTeam(player.getScoreboard());
            team.addEntry(this.hexName);
        }
    }

    private net.minecraft.world.item.ItemStack toNMSItemStack(ItemStack stack) {
        return (net.minecraft.world.item.ItemStack) Reflect.invokeMethod(Reflect.getCraftMethod(
                "inventory.CraftItemStack",
                "asNMSCopy",
                ItemStack.class
        ), null, stack);
    }

    private Player[] insureViewers(Player... players) {
        if (players == null || players.length == 0) {
            return showingTo.keySet().toArray(new Player[] {});
        }
        return players;
    }

    /**
     * @deprecated Use {@link Helper}
     */
    @Deprecated
    private Team getTeamOrCreate(Scoreboard scoreboard) {
        Team team = scoreboard.getTeam("ETERNA_API");
        if (team == null) {
            team = scoreboard.registerNewTeam("ETERNA_API");
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }
        return team;
    }

    public void syncText() {
        if (this.aboveHead != null) {
            this.aboveHead.teleport(this.location.clone().add(0.0d, 1.50d, 0.0d));
        }
    }

    @Nullable
    public static HumanNPC getById(int id) {
        return EternaRegistry.getNpcRegistry().getById(id);
    }

}
