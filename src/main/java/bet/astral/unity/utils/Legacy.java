package bet.astral.unity.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class Legacy {
	public static String string(Component component){
		return LegacyComponentSerializer.legacyAmpersand().serialize(component);
	}
	public static Component component(String legacy){
		return LegacyComponentSerializer.legacyAmpersand().deserialize(legacy);
	}
}
