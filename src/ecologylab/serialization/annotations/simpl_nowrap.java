package ecologylab.serialization.annotations;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Used to specify that the elements of a collection or map should not be wrapped by an outer tag
 * corresponding to their field name.
 * 
 * @author andruid
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface simpl_nowrap
{
}