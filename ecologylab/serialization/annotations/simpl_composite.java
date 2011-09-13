package ecologylab.serialization.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Metalanguage declaration that tells ecologylab.serialization translators that each Field it is
 * applied to as an annotation is represented in XML by a (non-leaf) nested child element. The
 * field must be a subclass of ElementState.
 * <p/>
 * The nested child element name will be derived from the field name, using camel case conversion,
 * unless @simpl_tag is used.
 * 
 * @author andruid
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface simpl_composite
{
	String value() default "";
}
