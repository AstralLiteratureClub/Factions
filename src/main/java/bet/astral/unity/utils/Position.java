package bet.astral.unity.utils;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Getter
@Setter
public class Position implements Identity, Translatable {
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
	private float yaw;
	@Expose
	private String world;

	private Position(){
		this.name = null;
		this.uniqueId = null;
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.yaw = 0;
		this.world = null;
	}

	public Position(String name, UUID uniqueId, double x, double y, double z, float yaw, @NotNull String worldName) {
		this.name = name;
		this.uniqueId = uniqueId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.world = worldName;
	}
	public Position(String name, UUID uniqueId, double x, double y, double z, float yaw, @NotNull World world) {
		this.name = name;
		this.uniqueId = uniqueId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.world = world.getName();
	}

	public Position(String name, double x, double y, double z, float yaw, @NotNull String worldName) {
		this.name = name;
		this.uniqueId = java.util.UUID.randomUUID();
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.world = worldName;
	}
	public Position(String name, double x, double y, double z, float yaw, @NotNull World world) {
		this.name = name;
		this.uniqueId = java.util.UUID.randomUUID();
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.world = world.getName();
	}


	public Position(String name, UUID uniqueId, Location location){
		this.name = name;
		this.uniqueId = uniqueId;
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
		this.yaw = location.getYaw();
		this.world = location.getWorld().getName();
	}

	public Position(String name, Location location){
		this.name = name;
		this.uniqueId = java.util.UUID.randomUUID();
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
		this.yaw = location.getYaw();
		this.world = location.getWorld().getName();
	}
	public void update(Location location){
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
		this.yaw = location.getYaw();
		this.world = location.getWorld().getName();
	}

	public Location asLocation(){
		return new Location(Bukkit.getWorld(world), x, y, z, yaw, 90);

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

	@Override
	public @NotNull String translationKey() {
		return "unity.home";
	}
}
