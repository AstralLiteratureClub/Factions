package bet.astral.unity.model;

import net.kyori.adventure.util.TriState;

public class FPermission {
	public static final FPermission INVITE = new FPermission("invite", TriState.FALSE);

	private final String name;
	private final TriState alwaysGiven;

	public FPermission(String name, TriState alwaysGiven) {
		this.name = name;
		this.alwaysGiven = alwaysGiven;
	}
}
