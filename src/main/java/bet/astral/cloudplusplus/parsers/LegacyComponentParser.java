package bet.astral.cloudplusplus.parsers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.incendo.cloud.component.CommandComponent;
import org.incendo.cloud.minecraft.extras.parser.ComponentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.parser.standard.StringParser;

public class LegacyComponentParser {
	public static <C> ParserDescriptor<C, Component> legacyParser(StringParser.StringMode stringMode){
		return ComponentParser.componentParser(LegacyComponentSerializer.legacyAmpersand(), stringMode);
	}
	public static <C> CommandComponent.Builder<C, Component> legacyComponent(StringParser.StringMode stringMode){
		return ComponentParser.componentComponent((val)->LegacyComponentSerializer.legacySection().deserialize(val), stringMode);
	}
}
