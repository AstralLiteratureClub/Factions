package bet.astral.unity.database;

import bet.astral.unity.Factions;
import bet.astral.unity.database.model.DBFactionMember;
import bet.astral.unity.database.model.LoginMaster;
import bet.astral.unity.model.Faction;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class Database {
	private final Factions factions;
	@Getter(AccessLevel.NONE)
	protected bet.astral.unity.database.internal.Database<UUID, Faction, Faction> factionDatabase;
	@Getter(AccessLevel.NONE)
	protected bet.astral.unity.database.internal.Database<UUID, DBFactionMember, DBFactionMember> playerDatabase;


	public Database(Factions factions) {
		this.factions = factions;
	}


	public abstract void connect(LoginMaster loginMaster) throws IllegalArgumentException;
	public abstract void disconnect();

	public abstract boolean isConnected();
}
