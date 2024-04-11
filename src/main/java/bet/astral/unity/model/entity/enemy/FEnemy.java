package bet.astral.unity.model.entity.enemy;

import bet.astral.unity.model.entity.Participant;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.identity.Identity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class FEnemy<Self, Enemy> implements Participant<Self, Enemy>, Identity {
	private final UUID uniqueId;
	private final Self self;
	private final Enemy enemy;
	@Setter
	@Getter
	private boolean hasWar = false;
	@Getter
	@Setter
	private UUID warId = null;
	private final long created;

	public FEnemy(Self self, Enemy enemy, UUID  uniqueId, long created) {
		this.self = self;
		this.enemy = enemy;
		this.uniqueId = uniqueId;
		this.created = created;
	}

	@Override
	public Self self() {
		return self;
	}

	@Override
	public Enemy participant() {
		return enemy;
	}

	@Override
	public long created() {
		return created;
	}

	@Override
	public java.util.@NotNull UUID uuid() {
		return uniqueId;
	}
}
