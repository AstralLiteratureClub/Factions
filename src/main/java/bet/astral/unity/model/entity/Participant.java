package bet.astral.unity.model.entity;

public interface Participant<Self, Entity> {
	Self self();
	Entity participant();
	long created();
}
