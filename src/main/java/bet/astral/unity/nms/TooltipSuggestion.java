package bet.astral.unity.nms;

import bet.astral.unity.commands.arguments.FactionParser;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.refrence.OfflinePlayerReference;
import com.mojang.brigadier.Message;
import io.papermc.paper.adventure.AdventureComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class TooltipSuggestion implements org.incendo.cloud.brigadier.suggestion.TooltipSuggestion {
	private final String suggestion;
	private final Message tooltip;
	private TooltipSuggestion(String suggestion, Message message){
		this.suggestion = suggestion;
		this.tooltip = message;
	}
	public TooltipSuggestion(String suggestion, Component tooltip){
		if (tooltip != null) {
			this.tooltip = new AdventureComponent(tooltip);
		} else  {
			this.tooltip = null;
		}
		this.suggestion = suggestion;
	}

	public TooltipSuggestion(OfflinePlayer suggestion, Component tooltip){
		if (tooltip != null) {
			this.tooltip = new AdventureComponent(tooltip);
		} else  {
			this.tooltip = null;
		}
		this.suggestion = suggestion.getName();
	}

	public TooltipSuggestion(OfflinePlayerReference suggestion, Component tooltip){
		if (tooltip != null) {
			this.tooltip = new AdventureComponent(tooltip);
		} else  {
			this.tooltip = null;
		}
		this.suggestion = suggestion.offlinePlayer().getName();
	}

	public TooltipSuggestion(Faction faction, FactionParser.Mode mode, Component tooltip) {
		if (tooltip != null) {
			this.tooltip = new AdventureComponent(tooltip);
		} else {
			this.tooltip = null;
		}
		this.suggestion = mode == FactionParser.Mode.UNIQUE_ID
				? faction.getUniqueId().toString() :
				mode == FactionParser.Mode.NAME
						? faction.getName() :
						PlainTextComponentSerializer.plainText().serialize(faction.getDisplayname());
	}

	@Override
	public @Nullable Message tooltip() {
		return tooltip;
	}

	@Override
	public org.incendo.cloud.brigadier.suggestion.@NonNull TooltipSuggestion withSuggestion(@NonNull String suggestion) {
		return new TooltipSuggestion(suggestion, this.tooltip);
	}


	@Override
	public @NonNull String suggestion() {
		return suggestion;
	}
}
