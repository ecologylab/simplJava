package ecologylab.serialization.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Metalanguage declaration that can be applied either to field or to class declarations.
 * 
 * Annotation that tells ecologylab.serialization translators that instead of generating a name
 * for XML elements corresponding to the field or class using camel case conversion, one is
 * specified explicitly. This name is specified by the value of this annotation.
 * <p/>
 * Note that programmers should be careful when specifying an xml_tag, to ensure that there are no
 * collisions with other names. Note that when an xml_tag is specified for a field or class, it
 * will ALWAYS EMIT AND TRANSLATE FROM USING THAT NAME.
 * 
 * xml_tag's should typically be something that cannot be represented using camel case name
 * conversion, such as utilizing characters that are not normally allowed in field names, but that
 * are allowed in XML names. This can be particularly useful for building ElementState objects out
 * of XML from the wild.
 * <p/>
 * You cannot use XML-forbidden characters or constructs in an xml_tag!
 * 
 * When using @xml_tag, you MUST create your corresponding TranslationSpace entry using a Class
 * object, instead of using a default package name.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface simpl_tag
{
	String value();
}
