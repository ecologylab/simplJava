package ecologylab.serialization.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * S.IM.PL declaration for hints that precisely define the syntactic structure of serialization.
 * 
 * @author andruid
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface simpl_hints
{
	Hint[] value() default
	{ Hint.XML_ATTRIBUTE };
}
