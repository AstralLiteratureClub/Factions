package bet.astral.cloudplusplus.command;


import bet.astral.cloudplusplus.CommandRegisterer;
import bet.astral.cloudplusplus.MessageReload;
import bet.astral.messenger.Messenger;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.Command;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;

public class CloudPPCommand<P extends JavaPlugin, C extends CommandSender> implements MessageReload {
	protected final PaperCommandManager<C> commandManager;
	protected final P plugin;
	protected final CommandRegisterer<P> registerer;

	@Deprecated
	protected Messenger<P> commandMessenger;
	protected Messenger<P> messenger;
	protected Messenger<P> debugMessenger;

	public CloudPPCommand(P plugin, CommandRegisterer<P> registerer, PaperCommandManager<C> commandManager) {
		this.plugin = plugin;
		this.commandManager = commandManager;
		this.registerer = registerer;
		reloadMessengers();
	}
	public CloudPPCommand(P plugin, PaperCommandManager<C> commandManager){
		this.plugin = plugin;
		//noinspection PatternVariableHidesField
		if (!(plugin instanceof CommandRegisterer<?> registerer)){
			throw new RuntimeException("You cannot use JavaPlugin.class, PaperCommandManager.class constructor if you're not extending CommandRegisterer.class in "+plugin.getClass().getName());
		}
		//noinspection unchecked
		this.registerer = (CommandRegisterer<P>) registerer;
		this.commandManager = commandManager;
		reloadMessengers();
	}

	@Override
	public void reloadMessengers() {
		this.messenger = registerer.commandMessenger();
		this.commandMessenger = registerer.commandMessenger();
		this.debugMessenger = registerer.debugMessenger();
	}

	public void command(Command.Builder<C> command){
		commandManager.command(command);
	}

	public Command.Builder<C> commandBuilder(String name,
	                                                     Description description,
	                                                     String... aliases
	                                                     ){
		return commandManager.commandBuilder(name, description, aliases);
	}
	public Command.Builder<C> commandBuilder(String name,
	                                                     String... aliases){
		return commandManager.commandBuilder(name, aliases);
	}
}
