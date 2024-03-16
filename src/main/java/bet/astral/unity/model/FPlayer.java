package bet.astral.unity.model;

import bet.astral.unity.Factions;
import bet.astral.unity.utils.flags.Flag;
import bet.astral.unity.utils.flags.FlagImpl;
import bet.astral.unity.utils.flags.Flaggable;
import bet.astral.unity.utils.refrence.FactionReference;
import bet.astral.unity.utils.refrence.OfflinePlayerReference;
import bet.astral.unity.utils.refrence.PlayerReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.ForwardingAudience;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
public class FPlayer implements Flaggable, ForwardingAudience, PlayerReference, FactionReference {
	private final Factions factions;
	@Getter(AccessLevel.NONE)
	private final Map<NamespacedKey, Flag<?>> flags = new HashMap<>();
	private final UUID uniqueId;
	private final String name;
	private UUID factionId;
	private FChat chatType = FChat.GLOBAL;

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
}
