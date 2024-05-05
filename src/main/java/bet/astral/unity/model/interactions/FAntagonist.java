package bet.astral.unity.model.interactions;

import bet.astral.annotations.CommonName;
import bet.astral.annotations.Heavy;
import bet.astral.unity.utils.UniqueId;
import bet.astral.unity.utils.refrence.OfflinePlayerReference;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

@CommonName("enemy")
public interface FAntagonist<D> extends FRelationship<D>, UniqueId {
	@NotNull
	@Heavy
	Set<@NotNull FRelationshipInfo> getEnemies();
	@NotNull
	@Heavy
	default Set<@NotNull FRelationshipInfo> getEnemiesShared(@NotNull FAntagonist<?> antagonist) {
		return getEnemies().stream().filter(truce-> antagonist.isEnemy((FAntagonist<?>) truce.getTo())).collect(Collectors.toSet());
	}

	boolean isEnemy(@NotNull FAntagonist<?> antagonist);
	boolean isEnemy(@NotNull OfflinePlayerReference reference);

	@NotNull
	FRelationshipInfo markEnemy(@NotNull FAntagonist<?> antagonist);
	default boolean canBeEnemy(@NotNull FRelationship<?> relationship){
		return relationship instanceof FAntagonist;
	}
}
