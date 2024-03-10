package bet.astral.unity.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class FRole {
	public static final FRole OWNER = new FRole("owner") {
		@Override
		public boolean hasPermission(FPermission permission) {
			return true;
		}};
	public static final FRole ADMIN = new FRole("admin",
			FPermission.INVITE, FPermission.INVITES, FPermission.CANCEL_INVITE
	);
	public static final FRole MODERATOR = new FRole("moderator"

	);
	public static final FRole DEFAULT = new FRole("default");


	private final String name;
	private Map<FPermission, Boolean> permissions = new HashMap<>();

	public FRole(String name, FPermission... permissions) {
		this.name = name;
		for (FPermission perm : permissions) {
			this.permissions.put(perm, true);
		}
	}

	public boolean hasPermission(FPermission permission){
		return permissions.get(permission) != null && permissions.get(permission);
	}
}
