package bet.astral.unity.commands.arguments;

import bet.astral.cloudplusplus.parsers.LegacyComponentParser;
import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.unity.Factions;
import bet.astral.unity.managers.FactionManager;
import bet.astral.unity.messenger.TranslationKeys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.caption.CaptionVariable;
import org.incendo.cloud.component.CommandComponent;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.exception.parsing.ParserException;
import org.incendo.cloud.minecraft.extras.parser.ComponentParser;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.suggestion.SuggestionProvider;
import org.jetbrains.annotations.NotNull;

public class FactionNameParser<C, T> implements ArgumentParser<C, T> {
	private final static Factions factions = Factions.getPlugin(Factions.class);
	private final NameMode<C, T> mode;

	public static <C, T> @NotNull ParserDescriptor<C, T> factionNameParser(NameMode<C, T> mode) {
		return ParserDescriptor.of(new FactionNameParser<>(mode), mode.getComponentType());
	}

	@NotNull
	public static <C, T> CommandComponent.Builder<C, T> factionNameComponent(@NotNull NameMode<C, T> mode) {
		return CommandComponent.<C, T>builder().parser(factionNameParser(mode));
	}
	private FactionNameParser(NameMode<C, T> mode){
		this.mode = mode;
	}

	@Override
	public @NonNull ArgumentParseResult<@NonNull T> parse(@NonNull CommandContext<@NonNull C> commandContext, @NonNull CommandInput commandInput) {
		ArgumentParseResult<T> result = mode.getParser().parse(commandContext, commandInput);
		if (result.failure().isPresent()){
			return result;
		}
		T value = result.parsedValue().orElse(null);
		if (value == null){
			throw new NullPointerException("Value for result is null even tho it is not supposed to be null!");
		}
		String plain = mode.asPlain(value);
		String valueAsString = mode.asString(value);

		FactionManager factionManager = factions.getFactionManager();
		boolean isBanned = factionManager.isBanned(plain);
		boolean exists = factionManager.exists(plain);
		boolean existsDisplayname = factionManager.existsDisplayname(plain);

		if (isBanned){
			return ArgumentParseResult.failure(new FactionNameBannedException(mode, commandContext,
					new Placeholder("input", valueAsString), new Placeholder("plain_input", plain),
					new Placeholder("input_normal", value)));
		}

		if (exists || existsDisplayname){
			return ArgumentParseResult.failure(new FactionNameExistsException(mode, commandContext,
					new Placeholder("input", valueAsString), new Placeholder("plain_input", plain),
					new Placeholder("input_normal", value)));
		}

		return ArgumentParseResult.success(value);
	}

	public abstract static class NameMode<C, T> {
		public static final NameMode<?, Component> COMPONENT = new ComponentMode<>();
		public static final NameMode<?, String> STRING = new StringMode<>();

		abstract ParserDescriptor<C, T> getParserDescriptor();
		ArgumentParser<C, T> getParser() {
			return getParserDescriptor().parser();
		}
		abstract CommandComponent.Builder<C, T> getComponent();
		abstract SuggestionProvider<C> getSuggestionProvider();

		abstract T asValue(String value);
		abstract String asString(T value);
		abstract String asPlain(T value);
		abstract Class<?> getParserClass();

		abstract Class<T> getComponentType();
	}
	private static class StringMode<C> extends NameMode<C, String> {
		private final ParserDescriptor<C, String> parserDescriptor = StringParser.stringParser(StringParser.StringMode.SINGLE);
		private final CommandComponent.Builder<C, String> component = StringParser.stringComponent(StringParser.StringMode.SINGLE);
		@Override
		public ParserDescriptor<C, String> getParserDescriptor() {
			return parserDescriptor;
		}

		@Override
		public CommandComponent.Builder<C, String> getComponent() {
			return component;
		}

		@Override
		public SuggestionProvider<C> getSuggestionProvider() {
			return parserDescriptor.parser().suggestionProvider();
		}

		@Override
		public String asValue(String value) {
			return value;
		}

		@Override
		public String asString(String value) {
			return value;
		}

		@Override
		public String asPlain(String value) {
			return value;
		}

		@Override
		Class<?> getParserClass() {
			return StringParser.class;
		}

		@Override
		Class<String> getComponentType() {
			return String.class;
		}
	}
	private static class ComponentMode<C> extends NameMode<C, Component> {
		private final ParserDescriptor<C, Component> parserDescriptor = LegacyComponentParser.legacyParser(StringParser.StringMode.SINGLE);
		private final CommandComponent.Builder<C, Component> component = LegacyComponentParser.legacyComponent(StringParser.StringMode.SINGLE);
		@Override
		public Component asValue(String value) {
			return LegacyComponentSerializer.legacyAmpersand().deserialize(value);
		}

		@Override
		public String asString(Component value) {
			return LegacyComponentSerializer.legacyAmpersand().serialize(value);
		}

		@Override
		public String asPlain(Component value) {
			return PlainTextComponentSerializer.plainText().serialize(value);
		}

		@Override
		Class<?> getParserClass() {
			return ComponentParser.class;
		}

		@Override
		Class<Component> getComponentType() {
			return Component.class;
		}

		@Override
		public ParserDescriptor<C, Component> getParserDescriptor() {
			return parserDescriptor;
		}

		@Override
		public CommandComponent.Builder<C, Component> getComponent() {
			return component;
		}

		@Override
		public SuggestionProvider<C> getSuggestionProvider() {
			return parserDescriptor.parser().suggestionProvider();
		}
	}

	private static class FactionNameBannedException extends ParserException {
		protected <C> FactionNameBannedException(NameMode<C, ?> componentMode, @NonNull CommandContext<?> context, @NonNull CaptionVariable... captionVariables) {
			super(componentMode.getParserClass(), context, TranslationKeys.CAPTION_FACTION_NAME_BANNED, captionVariables);
		}
	}

	private static class FactionNameExistsException extends ParserException {
		protected <C> FactionNameExistsException(NameMode<C, ?> componentMode, @NonNull CommandContext<?> context, @NonNull CaptionVariable... captionVariables) {
			super(componentMode.getParserClass(), context, TranslationKeys.CAPTION_FACTION_NAME_EXISTS, captionVariables);
		}
	}
}
