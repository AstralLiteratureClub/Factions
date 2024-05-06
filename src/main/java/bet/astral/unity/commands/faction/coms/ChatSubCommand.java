package bet.astral.unity.commands.faction.coms;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.core.FactionSubCommand;
import bet.astral.unity.model.FCommunicationChannel;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.messenger.TranslationKeys;
import bet.astral.unity.utils.refrence.PlayerFactionReferenceImpl;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;

@Cloud
public class ChatSubCommand extends FactionSubCommand {
	public ChatSubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		commandManager.command(
				root.literal(
								"chat",
								"factionchat",
								"fchat",
								"fc",
								"clanchat",
								"cchat",
								"cc",
								"guildchat",
								"gchat",
								"gc"
						)
						.commandDescription(
								loadDescription(TranslationKeys.DESCRIPTION_CHAT, "/factions chat"))
						.permission(PermissionUtils.of("chat", true))
						.optional(StringParser.stringComponent(StringParser.StringMode.GREEDY)
								.name("message")
								.description(loadDescription(TranslationKeys.DESCRIPTION_CHAT_MESSAGE, "/factions chat [message]"))
						)
						.senderType(Player.class)
						.handler(context -> {
							Player sender = context.sender();
							FPlayer player = plugin.getPlayerManager().convert(sender);
							Faction faction = plugin.getFactionManager().get(player.getFactionId());
							FCommunicationChannel.Toggleable chat = FCommunicationChannel.Toggleable.FACTION;

							if (context.optional("message").isEmpty()) {
								switch (player.getChatType()) {
									case ALLY, GLOBAL, CUSTOM -> {
										player.setChatType(FCommunicationChannel.FACTION);
										player.message(TranslationKeys.MESSAGE_CHAT_SWITCH_FACTION);
									}
									default -> {
										player.setChatType(FCommunicationChannel.GLOBAL);
										player.message(TranslationKeys.MESSAGE_CHAT_SWITCH_GLOBAL);
									}
								}
								return;
							}

							String message = context.get("message");

							for (Audience audience : faction.audiences()) {
								Component component = plugin.getChatHandler().handle(player, faction, new PlayerFactionReferenceImpl(sender, faction), Component.text(message), chat);
								if (component == null) {
									return;
								}
								audience.sendMessage(component);
							}
						})
		);
		commandManager.command(
				commandBuilder(
						"factionchat",
						"fchat",
						"fc",
						"clanchat",
						"cchat",
						"cc",
						"guildchat",
						"gchat",
						"gc"
				)
						.commandDescription(
								loadDescription(TranslationKeys.DESCRIPTION_CHAT, "/factions chat"))
						.permission(PermissionUtils.of("chat", true))
						.optional(StringParser.stringComponent(StringParser.StringMode.GREEDY)
								.name("message")
								.description(loadDescription(TranslationKeys.DESCRIPTION_CHAT_MESSAGE, "/factions chat [message]"))
						)
						.senderType(Player.class)
						.handler(context -> {
							Player sender = context.sender();
							FPlayer player = plugin.getPlayerManager().convert(sender);
							Faction faction = plugin.getFactionManager().get(player.getFactionId());
							FCommunicationChannel.Toggleable chat = FCommunicationChannel.Toggleable.FACTION;

							if (context.optional("message").isEmpty()) {
								switch (player.getChatType()) {
									case ALLY, GLOBAL, CUSTOM -> {
										player.setChatType(FCommunicationChannel.FACTION);
										player.message(TranslationKeys.MESSAGE_CHAT_SWITCH_FACTION);
									}
									default -> {
										player.setChatType(FCommunicationChannel.GLOBAL);
										player.message(TranslationKeys.MESSAGE_CHAT_SWITCH_GLOBAL);
									}
								}
								return;
							}

							String message = context.get("message");

							for (Audience audience : faction.audiences()) {
								Component component = plugin.getChatHandler().handle(player, faction, new PlayerFactionReferenceImpl(sender, faction), Component.text(message), chat);
								if (component == null) {
									return;
								}
								audience.sendMessage(component);
							}
						})
		);
	}
}