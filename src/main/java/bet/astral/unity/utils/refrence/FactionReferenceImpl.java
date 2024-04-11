package bet.astral.unity.utils.refrence;

import bet.astral.unity.Factions;
import bet.astral.unity.model.Faction;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class FactionReferenceImpl implements FactionReference{
	private final Factions factions;
	private final UUID uniqueId;

	public FactionReferenceImpl(Factions factions, UUID uniqueId) {
		this.factions = factions;
		this.uniqueId = uniqueId;
	}

	@Override
	public @Nullable Faction getFaction() {
		return factions.getFactionManager().get(uniqueId);
	}

	@Override
	public @Nullable UUID getFactionId() {
		return uniqueId;
	}
}
