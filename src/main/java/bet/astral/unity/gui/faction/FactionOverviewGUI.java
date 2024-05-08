package bet.astral.unity.gui.faction;

import bet.astral.guiman.GUI;
import bet.astral.unity.Factions;
import bet.astral.unity.gui.GUIExtend;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import org.bukkit.entity.Player;

public class FactionOverviewGUI extends GUIExtend {

	public FactionOverviewGUI(Factions factions) {
		super(factions);
	}

	@Override
	public boolean shareGUIFactionAndFactionless() {
		return false;
	}

	@Override
	protected void generateGUI(Player player, FPlayer fPlayer) {
		GUI gui = null;
		GeneratedFor generatedFor = gui != null ? gui.get

		if (fPlayer.getFaction() == null){

		} else {

		}


		setGUI(player, gui);
	}

}
