package bet.astral.unity.database.impl.sql.mysql;

import bet.astral.unity.database.impl.sql.SQLHikariDatabase;
import bet.astral.unity.database.impl.sql.source.HikariDatabaseSource;
import bet.astral.unity.database.structures.HomeDatabase;
import bet.astral.unity.model.Faction;
import bet.astral.unity.model.location.FHome;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class MySQLHomeDatabase extends SQLHikariDatabase<UUID, FHome, FHome> implements HomeDatabase<UUID, FHome, FHome, UUID, Faction, Faction> {
	public MySQLHomeDatabase(HikariDatabaseSource hikariDatabase, String table) {
		super(hikariDatabase, (home)->{}, (home)->{}, (data)->data, (cached)->cached, table);
	}

	@Override
	public CompletableFuture<FHome> load(@NotNull UUID key) {
		return handleExceptions(
				CompletableFuture.supplyAsync(
						()->{
							Connection connection = getConnection();
							if (connection == null){
								throw new IllegalStateException("Couldn't load home for key "+ key + " as connection is null.");
							}

							PreparedStatement statement = null;
							ResultSet resultSet = null;
							try {
								statement = connection.prepareStatement("SELECT * FROM "+ tableName() + " WHERE uniqueId = ?");
								statement.setString(1, key.toString());
								resultSet = statement.executeQuery();
								if (resultSet != null && resultSet.next()){
									double x = resultSet.getDouble("x");
									double y = resultSet.getDouble("y");
									double z = resultSet.getDouble("z");
									float yaw = resultSet.getFloat("yaw");
									float pitch = resultSet.getFloat("pitch");
									String worldName = resultSet.getString("world");
									String name = resultSet.getString("name");
									UUID factionId = UUID.fromString(resultSet.getString("factionId"));

									return new FHome(getFactions(), factionId, key, name, worldName, x, y, z, yaw, pitch);
								}
							} catch (SQLException e) {
								throw new RuntimeException(e);
							} finally {
								try {
									tryToClose(statement);
									tryToClose(resultSet);
								} catch (SQLException e) {
									throw new RuntimeException(e);
								}
							}

							return null;
						}
				)
		);
	}

	@Override
	public CompletableFuture<Void> save(@NotNull FHome value) {
		return CompletableFuture.runAsync(()->{
			Connection connection = getConnection();
			if (connection != null){
				throw new IllegalStateException("");
			}
		});
	}

	@Override
	public CompletableFuture<Void> saveCached(@NotNull FHome cachedValue) {
		return save(cachedValue);
	}

	@Override
	public CompletableFuture<Void> delete(@NotNull UUID key) {
		return CompletableFuture.completedFuture(null);
	}

	@Override
	public CompletableFuture<Void> deleteCached(@NotNull FHome cachedValue) {
		return CompletableFuture.completedFuture(null);
	}

	@Override
	public FHome createDefault(UUID key) {
		return null;
	}

	@Override
	public @NotNull CompletableFuture<@NotNull FHome[]> loadAllHome(Faction value) {
		return CompletableFuture.completedFuture(new FHome[1]);
	}

	@Override
	public @NotNull CompletableFuture<Void> deleteAllHomes(Faction value) {
		return CompletableFuture.completedFuture(null);
	}

	@Override
	protected boolean createTable() {
		Connection connection = getConnection();
		if (connection == null){
			throw new IllegalStateException("Couldn't create database table for " + tableName() + " as connection is null!");
		}
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(
					"CREATE TABLE IF NOT EXITS "+tableName() + "(uniqueId VARCHAR(36), name VARCHAR(20), factionId VARCHAR(36), x DOUBLE, y DOUBLE, z DOUBLE, yaw FLOAT, pitch FLOAT, PRIMARY KEY(uniqueId));");
			statement.execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				tryToClose(connection);
				tryToClose(statement);
			} catch (SQLException e) {
				//noinspection ThrowFromFinallyBlock
				throw new RuntimeException(e);
			}
		}
		return true;
	}

	@Override
	public @NotNull CompletableFuture<@NotNull FHome[]> loadAll() {
		return null;
	}

	@Override
	public @NotNull CompletableFuture<Void> deleteAll() {
		return loadAll().thenAccept(fHomes -> Stream.of(fHomes).forEach(fHome -> delete(fHome.getUniqueId())));
	}
}
