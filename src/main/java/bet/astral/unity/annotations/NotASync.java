package bet.astral.unity.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method or constructor which is not asynchronous. Methods flagged with this might cause lag to the server.
 */
@Inherited
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.SOURCE)
@Documented
@Deprecated(forRemoval = true)
public @interface NotASync {
}
