package bet.astral.unity.model.location;

import bet.astral.unity.Factions;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.location.IdentifiedPosition;
import bet.astral.unity.utils.refrence.FactionReference;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FHome extends IdentifiedPosition implements FactionReference {
	private final Factions factions;
	@Getter
	private final java.util.UUID factionId;
	public FHome(Factions factions, java.util.UUID factionId, java.util.UUID uniqueId, String name, @NotNull String worldName, double x, double y, double z, float yaw, float pitch) {
		super(uniqueId, name, worldName, x, y, z, yaw, pitch);
		this.factionId = factionId;
		this.factions = factions;
	}

	public FHome(Factions factions, java.util.UUID factionId, java.util.UUID uniqueId, String name, @NotNull World world, double x, double y, double z, float yaw, float pitch) {
		super(uniqueId, name, world, x, y, z, yaw, pitch);
		this.factionId = factionId;
		this.factions = factions;
	}

	public FHome(Factions factions, java.util.UUID factionId, java.util.UUID uniqueId, String name, Location location) {
		super(location, uniqueId, name);
		this.factionId = factionId;
		this.factions = factions;
	}

	@Override
	public @NotNull String translationKey() {
		return "unity.home";
	}

	@Override
	public @Nullable Faction getFaction() {
		return factions.getFactionManager().get(factionId);
	}
}
