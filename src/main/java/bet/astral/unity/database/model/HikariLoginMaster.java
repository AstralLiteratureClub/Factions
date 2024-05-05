package bet.astral.unity.database.model;

import bet.astral.unity.configuration.Configuration;
import lombok.Getter;

@Getter
public class HikariLoginMaster extends SQLLoginMaster {
	private final String tableFactions;
	private final String tableMembers;
	private final String tableHomes;
	private final String testQuery;
	private final int maximumPools;
	private final long minimumIdle;
	private final long idleTimeOut;
	private final long connectionTimeOut;
	private final long maxLifetime;

	public HikariLoginMaster(String connectionString, String user, String password, String tableFactions, String tableMembers, String tableHomes, String testQuery, int maximumPools, long minimumIdle, long idleTimeOut, long connectionTimeOut, long maxLifetime) {
		super(connectionString, user, password);
		this.tableFactions = tableFactions;
		this.tableMembers = tableMembers;
		this.tableHomes = tableHomes;
		this.testQuery = testQuery;
		this.maximumPools = maximumPools;
		this.minimumIdle = minimumIdle;
		this.idleTimeOut = idleTimeOut;
		this.connectionTimeOut = connectionTimeOut;
		this.maxLifetime = maxLifetime;
	}

	protected HikariLoginMaster(SQLLoginMaster master, String tableFactions, String tableMembers, String tableHomes, String testQuery, int maximumPools, long minimumIdle, long idleTimeOut, long connectionTimeOut, long maxLifetime) {
		super(master);
		this.tableFactions = tableFactions;
		this.tableMembers = tableMembers;
		this.tableHomes = tableHomes;
		this.testQuery = testQuery;
		this.maximumPools = maximumPools;
		this.minimumIdle = minimumIdle;
		this.idleTimeOut = idleTimeOut;
		this.connectionTimeOut = connectionTimeOut;
		this.maxLifetime = maxLifetime;
	}

	public static HikariLoginMaster load(Configuration configuration) {
		return new HikariLoginMaster(
				SQLLoginMaster.load(configuration),
				configuration.getString("sql.table.factions"),
				configuration.getString("sql.table.members"),
				configuration.getString("sql.table.homes"),
				configuration.getString("sql.hikari.test-query"),
				configuration.getInt("sql.hikari.maximum-pools"),
				configuration.getLong("sql.hikari.time-out-min-idle"),
				configuration.getLong("sql.hikari.time-out.idle"),
				configuration.getLong("sql.hikari.time-out.connection"),
				configuration.getLong("sql.hikari.maz-life-time")

		);
	}
}