package ecologylab.xml;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation for scalar-valued fields that should be translated to XML
 * as leaf nodes, instead of as attributes.
 * 
 * @author andruid
 */

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)

public @interface xml_inherit
{

}
