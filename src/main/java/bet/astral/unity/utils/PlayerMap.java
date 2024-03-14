package bet.astral.unity.utils;

import bet.astral.unity.utils.refrence.OfflinePlayerReference;
import bet.astral.unity.utils.refrence.PlayerReferenceImpl;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PlayerMap<V> extends HashMap<UUID, V> implements ForwardingAudience {
	public static <V> PlayerMap<V> of(Map<OfflinePlayer, V> map){
		return new PlayerMap<>(convert(map));
	}
	private static <V> Map<UUID, V> convert(Map<OfflinePlayer, V> map){
		Map<UUID, V> newMap = new HashMap<>();
		for (OfflinePlayer entity : map.keySet()){
			newMap.put(entity.getUniqueId(), map.get(entity));
		}
		return newMap;
	}
	private static <V> Map<UUID,? extends V> convertReference(Map<OfflinePlayerReference,? extends V> map) {
		Map<UUID, V> newMap = new HashMap<>();
		for (OfflinePlayerReference entity : map.keySet()){
			newMap.put(entity.uuid(), map.get(entity));
		}
		return newMap;
	}
	public PlayerMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public PlayerMap(int initialCapacity) {
		super(initialCapacity);
	}

	public PlayerMap() {
	}

	public PlayerMap(Map<? extends UUID, ? extends V> m) {
		super(m);
	}

	public V put(OfflinePlayer key, V value) {
		return super.put(key.getUniqueId(), value);
	}
	public V put(OfflinePlayerReference key, V value){
		return super.put(key.uuid(), value);
	}


	public void putAllPlayer(Map<OfflinePlayer, ? extends V> m) {
		super.putAll(convert(m));
	}
	public void putAllReference(Map<OfflinePlayerReference, ? extends V> m) {
		super.putAll(convertReference(m));
	}


	public V putIfAbsent(OfflinePlayer key, V value) {
		return super.putIfAbsent(key.getUniqueId(), value);
	}
	public V putIfAbsent(OfflinePlayerReference key, V value) {
		return super.putIfAbsent(key.uuid(), value);
	}

	public V computeIfAbsent(OfflinePlayer key, Function<? super OfflinePlayer, ? extends V> mappingFunction) {
		Function<UUID, ? extends V> newFunction = (Function<UUID, V>) uuid -> {
			OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
			return mappingFunction.apply(player);
		};
		return super.computeIfAbsent(key.getUniqueId(), newFunction);
	}
	public V computeIfAbsent(OfflinePlayerReference key, Function<? super OfflinePlayer, ? extends V> mappingFunction) {
		Function<UUID, ? extends V> newFunction = (Function<UUID, V>) uuid -> {
			OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
			return mappingFunction.apply(player);
		};
		return super.computeIfAbsent(key.uuid(), newFunction);
	}
	@Override
	public V get(Object key) {
		if (key instanceof OfflinePlayer player){
			return super.get(player.getUniqueId());
		} else if (key instanceof OfflinePlayerReference reference){
			return super.get(reference.uuid());
		}
		return super.get(key);
	}
	public V get(OfflinePlayer key) {
		return super.get(key.getUniqueId());
	}
	public V get(OfflinePlayerReference key) {
		return super.get(key.uuid());
	}


	@Override
	public boolean containsKey(Object key) {
		if (key instanceof OfflinePlayer player){
			return super.containsKey(player);
		} else if (key instanceof OfflinePlayerReference reference){
			return super.containsKey(reference.uuid());
		}
		return super.containsKey(key);
	}
	public boolean containsKey(OfflinePlayer key) {
		return containsKey((Object) key);
	}
	public boolean containsKey(OfflinePlayerReference key) {
		return containsKey((Object) key);
	}

	@Override
	public V remove(Object key) {
		if (key instanceof OfflinePlayer player){
			return super.remove(player.getUniqueId());
		} else if (key instanceof OfflinePlayerReference reference){
			return super.remove(reference.uuid());
		}
		return super.remove(key);
	}
	@SuppressWarnings("UnusedReturnValue")
	public V remove(OfflinePlayer key) {
		return remove((Object) key);
	}
	@SuppressWarnings("UnusedReturnValue")
	public V remove(OfflinePlayerReference key) {
		return remove((Object) key);
	}

	public Set<OfflinePlayer> keySetPlayer(){
		return keySet().stream().map(Bukkit::getOfflinePlayer).collect(Collectors.toSet());
	}
	public Set<OfflinePlayerReference> keySetPlayerReference(){
		return keySet().stream().map(PlayerReferenceImpl::new).collect(Collectors.toSet());
	}
	public Set<Entry<OfflinePlayer, V>> entrySetPlayer() {
		return super.entrySet().stream().map(entry->new Entry<OfflinePlayer, V>() {
			@Override
			public OfflinePlayer getKey() {
				return Bukkit.getOfflinePlayer(entry.getKey());
			}

			@Override
			public V getValue() {
				return entry.getValue();
			}

			@Override
			public V setValue(V value) {
				return entry.setValue(value);
			}
		}).collect(Collectors.toSet());
	}
	public Set<Entry<OfflinePlayerReference, V>> entrySetReference() {
		return super.entrySet().stream().map(entry->new Entry<OfflinePlayerReference, V>() {
			@Override
			public OfflinePlayerReference getKey() {
				return new PlayerReferenceImpl(entry.getKey());
			}

			@Override
			public V getValue() {
				return entry.getValue();
			}

			@Override
			public V setValue(V value) {
				return entry.setValue(value);
			}
		}).collect(Collectors.toSet());
	}

	@Override
	public V getOrDefault(Object key, V defaultValue) {
		if (key instanceof OfflinePlayer player){
			return super.getOrDefault(player.getUniqueId(), defaultValue);
		} else if (key instanceof OfflinePlayerReference reference){
			return super.getOrDefault(reference.uuid(), defaultValue);
		}
		return super.getOrDefault(key, defaultValue);
	}

	@Override
	public boolean remove(Object key, Object value) {
		if (key instanceof OfflinePlayer player){
			return super.remove(player.getUniqueId(), value);
		} else if (key instanceof OfflinePlayerReference reference){
			return super.remove(reference.uuid(), value);
		}
		return super.remove(key, value);
	}

	public boolean replace(OfflinePlayer key, V oldValue, V newValue) {
		return super.replace(key.getUniqueId(), oldValue, newValue);
	}
	public boolean replace(OfflinePlayerReference key, V oldValue, V newValue){
		return super.replace(key.uuid(), oldValue, newValue);
	}

	public V replace(OfflinePlayer key, V value) {
		return super.replace(key.getUniqueId(), value);
	}
	public V replace(OfflinePlayerReference reference, V value){
		return super.replace(reference.uuid(), value);
	}

	public V merge(OfflinePlayer key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
		return super.merge(key.getUniqueId(), value, remappingFunction);
	}

	public V merge(OfflinePlayerReference player, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction){
		return super.merge(player.uuid(), value, remappingFunction);
	}

	public void forEachPlayer(BiConsumer<? super OfflinePlayer, ? super V> action) {
		super.forEach((uuid, v) -> action.accept(Bukkit.getOfflinePlayer(uuid), v));
	}

	public void forEachReference(BiConsumer<? super OfflinePlayerReference, ? super V> action) {
		super.forEach((uuid, v) -> action.accept(OfflinePlayerReference.of(uuid), v));
	}

	public void replaceAllPlayer(BiFunction<? super OfflinePlayer, ? super V, ? extends V> function) {
		super.replaceAll((uuid, v) -> function.apply(Bukkit.getOfflinePlayer(uuid), v));
	}
	public void replaceAllReference(BiFunction<? super OfflinePlayerReference, ? super V, ? extends V> function) {
		super.replaceAll((uuid, v) -> function.apply(OfflinePlayerReference.of(uuid), v));
	}

	@Override
	public @NotNull Iterable<? extends Audience> audiences() {
		return keySetPlayer().stream().filter(OfflinePlayer::isOnline).map(OfflinePlayer::getPlayer).toList();
	}
}
