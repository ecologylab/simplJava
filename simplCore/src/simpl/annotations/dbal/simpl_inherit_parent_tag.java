package simpl.annotations.dbal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;




/**
 * Makes the tag name of a given class equal to its parent class's tag name.
 * This facilitates easy dummy-ing and replacement of classes. 
 * @author twhite
 */

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface simpl_inherit_parent_tag {
}
