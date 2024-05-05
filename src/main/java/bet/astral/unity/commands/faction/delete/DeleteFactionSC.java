package bet.astral.unity.commands.faction.delete;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.core.FactionCloudConfirmableCommand;
import bet.astral.unity.model.FPermission;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.FRole;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.messenger.TranslationKeys;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.permission.PredicatePermission;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Cloud
public class DeleteFactionSC extends FactionCloudConfirmableCommand {
	static DeleteFactionSC instance;
	final Consumer<CommandSender> acceptConsumer;
	final Consumer<CommandSender> timeRanOutConsumer;

	public DeleteFactionSC(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		instance = this;
		acceptConsumer = (sender) -> {
			FPlayer player = plugin.getPlayerManager().convert((Player) sender);
			UUID factionId = player.getFactionId();
			Faction faction = plugin.getFactionManager().get(factionId);

			PlaceholderList placeholders = new PlaceholderList();
			placeholders.addAll(messenger.getPlaceholderManager().senderPlaceholders("sender", sender));
			placeholders.add("faction", faction);
			placeholders.add("faction", faction.getName());

			messenger.message(faction, TranslationKeys.BROADCAST_DELETE_CONFIRM_FACTION_INTERNAL, placeholders);
			messenger.broadcast(PredicatePermission.of(receiver -> Stream.of(faction.audiences()).noneMatch(member -> member.equals(receiver))), TranslationKeys.BROADCAST_DELETE_CONFIRM_FACTION, placeholders);
			messenger.message(player, TranslationKeys.MESSAGE_DELETE_CONFIRM_FACTION, placeholders);

			plugin.getFactionManager().delete(faction, (Player) sender);
		};
		timeRanOutConsumer = (sender) -> {
			FPlayer player = plugin.getPlayerManager().convert((Player) sender);
			messenger.message(player, TranslationKeys.MESSAGE_DELETE_TIME_RAN_OUT);
		};

		Command.Builder<Player> builder = root.literal("disband",
						"delete")
				.senderType(Player.class)
				.commandDescription(loadDescription(TranslationKeys.DELETE_DESCRIPTION, "/factions disband"))
				.permission(PermissionUtils.of("delete", FPermission.DELETE).and(PredicatePermission.of(sender-> {
					Player player = (Player) sender;
					FPlayer fPlayer = plugin.getPlayerManager().convert(player);
					Faction faction = plugin.getFactionManager().get(fPlayer.getFactionId());
					if (faction == null) {
						return false;
					}
					FRole role = faction.getRole(player);
					if (role == null) {
						return false;
					}
					return role.isOwner();
				})))
				.handler(context -> {
							Player sender = context.sender();
							if (!tryConfirm(sender)) {
								instance.
										requestConfirm(sender,
												600,
												instance.acceptConsumer,
												(p) -> {
												},
												instance.timeRanOutConsumer
										);
								messenger.message(sender, TranslationKeys.MESSAGE_DELETE_REQUEST);
							}
						}
				);
		commandPlayer(builder);

	}

}