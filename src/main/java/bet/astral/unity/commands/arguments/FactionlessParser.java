package bet.astral.unity.commands.arguments;

import bet.astral.unity.utils.TranslationKey;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.bukkit.parser.PlayerParser;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.exception.parsing.ParserException;

public class FactionlessParser {















	public static final class SelfFactionlessParserException extends ParserException {
		private final String input;

		public SelfFactionlessParserException(final @NonNull String input, final @NonNull CommandContext<?> context) {
			super(PlayerParser.class, context, TranslationKey.CAPTION_SELF_HAS_NO_FACTION);
			this.input = input;
		}

		public @NonNull String input() {
			return this.input;
		}
	}
	public static final class FactionlessParserException extends ParserException {
		private final String input;

		public FactionlessParserException(final @NonNull String input, final @NonNull CommandContext<?> context) {
			super(PlayerParser.class, context, TranslationKey.CAPTION_HAS_A_FACTION);
			this.input = input;
		}

		public @NonNull String input() {
			return this.input;
		}
	}

	public static final class BannedFactionlessParserException extends ParserException {
		private final String input;

		public BannedFactionlessParserException(final @NonNull String input, final @NonNull CommandContext<?> context) {
			super(PlayerParser.class, context, TranslationKey.CAPTION_BANNED);
			this.input = input;
		}

		public @NonNull String input() {
			return this.input;
		}
	}
}
