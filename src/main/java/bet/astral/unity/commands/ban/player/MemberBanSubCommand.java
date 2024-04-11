package bet.astral.unity.commands.ban.player;

import bet.astral.unity.Factions;
import bet.astral.unity.commands.ban.FactionForceBanCommand;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.paper.PaperCommandManager;

public class MemberBanSubCommand extends FactionForceBanCommand {
	public MemberBanSubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);

		/*
		 * FORCE
		 */
		command(
				banRoot.
						literal(
								"member",
								"player"
						)
						.commandDescription(
								loadDescription(

								)
						)
		);
	}
}
