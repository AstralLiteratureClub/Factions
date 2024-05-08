package bet.astral.unity.commands.core;

import bet.astral.unity.Factions;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.minecraft.extras.MinecraftHelp;
import org.incendo.cloud.paper.PaperCommandManager;

import static bet.astral.unity.commands.core.FactionSubCommand.VALUE_FACTION;
import static bet.astral.unity.commands.core.FactionSubCommand.VALUE_PLAYER;

public class AllianceSubCommand extends RootCommand {
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
	public AllianceSubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		this.root = plugin.getRootAllyCommand();
		this.rootForce = plugin.getRootAllyForceCommand();
		this.factionAlly = plugin.getRootFactionAllyCommand();
		this.factionForceAlly = plugin.getRootFactionForceAllyCommand();
		this.rootHelp = plugin.getAllyRootHelp();
		this.factionHelp = plugin.getRootHelp();

		playerPreHandler = (context)->{
			Player player = context.sender();
			FPlayer fPlayer = plugin.getPlayerManager().convert(player);
			Faction faction = fPlayer.getFaction();

			context.set(VALUE_PLAYER, fPlayer);
			context.set(VALUE_FACTION, faction);
		};
	}
}
