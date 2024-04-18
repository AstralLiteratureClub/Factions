package bet.astral.unity.addon.internal;

import bet.astral.unity.Factions;
import bet.astral.unity.addon.Addon;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;

import java.util.Set;
import java.util.stream.Collectors;

@Deprecated(forRemoval = true)
public interface AddonRegistrar {
	default Logger logger() {
		return ((Factions) this).getSLF4JLogger();
	}
	Set<Addon> getAddons();

	@ApiStatus.NonExtendable
	default Set<Addon> getEnabledAddons(){
		return getAddons().stream().filter(Addon::isEnabled).collect(Collectors.toSet());
	}
	@ApiStatus.NonExtendable
	default Set<Addon> getDisabledAddons(){
		return getAddons().stream().filter(Addon::isDisabled).collect(Collectors.toSet());
	}

	PaperCommandManager<CommandSender> getCommandManager();

	default void registerCommands() {
		PaperCommandManager<CommandSender> commandManager = getCommandManager();
		Factions factions = (Factions) this;

		for (Addon addon : getEnabledAddons()){
			if (addon.registerCommands()){
				factions.registerCommands(addon.getCommandPackages(), commandManager);
			} else {
				Logger logger = addon.logger();
				logger.error("Couldn't register commands for factions addon "+ addon.getName());
			}
		}
	}
	void unregisterCommands();
}
