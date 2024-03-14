package bet.astral.unity.utils.flags;

import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.messenger.utils.PlaceholderUtils;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class FlagImpl<V> implements Flag<V>{
	private final V defaultValue;
	private final NamespacedKey key;
	private V value;

	public FlagImpl(NamespacedKey key, V defaultValue, V value) {
		this.defaultValue = defaultValue;
		this.key = key;
		this.value = value;
	}

	public FlagImpl(Flag<V> defaultFlag, V value){
		this.defaultValue = defaultFlag.defaultValue();
		this.key = defaultFlag.getKey();
		this.value = value;
	}

	@Override
	public V value() {
		return value;
	}

	@Override
	public void setValue(@NotNull V value) {
		this.value = value;
	}

	@Override
	public @NotNull V defaultValue() {
		return defaultValue;
	}

	@Override
	public @NotNull V valueOrDefault() {
		return value != null ? value : defaultValue;
	}

	@Override
	public @NotNull NamespacedKey getKey() {
		return key;
	}

	@Override
	public Collection<Placeholder> asPlaceholder(String s) {
		return List.of(
				PlaceholderUtils.createPlaceholder(s, "key", key),
				PlaceholderUtils.createPlaceholder(s, "value", value));
	}
}
