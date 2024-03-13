package bet.astral.unity.model;

import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.messenger.placeholder.Placeholderable;
import bet.astral.messenger.utils.PlaceholderUtils;
import bet.astral.unity.Factions;
import bet.astral.unity.event.ASyncPlayerKickedFromFactionEvent;
import bet.astral.unity.event.FactionEvent;
import bet.astral.unity.event.invite.ASyncInviteExpireEvent;
import bet.astral.unity.event.player.ASyncPlayerKickedFromFactionByPlayerEvent;
import bet.astral.unity.event.player.ASyncPlayerLeaveFactionEvent;
import bet.astral.unity.event.player.ASyncPlayerJoinFactionEvent;
import bet.astral.unity.event.player.change.ASyncPlayerPrefixChangeEvent;
import bet.astral.unity.event.player.invite.ASyncPlayerAcceptInviteEvent;
import bet.astral.unity.event.player.invite.ASyncPlayerCancelInviteEvent;
import bet.astral.unity.event.player.invite.ASyncPlayerDenyInviteEvent;
import bet.astral.unity.event.player.invite.ASyncPlayerSendInviteEvent;
import bet.astral.unity.utils.IdentifiedPosition;
import bet.astral.unity.utils.OfflinePlayerList;
import bet.astral.unity.utils.PlayerMap;
import bet.astral.unity.utils.TranslationKey;
import bet.astral.unity.utils.flags.Flag;
import bet.astral.unity.utils.flags.FlagImpl;
import bet.astral.unity.utils.flags.Flaggable;
import bet.astral.unity.utils.refrence.OfflinePlayerReference;
import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class Faction implements Identity, ForwardingAudience, Translatable, Flaggable, Placeholderable {
	private static final Map<FRole, FPrefix> DEFAULT_PRIVATE_PREFIXES = new HashMap<>();
	private static final Map<FRole, FPrefix> DEFAULT_PUBLIC_PREFIXES = new HashMap<>();
	static {
		DEFAULT_PUBLIC_PREFIXES.put(FRole.OWNER, new FPrefix(null, "+", NamedTextColor.RED));
		DEFAULT_PUBLIC_PREFIXES.put(FRole.ADMIN, new FPrefix(null, "**", NamedTextColor.GOLD));
		DEFAULT_PUBLIC_PREFIXES.put(FRole.MODERATOR, new FPrefix(null, "*", NamedTextColor.YELLOW));
		DEFAULT_PUBLIC_PREFIXES.put(FRole.DEFAULT, new FPrefix(null, "", NamedTextColor.GREEN));

		DEFAULT_PRIVATE_PREFIXES.put(FRole.OWNER, new FPrefix(null, Component.text("Owner ", NamedTextColor.RED, TextDecoration.BOLD).append(Component.text().decoration(TextDecoration.BOLD, false))));
		DEFAULT_PRIVATE_PREFIXES.put(FRole.ADMIN, new FPrefix(null, Component.text("Admin ", NamedTextColor.GOLD, TextDecoration.BOLD).append(Component.text().decoration(TextDecoration.BOLD, false))));
		DEFAULT_PRIVATE_PREFIXES.put(FRole.MODERATOR, new FPrefix(null, Component.text("Mod ", NamedTextColor.YELLOW, TextDecoration.BOLD).append(Component.text().decoration(TextDecoration.BOLD, false))));
		DEFAULT_PRIVATE_PREFIXES.put(FRole.DEFAULT, new FPrefix(null, Component.text("", NamedTextColor.GREEN).append(Component.text().decoration(TextDecoration.BOLD, false))));
	}
	public static PlaceholderList factionPlaceholders(@Nullable String prefix, @NotNull Faction faction){
		PlaceholderList placeholders = new PlaceholderList();
		if (prefix != null){
			placeholders.add(PlaceholderUtils.createPlaceholder(null, prefix, faction));
		}
		placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "name", faction.name));
		placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "displayname", faction.displayname));
		placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "customname", faction.displayname));
		placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "description", faction.description));
		if (faction.home != null) {
			placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "base_name", faction.home.getName()));
			placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "base_id", faction.home.getUniqueId()));
			placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "base_x", faction.home.getX()));
			placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "base_y", faction.home.getY()));
			placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "base_z", faction.home.getZ()));
			placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "base_pitch", faction.home.getPitch()));
			placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "base_home", faction.home.getWorld()));
			placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "base_home", faction.home.getWorld()));
		}


		return placeholders;
	}
	//
	private final Factions factions;
	// Members
	private final OfflinePlayerList members = new OfflinePlayerList();
	// Invites
	@Setter(AccessLevel.NONE)
	private final PlayerMap<FInvite> invites = new PlayerMap<>();
	// Roles
	@Setter(AccessLevel.NONE)
	private final PlayerMap<FRole> roles = new PlayerMap<>();
	// Prefixes
	@Getter(AccessLevel.NONE)
	private final Map<FRole, FPrefix> publicRolePrefixes = new HashMap<>();
	@Getter(AccessLevel.NONE)
	private final Map<FRole, FPrefix> rolePrefixes = new HashMap<>();
	@Getter(AccessLevel.NONE)
	private final PlayerMap<FPrefix> playerPrefixes = new PlayerMap<>();
	// Flags
	@Setter(AccessLevel.NONE)
	private final Map<NamespacedKey, Flag<?>> flags = new HashMap<>();
	private UUID superOwner;
	private final UUID uniqueId;
	private final long firstCreated;
	@Setter(AccessLevel.NONE)
	private String name;
	@Setter(AccessLevel.NONE)
	private Component displayname;
	private Component description;
	private Component joinInfo;
	private IdentifiedPosition home = null;
	private boolean isPublic = false;
	{
		publicRolePrefixes.put(FRole.OWNER, new FPrefix(this, DEFAULT_PUBLIC_PREFIXES.get(FRole.OWNER)));
		publicRolePrefixes.put(FRole.ADMIN, new FPrefix(this, DEFAULT_PUBLIC_PREFIXES.get(FRole.ADMIN)));
		publicRolePrefixes.put(FRole.MODERATOR, new FPrefix(this, DEFAULT_PUBLIC_PREFIXES.get(FRole.MODERATOR)));
		publicRolePrefixes.put(FRole.DEFAULT, new FPrefix(this, DEFAULT_PUBLIC_PREFIXES.get(FRole.DEFAULT)));

		rolePrefixes.put(FRole.OWNER, new FPrefix(this, DEFAULT_PRIVATE_PREFIXES.get(FRole.OWNER)));
		rolePrefixes.put(FRole.ADMIN, new FPrefix(this, DEFAULT_PRIVATE_PREFIXES.get(FRole.ADMIN)));
		rolePrefixes.put(FRole.MODERATOR, new FPrefix(this, DEFAULT_PRIVATE_PREFIXES.get(FRole.MODERATOR)));
		rolePrefixes.put(FRole.DEFAULT, new FPrefix(this, DEFAULT_PRIVATE_PREFIXES.get(FRole.DEFAULT)));
	}

	public Faction(Factions factions, UUID uniqueId, String name, long firstCreated){
		this.factions = factions;
		this.name = name.toLowerCase();
//		this.displayname = Component.text("["+name+"]", NamedTextColor.GOLD);
		this.displayname = Component.text(name, NamedTextColor.GOLD);
		this.uniqueId = uniqueId;
		this.firstCreated = firstCreated;
//		Message defaultDescription = factions.messenger().getMessage(TranslationKey.DEFAULT_FACTION_DESCRIPTION);
//		Message defaultJoin = factions.messenger().getMessage(TranslationKey.DEFAULT_FACTION_JOIN_INFO);
		PlaceholderList placeholders = new PlaceholderList();
		placeholders.add("name", name);
		placeholders.add("displayname", name);
		placeholders.add("faction", name);
		placeholders.add("id", uniqueId.toString());
//		this.description = factions.messenger().parse(defaultDescription, Message.Type.CHAT, placeholders);
//		this.joinInfo = factions.messenger().parse(defaultJoin, Message.Type.CHAT, placeholders);
		this.description = Component.text("Default description to factions");
		this.joinInfo = Component.text("Message the owner to join this faction!");
	}

	public Faction(Factions factions, java.util.UUID uniqueId, long firstCreated, String name, Component displayname, Component description, Component joinInfo, IdentifiedPosition home) {
		this.factions = factions;
		this.uniqueId = uniqueId;
		this.firstCreated = firstCreated;
		this.name = name;
		this.displayname = displayname;
		this.description = description;
		this.joinInfo = joinInfo;
		this.home = home;
	}

	private void requestSave(){
		factions.getFactionManager().requestSave(this);
	}


	@Override
	public @NotNull String translationKey() {
		return "unity.faction";
	}

	@Override
	public @NotNull Iterable<? extends Audience> audiences() {
		return members.audiences();
	}

	@Override
	public java.util.@NotNull UUID uuid() {
		return uniqueId;
	}

	@Override
	public <V> void addFlag(@NotNull Flag<V> flag) {
		flags.put(flag.getKey(), flag);
	}

	@Override
	public <V> void editFlag(@NotNull NamespacedKey key, @Nullable V newValue) throws IllegalStateException {
		if (flags.get(key) != null){
			//noinspection unchecked
			Flag<V> flag = (Flag<V>) flags.get(key);
			assert newValue != null;
			flag.setValue(newValue);
			return;
		}
		throw new IllegalStateException("Couldn't edit a flag which is not set!");
	}

	@Override
	public <V> void setIfAbsent(@NotNull Flag<V> flag) {
		flags.putIfAbsent(flag.getKey(), flag);
	}

	@Override
	public <V> void setIfAbsent(@NotNull NamespacedKey key, @Nullable V defaultValue) {
		flags.putIfAbsent(key, new FlagImpl<>(key, defaultValue, defaultValue));
	}

	@Override
	public <V> void setIfAbsent(@NotNull NamespacedKey key, @Nullable V defaultValue, @Nullable V currentValue) {
		flags.putIfAbsent(key, new FlagImpl<>(key, defaultValue, currentValue));
	}

	@Override
	public @NotNull <V> Flag<V> getFlag(@NotNull NamespacedKey key, @NotNull Flag<V> defaultFlag) {
		return getFlag(key) != null ? Objects.requireNonNull(getFlag(key)) : defaultFlag;
	}

	@Override
	public @Nullable <V> Flag<V> getFlag(@NotNull NamespacedKey key) {
		//noinspection unchecked
		return (Flag<V>) flags.get(key);
	}


	public FInvite getInvite(@NotNull OfflinePlayer player){
		return invites.get(player);
	}

	public FInvite getInvite(@NotNull UUID invited){
		return invites.get(invited);
	}

	public FInvite getInvite(@NotNull OfflinePlayerReference playerReference){
		return invites.get(playerReference.uuid());
	}

	public boolean isInvited(@NotNull OfflinePlayer player){
		return invites.get(player.getUniqueId()) != null;
	}
	public boolean isInvited(@NotNull OfflinePlayerReference player){
		return invites.get(player.offlinePlayer().getUniqueId()) != null;
	}
	public boolean isInvited(UUID player){
		return invites.get(player) != null;
	}

	public boolean invite(@NotNull Player from, @NotNull Player to, boolean forceInvite){
		FInvite invite = new FInvite(
				this,
				from,
				to,
				30*1000,
				forceInvite,
				factions.getServer().getAsyncScheduler().runDelayed(factions, (task)->{
					FInvite inv = this.invites.get(to);
					ASyncInviteExpireEvent event = new ASyncInviteExpireEvent(this, to, inv.getFrom());
					event.callEvent();

					this.invites.remove(to);
					PlaceholderList placeholders = new PlaceholderList();
					placeholders.addAll(factionPlaceholders("faction", this));
					placeholders.add("name", name);
					placeholders.add("displayname", name);
					placeholders.add("to", inv.getTo().offlinePlayer().getName());
					placeholders.add("from", inv.getFrom().offlinePlayer().getName());
					factions.messenger().message(this, TranslationKey.MESSAGE_INVITE_EXPIRED_FACTION, placeholders);
					if (inv.getTo().player() != null){
						factions.messenger().message(inv.getTo().player(), TranslationKey.MESSAGE_INVITE_EXPIRED, placeholders);
					}
				}, 30, TimeUnit.SECONDS));
		ASyncPlayerSendInviteEvent sendInviteEvent = new ASyncPlayerSendInviteEvent(this, from, to, invite, forceInvite ? FactionEvent.Cause.FORCE : FactionEvent.Cause.PLAYER);
		if (!sendInviteEvent.callEvent()){
			invite.getTask().cancel();
			return false;
		}
		invites.put(to.getUniqueId(), invite);
		return true;
	}

	public boolean acceptInvite(@NotNull Player to){
		FInvite invite = invites.get(to);
		if (invite == null){
			return false;
		}
		ASyncPlayerAcceptInviteEvent event = new ASyncPlayerAcceptInviteEvent(this, to, invite.getFrom());
		if (!event.callEvent()){
			return false;
		}
		invites.remove(to);
		if (!invite.getTask().isCancelled()){
			invite.getTask().cancel();
		}
		return join(to, FactionEvent.Cause.PLAYER);
	}

	public void denyInvite(@NotNull Player to){
		FInvite invite = invites.get(to);
		if (invite == null){
			return;
		}

		ASyncPlayerDenyInviteEvent inviteEvent = new ASyncPlayerDenyInviteEvent(this, to, invite.getFrom());
		inviteEvent.callEvent();

		invites.remove(to);
		if (!invite.getTask().isCancelled()) {
			invite.getTask().cancel();
		}
	}

	public void cancelInvite(@NotNull Player from, @NotNull OfflinePlayer to, @Nullable String reason){
		FInvite invite  = invites.get(to);
		if (invite == null){
			return;
		}

		ASyncPlayerCancelInviteEvent event = new ASyncPlayerCancelInviteEvent(this, to, from, reason);
		event.callEvent();

		invites.remove(to);
		if (!invite.getTask().isCancelled()) {
			invite.getTask().cancel();
		}
	}



	public boolean join(@NotNull Player player, FactionEvent.Cause cause){
		ASyncPlayerJoinFactionEvent event = new ASyncPlayerJoinFactionEvent(this, player, cause);
		if (!event.callEvent() && cause != FactionEvent.Cause.FORCE){
			return false;
		}
		members.add(player);
		roles.put(player.getUniqueId(), FRole.DEFAULT);
		requestSave();
		return true;
	}

	public boolean isBanned(@NotNull OfflinePlayerReference player) {
		return isBanned(player.uuid());
	}
	public boolean isBanned(@NotNull OfflinePlayer player) {
		return isBanned(player.getUniqueId());
	}
	public boolean isBanned(@NotNull UUID uuid) {
		return false;
	}


	public void setRole(UUID uuid, FRole role){
		this.roles.put(uuid, role);
	}
	public void setRole(OfflinePlayer uuid, FRole role){
		this.roles.put(uuid, role);
	}
	public void setRole(OfflinePlayerReference uuid, FRole role){
		this.roles.put(uuid.offlinePlayer(), role);
	}

	public FRole getRole(UUID uuid){
		return this.roles.get(uuid);
	}
	public FRole getRole(OfflinePlayer uuid){
		return this.roles.get(uuid);
	}
	public FRole getRole(OfflinePlayerReference uuid){
		return this.roles.get(uuid.offlinePlayer());
	}


	public void leave(Player sender) {
		ASyncPlayerLeaveFactionEvent event = new ASyncPlayerLeaveFactionEvent(this, sender);
		event.callEvent();

		members.remove(sender.getUniqueId());
		roles.remove(sender.getUniqueId());
		if (sender.getUniqueId().equals(superOwner)){
			setSuperOwner(null);
		}
	}

	public void kick(OfflinePlayer member, String reason) {
		ASyncPlayerKickedFromFactionEvent event = new ASyncPlayerKickedFromFactionEvent(this, FactionEvent.Cause.PLUGIN, member, reason);
		event.callEvent();

		FPlayer player = factions.getPlayerManager().get(member.getUniqueId());
		if (player == null){
			player = new FPlayer(factions, member.getUniqueId(), member.getUniqueId().toString());
		}
		factions.getPlayerDatabase().delete(player);
		player.setFactionId(null);


		members.remove(member.getUniqueId());
		roles.remove(member.getUniqueId());
		if (member.getUniqueId().equals(superOwner)){
			setSuperOwner(null);
		}
	}

	public void kick(Player whoKicked, OfflinePlayer member, String reason, boolean force){
		ASyncPlayerKickedFromFactionByPlayerEvent event = new ASyncPlayerKickedFromFactionByPlayerEvent(this, force ? FactionEvent.Cause.FORCE : FactionEvent.Cause.PLAYER, member, whoKicked, reason);
		event.callEvent();

		FPlayer player = factions.getPlayerManager().get(member.getUniqueId());
		if (player == null){
			player = new FPlayer(factions, member.getUniqueId(), member.getUniqueId().toString());
		}
		factions.getPlayerDatabase().delete(player);
		player.setFactionId(null);


		members.remove(member.getUniqueId());
		roles.remove(member.getUniqueId());
		if (member.getUniqueId().equals(superOwner)){
			setSuperOwner(null);
		}
	}

	public void setPublicPrefix(FRole role, Component prefix){
		if (prefix == null){
			prefix = Component.empty();
		}
		FPrefix fPrefix = new FPrefix(this, prefix.color(publicRolePrefixes.get(role).getPrefix().color()));
		publicRolePrefixes.put(role, fPrefix);
	}

	public FPrefix getPublicPrefix(FRole role){
		return publicRolePrefixes.get(role);
	}

	public void setPrivatePrefix(FRole role, Component prefix){
		if (prefix == null){
			prefix = Component.empty();
		}
		FPrefix fPrefix = rolePrefixes.get(role);
		fPrefix.setPrefix(prefix);
	}

	public void setPrivatePrefix(OfflinePlayer player, Component prefix, FactionEvent.Cause cause){
		if (prefix == null){
			resetPrefix(player);
			return;
		}
		FPrefix before = getPrivatePrefix(player);
		FPrefix after = new FPrefix(this, prefix);

		ASyncPlayerPrefixChangeEvent event =
				new ASyncPlayerPrefixChangeEvent(before, after, player, cause);
		event.callEvent();


		playerPrefixes.put(player, after);
	}
	public void resetPrefix(OfflinePlayer player){
		playerPrefixes.remove(player);
	}

	public FPrefix getPrivatePrefix(FRole role){
		return rolePrefixes.get(role);
	}

	public FPrefix getPrivatePrefix(OfflinePlayer player){
		FPrefix prefix = playerPrefixes.get(player);
		if (prefix == null) {
			FRole role = roles.get(player);
			if (role == null) {
				role = FRole.DEFAULT;
			}
			prefix = rolePrefixes.get(role);
			if (prefix == null){
				prefix = new FPrefix(this, Component.empty());
				rolePrefixes.put(role, prefix);
			}
		}
		return prefix;
	}

	/**
	 * Returns an immutable list of members.
	 * @return members
	 */
	public List<java.util.UUID> getMembers() {
		return ImmutableList.copyOf(new OfflinePlayerList(members));
	}

	@Override
	public Collection<Placeholder> asPlaceholder(String prefix) {
		return factionPlaceholders(prefix, this);
	}
}
