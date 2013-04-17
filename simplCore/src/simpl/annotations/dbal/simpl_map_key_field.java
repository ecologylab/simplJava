package simpl.annotations.dbal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicate name of the field used as key in a map. Used together with {@code @simpl_map}.
 * 
 * @author quyin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface simpl_map_key_field
{
	String value();
}