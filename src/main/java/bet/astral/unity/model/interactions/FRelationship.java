package bet.astral.unity.model.interactions;

import bet.astral.unity.utils.refrence.FactionReference;
import bet.astral.unity.utils.refrence.OfflinePlayerReference;
import org.jetbrains.annotations.NotNull;

public interface FRelationship {
	@NotNull
	FRelationshipStatus getRelationshipStatus(@NotNull FRelationship relationShip);
	@NotNull
	FRelationshipInfo getRelationshipInfo(@NotNull FRelationship relationship);

	boolean isNeutral(@NotNull FRelationship relationship);
	boolean isNeutral(@NotNull OfflinePlayerReference reference);
	boolean isNeutral(@NotNull FactionReference factionReference);

	FRelationshipInfo markNeutral(@NotNull FRelationship relationship);

	default boolean canBe(@NotNull FRelationship relationship, @NotNull FRelationshipStatus status) {
		if (this instanceof FAlliable && relationship instanceof FAlliable && status.equals(FRelationshipStatus.ALLY)){
			return true;
		} else if (this instanceof FAntagonist && relationship instanceof FAntagonist && status.equals(FRelationshipStatus.ENEMY)){
			return true;
		}
		return status.equals(FRelationshipStatus.NEUTRAL);
	}
}