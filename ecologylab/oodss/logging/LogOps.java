package ecologylab.oodss.logging;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.StringFormat;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.annotations.simpl_inherit;

/**
 * Bundle a sequence of {@link MixedInitiativeOp MixedInitiativeOp}s, and send them to the logging
 * server.
 * 
 * @author eunyee
 * @author andruid
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
@simpl_inherit
public final class LogOps extends LogEvent
{
	/** Constructor for XML translation. */
	public LogOps()
	{
		super();
	}

	public LogOps(int bufferSize)
	{
		super(bufferSize);
	}

	static final Class[]					CLASSES	=
																				{ LogOps.class, LogEvent.class };

	static final TranslationScope	TS			= TranslationScope.get("lo", CLASSES);

	public static void main(String[] a)
	{
		LogOps l = new LogOps();

		l.bufferToLog = new StringBuilder(
				"<cf_text_token delims_before=\" \" string=\"&quot;an\"/><cf_text_token delims_before=\" \" string=\"unprepared\"/>");

		try
		{
			ClassDescriptor.serialize(l, System.out, StringFormat.XML);

			StringBuilder buffy = ClassDescriptor.serialize(l, StringFormat.XML);
			System.out.println("");
			Object l2 = TS.deserialize(buffy.toString(), StringFormat.XML);

			ClassDescriptor.serialize(l2, System.out, StringFormat.XML);

		}
		catch (SIMPLTranslationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}