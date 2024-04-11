package bet.astral.unity.commands;

import bet.astral.cloudplusplus.command.CloudPPCommand;
import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.unity.Factions;
import bet.astral.unity.messenger.FactionPlaceholderManager;
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
	protected FactionPlaceholderManager placeholderManager;
	public FactionCloudCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		this.root = plugin.getRootFactionCommand();
		this.forceRoot = plugin.getRootForceFactionCommand();
		this.rootHelp = plugin.getRootHelp();
	}

	@Override
	public void reloadMessengers() {
		super.reloadMessengers();
		this.placeholderManager = (FactionPlaceholderManager) messenger.getPlaceholderManager();
	}

	public RichDescription loadDescription(String name, String command, Placeholder... placeholders){
		return plugin.loadDescription(name, command, placeholders);
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
