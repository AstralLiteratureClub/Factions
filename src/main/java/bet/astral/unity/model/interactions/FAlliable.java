package bet.astral.unity.model.interactions;

import bet.astral.annotations.CommonName;
import bet.astral.annotations.Heavy;
import bet.astral.unity.utils.UniqueId;
import bet.astral.unity.utils.refrence.OfflinePlayerReference;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

@CommonName("alliance")
public interface FAlliable<D> extends FRelationship<D>, UniqueId {
	@NotNull
	@Heavy
	Set<@NotNull FRelationshipInfo> getAllies();
	@NotNull
	@Heavy
	default Set<@NotNull FRelationshipInfo> getAlliesShared(@NotNull FAlliable<?> alliable) {
		return getAllies().stream().filter(truce-> alliable.isAllied((FAlliable<?>) truce.getTo())).collect(Collectors.toSet());
	}

	boolean isAllied(@NotNull FAlliable<?> alliable);
	boolean isAllied(@NotNull OfflinePlayerReference reference);

	@NotNull
	FRelationshipInfo markAlly(@NotNull FAlliable<?> alliable);

	default boolean canBeAlly(@NotNull FRelationship<?> relationship) {
		return relationship instanceof FAlliable;
	}
}
