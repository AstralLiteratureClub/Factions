package bet.astral.guiman;

/**
 * <a href="https://stackoverflow.com/questions/457629/how-to-return-multiple-objects-from-a-java-method">https://stackoverflow.com/questions/457629/how-to-return-multiple-objects-from-a-java-method</a>
 * @param a a param
 * @param b b param
 * @param <A> a param
 * @param <B> b param
 */
public record Pair<A, B>(A a, B b) {

	public static <P, Q> Pair<P, Q> makePair(P p, Q q) {
		return new Pair<>(p, q);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		@SuppressWarnings("rawtypes")
		Pair other = (Pair) obj;
		if (a == null) {
			if (other.a != null) {
				return false;
			}
		} else if (!a.equals(other.a)) {
			return false;
		}
		if (b == null) {
			if (other.b != null) {
				return false;
			}
		} else if (!b.equals(other.b)) {
			return false;
		}
		return true;
	}

	public boolean isInstance(Class<?> classA, Class<?> classB) {
		return classA.isInstance(a) && classB.isInstance(b);
	}

	@SuppressWarnings("unchecked")
	public static <P, Q> Pair<P, Q> cast(Pair<?, ?> pair, Class<P> pClass, Class<Q> qClass) {

		if (pair.isInstance(pClass, qClass)) {
			return (Pair<P, Q>) pair;
		}

		throw new ClassCastException();

	}

}