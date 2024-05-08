package bet.astral.unity.commands.core;

import bet.astral.cloudplusplus.Confirmable;
import bet.astral.cloudplusplus.Cooldown;
import bet.astral.cloudplusplus.command.CloudPPCommand;
import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.messenger.translation.TranslationKey;
import bet.astral.unity.Factions;
import bet.astral.unity.messenger.FactionPlaceholderManager;
import bet.astral.unity.utils.ReturningConsumer;
import org.apache.commons.lang3.tuple.Triple;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.incendo.cloud.Command;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.execution.CommandExecutionHandler;
import org.incendo.cloud.minecraft.extras.RichDescription;
import org.incendo.cloud.paper.PaperCommandManager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


public class RootCommand extends CloudPPCommand<Factions, CommandSender> implements Confirmable<CommandSender>, Cooldown<CommandSender> {
	private final Map<CommandSender, Triple<BukkitTask, Consumer<CommandSender>, Consumer<CommandSender>>> confirmable = new HashMap<>();
	private final Map<CommandSender, Long> cooldowns = new HashMap<>();
	protected CommandExecutionHandler<Player> playerPreHandler;
	protected FactionPlaceholderManager placeholderManager;

	public RootCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
	}

	@Override
	public void reloadMessengers() {
		super.reloadMessengers();
		this.placeholderManager = (FactionPlaceholderManager) messenger.getPlaceholderManager();
	}
	public RichDescription loadDescription(TranslationKey translationKey, String command, Placeholder... placeholders) {
		return plugin.loadDescription(translationKey, command, placeholders);
	}

	public Command.Builder<Player> commandBuilderPlayer(String name, Description description, String... aliases) {
		return commandManager.commandBuilder(name, description, aliases).senderType(Player.class);
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

	@Override
	public boolean hasRequestBending(CommandSender sender) {
		return confirmable.get(sender) != null && !confirmable.get(sender).getLeft().isCancelled();
	}

	@Override
	public boolean tryConfirm(CommandSender sender) {
		if (hasRequestBending(sender)) {
			Triple<BukkitTask, Consumer<CommandSender>, Consumer<CommandSender>> triple = confirmable.get(sender);

			triple.getMiddle().accept(sender);
			triple.getLeft().cancel();
			confirmable.remove(sender);
			return true;
		}
		return false;
	}

	@Override
	public void requestConfirm(CommandSender sender, int ticks, Consumer<CommandSender> acceptedConsumer, Consumer<CommandSender> deniedConsumer, Consumer<CommandSender> timeRanOutConsumer) {
		confirmable.put(sender, Triple.of(plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
			timeRanOutConsumer.accept(sender);
			confirmable.remove(sender);
		}, ticks), acceptedConsumer, deniedConsumer));
	}

	@Override
	public long getCooldownLength() {
		return 0;
	}

	@Override
	public void setCooldown(CommandSender sender) {
		cooldowns.put(sender, System.currentTimeMillis()+getCooldownLength());
	}

	@Override
	public void resetCooldown(CommandSender sender) {
		cooldowns.remove(sender);
	}

	@Override
	public long getCooldownLeft(CommandSender sender) {
		return cooldowns.get(sender) != null ? cooldowns.get(sender) : 0;
	}

	@Override
	public boolean hasCooldown(CommandSender sender) {
		return cooldowns.get(sender) != null && System.currentTimeMillis() < cooldowns.get(sender);
	}
}
