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
		}
	};
	public static final FRole DEFAULT = new FRole("default");


	private final String name;
	private Map<FPermission, Boolean> permissions = new HashMap<>();

	public FRole(String name) {
		this.name = name;
	}

	public boolean hasPermission(FPermission permission){
		return permissions.get(permission) != null && permissions.get(permission);
	}
}
