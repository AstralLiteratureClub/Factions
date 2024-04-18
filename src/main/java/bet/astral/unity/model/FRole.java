package bet.astral.unity.model;

import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.messenger.placeholder.Placeholderable;
import bet.astral.messenger.utils.PlaceholderUtils;
import bet.astral.unity.utils.UniqueId;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

@Getter
@Setter
public class FRole implements Placeholderable, ComponentLike, UniqueId {
	public static final FRole OWNER = new FRole("owner", UUID.fromString("2c5a2c8d-5dee-40fc-95e0-ed1cb5ebd2b6"), 10, FRole.CO_OWNER, null) {
		@Override
		public boolean hasPermission(FPermission permission) {
			return true;
		}};
	public static final FRole CO_OWNER = new FRole("co_owner", UUID.fromString("d8667856-9566-4284-b537-5d18d6591975"), 8, FRole.ADMIN, null);
	public static final FRole ADMIN = new FRole("admin", UUID.fromString("f463ef98-5885-403f-8a01-e6dd6786ff94"), 6, FRole.MODERATOR, FRole.CO_OWNER,
			FPermission.INVITE, FPermission.INVITES, FPermission.CANCEL_INVITE
	);
	public static final FRole MODERATOR = new FRole("moderator", UUID.fromString("9156227b-c4bd-4f07-9923-30038e58ca0e"), 4, FRole.MEMBER, FRole.ADMIN);
	public static final FRole MEMBER = new FRole("default", UUID.fromString("fab81c4c-c13d-48c6-b79c-31ba8e6c6a5d"), 2, FRole.GUEST, FRole.MODERATOR);
	public static final FRole GUEST = new FRole("guest", UUID.fromString("7830a078-48d8-4052-92c7-8e66003085e4"), 0, null, FRole.MEMBER);
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

	@Nullable
	public static FRole valueOf(@NotNull UUID id) {
		return Stream.of(values()).filter(role->role.getUniqueId().equals(id)).findAny().orElse(null);
	}




	private final String name;
	private final UUID uniqueId;
	private final int priority;
	@Nullable
	private final FRole before;
	@Nullable
	private final FRole after;
	private Map<FPermission, Boolean> permissions = new HashMap<>();

	public FRole(@NotNull String name, UUID uniqueId, int priority, @Nullable FRole before, @Nullable FRole after, FPermission... permissions) {
		this.name = name;
		this.uniqueId = uniqueId;
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
