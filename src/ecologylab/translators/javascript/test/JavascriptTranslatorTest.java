package ecologylab.translators.javascript.test;

import java.io.File;
import java.io.IOException;

import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;
import ecologylab.translators.javascript.JavascriptTranslator;

public class JavascriptTranslatorTest {
	public static void main(String args[]) throws IOException, SIMPLTranslationException
	{
		System.out.println("Javascript Translator");
		TranslationScope ts = new TranslationScope();
		ts = TranslationScope.get("somegame",Player.class, Bank.class,Computer.class,Human.class,Item.class,Move.class, Movements.class,Player.class, ReferToSelf.class);

		JavascriptTranslator jst = new JavascriptTranslator();
		jst.translateToJavascript(new File("jscode/gamething.js"), ts);

		/* test graph serialization 
		ReferToSelf self1 = new ReferToSelf();
		ReferToSelf self2 = new ReferToSelf(1);
		
		System.out.println("var self1 = '"+self1.serialize(FORMAT.JSON).toString()+"';");
		System.out.println("var self1 = '"+self2.serialize(FORMAT.JSON).toString()+"';");
	   */
		
	}
}
