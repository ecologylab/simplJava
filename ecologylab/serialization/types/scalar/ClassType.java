/**
 * 
 */
package ecologylab.serialization.types.scalar;

import java.lang.reflect.Field;

import ecologylab.serialization.ScalarUnmarshallingContext;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.TranslationScope;

/**
 * For marshalling Class objects themselves. Particularly useful in ports.
 * 
 * @author andruid
 */
public class ClassType extends ReferenceType<Class>
{
	public ClassType()
	{
		super(Class.class);
	}

	/**
	 * Create a class object, using the value as a fully qualified class name.
	 * Use the TranslationScope name passed in as the first formatString argument, if there is one, as the source of the Class.
	 * Otherwise, use Class.forName().
	 */
	@Override
	public Class getInstance(String value, String[] formatStrings,
			ScalarUnmarshallingContext scalarUnmarshallingContext)
	{
		Class result	= null;
		
		if (formatStrings != null)
		{
			String scopeName	= formatStrings[0];
			if (scopeName != null && scopeName.length() > 0)
			{
				TranslationScope tScope	= TranslationScope.get(scopeName);
				if (tScope != null)
					result		= tScope.getClassByName(value);
			}
		}
		if (result == null)
			try
			{
				result				= Class.forName(value);
			}
			catch (Throwable e)
			{
				warning(": Can't find class " + value + " so not translated");
			}
		return result;
	}

	/**
	 * The string representation for a Field of the type Class
	 */
	@Override
	public String marshall(Class instance, TranslationContext serializationContext)
	{
		return instance.getName();
	}

	@Override
	public String getCSharptType()
	{
		return MappingConstants.OBJC_CLASS;
	}

	@Override
	public String getDbType()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getObjectiveCType()
	{
		return MappingConstants.DOTNET_CLASS;
	}

}
