package bet.astral.unity.utils.refrence;

import bet.astral.unity.model.Faction;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface FactionReference {
	@Nullable
	Faction getFaction();
	@Nullable
	UUID getFactionId();
}
