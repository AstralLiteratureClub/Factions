package bet.astral.unity.utils;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.identity.Identity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Getter
@Setter
public class IdentifiedPosition implements Identity {
	@Expose
	private final UUID uniqueId;
	@Expose
	private String name;
	@Expose
	private double x;
	@Expose
	private double y;
	@Expose
	private double z;
	@Expose
	private float pitch;
	@Expose
	private String world;

	private IdentifiedPosition(){
		this.name = null;
		this.uniqueId = null;
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.pitch = 0;
		this.world = null;
	}

	public IdentifiedPosition(String name, UUID uniqueId, double x, double y, double z, float pitch, @NotNull String worldName) {
		this.name = name;
		this.uniqueId = uniqueId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.world = worldName;
	}
	public IdentifiedPosition(String name, UUID uniqueId, double x, double y, double z, float pitch, @NotNull World world) {
		this.name = name;
		this.uniqueId = uniqueId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.world = world.getName();
	}

	public IdentifiedPosition(String name, UUID uniqueId, Location location){
		this.name = name;
		this.uniqueId = uniqueId;
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
		this.pitch = location.getPitch();
		this.world = location.getWorld().getName();
	}


	public void update(Location location){
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
		this.pitch = location.getPitch();
		this.world = location.getWorld().getName();
	}

	public Location asLocation(){
		return new Location(Bukkit.getWorld(world), x, y, z, pitch, 90);

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
	public java.util.@NotNull UUID uuid() {
		return uniqueId;
	}
}
