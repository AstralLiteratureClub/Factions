package bet.astral.unity.commands;

import bet.astral.cloudplusplus.command.CloudPPCommand;
import bet.astral.unity.Factions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.minecraft.extras.MinecraftHelp;
import org.incendo.cloud.minecraft.extras.RichDescription;
import org.incendo.cloud.paper.PaperCommandManager;

public class FactionCloudCommand extends CloudPPCommand<Factions, CommandSender> {
	protected final Command.Builder<CommandSender> root;
	protected final Command.Builder<CommandSender> forceRoot;
	protected final MinecraftHelp<CommandSender> rootHelp;
	public FactionCloudCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		this.root = plugin.getRootFactionCommand();
		this.forceRoot = plugin.getRootForceFactionCommand();
		this.rootHelp = plugin.getRootHelp();
	}



	public RichDescription loadDescription(String name, String command){
		return plugin.loadDescription(name, command);
	}


	public void commandPlayer(Command<Player> command) {
		commandManager.command(command);
	}

	public void commandPlayer(Command.Builder<Player> command) {
		commandManager.command(command);
	}

	public Command.Builder<Player> commandBuilderPlayer(String name, Description description, String... aliases) {
		return commandManager.commandBuilder(name, description, aliases).senderType(Player.class);
	}

}
