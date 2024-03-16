package bet.astral.unity.database.internal;

import java.util.concurrent.CompletableFuture;

public interface Converter<K, V, C> {
	CompletableFuture<Void> convert(K key, V value);
	CompletableFuture<Void> convert(K key, V value, C cached);
}
