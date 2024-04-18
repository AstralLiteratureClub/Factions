package bet.astral.unity.utils.location;

import bet.astral.unity.utils.UniqueId;
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
public class IdentifiedPosition extends Position implements Identity, UniqueId {
	@Expose
	private final UUID uniqueId;
	@Expose
	private String name;

	public IdentifiedPosition(java.util.UUID uniqueId, String name, @NotNull String worldName, double x, double y, double z, float yaw, float pitch) {
		super(worldName, x, y, z, yaw, pitch);
		this.uniqueId = uniqueId;
		this.name = name;
	}

	public IdentifiedPosition(java.util.UUID uniqueId, String name, @NotNull World world, double x, double y, double z, float yaw, float pitch) {
		super(world, x, y, z, yaw, pitch);
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
