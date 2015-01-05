package ecologylab.serialization.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Optional metalanguage declaration. Enables specificaition of one or more formatting strings.
 * Only affects ScalarTyped Fields (ignored otherwise). The format string will be passed to the
 * ScalarType for type-specific interpretation.
 * <p/>
 * An example of use is to pass DateFormat info to the DateType.
 * 
 * @author andruid
 * @author toupsz
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface simpl_format
{
	String[] value();
}