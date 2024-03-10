package bet.astral.unity.commands.basic;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.permission.PredicatePermission;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.FactionCloudConfirmableCommand;
import bet.astral.unity.model.FPermission;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.utils.TranslationKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.paper.PaperCommandManager;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Cloud
public class DeleteSubCommand extends FactionCloudConfirmableCommand {
	private final Consumer<CommandSender> acceptConsumer;
	private final Consumer<CommandSender> timeRanOutConsumer;

	public DeleteSubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		acceptConsumer = (sender) -> {
			FPlayer player = plugin.getPlayerManager().convert((Player) sender);
			UUID factionId = player.getFactionId();
			Faction faction = plugin.getFactionManager().get(factionId);

			PlaceholderList placeholders = new PlaceholderList();
			placeholders.addAll(commandMessenger.createPlaceholders((Player) sender));
			placeholders.addAll(Faction.factionPlaceholders("faction", faction));

			commandMessenger.message(faction, TranslationKey.BROADCAST_DELETE_CONFIRM_FACTION_INTERNAL, placeholders);
			commandMessenger.broadcast(new PredicatePermission(receiver -> Stream.of(faction.audiences()).noneMatch(member -> member.equals(receiver))), TranslationKey.BROADCAST_DELETE_CONFIRM_FACTION, placeholders);
			commandMessenger.message(player, TranslationKey.MESSAGE_DELETE_CONFIRM_FACTION, placeholders);

			plugin.getFactionManager().delete(faction, (Player) sender);
		};
		timeRanOutConsumer = (sender) -> {
			FPlayer player = plugin.getPlayerManager().convert((Player) sender);
			commandMessenger.message(player, TranslationKey.MESSAGE_DELETE_TIME_RAN_OUT);
		};

		Command.Builder<Player> builder = commandBuilder("disband",
				loadDescription(TranslationKey.DELETE_DESCRIPTION, "/factions disband"),
				"delete")
				.senderType(Player.class)
				.permission(PermissionUtils.of("delete", FPermission.DELETE))
				.handler(context -> {
							Player sender = context.sender();
							if (!tryConfirm(sender)) {
								requestConfirm(sender,
										600,
										acceptConsumer,
										(p) -> {
										},
										timeRanOutConsumer
								);
							}
						}
				);
		commandPlayer(builder);
	}
}