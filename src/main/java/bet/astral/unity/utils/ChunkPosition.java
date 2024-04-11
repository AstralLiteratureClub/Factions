package bet.astral.unity.utils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

@Getter
@Setter
public class ChunkPosition {
	private final String world;
	private int x;
	private int y;

	public ChunkPosition(String world, int x, int y) {
		this.x = x;
		this.y = y;
		this.world = world;
	}


	public @Nullable World getWorld(){
		return Bukkit.getWorld(world);
	}
	public CompletableFuture<Chunk> asChunkASync() {
		World world = getWorld();
		if (world == null){
			return null;
		}
		return world.getChunkAtAsync(x, y);
	}
	public Chunk getChunk() {
		World world = getWorld();
		if (world == null){
			return null;
		}
		return world.getChunkAt(x, y);
	}
}
