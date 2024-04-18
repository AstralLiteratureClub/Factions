package bet.astral.unity.addon;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.Plugin;
import org.incendo.cloud.description.Description;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Deprecated(forRemoval = true)
public interface PluginAddon extends Addon{
	@Override
	default @NotNull String getName() {
		return ((Plugin) this).getPluginMeta().getName();
	}

	@Override
	default @NotNull String getVersion() {
		return ((Plugin) this).getPluginMeta().getVersion();
	}

	@Override
	default @NotNull List<String> getAuthors() {
		return ((Plugin) this).getPluginMeta().getAuthors();
	}

	@Override
	default @NotNull Description getDescription() {
		return Description.of(((Plugin) this).getPluginMeta().getDescription());
	}


	default @NotNull ComponentLogger logger(){
		return ((Plugin) this).getComponentLogger();
	}
}
