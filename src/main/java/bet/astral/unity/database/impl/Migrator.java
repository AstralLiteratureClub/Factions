package bet.astral.unity.database.impl;

import bet.astral.unity.database.structures.Database;
import bet.astral.unity.database.structures.migrator.DataMigrator;

import java.util.concurrent.CompletableFuture;

// TODO
public class Migrator<K, V, C> implements DataMigrator<Database<K, V, C>, Database<K, V, C>, K, V, C> {
	private final Database<K, V, C> oldDatabase;
	private final Database<K, V, C> newDatabase;

	public Migrator(Database<K, V, C> oldDatabase, Database<K, V, C> newDatabase) {
		this.oldDatabase = oldDatabase;
		this.newDatabase = newDatabase;
	}

	@Override
	public Database<K, V, C> getOldDatabase() {
		return oldDatabase;
	}

	@Override
	public Database<K, V, C> getNewDatabase() {
		return newDatabase;
	}

	@Override
	public CompletableFuture<Void> convert(K key) {
		CompletableFuture<V> completableFuture = oldDatabase.load(key);
		return completableFuture.thenAccept(newDatabase::save);
	}

	@Override
	public CompletableFuture<Void> convertAll() {
		return null;
	}

	@Override
	public CompletableFuture<Void> deleteOld(K value) {
		return null;
	}

	@Override
	public CompletableFuture<Void> deleteAllOld() {
		return null;
	}
}
