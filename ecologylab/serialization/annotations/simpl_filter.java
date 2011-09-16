package ecologylab.serialization.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * S.IM.PL declaration for scalar fields.
 * <p/>
 * Specifies filtering a scalar value on input, using a regex, before marshalling by a ScalarType.
 * Only activated when you call on your TranslationScope instance, setPerformFilters(), before
 * calling deserialize(Stream).
 * 
 * @author andruid
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface simpl_filter
{
	String regex();

	String replace() default "";
}