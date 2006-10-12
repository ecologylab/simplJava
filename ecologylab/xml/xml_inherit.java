package ecologylab.xml;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)

/**
 * Marker annotation for scalar-valued fields that should be translated to XML
 * as leaf nodes, instead of as attributes.
 * 
 * @author andruid
 */

public @interface xml_inherit
{

}
