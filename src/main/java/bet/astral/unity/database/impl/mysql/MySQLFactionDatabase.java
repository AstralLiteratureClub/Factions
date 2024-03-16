package bet.astral.unity.database.impl.mysql;

import bet.astral.unity.database.internal.FactionDatabase;
import bet.astral.unity.database.internal.SQLDatabase;
import bet.astral.unity.database.model.DBFactionMember;
import bet.astral.unity.model.Faction;

import java.util.UUID;

public class MySQLFactionDatabase extends SQLDatabase<UUID, Faction, Faction> implements FactionDatabase<UUID, DBFactionMember, DBFactionMember> {
}
