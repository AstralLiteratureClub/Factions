package bet.astral.unity.utils.location;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public class Position implements Translatable {
	@Expose
	private double x;
	@Expose
	private double y;
	@Expose
	private double z;
	@Expose
	private float yaw;
	@Expose
	private float pitch;
	@Expose
	private String world;

	private Position(){
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.yaw = 0;
		this.pitch = 0;
		this.world = null;
	}

	public Position(@NotNull String worldName, double x, double y, double z, float yaw, float pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.world = worldName;
	}
	public Position(@NotNull World world, double x, double y, double z, float yaw, float pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.world = world.getName();
	}

	public Position(Location location){
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
		this.yaw = location.getYaw();
		this.pitch = location.getPitch();
		this.world = location.getWorld().getName();
	}

	public void update(Location location){
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
		this.yaw = location.getYaw();
		this.pitch = location.getPitch();
		this.world = location.getWorld().getName();
	}

	public Location asLocation(){
		return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);

	}

	@Nullable
	public World getWorld(){
		return Bukkit.getWorld(world);
	}

	@NotNull
	public String getWorldName(){
		return world;
	}

	@Override
	public @NotNull String translationKey() {
		return "unity.position";
	}
}
