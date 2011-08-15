package ecologylab.serialization.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import ecologylab.serialization.ElementState;

/**
 * Supplementary metalanguage declaration that can be applied only to a field. The argument is an
 * array of Class objects.
 * <p/>
 * Annotation forms tag names from each of the class names, using camel case conversion. It then
 * creates a mapping from the tag and class names to the field it is applied to, so that
 * translateFromXML(...) will set a value based on an element with the tags, if field is also
 * declared with @xml_nested, or collect values when elements have the tags, if the field is
 * declared with @xml_collection.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface simpl_classes
{
	Class<? extends ElementState>[] value();
}