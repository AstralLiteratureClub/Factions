package bet.astral.unity.model;

import lombok.Getter;
import net.kyori.adventure.util.TriState;

public class FPermission {
	public static final FPermission RENAME_DISPLAYNAME = new FPermission("rename.displayname", TriState.FALSE);
	public static final FPermission RENAME_NAME = new FPermission("rename.name", TriState.FALSE);
	public static final FPermission INVITE = new FPermission("invite", TriState.FALSE);
	public static final FPermission CANCEL_INVITE = new FPermission("cancel_invite", TriState.FALSE);
	public static final FPermission INVITES = new FPermission("invites", TriState.FALSE);
	public static final FPermission DELETE = new FPermission("delete", TriState.FALSE);
	public static final FPermission KICK = new FPermission("kick", TriState.FALSE);
	public static final FPermission EDIT_ROLE_PREFIX_PUBLIC = new FPermission("meta.prefix.public.role", TriState.FALSE);
	public static final FPermission EDIT_ROLE_PREFIX_PRIVATE = new FPermission("meta.prefix.private.role,", TriState.FALSE);
	public static final FPermission EDIT_MEMBER_PREFIX_PRIVATE = new FPermission("meta.prefix.private.member", TriState.FALSE);

	@Getter
	private final String name;
	@Getter
	private final TriState alwaysGiven;

	public FPermission(String name, TriState alwaysGiven) {
		this.name = name;
		this.alwaysGiven = alwaysGiven;
	}
}
