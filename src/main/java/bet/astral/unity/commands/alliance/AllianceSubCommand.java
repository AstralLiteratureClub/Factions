package bet.astral.unity.commands.alliance;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.core.AllyCloudCommand;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.paper.PaperCommandManager;

@Cloud
public class AllianceSubCommand extends AllyCloudCommand {
	public AllianceSubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		registerPlayer(
				(root)-> root
						.literal("test")
						.handler(context->{
							context.sender().sendMessage("Hey");
						})
				,
				root,
				factionAlly
		);
		register(root->
				root.literal("test")
						.handler(context->{
							context.sender().sendMessage("Hey 2!");
						})
				,
				rootForce,
				factionForceAlly
				);
	}
}
