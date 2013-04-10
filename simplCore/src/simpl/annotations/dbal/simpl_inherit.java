package simpl.annotations.dbal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Metalanguage declaration for classes with metalanguage inside, 
 * which are derived from other classes that also have metalanguage that needs interpreting.
 * 
 * @author andruid
 */

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)

public @interface simpl_inherit
{

}
