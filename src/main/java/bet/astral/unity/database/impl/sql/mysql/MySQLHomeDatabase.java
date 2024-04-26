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
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class MySQLHomeDatabase extends SQLHikariDatabase<UUID, FHome, FHome> implements HomeDatabase<UUID, FHome, FHome, UUID, Faction, Faction> {
	public MySQLHomeDatabase(HikariDatabaseSource hikariDatabase, String table) {
		super(hikariDatabase, (home) -> {
		}, (home) -> {
		}, (data) -> data, (cached) -> cached, table);
	}

	@Override
	public CompletableFuture<FHome> load(@NotNull UUID key) {
		return handleExceptions(
				CompletableFuture.supplyAsync(
						() -> {
							Connection connection = getConnection();
							if (connection == null) {
								throw new IllegalStateException("Couldn't load home for key " + key + " as connection is null.");
							}

							PreparedStatement statement = null;
							ResultSet resultSet = null;
							try {
								statement = connection.prepareStatement("SELECT * FROM " + tableName() + " WHERE uniqueId = ?");
								statement.setString(1, key.toString());
								resultSet = statement.executeQuery();
								if (resultSet != null && resultSet.next()) {
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
		return handleExceptions(
				CompletableFuture.runAsync(() -> {
					Connection connection = getConnection();
					if (connection != null) {
						throw new IllegalStateException("Couldn't save " + value.getUniqueId() + "(" + value.getName() + ") as connection to database is null!");
					}
					PreparedStatement getOld = null;
					ResultSet oldResult = null;
					PreparedStatement update = null;
					try {
						getOld = connection.prepareStatement("SELECT * FROM " + tableName() + " WHERE uniqueId = ?");
						getOld.setString(1, value.getUniqueIdString());
						oldResult = getOld.executeQuery();
						if (oldResult != null && oldResult.next()) {
							update = connection.prepareStatement("UPDATE " + tableName() + " SET name = ?, factionId = ?, world = ?, x = ?, y = ?, z = ?, yaw = ?, pitch = ? WHERE uniqueId = ?");
							update.setString(1, value.getName());
							update.setString(2, value.getFactionId().toString());
							update.setString(3, value.getWorldName());
							update.setDouble(4, value.getX());
							update.setDouble(5, value.getY());
							update.setDouble(6, value.getY());
							update.setFloat(7, value.getYaw());
							update.setFloat(8, value.getPitch());
							update.setString(9, value.getUniqueIdString());
							update.executeUpdate();
						} else {
							update = connection.prepareStatement("INSERT INTO " + tableName() + " VALUES (uniqueId = ?, name = ?, factionId = ?, world = ?, x = ?, y = ?, z = ?, yaw = ?, pitch = ?");
							update.setString(1, value.getUniqueIdString());
							update.setString(2, value.getName());
							update.setString(3, value.getFactionId().toString());
							update.setString(4, value.getWorldName());
							update.setDouble(5, value.getX());
							update.setDouble(6, value.getY());
							update.setDouble(7, value.getY());
							update.setFloat(8, value.getYaw());
							update.setFloat(9, value.getPitch());
							update.executeUpdate();
						}
					} catch (SQLException e) {
						throw new RuntimeException(e);
					} finally {
						try {
							tryToClose(connection);
							tryToClose(getOld);
							tryToClose(oldResult);
							tryToClose(update);
						} catch (SQLException e) {
							throw new RuntimeException(e);
						}
					}
				}));
	}

	@Override
	public CompletableFuture<Void> saveCached(@NotNull FHome cachedValue) {
		return save(cachedValue);
	}

	@Override
	public CompletableFuture<Void> delete(@NotNull UUID key) {
		return handleExceptions(
				CompletableFuture.runAsync(() -> {
					Connection connection = getConnection();
					if (connection == null) {
						throw new IllegalStateException("Couldn't delete a home from database as connection is null!");
					}

					PreparedStatement statement = null;
					try {
						statement = connection.prepareStatement("DELETE FROM " + tableName() + " WHERE uniqueId = ?");
						statement.setString(1, key.toString());
						statement.executeUpdate();
					} catch (SQLException e) {
						throw new RuntimeException(e);
					} finally {
						try {
							tryToClose(connection);
							tryToClose(statement);
						} catch (SQLException e) {
							throw new RuntimeException(e);
						}
					}
				})
		);
	}

	@Override
	public CompletableFuture<Void> deleteCached(@NotNull FHome cachedValue) {
		return delete(cachedValue.getUniqueId());
	}

	@Override
	public FHome createDefault(UUID key) {
		return new FHome(getFactions(), key, key, key.toString(), key.toString(), 0, 0, 0, 0, 0);
	}

	@Override
	public @NotNull CompletableFuture<@NotNull FHome[]> loadAllHome(Faction value) {
		return
				handleExceptions(
						CompletableFuture.supplyAsync(() -> {
							Connection connection = getConnection();
							if (connection == null) {
								throw new IllegalStateException("Couldn't load faction's homes as connection is null!");
							}

							PreparedStatement getStatement = null;
							ResultSet resultSet = null;

							Collection<FHome> homes = new HashSet<>();
							try {
								getStatement = connection.prepareStatement("SELECT * FROM " + tableName() + " WHERE factionId = ?");
								getStatement.setString(1, value.getUniqueIdString());
								resultSet = getStatement.executeQuery();
								if (resultSet != null) {
									while (resultSet.next()) {
										double x = resultSet.getDouble("x");
										double y = resultSet.getDouble("y");
										double z = resultSet.getDouble("z");
										float yaw = resultSet.getFloat("yaw");
										float pitch = resultSet.getFloat("pitch");
										String worldName = resultSet.getString("world");
										String name = resultSet.getString("name");
										UUID factionId = UUID.fromString(resultSet.getString("factionId"));
										UUID uniqueId = UUID.fromString(resultSet.getString("uniqueId"));

										homes.add(new FHome(getFactions(), factionId, uniqueId, name, worldName, x, y, z, yaw, pitch));
									}
								}
							} catch (SQLException e) {
								throw new RuntimeException(e);
							} finally {
								try {
									tryToClose(connection);
									tryToClose(getStatement);
									tryToClose(resultSet);
								} catch (SQLException e) {
									throw new RuntimeException(e);
								}
							}
							return homes.toArray(FHome[]::new);
						}));
	}

	@Override
	public @NotNull CompletableFuture<Void> deleteAllHomes(Faction value) {
		Collection<CompletableFuture<Void>> delete = new HashSet<>();
		value.getHomesById().keySet()
				.forEach(id -> delete.add(delete(id)));

		return handleExceptions(CompletableFuture.allOf(delete.toArray(CompletableFuture[]::new)));
	}

	@Override
	protected boolean createTable() {
		Connection connection = getConnection();
		if (connection == null) {
			throw new IllegalStateException("Couldn't create database table for " + tableName() + " as connection is null!");
		}
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(
					"CREATE TABLE IF NOT EXITS " + tableName() + "(uniqueId VARCHAR(36), name VARCHAR(20), factionId VARCHAR(36), world VARCHAR(20), x DOUBLE, y DOUBLE, z DOUBLE, yaw FLOAT, pitch FLOAT, PRIMARY KEY(uniqueId));");
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
		return handleExceptions(CompletableFuture.supplyAsync(() -> {
			Connection connection = getConnection();
			if (connection == null) {
				throw new IllegalStateException("Couldn't load all homes from database as connection is null!");
			}
			Collection<FHome> homes = new HashSet<>();

			PreparedStatement statement = null;
			ResultSet resultSet = null;
			try {
				statement = connection.prepareStatement("SELECT * FROM " + tableName());
				resultSet = statement.executeQuery();
				if (resultSet != null) {
					while (resultSet.next()) {
						double x = resultSet.getDouble("x");
						double y = resultSet.getDouble("y");
						double z = resultSet.getDouble("z");
						float yaw = resultSet.getFloat("yaw");
						float pitch = resultSet.getFloat("pitch");
						String worldName = resultSet.getString("world");
						String name = resultSet.getString("name");
						UUID factionId = UUID.fromString(resultSet.getString("factionId"));
						UUID uniqueId = UUID.fromString(resultSet.getString("uniqueId"));

						homes.add(new FHome(getFactions(), factionId, uniqueId, name, worldName, x, y, z, yaw, pitch));
					}
				}

				return homes.toArray(FHome[]::new);
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
		}));
	}

	@Override
	public @NotNull CompletableFuture<Void> deleteAll() {
		return handleExceptions(loadAll().thenAccept(fHomes -> Stream.of(fHomes).forEach(fHome -> delete(fHome.getUniqueId()))));
	}
}
