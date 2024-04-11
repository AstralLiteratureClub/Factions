package bet.astral.unity.commands.ban;

import bet.astral.unity.Factions;
import bet.astral.unity.commands.FactionCloudCommand;
import bet.astral.unity.utils.TranslationKey;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.Command;
import org.incendo.cloud.paper.PaperCommandManager;

public class FactionForceBanCommand extends FactionCloudCommand {
	protected final Command.Builder<CommandSender> banRoot;
	public FactionForceBanCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		banRoot = forceRoot
				.literal("ban")
				.commandDescription(loadDescription(TranslationKey.DESCRIPTION_FORCE_BAN, "/factions force ban"))
				.handler(context->{
					rootHelp.queryCommands("factions force ban", context.sender());
				})
				;
	}
}
