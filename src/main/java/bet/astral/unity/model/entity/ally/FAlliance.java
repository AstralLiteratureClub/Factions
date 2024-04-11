package bet.astral.unity.model.entity.ally;

import bet.astral.unity.model.entity.Participant;
import net.kyori.adventure.identity.Identity;
import org.jetbrains.annotations.NotNull;

public class FAlliance<Self, Ally> implements Participant<Self, Ally>, Identity {
	private final Self self;
	private final Ally ally;
	private final java.util.UUID uniqueId;

	private final long created;

	public FAlliance(Self self, Ally ally, java.util.UUID uniqueId, long created) {
		this.self = self;
		this.ally = ally;
		this.uniqueId = uniqueId;
		this.created = created;
	}
	@Override
	public Self self() {
		return self;
	}

	@Override
	public Ally participant() {
		return ally;
	}

	@Override
	public long created() {
		return created;
	}

	@Override
	public java.util.@NotNull UUID uuid() {
		return uniqueId;
	}
}
