package bet.astral.unity.database.impl.sql;

import bet.astral.unity.database.impl.sql.source.HikariDatabaseSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class SQLHikariDatabase<K, V, C> extends SQLDatabase<K, V, C> {
	private final HikariDatabaseSource hikariDatabase;

	protected SQLHikariDatabase(HikariDatabaseSource hikariDatabase, Consumer<C> cacheConsumer, Consumer<C> uncacheCOnsumer, Function<C, V> dataToCached, Function<V, C> cachedToData, String table) {
		super(hikariDatabase.getFactions(), null, cacheConsumer, uncacheCOnsumer, dataToCached, cachedToData, table);
		this.hikariDatabase = hikariDatabase;
	}

	public HikariDatabaseSource getHikariDatabase(){
		return hikariDatabase;
	}

	@Override
	public Connection getConnection() {
		try {
			return hikariDatabase.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
