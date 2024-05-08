package bet.astral.unity.commands.alliance;

import bet.astral.unity.Factions;
import bet.astral.unity.commands.core.AllianceSubCommand;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.paper.PaperCommandManager;

public class AllianceTestSubCommand extends AllianceSubCommand {
	public AllianceTestSubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
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
