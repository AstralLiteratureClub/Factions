package bet.astral.unity.database.model;

import bet.astral.messenger.Messenger;
import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.unity.Factions;
import bet.astral.unity.model.FRole;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.refrence.FactionReference;
import bet.astral.unity.utils.refrence.PlayerReference;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class DBFactionMember implements PlayerReference, FactionReference {
	private final UUID uniqueId;
	@Getter
	private final Component prefix;
	private final Faction faction;
	@Getter
	private final FRole role;

	public DBFactionMember(java.util.UUID uniqueId, Component prefix, Faction faction, FRole role) {
		this.uniqueId = uniqueId;
		this.prefix = prefix;
		this.faction = faction;
		this.role = role;
	}

	@Override
	public @Nullable Faction getFaction() {
		return faction;
	}

	@Override
	public java.util.@Nullable UUID getFactionId() {
		return faction != null ? faction.getUniqueId() : null;
	}

	@Override
	public java.util.@NotNull UUID uuid() {
		return uniqueId;
	}

	@Override
	public java.util.@NotNull UUID getUniqueId() {
		return uniqueId;
	}

	@Override
	public Messenger<Factions> messenger() {
		return faction.getFactions().getMessenger();
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
		if (player() == null){
			return List.of();
		}
		return List.of(Objects.requireNonNull(player()));
	}
}
