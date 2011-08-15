package ecologylab.serialization.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * annotation to define the type of a bibtex entry.
 * 
 * @author nabeel
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface bibtex_type
{
	String value();
}
