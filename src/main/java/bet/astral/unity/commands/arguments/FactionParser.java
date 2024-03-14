package bet.astral.unity.commands.arguments;

import bet.astral.unity.Factions;
import bet.astral.unity.model.Faction;
import bet.astral.unity.nms.TooltipSuggestion;
import bet.astral.unity.utils.TranslationKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.bukkit.parser.PlayerParser;
import org.incendo.cloud.caption.CaptionVariable;
import org.incendo.cloud.component.CommandComponent;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.exception.parsing.ParserException;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class FactionParser<C> implements ArgumentParser<C, Faction>, SuggestionProvider {
	protected static final Factions factions = Factions.getPlugin(Factions.class);
	protected final Mode mode;
	protected FactionParser(Mode mode) {
		this.mode = mode;
	}

	public static <C> @NonNull ParserDescriptor<C, Faction> factionParser() {
		return ParserDescriptor.of(new FactionParser<>(Mode.NAME), Faction.class);
	}
	public static <C> @NonNull ParserDescriptor<C, Faction> factionParser(Mode mode) {
		return ParserDescriptor.of(new FactionParser<>(mode), Faction.class);
	}

	public static <C> CommandComponent.@NonNull Builder<C, Faction> factionComponent() {
		return CommandComponent.<C, Faction>builder().parser(factionParser());
	}
	public static <C> CommandComponent.@NonNull Builder<C, Faction> factionComponent(Mode mode) {
		return CommandComponent.<C, Faction>builder().parser(factionParser(mode));
	}

	public @NonNull ArgumentParseResult<Faction> parse(final @NonNull CommandContext<C> commandContext, final @NonNull CommandInput commandInput) {
		String input = commandInput.readString();
		Faction faction;
		try {
			UUID id = UUID.fromString(input);
			faction = factions.getFactionManager().get(id);
		} catch (IllegalArgumentException ignore){
			faction = factions.getFactionManager().get(input);
			if (faction == null){
				faction = factions.getFactionManager().get(Component.text(input));
			}
		}

		if (faction == null){
			return ArgumentParseResult.failure(new FactionParserException(input, commandContext));
		}
		return ArgumentParseResult.success(faction);
	}

	@Override
	public @NonNull CompletableFuture<? extends @NonNull Iterable<? extends @NonNull Suggestion>> suggestionsFuture(@NonNull CommandContext context, @NonNull CommandInput input) {
		return CompletableFuture.supplyAsync(()->factions.getFactionManager().created().stream()
				.map(
						faction -> new TooltipSuggestion(
								faction,
								mode,
								Component.text(faction.getName(), NamedTextColor.WHITE)
										.append(Component.text(" | ", NamedTextColor.DARK_GRAY))
										.append(Component.text("Owner: ", NamedTextColor.WHITE))
						)
				).collect(Collectors.toList()));
	}

	public static final class FactionParserException extends ParserException {
		private final String input;

		public FactionParserException(final @NonNull String input, final @NonNull CommandContext<?> context) {
			super(PlayerParser.class, context, TranslationKey.CAPTION_UNKNOWN_FACTION, CaptionVariable.of("input", input));
			this.input = input;
		}

		public @NonNull String input() {
			return this.input;
		}
	}

	public enum Mode {
		UNIQUE_ID,
		NAME,
		DISPLAYNAME
	}
}
