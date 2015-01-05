package ecologylab.serialization.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Supplementary metalanguage declaration that can be applied only to a field. This is used for
 * assigning database constraints to a field, which are referenced in creating correponding sql
 * table schema. Database constraints are defined in 'DbHint'
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface simpl_db
{
	/**
	 * @return database constraints defined in 'DbHint' and name of reference table
	 */
	DbHint[] value();

	String references() default "null";
}
