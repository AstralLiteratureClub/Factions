package bet.astral.unity.nms;

import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.refrence.OfflinePlayerReference;
import com.mojang.brigadier.Message;
import io.papermc.paper.adventure.AdventureComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class TooltipSuggestion implements org.incendo.cloud.brigadier.suggestion.TooltipSuggestion {
	private final String suggestion;
	private final net.minecraft.network.chat.Component tooltip;
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

	public TooltipSuggestion(Faction faction, boolean id, Component tooltip){
		if (tooltip != null) {
			this.tooltip = new AdventureComponent(tooltip);
		} else  {
			this.tooltip = null;
		}
		this.suggestion = id ? faction.getUniqueId().toString() : faction.getName();
	}

	@Override
	public @Nullable Message tooltip() {
		return tooltip;
	}

	@Override
	public @NonNull String suggestion() {
		return suggestion;
	}
}
