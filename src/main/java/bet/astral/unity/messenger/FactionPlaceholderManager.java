package bet.astral.unity.messenger;

import bet.astral.messenger.adventure.AdventurePlaceholderMessenger;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.messenger.utils.PlaceholderUtils;
import bet.astral.unity.model.FRole;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.refrence.FactionReference;

public class FactionPlaceholderManager extends AdventurePlaceholderMessenger {
	public PlaceholderList factionPlaceholders(String prefix, FactionReference factionReference){
		Faction faction = factionReference.getFaction();
		if (faction == null){
			return new PlaceholderList();
		}
		PlaceholderList placeholders = new PlaceholderList();
		if (prefix != null){
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
}
