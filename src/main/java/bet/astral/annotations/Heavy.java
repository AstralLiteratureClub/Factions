package bet.astral.annotations;

import java.lang.annotation.Documented;

/**
 * A class or method is heavy for the server or the platform.
 * Methods or classes with this annotation should not be used that often.
 */
@Documented
public @interface Heavy {
}
