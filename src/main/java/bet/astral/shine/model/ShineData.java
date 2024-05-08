package bet.astral.shine.model;

import lombok.Getter;

import javax.xml.stream.Location;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class ShineData {
	private final UUID playerId;
	private final Map<UUID, Shine> entityShineMap = new HashMap<>();
	private final Map<Location, Shine> locationShineMap = new HashMap<>();

	public ShineData(UUID playerId) {
		this.playerId = playerId;
	}
}
