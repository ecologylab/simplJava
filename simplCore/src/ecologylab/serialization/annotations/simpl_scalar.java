package ecologylab.serialization.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Metalanguage declaration that tells simpl serialization that each Field it is applied to as an
 * annotation is a scalar-value.
 * <p/>
 * The attribute name will be derived from the field name, using camel case conversion, unless @simpl_tag
 * is used.
 * 
 * @author andruid
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface simpl_scalar
{
}