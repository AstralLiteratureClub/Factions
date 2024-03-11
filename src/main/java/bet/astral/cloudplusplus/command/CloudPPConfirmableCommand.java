package bet.astral.cloudplusplus.command;

import bet.astral.cloudplusplus.CommandRegisterer;
import bet.astral.cloudplusplus.Confirmable;
import org.apache.commons.lang3.tuple.Triple;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.units.qual.C;
import org.incendo.cloud.paper.PaperCommandManager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CloudPPConfirmableCommand<P extends JavaPlugin, C extends CommandSender> extends CloudPPCommand<P, C> implements Confirmable<C> {
	private final Map<C, Triple<BukkitTask, Consumer<C>, Consumer<C>>> confirmable = new HashMap<>();

	public CloudPPConfirmableCommand(P plugin, CommandRegisterer<P> registerer, PaperCommandManager<C> commandManager) {
		super(plugin, registerer, commandManager);
	}

	public CloudPPConfirmableCommand(P plugin, PaperCommandManager<C> commandManager) {
		super(plugin, commandManager);
	}

	@Override
	public boolean hasRequestBending(C sender) {
		return confirmable.get(sender) != null && !confirmable.get(sender).getLeft().isCancelled();
	}

	@Override
	public boolean tryConfirm(C sender) {
		if (hasRequestBending(sender)) {
			Triple<BukkitTask, Consumer<C>, Consumer<C>> triple = confirmable.get(sender);

			triple.getMiddle().accept(sender);
			confirmable.remove(sender);
			return true;
		}
		return false;
	}

	@Override
	public void requestConfirm(C sender, int ticks, Consumer<C> acceptedConsumer, Consumer<C> deniedConsumer, Consumer<C> timeRanOutConsumer) {
		confirmable.put(sender, Triple.of(plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
			timeRanOutConsumer.accept(sender);
			confirmable.remove(sender);
		}, ticks), acceptedConsumer, deniedConsumer));
	}
}