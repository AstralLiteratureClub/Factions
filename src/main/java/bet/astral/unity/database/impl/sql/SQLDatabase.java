package bet.astral.unity.database.impl.sql;

import bet.astral.unity.Factions;
import bet.astral.unity.database.structures.Database;
import bet.astral.unity.database.structures.FactionDatabase;
import bet.astral.unity.managers.FactionManager;
import bet.astral.unity.model.Faction;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class SQLDatabase<K, V, C> implements Database<K, V, C> {
	private final Factions factions;
	@Getter
	private final Connection connection;
	private final Consumer<C> cacheConsumer;
	private final Consumer<C> uncacheCOnsumer;
	private final Function<C, V> dataToCached;
	private final Function<V, C> cachedToData;
	private final String table;

	protected SQLDatabase(Factions factions, Connection connection, Consumer<C> cacheConsumer, Consumer<C> uncacheCOnsumer, Function<C, V> dataToCached, Function<V, C> cachedToData, String table) {
		this.factions = factions;
		this.connection = connection;
		this.cacheConsumer = cacheConsumer;
		this.uncacheCOnsumer = uncacheCOnsumer;
		this.dataToCached = dataToCached;
		this.cachedToData = cachedToData;
		this.table = table;
	}

	protected abstract boolean createTable();
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

	protected void tryToClose(Connection connection) throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
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
	public Factions getFactions() {
		return factions;
	}

	public String tableName(){
		return table;
	}

	@Override
	public void init() {
		try {
			if (createTable()) {
				if (this instanceof FactionDatabase<?, ?, ?, ?, ?, ?, ?, ?, ?> database) {
					database.loadAllFactions()
							.thenAccept((factions) -> {
								FactionManager factionManager = getFactions().getFactionManager();
								factionManager.clearCache();
								for (Faction faction : factions) {
									factionManager.addToCache(faction);
								}
							})
					;
				}
			} else {
				getLogger().error("Couldn't create database named " + tableName() + ". Please check your configuration and restart the server.");
			}
		} catch (Exception e){
			getLogger().error("Couldn't create database named " + tableName()+". Please check your configuration and restart the server.", e);
		}
	}
}
