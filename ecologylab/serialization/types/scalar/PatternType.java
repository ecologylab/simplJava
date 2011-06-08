/**
 * 
 */
package ecologylab.serialization.types.scalar;

import java.util.regex.Pattern;

import ecologylab.serialization.ScalarUnmarshallingContext;

/**
 * @author andruid
 *
 */
public class PatternType extends ReferenceType<Pattern>
{

	public PatternType()
	{
		super(Pattern.class);
	}

	@Override
	public Pattern getInstance(String value, String[] formatStrings,
			ScalarUnmarshallingContext scalarUnmarshallingContext)
	{
		// TODO Auto-generated method stub
		return (formatStrings == null) ? Pattern.compile(value) : Pattern.compile(value, Integer.parseInt(formatStrings[0]));
	}

	@Override
	public String getCSharptType()
	{
		return "System.Text.RegularExpressions.Regex";
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
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getJavaType()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
