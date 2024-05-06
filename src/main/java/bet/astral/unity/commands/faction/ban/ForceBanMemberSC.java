package bet.astral.unity.commands.faction.ban;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.unity.Factions;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.paper.PaperCommandManager;

@Cloud
public class ForceBanMemberSC extends AbstractBanSC{
	public ForceBanMemberSC(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
	}
}
