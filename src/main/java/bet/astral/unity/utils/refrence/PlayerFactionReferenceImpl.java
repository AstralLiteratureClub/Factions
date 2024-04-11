package bet.astral.unity.utils.refrence;

import bet.astral.unity.model.Faction;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PlayerFactionReferenceImpl extends PlayerReferenceImpl implements FactionReference{
	private final Faction faction;
	private final UUID factionId;
	public PlayerFactionReferenceImpl(@NotNull Player player, @Nullable Faction faction) {
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

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FactionReference factionReference){
			if (factionReference instanceof OfflinePlayerReference reference){
				boolean equals = reference.getUniqueId().equals(getUniqueId());
				if (!equals){
					return false;
				}
			}
			if (factionReference.getFactionId() != null && getFactionId() != null){
				return this.getFactionId().equals(factionReference.getFactionId());
			} else if (factionReference.getFactionId() == null && getFactionId() == null){
				return true;
			}
		}
		return false;
	}

	@Override
	public @Nullable Player player() {
		return super.player();
	}

	@Override
	public @NotNull Iterable<? extends Audience> audiences() {
		Player player = player();
		if (player == null){
			return Collections.emptyList();
		}
		return List.of(player);
	}
}
