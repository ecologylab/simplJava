/*
 * Created on Jan 2, 2005 at the Interface Ecology Lab.
 */
package ecologylab.serialization.types.scalar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ecologylab.serialization.ScalarUnmarshallingContext;
import ecologylab.serialization.types.CrossLanguageTypeConstants;
import ecologylab.serialization.types.ScalarType;

/**
 * Type system entry for {@link java.util.Date Date}.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public class DateType extends ReferenceType<Date> implements CrossLanguageTypeConstants
{
	static final DateFormat	df			= new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy");

	static final DateFormat	plainDf	= DateFormat.getDateTimeInstance();

	public DateType()
	{
		super(Date.class, JAVA_DATE, DOTNET_DATE, OBJC_DATE, null);
	}

	/**
	 * @param value
	 *          is interpreted as a SimpleDateFormat in the form EEE MMM dd kk:mm:ss zzz yyyy (for
	 *          example Wed Aug 02 13:12:50 CDT 2006); if that does not work, then attempts to use the
	 *          DateFormat for the current locale instead.
	 * 
	 * @see ecologylab.serialization.types.ScalarType#getInstance(java.lang.String, String[],
	 *      ScalarUnmarshallingContext)
	 */
	public Date getInstance(String value, String[] formatStrings,
			ScalarUnmarshallingContext scalarUnmarshallingContext)
	{
		Date result = null;

		try
		{
			result = df.parse(value);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
			debug("exception caught, trying plainDf");

			try
			{
				result = plainDf.parse(value);
			}
			catch (ParseException e1)
			{
				debug("failed to parse date!");
				e1.printStackTrace();
			}
		}
		return result;
	}
}
