package ecologylab.translators.javascript.test;

import java.io.File;
import java.io.IOException;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.XMLTools;
import ecologylab.translators.cocoa.CocoaTranslationException;
import ecologylab.translators.cocoa.CocoaTranslator;
import ecologylab.translators.java.JavaTranslator;
import ecologylab.translators.javascript.JavascriptTranslator;
import ecologylab.translators.net.DotNetTranslationException;
import ecologylab.translators.net.DotNetTranslator;

public class JavascriptTranslatorTest {

	public static void main(String args[]) throws IOException, SIMPLTranslationException, DotNetTranslationException, CocoaTranslationException
	{
		System.out.println("Javascript Translator");
		
		//c.tra
		//xmltools.getc get xmltag name
		
		TranslationScope ts = new TranslationScope();
		ts.addTranslation(Player.class);
		
		ts = TranslationScope.get("somegame",Bank.class,Computer.class,Human.class,Item.class,Move.class, Movements.class,Player.class);
		
		System.out.println(ts.getClassByName("player"));
		DotNetTranslator c = new DotNetTranslator();
		c.translateToCSharp(new File("C:/testjs/cs"),ts);
		CocoaTranslator coco = new CocoaTranslator();
		
		JavascriptTranslator jst = new JavascriptTranslator();
		jst.translateToJavascript(new File("C:/testjs/js"), ts);

		//not sure why this is failing
		//coco.translateToObjC(new File("C:/testjs/coco"), ts);
		
		
	}
}
