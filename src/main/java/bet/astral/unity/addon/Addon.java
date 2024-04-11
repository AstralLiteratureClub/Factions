package bet.astral.unity.addon;

import bet.astral.unity.Factions;
import org.incendo.cloud.description.Description;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Addon {
	@NotNull
	String getName();
	@NotNull
	String getVersion();
	@NotNull
	List<String> getAuthors();
	@NotNull
	Description getDescription();
	String getCommandPackage();

	boolean registerCommands();
	Factions getUnity();

	@ApiStatus.NonExtendable
	default boolean requestLink() {
		Factions unity = getUnity();
		if (unity == null){
			return false;
		}
		return true;
	}
}
