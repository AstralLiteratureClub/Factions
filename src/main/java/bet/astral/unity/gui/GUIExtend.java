package bet.astral.unity.gui;

import bet.astral.guiman.GUI;
import bet.astral.guiman.GUIReference;
import bet.astral.unity.Factions;
import bet.astral.unity.model.FPlayer;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public abstract class GUIExtend implements GUIReference {
	private final Factions factions;
	private final Map<UUID, GUI> guis = new HashMap<>();

	public GUIExtend(Factions factions) {
		this.factions = factions;
	}

	public void setGUI(@NotNull Player player, @Nullable GUI gui){
		this.guis.put(player.getUniqueId(), gui);
		if (gui == null){
			this.guis.remove(player.getUniqueId());
		}
	}

	public abstract boolean shareGUIFactionAndFactionless();
	public boolean generateNewGUI(Player player){
		return !shareGUIFactionAndFactionless() || guis.get(player.getUniqueId()) == null;
	}

	@Override
	public @NotNull GUI getGUI(@NotNull Player player) {
		return guis.get(player.getUniqueId());
	}

	protected abstract void generateGUI(Player player, FPlayer fPlayer);

	@Override
	public void generateInventory(@NotNull Player player) {
		FPlayer fPlayer = factions.getPlayerManager().convert(player);
		generateGUI(player, fPlayer);
		GUIReference.super.generateInventory(player);
	}

	public enum GeneratedFor {
		FACTION,
		FACTIONLESS
	}
}
