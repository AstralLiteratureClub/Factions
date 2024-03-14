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
	public static final FRole OWNER = new FRole("owner", 6) {
		@Override
		public boolean hasPermission(FPermission permission) {
			return true;
		}};
	public static final FRole ADMIN = new FRole("admin", 4,
			FPermission.INVITE, FPermission.INVITES, FPermission.CANCEL_INVITE
	);
	public static final FRole MODERATOR = new FRole("moderator", 2

	);
	public static final FRole DEFAULT = new FRole("default", 0);
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
	private final int priority;
	private Map<FPermission, Boolean> permissions = new HashMap<>();

	public FRole(String name, int priority, FPermission... permissions) {
		this.name = name;
		this.priority = priority;
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
		placeholders.add(PlaceholderUtils.createPlaceholder(null, prefix, name));
		placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "permissions", permissions.size()));
		return placeholders;
	}

	public int priority() {
		return priority;
	}
	public boolean isHigherThan(FRole role){
		return this.priority > role.priority;
	}
}
