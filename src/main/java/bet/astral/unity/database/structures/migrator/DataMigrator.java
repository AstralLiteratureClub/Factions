package bet.astral.unity.database.structures.migrator;

import bet.astral.unity.database.structures.Database;

import java.util.concurrent.CompletableFuture;

public interface DataMigrator<O extends Database<K, V, C>, N extends Database<K, V, C>, K, V, C> {
	O getOldDatabase();
	N getNewDatabase();

	CompletableFuture<Void> convert(K key);
	CompletableFuture<Void> convertAll();

	CompletableFuture<Void> deleteOld(K key);
	CompletableFuture<Void> deleteAllOld();
}
