package bet.astral.unity.database;

import bet.astral.unity.Factions;
import bet.astral.unity.database.impl.DatabaseType;
import bet.astral.unity.database.structures.FactionDatabase;
import bet.astral.unity.database.structures.HomeDatabase;
import bet.astral.unity.database.structures.MemberDatabase;
import bet.astral.unity.database.model.DBFactionMember;
import bet.astral.unity.database.model.LoginMaster;
import bet.astral.unity.model.Faction;
import bet.astral.unity.model.location.FHome;
import lombok.Getter;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

import java.util.UUID;

@Getter
public abstract class Database {
	private final Factions factions;
	protected FactionDatabase<UUID, Faction, Faction, UUID, DBFactionMember, DBFactionMember, UUID, FHome, FHome> factionDatabase;
	protected MemberDatabase<UUID, DBFactionMember, DBFactionMember> memberDatabase;
	protected HomeDatabase<UUID, FHome, FHome, UUID, Faction, Faction> homeDatabase;
	private final DatabaseType type;

	public ComponentLogger getLogger(){
		return factions.getComponentLogger();
	}


	public Database(Factions factions, DatabaseType type) {
		this.factions = factions;
		this.type = type;
	}

	public abstract void connect(LoginMaster loginMaster) throws IllegalArgumentException;
	public abstract void disconnect();

	public abstract boolean isConnected();
}
