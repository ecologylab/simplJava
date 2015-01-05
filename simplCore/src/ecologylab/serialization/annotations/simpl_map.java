package ecologylab.serialization.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Metalanguage declaration that tells ecologylab.serialization translators that each Field it is
 * applied to as an annotation is of type Map. An argument may be passed to declare the tag name
 * of the child elements. The XML may define any number of child elements with this tag. In this
 * case, the class of the elements will be dervied from the instantiated generic type declaration
 * of the children.
 * <p/>
 * For example, <code>@xml_map("foo")    HashMap&lt;String, FooFoo&gt;	items;</code><br/>
 * The values of the Map must implement the Mappable interface, to supply a key which matches the
 * key declaration in the Map's instantiated generic types.
 * <p/>
 * Without the tag name declaration, the tag name will be derived from the class name of the
 * children, and in translate from XML, the class name will be derived from the tag name, and then
 * resolved in the TranslationSpace.
 * 
 * @author andruid
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface simpl_map
{
	String value() default "";
}
