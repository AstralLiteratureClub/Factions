package bet.astral.unity.commands.ban.faction;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.FactionCloudCommand;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.paper.PaperCommandManager;

@Cloud
public class FactionBanSubCommand extends FactionCloudCommand {
	public FactionBanSubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
	}
}
