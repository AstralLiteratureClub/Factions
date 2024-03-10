package bet.astral.unity.database;

import bet.astral.unity.Factions;
import bet.astral.unity.annotations.ASync;
import bet.astral.unity.annotations.NotASync;
import bet.astral.unity.model.FPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
/*
 * TODO Make the database delete row if the user's faction is not set.
 *  Deleting the user data would make database lookup faster.
 */
public abstract class PlayerDatabase {
	protected Factions factions;
	protected Connection connection;
	public PlayerDatabase(Factions factions){
		this.factions = factions;
		connect().thenAccept(connection->{
			this.connection = connection;
		});
	}
	/**
	 * Loads a player from the database asynchronously. This method should be used when something is not critical.
	 * @param player player to load
	 * @return completable future, returning possible player
	 */
	@ASync
	@NotNull
	public abstract CompletableFuture<@Nullable FPlayer> load(Player player);

	/**
	 * Loads a player from the database using non-asynchronous ways.
	 * This is generally unsafe, but used in offline player arguments.
	 * @param uniqueId unique id
	 * @return faction player instance
	 */
	@Nullable
	@NotASync
	public abstract FPlayer load(UUID uniqueId);

	/**
	 * Saves the player to the database using asynchronous methods.
	 * @param player player to save
	 */
	@ASync
	public abstract void save(FPlayer player);

	/**
	 * Saves the player to the database using the main thread of the server.
	 * @param player player to save
	 */
	@NotASync
	public abstract void saveUnsafe(FPlayer player);

	/**
	 * Connects to the database asynchronously
	 */
	@ASync
	public abstract CompletableFuture<Connection> connect();

	/**
	 * Deletes a user row from the database.
	 * This is used when a player has no faction and is supposed to be deleted.
	 * <p>This method is asynchronous</p>
	 * @param player player
	 */
	@ASync
	public abstract void delete(FPlayer player);

	/**
	 * Deletes a user row from the database.
	 * This is used when a player has no faction and is supposed to be deleted.
	 * <p>This method is asynchronous</p>
	 * @param player player
	 */
	@NotASync
	public abstract void deleteUnsafe(FPlayer player);

	/**
	 * Returns the connection to the database
	 * @return connection to the database
	 * @throws IllegalAccessException Throws illegal access exception if the connection has not been established yet.
	 */
	public abstract Connection getConnection() throws IllegalAccessException;
}
