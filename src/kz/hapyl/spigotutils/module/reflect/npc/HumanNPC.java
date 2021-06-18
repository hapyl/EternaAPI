package kz.hapyl.spigotutils.module.reflect.npc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import kz.hapyl.spigotutils.SpigotUtilsPlugin;
import kz.hapyl.spigotutils.module.annotate.DevNote;
import kz.hapyl.spigotutils.module.annotate.InsuredViewers;
import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.hologram.Hologram;
import kz.hapyl.spigotutils.module.math.IntInt;
import kz.hapyl.spigotutils.module.math.Numbers;
import kz.hapyl.spigotutils.module.player.PlayerLib;
import kz.hapyl.spigotutils.module.quest.PlayerQuestObjective;
import kz.hapyl.spigotutils.module.quest.QuestManager;
import kz.hapyl.spigotutils.module.quest.QuestObjectiveType;
import kz.hapyl.spigotutils.module.reflect.Reflect;
import kz.hapyl.spigotutils.module.reflect.ReflectPacket;
import kz.hapyl.spigotutils.module.util.BukkitUtils;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EnumItemSlot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nullable;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

@SuppressWarnings("unused")
public class HumanNPC implements Intractable {

	public static final String chatFormat = "&e[NPC] &a{NAME}: " + ChatColor.WHITE + "{MESSAGE}";
	public static final HashMap<Integer, HumanNPC> byId = new HashMap<>();

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

	private Location location;
	private String chatPrefix;

	private boolean persistent;
	private int farAwayDist = 40;
	private int lookAtCloseDist;

	private final HashMap<Player, Boolean> showingTo = new HashMap<>();
	private final List<NPCEntry> entries = new ArrayList<>();

	public HumanNPC() {
		this(BukkitUtils.getSpawnLocation(), "", "");
	}

	public HumanNPC(Location location, @Nullable String npcName) {
		this(location, npcName, npcName);
	}

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
		this.npcName = npcName;
		this.hexName = ("§8[NPC] " + uuid.toString().replace("-", "")).substring(0, 16);
		this.aboveHead = new Hologram().addLine(this.npcName).create(this.location.clone().subtract(0.0d, 0.51d, 0.0d));
		this.profile = new GameProfile(this.uuid, this.hexName);

		if (skinOwner != null && !skinOwner.isEmpty()) {
			this.setSkin(skinOwner);
		}

		this.human = new EntityPlayer(Reflect.getMinecraftServer(), (WorldServer)Reflect.getMinecraftWorld(location.getWorld()), profile);
		this.human.setLocation(this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch());

		this.packetAddPlayer = new ReflectPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, this.human));
		this.packetRemovePlayer = new ReflectPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, this.human));
		this.packetSpawn = new ReflectPacket(new PacketPlayOutNamedEntitySpawn(this.human));
		this.packetDestroy = new ReflectPacket(new PacketPlayOutEntityDestroy(this.human.getId()));

		byId.put(this.human.getId(), this);

	}

	public static void hideAllNames(Scoreboard score) {
		for (final HumanNPC value : byId.values()) {
			value.hideTabListName();
		}
	}

	public Set<Player> getViewers() {
		return showingTo.keySet();
	}

	public Location getLocation() {
		return location;
	}

	public boolean isShowingTo(Player player) {
		return this.showingTo.containsKey(player) && this.showingTo.get(player);
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

	private long interactionDelay = 0L;
	private final Map<UUID, Long> interactedAt = new HashMap<>();

	private String getPrefix() {
		return this.npcName == null ? "&aNPC" : this.npcName;
	}

	private String cannotInteractMessage = "Not now.";

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
				SpigotUtilsPlugin.runTaskLater((task) -> {

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

						final int needMore = (int)(obj.getCompletionGoal() - obj.getGoalCurrent());
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

	private final NPCResponse responses = new NPCResponse();

	public void setInteractDelay(Player player) {
		this.interactedAt.put(player.getUniqueId(), System.currentTimeMillis());
	}

	public boolean exists() {
		return byId.containsKey(this.getHuman().getId());
	}

	public String getName() {
		return npcName;
	}

	private boolean stopTalking = false;

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

	@DevNote(comment = "Couldn't think of a cool way of doing this so just hardcoding.")
	private String placeHold(String entry, Player player) {
		final String[] splits = entry.split(" ");
		final StringBuilder builder = new StringBuilder();
		for (String split : splits) {
			split = split
					.replace(Placeholders.PLAYER.get(), player.getName())
					.replace(Placeholders.NAME.get(), this.getName())
					.replace(Placeholders.LOCATION.get(), BukkitUtils.locationToString(this.getLocation()));
			builder.append(split);
/*			if (split.startsWith("{") && split.endsWith("}")) {
				final String between = split.substring(1, split.length() - 1);
				for (final Placeholders value : Placeholders.values()) {
					if (value.test(between)) {
						builder.append(value.)
						break;
					}
				}
			}
			else {
				builder.append(split);
			}*/
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
		this.human.setPose(pose.getNMSValue());
		this.updateDataWatcher();
		return this;
	}

	@Override
	public void onClick(Player player, HumanNPC npc, ClickType clickType) {

	}

	public void lookAt(Entity entity, Player... viewers) {
		this.location.setDirection(entity.getLocation().clone().subtract(this.location).toVector());
		this.setHeadRotation(this.location.getYaw(), viewers);
		new ReflectPacket(new PacketPlayOutEntity.PacketPlayOutEntityLook(this.human.getId(), (byte)(this.location.getYaw() * 256 / 360), (byte)(this.location
				.getPitch() * 256 / 360), true)).sendPackets(viewers);
	}

	public void teleport(double x, double y, double z, float yaw, float pitch) {
		this.location.setX(x);
		this.location.setY(y);
		this.location.setZ(z);
		this.location.setYaw(yaw);
		this.location.setPitch(pitch);
		this.setLocation(this.location);
	}

	@InsuredViewers
	public void setLocation(Location location, Player... viewers) {
		viewers = insureViewers(viewers);
		this.location = location;
		this.human.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		this.setHeadRotation(this.location.getYaw(), viewers);
		new ReflectPacket(new PacketPlayOutEntityTeleport(this.human)).sendPackets(viewers);
	}

	@InsuredViewers
	public void setHeadRotation(float yaw, Player... viewers) {
		viewers = insureViewers(viewers);
		new ReflectPacket(new PacketPlayOutEntityHeadRotation(this.human, (byte)((yaw * 256) / 360))).sendPackets(viewers);
	}

	@InsuredViewers
	public void swingMainHand(Player... v1) {
		swingArm(true, insureViewers(v1));
	}

	@InsuredViewers
	public void swingOffHand(Player... v1) {
		swingArm(false, insureViewers(v1));
	}

	private void swingArm(boolean b, Player... insured) {
		new ReflectPacket(new PacketPlayOutAnimation(this.human, b ? 0 : 3)).sendPackets(insured);

	}

	public HumanNPC setSkin(String texture, String signature) {
		this.profile.getProperties().put("textures", new Property("textures", texture, signature));
		return this;
	}

	public HumanNPC setSkin(String skinOwner) {
		final String[] data = getSkin(skinOwner);
		this.setSkin(data[0], data[1]);
		return this;
	}

	public String[] getSkin(String targetName) {
		// if player on the server just get their game profile
		if (Bukkit.getPlayer(targetName) != null) {
			try {
				final Player player = Bukkit.getPlayer(targetName);
				final Object craftPlayer = Reflect.getCraftPlayer(player);
				final GameProfile profile = (GameProfile)craftPlayer.getClass().getMethod("getProfile").invoke(craftPlayer);

				final Property textures = profile.getProperties()
						.get("textures")
						.stream()
						.findFirst()
						.orElse(new Property(targetName, targetName));
				return new String[]{textures.getValue(), textures.getSignature()};
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		// if name longer than max name than have to assume it's texture
		if (targetName.length() > 16) {
			return new String[]{"invalidValue", "invalidSignature"};
		}
		try {
			// FIXME: 029. 05/29/2021 - timeout
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
			return new String[]{property.get("value").getAsString(), property.get("signature").getAsString()};
		}
		catch (Exception error) {
			return new String[]{targetName, targetName};
		}
	}

	@InsuredViewers
	public void setItem(ItemSlot slot, ItemStack stack, Player... players) {
		players = insureViewers(players);
		final List<Pair<EnumItemSlot, net.minecraft.world.item.ItemStack>> list = new ArrayList<>();
		list.add(new Pair<>(slot.getSlot(), toNMSItemStack(stack)));
		new ReflectPacket(new PacketPlayOutEntityEquipment(this.human.getId(), list)).sendPackets(players);
	}

	public void showAll() {
		for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			this.show(onlinePlayer);
		}
	}

	public void show(final Player... players) {
		this.aboveHead.show(players);
		this.hideDisplayName(players);

		this.packetAddPlayer.sendPackets(players);
		this.packetSpawn.sendPackets(players);
		this.setLocation(this.location, players);
		this.updateSkin(players);

		for (final Player player : players) {
			this.showingTo.put(player, true);
		}

		this.human.setPose(this.human.getPose());
		this.updateDataWatcher(players);

		this.hideTabListName(players);
	}

	@InsuredViewers
	public void hide(Player... players) {
		players = insureViewers(players);

		for (final Player player : players) {
			this.showingTo.put(player, false);
		}

		this.aboveHead.hide(players);
		this.packetRemovePlayer.sendPackets(players);
		this.packetDestroy.sendPackets(players);

	}

	public HumanNPC setCollision(boolean flag) {
		for (final Player viewer : this.getViewers()) {
			final Team team = getTeamOrCreate(viewer.getScoreboard());
			team.setOption(Team.Option.COLLISION_RULE, flag ? Team.OptionStatus.ALWAYS : Team.OptionStatus.NEVER);
			team.addEntry(this.hexName);
		}
		return this;
	}


	public void setDataWatcherByteValue(int key, byte value, Player... viewers) {
		viewers = this.insureViewers(viewers);
		this.human.getDataWatcher().set(DataWatcherRegistry.a.a(key), value);
		this.updateDataWatcher(viewers);
	}

	public void remove() {
		this.hide();
		this.showingTo.clear();
		byId.remove(this.getHuman().getId());
	}

	@InsuredViewers
	public void hideTabListName(Player... players) {
		final Player[] finalPlayers = insureViewers(players);
		Bukkit.getScheduler().runTaskLater(SpigotUtilsPlugin.getPlugin(), () -> this.packetRemovePlayer.sendPackets(finalPlayers), 20);
	}

	@InsuredViewers
	public void updateSkin(Player... viewers) {
		this.human.getDataWatcher().set(DataWatcherRegistry.a.a(17), (byte)(0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40));
		this.updateDataWatcher(viewers);
	}

	public void updateDataWatcher(Player... players) {
		players = insureViewers(players);
		new ReflectPacket(new PacketPlayOutEntityMetadata(this.human.getId(), this.human.getDataWatcher(), true)).sendPackets(players);
	}

	public EntityPlayer getHuman() {
		return human;
	}

	public void hideDisplayName(Player... players) {
		for (final Player player : players) {
			final Team t = getTeamOrCreate(player.getScoreboard());
			t.addEntry(this.hexName);
		}
	}

	private net.minecraft.world.item.ItemStack toNMSItemStack(ItemStack stack) {
		return (net.minecraft.world.item.ItemStack)Reflect.invokeMethod(Reflect.getCraftMethod("inventory.CraftItemStack", "asNMSCopy", ItemStack.class), null, stack);
	}

	private Player[] insureViewers(Player... players) {
		if (players == null || players.length == 0) {
			return this.showingTo.keySet().toArray(new Player[]{});
		}
		return players;
	}

	private Team getTeamOrCreate(Scoreboard scoreboard) {
		Team team = scoreboard.getTeam("ETERNA_API");
		if (team == null) {
			team = scoreboard.registerNewTeam("ETERNA_API");
			team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
		}
		return team;
	}

	private void syncText() {
		if (this.aboveHead != null) {
			this.aboveHead.teleport(this.location.clone().add(0.0d, 1.50d, 0.0d));
		}
	}

	public static void removeAll() {
		if (!byId.isEmpty()) {
			byId.forEach((a, b) -> b.getViewers().forEach(b::hide));
		}
		byId.clear();
	}

}
