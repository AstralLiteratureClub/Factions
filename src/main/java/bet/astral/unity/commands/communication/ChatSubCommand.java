package bet.astral.unity.commands.communication;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.FactionCloudCommand;
import bet.astral.unity.event.FactionEvent;
import bet.astral.unity.event.player.ASyncPlayerChangeChatEvent;
import bet.astral.unity.model.FChat;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.utils.TranslationKey;
import bet.astral.unity.utils.refrence.FactionReferenceImpl;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.bukkit.parser.PlayerParser;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.flag.CommandFlag;
import org.incendo.cloud.parser.standard.EnumParser;
import org.incendo.cloud.parser.standard.StringParser;

@Cloud
public class ChatSubCommand extends FactionCloudCommand {
	public ChatSubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);

		commandManager.command(
				root.literal(
						"chat",
						loadDescription(TranslationKey.DESCRIPTION_CHAT, "/factions chat")
				)
						.permission(PermissionUtils.of("chat", true))
						.optional(StringParser.stringComponent(StringParser.StringMode.GREEDY)
								.name("message")
								.description(loadDescription(TranslationKey.DESCRIPTION_CHAT_MESSAGE, "/factions chat [message]"))
								.suggestionProvider(
										PlayerParser.playerParser().parser().suggestionProvider()
								))
						.flag(
								CommandFlag.builder(
												"chat-type")
										.withDescription(loadDescription(TranslationKey.DESCRIPTION_CHAT_TYPE_FLAG, "/factions chat [message] --type"))
										.withComponent(
												EnumParser.enumComponent(
																FChat.Toggleable.class)
														.name("type")
														.description(loadDescription(TranslationKey.DESCRIPTION_CHAT_TYPE, "/factions chat [message] --type <type>"))
														.required()
										)
						)
						.senderType(Player.class)
						.handler(context -> {
							Player sender = context.sender();
							FPlayer player = plugin.getPlayerManager().convert(sender);
							Faction faction = plugin.getFactionManager().get(player.getFactionId());
							FChat.Toggleable chat = (FChat.Toggleable) context.flags().getValue("chat-type").orElse(FChat.Toggleable.FACTION);
							if (context.optional("message").isEmpty()) {
								FChat from = player.getChatType();
								final FChat to = chat.getChat();

								ASyncPlayerChangeChatEvent event = new ASyncPlayerChangeChatEvent(player, from, to, FactionEvent.Cause.PLAYER);
								if (event.isCancel()) {
									return;
								}
								FChat toEvent = event.getTo();

								event.getPlayer().setChatType(toEvent);
								if (toEvent != to) {
									return;
								}
								PlaceholderList placeholders = new PlaceholderList();
								placeholders.add("type", to.name());
								switch (to) {
									case FACTION -> commandMessenger.message(sender, TranslationKey.MESSAGE_CHAT_SWITCH_FACTION, placeholders);
									case ALLY -> commandMessenger.message(sender, TranslationKey.MESSAGE_CHAT_SWITCH_ALLY, placeholders);
									case GLOBAL -> commandMessenger.message(sender, TranslationKey.MESSAGE_CHAT_SWITCH_GLOBAL, placeholders);
								}
							} else {
								String message = (String) context.optional("message").get();

								for (Audience audience : faction.audiences()) {
									Component component = plugin.getChatHandler().handle(player, faction, new FactionReferenceImpl(sender, faction), Component.text(message), chat);
									if (component == null) {
										return;
									}
									audience.sendMessage(component);
								}
							}
						})
		);
		commandManager.command(
				commandBuilderPlayer(
								"chat",
								loadDescription(TranslationKey.DESCRIPTION_CHAT, "/factions chat")
						)
						.permission(PermissionUtils.of("chat", true))
						.optional(StringParser.stringComponent(StringParser.StringMode.GREEDY)
								.name("message")
								.description(loadDescription(TranslationKey.DESCRIPTION_CHAT_MESSAGE, "/factions chat [message]"))
								.suggestionProvider(
										PlayerParser.playerParser().parser().suggestionProvider()
								))
						.flag(
								CommandFlag.builder(
												"chat-type")
										.withDescription(loadDescription(TranslationKey.DESCRIPTION_CHAT_TYPE_FLAG, "/factions chat [message] --type"))
										.withComponent(
												EnumParser.enumComponent(
																FChat.Toggleable.class)
														.name("type")
														.description(loadDescription(TranslationKey.DESCRIPTION_CHAT_TYPE, "/factions chat [message] --type <type>"))
														.required()
										)
						)
						.senderType(Player.class)
						.handler(context -> {
							Player sender = context.sender();
							FPlayer player = plugin.getPlayerManager().convert(sender);
							Faction faction = plugin.getFactionManager().get(player.getFactionId());
							FChat.Toggleable chat = (FChat.Toggleable) context.flags().getValue("chat-type").orElse(FChat.Toggleable.FACTION);
							if (context.optional("message").isEmpty()) {
								FChat from = player.getChatType();
								final FChat to = chat.getChat();

								ASyncPlayerChangeChatEvent event = new ASyncPlayerChangeChatEvent(player, from, to, FactionEvent.Cause.PLAYER);
								if (event.isCancel()) {
									return;
								}
								FChat toEvent = event.getTo();

								event.getPlayer().setChatType(toEvent);
								if (toEvent != to) {
									return;
								}
								PlaceholderList placeholders = new PlaceholderList();
								placeholders.add("type", to.name());
								switch (to) {
									case FACTION -> commandMessenger.message(sender, TranslationKey.MESSAGE_CHAT_SWITCH_FACTION, placeholders);
									case ALLY -> commandMessenger.message(sender, TranslationKey.MESSAGE_CHAT_SWITCH_ALLY, placeholders);
									case GLOBAL -> commandMessenger.message(sender, TranslationKey.MESSAGE_CHAT_SWITCH_GLOBAL, placeholders);
								}
							} else {
								String message = (String) context.optional("message").get();

								for (Audience audience : faction.audiences()) {
									Component component = plugin.getChatHandler().handle(player, faction, new FactionReferenceImpl(sender, faction), Component.text(message), chat);
									if (component == null) {
										return;
									}
									audience.sendMessage(component);
								}
							}
						})
		);

	}
}