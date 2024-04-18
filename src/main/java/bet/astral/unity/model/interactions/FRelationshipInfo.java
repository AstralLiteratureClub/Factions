package bet.astral.unity.model.interactions;

import bet.astral.unity.utils.collections.UniqueMap;
import lombok.Getter;

@Getter
public class FRelationshipInfo {
	private final FRelationshipStatus status;
	private final UniqueMap<FAlliable> sharedAllies;
	private final UniqueMap<FAntagonist> sharedEnemies;
	private final FRelationship from;
	private final FRelationship to;

	public FRelationshipInfo(FRelationshipStatus status, UniqueMap<FAlliable> sharedAllies, UniqueMap<FAntagonist> sharedEnemies, FRelationship from, FRelationship relationship) {
		this.status = status;
		this.sharedAllies = sharedAllies;
		this.sharedEnemies = sharedEnemies;
		this.from = from;
		to = relationship;
	}
}
