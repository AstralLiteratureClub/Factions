package bet.astral.unity.database.impl.mysql;

import bet.astral.unity.database.impl.HikariDatabase;
import bet.astral.unity.database.internal.MemberDatabase;
import bet.astral.unity.database.internal.SQLDatabase;
import bet.astral.unity.database.model.DBFactionMember;
import bet.astral.unity.model.FRole;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.refrence.OfflinePlayerReference;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class MySQLPlayerDatabase extends SQLDatabase<UUID, DBFactionMember, DBFactionMember> implements MemberDatabase<UUID, DBFactionMember> {
	public MySQLPlayerDatabase(HikariDatabase hikariDatabase, Consumer<DBFactionMember> cacheConsumer, Consumer<DBFactionMember> uncacheCOnsumer, Function<DBFactionMember, DBFactionMember> dataToCached, Function<DBFactionMember, DBFactionMember> cachedToData) {
		super(hikariDatabase, cacheConsumer, uncacheCOnsumer, dataToCached, cachedToData, "players");
	}


	@Override
	public boolean createTable() {
		Connection connection = getConnection();
		if (connection == null){
			throw new IllegalStateException("Couldn't create table as connection is null!");
		}
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS "+tableName()+" (uniqueId VARCHAR(36), factionId VARCHAR(36), role VARCHAR(10), prefix JSON, PRIMARY KEY (uniqueId));");
			statement.execute();
			return true;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				tryToClose(statement);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public CompletableFuture<DBFactionMember> load(@NotNull UUID key) {
		return CompletableFuture.supplyAsync(()->{
			Connection connection = getConnection();
			if (connection == null){
				throw new IllegalStateException("Couldn't load player profile as connection is null!");
			}
			PreparedStatement getStatement = null;
			ResultSet resultSet = null;
			try {
				getStatement = connection.prepareStatement("GET * WHERE uniqueId = ?");
				getStatement.setString(1, uuidToString(key));
				resultSet = getStatement.executeQuery();
				if (resultSet != null && resultSet.next()){
					Optional<Faction> faction = getFactions().getFactionManager().getPlayerFaction(key);
					if (faction.isEmpty())  {
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
				return null;
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally {
				try {
					tryToClose(getStatement);
					tryToClose(resultSet);
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	@Override
	public CompletableFuture<Void> save(@NotNull DBFactionMember value) {
		return CompletableFuture.runAsync(()->{
			Connection connection = getConnection();
			if (connection == null){
				throw new IllegalStateException("Couldn't save a player as connection is null!");
			}
			PreparedStatement getStatement = null;
			ResultSet resultSet = null;
			PreparedStatement saveStatement = null;
			try {
				getStatement = connection.prepareStatement("GET * FROM "+ tableName() + " WHERE uniqueId = ?");
				resultSet = getStatement.executeQuery();
				if (resultSet != null && resultSet.next()){
					saveStatement = connection.prepareStatement("UPDATE "+ tableName()+ " SET factionId = ?, role = ?, prefix = ? WHERE uniqueId = ?");
					saveStatement.setString(1,uuidToString(value.getFactionId()));
					saveStatement.setString(2, value.getFaction() != null ? value.getFaction().getRole(value).getName() : null);
					saveStatement.setString(3, value.getFaction() != null ? value.getFaction().hasPrivatePrefix(value) ?
									GsonComponentSerializer.gson()
													.serialize(value.getFaction().getPrivatePrefix(value).asComponent()) : null : null);
					saveStatement.setString(4, uuidToString(value.uuid()));
					saveStatement.executeUpdate();
				} else {
					saveStatement = connection.prepareStatement("INSERT INTO "+ tableName()+ " VALUES uniqueId, factionId, role, prefix WHERE ?, ?, ?, ?");
					saveStatement.setString(1, uuidToString(value.getUniqueId()));
					saveStatement.setString(2, uuidToString(value.getFactionId()));
					saveStatement.setString(3, value.getFaction() != null ? value.getFaction().getRole(value).getName() : null);
					saveStatement.setString(4, value.getFaction() != null ? value.getFaction().hasPrivatePrefix(value) ?
							GsonComponentSerializer.gson()
									.serialize(value.getFaction().getPrivatePrefix(value).asComponent()) : null : null);
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally {
				try {
					tryToClose(getStatement);
					tryToClose(resultSet);
					tryToClose(saveStatement);
				} catch (SQLException e) {
					//noinspection CallToPrintStackTrace
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public CompletableFuture<Void> saveCached(@NotNull DBFactionMember cachedValue) {
		return save(convertCachedToData().apply(cachedValue));
	}

	@Override
	public CompletableFuture<Void> delete(@NotNull UUID key) {
		return CompletableFuture.runAsync(()->{
			Connection connection = getConnection();
			if (connection != null){
				throw new IllegalStateException("Couldn't delete a player as connection is null!");
			}
			PreparedStatement deleteStatement = null;
			try {
				deleteStatement = connection.prepareStatement("DELETE FROM "+ tableName() + " WHERE uniqueId = ?");
				deleteStatement.setString(1, uuidToString(key));
				deleteStatement.executeUpdate();
			}catch (SQLException e){
				throw new RuntimeException(e);
			} finally {
				try {
					tryToClose(deleteStatement);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public CompletableFuture<Void> deleteCached(@NotNull DBFactionMember cachedValue) {
		return delete(cachedValue.getUniqueId());
	}

	@Override
	public DBFactionMember createDefault(UUID key) {
		Optional<Faction> optFac = getFactions().getFactionManager().getPlayerFaction(key);
		if (optFac.isPresent()){
			Faction faction = optFac.get();
			if (faction.hasPrivatePrefix(OfflinePlayerReference.of(key))) {
				return new DBFactionMember(
						key,
						null,
						faction,
						FRole.DEFAULT
						);
			}
			return new DBFactionMember(key,
					null,
					faction,
					FRole.DEFAULT
					);
		}
		return new DBFactionMember(key,
				null,
				null,
				FRole.DEFAULT
		);
	}

	@Override
	public @NotNull CompletableFuture<DBFactionMember[]> getAllMembers(@NotNull Faction faction, @NotNull UUID factionId) {
		return CompletableFuture.supplyAsync(()->{
			List<DBFactionMember> members = new LinkedList<>();

			Connection connection = getConnection();
			if (connection == null){
				throw new IllegalStateException("Couldn't load members from faction "+ factionId + " as the connection to database is null!");
			}

			PreparedStatement getStatement = null;
			ResultSet resultSet = null;
			try {
				getStatement = connection.prepareStatement("GET * WHERE factionId = ?");
				getStatement.setString(1, uuidToString(factionId));
				resultSet = getStatement.executeQuery();
				while (resultSet != null && resultSet.next()){
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
			} finally{
				try {
					tryToClose(getStatement);
					tryToClose(resultSet);
				}catch (SQLException e){
					throw new RuntimeException(e);
				}
			}

			return members.toArray(DBFactionMember[]::new);
		});
	}

	@Override
	public @NotNull CompletableFuture<Void> deleteAllMembers(@NotNull Faction faction, @NotNull UUID factionId) {
		return CompletableFuture.runAsync(()->{
			getAllMembers(faction, factionId)
					.thenAccept((u)->{
							for (DBFactionMember member : u){
								deleteCached(member);
					}
					});
		});
	}
}
