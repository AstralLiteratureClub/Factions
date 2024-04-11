package bet.astral.unity.model;

import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.messenger.placeholder.Placeholderable;
import bet.astral.messenger.utils.PlaceholderUtils;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Getter
@Setter
public class FRole implements Placeholderable, ComponentLike {
	public static final FRole OWNER = new FRole("owner", 10, FRole.CO_OWNER, null) {
		@Override
		public boolean hasPermission(FPermission permission) {
			return true;
		}};
	public static final FRole CO_OWNER = new FRole("co_owner", 8, FRole.ADMIN, null);
	public static final FRole ADMIN = new FRole("admin", 6, FRole.MODERATOR, FRole.CO_OWNER,
			FPermission.INVITE, FPermission.INVITES, FPermission.CANCEL_INVITE
	);
	public static final FRole MODERATOR = new FRole("moderator", 4, FRole.MEMBER, FRole.ADMIN);
	public static final FRole MEMBER = new FRole("default", 2, FRole.GUEST, FRole.MODERATOR);
	public static final FRole GUEST = new FRole("guest", 0, null, FRole.MEMBER);
	private static final FRole[] roles = new FRole[]{
			OWNER,
			CO_OWNER,
			ADMIN,
			MODERATOR,
			MEMBER,
			GUEST
	};
	public static FRole[] values() {
		return Arrays.copyOf(roles, roles.length);
	}
	@Nullable
	public static FRole valueOf(@NotNull String name) {
		return Stream.of(values()).filter(role->role.getName().equalsIgnoreCase(name)).findAny().orElse(null);
	}




	private final String name;
	private final int priority;
	@Nullable
	private final FRole before;
	@Nullable
	private final FRole after;
	private Map<FPermission, Boolean> permissions = new HashMap<>();

	public FRole(@NotNull String name, int priority, @Nullable FRole before, @Nullable FRole after, FPermission... permissions) {
		this.name = name;
		this.priority = priority;
		this.before = before;
		this.after = after;
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
		placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "name", name));
		placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "priority", priority));
		if (before != null) {
			placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "before", before.name));
		}
		if (after != null) {
			placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "after", after.name));
		}
		placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "permissions", permissions.size()));
		return placeholders;
	}

	public int priority() {
		return priority;
	}
	public boolean isHigherThan(FRole role){
		return this.priority > role.priority;
	}

	@Override
	public @NotNull Component asComponent() {
		return Component.text(name);
	}
}
