package bet.astral.unity.utils;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface UniqueId {
	@NotNull
	UUID getUniqueId();
	@NotNull
	default String getUniqueIdString() {
		return getUniqueId().toString();
	}
}
