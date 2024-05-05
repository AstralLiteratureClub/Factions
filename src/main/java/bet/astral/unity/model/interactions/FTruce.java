package bet.astral.unity.model.interactions;

import bet.astral.annotations.CommonName;
import bet.astral.annotations.Heavy;
import bet.astral.unity.utils.UniqueId;
import bet.astral.unity.utils.refrence.OfflinePlayerReference;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

@CommonName("cease fire")
public interface FTruce<D> extends FRelationship<D>, UniqueId {
	@NotNull
	@Heavy
	Set<@NotNull FRelationshipInfo> getTruces();
	@NotNull
	@Heavy
	default Set<@NotNull FRelationshipInfo> getTrucesShared(@NotNull FTruce<?> truced) {
		return getTruces().stream().filter(truce-> truced.isTruced((FTruce<?>) truce.getTo())).collect(Collectors.toSet());
	}

	boolean isTruced(@NotNull FTruce<?> truce);
	boolean isTruced(@NotNull OfflinePlayerReference reference);

	@Heavy
	@NotNull
	FRelationshipInfo markTruced(@NotNull FTruce<?> truce);
	default boolean canBeTruced(@NotNull FRelationship<?> truce){
		return truce instanceof FTruce;
	}
}
