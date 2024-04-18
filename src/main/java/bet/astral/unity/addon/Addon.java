package bet.astral.unity.addon;

import bet.astral.unity.Factions;
import org.incendo.cloud.description.Description;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.List;

@Deprecated(forRemoval = true)
public interface Addon {
	boolean isDisabled();
	default boolean isEnabled() {
		return !isDisabled();
	}
	@ApiStatus.OverrideOnly
	@MustBeInvokedByOverriders
	default boolean shouldRegisterCommands() {
		return false;
	}
	@NotNull
	String getName();
	@NotNull
	String getVersion();
	@NotNull
	List<String> getAuthors();
	@NotNull
	Description getDescription();
	List<String> getCommandPackages();

	@NotNull
	Logger logger();

	@ApiStatus.NonExtendable
	default boolean registerCommands() {
		if (isDisabled()){
			return true;
		}
		Factions factions = getUnity();
		if (getCommandPackages() == null || getCommandPackages().isEmpty()){
			return false;
		}
		factions.registerCommands(getCommandPackages(), factions.getCommandManager());
		return true;
	}

	@ApiStatus.NonExtendable
	default boolean unregisterCommands() {
		Factions factions = getUnity();
		return getCommandPackages() != null && !getCommandPackages().isEmpty();
	}

	@NotNull
	@ApiStatus.NonExtendable
	default Factions getUnity() {
		return Factions.getPlugin(Factions.class);
	}
	@ApiStatus.NonExtendable
	default boolean requestLink() {
		Factions unity = getUnity();
		if (unity == null){
			return false;
		}
		return true;
	}
}
