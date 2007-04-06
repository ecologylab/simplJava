/*
 * Created on Jan 2, 2005 at the Interface Ecology Lab.
 */
package ecologylab.xml.types.scalar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Type system entry for {@link java.util.Date Date}.
 * 
 * @author toupsz
 */
public class DateType extends ScalarType<Date>
{
    static final DateFormat df      = new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy");

    static final DateFormat plainDf = DateFormat.getDateTimeInstance();

    public DateType()
    {
        super(Date.class);
    }

    /**
     * @param value
     *            is interpreted as a SimpleDateFormat in the form EEE MMM dd kk:mm:ss zzz yyyy (for
     *            example Wed Aug 02 13:12:50 CDT 2006); if that does not work, then attempts to use
     *            the DateFormat for the current locale instead.
     * 
     * @see ecologylab.xml.types.scalar.ScalarType#getInstance(java.lang.String)
     */
    public Date getInstance(String value)
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
