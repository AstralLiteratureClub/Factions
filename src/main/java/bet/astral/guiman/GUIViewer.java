package bet.astral.guiman;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public interface GUIViewer {
	@Nullable
	Player getPlayer();
	default void openGUI(GUI gui) {
		if (getPlayer() == null){
			return;
		}
		gui.generateInventory(getPlayer());
	}
	default void openGUI(GUIReference guiReference){
		if (getPlayer() == null){
			return;
		}
		guiReference.generateInventory(getPlayer());
	}
}
