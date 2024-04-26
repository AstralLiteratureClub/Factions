package bet.astral.unity.database.impl.sql.source;

import bet.astral.unity.Factions;
import bet.astral.unity.database.Database;
import bet.astral.unity.database.impl.DatabaseType;
import bet.astral.unity.database.impl.sql.mysql.MySQLFactionDatabase;
import bet.astral.unity.database.impl.sql.mysql.MySQLHomeDatabase;
import bet.astral.unity.database.impl.sql.mysql.MySQLMemberDatabase;
import bet.astral.unity.database.model.HikariLoginMaster;
import bet.astral.unity.database.model.LoginMaster;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class HikariDatabaseSource extends Database {
	private boolean isConnected = false;
	private HikariDataSource hikari;

	public HikariDatabaseSource(Factions factions, DatabaseType type) {
		super(factions, type);
	}

	@Override
	public void connect(LoginMaster loginMaster) throws IllegalArgumentException {
		if (!(loginMaster instanceof HikariLoginMaster hikariLoginMaster)){
			throw new IllegalArgumentException("Trying to connect using hikari was denied, as LoginMaster is not the right format ("+HikariLoginMaster.class.getName()+") found: "+loginMaster.getClass().getName());
		}
		HikariConfig config = getHikariConfig(hikariLoginMaster, hikariLoginMaster, getType());
		hikari = new HikariDataSource(config);
		isConnected = true;

		switch (getType()){
			case MYSQL -> {
				memberDatabase = new MySQLMemberDatabase(
						this,
						hikariLoginMaster.getTableMembers()
				);
				homeDatabase = new MySQLHomeDatabase(this,
						hikariLoginMaster.getTableHomes()
						);
				factionDatabase = new MySQLFactionDatabase(this,
						(faction)->getFactions().getFactionManager().removeFromCache(faction),
						(faction)->getFactions().getFactionManager().addToCache(faction),
						hikariLoginMaster.getTableFactions(),
						memberDatabase,
						homeDatabase
				);
			}
		}

		memberDatabase.init();
		homeDatabase.init();
		factionDatabase.init();
	}

	@NotNull
	private HikariConfig getHikariConfig(HikariLoginMaster loginMaster, HikariLoginMaster hikariLoginMaster, DatabaseType type) {
		HikariConfig config = new HikariConfig();

		switch (type){
			case MYSQL -> {
				config.setJdbcUrl(loginMaster.getConnectionString());
				config.setUsername(loginMaster.getUser());
				config.setPassword(loginMaster.getPassword());
			}
		}
		config.setDriverClassName(type.getActualDataSourceClass());
		config.setMinimumIdle(hikariLoginMaster.getMinimumIdle());
		config.setMaximumPoolSize(hikariLoginMaster.getMaximumPools());
		config.setConnectionTimeout(hikariLoginMaster.getTimeOut());

		if (!hikariLoginMaster.getTestQuery().equalsIgnoreCase("IGNORE")){
			config.setConnectionTestQuery(hikariLoginMaster.getTestQuery());
		}
		return config;
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
}
