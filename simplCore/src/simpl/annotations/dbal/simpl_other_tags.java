package simpl.annotations.dbal;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This optional metalanguage declaration is used to add extra tags to a field or class, in order
 * to enable backwards compatability with a previous dialect of XML. It affects only translate
 * from XML; translateToXML() never uses these entries.
 * 
 * @author andruid
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface simpl_other_tags
{
	String[] value();
}
