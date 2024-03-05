package bet.astral.unity.utils.flags;

import org.bukkit.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Flag<V> extends Keyed {
	@Nullable
	V value();
	void setValue(@NotNull V value);
	@NotNull
	V defaultValue();
	@NotNull
	V valueOrDefault();
}
