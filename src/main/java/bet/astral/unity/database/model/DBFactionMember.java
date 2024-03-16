package bet.astral.unity.database.model;

import bet.astral.unity.model.FRole;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.refrence.FactionReference;
import bet.astral.unity.utils.refrence.PlayerReference;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
}
