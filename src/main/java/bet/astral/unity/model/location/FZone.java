package bet.astral.unity.model.location;

import bet.astral.messenger.message.part.IMessagePart;
import bet.astral.unity.utils.ChunkPosition;
import lombok.Getter;
import net.kyori.adventure.text.Component;

@Getter
public class FZone extends ChunkPosition {
	private final String name;
	private final Type type;
	private Component description;

	public FZone(String world, int x, int y, String name, Type type, Component component) {
		super(world, x, y);
		this.name = name;
		this.type = type;
	}
	public FZone(String world, int x, int y, String name, Type type, IMessagePart<Component> messagePart){
		super(world, x, y);
		this.name = name;
		this.type = type;
		this.description = messagePart.asComponent();
	}


	@Getter
	public enum Type {
		SPAWN("Spawn"),
		SAFE_ZONE("Safe Zone"),
		WILDERNESS("Wilderness"),
		ARENA("Arena"),
		CLAIMED("%name%")
		;
		private final String name;
		private final String id;

		Type(String name) {
			this.name = name;
			this.id = name.replace(" ", "_").toLowerCase();
		}
	}
}
