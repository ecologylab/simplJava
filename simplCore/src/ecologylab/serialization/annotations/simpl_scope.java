package ecologylab.serialization.annotations;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Supplementary metalanguage declaration that can be applied only to a field. The argument is the
 * name of a TranslationScope.
 * <p/>
 * Annotation uses the argument to lookup a TranslationScope. If there is none, a warning is
 * provided. Otherwise, mappings are created for tag names associated with each class in the
 * TranslationScope. It then creates a mapping from the tag and class names to the field it is
 * applied to, so that translateFromXML(...) will set a value based on an element with the tags,
 * if field is also declared with @xml_nested, or collect values when elements have the tags, if
 * the field is declared with @xml_collection.
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface simpl_scope
{
	String value();
}
