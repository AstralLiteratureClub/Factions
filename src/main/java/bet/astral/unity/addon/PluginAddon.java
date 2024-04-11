package bet.astral.unity.addon;

import bet.astral.unity.Factions;
import org.bukkit.plugin.Plugin;
import org.incendo.cloud.description.Description;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PluginAddon<P extends Plugin> extends Addon{
	P getPlugin();

	@Override
	default @NotNull String getName() {
		return getPlugin().getPluginMeta().getName();
	}

	@Override
	default @NotNull String getVersion() {
		return getPlugin().getPluginMeta().getVersion();
	}

	@Override
	default @NotNull List<String> getAuthors() {
		return getPlugin().getPluginMeta().getAuthors();
	}

	@Override
	default @NotNull Description getDescription() {
		return Description.of(getPlugin().getPluginMeta().getDescription());
	}

	@Override
	default Factions getUnity() {
		return Factions.getPlugin(Factions.class);
	}
}
