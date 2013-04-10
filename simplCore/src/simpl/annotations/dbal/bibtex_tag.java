package simpl.annotations.dbal;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * annotation used for serializing in bibtex format. The tag value is the name of the key in
 * key-value pairs of a bibtex entry
 * 
 * @author nabeel
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface bibtex_tag
{
	String value();
}