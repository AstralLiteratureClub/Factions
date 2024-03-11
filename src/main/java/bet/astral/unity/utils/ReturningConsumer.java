package bet.astral.unity.utils;

@FunctionalInterface
public interface ReturningConsumer<T> {
	T accept(T value);
}
