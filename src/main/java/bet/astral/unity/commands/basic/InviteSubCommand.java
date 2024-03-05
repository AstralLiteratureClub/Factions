package bet.astral.unity.commands.basic;

import bet.astral.unity.Factions;
import bet.astral.unity.commands.FactionCloudCommand;
import bet.astral.unity.model.FPermission;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.utils.TranslationKey;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.bukkit.parser.PlayerParser;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;

import java.util.concurrent.CompletableFuture;

public class InviteSubCommand extends FactionCloudCommand {
	public InviteSubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		commandPlayer(
				commandBuilderPlayer(
						"invite",
						loadDescription(TranslationKey.DESCRIPTION_INVITE, "/factions invite"),
						"add")
						.permission(PermissionUtils.of("invite", FPermission.INVITE))
						.required(
								PlayerParser.playerComponent()
										.name("who to invite")
										.description(loadDescription(TranslationKey.DESCRIPTION_INVITE_WHO, "/factions invite <who>"))
										.suggestionProvider(
												new SuggestionProvider<>() {
													@Override
													public @NonNull CompletableFuture<? extends @NonNull Iterable<? extends @NonNull Suggestion>> suggestionsFuture(@NonNull CommandContext<Object> context, @NonNull CommandInput input) {
														return CompletableFuture.supplyAsync(
																Bukkit.getOnlinePlayers()
																		.stream().filter(player->player.canSee((Player) context.sender()))
																		.map(player->plugin.getPlayerManager().convert(player))
																		.map(player->player.getFactionId()==null)
																		.map()

														);
													}
												}
										)
						)
		);
	}
}
