package bet.astral.unity.utils.flags;

import bet.astral.messenger.placeholder.Placeholderable;
import org.bukkit.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Flag<V> extends Keyed, Placeholderable {
	@Nullable
	V value();
	void setValue(@NotNull V value);
	@NotNull
	V defaultValue();
	@NotNull
	V valueOrDefault();
}
