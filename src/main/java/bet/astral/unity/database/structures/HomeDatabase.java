package bet.astral.unity.database.structures;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface HomeDatabase<K, V, C, K2, V2, C2> extends Database<K, V, C> {
	@NotNull
	CompletableFuture<@NotNull V[]> loadAllHome(V2 value);
	@NotNull
	CompletableFuture<Void> deleteAllHomes(V2 value);

}
