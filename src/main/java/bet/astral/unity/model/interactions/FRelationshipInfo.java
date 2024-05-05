package bet.astral.unity.model.interactions;

import bet.astral.annotations.Heavy;
import bet.astral.unity.utils.collections.UniqueMap;
import lombok.Getter;

@Getter
@Heavy
public class FRelationshipInfo {
	private final FRelationshipStatus status;
	private final UniqueMap<FAlliable<?>> sharedAllies;
	private final UniqueMap<FAntagonist<?>> sharedEnemies;
	private final UniqueMap<FTruce<?>> sharedTruces;
	private final FRelationship<?> from;
	private final FRelationship<?> to;

	public FRelationshipInfo(FRelationshipStatus status, UniqueMap<FAlliable<?>> sharedAllies, UniqueMap<FAntagonist<?>> sharedEnemies, UniqueMap<FTruce<?>> sharedTruces, FRelationship<?> from, FRelationship<?> relationship) {
		this.status = status;
		this.sharedAllies = sharedAllies;
		this.sharedEnemies = sharedEnemies;
		this.sharedTruces = sharedTruces;
		this.from = from;
		this.to = relationship;
	}
}
