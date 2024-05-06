package bet.astral.unity.commands.faction.meta.prefix;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.core.FactionSubCommand;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.paper.PaperCommandManager;

@Cloud
public class ForceChangePrefixSC extends FactionSubCommand {
	public ForceChangePrefixSC(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
	}
}
