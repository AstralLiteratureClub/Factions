package bet.astral.unity.utils.location;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.identity.Identity;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Setter
public class IdentifiedPosition extends Position implements Identity {
	@Expose
	private final UUID uniqueId;
	@Expose
	private String name;

	public IdentifiedPosition(@NotNull String worldName, double x, double y, double z, float yaw, java.util.UUID uniqueId, String name) {
		super(worldName, x, y, z, yaw);
		this.uniqueId = uniqueId;
		this.name = name;
	}

	public IdentifiedPosition(@NotNull World world, double x, double y, double z, float yaw, java.util.UUID uniqueId, String name) {
		super(world, x, y, z, yaw);
		this.uniqueId = uniqueId;
		this.name = name;
	}

	public IdentifiedPosition(Location location, java.util.UUID uniqueId, String name) {
		super(location);
		this.uniqueId = uniqueId;
		this.name = name;
	}


	@Override
	public java.util.@NotNull UUID uuid() {
		return uniqueId;
	}
}
