package bet.astral.unity.commands.arguments;

import bet.astral.unity.Factions;
import bet.astral.unity.model.FRole;
import bet.astral.unity.messenger.TranslationKeys;
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

import java.util.Arrays;
import java.util.stream.Collectors;

public class RoleParser<C> implements ArgumentParser<C, FRole>, BlockingSuggestionProvider<C> {
	private static final Factions factions = Factions.getPlugin(Factions.class);
	protected RoleParser(){
	}

	public static <C> @NonNull ParserDescriptor<C, FRole> roleParser() {
		return ParserDescriptor.of(new RoleParser<>(), FRole.class);
	}

	public static <C> CommandComponent.@NonNull Builder<C, FRole> roleComponent() {
		return CommandComponent.<C, FRole>builder().parser(roleParser());
	}




	public @NonNull ArgumentParseResult<FRole> parse(final @NonNull CommandContext<C> commandContext, final @NonNull CommandInput commandInput) {
		String input = commandInput.readString();
		for (FRole role : FRole.values()) {
			if (role.getName().equalsIgnoreCase(input)) {
				return ArgumentParseResult.success(role);
			}
		}
		return ArgumentParseResult.failure(new RoleParserException(input, commandContext));
	}

	public @NonNull Iterable<@NonNull Suggestion> suggestions(final @NonNull CommandContext<C> commandContext, final @NonNull CommandInput input) {
		return Arrays.stream(FRole.values()).map(v->Suggestion.suggestion(v.getName())).collect(Collectors.toList());
	}

	public static final class RoleParserException extends ParserException {
		private final String input;

		public RoleParserException(final @NonNull String input, final @NonNull CommandContext<?> context) {
			super(PlayerParser.class, context, TranslationKeys.CAPTION_UNKNOWN_ROLE, CaptionVariable.of("input", input));
			this.input = input;
		}

		public @NonNull String input() {
			return this.input;
		}
	}

}