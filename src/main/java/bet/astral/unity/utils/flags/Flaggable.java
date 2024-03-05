package bet.astral.unity.utils.flags;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Flaggable {
	/**
	 * Adds a new flag to the flags.
	 * @param flag flag
	 * @param <V> type
	 */
	<V> void addFlag(@NotNull Flag<V> flag);

	/**
	 * Edits a current value of a flag. If a flag is absent, throws IllegalStateException.
	 * @param key flag key
	 * @param newValue new value
	 * @param <V> type
	 * @throws IllegalStateException if a flag is absent.
	 */
	<V> void editFlag(@NotNull NamespacedKey key, @Nullable V newValue) throws IllegalStateException;

	/**
	 * Sets a flag if the flag is absent from the user.
	 * @param flag flag
	 * @param <V> type
	 */
	<V> void setIfAbsent(@NotNull Flag<V> flag);

	/**
	 * Sets a flag if the flag is absent from the user, with a default value
	 * @param key key for the flag
	 * @param defaultValue default value
	 * @param <V> type
	 */
	<V> void setIfAbsent(@NotNull NamespacedKey key, @Nullable V defaultValue);

	/**
	 * Sets a flag if the flag is absent from the user, with a default value, and a current value
	 * @param key key for the flag
	 * @param defaultValue default value for the flag
	 * @param currentValue current value for the flag
	 * @param <V> type
	 */
	<V> void setIfAbsent(@NotNull NamespacedKey key, @Nullable V defaultValue, @Nullable V currentValue);

	/**
	 * Gets a flag of entity if the flag is set, else returns default flag
	 * @param key key to flag
	 * @param defaultFlag default flag
	 * @return flag, else default flag
	 * @param <V> type
	 */
	@NotNull
	<V> Flag<V> getFlag(@NotNull NamespacedKey key, @NotNull Flag<V> defaultFlag);

	/**
	 * Gets a flag of entity, else null
	 * @param key key to flag
	 * @return flag, else null
	 * @param <V> type
	 */
	@Nullable
	<V> Flag<V> getFlag(@NotNull NamespacedKey key);
}
