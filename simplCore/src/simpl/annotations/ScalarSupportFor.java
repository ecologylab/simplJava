package simpl.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * Represents the classes that this given ScalarType implementation intends to support.
 * Used for the relevant mappings in the TypeRegistry and also in the scalar type logic
 * to make sure that unsupported types are not meddled with.
 *
 */
public @interface ScalarSupportFor {
	public Class<?>[] value();
}
