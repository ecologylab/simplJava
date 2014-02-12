/*
 * Created on Jan 2, 2005 at the Interface Ecology Lab.
 */
package ecologylab.serialization.types.scalar;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.json.simple.JSONObject;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.ScalarUnmarshallingContext;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.XMLTools;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.formatenums.Format;
import ecologylab.serialization.types.CrossLanguageTypeConstants;

/**
 * Type system entry for {@link java.util.Calendar Calendar}.
 * 
 * @author Zachary O. Toups (ztoups@nmsu.edu)
 */
@simpl_inherit
public class GregorianCalendarType extends ReferenceType<GregorianCalendar> implements
		CrossLanguageTypeConstants
{
	static final String			datePatterns[]	=
																					{
																					"EEE MMM dd kk:mm:ss zzz yyyy",
																					"yyyy:MM:dd HH:mm:ss",
																					"yyyy:MM:dd HH:mm",
																					"yyyy-MM-dd HH:mm:ss",
																					"yyyy-MM-dd HH:mm",
																					"MMM dd, yyyy",
																					"yyyyMMdd",
																					"MM/dd/yyyy",
																					"MM/dd/yyyy K:mm aa",
																					};

	static final DateFormat	dateFormats[]		= new DateFormat[datePatterns.length + 1];

	static final DateFormat	plainDf					= DateFormat.getDateTimeInstance();

	static
	{
		for (int i = 0; i < datePatterns.length; i++)
			dateFormats[i] = new SimpleDateFormat(datePatterns[i]);
		dateFormats[datePatterns.length] = plainDf;
	}

	public GregorianCalendarType()
	{
		super(GregorianCalendar.class, JAVA_CALENDAR, null, null, null);
	}

	/**
	 * Uses the format specified by the first entry in simpl_format; if that fails, cycles through
	 * additional patterns specified by datePatterns[] until one succeeds.
	 * 
	 * @param value
	 * @see ecologylab.serialization.types.ScalarType#getInstance(java.lang.String, String[],
	 *      ScalarUnmarshallingContext)
	 */
	@Override
	public GregorianCalendar getInstance(String value, String[] formatStrings,
			ScalarUnmarshallingContext scalarUnmarshallingContext)
	{
		if (formatStrings[0] != null && !"".equals(formatStrings[0]))
			try
			{
				DateFormat df = new SimpleDateFormat(formatStrings[0]);
				Date d = df.parse(value);
				GregorianCalendar c = (GregorianCalendar) Calendar.getInstance();
				c.setTimeInMillis(d.getTime());

				return c;
			}
			catch (ParseException e)
			{ // simply try all the patterns
			}

		for (DateFormat dateFormatParser : dateFormats)
		{
			try
			{
				Date d = dateFormatParser.parse(value);
				GregorianCalendar c = (GregorianCalendar) Calendar.getInstance();
				c.setTimeInMillis(d.getTime());

				return c;
			}
			catch (java.text.ParseException ex)
			{ // simply try the next pattern
			}
		}

		error("Failed to parse date: " + value);
		return null;
	}

	@Override
	public void appendValue(StringBuilder buffy, FieldDescriptor fieldDescriptor, Object context)
			throws IllegalArgumentException, IllegalAccessException
	{
		try
		{
			GregorianCalendar instance = (GregorianCalendar) fieldDescriptor.getField().get(context);

			appendValue(instance, fieldDescriptor.getFormat(), buffy, !fieldDescriptor.isCDATA(), null);
		}
		catch (IllegalArgumentException e)
		{
			throw e;
		}
	}

	/**
	 * Append the String directly, unless it needs escaping, in which case, call escapeXML.
	 * 
	 * @param instance
	 * @param buffy
	 * @param needsEscaping
	 */
	public void appendValue(
			GregorianCalendar instance,
			String[] formatDescriptors,
			StringBuilder buffy,
			boolean needsEscaping,
			TranslationContext serializationContext)
	{
		String instanceString = marshall(instance, formatDescriptors, serializationContext);// instance.toString();
		if (needsEscaping)
			XMLTools.escapeXML(buffy, instanceString);
		else
			buffy.append(instanceString);
	}

	@Override
	public void appendValue(GregorianCalendar instance, StringBuilder buffy, boolean needsEscaping,
			TranslationContext serializationContext)
	{
		ClassDescriptor compositeElement = ClassDescriptor.getClassDescriptor(instance);
		FieldDescriptor scalarValueFD = compositeElement.getScalarValueFieldDescripotor();
		String instanceString = marshall(instance, scalarValueFD.getFormat(), serializationContext);// instance.toString();
		if (needsEscaping)
			XMLTools.escapeXML(buffy, instanceString);
		else
			buffy.append(instanceString);
	}

	@Override
	public void appendValue(GregorianCalendar instance, Appendable buffy, boolean needsEscaping,
			TranslationContext serializationContext, Format format) throws IOException
	{
		String instanceString = "";
		if (instance != null && serializationContext != null)
		{
			ClassDescriptor compositeElement = ClassDescriptor.getClassDescriptor(instance);
			FieldDescriptor scalarValueFD = compositeElement.getScalarValueFieldDescripotor();

			if (scalarValueFD != null)
				instanceString = marshall(instance, scalarValueFD.getFormat(), serializationContext); // andruid
																																															// 1/4/10
			else
				instanceString = marshall(instance, new String[0], serializationContext);
		}
		// instance.toString();
		if (needsEscaping)
		{
			switch (format)
			{
				case JSON:
					buffy.append(JSONObject.escape(instanceString));
					break;
				case XML:
					XMLTools.escapeXML(buffy, instanceString);
					break;
				default:
					XMLTools.escapeXML(buffy, instanceString);
					break;
			}

		}
		else
			buffy.append(instanceString);
	}

	/**
	 * Get a String representation of the instance, using this. The default just calls the toString()
	 * method on the instance.
	 * 
	 * @param instance
	 * @param serializationContext
	 *          TODO
	 * @return
	 */
	public String marshall(GregorianCalendar instance, String[] formatDescriptors,
			TranslationContext serializationContext)
	{
		String pattern = null;
		if (formatDescriptors.length > 0 && formatDescriptors[0] != null)
			pattern = formatDescriptors[0];
		else
			pattern = datePatterns[0];

		DateFormat df = new SimpleDateFormat(pattern);
		Date d = instance.getTime();
		return df.format(d);
	}
}
