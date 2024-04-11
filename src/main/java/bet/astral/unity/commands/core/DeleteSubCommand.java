package bet.astral.unity.commands.core;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.Messenger;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.FactionCloudConfirmableCommand;
import bet.astral.unity.commands.arguments.FactionParser;
import bet.astral.unity.messenger.FactionPlaceholderManager;
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
import org.incendo.cloud.parser.flag.CommandFlag;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.permission.PermissionResult;
import org.incendo.cloud.permission.PredicatePermission;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Cloud
public class DeleteSubCommand extends FactionCloudConfirmableCommand {
	private static DeleteSubCommand instance;
	private final Consumer<CommandSender> acceptConsumer;
	private final Consumer<CommandSender> timeRanOutConsumer;

	public DeleteSubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
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

			messenger.message(faction, TranslationKey.BROADCAST_DELETE_CONFIRM_FACTION_INTERNAL, placeholders);
			messenger.broadcast(PredicatePermission.of(receiver -> Stream.of(faction.audiences()).noneMatch(member -> member.equals(receiver))), TranslationKey.BROADCAST_DELETE_CONFIRM_FACTION, placeholders);
			messenger.message(player, TranslationKey.MESSAGE_DELETE_CONFIRM_FACTION, placeholders);

			plugin.getFactionManager().delete(faction, (Player) sender);
		};
		timeRanOutConsumer = (sender) -> {
			FPlayer player = plugin.getPlayerManager().convert((Player) sender);
			messenger.message(player, TranslationKey.MESSAGE_DELETE_TIME_RAN_OUT);
		};

		Command.Builder<Player> builder = root.literal("disband",
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
								instance.
										requestConfirm(sender,
												600,
												instance.acceptConsumer,
												(p) -> {
												},
												instance.timeRanOutConsumer
										);
								messenger.message(sender, TranslationKey.MESSAGE_DELETE_REQUEST);
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
								.description(loadDescription(TranslationKey.DESCRIPTION_FORCE_DELETE_REASON, "/factions force delete <faction> <reason>"))
						)
						.flag(CommandFlag.builder("silent")
								.withDescription(loadDescription(TranslationKey.DESCRIPTION_FORCE_DELETE_SILENT, "/factions force delete <faction> <reason> [--silent]"))
								.build()
						)
						.handler(context -> {
							CommandSender sender = context.sender();
							Faction faction = context.get("faction");
							String reason = context.get("reason");
							boolean isSilent = context.flags().isPresent("silent");
							handleForce(sender, faction, reason, isSilent, messenger);
						})
		);
	}

	public static void handleForced(Player player){
		instance.
				requestConfirm(player,
				600,
				instance.acceptConsumer,
				(p) -> {
				},
				instance.timeRanOutConsumer
		);
		instance.tryConfirm(player);
	}

	public static void handleForce(CommandSender sender, Faction faction, String reason, boolean isSilent, Messenger<Factions> commandMessenger) {
		PlaceholderList placeholders = new PlaceholderList();
		placeholders.addAll(((FactionPlaceholderManager) commandMessenger.getPlaceholderManager()).factionPlaceholders("faction", faction));
		placeholders.add("reason", reason);
		placeholders.addAll(commandMessenger.getPlaceholderManager().senderPlaceholders("sender", sender));
		commandMessenger.message(sender, TranslationKey.MESSAGE_FORCE_DELETE_SENDER, placeholders);
		commandMessenger.broadcast(PermissionUtils.forceOf("disband"), TranslationKey.MESSAGE_FORCE_DELETE_ADMIN, placeholders);
		if (!isSilent) {
			commandMessenger.message(faction, TranslationKey.MESSAGE_FORCE_DELETE_FACTION, placeholders);
			commandMessenger.broadcast(PredicatePermission.of(s -> s instanceof Player player
					&& !faction.getMembers().contains(player.getUniqueId())
			), TranslationKey.MESSAGE_FORCE_DELETE_PUBLIC, placeholders);
		}
		commandMessenger.getMain().getFactionManager().delete(faction, false);
	}
}