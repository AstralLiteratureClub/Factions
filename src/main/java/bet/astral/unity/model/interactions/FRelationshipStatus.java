package bet.astral.unity.model.interactions;

public record FRelationshipStatus(String name, int priority) {
	public static final FRelationshipStatus ALLY = new FRelationshipStatus("ally", 100);
	public static final FRelationshipStatus NEUTRAL = new FRelationshipStatus("neutral", 0);
	public static final FRelationshipStatus ENEMY = new FRelationshipStatus("enemy", -100);
}
