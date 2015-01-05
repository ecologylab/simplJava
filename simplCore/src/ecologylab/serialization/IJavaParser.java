/**
 * 
 */
package ecologylab.serialization;

import java.lang.reflect.Field;

/**
 * Interface for modularizing access to a Java Parser.
 * 
 * @author andruid
 */
public interface IJavaParser
{
	
	public String getJavaDocComment(Class thatClass);
	
	public String getJavaDocComment(Field field);
	
}
