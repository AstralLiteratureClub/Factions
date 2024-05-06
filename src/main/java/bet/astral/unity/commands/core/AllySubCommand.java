package bet.astral.unity.commands.core;

import bet.astral.cloudplusplus.command.CloudPPCommand;
import bet.astral.unity.Factions;
import bet.astral.unity.messenger.FactionPlaceholderManager;
import bet.astral.unity.utils.ReturningConsumer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.minecraft.extras.MinecraftHelp;
import org.incendo.cloud.minecraft.extras.RichDescription;
import org.incendo.cloud.paper.PaperCommandManager;

public class AllySubCommand extends CloudPPCommand<Factions, CommandSender> {
	// faction ally
	protected final Command.Builder<CommandSender> factionAlly;
	// faction force ally
	protected final Command.Builder<CommandSender> factionForceAlly;
	// /ally
	protected final Command.Builder<CommandSender> root;
	// /ally force
	protected final Command.Builder<CommandSender> rootForce;
	protected final MinecraftHelp<CommandSender> rootHelp;
	protected final MinecraftHelp<CommandSender> factionHelp;
	protected FactionPlaceholderManager placeholderManager;
	public AllySubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		this.root = plugin.getRootAllyCommand();
		this.rootForce = plugin.getRootAllyForceCommand();
		this.factionAlly = plugin.getRootFactionAllyCommand();
		this.factionForceAlly = plugin.getRootFactionForceAllyCommand();
		this.rootHelp = plugin.getAllyRootHelp();
		this.factionHelp = plugin.getRootHelp();
	}


	@Override
	public void reloadMessengers() {
		super.reloadMessengers();
		this.placeholderManager = (FactionPlaceholderManager) messenger.getPlaceholderManager();
	}
	public RichDescription loadDescription(String name, String command){
		return plugin.loadDescription(name, command);
	}


	@SafeVarargs
	public final void register(ReturningConsumer<Command.Builder<CommandSender>> consumer, Command.Builder<CommandSender>... roots){
		for (Command.Builder<CommandSender> root : roots){
			command(consumer.accept(root));
		}
	}
	@SafeVarargs
	public final void registerPlayer(ReturningConsumer<Command.Builder<Player>> consumer, Command.Builder<CommandSender>... roots){
		for (Command.Builder<CommandSender> root : roots){
			commandPlayer(consumer.accept(root.senderType(Player.class)));
		}
	}

	public void commandPlayer(Command<Player> command) {
		commandManager.command(command);
	}

	public void commandPlayer(Command.Builder<Player> command) {
		commandManager.command(command);
	}
}
