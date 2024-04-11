package bet.astral.unity.commands.plugin;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.FactionCloudCommand;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;

@Cloud
public class AuthorSubCommand extends FactionCloudCommand {
	public AuthorSubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		command(
				root.literal("author")
						.commandDescription(Description.of("Shows the author of this factions plugin."))
						.handler(context->{
							context.sender().sendRichMessage(
									     "<yellow><strikethrough>                                                    <reset>\n"+
											"<yellow>Plugin: <white>"+plugin.getPluginMeta().getName()+"\n"+
											"<yellow>Author(s): <white>"+plugin.getPluginMeta().getAuthors().get(0)+"\n"+
											"<yellow>Version: <white>"+plugin.getPluginMeta().getVersion()+"\n"+
											"<yellow>Description: <white>"+plugin.getPluginMeta().getDescription()+"\n"+
											"<yellow><strikethrough>                                                    <reset>"
							);
						})
		);
	}
}
