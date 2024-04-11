package bet.astral.unity.database.impl.mysql;

import bet.astral.unity.database.impl.HikariDatabase;
import bet.astral.unity.database.internal.FactionDatabase;
import bet.astral.unity.database.internal.MemberDatabase;
import bet.astral.unity.database.internal.SQLDatabase;
import bet.astral.unity.database.model.DBFactionMember;
import bet.astral.unity.model.Faction;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class MySQLFactionDatabase extends SQLDatabase<UUID, Faction, Faction> implements FactionDatabase<UUID, Faction, Faction, UUID, DBFactionMember, DBFactionMember> {
	private final MySQLPlayerDatabase playerDatabase;
	public MySQLFactionDatabase(HikariDatabase hikariDatabase, Consumer<Faction> cacheConsumer, Consumer<Faction> uncacheCOnsumer, Function<Faction, Faction> dataToCached, Function<Faction, Faction> cachedToData, String table, MySQLPlayerDatabase playerDatabase) {
		super(hikariDatabase, cacheConsumer, uncacheCOnsumer, dataToCached, cachedToData, table);
		this.playerDatabase = playerDatabase;
	}

	@Override
	public CompletableFuture<Faction> load(@NotNull UUID key) {
		return null;
	}

	@Override
	public CompletableFuture<Void> save(@NotNull Faction value) {
		return null;
	}

	@Override
	public CompletableFuture<Void> saveCached(@NotNull Faction cachedValue) {
		return save(cachedValue);
	}

	@Override
	public CompletableFuture<Void> delete(@NotNull UUID key) {
		return CompletableFuture.runAsync(()->{
			Connection connection = getConnection();
			if (connection == null){
				throw new RuntimeException("Couldn't delete faction for key " + key + " because the connection is null!");
			}
			Faction faction = getFactions().getFactionManager().get(key);
			if (faction == null){
				faction = createDefault(key);
			}
			PreparedStatement deleteStatement = null;
			try {
				deleteStatement = connection.prepareStatement("DELETE FROM factions WHERE uniqueId = ?");
				deleteStatement.setString(1, uuidToString(key));
				deleteStatement.executeUpdate();
				getMemberDatabase()
						.deleteAllMembers(faction, key);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally {
				try {
					tryToClose(deleteStatement);
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	@Override
	public CompletableFuture<Void> deleteCached(@NotNull Faction cachedValue) {
		return delete(cachedValue.getUniqueId());
	}

	@Override
	public Faction createDefault(UUID key) {
		return new Faction(getFactions(), key, "$Faction", System.currentTimeMillis());
	}

	@Override
	public MemberDatabase<UUID, DBFactionMember, DBFactionMember> getMemberDatabase() {
		return playerDatabase;
	}

	@Override
	public boolean createTable() {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		if (connection == null){
			throw new IllegalStateException("Couldn't create the faction table because connection is null!");
		}
		try {
			statement = connection.prepareStatement("CREATE TABLE IF NOT EXITS factions (uniqueId VARCHAR(36), ");
			statement.execute();
			return true;
		} catch (SQLException e){
			throw new RuntimeException(e);
		} finally {
			try {
				tryToClose(statement);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
