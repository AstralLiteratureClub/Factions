package bet.astral.unity.database.model;

import bet.astral.unity.configuration.Configuration;
import lombok.Getter;

@Getter
public class SQLLoginMaster {
	private final String connectionString;
	private final String user;
	private final String password;
	private final long timeOut;

	public SQLLoginMaster(String connectionString, String user, String password, long timeOut) {
		this.connectionString = connectionString;
		this.user = user;
		this.password = password;
		this.timeOut = timeOut;
	}

	protected SQLLoginMaster(SQLLoginMaster master){
		this.user = master.user;
		this.connectionString = master.connectionString;
		this.password = master.password;
		this.timeOut = master.timeOut;
	}

	public static SQLLoginMaster load(Configuration configuration) {
		return new SQLLoginMaster(
				configuration.getString("sql.url"),
				configuration.getString("sql.user"),
				configuration.getString("sql.password"),
				configuration.getLong("sql.time-out")
		);
	}
}
