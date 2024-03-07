package bet.astral.unity.model;

import bet.astral.messenger.Message;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.messenger.utils.PlaceholderUtils;
import bet.astral.unity.Factions;
import bet.astral.unity.event.ASyncInviteExpireEvent;
import bet.astral.unity.event.player.ASyncPlayerAcceptInviteEvent;
import bet.astral.unity.event.player.ASyncPlayerCancelInviteEvent;
import bet.astral.unity.event.player.ASyncPlayerDenyInviteEvent;
import bet.astral.unity.event.player.ASyncPlayerJoinFactionEvent;
import bet.astral.unity.utils.IdentifiedPosition;
import bet.astral.unity.utils.OfflinePlayerList;
import bet.astral.unity.utils.PlayerMap;
import bet.astral.unity.utils.TranslationKey;
import bet.astral.unity.utils.flags.Flag;
import bet.astral.unity.utils.flags.FlagImpl;
import bet.astral.unity.utils.flags.Flaggable;
import bet.astral.unity.utils.refrence.OfflinePlayerReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
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
public class Faction implements Identity, ForwardingAudience, Translatable, Flaggable {
	public static PlaceholderList factionPlaceholders(@Nullable String prefix, @NotNull Faction faction){
		PlaceholderList placeholders = new PlaceholderList();
		placeholders.add(PlaceholderUtils.createPlaceholder(prefix, prefix != null ? prefix : "faction", faction));
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
	private final Factions factions;
	private final OfflinePlayerList members = new OfflinePlayerList();
	private final PlayerMap<FInvite> invites = new PlayerMap<>();
	private final PlayerMap<FRole> roles = new PlayerMap<>();
	private final Map<NamespacedKey, Flag<?>> flags = new HashMap<>();
	private final UUID uniqueId;
	private final long firstCreated;
	@Setter(AccessLevel.NONE)
	private String name;
	@Setter(AccessLevel.NONE)
	private Component displayname;
	private Component description;
	private Component joinInfo;
	private IdentifiedPosition home = null;

	public Faction(Factions factions, UUID uniqueId, String name, long firstCreated){
		this.factions = factions;
		this.uniqueId = uniqueId;
		this.firstCreated = firstCreated;
		Message defaultDescription = factions.messenger().getMessage(TranslationKey.DEFAULT_FACTION_DESCRIPTION);
		Message defaultJoin = factions.messenger().getMessage(TranslationKey.DEFAULT_FACTION_JOIN_INFO);
		PlaceholderList placeholders = new PlaceholderList();
		placeholders.add("name", name);
		placeholders.add("displayname", name);
		placeholders.add("faction", name);
		placeholders.add("id", uniqueId.toString());
		this.description = factions.messenger().parse(defaultDescription, Message.Type.CHAT, placeholders);
		this.joinInfo = factions.messenger().parse(defaultJoin, Message.Type.CHAT, placeholders);
		this.displayname = Component.text(name);
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

	public void invite(@NotNull Player from, @NotNull Player to){
		FInvite invite = new FInvite(
				this,
				from,
				to,
				30*1000,
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
		invites.put(to.getUniqueId(), invite);
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
		return join(to);
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

	public void cancelInvite(@NotNull Player from, @NotNull Player to, @Nullable String reason){
		FInvite invite  = invites.get(to);
		if (invite == null){
			return;
		}

		ASyncPlayerCancelInviteEvent event = new ASyncPlayerCancelInviteEvent(this, to, from, reason);

	}



	public boolean join(@NotNull Player player){
		ASyncPlayerJoinFactionEvent event = new ASyncPlayerJoinFactionEvent(this, player);
		if (!event.callEvent()){
			return false;
		}
		members.add(player);
		roles.put(player, FRole.DEFAULT);
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

}
