package bet.astral.unity.commands.alliance;

import bet.astral.unity.Factions;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.paper.PaperCommandManager;

public class AllianceCreateSubCommand extends AllianceTestSubCommand {
	public AllianceCreateSubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		/*
		 *
		 * /alliance request -player %player%
		 * /alliance request %faction%
		 *
		 * /alliance accept %faction%
		 * /alliance deny %faction%
		 * /alliance cancel/revoke %faction%
		 *
		 * /alliance chat %message%
		 *
		 */
	}
}
