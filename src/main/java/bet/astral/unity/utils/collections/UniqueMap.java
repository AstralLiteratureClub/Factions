package bet.astral.unity.utils.collections;

import bet.astral.unity.utils.UniqueId;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UniqueMap<V extends UniqueId> extends HashMap<UUID, V> {
	public UniqueMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public UniqueMap(int initialCapacity) {
		super(initialCapacity);
	}

	public UniqueMap() {
	}

	public UniqueMap(Map<? extends UUID, ? extends V> m) {
		super(m);
	}
}
