package bet.astral.unity.model;

import bet.astral.unity.Factions;
import bet.astral.unity.utils.refrence.FactionReferenceImpl;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Deprecated(forRemoval = true)
public class FWarPoints extends FactionReferenceImpl {
	private int raidPoints;
	private int combatPoints;

	public FWarPoints(Factions factions, UUID uniqueId) {
		super(factions, uniqueId);
	}

	public void addRaidPoint(int amount){
		this.raidPoints+=amount;
	}

	public void addCombatPoint(int amount){
		this.combatPoints+=amount;
	}

}
