package bet.astral.unity.database.model;

import bet.astral.unity.configuration.Configuration;
import lombok.Getter;

@Getter
public class HikariLoginMaster extends LoginMaster{
	private final int maximumPools;
	private final int minimumPools;

	HikariLoginMaster(String hostName, int port, String database, String user, String password, long timeOut, int maximumPools, int minimumPools) {
		super(hostName, port, database, user, password, timeOut);
		this.maximumPools = maximumPools;
		this.minimumPools = minimumPools;
	}

	protected HikariLoginMaster(LoginMaster master, int maximumPools, int minimumPools) {
		super(master);
		this.maximumPools = maximumPools;
		this.minimumPools = minimumPools;
	}

	public static HikariLoginMaster load(Configuration configuration) {
		LoginMaster superMaster = LoginMaster.load(configuration);
		return new HikariLoginMaster(
				superMaster,
				configuration.getInt("hikari.maximum-pools"),
				configuration.getInt("hikari.minimum-pools")
				);
	}
}
