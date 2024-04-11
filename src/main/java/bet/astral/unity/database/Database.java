package bet.astral.unity.database;

import bet.astral.unity.Factions;
import bet.astral.unity.database.internal.FactionDatabase;
import bet.astral.unity.database.internal.MemberDatabase;
import bet.astral.unity.database.model.DBFactionMember;
import bet.astral.unity.database.model.LoginMaster;
import bet.astral.unity.model.Faction;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class Database {
	private final Factions factions;
	@Getter(AccessLevel.PUBLIC)
	protected FactionDatabase<UUID, Faction, Faction, UUID, DBFactionMember, DBFactionMember> factionDatabase;
	@Getter(AccessLevel.PUBLIC)
	protected MemberDatabase<UUID, DBFactionMember, DBFactionMember> playerDatabase;


	public Database(Factions factions) {
		this.factions = factions;
	}


	public abstract void connect(LoginMaster loginMaster) throws IllegalArgumentException;
	public abstract void disconnect();

	public abstract boolean isConnected();
}
