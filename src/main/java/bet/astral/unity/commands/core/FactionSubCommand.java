package bet.astral.unity.commands.core;

import bet.astral.unity.Factions;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.minecraft.extras.MinecraftHelp;
import org.incendo.cloud.paper.PaperCommandManager;

public class FactionSubCommand extends RootCommand {
	public static final String VALUE_PLAYER = "unity/player";
	public static final String VALUE_FACTION = "unity/faction";
	protected final Command.Builder<CommandSender> root;
	protected final Command.Builder<CommandSender> forceRoot;
	protected final MinecraftHelp<CommandSender> rootHelp;
	public FactionSubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		this.root = plugin.getRootFactionCommand();
		this.forceRoot = plugin.getRootForceFactionCommand();
		this.rootHelp = plugin.getRootHelp();

		playerPreHandler = (context)->{
			Player player = context.sender();
			FPlayer fPlayer = plugin.getPlayerManager().convert(player);
			Faction faction = fPlayer.getFaction();

			context.set(VALUE_PLAYER, fPlayer);
			context.set(VALUE_FACTION, faction);
		};
	}
}
