package bet.astral.unity.model.interactions;

import bet.astral.annotations.Heavy;
import bet.astral.unity.model.FEntity;
import bet.astral.unity.utils.UniqueId;
import bet.astral.unity.utils.refrence.FactionReference;
import bet.astral.unity.utils.refrence.OfflinePlayerReference;
import org.jetbrains.annotations.NotNull;

public interface FRelationship<D> extends FEntity<D>, UniqueId {
	@NotNull
	FRelationshipStatus getRelationshipStatus(@NotNull FRelationship<?> relationShip);
	@NotNull
	FRelationshipStatus getRelationshipStatus(@NotNull UniqueId relationShip);
	@NotNull
	@Heavy
	FRelationshipInfo getRelationshipInfo(@NotNull FRelationship<?> relationship);

	boolean isNeutral(@NotNull FRelationship<?> relationship);
	boolean isNeutral(@NotNull OfflinePlayerReference reference);
	boolean isNeutral(@NotNull FactionReference factionReference);

	@Heavy
	FRelationshipInfo markNeutral(@NotNull FRelationship<?> relationship);

	default boolean canBe(@NotNull FRelationship<?> relationship, @NotNull FRelationshipStatus status) {
		return switch (this) {
			case FAlliable<?> fAlliable when relationship instanceof FAlliable && status.equals(FRelationshipStatus.ALLY) ->
					true;
			case FAntagonist<?> fAntagonist when relationship instanceof FAntagonist && status.equals(FRelationshipStatus.ENEMY) ->
					true;
			case FTruce<?> fTruce when relationship instanceof FTruce && status.equals(FRelationshipStatus.TRUCE) -> true;
			default -> status.equals(FRelationshipStatus.NEUTRAL);
		};
	}
}