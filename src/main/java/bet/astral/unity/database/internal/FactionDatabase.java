package bet.astral.unity.database.internal;

public interface FactionDatabase<K, V, C, K2, V2, C2> extends Database<K, V, C> {
	MemberDatabase<K2, V2, C2> getMemberDatabase();
}
