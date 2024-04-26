package bet.astral.unity.database.impl.sql.mysql;

import bet.astral.unity.database.impl.sql.source.HikariDatabaseSource;
import bet.astral.unity.database.impl.sql.SQLHikariDatabase;
import bet.astral.unity.database.structures.FactionDatabase;
import bet.astral.unity.database.structures.HomeDatabase;
import bet.astral.unity.database.structures.MemberDatabase;
import bet.astral.unity.database.model.DBFactionMember;
import bet.astral.unity.gson.RolePrefixMapSerializer;
import bet.astral.unity.model.Faction;
import bet.astral.unity.model.location.FHome;
import bet.astral.unity.utils.collections.PrefixMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class MySQLFactionDatabase extends SQLHikariDatabase<UUID, Faction, Faction> implements FactionDatabase<UUID, Faction, Faction, UUID, DBFactionMember, DBFactionMember, UUID, FHome, FHome> {
	private final Gson gson = new GsonBuilder()
			.registerTypeAdapter(PrefixMap.class, new RolePrefixMapSerializer())
			.create();
	private final MemberDatabase<UUID, DBFactionMember, DBFactionMember> playerDatabase;
	private final HomeDatabase<UUID, FHome, FHome, UUID, Faction, Faction> homeDatabase;
	public MySQLFactionDatabase(HikariDatabaseSource hikariDatabase, Consumer<Faction> cacheConsumer, Consumer<Faction> uncacheCOnsumer, String table, MemberDatabase<UUID, DBFactionMember, DBFactionMember> playerDatabase, HomeDatabase<UUID, FHome, FHome, UUID, Faction, Faction> homeDatabase) {
		super(hikariDatabase, cacheConsumer, uncacheCOnsumer, (data)->data, (cached)->cached, table);
		this.playerDatabase = playerDatabase;
		this.homeDatabase = homeDatabase;
	}
	@Override
	public @NotNull CompletableFuture<@NotNull Faction[]> loadAllFactions() {
		return handleExceptions(
				CompletableFuture.supplyAsync(()->{
					Connection connection = getConnection();
					if (connection == null){
						throw new IllegalStateException("Couldn't load factions as connection is null!");
					}
					PreparedStatement statement = null;
					ResultSet resultSet = null;
					try {
						Collection<Faction> factions = new LinkedList<>();
						statement = connection.prepareStatement("SELECT * FROM "+ tableName());
						resultSet = statement.executeQuery();
						if (resultSet != null){
							while (resultSet.next()){
								Faction faction = load(resultSet);
								factions.add(faction);
							}
						}
						return factions.toArray(Faction[]::new);
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
				})
		);
	}
	@Override
	public CompletableFuture<Faction> load(@NotNull UUID key) {
		return handleExceptions(
				CompletableFuture.supplyAsync(()->{
					Connection connection = getConnection();
					if (connection == null){
						throw new IllegalStateException("Couldn't load a faction as connection is null!");
					}
					PreparedStatement statement = null;
					ResultSet resultSet = null;
					try {
						statement = connection.prepareStatement("SELECT * FROM "+ tableName() + " WHERE uniqueId = ?");
						statement.setString(1, uuidToString(key));
						resultSet = statement.executeQuery();
						if (resultSet != null && resultSet.next()){
							return load(resultSet);
						}
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
					return null;
				})
		);
	}

	private Faction load(ResultSet resultSet) throws SQLException {
		UUID uniqueId = UUID.fromString(resultSet.getString("uniqueId"));
		long firstCreated = resultSet.getLong("created");
		String name = resultSet.getString("name");

		GsonComponentSerializer serializer = GsonComponentSerializer.gson();
		String displayJson = resultSet.getString("displayname");
		Component displayname = serializer.deserialize(displayJson);

		String descriptionJson = resultSet.getString("description");
		Component description = serializer.deserialize(descriptionJson);

		String joinInfoJson = resultSet.getString("joinInfo");
		Component joinInfo = serializer.deserialize(joinInfoJson);

		String privatePrefixesJson = resultSet.getString("privateRolePrefixes");
		PrefixMap privatePrefixes = gson.fromJson(privatePrefixesJson, PrefixMap.class);

		String publicPrefixesJson = resultSet.getString("publicRolePrefixes");
		PrefixMap publicPrefixes = gson.fromJson(publicPrefixesJson, PrefixMap.class);

		Faction faction = new Faction(
				getFactions(),
				uniqueId,
				firstCreated,
				name,
				displayname,
				description,
				joinInfo,
				privatePrefixes,
				publicPrefixes
		);

		CompletableFuture.allOf(
				getHomeDatabase().loadAllHome(faction)
						.thenAccept(homes -> {
							for (FHome home : homes) {
								faction.addHome(home);
							}
						}),
				getMemberDatabase().getAllMembers(faction, faction.getFactionId())
						.thenAccept((members) -> {
							for (DBFactionMember member : members) {
								faction.addMember(member);
							}
						})
		);
		return faction;
	}

	@Override
	public CompletableFuture<Void> save(@NotNull Faction value) {
		return handleExceptions(
				CompletableFuture.supplyAsync(()->{
					Connection connection = getConnection();
					if (connection == null){
						throw new IllegalStateException("Couldn't get connection for factions database while trying to save a faction!");
					}
					GsonComponentSerializer jsonSerializer = GsonComponentSerializer.gson();

					PreparedStatement getStatement = null;
					ResultSet getResult = null;
					PreparedStatement saveStatement = null;
					try {
						getStatement = connection.prepareStatement("GET * FROM "+ tableName() + " WHERE uniqueId = ?");
						getStatement.setString(1, value.getUniqueIdString());
						getResult = getStatement.executeQuery();
						if (getResult != null && getResult.next()){
							// EXISTS
							saveStatement = connection
									.prepareStatement("UPDATE "+ tableName() +
											" SET name = ?, public = ?, created = ?, displayname = ?, description = ?, joinInfo = ?, privateRolePrefixes = ?, publicRolePrefixes = ?, bannedPlayers = ? WHERE uniqueId = ?");
							saveStatement.setString(1, value.getName());
							saveStatement.setBoolean(2, value.isPublic());
							saveStatement.setLong(3, value.getFirstCreated());
							saveStatement.setString(4, jsonSerializer.serialize(value.getDisplayname()));
							saveStatement.setString(5, jsonSerializer.serialize(value.getDescription()));
							saveStatement.setString(6, jsonSerializer.serialize(value.getJoinInfo()));
							saveStatement.setString(7, gson.toJson(value.getPrivateRolePrefixes()));
							saveStatement.setString(8, gson.toJson(value.getPublicRolePrefixes()));
							saveStatement.setString(9, gson.toJson(value.getBanned()));
							saveStatement.setString(10, value.getUniqueIdString());
						} else {
							// NEW
							saveStatement = connection.
									prepareStatement("INSERT INTO " + tableName() + " VALUES (uniqueId, name, public, created, displayname, " +
											"description, joinInfo, privateRolePrefixes, publicRolePrefixes, bannedPlayers) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
							saveStatement.setString(1, value.getUniqueIdString());
							saveStatement.setString(2, value.getName());
							saveStatement.setBoolean(3, value.isPublic());
							saveStatement.setLong(4, value.getFirstCreated());
							saveStatement.setString(5, jsonSerializer.serialize(value.getDisplayname()));
							saveStatement.setString(6, jsonSerializer.serialize(value.getDescription()));
							saveStatement.setString(7, jsonSerializer.serialize(value.getJoinInfo()));
							saveStatement.setString(8, gson.toJson(value.getPrivateRolePrefixes()));
							saveStatement.setString(9, gson.toJson(value.getPublicRolePrefixes()));
							saveStatement.setString(10, gson.toJson(value.getBanned()));
						}
					} catch (SQLException e){
						throw new RuntimeException(e);
					}
					finally {
						try {
							tryToClose(getStatement);
							tryToClose(getResult);
							tryToClose(saveStatement);
							tryToClose(connection);
						} catch (SQLException e){
							throw new RuntimeException(e);
						}
					}
					return null;
				})
		);
	}


	@Override
	public CompletableFuture<Void> saveCached(@NotNull Faction cachedValue) {
		return save(cachedValue);
	}

	@Override
	public CompletableFuture<Void> delete(@NotNull UUID key) {
		return handleExceptions(
				CompletableFuture.runAsync(() -> {
					Connection connection = getConnection();
					if (connection == null) {
						throw new RuntimeException("Couldn't delete faction for key " + key + " because the connection is null!");
					}
					Faction faction = getFactions().getFactionManager().get(key);
					if (faction == null) {
						faction = createDefault(key);
					}
					PreparedStatement deleteStatement = null;
					try {
						deleteStatement = connection.prepareStatement("DELETE FROM "+tableName()+" WHERE uniqueId = ?");
						deleteStatement.setString(1, uuidToString(key));
						deleteStatement.executeUpdate();
						getMemberDatabase()
								.deleteAllMembers(faction, key);
						getHomeDatabase().deleteAllHomes(faction);
					} catch (SQLException e) {
						throw new RuntimeException(e);
					} finally {
						try {
							tryToClose(deleteStatement);
							tryToClose(connection);
						} catch (SQLException e) {
							throw new RuntimeException(e);
						}
					}
				}));
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
	public @NotNull MemberDatabase<UUID, DBFactionMember, DBFactionMember> getMemberDatabase() {
		return playerDatabase;
	}

	@Override
	public @NotNull HomeDatabase<UUID, FHome, FHome, UUID, Faction, Faction> getHomeDatabase() {
		return homeDatabase;
	}


	@Override
	protected boolean createTable() {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		if (connection == null){
			try {
				throw new IllegalStateException("Couldn't create the faction table because connection is null!");
			} catch (IllegalStateException e){
				throw  new RuntimeException(e);
			}
		}
		try {
			statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS "+tableName()+" (uniqueId VARCHAR(36), name VARCHAR(20), public BOOL, created BIGINT, displayname JSON, description JSON, joinInfo JSON, privateRolePrefixes JSON, publicRolePrefixes JSON, bannedPlayers JSON, PRIMARY KEY(uniqueId));");
			statement.execute();
			return true;
		} catch (SQLException e){
			throw new RuntimeException(e);
		} finally {
			try {
				tryToClose(statement);
				tryToClose(connection);
			} catch (SQLException e) {
				//noinspection ThrowFromFinallyBlock
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public @NotNull CompletableFuture<@NotNull Faction[]> loadAll() {
		return loadAllFactions();
	}

	@Override
	public @NotNull CompletableFuture<Void> deleteAll() {
		return loadAll().thenAccept(factions -> Stream.of(factions).forEach(faction->delete(faction.getUniqueId())));
	}
}
