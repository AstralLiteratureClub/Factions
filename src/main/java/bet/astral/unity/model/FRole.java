package bet.astral.unity.model;

import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.messenger.placeholder.Placeholderable;
import bet.astral.messenger.utils.PlaceholderUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class FRole implements Placeholderable {
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
	private static final FRole[] roles = new FRole[]{
			OWNER,
			ADMIN,
			MODERATOR,
			DEFAULT
	};
	public static FRole[] values() {
		return Arrays.copyOf(roles, roles.length);
	}




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

	@Override
	public Collection<Placeholder> asPlaceholder(String prefix) {
		PlaceholderList placeholders = new PlaceholderList();
		placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "name", name));
		placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "permissions", permissions.size()));
		return placeholders;
	}
}
