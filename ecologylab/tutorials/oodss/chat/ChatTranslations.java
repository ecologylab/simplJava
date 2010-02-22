package ecologylab.tutorials.oodss.chat;

import ecologylab.services.messages.DefaultServicesTranslations;
import ecologylab.xml.TranslationScope;

public class ChatTranslations
{
	public final static String	TRANSLATION_SPACE_NAME	= "ChatTranslations";

	public static TranslationScope get()
	{
		return TranslationScope.get(TRANSLATION_SPACE_NAME,
				DefaultServicesTranslations.get(), ChatRequest.class,
				ChatUpdate.class);
	}
}
