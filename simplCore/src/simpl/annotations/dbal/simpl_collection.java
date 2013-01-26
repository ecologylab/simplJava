
package simpl.annotations.dbal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Metalanguage declaration that tells ecologylab.serialization translators that each Field it is
 * applied to as an annotation is of type Collection. An argument may be passed to declare the tag
 * name of the child elements. The XML may define any number of child elements with this tag. In
 * this case, the class of the elements will be dervied from the instantiated generic type
 * declaration of the children. For example,
 * <code>@xml_collection("item")    ArrayList&lt;Item&gt;	items;</code>
 * <p/>
 * For that formulation, the type of the children may be a subclass of ElementState, for full
 * nested elements, or it may be a ScalarType, for leaf nodes.
 * <p/>
 * Without the tag name declaration, the tag name will be derived from the class name of the
 * children, and in translate from XML, the class name will be derived from the tag name, and then
 * resolved in the TranslationSpace.
 * <p/>
 * Alternatively, to achieve polymorphism, for children subclassed from ElementState only, this
 * declaration can be combined with @xml_classes. In such cases, items of the various classes will
 * be collected together in the declared Collection. Then, the tag names for these elements will
 * be derived from their class declarations.
 * 
 * @author andruid
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface simpl_collection
{
	String value() default "";
}