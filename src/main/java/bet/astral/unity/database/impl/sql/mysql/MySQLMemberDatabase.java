package bet.astral.unity.database.impl.sql.mysql;

import bet.astral.unity.database.impl.sql.source.HikariDatabaseSource;
import bet.astral.unity.database.impl.sql.SQLHikariDatabase;
import bet.astral.unity.database.model.DBFactionMember;
import bet.astral.unity.database.structures.MemberDatabase;
import bet.astral.unity.model.FRole;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.UniqueId;
import bet.astral.unity.utils.refrence.OfflinePlayerReference;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class MySQLMemberDatabase extends SQLHikariDatabase<UUID, DBFactionMember, DBFactionMember> implements MemberDatabase<UUID, DBFactionMember, DBFactionMember> {
	public MySQLMemberDatabase(HikariDatabaseSource hikariDatabase, String table) {
		super(hikariDatabase, (member)->{}, (member)->{}, (member)->member, (member)->member, table);
	}


	@Override
	protected boolean createTable() {
		Connection connection = getConnection();
		if (connection == null) {
			getLogger().error("Couldn't create a table as connection is null!", new IllegalStateException("Couldn't create table as connection is null!"));
			return false;
		}
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + tableName() + " (uniqueId VARCHAR(36), factionId VARCHAR(36), role VARCHAR(10), prefix JSON, PRIMARY KEY (uniqueId));");
			statement.execute();
			return true;
		} catch (SQLException e) {
			getLogger().error("Failed to create a database table for member data.", e);
		} finally {
			try {
				tryToClose(statement);
			} catch (SQLException e) {
				getLogger().error("Failed to close a statement.", e);
			}
		}
		return false;
	}

	@Override
	public CompletableFuture<DBFactionMember> load(@NotNull UUID key) {
		return handleExceptions(
				CompletableFuture.supplyAsync(() -> {
					Connection connection = getConnection();
					if (connection == null) {
						throw new IllegalStateException("Couldn't load player profile as connection is null!");
					}
					PreparedStatement getStatement = null;
					ResultSet resultSet = null;
					try {
						getStatement = connection.prepareStatement("SELECT * WHERE uniqueId = ?");
						getStatement.setString(1, uuidToString(key));
						resultSet = getStatement.executeQuery();
						if (resultSet != null && resultSet.next()) {
							return load(resultSet, key);
						}
						return null;
					} catch (SQLException e) {
						throw new RuntimeException(e);
					} finally {
						try {
							tryToClose(getStatement);
							tryToClose(resultSet);
							tryToClose(connection);
						} catch (SQLException e) {
							throw new RuntimeException(e);
						}
					}
				}));
	}

	private DBFactionMember load(ResultSet resultSet, UUID key) throws SQLException {
		Optional<Faction> faction = getFactions().getFactionManager().getPlayerFaction(key);
		if (faction.isEmpty()) {
			return null;
		}
		return new DBFactionMember(
				key,
				GsonComponentSerializer.gson()
						.deserialize(
								resultSet.getString("prefix")
						),
				faction.get(),
				FRole.valueOf(resultSet.getString("role"))
		);
	}

	@Override
	public CompletableFuture<Void> save(@NotNull DBFactionMember value) {
		return handleExceptions(CompletableFuture.runAsync(() -> {
			if (value.getFaction() == null) {
				return;
			}
			try (Connection connection = getConnection()) {
				if (connection == null) {
					getLogger().info("Couldn't save a member as connection is null!", new IllegalStateException());
					return;
				}
				try (PreparedStatement getStatement = connection.prepareStatement("SELECT * FROM " + tableName() + " WHERE uniqueId = ?");
				     ResultSet resultSet = getStatement.executeQuery()) {
					PreparedStatement saveStatement;
					if (resultSet != null && resultSet.next()) {
						saveStatement = connection.prepareStatement("UPDATE " + tableName() + " SET factionId = ?, role = ?, prefix = ? WHERE uniqueId = ?");
						saveStatement.setString(1, uuidToString(value.getFactionId()));
						saveStatement.setString(2, value.getFaction() != null ? value.getFaction().getRole(value).getName() : null);
						saveStatement.setString(3, value.getFaction() != null && value.getFaction().hasPrivatePrefix(value) ?
								GsonComponentSerializer.gson().serialize(value.getFaction().getPrivatePrefix(value).asComponent()) : null);
						saveStatement.setString(4, uuidToString(value.uuid()));
						saveStatement.executeUpdate();
					} else {
						saveStatement = connection.prepareStatement("INSERT INTO " + tableName() + " (factionId, role, prefix, uniqueId) VALUES (?, ?, ?, ?)");
						saveStatement.setString(1, value.getFaction().getUniqueIdString());
						saveStatement.setString(2, value.getFaction() != null ? value.getFaction().getRole(value).getName() : null);
						saveStatement.setString(3, value.getFaction() != null && value.getFaction().hasPrivatePrefix(value) ?
								GsonComponentSerializer.gson().serialize(value.getFaction().getPrivatePrefix(value).asComponent()) : null);
						saveStatement.setString(4, uuidToString(value.getUniqueId()));
						saveStatement.executeUpdate();
					}
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}));
	}

	@Override
	public CompletableFuture<Void> saveCached(@NotNull DBFactionMember cachedValue) {
		return handleExceptions(
				save(convertCachedToData().apply(cachedValue)));
	}

	@Override
	public CompletableFuture<Void> delete(@NotNull UUID key) {
		return handleExceptions(
				CompletableFuture.runAsync(() -> {
					Connection connection = getConnection();
					if (connection != null) {
						throw new IllegalStateException("Couldn't delete a player as connection is null!");
					}
					PreparedStatement deleteStatement = null;
					try {
						deleteStatement = connection.prepareStatement("DELETE FROM " + tableName() + " WHERE uniqueId = ?");
						deleteStatement.setString(1, uuidToString(key));
						deleteStatement.executeUpdate();
					} catch (SQLException e) {
						throw new RuntimeException(e);
					} finally {
						try {
							tryToClose(deleteStatement);
							tryToClose(connection);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}));
	}

	@Override
	public CompletableFuture<Void> deleteCached(@NotNull DBFactionMember cachedValue) {
		return delete(cachedValue.getUniqueId());
	}

	@Override
	public @NotNull CompletableFuture<Void> delete(@NotNull UniqueId uniqueId) {
		return delete(uniqueId.getUniqueId());
	}

	@Override
	public @NotNull CompletableFuture<Void> delete(@NotNull OfflinePlayer uniqueId) {
		return delete(uniqueId.getUniqueId());
	}

	@Override
	public DBFactionMember createDefault(UUID key) {
		Optional<Faction> optFac = getFactions().getFactionManager().getPlayerFaction(key);
		if (optFac.isPresent()) {
			Faction faction = optFac.get();
			if (faction.hasPrivatePrefix(OfflinePlayerReference.of(key))) {
				return new DBFactionMember(
						key,
						null,
						faction,
						FRole.MEMBER
				);
			}
			return new DBFactionMember(key,
					null,
					faction,
					FRole.MEMBER
			);
		}
		return new DBFactionMember(key,
				null,
				null,
				FRole.MEMBER
		);
	}

	@Override
	public @NotNull CompletableFuture<DBFactionMember[]> getAllMembers(@NotNull Faction faction, @NotNull UUID factionId) {
		return handleExceptions(
				CompletableFuture.supplyAsync(() -> {
					List<DBFactionMember> members = new LinkedList<>();

					Connection connection = getConnection();
					if (connection == null) {
						throw new IllegalStateException("Couldn't load members from faction " + factionId + " as the connection to database is null!");
					}

					PreparedStatement getStatement = null;
					ResultSet resultSet = null;
					try {
						getStatement = connection.prepareStatement("SELECT * WHERE factionId = ?");
						getStatement.setString(1, uuidToString(factionId));
						resultSet = getStatement.executeQuery();
						while (resultSet != null && resultSet.next()) {
							UUID uniqueId = UUID.fromString(resultSet.getString("uniqueId"));
							String roleStr = resultSet.getString("role");
							String prefixStr = resultSet.getString("prefix");
							FRole role = FRole.valueOf(roleStr);
							Component prefix = GsonComponentSerializer.gson()
									.deserialize(prefixStr);

							members.add(
									new DBFactionMember(
											uniqueId,
											prefix,
											faction,
											role));
						}
					} catch (SQLException e) {
						throw new RuntimeException(e);
					} finally {
						try {
							tryToClose(getStatement);
							tryToClose(resultSet);
							tryToClose(connection);
						} catch (SQLException e) {
							throw new RuntimeException(e);
						}
					}

					return members.toArray(DBFactionMember[]::new);
				}));
	}

	@Override
	public @NotNull CompletableFuture<Void> deleteAllMembers(@NotNull Faction faction, @NotNull UUID factionId) {
		return
				handleExceptions(
						CompletableFuture.runAsync(() -> {
							getAllMembers(faction, factionId)
									.thenAccept((u) -> {
										for (DBFactionMember member : u) {
											deleteCached(member);
										}
									});
						}));
	}

	@Override
	public @NotNull CompletableFuture<@NotNull DBFactionMember[]> loadAll() {
		return handleExceptions(
				CompletableFuture.supplyAsync(() -> {
					Connection connection = getConnection();
					if (connection == null) {
						throw new IllegalStateException("Couldn't load all members from the members database as the database connection is not set!");
					}

					PreparedStatement statement = null;
					ResultSet resultSet = null;
					try {
						List<DBFactionMember> members = new LinkedList<>();
						statement = connection.prepareStatement("SELECT * FROM "+ tableName());
						resultSet = statement.executeQuery();
						if (resultSet != null){
							while (resultSet.next()){
								UUID key = UUID.fromString(resultSet.getString("uniqueId"));
								members.add(load(resultSet, key));
							}
						}
						return members.toArray(DBFactionMember[]::new);
					} catch (SQLException e) {
						throw new RuntimeException(e);
					} finally {
						try {
							tryToClose(statement);
							tryToClose(resultSet);
							tryToClose(connection);
						} catch (SQLException e) {
							throw new RuntimeException(e);
						}
					}
				}));
	}

	@Override
	public @NotNull CompletableFuture<Void> deleteAll() {
		return loadAll().thenAccept(members-> Stream.of(members).forEach(member->delete(member.getUniqueId())));
	}
}
