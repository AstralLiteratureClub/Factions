package bet.astral.unity.database.impl;

import bet.astral.unity.Factions;
import bet.astral.unity.database.Database;
import bet.astral.unity.database.impl.mysql.MySQLFactionDatabase;
import bet.astral.unity.database.impl.mysql.MySQLPlayerDatabase;
import bet.astral.unity.database.model.HikariLoginMaster;
import bet.astral.unity.database.model.LoginMaster;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class HikariDatabase extends Database {
	private boolean isConnected = false;
	private HikariDataSource hikari;
	private final String type;

	public HikariDatabase(Factions factions, String type) {
		super(factions);
		this.type = type;
	}

	@Override
	public void connect(LoginMaster loginMaster) throws IllegalArgumentException {
		if (!(loginMaster instanceof HikariLoginMaster hikariLoginMaster)){
			throw new IllegalArgumentException("Trying to connect using hikari was denied, as LoginMaster is not the right format ("+HikariLoginMaster.class.getName()+") found: "+loginMaster.getClass().getName());
		}
		Type type = Type.valueOf(this.type.toUpperCase());
		HikariConfig config = new HikariConfig();
		switch (type){
			case MYSQL -> {
				config.setJdbcUrl(
						"jdbc:mysql://" +
								hikariLoginMaster.getHostName() + ":" +
								hikariLoginMaster.getPort() + "/" +
								hikariLoginMaster.getDatabase());
				config.setUsername(loginMaster.getUser());
				config.setPassword(loginMaster.getPassword());
			}
			case SQLITE -> {
				File file = new File(getFactions().getDataFolder(), loginMaster.getHostName()+".db");
				config.setJdbcUrl(
						"jdbc:sqlite:" +
								file.getName());
			}
		}
		config.setDriverClassName(type.driverClass);
		config.setMinimumIdle(hikariLoginMaster.getMinimumPools());
		config.setMaximumPoolSize(hikariLoginMaster.getMaximumPools());
		config.setConnectionTimeout(hikariLoginMaster.getTimeOut());
		config.setConnectionTestQuery("");
		hikari = new HikariDataSource(config);
		isConnected = true;

		switch (type){
			case MYSQL -> {
				playerDatabase = new MySQLPlayerDatabase(this,
						(player) -> {},
						(player) -> {},
						(player) -> player,
						(player) -> player
						);
				factionDatabase = new MySQLFactionDatabase(this,
						(faction)->getFactions().getFactionManager().removeFromCache(faction),
						(faction)->getFactions().getFactionManager().addToCache(faction),
						(faction)->faction,
						(faction)->faction,
						"factions",
						(MySQLPlayerDatabase) playerDatabase
				);
			}
			case SQLITE -> {
			}
		}
	}

	@Override
	public void disconnect() {

	}

	@Override
	public boolean isConnected() {
		return isConnected;
	}

	public Connection getConnection() throws SQLException {
		return hikari.getConnection();
	}

	public enum Type {
		SQLITE("org.sqlite.JDBC"),
		MYSQL("com.mysql.jdbc.Driver"),
		;

		private final String driverClass;
		Type(String driverClass) {
			this.driverClass = driverClass;
		}
	}
}
