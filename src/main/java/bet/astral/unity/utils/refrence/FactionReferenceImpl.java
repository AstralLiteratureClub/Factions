package bet.astral.unity.utils.refrence;

import bet.astral.unity.model.Faction;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class FactionReferenceImpl extends PlayerReferenceImpl implements FactionReference{
	private final Faction faction;
	private final UUID factionId;
	public FactionReferenceImpl(@NotNull Player player, @Nullable Faction faction) {
		super(player.getUniqueId());
		this.faction = faction;
		this.factionId = faction != null ? faction.getUniqueId() : null;
	}


	@Override
	public @Nullable Faction getFaction() {
		return faction;
	}

	@Override
	public java.util.@Nullable UUID getFactionId() {
		return factionId;
	}

	@Override
	public java.util.@NotNull UUID getUniqueId() {
		return uuid();
	}
}
