package bet.astral.unity.database;

import bet.astral.unity.annotations.ASync;
import bet.astral.unity.database.internal.Database;

import java.util.concurrent.CompletableFuture;

public interface DataConverter<O extends Database<K, V, C>, N extends Database<K, V, C>, K, V, C> {
	O getOldDatabase();
	N getNewDatabase();

	@ASync
	CompletableFuture<Void> convert(K value);
	@ASync
	CompletableFuture<Void> convertAll();
}
