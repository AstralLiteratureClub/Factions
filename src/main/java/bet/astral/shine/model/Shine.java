package bet.astral.shine.model;

import lombok.Getter;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;

import java.util.UUID;

@Getter
public class Shine {
	private final UUID entityId;
	private final ShineColor color;
	private final String team;
	private final ClientboundSetPlayerTeamPacket createPacket;
	private final ClientboundSetPlayerTeamPacket deletePacket;

	public Shine(UUID entityId, ShineColor color, String team, ClientboundSetPlayerTeamPacket createPacket, ClientboundSetPlayerTeamPacket deletePacket) {
		this.entityId = entityId;
		this.color = color;
		this.team = team;
		this.createPacket = createPacket;
		this.deletePacket = deletePacket;
	}
}
