package bet.astral.annotations;

import java.lang.annotation.Documented;

/**
 * Tells the common name for classes.
 * This is only to let developers know of classes which might not make sense, in their opinion
 */
@Documented
public @interface CommonName {
	String[] value();
}
