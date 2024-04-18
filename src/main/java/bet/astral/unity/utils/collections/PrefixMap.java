package bet.astral.unity.utils.collections;

import bet.astral.unity.model.FPrefix;
import bet.astral.unity.utils.UniqueId;

import java.util.HashMap;
import java.util.Map;

public class PrefixMap extends HashMap<UniqueId, FPrefix> {
	public PrefixMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public PrefixMap(int initialCapacity) {
		super(initialCapacity);
	}

	public PrefixMap() {
	}

	public PrefixMap(Map<? extends UniqueId, ? extends FPrefix> m) {
		super(m);
	}
}
