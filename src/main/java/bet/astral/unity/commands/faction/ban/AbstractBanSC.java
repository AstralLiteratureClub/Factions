package bet.astral.unity.commands.faction.ban;

import bet.astral.unity.Factions;
import bet.astral.unity.commands.core.FactionCloudCommand;
import bet.astral.unity.messenger.TranslationKeys;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.Command;
import org.incendo.cloud.paper.PaperCommandManager;

public class AbstractBanSC extends FactionCloudCommand {
	protected final Command.Builder<CommandSender> banRoot;
	public AbstractBanSC(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		banRoot = forceRoot
				.literal("ban")
				.commandDescription(loadDescription(TranslationKeys.DESCRIPTION_FORCE_BAN, "/factions force ban"))
				.handler(context->{
					rootHelp.queryCommands("factions force ban", context.sender());
				})
				;
	}
}
