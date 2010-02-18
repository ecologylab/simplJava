//Translator.java

package ecologylab.tutorials.rss;

import java.io.*;

import ecologylab.xml.TranslationScope;
import ecologylab.xml.internaltranslators.cocoa.*;
import ecologylab.tutorials.rss.*;
import ecologylab.net.ParsedURL;


public class Translator {

	public static void main(String[] args) {

		CocoaTranslator c = new CocoaTranslator();
		TranslationScope tScope	= RssTranslations.get();
		try 
		{
			c.translateToObjC(new ParsedURL(new File ("/output")), RssState.class, Channel.class, Item.class);
			tScope.translateToXML(System.out);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
