package bet.astral.unity.model;

import lombok.Getter;
import net.kyori.adventure.util.TriState;

public class FPermission {
	public static final FPermission INVITE = new FPermission("invite", TriState.FALSE);
	public static final FPermission CANCEL_INVITE = new FPermission("cancel_invite", TriState.FALSE);
	public static final FPermission INVITES = new FPermission("invites", TriState.FALSE);
	public static final FPermission DELETE = new FPermission("delete", TriState.FALSE);
	public static final FPermission INFO = new FPermission("info", TriState.TRUE);

	@Getter
	private final String name;
	@Getter
	private final TriState alwaysGiven;

	public FPermission(String name, TriState alwaysGiven) {
		this.name = name;
		this.alwaysGiven = alwaysGiven;
	}
}
