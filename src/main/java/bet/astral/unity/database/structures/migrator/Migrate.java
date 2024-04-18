package bet.astral.unity.database.structures.migrator;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface Migrate<K, V, C> {
	@NotNull
	CompletableFuture<@NotNull V[]> loadAll();
	@NotNull
	CompletableFuture<Void> deleteAll();
}

