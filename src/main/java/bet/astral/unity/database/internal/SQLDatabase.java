package bet.astral.unity.database.internal;

import bet.astral.unity.Factions;
import bet.astral.unity.database.impl.HikariDatabase;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class SQLDatabase<K, V, C> implements Database<K, V, C> {
	@Getter
	private final HikariDatabase hikariDatabase;
	private final Consumer<C> cacheConsumer;
	private final Consumer<C> uncacheCOnsumer;
	private final Function<C, V> dataToCached;
	private final Function<V, C> cachedToData;
	private final String table;

	protected SQLDatabase(HikariDatabase hikariDatabase, Consumer<C> cacheConsumer, Consumer<C> uncacheCOnsumer, Function<C, V> dataToCached, Function<V, C> cachedToData, String table) {
		this.hikariDatabase = hikariDatabase;
		this.cacheConsumer = cacheConsumer;
		this.uncacheCOnsumer = uncacheCOnsumer;
		this.dataToCached = dataToCached;
		this.cachedToData = cachedToData;
		this.table = table;
	}

	public abstract boolean createTable();
	protected void tryToClose(PreparedStatement statement) throws SQLException {
		if (statement != null && !statement.isClosed()) {
			statement.close();
		}
	}
	protected void tryToClose(ResultSet resultSet) throws SQLException {
		if (resultSet != null && !resultSet.isClosed()) {
			resultSet.close();
		}
	}


	@Override
	public void addToCache(@NotNull C value) {
		cacheConsumer.accept(value);
	}

	@Override
	public void removeFromCache(@NotNull C value) {
		uncacheCOnsumer.accept(value);
	}

	@Override
	public @NotNull Consumer<@NotNull C> cacheConsumer() {
		return cacheConsumer;
	}

	@Override
	public @NotNull Consumer<@NotNull C> uncacheConsumer() {
		return uncacheCOnsumer;
	}

	@Override
	public @NotNull Function<@NotNull V, @NotNull C> convertDataToCached() {
		return cachedToData;
	}

	@Override
	public @NotNull Function<@NotNull C, @NotNull V> convertCachedToData() {
		return dataToCached;
	}

	@Override
	public boolean close() {
		if (getConnection() != null){
			try {
				getConnection().close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return true;
	}

	public Connection getConnection() {
		try {
			return hikariDatabase.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Factions getFactions() {
		return hikariDatabase.getFactions();
	}

	public String tableName(){
		return table;
	}
}
