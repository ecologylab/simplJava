/**
 * 
 */
package ecologylab.serialization.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used for excluding a field from certain usages. The usage of a field may affect various aspects of how it will be
 * used, e.g. code generation, serialization, persistence, etc. 
 * <p>
 * <b>Note: currently, only excluding for SERIALIZATION_IN_STREAM is implemented.</b>
 * 
 * @author quyin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface simpl_exclude_usage
{
	
	FieldUsage[] value() default { };
	
}
