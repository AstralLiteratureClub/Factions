package bet.astral.unity.database.structures;

import bet.astral.unity.Factions;
import bet.astral.unity.database.structures.migrator.Migrate;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public interface Database<K, V, C> extends Migrate<K, V, C> {
	Factions getFactions();
	default ComponentLogger getLogger(){
		return getFactions().getComponentLogger();
	}

	@ApiStatus.Internal
	void addToCache(@NotNull C value);
	@ApiStatus.Internal
	void removeFromCache(@NotNull C value);

	@NotNull
	Consumer<@NotNull C> cacheConsumer();
	@NotNull
	Consumer<@NotNull C> uncacheConsumer();

	@NotNull
	Function<@NotNull V, @NotNull C> convertDataToCached();
	@NotNull
	Function<@NotNull C, @NotNull V> convertCachedToData();

	CompletableFuture<V> load(@NotNull K key);
	CompletableFuture<Void> save(@NotNull V value);
	CompletableFuture<Void> saveCached(@NotNull C cachedValue);
	CompletableFuture<Void> delete(@NotNull K key);
	CompletableFuture<Void> deleteCached(@NotNull C cachedValue);

	String tableName();

	C createDefault(K key);

	default <U> CompletableFuture<U> handleExceptions(CompletableFuture<U> future){
		future.exceptionallyAsync(throwable -> {
			getLogger().error("When executing database tasks, came across an exception!", throwable);
			return null;
		});
		return future;
	}

	@Nullable
	default String uuidToString(@Nullable UUID uuid){
		if (uuid == null){
			return null;
		}
		return uuid.toString();
	}

	/**
	 * Starts this given database branch.
	 * In SQL-based databases, this creates the tables and in the faction database it loads all the factions to cache.
	 */
	void init();
}
