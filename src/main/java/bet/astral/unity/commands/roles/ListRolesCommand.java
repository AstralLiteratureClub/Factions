package bet.astral.unity.commands.roles;

import bet.astral.unity.Factions;
import bet.astral.unity.commands.FactionCloudCommand;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.paper.PaperCommandManager;

public class ListRolesCommand extends FactionCloudCommand {
	public ListRolesCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
	}
}
