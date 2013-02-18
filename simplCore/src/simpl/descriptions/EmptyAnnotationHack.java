package simpl.descriptions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An annotation is an awkward proxy object in Java. Attribute values are methods() that return the value. 
 * This is a cool property for consumers, allows for some other stuff that's great, but doesn't make 
 * any sort of reflective understanding of the annotation easy.
 * 
 * If I have an empty instance of an annotation, I have some methods that the proxying system defines.
 * I can take the symmetric difference between these methods and any other annotation's methods, and those will give 
 * me the methods that represent annotation values. Those can then be executed to get the value. 
 * 
 * This is a dirty dirty thing, but it works. 
 * @author twhite
 *	
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface EmptyAnnotationHack {
}
