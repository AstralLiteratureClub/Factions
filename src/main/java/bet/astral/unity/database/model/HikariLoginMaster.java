package bet.astral.unity.database.model;

import bet.astral.unity.configuration.Configuration;
import lombok.Getter;

@Getter
public class HikariLoginMaster extends LoginMaster{
	private final String tableFactions;
	private final String tableMembers;
	private final String tableHomes;
	private final String testQuery;
	private final int maximumPools;
	private final int minimumIdle;

	HikariLoginMaster(String hostName, int port, String database, String user, String password, long timeOut, String tableFactions, String tableMembers, String tableHomes, String testQuery, int maximumPools, int minimumIdle) {
		super(hostName, port, database, user, password, timeOut);
		this.tableFactions = tableFactions;
		this.tableMembers = tableMembers;
		this.tableHomes = tableHomes;
		this.testQuery = testQuery;
		this.maximumPools = maximumPools;
		this.minimumIdle = minimumIdle;
	}

	protected HikariLoginMaster(LoginMaster master, String tableFactions, String tableMembers, String tableHomes, String testQuery, int maximumPools, int minimumIdle) {
		super(master);
		this.tableFactions = tableFactions;
		this.tableMembers = tableMembers;
		this.tableHomes = tableHomes;
		this.testQuery = testQuery;
		this.maximumPools = maximumPools;
		this.minimumIdle = minimumIdle;
	}

	public static HikariLoginMaster load(Configuration configuration) {
		LoginMaster superMaster = LoginMaster.load(configuration);
		return new HikariLoginMaster(
				superMaster,
				configuration.getString("sql.table.factions"),
				configuration.getString("sql.table.members"),
				configuration.getString("sql.table.homes"),
				configuration.getString("hikari.test-query"),
				configuration.getInt("hikari.maximum-pools"),
				configuration.getInt("hikari.minimum-idle")
				);
	}
}
