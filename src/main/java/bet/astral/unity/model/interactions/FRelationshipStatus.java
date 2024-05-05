package bet.astral.unity.model.interactions;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public record FRelationshipStatus(String name, int priority) {
	public static final FRelationshipStatus ALLY = new FRelationshipStatus("ally", 100);
	public static final FRelationshipStatus NEUTRAL = new FRelationshipStatus("neutral", 0);
	public static final FRelationshipStatus TRUCE = new FRelationshipStatus("truce", -50);
	public static final FRelationshipStatus ENEMY = new FRelationshipStatus("enemy", -100);
}
