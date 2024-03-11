package bet.astral.unity.commands.arguments;

import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.TranslationKey;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.bukkit.parser.PlayerParser;
import org.incendo.cloud.caption.CaptionVariable;
import org.incendo.cloud.component.CommandComponent;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.exception.parsing.ParserException;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.Suggestion;

public class PublicFactionParser<C> extends FactionParser<C>{
	protected PublicFactionParser(Mode mode) {
		super(mode);
	}
	public static <C> @NonNull ParserDescriptor<C, Faction> factionParser() {
		return ParserDescriptor.of(new PublicFactionParser<>(Mode.NAME), Faction.class);
	}
	public static <C> @NonNull ParserDescriptor<C, Faction> factionParser(Mode mode) {
		return ParserDescriptor.of(new PublicFactionParser<>(mode), Faction.class);
	}

	public static <C> CommandComponent.@NonNull Builder<C, Faction> factionComponent() {
		return CommandComponent.<C, Faction>builder().parser(factionParser());
	}
	public static <C> CommandComponent.@NonNull Builder<C, Faction> factionComponent(Mode mode) {
		return CommandComponent.<C, Faction>builder().parser(factionParser(mode));
	}

	@Override
	public @NonNull ArgumentParseResult<Faction> parse(@NonNull CommandContext<C> commandContext, @NonNull CommandInput commandInput) {
		String input = commandInput.peekString();
		ArgumentParseResult<Faction> result = super.parse(commandContext, commandInput);
		try {
			result.getClass().getDeclaredField("failure");
			return result;
		} catch (NoSuchFieldException ignore) {
			try {
				Faction value = (Faction) result.getClass().getDeclaredField("value").get(result);
				if (!value.isPublic()){
					return ArgumentParseResult.failure(new PublicFactionParserException(input, commandContext));
				}
				return result;
			} catch (IllegalAccessException | NoSuchFieldException e) {
				return ArgumentParseResult.failure(new RuntimeException(e));
			}
		}
	}

	@Override
	public @NonNull Iterable<@NonNull Suggestion> suggestions(@NonNull CommandContext<C> commandContext, @NonNull CommandInput input) {
		return super.suggestions(commandContext, input);
	}


	public static final class PublicFactionParserException extends ParserException {
		private final String input;

		public PublicFactionParserException(final @NonNull String input, final @NonNull CommandContext<?> context) {
			super(PlayerParser.class, context, TranslationKey.CAPTION_ALREADY_INVITED, CaptionVariable.of("input", input));
			this.input = input;
		}

		public @NonNull String input() {
			return this.input;
		}
	}

}
