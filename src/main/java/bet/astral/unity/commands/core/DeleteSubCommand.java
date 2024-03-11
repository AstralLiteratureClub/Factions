package bet.astral.unity.commands.core;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.permission.Permission;
import bet.astral.messenger.permission.PredicatePermission;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.FactionCloudConfirmableCommand;
import bet.astral.unity.commands.arguments.FactionParser;
import bet.astral.unity.model.FPermission;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.utils.TranslationKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.Command;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.permission.PermissionResult;

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

		Command.Builder<Player> builder = root.literal("disband",
						loadDescription(TranslationKey.DELETE_DESCRIPTION, "/factions disband"),
						"delete")
				.senderType(Player.class)
				.commandDescription(loadDescription(TranslationKey.DELETE_DESCRIPTION, "/factions disband"))
				.permission(PermissionUtils.of("delete", FPermission.DELETE).and(new org.incendo.cloud.permission.PredicatePermission<>() {
					@Override
					public @NonNull PermissionResult testPermission(@NonNull Object sender) {
						Player player = (Player) sender;
						FPlayer fPlayer = plugin.getPlayerManager().convert(player);
						Faction faction = plugin.getFactionManager().get(fPlayer.getFactionId());
						if (faction == null) {
							return PermissionResult.denied(this);
						}

						if (faction.getSuperOwner() == null) {
							return PermissionResult.denied(this);
						}
						if (faction.getSuperOwner().equals(player.getUniqueId())) {
							return PermissionResult.allowed(this);
						}
						return PermissionResult.denied(this);
					}
				}))
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
							} else {
								commandMessenger.message(sender, TranslationKey.MESSAGE_DELETE);
							}
						}
				);
		commandPlayer(builder);
		command(
				forceRoot
						.literal("disband",
								"delete")
						.permission(PermissionUtils.forceOfFactionsExist("disband"))
						.commandDescription(loadDescription(TranslationKey.DESCRIPTION_FORCE_DELETE, "/factions force delete"))
						.required(FactionParser.factionComponent(FactionParser.Mode.NAME)
								.name("faction")
								.description(loadDescription(TranslationKey.DESCRIPTION_FORCE_DELETE_FACTION, "/factions force delete <faction>"))
						)
						.required(StringParser.stringComponent(StringParser.StringMode.GREEDY)
								.name("reason")
								.description(loadDescription(TranslationKey.DESCRIPTION_FORCE_DELETE_REASON, "/factions force delete <reason>"))
						)
						.handler(context -> {
							CommandSender sender = context.sender();
							Faction faction = context.get("faction");
							String reason = context.get("reason");
							PlaceholderList placeholders = new PlaceholderList();
							placeholders.addAll(Faction.factionPlaceholders("faction", faction));
							placeholders.add("reason", reason);
							placeholders.addAll(commandMessenger.createPlaceholders("sender", sender));
							commandMessenger.message(sender, TranslationKey.MESSAGE_FORCE_DELETE_SENDER, placeholders);
							commandMessenger.message(faction, TranslationKey.MESSAGE_FORCE_DELETE_SENDER, placeholders);
							commandMessenger.broadcast(Permission.of((s)->sender instanceof Player player
									&& !faction.getMembers().contains(player.getUniqueId())), TranslationKey.MESSAGE_FORCE_DELETE_SENDER, placeholders);

							plugin.getFactionManager().delete(faction);
						})
		);
	}
}