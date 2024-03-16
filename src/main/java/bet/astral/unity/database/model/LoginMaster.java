package bet.astral.unity.database.model;

import bet.astral.unity.configuration.Configuration;
import lombok.Getter;

@Getter
public class LoginMaster {
	private final String hostName;
	private final int port;
	private final String database;
	private final String user;
	private final String password;
	private final long timeOut;

	LoginMaster(String hostName, int port, String database, String user, String password, long timeOut) {
		this.hostName = hostName;
		this.port = port;
		this.database = database;
		this.user = user;
		this.password = password;
		this.timeOut = timeOut;
	}
	protected LoginMaster(LoginMaster master){
		this.hostName = master.hostName;
		this.port = master.port;
		this.database = master.database;
		this.user = master.user;
		this.password = master.password;
		this.timeOut = master.timeOut;
	}

	public static LoginMaster load(Configuration configuration) {
		return new LoginMaster(
				configuration.getString("host"),
				configuration.getInt("port"),
				configuration.getString("database"),
				configuration.getString("user"),
				configuration.getString("password"),
				configuration.getLong("timeout")
		);
	}
}
