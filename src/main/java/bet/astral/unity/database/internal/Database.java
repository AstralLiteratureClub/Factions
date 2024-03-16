package bet.astral.unity.database.internal;

import org.incendo.cloud.type.tuple.Pair;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public interface Database<K, V, C> {
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

	boolean close();
	String tableName();

	C createDefault(K key);

	@Nullable
	default String uuidToString(@Nullable UUID uuid){
		if (uuid == null){
			return null;
		}
		return uuid.toString();
	}
}
