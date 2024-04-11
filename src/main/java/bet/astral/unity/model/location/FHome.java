package bet.astral.unity.model.location;

import bet.astral.unity.utils.location.IdentifiedPosition;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class FHome extends IdentifiedPosition {
	public FHome(@NotNull String worldName, double x, double y, double z, float yaw, java.util.UUID uniqueId, String name) {
		super(worldName, x, y, z, yaw, uniqueId, name);
	}

	public FHome(@NotNull World world, double x, double y, double z, float yaw, java.util.UUID uniqueId, String name) {
		super(world, x, y, z, yaw, uniqueId, name);
	}

	public FHome(Location location, java.util.UUID uniqueId, String name) {
		super(location, uniqueId, name);
	}

	@Override
	public @NotNull String translationKey() {
		return "unity.home";
	}
}
