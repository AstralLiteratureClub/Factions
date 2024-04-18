package bet.astral.unity.database.impl.sql.source;

import bet.astral.unity.Factions;
import bet.astral.unity.database.Database;
import bet.astral.unity.database.impl.DatabaseType;
import bet.astral.unity.database.model.LoginMaster;

public class SQLDatabaseSource extends Database {
	public SQLDatabaseSource(Factions factions, DatabaseType type) {
		super(factions, type);
	}

	@Override
	public void connect(LoginMaster loginMaster) throws IllegalArgumentException {

	}

	@Override
	public void disconnect() {

	}

	@Override
	public boolean isConnected() {
		return false;
	}
}
