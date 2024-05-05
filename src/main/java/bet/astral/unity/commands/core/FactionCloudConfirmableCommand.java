package bet.astral.unity.commands.core;

import bet.astral.cloudplusplus.Confirmable;
import bet.astral.unity.Factions;
import org.apache.commons.lang3.tuple.Triple;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitTask;
import org.incendo.cloud.paper.PaperCommandManager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class FactionCloudConfirmableCommand extends FactionCloudCommand implements Confirmable<CommandSender> {
	private final Map<CommandSender, Triple<BukkitTask, Consumer<CommandSender>, Consumer<CommandSender>>> confirmable = new HashMap<>();
	public FactionCloudConfirmableCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
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
}