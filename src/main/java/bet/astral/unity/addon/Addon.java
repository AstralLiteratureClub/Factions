package bet.astral.unity.addon;

import org.incendo.cloud.description.Description;
import org.jetbrains.annotations.NotNull;

public interface Addon {
	@NotNull
	String getName();
	@NotNull
	String getVersion();
	@NotNull
	String[] getAuthors();
	@NotNull
	Description getDescription();

	String getCommandPackage();
}
