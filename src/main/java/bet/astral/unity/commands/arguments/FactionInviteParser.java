package bet.astral.unity.commands.arguments;

import bet.astral.unity.Factions;
import bet.astral.unity.model.Faction;
import bet.astral.unity.nms.TooltipSuggestion;
import bet.astral.unity.messenger.TranslationKeys;
import bet.astral.unity.utils.refrence.OfflinePlayerReference;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.bukkit.BukkitCommandContextKeys;
import org.incendo.cloud.bukkit.parser.PlayerParser;
import org.incendo.cloud.caption.CaptionVariable;
import org.incendo.cloud.component.CommandComponent;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.exception.parsing.ParserException;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


public class FactionInviteParser<C> implements ArgumentParser<C, Faction>, SuggestionProvider<C> {
	private static Component toString(List<OfflinePlayerReference> members) {
		Component component = null;
		for (OfflinePlayerReference reference : members){
			OfflinePlayer player = reference.offlinePlayer();
			if (component != null){
				component =component.append(Component.text(", ", NamedTextColor.GRAY));
			} else{
				component = Component.empty();
			}
			component = component.append(Component.text(player.getName(), NamedTextColor.GREEN));
		}

		return component;
	}

	private static final Factions factions = Factions.getPlugin(Factions.class);
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("E, dd MMM yyyy");
	private FactionInviteParser() {
	}

	public static <C> @NonNull ParserDescriptor<C, Faction> inviteParser() {
		return ParserDescriptor.of(new FactionInviteParser<>(), Faction.class);
	}

	public static <C> CommandComponent.@NonNull Builder<C, Faction> inviteComponent() {
		return CommandComponent.<C, Faction>builder().parser(inviteParser());
	}

	public @NonNull ArgumentParseResult<Faction> parse(final @NonNull CommandContext<C> commandContext, final @NonNull CommandInput commandInput) {
		String input = commandInput.readString();

		Faction faction = factions.getFactionManager().get(input);
		if (faction == null)  {
			return ArgumentParseResult.failure(new FactionInviteParserException(input, commandContext));
		}

		return ArgumentParseResult.success(faction);
	}

	/*
	 * Using the Completable future one might be better as it won't block the main thread hopefully,
	 */
	@Override
	public @NonNull CompletableFuture<? extends @NonNull Iterable<? extends @NonNull Suggestion>> suggestionsFuture(@NonNull CommandContext<C> context, @NonNull CommandInput input) {
		return CompletableFuture.supplyAsync(()->{
					Player sender = (Player) context.get(BukkitCommandContextKeys.BUKKIT_COMMAND_SENDER);

					return factions.getFactionManager().created().stream()
							.filter(faction->faction.isInvited(sender))
							.map(faction -> new TooltipSuggestion(faction, FactionParser.Mode.NAME,
									Component.text(faction.getName(), NamedTextColor.WHITE)
											.append(Component.text(" | ", NamedTextColor.DARK_GRAY))
											.append(Component.text("Owner: ", NamedTextColor.WHITE))
							))
							.collect(Collectors.toList());
				}
		);
	}

	public static final class FactionInviteParserException extends ParserException {
		private final String input;

		public FactionInviteParserException(final @NonNull String input, final @NonNull CommandContext<?> context) {
			super(PlayerParser.class, context, TranslationKeys.CAPTION_PLAYER_HAS_NOT_BEEN_INVITED, CaptionVariable.of("input", input));
			this.input = input;
		}

		public @NonNull String input() {
			return this.input;
		}
	}
}
