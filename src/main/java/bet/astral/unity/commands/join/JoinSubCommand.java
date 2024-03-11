package bet.astral.unity.commands.join;


import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.FactionCloudCommand;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.paper.PaperCommandManager;

@Cloud
public class JoinSubCommand extends FactionCloudCommand {
	public JoinSubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
	}
}
