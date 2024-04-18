package bet.astral.unity.model.interactions;

import bet.astral.unity.utils.UniqueId;
import bet.astral.unity.utils.refrence.OfflinePlayerReference;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface FAlliable extends FRelationship, UniqueId {
	@NotNull
	Set<@NotNull FRelationshipInfo> getAllies();
	@NotNull
	Set<@NotNull FRelationshipInfo> getAlliesShared(@NotNull FAlliable uniqueId);

	boolean isAllied(@NotNull FAlliable alliable);
	boolean isAllied(@NotNull OfflinePlayerReference reference);

	@NotNull
	FRelationshipInfo markAlly(@NotNull FAlliable alliable);

	default boolean canBeAlly(@NotNull FRelationship relationship) {
		return relationship instanceof FAlliable;
	}
}
