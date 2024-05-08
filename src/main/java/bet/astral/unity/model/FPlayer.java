package bet.astral.unity.model;

import bet.astral.guiman.GUIViewer;
import bet.astral.messenger.Messenger;
import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.shine.ShineHandler;
import bet.astral.shine.model.ShineReceiver;
import bet.astral.unity.Factions;
import bet.astral.unity.model.interactions.*;
import bet.astral.unity.utils.UniqueId;
import bet.astral.unity.utils.flags.Flag;
import bet.astral.unity.utils.flags.FlagImpl;
import bet.astral.unity.utils.flags.Flaggable;
import bet.astral.unity.utils.refrence.FactionReference;
import bet.astral.unity.utils.refrence.OfflinePlayerReference;
import bet.astral.unity.utils.refrence.PlayerReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Setter
@Getter
public class FPlayer implements Flaggable,
		ForwardingAudience, PlayerReference,
		FactionReference, UniqueId,
		FRelationship, FAntagonist, FAlliable, FTruce<FPlayer>,
		ShineReceiver, GUIViewer, FEntity<FPlayer>
{
	private final Factions factions;
	@Getter(AccessLevel.NONE)
	private final Map<NamespacedKey, Flag<?>> flags = new HashMap<>();
	private final UUID uniqueId;
	private final String name;
	private UUID factionId;
	private FCommunicationChannel chatType = FCommunicationChannel.GLOBAL;

	public FPlayer(Factions factions, Player player){
		this.factions = factions;
		this.uniqueId = player.getUniqueId();
		this.name = player.getName();
	}

	public FPlayer(Factions factions, UUID uniqueId, String name) {
		this.factions = factions;
		this.uniqueId = uniqueId;
		this.name = name;
	}

	public FPlayer(Factions factions, OfflinePlayerReference of) {
		this.factions = factions;
		this.uniqueId = of.uuid();
		this.name = of.name();
	}

	@Override
	public java.util.@NotNull UUID uuid() {
		return uniqueId;
	}

	public void setFaction(@NotNull FactionReference reference){
		this.factionId = reference.getFactionId();
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

	@Override
	@Nullable
	public Faction getFaction() {
		if (factionId == null){
			return null;
		}
		return factions.getFactionManager().get(factionId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FPlayer player = (FPlayer) o;
		return
				Objects.equals(factions, player.factions)
				&& Objects.equals(uniqueId, player.uniqueId)
				&& Objects.equals(factionId, player.factionId)
				&& chatType == player.chatType;
	}

	@Override
	public Messenger<Factions> messenger() {
		return factions.getMessenger();
	}

	@Override
	public Collection<Placeholder> asPlaceholder(String s) {
		return null;
	}

	@Override
	public @Nullable Player player() {
		return PlayerReference.super.player();
	}

	@Override
	public @NotNull Iterable<? extends Audience> audiences() {
		return PlayerReference.super.audiences();
	}


	@Override
	public List<Player> getShineReceivers() {
		Player player = player();
		if (player == null || !player.isOnline()){
			return Collections.emptyList();
		}
		return List.of(player);
	}

	@Override
	public ShineHandler getShineHandler() {
		return getFactions().getShineHandler();
	}

	@Override
	public @Nullable Player getPlayer() {
		return player();
	}

	@Override
	public FEntityType<FPlayer> getEntityType() {
		return FEntityType.PLAYER_UNITY;
	}
}
