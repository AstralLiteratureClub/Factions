package bet.astral.unity.messenger;

import bet.astral.messenger.adventure.AdventurePlaceholderManager;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.messenger.utils.PlaceholderUtils;
import bet.astral.shine.model.ShineColor;
import bet.astral.unity.model.FRole;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.refrence.FactionReference;
import net.kyori.adventure.text.Component;

public class FactionPlaceholderManager extends AdventurePlaceholderManager {
	public PlaceholderList factionPlaceholders(String prefix, FactionReference factionReference) {
		Faction faction = factionReference.getFaction();
		if (faction == null) {
			return new PlaceholderList();
		}
		PlaceholderList placeholders = new PlaceholderList();
		if (prefix != null) {
			placeholders.add(PlaceholderUtils.createPlaceholder(null, prefix, faction.getName()));
		}
		placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "name", faction.getName()));
		placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "displayname", faction.getDisplayname()));
		placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "customname", faction.getDisplayname()));
		placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "description", faction.getDescription()));
		placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "how_to_join", faction.getJoinInfo()));
		placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "leader", faction.getMembersWithRole(FRole.OWNER).get(0)));
		/*
		if (faction.getHome() != null) {
			placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "base_name", faction.getHome().getName()));
			placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "base_id", faction.getHome().getUniqueId()));
			placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "base_x", faction.getHome().getX()));
			placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "base_y", faction.getHome().getY()));
			placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "base_z", faction.getHome().getZ()));
			placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "base_pitch", faction.getHome().getPitch()));
			placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "base_home", faction.getHome().getWorld()));
			placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "base_home", faction.getHome().getWorld()));
		}
		 */

		return placeholders;
	}

	public PlaceholderList shineColor(String prefix, ShineColor shineColor) {
		if (shineColor == null) {
			return new PlaceholderList();
		}
		PlaceholderList placeholders = new PlaceholderList();
		if (prefix != null) {
			placeholders.add(PlaceholderUtils.createPlaceholder(null, prefix, properCase(shineColor)));
		}
		placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "formatted", Component.text(properCase(shineColor), shineColor.getAdventureColor())));
		return placeholders;
	}

	public <T extends Enum<T>> String properCase(T enumm) {
		// Convert constant name to proper case with spaces
		String[] words = enumm.name().split("_");
		StringBuilder properCaseName = new StringBuilder();
		for (String word : words) {
			properCaseName.append(word.substring(0, 1).toUpperCase())
					.append(word.substring(1).toLowerCase())
					.append(" ");
		}
		// Remove the trailing space
		properCaseName.setLength(properCaseName.length() - 1);

		return properCaseName.toString();
	}
}