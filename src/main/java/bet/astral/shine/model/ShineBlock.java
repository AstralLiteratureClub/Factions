package bet.astral.shine.model;

import lombok.Getter;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import org.bukkit.Location;

import java.util.UUID;

@Getter
public class ShineBlock extends Shine{
	private final Location location;
	private final int entityIdInt;

	public ShineBlock(UUID entityId, ShineColor color, String team, ClientboundSetPlayerTeamPacket createPacket, ClientboundSetPlayerTeamPacket deletePacket, Location location, int entityIdInt) {
		super(entityId, color, team, createPacket, deletePacket);
		this.location = location;
		this.entityIdInt = entityIdInt;
	}
}
