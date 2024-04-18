package bet.astral.unity.model.interactions;

import bet.astral.unity.utils.UniqueId;
import bet.astral.unity.utils.refrence.OfflinePlayerReference;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface FAntagonist extends FRelationship, UniqueId {
	@NotNull
	Set<@NotNull FRelationshipInfo> getEnemies();
	@NotNull
	Set<@NotNull FRelationshipInfo> getEnemiesShared(@NotNull FAntagonist antagonist);

	boolean isEnemy(@NotNull FAntagonist antagonist);
	boolean isEnemy(@NotNull OfflinePlayerReference reference);

	@NotNull
	FRelationshipInfo markEnemy(@NotNull FAntagonist antagonist);
	default boolean canBeEnemy(@NotNull FRelationship relationship){
		return relationship instanceof FAntagonist;
	}
}
