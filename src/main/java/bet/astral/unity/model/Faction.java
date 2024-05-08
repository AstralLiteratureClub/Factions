package bet.astral.unity.model;

import bet.astral.annotations.Heavy;
import bet.astral.messenger.Messenger;
import bet.astral.messenger.adventure.MessengerAudience;
import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.messenger.placeholder.Placeholderable;
import bet.astral.shine.ShineHandler;
import bet.astral.shine.model.ShineReceiver;
import bet.astral.unity.Factions;
import bet.astral.unity.annotations.DoNotSave;
import bet.astral.unity.database.model.DBFactionMember;
import bet.astral.unity.event.ASyncPlayerKickedFromFactionEvent;
import bet.astral.unity.event.FactionEvent;
import bet.astral.unity.event.interactions.RelationshipStatusChangeEvent;
import bet.astral.unity.event.invite.ASyncInviteExpireEvent;
import bet.astral.unity.event.player.ASyncPlayerKickedFromFactionByPlayerEvent;
import bet.astral.unity.event.player.ASyncPlayerLeaveFactionEvent;
import bet.astral.unity.event.player.ASyncPlayerJoinFactionEvent;
import bet.astral.unity.event.player.change.ASyncPlayerPrefixChangeEvent;
import bet.astral.unity.event.player.invite.ASyncPlayerAcceptInviteEvent;
import bet.astral.unity.event.player.invite.ASyncPlayerCancelInviteEvent;
import bet.astral.unity.event.player.invite.ASyncPlayerDenyInviteEvent;
import bet.astral.unity.event.player.invite.ASyncPlayerSendInviteEvent;
import bet.astral.unity.event.teams.FactionDisableEvent;
import bet.astral.unity.event.teams.FactionEnableEvent;
import bet.astral.unity.messenger.FactionPlaceholderManager;
import bet.astral.unity.messenger.TranslationKeys;
import bet.astral.unity.model.interactions.FAlliable;
import bet.astral.unity.model.interactions.FAntagonist;
import bet.astral.unity.model.interactions.FRelationship;
import bet.astral.unity.model.interactions.FRelationshipInfo;
import bet.astral.unity.model.interactions.FRelationshipStatus;
import bet.astral.unity.model.interactions.FTruce;
import bet.astral.unity.model.location.FHome;
import bet.astral.unity.model.minecraft.Team;
import bet.astral.unity.utils.UniqueId;
import bet.astral.unity.utils.collections.OfflinePlayerList;
import bet.astral.unity.utils.collections.PlayerMap;
import bet.astral.unity.utils.collections.PrefixMap;
import bet.astral.unity.utils.collections.UniqueMap;
import bet.astral.unity.utils.flags.Flag;
import bet.astral.unity.utils.flags.FlagImpl;
import bet.astral.unity.utils.flags.Flaggable;
import bet.astral.unity.utils.refrence.FactionReference;
import bet.astral.unity.utils.refrence.OfflinePlayerReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.incendo.cloud.permission.Permission;
import org.incendo.cloud.permission.PredicatePermission;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Getter
@Setter
public final class Faction extends Team
		implements Identity, ForwardingAudience, Translatable,
		Flaggable, Placeholderable, UniqueId,
		FactionReference, MessengerAudience<Factions>,
		FRelationship<Faction>, FAlliable<Faction>, FAntagonist<Faction>, FTruce<Faction>,
		ShineReceiver
{
	private static final Map<FRole, FPrefix> DEFAULT_PRIVATE_PREFIXES = new HashMap<>();
	private static final Map<FRole, FPrefix> DEFAULT_PUBLIC_PREFIXES = new HashMap<>();

	static {
		DEFAULT_PUBLIC_PREFIXES.put(FRole.OWNER, new FPrefix(null, Component.text("*")));
		DEFAULT_PUBLIC_PREFIXES.put(FRole.CO_OWNER, new FPrefix(null, Component.text("*")));
		DEFAULT_PUBLIC_PREFIXES.put(FRole.ADMIN, new FPrefix(null, Component.text("+")));
		DEFAULT_PUBLIC_PREFIXES.put(FRole.MODERATOR, new FPrefix(null, Component.text("~")));
		DEFAULT_PUBLIC_PREFIXES.put(FRole.MEMBER, new FPrefix(null, Component.text("-")));
		DEFAULT_PUBLIC_PREFIXES.put(FRole.GUEST, new FPrefix(null, Component.text("")));

		DEFAULT_PRIVATE_PREFIXES.put(FRole.OWNER, new FPrefix(null, Component.text("Owner ", NamedTextColor.RED, TextDecoration.BOLD).append(Component.text().decoration(TextDecoration.BOLD, false))));
		DEFAULT_PRIVATE_PREFIXES.put(FRole.CO_OWNER, new FPrefix(null, Component.text("Co-Owner ", NamedTextColor.RED, TextDecoration.BOLD).append(Component.text().decoration(TextDecoration.BOLD, false))));
		DEFAULT_PRIVATE_PREFIXES.put(FRole.ADMIN, new FPrefix(null, Component.text("Admin ", NamedTextColor.GOLD, TextDecoration.BOLD).append(Component.text().decoration(TextDecoration.BOLD, false))));
		DEFAULT_PRIVATE_PREFIXES.put(FRole.MODERATOR, new FPrefix(null, Component.text("Mod ", NamedTextColor.YELLOW, TextDecoration.BOLD).append(Component.text().decoration(TextDecoration.BOLD, false))));
		DEFAULT_PRIVATE_PREFIXES.put(FRole.MEMBER, new FPrefix(null, Component.text("Member ", NamedTextColor.GREEN).append(Component.text().decoration(TextDecoration.BOLD, false))));
		DEFAULT_PRIVATE_PREFIXES.put(FRole.GUEST, new FPrefix(null, Component.text("", NamedTextColor.GRAY).append(Component.text().decoration(TextDecoration.BOLD, false))));
	}
	@Deprecated(forRemoval = true)
	public static PlaceholderList factionPlaceholders(@Nullable String prefix, @NotNull Faction faction){
		return ((FactionPlaceholderManager)faction.getFactions().getMessenger().getPlaceholderManager()).factionPlaceholders(prefix, faction);
	}
	//
	private final Factions factions;
	// Members
	private final OfflinePlayerList members = new OfflinePlayerList();
	// Invites
	@Setter(AccessLevel.NONE)
	@DoNotSave
	private final PlayerMap<FInvite> invites = new PlayerMap<>();
	// Roles
	@Setter(AccessLevel.NONE)
	private final PlayerMap<FRole> roles = new PlayerMap<>();
	// Prefixes
	@Getter(AccessLevel.NONE)
	private final PrefixMap publicRolePrefixes = new PrefixMap();
	@Getter(AccessLevel.NONE)
	private final PrefixMap rolePrefixes = new PrefixMap();
	@Getter(AccessLevel.NONE)
	private final PlayerMap<FPrefix> playerPrefixes = new PlayerMap<>();

	// Relationships
	private final UniqueMap<FAlliable<?>> alliances = new UniqueMap<>();
	private final UniqueMap<FAntagonist<?>> enemies = new UniqueMap<>();
	private final UniqueMap<FTruce<?>> truces = new UniqueMap<>();

	// Tag Color
	private final TextColor tagColor = NamedTextColor.GRAY;
	private final PlayerMap<TextColor> playerTagColors = new PlayerMap<>();

	// Flags
	@Setter(AccessLevel.NONE)
	@DoNotSave
	private final Map<NamespacedKey, Flag<?>> flags = new HashMap<>();
	private final OfflinePlayerList banned = new OfflinePlayerList();
	private final UUID uniqueId;
	private final long firstCreated;
	@Setter(AccessLevel.NONE)
	private String name;
	@Setter(AccessLevel.NONE)
	private Component displayname;
	private Component description;
	private Component joinInfo;
	// Store homes in their own database
	private Map<String, FHome> homesByName;
	private Map<UUID, FHome> homesById;
	private boolean isPublic = false;
	{
		publicRolePrefixes.put(FRole.OWNER, new FPrefix(this, DEFAULT_PUBLIC_PREFIXES.get(FRole.OWNER)));
		publicRolePrefixes.put(FRole.ADMIN, new FPrefix(this, DEFAULT_PUBLIC_PREFIXES.get(FRole.ADMIN)));
		publicRolePrefixes.put(FRole.MODERATOR, new FPrefix(this, DEFAULT_PUBLIC_PREFIXES.get(FRole.MODERATOR)));
		publicRolePrefixes.put(FRole.MEMBER, new FPrefix(this, DEFAULT_PUBLIC_PREFIXES.get(FRole.MEMBER)));

		rolePrefixes.put(FRole.OWNER, new FPrefix(this, DEFAULT_PRIVATE_PREFIXES.get(FRole.OWNER)));
		rolePrefixes.put(FRole.ADMIN, new FPrefix(this, DEFAULT_PRIVATE_PREFIXES.get(FRole.ADMIN)));
		rolePrefixes.put(FRole.MODERATOR, new FPrefix(this, DEFAULT_PRIVATE_PREFIXES.get(FRole.MODERATOR)));
		rolePrefixes.put(FRole.MEMBER, new FPrefix(this, DEFAULT_PRIVATE_PREFIXES.get(FRole.MEMBER)));
	}

	public Faction(Factions factions, UUID uniqueId, String name, long firstCreated){
		this.factions = factions;
		this.name = name;
		this.name = this.name.toLowerCase();
		this.displayname = Component.text(name);
		this.uniqueId = uniqueId;
		this.firstCreated = firstCreated;
		this.description = Component.text("Default description to factions");
		this.joinInfo = Component.text("Message the owner to join this faction!");
		homesByName = new HashMap<>();
		homesById = new HashMap<>();
	}

	public Faction(Factions factions, java.util.UUID uniqueId, long firstCreated, String name, Component displayname, Component description, Component joinInfo, PrefixMap privatePrefixes, PrefixMap publicPrefixes) {
		this.factions = factions;
		this.uniqueId = uniqueId;
		this.firstCreated = firstCreated;
		this.name = name;
		this.displayname = displayname;
		this.description = description;
		this.joinInfo = joinInfo;
		this.homesByName = new HashMap<>();
		this.homesById = new HashMap<>();
		this.publicRolePrefixes.putAll(publicPrefixes);
		this.rolePrefixes.putAll(privatePrefixes);
	}

	@Override
	public void enable() {
		super.enable();
		for (OfflinePlayerReference reference : members.toUnmodifiableReferenceList()){
			super.addPlayer(reference);
		}

		FactionEnableEvent event = new FactionEnableEvent(!Bukkit.isPrimaryThread(), this);
		event.callEvent();
	}

	@Override
	public void disable() {
		super.disable();
		FactionDisableEvent event = new FactionDisableEvent(!Bukkit.isPrimaryThread(), this);
		event.callEvent();
	}

	public void requestSave(){
		factions.getFactionManager().requestSave(this);
	}

	public Permission notInThisPermission(){
		return PredicatePermission.of((sender)->{
			if (!(sender instanceof Player player)){
				return true;
			}
			FPlayer fPlayer = factions.getPlayerManager().convert(player);
			return fPlayer.getFaction() == null || !fPlayer.getFaction().equals(this);
		});
	}
	public Permission inThisPermission(){
		return PredicatePermission.of((sender)->{
			if (!(sender instanceof Player player)){
				return false;
			}
			FPlayer fPlayer = factions.getPlayerManager().convert(player);
			return fPlayer.getFaction() != null && fPlayer.getFaction().equals(this);
		});
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
					factions.messenger().message(this, TranslationKeys.MESSAGE_INVITE_EXPIRED_FACTION, placeholders);
					if (inv.getTo().player() != null){
						factions.messenger().message(inv.getTo().player(), TranslationKeys.MESSAGE_INVITE_EXPIRED, placeholders);
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


	@ApiStatus.Internal
	public void addMember(@NotNull DBFactionMember member) {
		members.add(member.getUniqueId());
		roles.put(member.getUniqueId(), member.getRole());
		if (member.getPrefix() != null){
			playerPrefixes.put(member.getUniqueId(), new FPrefix(this, member.getPrefix()));
		}
	}

	@ApiStatus.Internal
	public void joinAsOwner(@NotNull Player player){
		members.add(player);
		roles.put(player.getUniqueId(), FRole.OWNER);
	}

	public boolean join(@NotNull Player player, FactionEvent.Cause cause){
		ASyncPlayerJoinFactionEvent event = new ASyncPlayerJoinFactionEvent(this, player, cause);
		if (!event.callEvent() && cause != FactionEvent.Cause.FORCE){
			return false;
		}
		members.add(player);
		roles.put(player.getUniqueId(), FRole.GUEST);

		FPlayer fPlayer = factions.getPlayerManager().convert(player);
		fPlayer.setFaction(this);

		requestSave();
		return true;
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public boolean isBanned(@NotNull OfflinePlayerReference player) {
		return isBanned(player.uuid());
	}
	public boolean isBanned(@NotNull OfflinePlayer player) {
		return isBanned(player.getUniqueId());
	}
	public boolean isBanned(@NotNull UUID uuid) {
		return banned.contains(uuid);
	}


	public void setRole(@NotNull UUID uuid, @NotNull FRole role){
		this.roles.put(uuid, role);
	}
	public void setRole(@NotNull OfflinePlayer uuid, @NotNull FRole role){
		this.roles.put(uuid, role);
	}
	public void setRole(@NotNull OfflinePlayerReference uuid, @NotNull FRole role){
		this.roles.put(uuid.offlinePlayer(), role);
	}

	public FRole getRole(@NotNull UUID uuid){
		return this.roles.get(uuid);
	}
	public FRole getRole(@NotNull OfflinePlayer uuid){
		return this.roles.get(uuid);
	}
	public FRole getRole(@NotNull OfflinePlayerReference uuid){
		return this.roles.get(uuid.offlinePlayer());
	}


	public void leave(@NotNull Player sender) {
		ASyncPlayerLeaveFactionEvent event = new ASyncPlayerLeaveFactionEvent(this, sender);
		event.callEvent();

		if (isOwner(getFactionId())){
			factions.getLogger().severe("Faction " + getFactionIdString() + " ("+ getName() + ") does not have a owner as they have left the faction! This should not be happening unless the player has used force commands!");
		}
		members.remove(sender.getUniqueId());
		roles.remove(sender.getUniqueId());
		factions.getDatabase().getMemberDatabase().delete(sender);
	}

	public void kick(@NotNull OfflinePlayer member, @NotNull String reason) {
		ASyncPlayerKickedFromFactionEvent event = new ASyncPlayerKickedFromFactionEvent(this, FactionEvent.Cause.PLUGIN, member, reason);
		event.callEvent();

		FPlayer player = factions.getPlayerManager().get(member.getUniqueId());
		if (player == null){
			player = new FPlayer(factions, member.getUniqueId(), member.getUniqueId().toString());
		}
		factions.getDatabase().getMemberDatabase().delete(player);
		player.setFactionId(null);


		members.remove(member.getUniqueId());
		roles.remove(member.getUniqueId());
	}

	public void kick(@NotNull Player whoKicked, @NotNull OfflinePlayer member, @NotNull String reason, boolean force){
		ASyncPlayerKickedFromFactionByPlayerEvent event = new ASyncPlayerKickedFromFactionByPlayerEvent(this, force ? FactionEvent.Cause.FORCE : FactionEvent.Cause.PLAYER, member, whoKicked, reason);
		event.callEvent();

		FPlayer player = factions.getPlayerManager().get(member.getUniqueId());
		if (player == null){
			player = new FPlayer(factions, member.getUniqueId(), member.getUniqueId().toString());
		}
		factions.getDatabase().getMemberDatabase().delete(player);
		player.setFactionId(null);


		members.remove(member.getUniqueId());
		roles.remove(member.getUniqueId());
	}

	public void setPublicPrefix(@NotNull FRole role, @Nullable Component prefix){
		if (prefix == null){
			prefix = Component.empty();
		}
		FPrefix fPrefix = new FPrefix(this, prefix.color(publicRolePrefixes.get(role).getPrefix().color()));
		publicRolePrefixes.put(role, fPrefix);
	}

	public FPrefix getPublicPrefix(@NotNull FRole role){
		return publicRolePrefixes.get(role);
	}

	public void setPrivatePrefix(@NotNull FRole role, @Nullable Component prefix){
		if (prefix == null){
			prefix = Component.empty();
		}
		FPrefix fPrefix = rolePrefixes.get(role);
		fPrefix.setPrefix(prefix);
	}

	public void setPrivatePrefix(@NotNull OfflinePlayer player, @Nullable Component prefix, @NotNull FactionEvent.Cause cause){
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

	public void resetPrivatePrefix(@NotNull FRole role){
		this.rolePrefixes.put(role, DEFAULT_PRIVATE_PREFIXES.get(role));
	}
	public void resetPublicPrefix(@NotNull FRole role){
		this.publicRolePrefixes.put(role, DEFAULT_PUBLIC_PREFIXES.get(role));
	}
	public void resetPrefix(@NotNull OfflinePlayer player){
		this.playerPrefixes.remove(player);
	}

	public void resetPrefix(@NotNull UUID uniqueId){
		this.playerPrefixes.remove(uniqueId);
	}

	public FPrefix getPrivatePrefix(@NotNull FRole role){
		return rolePrefixes.get(role);
	}

	public FPrefix getPrivatePrefix(@NotNull OfflinePlayerReference reference){
		return getPrivatePrefix(reference.getUniqueId());
	}
	public FPrefix getPrivatePrefix(@NotNull OfflinePlayer player){
		return getPrivatePrefix(player.getUniqueId());
	}
	public FPrefix getPrivatePrefix(@NotNull UUID uniqueId){
		FPrefix prefix = playerPrefixes.get(uniqueId);
		if (prefix == null) {
			FRole role = roles.get(uniqueId);
			if (role == null) {
				role = FRole.MEMBER;
			}
			prefix = rolePrefixes.get(role);
			if (prefix == null){
				prefix = new FPrefix(this, Component.empty());
				rolePrefixes.put(role, prefix);
			}
		}
		return prefix;
	}


	public boolean hasPrivatePrefix(@NotNull FPlayer player) {
		return playerPrefixes.containsKey(player.getUniqueId());
	}
	public boolean hasPrivatePrefix(@NotNull UUID uniqueId){
		return playerPrefixes.containsKey(uniqueId);
	}
	public boolean hasPrivatePrefix(@NotNull OfflinePlayerReference value) {
		return playerPrefixes.containsKey(value.uuid());
	}

	@NotNull
	@ApiStatus.Internal
	public PrefixMap getPublicRolePrefixes(){
		return publicRolePrefixes;
	}
	@NotNull
	@ApiStatus.Internal
	public PrefixMap getPrivateRolePrefixes(){
		return rolePrefixes;
	}

	/**
	 * Returns an immutable list of members.
	 * @return members
	 */
	@NotNull
	public OfflinePlayerList getMembers() {
		return new OfflinePlayerList(members);
	}
	@NotNull
	public OfflinePlayerList getMembersWithRole(@NotNull FRole role){
		return new OfflinePlayerList(roles.keySet()
				.stream().filter(ref->roles.get(ref)==role).collect(Collectors.toList()));
	}

	public boolean isOwner(OfflinePlayer player) {
		return isOwner(player.getUniqueId());
	}
	public boolean isOwner(OfflinePlayerReference reference) {
		return isOwner(reference.getUniqueId());
	}
	public boolean isOwner(UUID id) {
		FRole role = getRole(id);
		if (role == null){
			return false;
		}
		return role.isOwner();
	}

	@Override
	@NotNull
	public Collection<Placeholder> asPlaceholder(String prefix) {
		return ((FactionPlaceholderManager) messenger().getPlaceholderManager()).factionPlaceholders(prefix, this);
	}

	public boolean allowsOwnerKicking() {
		if (members.size()>10){
			return true;
		}
		return members.stream().anyMatch(member -> getRole(member).isOwner());
	}

	@Override
	@NotNull
	public Faction getFaction() {
		return this;
	}

	@Override
	@NotNull
	public java.util.UUID getFactionId() {
		return uniqueId;
	}

	@Override
	@NotNull
	public Messenger<Factions> messenger() {
		return factions.messenger();
	}


	public void addHome(@NotNull FHome home) {
		homesById.put(home.getUniqueId(), home);
		homesByName.put(home.getName().toLowerCase(), home);
	}

	public void removeHome(@NotNull FHome home) {
		homesById.remove(home.getUniqueId());
		homesByName.remove(home.getName().toLowerCase());
	}

	@Nullable
	public FHome getHome(@NotNull UUID uniqueId){
		return homesById.get(uniqueId);
	}
	@Nullable
	public FHome getHome(@NotNull String name){
		return homesByName.get(name);
	}


	@Override
	public FEntityType.FactionType getEntityType() {
		return FEntityType.FACTION;
	}

	private void clearRelationshipStatus(UniqueId uniqueId){
		alliances.remove(uniqueId.getUniqueId());
		enemies.remove(uniqueId.getUniqueId());
		truces.remove(uniqueId.getUniqueId());
	}
	private void setRelationship(FRelationship<?> relationship, FRelationshipStatus status){
		clearRelationshipStatus(relationship);
		if (status.equals(FRelationshipStatus.ALLY)){
			alliances.put(relationship.getUniqueId(), (FAlliable<?>) relationship);
		} else if (status.equals(FRelationshipStatus.TRUCE)){
			truces.put(relationship.getUniqueId(), (FTruce<?>) relationship);
		} else if (status.equals(FRelationshipStatus.ENEMY)){
			enemies.put(relationship.getUniqueId(), (FAntagonist<?>) relationship);
		}
	}

	private void handleRelationshipChange(FRelationship<?> who, FRelationshipStatus oldStatus, FRelationshipStatus newStatus){
		FRelationshipInfo info = getRelationshipInfo(who);

		RelationshipStatusChangeEvent event = new RelationshipStatusChangeEvent(this, who, oldStatus, newStatus, info);
		if (!event.callEvent()){
			setRelationship(who, oldStatus);
		}
	}

	@Override
	@Heavy
	public @NotNull Set<@NotNull FRelationshipInfo> getAllies() {
		return alliances.values().stream().map(this::getRelationshipInfo).collect(Collectors.toSet());
	}

	@Override
	public boolean isAllied(@NotNull FAlliable<?> alliable) {
		return alliances.get(alliable.getUniqueId()) != null;
	}

	@Override
	public boolean isAllied(@NotNull OfflinePlayerReference reference) {
		return alliances.get(reference.getUniqueId()) != null;
	}

	@Override
	@Heavy
	public @NotNull FRelationshipInfo markAlly(@NotNull FAlliable<?> alliable) {
		FRelationshipStatus statusCurrently = getRelationshipStatus(alliable);
		setRelationship(alliable, FRelationshipStatus.ALLY);
		handleRelationshipChange(alliable, statusCurrently, FRelationshipStatus.ALLY);
		return getRelationshipInfo(alliable);
	}

	@Override
	public boolean isEnemy(@NotNull FAntagonist<?> antagonist) {
		return enemies.containsKey(antagonist.getUniqueId());
	}

	@Override
	public boolean isEnemy(@NotNull OfflinePlayerReference reference) {
		return enemies.containsKey(reference.getUniqueId());
	}

	@Override
	@Heavy
	public @NotNull FRelationshipInfo markEnemy(@NotNull FAntagonist<?> antagonist) {
		FRelationshipStatus statusCurrently = getRelationshipStatus(antagonist);
		setRelationship(antagonist, FRelationshipStatus.ENEMY);
		handleRelationshipChange(antagonist, statusCurrently, FRelationshipStatus.ENEMY);
		return getRelationshipInfo(antagonist);
	}

	@Override
	public @NotNull FRelationshipStatus getRelationshipStatus(@NotNull UniqueId entity) {
		java.util.UUID id = entity.getUniqueId();
		return alliances.containsKey(id)
				? FRelationshipStatus.ALLY : enemies.containsKey(id)
				? FRelationshipStatus.ENEMY : truces.containsKey(id)
				? FRelationshipStatus.TRUCE : FRelationshipStatus.NEUTRAL;
	}

	@Override
	public @NotNull FRelationshipStatus getRelationshipStatus(@NotNull FRelationship<?> relationShip) {
		return getRelationshipStatus((UniqueId) relationShip);
	}

	@SafeVarargs
	private <T> Collection<T> combine(Collection<? extends T>... collections){
		List<T> list = new ArrayList<>();
		for (Collection<? extends T> collection : collections){
			list.addAll(collection);
		}
		return list;
	}

	@Override
	@Heavy
	public @NotNull FRelationshipInfo getRelationshipInfo(@NotNull FRelationship<?> relationship) {
		FRelationshipStatus status = getRelationshipStatus(relationship);
		UniqueMap<FAlliable<?>> sharedAllies = new UniqueMap<>();
		UniqueMap<FAntagonist<?>> sharedEnemies = new UniqueMap<>();
		UniqueMap<FTruce<?>> sharedTruces = new UniqueMap<>();

		Collection<FRelationship<?>> relationships = combine(this.alliances.values(), this.enemies.values(), this.truces.values());
		relationships.forEach(otherRelationship -> {
			FRelationshipStatus relationshipStatus = relationship.getRelationshipStatus(otherRelationship);
			if (relationshipStatus.equals(FRelationshipStatus.ALLY) && getRelationshipStatus(otherRelationship).equals(FRelationshipStatus.ALLY)){
				sharedAllies.put(otherRelationship.getUniqueId(), (FAlliable<?>) otherRelationship);
			} else if (relationshipStatus.equals(FRelationshipStatus.ENEMY) && getRelationshipStatus(otherRelationship).equals(FRelationshipStatus.ENEMY)){
				sharedEnemies.put(otherRelationship.getUniqueId(), (FAntagonist<?>) otherRelationship);
			} else if (relationshipStatus.equals(FRelationshipStatus.TRUCE) && getRelationshipStatus(otherRelationship).equals(FRelationshipStatus.TRUCE)){
				sharedTruces.put(otherRelationship.getUniqueId(), (FTruce<?>) otherRelationship);
			}
		});

		return new FRelationshipInfo(
				status,
				sharedAllies,
				sharedEnemies,
				sharedTruces,
				this,
				relationship);
	}

	@Override
	public boolean isNeutral(@NotNull FRelationship<?> relationship) {
		return getRelationshipStatus(relationship).equals(FRelationshipStatus.NEUTRAL);
	}

	@Override
	public boolean isNeutral(@NotNull OfflinePlayerReference reference) {
		return getRelationshipStatus(reference).equals(FRelationshipStatus.NEUTRAL);
	}

	@Override
	public boolean isNeutral(@NotNull FactionReference factionReference) {
		if (factionReference.getFactionId() == null){
			return true;
		}
		return getRelationshipStatus(Objects.requireNonNull(factionReference.getFaction())).equals(FRelationshipStatus.NEUTRAL);
	}

	@Override
	@Heavy
	public FRelationshipInfo markNeutral(@NotNull FRelationship<?> relationship) {
		FRelationshipStatus statusCurrently = getRelationshipStatus(relationship);
		setRelationship(relationship, FRelationshipStatus.NEUTRAL);
		handleRelationshipChange(relationship, statusCurrently, FRelationshipStatus.NEUTRAL);
		return getRelationshipInfo(relationship);
	}
	@Override
	public boolean isTruced(@NotNull FTruce<?> truce) {
		return truces.containsKey(truce.getUniqueId());
	}

	@Override
	public boolean isTruced(@NotNull OfflinePlayerReference reference) {
		return truces.containsKey(reference.getUniqueId());
	}

	@Override
	@Heavy
	public @NotNull FRelationshipInfo markTruced(@NotNull FTruce<?> truce) {
		FRelationshipStatus statusCurrently = getRelationshipStatus(truce);
		setRelationship(truce, FRelationshipStatus.TRUCE);
		handleRelationshipChange(truce, statusCurrently, FRelationshipStatus.TRUCE);
		return getRelationshipInfo(truce);
	}

	@Override
	public List<Player> getShineReceivers() {
		return getMembers().getAllOnline();
	}

	@Override
	public ShineHandler getShineHandler() {
		return getFactions().getShineHandler();
	}
}
