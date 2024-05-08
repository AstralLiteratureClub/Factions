package bet.astral.shine.model;

import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.ChatFormatting;

@Getter
public enum ShineColor {
	BLACK(NamedTextColor.BLACK, ChatFormatting.BLACK),
	DARK_AQUA(NamedTextColor.DARK_AQUA, ChatFormatting.DARK_AQUA),
	DARK_GREEN(NamedTextColor.DARK_GREEN, ChatFormatting.DARK_GREEN),
	DARK_RED(NamedTextColor.DARK_RED, ChatFormatting.DARK_RED),
	GOLD(NamedTextColor.GOLD, ChatFormatting.GOLD),
	GRAY(NamedTextColor.GRAY, ChatFormatting.GRAY),
	DARK_GRAY(NamedTextColor.DARK_GRAY, ChatFormatting.DARK_GRAY),
	BLUE(NamedTextColor.BLUE, ChatFormatting.BLUE),
	GREEN(NamedTextColor.GREEN, ChatFormatting.GREEN),
	AQUA(NamedTextColor.AQUA, ChatFormatting.AQUA),
	DARK_PURPLE(NamedTextColor.DARK_PURPLE, ChatFormatting.DARK_PURPLE),
	PURPLE(NamedTextColor.LIGHT_PURPLE, ChatFormatting.LIGHT_PURPLE),
	YELLOW(NamedTextColor.YELLOW, ChatFormatting.YELLOW),
	WHITE(NamedTextColor.WHITE, ChatFormatting.WHITE),
	IGNORE(NamedTextColor.WHITE, ChatFormatting.WHITE),
	;
	private final NamedTextColor adventureColor;
	private final ChatFormatting nmsColor;

	ShineColor(NamedTextColor adventureColor, ChatFormatting nmsColor) {
		this.adventureColor = adventureColor;
		this.nmsColor = nmsColor;
	}
}
