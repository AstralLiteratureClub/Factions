package bet.astral.guiman;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface GUIReference {
	@NotNull
	GUI getGUI(@NotNull Player player);
	default void generateInventory(@NotNull Player player) {
		GUI gui = getGUI(player);
		gui.generateInventory(player);
	}
}
