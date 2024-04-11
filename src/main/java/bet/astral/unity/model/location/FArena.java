package bet.astral.unity.model.location;

import bet.astral.messenger.message.part.IMessagePart;
import bet.astral.unity.utils.location.Position;
import net.kyori.adventure.text.Component;

import java.util.List;

public class FArena extends FZone {
	private final Position cornerOne;
	private final Position cornerTwo;
	private final int maxPlayersPerTeam;
	private final List<Position> spawnsOne;
	private final List<Position> spawnsTwo;

	public FArena(String world, int x, int y, IMessagePart<Component> messagePart, Position cornerOne, Position cornerTwo, int maxPlayersPerTeam, List<Position> spawnsOne, List<Position> spawnsTwo) {
		super(world, x, y, "arena", Type.ARENA, messagePart);
		this.cornerOne = cornerOne;
		this.cornerTwo = cornerTwo;
		this.maxPlayersPerTeam = maxPlayersPerTeam;
		this.spawnsOne = spawnsOne;
		this.spawnsTwo = spawnsTwo;
	}
}
